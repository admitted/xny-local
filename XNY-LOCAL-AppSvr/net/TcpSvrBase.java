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

	/** TCP������  */
	private ServerSocket objTcpSvrSock = null;
	
	/** ���������б�,���ڿͻ������ݽ��� */
	public LinkedList<Object> recvMsgList = null;
	
	/** �߳�ͬ���� */
	public Byte markRecv = new Byte((byte)1);      
	
	private int m_Seq      = 0;
	private int m_iPort    = 0;
	private int m_iTimeOut = 0;
	
	/**
	 * ��ȡ�����ļ����� �̳���ʵ��
	 * @throws Exception
	 */
	public TcpSvrBase() throws Exception
	{
	}
	
	/**
	 * ��ʼ��TCP������
	 * ���� �˿� 60110 ��ô������� Socket
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
	 * ���� Socket ���� [TcpSvrBase������һ���߳�]
	 * (ѭ������accept()�ȴ��ͻ�������) ��ͻ���ͨ��
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{	
		while (true)
		{  
			try
			{
				Socket objClient = objTcpSvrSock.accept(); // ����һ�� ��Կͻ��� socket ���жԽ�
				objClient.setSoTimeout(m_iTimeOut*1000);   // ͨ��ָ����ʱֵ ����/���� SO_TIMEOUT���Ժ���Ϊ��λ��
														   // ����ʱ��  �ͶϿ� �ͻ���
				DataInputStream RecvChannel = new DataInputStream(objClient.getInputStream());
				byte[] Buffer = new byte[1024];            // ��������������Buffer
				
				int RecvLen = RecvChannel.read(Buffer);    // ���ض�ȡ���Ļ�����Buffer �ֽڳ���
				
				CommUtil.PRINT("Send Original:");          
				CommUtil.printMsg(Buffer, RecvLen);        
				/**
				Send Original:
				
				5a 00 00 00   01 00 00 00   00 00 00 00   01 00 00 00   00 00 00 00
				30 30 30 30   30 31 30 30   30 30 30 30   30 31 20 20   20 20 20 20
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
				
				//������֤
				String Pid = null;
				if(null == (Pid = CheckClient(Buffer, objClient))) // Pid = [0100000001          ]
					continue;
				
				//����ظ�
				DataOutputStream SendChannel = new DataOutputStream(objClient.getOutputStream());
				SendChannel.write(new String(Buffer, 0, 44).getBytes());
				SendChannel.flush();
				objClient.setSoTimeout(0);
				ClientStatusNotify(Pid, STATUS_CLIENT_ONLINE); // ֪ͨ����
		 		continue;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				continue;
			}
		}//while
	}
	
	/** ������֤ ����ʵ�ִ˳��󷽷�   */
	protected abstract String CheckClient(byte[] buf, Socket objClient);
	
	/** ״̬֪ͨ  */
	protected abstract void ClientStatusNotify(String strClientKey, int Status);
	
	/** ����յ��ر�ָ��͹ر�SOCKET���ͷ���Դ */
	protected abstract void ClientClose(String pClientKey);
	
	/**
	 * ȡ�ý����߳������б�
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
	 * �� �����߳� ��ӵ� �����б� 
	 * [�̰߳�ȫ��]
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
	 * �������к�
	 * @return int m_Seq
	 */
	public int GetSeq()
	{
		if(m_Seq++ == 0xffffff)
			m_Seq = 0;
		return m_Seq;
	}
	
	/**
	 * ���ؽ����б�size��С
	 * @return long recvMsgList.size()
	 */
	public long GetRecvMsgListLength()
	{
		return recvMsgList.size();
	}
	
	protected abstract byte[] GetActiveTestBuf();
	protected abstract byte[] EnCode(int msgCode, String pData);
		
	/**
	 * ��Ϊ ÿ��CPM ���Ӧ�ķ���� , (���Լ���������һ���ͻ���)
	 * @author cui
	 */
	public class ClientSocket extends Thread
	{	
		public  Socket   objSocket   = null;	
		private RecvThrd objRecvThrd = null;
		private SendThrd objSendThrd = null;
		
		/** ������Ϣ�б�        */
		private LinkedList<Object> sendMsgList = null;
		
		/** ͬ����        */
		private byte[]         markSend    = new byte[1]; 
		/** �ͻ��� ID  */
		public  String         m_ClientKey = "";          
		/** ���԰����Դ��� */
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
				objSocket   = objClient;  // TCP������  objTcpSvrSock.accept();
				objSocket.setSoTimeout(0);
							
				sendMsgList = new LinkedList<Object>();	
				
				objRecvThrd = new RecvThrd(objSocket);
				objRecvThrd.start();
				
				objSendThrd = new SendThrd(objClient);
				objSendThrd.start();
						
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
			int testTime = (int) (new java.util.Date().getTime() / 1000);    // ����ʱ��
			int nowTime  = testTime;                                         // ��ǰʱ��
			int dTime    = 0;
			
			//Active Test
			while(true)
			{
				try
				{
					sleep(2000); // 2��
					if(null == objSocket || objSocket.isClosed())
					{
						CommUtil.LOG("socket is closed " + m_ClientKey);
						ClientClose(m_ClientKey);
						break;
					}
					nowTime = (int)(new java.util.Date().getTime()/1000);   // �˿�ʱ��
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
									SetSendMsgList(byteData);    //���뷢���б�			
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
		
		//����Ϣ�͵����Ͷ���
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
		
		//�ӷ��Ͷ���ȡһ����Ϣ
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
		 * �����߳�
		 * @author cui
		 */
		private class RecvThrd extends Thread
		{
			/** ����Recv */
			private DataInputStream RecvChannel = null;
			/** ���������߳� */
			public  RecvThrd(Socket pSocket) throws Exception
			{
				RecvChannel = new DataInputStream(pSocket.getInputStream());
			}
			public void run()
			{
				Vector<Object> data    = new Vector<Object>();
				int         nRecvLen   = 0;  // ��ȡ����
				int         nRcvPos    = 0;  // ��ȡλ��
				int         nCursor    = 0;  // ���뿪ʼλ��
				byte        ctRslt     = 0;  // DeCode�����ķ���ֵ 
				boolean     bContParse = true;
				byte[]      cBuff      = new byte[Cmd_Sta.CONST_MAX_BUFF_SIZE]; // 2048 �ֽ�
				
				while (true)
				{
					try
					{
						if(null == objSocket || objSocket.isClosed())
						{
							ClientClose(m_ClientKey);
							break;
						}
						nRecvLen = RecvChannel.read(cBuff, nRcvPos, (Cmd_Sta.CONST_MAX_BUFF_SIZE - nRcvPos)); //��ȡ 2048 �ֽ� [ʵ��û����ô��]
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
							data.clear(); // ��� Vector
							data.insertElementAt(new Integer(nLen),0);    // 290
							data.insertElementAt(new Integer(nCursor),1); // 0
							ctRslt = DeCode(cBuff, data);
							nLen = ((Integer)data.get(0)).intValue();
							switch(ctRslt)
							{
								case CmdUtil.CODEC_CMD:
									byte [] Resp = ((byte[])data.get(1));					
									if(null != Resp && Resp.length > 0)
									{
										SetSendMsgList(Resp);
									}		
							
									byte[] transData = (byte[])data.get(2);
									if(null != transData && transData.length >= Cmd_Sta.CONST_MSGHDRLEN)
									{
										SetRecvMsgList(transData);
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
			 * ����
			 * @param pMsg
			 * @param vectData
			 * @return
			 */
			private byte DeCode(byte[] pMsg, Vector<Object> vectData)
			{
				byte RetVal  = CmdUtil.CODEC_ERR; // 4
				int  nUsed   = ((Integer)vectData.get(0)).intValue();   // ���е����ݳ���
				int  nCursor = ((Integer)vectData.get(1)).intValue();   // ���뿪ʼλ��
				try
				{
					DataInputStream DinStream = new DataInputStream(new ByteArrayInputStream(pMsg));
					if(nUsed < (int)CmdUtil.MSGHDRLEN )
					{
						return CmdUtil.CODEC_NEED_DATA; // 3
					}
					DinStream.skip(nCursor);  // nCursor = 0 
					int unMsgLen  = CommUtil.converseInt(DinStream.readInt());  // ͨ�Ű�����
					int unMsgCode = CommUtil.converseInt(DinStream.readInt());  // ҵ������
					int unStatus  = CommUtil.converseInt(DinStream.readInt());  // ״̬
					int unMsgSeq  = CommUtil.converseInt(DinStream.readInt());  // ���к�
					int unReserve = CommUtil.converseInt(DinStream.readInt());  // �����ֶ�
					//System.out.println("DeCode:" + new String(pMsg));
					if(unMsgLen < CmdUtil.MSGHDRLEN || unMsgLen > CmdUtil.RECV_BUFFER_SIZE)
					{	// �������ͨ�Ű�����	< ��ͷ20   ��  > 2048 	
						CommUtil.LOG("unMsgLen < CmdUtil.MSGHDRLEN " + unMsgLen);
						return CmdUtil.CODEC_ERR;
					}
			
					if(nUsed < unMsgLen) 
					{   // buffer �ĳ��� < �������ͨ�Ű�����
						return CmdUtil.CODEC_NEED_DATA;
					}
					
					// �� nUsed >= unMsgLen ʱ�ߵ���  
					vectData.insertElementAt(new Integer(unMsgLen), 0); 		
					if((unMsgCode & CmdUtil.COMM_RESP) != 0)           
					{	//�� unMsgCode = COMM_RESP ���� CODEC_TRANS ʱ
						return CmdUtil.CODEC_RESP;
					}
			
					DinStream.close();

					//CommUtil.printMsg(pMsg, unMsgLen);		
					ByteArrayOutputStream boutStream = new ByteArrayOutputStream();
					DataOutputStream doutStream = new DataOutputStream(boutStream);
					//��Ӧ���
					doutStream.writeInt(CommUtil.converseInt(CmdUtil.MSGHDRLEN));
					doutStream.writeInt(CommUtil.converseInt(unMsgCode|CmdUtil.COMM_RESP));
					doutStream.writeInt(CommUtil.converseInt(unStatus));//Sta
					doutStream.writeInt(CommUtil.converseInt(unMsgSeq));//seq
					doutStream.writeInt(CommUtil.converseInt(unReserve));//
					vectData.insertElementAt(boutStream.toByteArray(), 1);
					boutStream.close();
					doutStream.close();	 
    	
					vectData.insertElementAt(null,2); // ???
					switch(unMsgCode)
					{
						case CmdUtil.COMM_ACTIVE_TEST:
							vectData.insertElementAt(null, 2);
							RetVal = CmdUtil.CODEC_CMD;
							break;
						case CmdUtil.COMM_SUBMMIT: // �ͻ����ύ  ����
						case CmdUtil.COMM_DELIVER: // �������ɷ�  ����
						{
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							DataOutputStream dout = new DataOutputStream(bout);
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
		 * �����߳�
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