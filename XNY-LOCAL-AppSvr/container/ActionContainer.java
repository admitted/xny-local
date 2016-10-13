package container;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import util.*;
import bean.*;

public class ActionContainer
{
	public  static Hashtable<String, BaseCmdBean> objActionTable = null; //��½�ͻ����б�
	private static Byte markActionTable = new Byte((byte)1);	         //�� 
	
	private static TimeCheckThrd checkThrd = null;

	InetAddress    addr      = InetAddress.getLocalHost();               //���ر�������
	public String  m_LocalIp = addr.getHostAddress().toString();         //��ñ���IP
	
	public ActionContainer() throws Exception
	{	  
		
	}
	
	/**
	 * ��ʼ�� �ͻ����б�objActionTable ��ʱ��У���߳�checkThrd
	 * �������߳� checkThrd
	 * @return
	 */
	public static boolean Initialize()
	{
		System.out.println("�����߳�......");
		boolean ret = false;
		try
		{
			objActionTable  = new Hashtable<String, BaseCmdBean>(); //��ʼ�� Hashtable �ͻ����б� (��ʱΪ��)
			checkThrd       = new TimeCheckThrd(30);
			checkThrd.start();
			
			ret             = true;  //��������쳣 ,ִ�в�����һ��
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * ��HashTable�� ���ݴ���� String (ʵ���ϵ�objActionTable keyֵ) �����ض��� BaseCmdBean
	 * @param pKey
	 * @return BaseCmdBean
	 */
	public static BaseCmdBean GetAction(String pKey)
	{
		BaseCmdBean bean = null;
		try
		{			
			synchronized(markActionTable)
			{
				if(!objActionTable.isEmpty() && objActionTable.containsKey(pKey))
				{
					bean = (BaseCmdBean) objActionTable.get(pKey);
					objActionTable.remove(pKey);		//�ڹ�ϣ�����Ƴ�
				}
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
		return bean;
	}
	
	/**
	 * ��objActionTable ����һ�� ��ֵ�� (pKey,baseCmdBean)
	 * @param pKey
	 * @param bean
	 */
	public static void InsertAction(String pKey, BaseCmdBean bean)
	{
		try
		{			
			synchronized(markActionTable)					
			{
				if(objActionTable.containsKey(pKey))
				{
					CommUtil.PRINT("Key[" + pKey + "] Already Exist!");
					objActionTable.remove(pKey);		//�ڹ�ϣ�����Ƴ��ͻ���
				}		
				objActionTable.put(pKey , bean);
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
	}

	/**
	 * ��objActionTable ɾ�� һ�� ��ֵ�� (pKey,baseCmdBean)
	 * @param pKey
	 */
	public static void RemoveAction(String pKey)
	{
		try
		{			
			synchronized(markActionTable)
			{
				if(!objActionTable.isEmpty() && objActionTable.containsKey(pKey))
				{
						objActionTable.remove(pKey);		//�ڹ�ϣ�����Ƴ��ͻ���
				}
			}
		}
		catch(Exception exp)
		{		
			exp.printStackTrace();	
		}
	}
	
	/**
	 * ��ȡ ��½�ͻ���  ��Ӧ��ʱ�б� (��Ӧʱ�����30���)
	 * @param mTimeOut
	 * @return LinkedList<String>
	 */
	public static LinkedList<String> GetTimeOutList(int mTimeOut) // m_TimeOut = 30
	{
		LinkedList<String> checkList = new LinkedList<String>();  // ���������б�,���ڿͻ������ݽ��� 
		try
		{
			synchronized(markActionTable)
			{
				Enumeration<BaseCmdBean> en = objActionTable.elements(); // ���ش� objActionTable ��ϣ���е�ֵ��ö��
				while(en.hasMoreElements())
				{    
					BaseCmdBean client = en.nextElement();
					int TestTime = (int)(new java.util.Date().getTime()/1000); // ��ǰʱ��ֵ 
					if( TestTime > client.getTestTime() + mTimeOut )           // ���ʱ�� ���� 30 ��
					{  
						checkList.addLast(CommUtil.StrBRightFillSpace(client.getSeq(), 20));   // SessionID
					}
				}
			}
			while(!checkList.isEmpty())
			{
				String data = checkList.removeFirst();
				if(null ==  data)
				{
					break;
				}						
				BaseCmdBean bean = GetAction(data); 
				if(null != bean)
					bean.noticeTimeOut();
				CommUtil.LOG(data + " ��Ӧ��ʱ 111");
			}
		}catch(Exception e)
		{}		
		return checkList;
	}	
}//ActionContainer