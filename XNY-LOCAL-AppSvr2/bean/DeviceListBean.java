package bean;

import java.io.UnsupportedEncodingException;

import net.MsgCtrl;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
import bean.BaseCmdBean;

public class DeviceListBean extends BaseCmdBean {	
	
	private String d_Satus 		= "";		// *�����״̬
	private String deal   		= "";		// *�����ָ��
	private String dev_Seq   	= "";		// *���
	private String dev_Type     = "";		// *�豸����
	private String dev_Id	    = "";		// *�豸��� Id
	private String dev_Name	    = "";		// *�豸����
	private String brief	    = "";		// *�豸���
	private String status     	= "";		// *�豸״̬
	private String cType	  	= "";		// *�豸����
	
	private String cpm_Id   	= "";		// Cpm_Id
	
	public DeviceListBean(int action, String seq)
	{
		super(action, seq);
		setBeanName("DeviceListBean");
	}
	

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData) {
		// TODO Auto-generated method stub

		//String strCData = CommUtil.BytesToHexString(CommUtil.BSubstring(strRequest, 48, 2).getBytes(), 2);	
		//System.out.println("strCData[" + strCData + "]");
		
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
//		d_Satus        	= CommUtil.BSubstring(strRequest, 20, 4).trim();
//		deal        	= CommUtil.BSubstring(strRequest, 24, 4).trim();
//		dev_Seq       	= CommUtil.BSubstring(strRequest, 28, 4).trim();	
//		dev_Type       	= CommUtil.BSubstring(strRequest, 32, 6).trim();	
//		dev_Id       	= CommUtil.BSubstring(strRequest, 38, 4).trim();	
//		dev_Name       	= CommUtil.BSubstring(strRequest, 42, 20).trim();	
//		brief       	= CommUtil.BSubstring(strRequest, 62, 20).trim();	
//		status       	= CommUtil.BSubstring(strRequest, 82, 1).trim();	
//		cType       	= CommUtil.BSubstring(strRequest, 83, 2).trim();	
//		
//		cpm_Id			= srcKey.trim();
		
		try{
			d_Satus        	= new String(strData, 60, 4).trim();
			deal        	= new String(strData, 64, 4).trim();
			dev_Seq       	= new String(strData, 68, 4).trim();	
			dev_Type       	= new String(strData, 72, 6).trim();	
			dev_Id       	= new String(strData, 78, 4).trim();	
			dev_Name       	= new String(strData, 82, 20, "GB2312").trim();	
			brief       	= new String(strData, 102, 20, "GB2312").trim();	
			status       	= new String(strData, 122, 1).trim();	
			cType       	= new String(strData, 123, 2).trim();	
			
			cpm_Id			= srcKey.trim();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//byte[] byteData = new byte[2];
		//byteData[0] = strData[85];
		//byteData[1] = strData[86];
	}

	@Override
	public int execRequest(MsgCtrl m_MsgCtrl)
	{
		// TODO Auto-generated method stub
		int ret = Cmd_Sta.STA_ERROR;
		String sql = "";
		if(!d_Satus.equalsIgnoreCase("NULL") && d_Satus.length() > 0)
		{
			sql = " replace into dev_list(cpm_id, dev_Type, dev_Id, dev_Name, brief, status, cType)" + 
				  " values('" + cpm_Id + "','" + dev_Type + "','" + dev_Id + "','" + dev_Name + "','" + brief + "','" + status + "','" + cType + "')";
			
			System.out.println("d_Satus["+d_Satus+"]");
			System.out.println("deal["+deal+"]");
			System.out.println("dev_Seq["+dev_Seq+"]");
			System.out.println("dev_Type["+dev_Type+"]");
			System.out.println("dev_Id["+dev_Id+"]");
			System.out.println("dev_Name["+dev_Name+"]");
			System.out.println("brief["+brief+"]");
			System.out.println("status["+status+"]");
			System.out.println("cType["+cType+"]");
		}
		
		System.out.println("DeviceList Sql[" + sql +"]");
		//CommUtil.LOG("DeviceList Sql[" + sql +"]");
		
		if (m_MsgCtrl.getM_DBUtil().doUpdate(sql))
		{
			ret = Cmd_Sta.STA_SUCCESS;
		}
		
		//�ظ�
		//Status(CommUtil.IntToStringLeftFillZero(ret, 4));
		//execResponse();
		return ret;
	}
	@SuppressWarnings("unused")
	private String EncodeSendMsg()
	{
		String ret = "";
//		ret += CommUtil.StrBRightFillSpace(getReserve(), 20);		//������
//		ret += CommUtil.StrBRightFillSpace(getD_Satus(), 4);		//�����״̬
//		ret += CommUtil.StrBRightFillSpace(getDeal(), 4);			//����ָ��
		
		return ret;

		//	D_STATUS	4	Octet String	�����״̬(0000)
		//	DEAL		4	Octet String	����ָ��(1006)
		//	DEV_SEQ		4	Octet String	���
		//	DEV_TYPE	6	Octet String	�豸���ͣ��μ���¼1
		//	ID			4	Octet String	�豸���
		//	DEV_NAME	20	Octet String	�豸����
		//	BRIEF		20	Octet String	�豸���
		//	STATUS		1	Octet String	�豸״̬
		//	CTYPE		2	Octet String	�豸����
	}
	public void noticeTimeOut()
	{
		
	}
	
	@Override
	public void parseReponse(String strResponse){
		// TODO Auto-generated method stub
		this.setStatus(strResponse.substring(20, 24));
	}

	@Override
	public void execResponse(MsgCtrl m_MsgCtrl) {
		// TODO Auto-generated method stub
		
	}	

	private String EncodeRespMsg()
	{
		String ret = null;
		ret = getStatus() + getAction();
		return ret;
	}


	public String getD_Satus()
	{
		return d_Satus;
	}


	public void setD_Satus(String d_Satus)
	{
		this.d_Satus = d_Satus;
	}


	public String getDeal()
	{
		return deal;
	}


	public void setDeal(String deal)
	{
		this.deal = deal;
	}


	public String getCpm_Id()
	{
		return cpm_Id;
	}


	public void setCpm_Id(String cpm_Id)
	{
		this.cpm_Id = cpm_Id;
	}


	public String getDev_Type()
	{
		return dev_Type;
	}


	public void setDev_Type(String dev_Type)
	{
		this.dev_Type = dev_Type;
	}


	public String getDev_Id()
	{
		return dev_Id;
	}


	public void setDev_Id(String dev_Id)
	{
		this.dev_Id = dev_Id;
	}


	public String getDev_Seq()
	{
		return dev_Seq;
	}


	public void setDev_Seq(String dev_Seq)
	{
		this.dev_Seq = dev_Seq;
	}


	public String getDev_Name()
	{
		return dev_Name;
	}


	public void setDev_Name(String dev_Name)
	{
		this.dev_Name = dev_Name;
	}


	public String getBrief()
	{
		return brief;
	}


	public void setBrief(String brief)
	{
		this.brief = brief;
	}


	public String getStatus()
	{
		return status;
	}


	public void setStatus(String status)
	{
		this.status = status;
	}


	public String getcType()
	{
		return cType;
	}


	public void setcType(String cType)
	{
		this.cType = cType;
	}

}

