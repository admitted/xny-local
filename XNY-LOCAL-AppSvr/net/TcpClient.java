package net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;
import net.Md5;
import net.appsvr.CnoocStationBean;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import util.*;

public class TcpClient extends Thread
{
	private String				m_IP		= null;     //IP地址
	private int					m_Port		= 0;        //端口号
	private int					m_TimeOut	= 0;        //超时时间
	private int					m_TestSta	= 0;        
	private String				m_ID		= "";       
	private String				m_PWD		= "";
	private int					m_Seq		= 0;

	private Socket				objSocket	= null;
	private RecvThrd			objRecvThrd	= null;
	private Byte				markRecv	= new Byte((byte) 1);
	private LinkedList<Object>	recvMsgList	= null;
	private SendThrd			objSendThrd	= null;
	private Byte				markSend	= new Byte((byte) 1);
	private LinkedList<Object>	sendMsgList	= null;
	private DBUtil				m_DBUtil;

	public TcpClient(DBUtil dbUtil) throws Exception
	{
		this.m_DBUtil = dbUtil;
	}

	/**
	 * 初始化 Client: Config.xml
	 * @return
	 */
	public boolean init()
	{
		boolean RetVal = false;
		try
		{
			SAXReader reader = new SAXReader();
			Document document = reader.read(new FileInputStream("Config.xml"));
			Element root = document.getRootElement();// 取得根节点

			m_IP      = root.element("app_client").element("client_ip").getText();
			m_Port    = Integer.parseInt(root.element("app_client").element("client_prot").getText());
			m_TimeOut = Integer.parseInt(root.element("app_client").element("client_timeout").getText());
			m_ID      = root.element("app_client").element("client_id").getText();
			m_PWD     = root.element("app_client").element("client_pwd").getText();
			
			System.out.println("m_IP[" + m_IP + "] m_Port[" + m_Port + "]");

			if (null == recvMsgList) recvMsgList = new LinkedList<Object>();
			if (null == sendMsgList) sendMsgList = new LinkedList<Object>();

			if (Reconnect())
			{
				objRecvThrd = new RecvThrd(objSocket);
				objRecvThrd.start();
				objSendThrd = new SendThrd(objSocket);
				objSendThrd.start();
				RetVal = true;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		if (m_TimeOut != 0)
		{
			if (!this.isAlive())
			{
				this.start();
			}
		}
		return RetVal;
	}

	/**
	 * 重新连接
	 * @return
	 */
	private boolean Reconnect()
	{
		boolean RetVal = false;
		try
		{
			if (null != objSocket)
			{
				objSocket.close();
				objSocket = null;
			}

			objSocket = new Socket();
			objSocket.setSoTimeout(3000);
			objSocket.connect(new InetSocketAddress(m_IP, m_Port), 3000);

			if (null != objSocket)
			{
				RetVal = Login();
				if (RetVal)
				{
					System.out.println("Connect " + m_IP + " Success");
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println("objSocket new failed!!!");
		}
		return RetVal;
	}

	/**
	 * 登录
	 * @return
	 */
	private boolean Login()
	{
		boolean RetVal = false;
		try
		{
			String strToday   = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
			String strData    = CommUtil.StrBRightFillSpace(m_ID, 20) + strToday + m_PWD;
			byte[] md5_output = new Md5().encrypt(strData.getBytes());
			strData  = "0000" + CommUtil.StrBRightFillSpace(m_ID, 20) + strToday + CommUtil.BytesToHexString(md5_output, 16).toUpperCase();
			ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
			DataOutputStream doutStream = new DataOutputStream(boutStream);
			doutStream.writeInt(CommUtil.converseInt(70));
			doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.COMM_LOGON));// 连接
			doutStream.writeInt(CommUtil.converseInt(0));
			doutStream.writeInt(CommUtil.converseInt(GetSeq()));
			doutStream.writeInt(CommUtil.converseInt(0));
			doutStream.writeBytes(strData);
			byte[] byteData = boutStream.toByteArray();
			DataOutputStream SendChannel = null;
			SendChannel = new DataOutputStream(objSocket.getOutputStream());
			SendChannel.write(byteData);
			SendChannel.flush();
			DataInputStream RecvChannlLogin = new DataInputStream(objSocket.getInputStream());
			objSocket.setSoTimeout(20000);
			int iRecvLen = RecvChannlLogin.read(byteData);
			if (iRecvLen >= 20)
			{
				DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(byteData));
				DinStream.skipBytes(8);
				int unMsgStatus = DinStream.readInt();
				if (0 == unMsgStatus)
				{
					RetVal = true;
				}
				objSocket.setSoTimeout(0);
			}
		}
		catch (Exception exp)
		{
			System.out.println("Logon failed");
			exp.printStackTrace();
		}
		return RetVal;
	}

	public void run()
	{
		int testTime = (int) (new java.util.Date().getTime() / 1000);
		int nowTime = testTime;
		int dTime = 0;
		while (true)
		{
			try
			{
				sleep(1000);
				if (objSocket == null || objSocket.isClosed())
				{
					if (init())
					{
						System.out.println(m_IP + "Reconnect sucess.............\n");
					}
					else
					{
						if (null != objSocket)
						{
							objSocket.close();
							objSocket = null;
						}
						System.out.println(m_IP + "Reconnect Failed.............\n");
					}
					continue;
				}

				nowTime = (int) (new java.util.Date().getTime() / 1000);
				dTime = nowTime - testTime;
				int daylyTime = nowTime;
				if (dTime > m_TimeOut)
				{
					// 连接超时的一个处理
					m_TestSta++;
					if (m_TestSta > Cmd_Sta.COMM_ACTIVE_TEST)
					{
						m_TestSta = 0;
						objSocket.close();
						objSocket = null;
						System.out.println("Active Test...Close the socket");
					}
					else
					{
						// 如果正常的话，就发出测试包
						ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
						DataOutputStream doutStream = new DataOutputStream(boutStream);
						doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.CONST_MSGHDRLEN));
						doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.COMM_ACTIVE_TEST));
						doutStream.writeInt(0);
						doutStream.writeInt(CommUtil.converseInt(GetSeq()));
						doutStream.writeInt(CommUtil.converseInt(0));
						byte[] msg = boutStream.toByteArray();
						SetSendMsgList(msg);
						CommUtil.PRINT("Send Active Package to LNG!");
					}
					testTime = nowTime;
				}

				// 每天上传公司站点信息
				if (nowTime - daylyTime <= 0) continue;
				daylyTime += 86400;

				String sql = new CnoocStationBean().getSql(0);
				LinkedList sendList = this.m_DBUtil.getStationInfo(sql);
				for (int i = 0; i < sendList.size(); i++)
				{
					CnoocStationBean StationBean = (CnoocStationBean) sendList.get(i);
					SetSendMsg(CommUtil.StrBRightFillSpace(this.m_ID, 20) + CommUtil.StrBRightFillSpace("", 20) + "0000" + 4001 + CommUtil.StrBRightFillSpace(StationBean.getCPM_Id(), 10) + CommUtil.StrBRightFillSpace(StationBean.getCPM_Name(), 30) + CommUtil.StrBRightFillSpace(StationBean.getCPM_Brief(), 20) + CommUtil.StrBRightFillSpace(StationBean.getCPM_Status(), 1) + CommUtil.StrBRightFillSpace(StationBean.getCPM_OnOff(), 1) + CommUtil.StrBRightFillSpace(StationBean.getCPM_Type(), 4)
							+ CommUtil.StrBRightFillSpace(StationBean.getCPM_Time(), 10) + CommUtil.StrBRightFillSpace(StationBean.getLink_Url(), 20) + CommUtil.StrBRightFillSpace(StationBean.getLink_Port(), 6) + CommUtil.StrBRightFillSpace(StationBean.getLink_Id(), 20) + CommUtil.StrBRightFillSpace(StationBean.getLink_Pwd(), 6) + StationBean.getGis_Sign() + CommUtil.StrBRightFillSpace(StationBean.getLongitude(), 10) + CommUtil.StrBRightFillSpace(StationBean.getLatitude(), 10), 1);
				}
			}
			catch (SocketException exp)
			{
				try
				{
					exp.printStackTrace();
					if (null != objSocket)
					{
						objSocket.close();
						objSocket = null;
					}
				}
				catch (Exception e)
				{
				}
				continue;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				continue;
			}
		}
	}

	
	/**
	 * 生成序列号
	 * @return
	 */
	public int GetSeq()
	{
		if (m_Seq++ == 0xffffff) m_Seq = 0;
		return m_Seq;
	}

	/**
	 * 添加发送数据
	 * @param pData
	 * @param pType
	 * @return boolean
	 */
	public boolean SetSendMsg(String pData, int pType)
	{
		boolean ret = false;
		if (SetSendMsgList(EnCode(pData, pType)))
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * 获取  RecvMsgList : 一个 byte[]
	 * @return
	 */
	public byte[] GetRecvMsgList()
	{
		byte[] data = null;
		synchronized (markRecv)
		{
			if (!recvMsgList.isEmpty())
			{
				data = (byte[]) recvMsgList.removeFirst();
			}
		}
		return data;
	}

	/**
	 * 数据对象 添加到 LinkedList 中
	 * @param object
	 * @return boolean
	 */
	public boolean SetSendMsgList(Object object)
	{
		synchronized (markSend)
		{
			sendMsgList.addLast(object);
		}
		return true;
	}

	/**
	 * 从 LinkedList 中获取数据对象
	 * @return
	 */
	private byte[] getSendMsgList()
	{
		byte[] byteData = null;
		synchronized (markSend)
		{
			if (null != sendMsgList && !sendMsgList.isEmpty())
			{
				byteData = (byte[]) sendMsgList.removeFirst();
			}
		}
		return byteData;
	}

	/**
	 * 数据编码   返回一个已编码 的 byte[]
	 * @param pData
	 * @param pType
	 * @return byte[]
	 */
	public byte[] EnCode(String pData, int pType)
	{
		byte[] byteData = null;
		int msgLen = Cmd_Sta.CONST_MSGHDRLEN + pData.getBytes().length;
		ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
		DataOutputStream doutStream = new DataOutputStream(boutStream);
		try
		{
			doutStream.writeInt(CommUtil.converseInt(msgLen)); // msgLen    数据长度
			switch (pType)
			{
				case 1: //业务类型---客户端提交                                    // Cmd       业务类型
					doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.COMM_SUBMMIT)); 
					break;
				case 2: //业务类型---服务器派发
					doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.COMM_DELIVER)); 
					break;
				default:
					doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.COMM_SUBMMIT));
					break;
			}
			doutStream.writeInt(CommUtil.converseInt(0));       // Sta      状态
			doutStream.writeInt(CommUtil.converseInt(GetSeq()));// seq      序列号
			doutStream.writeInt(CommUtil.converseInt(0));       // reserve  保留字段
			doutStream.write(pData.getBytes());                 // 
			byteData = boutStream.toByteArray();
			boutStream.close();
			doutStream.close();
		}
		catch (Exception exp)
		{
			System.out.println("EnCode Exp:" + exp.getMessage());
			exp.printStackTrace();
		}
		return byteData;
	}

	/**
	 * 接收线程 内部类  
	 * @author cui
	 *
	 */
	private class RecvThrd extends Thread
	{
		private DataInputStream	RecvChannel	= null;

		public RecvThrd(Socket pSocket) throws Exception
		{
			RecvChannel = new DataInputStream(pSocket.getInputStream());
		}

		public void run()
		{
			byte[] data = null;
			Vector<Object> vectData = new Vector<Object>();
			int nRcvLen = 0;
			int nRcvPos = 0;
			int nCursor = 0;
			byte ctRslt = 0;
			boolean bContParse = true;
			byte[] cBuff = new byte[Cmd_Sta.CONST_MAX_BUFF_SIZE];
			while (true)
			{
				try
				{
					if (null == objSocket || objSocket.isClosed())
					{
						System.out.println("objSocket error");
						objSocket = null;
						break;
					}

					nRcvLen = RecvChannel.read(cBuff, nRcvPos, (Cmd_Sta.CONST_MAX_BUFF_SIZE - nRcvPos));
					if (nRcvLen <= 0)
					{
						objSocket.close();
						objSocket = null;
						System.out.println("LNG Server Closed the socket");
						break;
					}
					// CommUtil.printMsg(cBuff, nRcvLen+nRcvPos);
					m_TestSta = 0;
					nRcvPos += nRcvLen;
					nRcvLen = 0;
					nCursor = 0;
					int nLen = 0;
					bContParse = true;
					while (bContParse)
					{
						nLen = nRcvPos - nCursor;
						if (0 >= nLen)
						{
							break;
						}
						vectData.clear();
						vectData.insertElementAt(new Integer(nLen), 0);
						vectData.insertElementAt(new Integer(nCursor), 1);
						ctRslt = DeCode(cBuff, vectData);
						nLen = ((Integer) vectData.get(0)).intValue();
						switch (ctRslt)
						{
							case Cmd_Sta.CODEC_CMD:
								byte[] Resp = ((byte[]) vectData.get(1));
								if (null != Resp && Resp.length > 0)
								{
									SetSendMsgList(Resp);
								}
								data = (byte[]) vectData.get(2);
								if (null != data && data.length > Cmd_Sta.CONST_MSGHDRLEN)
								{
									//SetRecvMsgList(data);
									/*
									//接收
									m_DBUtil.TPC(new String(data, 20, 28) + new String(data, 136, data.length-136));
									//回应
									if(SetSendMsg(new String(data, 20, 28), 2))
									{
									}
									*/
								}
								nCursor += nLen;
								break;
							case Cmd_Sta.CODEC_RESP:
								nCursor += nLen;
								break;
							case Cmd_Sta.CODEC_NEED_DATA:
								bContParse = false;
								break;
							case Cmd_Sta.CODEC_ERR:
								nRcvPos = 0;
								bContParse = false;
								break;
							default:
								break;
						}
					}// bContParse
					if (0 != nRcvPos)
					{
						System.arraycopy(cBuff, nCursor, cBuff, 0, nRcvPos - nCursor);
						nRcvPos -= nCursor;
					}
				}
				catch (SocketException ex)
				{
					System.out.println("RevThread SocketException Exp:" + ex.getMessage());
					ex.printStackTrace();
					try
					{
						if (null != objSocket)
						{
							objSocket.close();
							objSocket = null;
						}
					}
					catch (Exception e)
					{

					}
					break;
				}
				catch (Exception exp)
				{
					exp.printStackTrace();
				}
			}// while

		}

		private byte DeCode(byte[] pMsg, Vector<Object> data)
		{
			byte RetVal = Cmd_Sta.CODEC_ERR;
			int nUsed = ((Integer) data.get(0)).intValue();  // 现有的数据长度
			int nCursor = ((Integer) data.get(1)).intValue();// 从什么地方开始
			try
			{
				DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(pMsg));
				if (nUsed < (int) Cmd_Sta.CONST_MSGHDRLEN)
				{
					return Cmd_Sta.CODEC_NEED_DATA;
				}
				DinStream.skip(nCursor);

				int unMsgLen = CommUtil.converseInt(DinStream.readInt());
				int unMsgCode = CommUtil.converseInt(DinStream.readInt());
				int unStatus = CommUtil.converseInt(DinStream.readInt());
				int unMsgSeq = CommUtil.converseInt(DinStream.readInt());
				int unReserve = CommUtil.converseInt(DinStream.readInt());

				if (unMsgLen < Cmd_Sta.CONST_MSGHDRLEN || unMsgLen > Cmd_Sta.CONST_MAX_BUFF_SIZE)
				{
					return Cmd_Sta.CODEC_ERR;
				}
				if (nUsed < unMsgLen)
				{
					return Cmd_Sta.CODEC_NEED_DATA;
				}

				data.insertElementAt(new Integer(unMsgLen), 0); // nUsed =
																// unMsgLen;

				if ((unMsgCode & Cmd_Sta.COMM_RESP) != 0)// 是应答包
				{
					RetVal = Cmd_Sta.CODEC_RESP;
					return RetVal;
				}

				ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
				DataOutputStream doutStream = new DataOutputStream(boutStream);
				// 置应答包
				doutStream.writeInt(CommUtil.converseInt(Cmd_Sta.CONST_MSGHDRLEN));
				doutStream.writeInt(CommUtil.converseInt(unMsgCode | Cmd_Sta.COMM_RESP));
				doutStream.writeInt(CommUtil.converseInt(unStatus));// Sta
				doutStream.writeInt(CommUtil.converseInt(unMsgSeq));// seq
				doutStream.writeInt(CommUtil.converseInt(unReserve));// seq
				byte hdrMsg[] = boutStream.toByteArray();
				data.insertElementAt(hdrMsg, 1);
				DinStream.close();
				boutStream.close();
				doutStream.close();

				switch (unMsgCode)
				{
					case Cmd_Sta.COMM_ACTIVE_TEST:
					{
						data.insertElementAt(null, 2);
						RetVal = Cmd_Sta.CODEC_CMD;
						break;
					}
					case Cmd_Sta.COMM_SUBMMIT:
					{
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
						DataOutputStream dout = new DataOutputStream(bout);
						dout.write(pMsg, nCursor, unMsgLen);
						data.insertElementAt(bout.toByteArray(), 2);
						dout.close();
						bout.close();
						RetVal = Cmd_Sta.CODEC_CMD;
						break;
					}
					case Cmd_Sta.COMM_DELIVER:
					{
						data.insertElementAt(null, 2);
						RetVal = Cmd_Sta.CODEC_CMD;
						break;
					}
					default:
					{
						RetVal = Cmd_Sta.CODEC_ERR;
						break;
					}
				}
			}
			catch (Exception exp)
			{
				exp.printStackTrace();
			}
			return RetVal;
		}
	}

	/**
	 * 发送线程
	 * 
	 */
	private class SendThrd extends Thread
	{
		private DataOutputStream	SendChannel	= null;

		public SendThrd(Socket pSocket) throws Exception
		{
			SendChannel = new DataOutputStream(pSocket.getOutputStream());
		}

		public void run()
		{
			while (true)
			{
				try
				{
					if ((objSocket == null) || objSocket.isClosed())
					{
						objSocket = null;
						break;
					}
					byte[] byteData = (byte[]) getSendMsgList();
					if (byteData == null)
					{
						sleep(10);
						continue;
					}
					// System.out.println("NM -> CPM:"+new String(byteData));
					SendChannel.write(byteData);
					SendChannel.flush();
				}
				catch (SocketException ex)
				{
					System.out.println("SendThread SocketException Exp:" + ex.getMessage());
					ex.printStackTrace();
					try
					{
						if (null != objSocket)
						{
							objSocket.close();
							objSocket = null;
						}
					}
					catch (Exception e)
					{

					}
					break;
				}
				catch (Exception ex)
				{
					System.out.println("SendThread Exception Exp:" + ex.getMessage());
					ex.printStackTrace();
				}
			}
		} // SendThrdRun
	}// SendThrd
}