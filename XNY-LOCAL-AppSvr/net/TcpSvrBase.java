package net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Vector;

import util.*;

public abstract class TcpSvrBase extends Thread
{
	public static final int  STATUS_CLIENT_ONLINE  = 0;
	public static final int  STATUS_CLIENT_OFFLINE = 1;
	public static final String TYPE_OPERATOR	   = "0";

	/** TCP服务器  */
	private ServerSocket objTcpSvrSock = null;
	
	/** 接收数据列表,用于客户端数据交换 */
	public LinkedList<Object> recvMsgList = null;
	
	/** 线程同步锁 */
	public Byte markRecv = new Byte((byte)1);      
	
	private int m_Seq      = 0;
	private int m_iPort    = 0;
	private int m_iTimeOut = 0;
	
	/**
	 * 读取配置文件内容 继承者实现
	 * @throws Exception
	 */
	public TcpSvrBase() throws Exception
	{
	}
	
	/**
	 * 初始化TCP服务器
	 * 监听 端口 60110 获得传上来的 Socket
	 * @param iPort
	 * @param iTimeOut
	 * @return boolean
	 */
	public boolean init(int iPort, int iTimeOut)
	{
		try
		{
			m_iPort       = iPort;
			m_iTimeOut    = iTimeOut;
			objTcpSvrSock = new ServerSocket(m_iPort);  
			if(null == objTcpSvrSock) 
			{
				return false;
			}	
			recvMsgList = new LinkedList<Object>();		
			this.start();                  
			return true;
		}
		catch (IOException ioExp)
		{
			ioExp.printStackTrace();
			return false;
		}		
	}	
	
	/**
	 * 监听 Socket 连接 [TcpSvrBase本身是一个线程]
	 * (循环调用accept()等待客户端连接) 多客户端通信
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{	
		while (true)
		{  
			try
			{
				Socket objClient = objTcpSvrSock.accept(); // 接收一个 相对客户端 socket 进行对接
				objClient.setSoTimeout(m_iTimeOut*1000);   // 通过指定超时值 启用/禁用 SO_TIMEOUT，以毫秒为单位。
														   // 超过时间  就断开 客户端
				DataInputStream RecvChannel = new DataInputStream(objClient.getInputStream());
				byte[] Buffer = new byte[1024];            // 创建缓冲区数组Buffer
				
				int RecvLen = RecvChannel.read(Buffer);    // 返回读取到的缓冲区Buffer 字节长度
				
				CommUtil.PRINT("Send Original:");          
				CommUtil.printMsg(Buffer, RecvLen);        
				/*
				Send Original:
				
				5a 00 00 00   01 00 00 00   00 00 00 00   01 00 00 00   00 00 00 00  > 包头
				30 30 30 30   30 31 30 30   30 30 30 30   30 31 20 20   20 20 20 20  > 登入包 
				20 20 20 20   32 30 31 36   2d 31 30 2d   31 33 20 31   30 3a 35 45
				39 38 44 36   31 41 43 39   44 37 42 31   46 30 45 37   39 35 43 43
				37 32 34 44   44 35 46 41   30 35 
				*/
				if(20 > RecvLen)
				{
					objClient.close();
					objClient = null;
					continue;
				}
				
				//登入验证
				String Pid = null;
				if(null == (Pid = CheckClient(Buffer, objClient))) // Pid = [0100000001          ]
					continue;
				
				//登入回复
				DataOutputStream SendChannel = new DataOutputStream(objClient.getOutputStream());
				SendChannel.write(new String(Buffer, 0, 44).getBytes());
				SendChannel.flush();
				objClient.setSoTimeout(0);
				ClientStatusNotify(Pid, STATUS_CLIENT_ONLINE); // 通知在线
		 		continue;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				continue;
			}
		}//while
	}
	
	/** 登入验证 子类实现此抽象方法   */
	protected abstract String CheckClient(byte[] buf, Socket objClient);
	
	/** 状态通知  */
	protected abstract void ClientStatusNotify(String strClientKey, int Status);
	
	/** 如果收到关闭指令，就关闭SOCKET和释放资源 */
	protected abstract void ClientClose(String pClientKey);
	
	/**
	 * 取得接收线程数据列表
	 * @return byte[] data
	 */ 
	public byte[] GetRecvMsgList()
	{
		byte[] data = null;
		synchronized(markRecv)
		{
			if(!recvMsgList.isEmpty())
			{	
				data = (byte[])recvMsgList.removeFirst();
			}
		}
		return data;
	}
	
	/**
	 * 将 接收线程 添加到 数据列表 
	 * [线程安全的]
	 * @param object
	 */
	public void SetRecvMsgList(Object object)
	{
		synchronized(markRecv)
		{
			recvMsgList.addLast(object);
		}
	}	

	/**
	 * 生成序列号
	 * @return int m_Seq
	 */
	public int GetSeq()
	{
		if(m_Seq++ == 0xffffff)
			m_Seq = 0;
		return m_Seq;
	}
	
	/**
	 * 返回接收列表size大小
	 * @return long recvMsgList.size()
	 */
	public long GetRecvMsgListLength()
	{
		return recvMsgList.size();
	}
	
	protected abstract byte[] GetActiveTestBuf();
	protected abstract byte[] EnCode(int msgCode, String pData);
		
	/**
	 * 作为 每个CPM 相对应的服务端 , (但自己本身又相当于一个客户端)
	 * @author cui
	 */
	public class ClientSocket extends Thread
	{	
		public  Socket   objSocket   = null;	
		private RecvThrd objRecvThrd = null;
		private SendThrd objSendThrd = null;
		
		/** 发送信息列表 */
		private LinkedList<Object> sendMsgList = null;
		
		/** 同步锁  */
		private byte[]         markSend    = new byte[1]; 
		/** 客户端 ID */
		public  String         m_ClientKey = "";          
		/** 测试包测试次数 */
		private int            m_TestSta   = 0;           
		
		/**
		 * @param objClient
		 * @param pClientKey
		 * @return
		 */
		public boolean init(Socket objClient, String pClientKey)
		{		
			try
			{
				m_ClientKey = pClientKey; // pClientKey = Pid = [0100000001          ]
				objSocket   = objClient;  // objTcpSvrSock.accept();
				objSocket.setSoTimeout(0);
							
				sendMsgList = new LinkedList<Object>();	
				
				objRecvThrd = new RecvThrd(objSocket);
				objRecvThrd.start(); // Decode; RecvMsg入库 
				
				objSendThrd = new SendThrd(objClient);
				objSendThrd.start(); // SendMsg 入库
						
				this.start();			
			}
			catch(Exception exp)
			{
				exp.printStackTrace();	
				return false;
			}
			return true;
		}
		
		public void run()
		{
			int testTime = (int) (new java.util.Date().getTime() / 1000);    // 测试时间
			int nowTime  = testTime;                                         // 当前时间
			int dTime    = 0;
			
			//Active Test
			while(true)
			{
				try
				{
					sleep(2000); // 2秒
					if(null == objSocket || objSocket.isClosed())
					{
						CommUtil.LOG("socket is closed " + m_ClientKey);
						ClientClose(m_ClientKey);
						break;
					}
					nowTime = (int)(new java.util.Date().getTime()/1000);   // 此刻时间
					dTime   = nowTime - testTime;
					if(dTime > m_iTimeOut)
					{
						m_TestSta++;
						if(m_TestSta > CmdUtil.ACTIVE_TEST_END)
						{		
							CommUtil.LOG("m_TestSta > CmdUtil.ACTIVE_TEST_END " + m_ClientKey);
							ClientClose(m_ClientKey);
						}
						else
						{
							if(m_TestSta >= CmdUtil.ACTIVE_TEST_START)
							{
								byte[] byteData = GetActiveTestBuf();
								if(null != byteData)
								{
									SetSendMsgList(byteData);    //放入发送列表			
									CommUtil.LOG("Send Active Test..");	
								}
							}
						}
						testTime = nowTime;
					}				
				}
				catch(Exception ex)
				{
					CommUtil.LOG("TcpSvr/Run:Active Test Error.............\n");
					ex.printStackTrace();
					continue;
				}
			}
		}
		
		//将信息送到发送队列
		public void SendMsg(int msgCode, String pData)
		{
			SetSendMsgList(EnCode(msgCode, pData));
		}
		
		private void SetSendMsgList(Object  object)
		{
			synchronized(markSend)
			{
				sendMsgList.addLast(object);
			}
		}
		
		//从发送队列取一条信息
		private byte[] getSendMsgList()
		{
			byte[] data = null;			
			synchronized(markSend)
			{
				if(null != sendMsgList && !sendMsgList.isEmpty())
				{	
					data = (byte[]) sendMsgList.removeFirst();
				}
			}
			return data;
		}	
	
		/**
		 * 接收线程
		 * @author cui
		 */
		private class RecvThrd extends Thread
		{
			/** 接收Recv */
			private DataInputStream RecvChannel = null;
			/** 创建接收线程 */
			public  RecvThrd(Socket pSocket) throws Exception
			{
				RecvChannel = new DataInputStream(pSocket.getInputStream());
			}
			public void run()
			{
				Vector<Object> data    = new Vector<Object>();
				int         nRecvLen   = 0;  // 读取长度
				int         nRcvPos    = 0;  // 读取位置
				int         nCursor    = 0;  // 解码开始位置
				byte        ctRslt     = 0;  // DeCode函数的返回值 
				boolean     bContParse = true;
				byte[]      cBuff      = new byte[Cmd_Sta.CONST_MAX_BUFF_SIZE]; // 2048 字节
				
				while (true)
				{
					try
					{
						if(null == objSocket || objSocket.isClosed())
						{
							ClientClose(m_ClientKey);
							break;
						}
						nRecvLen = RecvChannel.read(cBuff, nRcvPos, (Cmd_Sta.CONST_MAX_BUFF_SIZE - nRcvPos)); //读取 2048 字节 [实际没有这么多]
						if(nRecvLen <= 0)
						{ 
							ClientClose(m_ClientKey);
							CommUtil.LOG("closed the socket in TcpSvr Recvs" + m_ClientKey);
							break;
						}
						m_TestSta  = 0;
						nRcvPos   += nRecvLen;
						nRecvLen   = 0;
						nCursor    = 0;
						int nLen   = 0;
						bContParse = true;					
				
						while (bContParse)
						{
							nLen = nRcvPos - nCursor; // 290
							if(0 >= nLen) 
							{
								break;
							}
							data.clear(); // 清空 Vector
							data.insertElementAt(new Integer(nLen),0);    // 数据长度 290
							data.insertElementAt(new Integer(nCursor),1); // 当前起始位置
							ctRslt = DeCode(cBuff, data);
							nLen = ((Integer)data.get(0)).intValue();     // vectData[0] = unMsgLen
							switch(ctRslt)
							{
								case CmdUtil.CODEC_CMD:
									byte [] Resp = ((byte[])data.get(1)); // 应答包					
									if(null != Resp && Resp.length > 0)
									{
										SetSendMsgList(Resp);       // 将应答包 放入 SendMsgList
									}		
									byte[] transData = (byte[])data.get(2); // 
									if(null != transData && transData.length >= Cmd_Sta.CONST_MSGHDRLEN)
									{
										SetRecvMsgList(transData);  // 接收列表 
									}
									nCursor += nLen;
									break;
								case CmdUtil.CODEC_RESP:
									nCursor += nLen;
									break;
								case CmdUtil.CODEC_NEED_DATA:
									bContParse = false;
									break;						
								case CmdUtil.CODEC_ERR:		
									nRcvPos = 0;							
									bContParse = false;
									break;
								default:
									break;
							}
						}//bContParse
						if(0 != nRcvPos)
						{
							System.arraycopy(cBuff, nCursor, cBuff, 0, nRcvPos - nCursor);
							nRcvPos -= nCursor;
						}
					}
					catch(SocketException Ex1)
					{
						Ex1.printStackTrace();
						ClientClose(m_ClientKey);
						break;
					}
					catch(Exception Ex)
					{
						Ex.printStackTrace();
						continue;
					}
				}//while		
			}
			
			/**
			 * 解码
			 * @param pMsg
			 * @param vectData
			 * @return
			 */
			private byte DeCode(byte[] pMsg, Vector<Object> vectData)
			{
				byte RetVal  = CmdUtil.CODEC_ERR; // 4
				int  nUsed   = ((Integer)vectData.get(0)).intValue();   // 现有的数据长度
				int  nCursor = ((Integer)vectData.get(1)).intValue();   // 解码开始位置
				try
				{
					DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(pMsg));
					if(nUsed < (int)CmdUtil.MSGHDRLEN )
					{
						return CmdUtil.CODEC_NEED_DATA; // 3
					}
					DinStream.skip(nCursor);  // nCursor = 0 
					int unMsgLen  = CommUtil.converseInt(DinStream.readInt());  // 通信包长度
					int unMsgCode = CommUtil.converseInt(DinStream.readInt());  // 业务类型
					int unStatus  = CommUtil.converseInt(DinStream.readInt());  // 状态
					int unMsgSeq  = CommUtil.converseInt(DinStream.readInt());  // 序列号
					int unReserve = CommUtil.converseInt(DinStream.readInt());  // 保留字段
					//System.out.println("DeCode:" + new String(pMsg));
					if(unMsgLen < CmdUtil.MSGHDRLEN || unMsgLen > CmdUtil.RECV_BUFFER_SIZE)
					{	// 解析后的通信包长度	< 包头20 或 > 2048 	
						CommUtil.LOG("unMsgLen < CmdUtil.MSGHDRLEN " + unMsgLen);
						return CmdUtil.CODEC_ERR;
					}
			
					if(nUsed < unMsgLen) 
					{   // buffer 的长度 < 解析后的通信包长度
						return CmdUtil.CODEC_NEED_DATA;
					}
					
					// 当 nUsed >= unMsgLen 且 20 < unMsgLen < 2048 时  
					vectData.insertElementAt(new Integer(unMsgLen), 0); // vectData[0] = unMsgLen 		
					if((unMsgCode & CmdUtil.COMM_RESP) != 0)           
					{	// 当 unMsgCode = COMM_RESP (各自回应0x80000000|CMD)
						return CmdUtil.CODEC_RESP;
					}
					
					DinStream.close();
					//CommUtil.printMsg(pMsg, unMsgLen);		
					ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
					DataOutputStream      doutStream = new DataOutputStream(boutStream);
					//置应答包
					doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN));
					doutStream.writeInt(CommUtil.converseInt(unMsgCode|CmdUtil.COMM_RESP));
					doutStream.writeInt(CommUtil.converseInt(unStatus));
					doutStream.writeInt(CommUtil.converseInt(unMsgSeq));
					doutStream.writeInt(CommUtil.converseInt(unReserve));
					vectData.insertElementAt(boutStream.toByteArray(), 1);  // 应答包
					boutStream.close();
					doutStream.close();	 
    	
					vectData.insertElementAt(null,2); // ???
					switch(unMsgCode)
					{
						case CmdUtil.COMM_ACTIVE_TEST:
							vectData.insertElementAt(null, 2);
							RetVal = CmdUtil.CODEC_CMD;
							break;
						case CmdUtil.COMM_SUBMMIT: // 客户端提交  上行
						case CmdUtil.COMM_DELIVER: // 服务器派发  下行
						{
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							DataOutputStream      dout = new DataOutputStream(bout);
							/*
							PlatForm Submit 
							[0100000002          ] 20字节
							[                7186000010010431090001天信流量计                    0003标况流量
							            2016-10-23 10:44:07 724.56                                         
							                                                                               
							t         ]250字节
							*/
							dout.write(CommUtil.StrRightFillSpace(m_ClientKey, 20).getBytes());
							dout.write(pMsg, nCursor, unMsgLen);
							vectData.insertElementAt(bout.toByteArray(), 2);
							dout.close();
							bout.close();
							RetVal = Cmd_Sta.CODEC_CMD;
							break;
						}
						default:
							break;
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
		 * @author cui
		 */
		private class SendThrd extends Thread
		{
			private DataOutputStream SendChannel = null;
			public SendThrd(Socket pSocket)throws Exception
			{
				SendChannel = new DataOutputStream(pSocket.getOutputStream());
			}
			public void run()
			{
				while(true)
				{
					try
					{
						if(null == objSocket || objSocket.isClosed())
						{
							ClientClose(m_ClientKey);
							break;
						}
				
						byte[] data = getSendMsgList();
						if(null == data )
						{
							sleep(10);
							continue;
						}
						if(data.length > 20)
						{
							//CommUtil.printMsg(data,  data.length);
						}
						SendChannel.write(data);
						SendChannel.flush();
					
					}
					catch(SocketException Ex)
					{
						ClientClose(m_ClientKey);
						break;
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}					
				}				
			}	
		}//SendThrd
	}//ClientSocket
}//TcpSvrCls