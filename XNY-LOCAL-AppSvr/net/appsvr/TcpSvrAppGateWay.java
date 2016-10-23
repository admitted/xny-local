package net.appsvr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import net.Md5;
import net.MsgHeadBean;
import net.TcpClient;
import net.TcpSvrBase;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import container.ActionContainer;
import bean.BaseCmdBean;
import util.*;

public class TcpSvrAppGateWay extends TcpSvrBase
{
	private int m_iPort     = 0;
	private int m_iTimeOut  = 0;
	private int m_iStatus   = 0;
	DBUtil      m_DbUtil    = null;
	TcpClient   m_TcpClient = null;
	
	/** 客户端列表 */
	private static Hashtable<String, ClientSocket> objClientTable = null;  // [ "0100000001" , ClientSocket ]
	private static Byte markClientTable = new Byte((byte)1); // 同步锁
	
	/**
	 * 读取配置文件内容
	 * @param dbUtil
	 * @throws Exception
	 */
	public TcpSvrAppGateWay(DBUtil dbUtil)throws Exception
	{
		SAXReader reader  = new SAXReader();
		Document document = reader.read(new FileInputStream("Config.xml"));
		Element root = document.getRootElement();
		m_iPort      = Integer.parseInt(root.element("app_server").element("server_prot").getText());
		m_iTimeOut   = Integer.parseInt(root.element("app_server").element("server_timeout").getText());
		m_iStatus    = Integer.parseInt(root.element("app_client").element("client_sta").getText());
		m_DbUtil     = dbUtil;
		
		if(1 == m_iStatus)
		{
			m_TcpClient = new TcpClient(m_DbUtil);
			m_TcpClient.init();
		}
	}
	
	//初始化
	public boolean Initialize()
	{
		if(!init(m_iPort, m_iTimeOut))
			return false;
		objClientTable  = new Hashtable<String, ClientSocket>();
		MsgCtrl msgCtrl = new MsgCtrl();
		msgCtrl.start();
		return true;
	}
	
	public String CheckClient(byte[] Buffer, Socket objClient)
	{
		String ret = null;
		try
		{
			DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(Buffer));
			DinStream.readInt();
			int Cmd = CommUtil.converseInt(DinStream.readInt()); // 1
//			int Cmd2 = CommUtil.converseInt(DinStream.readInt()); // 0
			if(Cmd_Sta.COMM_LOGON != Cmd) // 请求登陆  = 0x00000001;	
			{
				return null;
			}
			/*
			Send Original:
			
			5a 00 00 00   01 00 00 00   00 00 00 00   01 00 00 00   00 00 00 00  > 包头
			30 30 30 30   30 31 30 30   30 30 30 30   30 31 20 20   20 20 20 20  > 登入包
			20 20 20 20   32 30 31 36   2d 31 30 2d   31 33 20 31   30 3a 35 45
			39 38 44 36   31 41 43 39   44 37 42 31   46 30 45 37   39 35 43 43
			37 32 34 44   44 35 46 41   30 35 
			 */
			
			//登入验证
			String Status    = new String(Buffer, 20, 4);    // 业务执行状态 成功为 [0000]
			String PId       = new String(Buffer, 24, 20);   // CPM_ID = [0100000001          ] 
			String TimeStamp = new String(Buffer, 44, 14);   // [2016-10-13 10:]  ?????? 小错误
			String strMd5    = new String(Buffer, 58, 32);   // 
//			String checkResult = checkClient(Status, PId, TimeStamp, strMd5);
//			if(!checkResult.substring(0, 4).equalsIgnoreCase("0000"))
//			{
//				return null;
//			}
			ret = PId;
			
			//验证是否已存在  ,若存在就关闭
			if(objClientTable.containsKey(PId))
			{
				CommUtil.PRINT("Id Already Exist!" + PId);
				ClientClose(PId);
			}
			
			//新建通道
			ClientSocket objChannel= new ClientSocket();	
			if(!objChannel.init(objClient, PId))
			{
				CommUtil.LOG("ClientId [" + PId + "] ClientSocket init failed!");
			}
			
			synchronized(markClientTable)
			{
				objClientTable.put(PId , objChannel); // objChannel 是什么  [ "0100000001" , ClientSocket ]
			}
			
			//更新通道IP
			CommUtil.LOG("CPM_IP:" + objClient.getInetAddress().toString());
			String pSql = "update device_detail t set t.link_url = '"+ objClient.getInetAddress().toString().substring(1) +"' where t.id = '"+ PId.trim() +"'";
			m_DbUtil.doUpdate(pSql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return ret;
	}
	
	public String checkClient(String strStatus, String strId, String strTimestamp, String strOriginalMd5)
	{
		String ret = "3006";    // 系统状态  失败
		String password = m_DbUtil.APC(CommUtil.StrRightFillSpace(strId, 40)+ strStatus + "0001");
		String strData = strId + strTimestamp + password;
		String Temp = CommUtil.BytesToHexString(new Md5().encrypt(strData.getBytes()), 16);
		CommUtil.LOG("Client[" + strId + "] TimeStamp[" + strTimestamp + "] OldMd5[" + strOriginalMd5 + "] NewMd5[" + Temp + "] DbMsg[" + password + "]");
		
		if(Temp.equalsIgnoreCase(strOriginalMd5))
		{
			ret = "0000";
		}
		return ret;
	}

	/**
	 * 
	 *  (non-Javadoc)
	 * @see net.TcpSvrBase#GetActiveTestBuf()
	 */
	public byte[] GetActiveTestBuf()
	{
		byte[] byteData = null;
		try
		{
			ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
			DataOutputStream doutStream = new DataOutputStream(boutStream);
			doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN)); // 包头长度
			doutStream.writeInt(CommUtil.converseInt(CmdUtil.COMM_ACTIVE_TEST)); // 链接测试
			doutStream.writeInt(0); // 成功状态 0 
			doutStream.writeInt(CommUtil.converseInt(GetSeq())); // 序列号
			doutStream.writeInt(0); // 保留字段
			byteData = boutStream.toByteArray();
			doutStream.close();
			boutStream.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			byteData = null;
		}
		return byteData;
	}
	
	
	/**
	 * 状态通知  且  SetRecvMsgList
	 * [STATUS_CLIENT_ONLINE,STATUS_CLIENT_OFFLINE]
	 * 
	 * @see net.TcpSvrBase#ClientStatusNotify(java.lang.String, int)
	 */
	public void ClientStatusNotify(String strClientKey, int iStatus)
	{
		switch(iStatus)
		{
			case STATUS_CLIENT_ONLINE:
			{
				//CPM网关恢复在线
				String OffStr = "";
				OffStr = CommUtil.StrBRightFillSpace("", 20)
					   + "0000"
					   + "1004"
					   + CommUtil.StrBRightFillSpace("", 10)
					   + CommUtil.StrBRightFillSpace("", 30)
					   + CommUtil.StrBRightFillSpace("", 4)
					   + CommUtil.StrBRightFillSpace("", 20)
					   + "7"
					   + CommUtil.StrBRightFillSpace((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), 20)
					   + CommUtil.StrBRightFillSpace("网关恢复在线", 128);
	/*
	[0100000002          ] 
	[                    00001004                                                                72016-10-18 11:22:49 网关恢复在线                                                                                                                    ]
	*/
				SetRecvMsgList((strClientKey + new String(EnCode(Cmd_Sta.COMM_SUBMMIT, OffStr))).getBytes());
				break;
			}
			case STATUS_CLIENT_OFFLINE:
			{
				//CPM网关离线
				String OffStr = "";
				OffStr = CommUtil.StrBRightFillSpace("", 20)
					   + "0000"
					   + "1004"
					   + CommUtil.StrBRightFillSpace("", 10)
					   + CommUtil.StrBRightFillSpace("", 30)
					   + CommUtil.StrBRightFillSpace("", 4)
					   + CommUtil.StrBRightFillSpace("", 20)
					   + "6"
					   + CommUtil.StrBRightFillSpace((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), 20)
					   + CommUtil.StrBRightFillSpace("网关离线", 128);
				SetRecvMsgList((strClientKey + new String(EnCode(Cmd_Sta.COMM_SUBMMIT, OffStr))).getBytes());
				break;
			}
		}
	}
	
	public byte[] EnCode(int msgCode, String pData)
	{
		byte[] byteData = null;
		try
		{
			ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
			DataOutputStream doutStream = new DataOutputStream(boutStream);
			{
				doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN + pData.getBytes().length));//长度
				doutStream.writeInt(CommUtil.converseInt(msgCode));
				doutStream.writeInt(CommUtil.converseInt(0));
				doutStream.writeInt(CommUtil.converseInt(GetSeq()));
				doutStream.writeInt(CommUtil.converseInt(0));
				doutStream.write(pData.getBytes());
			}
			byteData = boutStream.toByteArray();
			boutStream.close();
			doutStream.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return byteData;
	}
	
	public static boolean DisPatch(int msgCode, String clientKey, String pData)
	{
		boolean ret = false;
		try
		{
			synchronized(markClientTable)
			{
				if(!objClientTable.isEmpty() && objClientTable.containsKey(clientKey))
				{
					CommUtil.LOG("Succee DisPatch Client[" + clientKey + "] Data[" + pData + "]");
					ClientSocket objChannel = (ClientSocket) objClientTable.get(clientKey);	
					objChannel.SendMsg(msgCode, pData);
					ret = true;
				}
				else
				{
					CommUtil.LOG("Failed DisPatch Client[" + clientKey + "] Data[" + pData + "]");
				}
			}
		}
		catch(Exception e)
		{
		}
		return ret;
	}
	
	/** 
	 * 如果收到关闭指令，就关闭SOCKET和释放资源
	 * @see net.TcpSvrBase#ClientClose(java.lang.String)
	 */
	public synchronized void ClientClose(String pClientKey)
	{
		try
		{			
			synchronized(markClientTable)
			{
				if(!objClientTable.isEmpty() && objClientTable.containsKey(pClientKey))
				{
					ClientSocket objChannel = (ClientSocket) objClientTable.get(pClientKey);
					if(null != objChannel.objSocket && !objChannel.objSocket.isClosed())
					{
						//关掉SOCKET连接
						objChannel.objSocket.close();
						objChannel.objSocket = null;
						ClientStatusNotify(pClientKey, STATUS_CLIENT_OFFLINE);
					}
					//在哈希表里移除客户端
					objClientTable.remove(pClientKey);			
				}
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
	/**
	 * 接收线程列表 数据处理 类
	 * @author CuiJing
	 *  
	 */
	private class MsgCtrl extends Thread
	{
		public void run()
		{
			String dealData = "";
			while (true)
			{
				try
				{
					byte[] data = (byte[])GetRecvMsgList();                    //取得接收线程数据列表
					if(null ==  data || data.length < Cmd_Sta.CONST_MSGHDRLEN) //Cmd_Sta.CONST_MSGHDRLEN: 包头长度
					{
						sleep(10); 
						continue;
					}
					String strClientKey = new String(data, 0, 20);   // 客户端key Cpm_Id [0100000001          ]
					DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(data));
					DinStream.skip(20);   
					MsgHeadBean msgHead = new MsgHeadBean();         //包头
					msgHead.setUnMsgLen(CommUtil.converseInt(DinStream.readInt())); // 通信包长度
					msgHead.setUnMsgCode(CommUtil.converseInt(DinStream.readInt()));// 业务类型
					msgHead.setUnStatus(CommUtil.converseInt(DinStream.readInt())); // 状态
					msgHead.setUnMsgSeq(CommUtil.converseInt(DinStream.readInt())); // 序列号
					msgHead.setUnReserve(CommUtil.converseInt(DinStream.readInt()));// 保留字段
					DinStream.close();
// Data    (290字节) = [0100000001          ][包头                             ] + dealData 
// dealData(250字节) = [                  95000010010431080001瑞烨流量计                    0026集合数据            2016-07-26 15:01:03 41EE64D2437B45B6409800000000400044220205420CEAC7                                                                                          ]
					dealData = new String(data, 40, data.length - 40);
					
					String dealReserve = dealData.substring(0, 20);        //保留字
					String dealCmd     = dealData.substring(24, 28);       //处理指令(1001)
					switch(msgHead.getUnMsgCode()) //根据业务类型处理
					{
						case Cmd_Sta.COMM_SUBMMIT: //客户端   数据上行
						{
							CommUtil.LOG("PlatForm Submit [" + strClientKey + "] " + "[" + dealData + "]");
							BaseCmdBean cmdBean = BaseCmdBean.getBean(Integer.parseInt(dealCmd), m_DbUtil);	
							if(null != cmdBean)
							{                      //Cpm_Id       数据包        
								cmdBean.parseReqest(strClientKey, dealData, data);
								cmdBean.execRequest();
								
								if(1 == m_iStatus)  //客户端上传???
								{
									//上传
									m_TcpClient.SetSendMsg(strClientKey + dealData, 1);
								}
							}
							break;
						}
						case Cmd_Sta.COMM_DELIVER: //服务器派发  下行
						{
							CommUtil.LOG("PlatForm Deliver [" + strClientKey + "] " + "[" + dealData + "]");
							BaseCmdBean cmdBean = ActionContainer.GetAction(dealReserve);
							if(null != cmdBean)
							{
								cmdBean.parseReponse(dealData);
								cmdBean.execResponse();
							}
							break;
						}
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					CommUtil.PRINT("TcpSvrAppGateWay Exception [" + dealData+"]");
					continue;
				}
			}//while
		}
	}
}//TcpSvrCls
