package bean;

import java.io.UnsupportedEncodingException;

import net.MsgCtrl;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
import bean.BaseCmdBean;

public class DeviceActBean extends BaseCmdBean {	
	
	private String d_Satus 		= "";		// *命令发送状态
	private String deal   		= "";		// *处理的指令
	private String dev_Seq   	= "";		// *序号
	private String dev_Type     = "";		// *设备类型
	private String dev_Id	    = "";		// *设备编号 Id
	private String act_Id	    = "";		// *动作编号
	private String act_Name	    = "";		// *动作名称
	private String status	    = "";		// *是否生效
	private String is_Status	= "";		// *是否填值，0：否；1：是
	
	private String cpm_Id   	= "";		// Cpm_Id

	public DeviceActBean(int action, String seq)
	{
		super(action, seq);
		setBeanName("DeviceActBean");
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
//		act_Id       	= CommUtil.BSubstring(strRequest, 42, 8).trim();	
//		act_Name      	= CommUtil.BSubstring(strRequest, 50, 20).trim();	
//		status       	= CommUtil.BSubstring(strRequest, 70, 1).trim();
//		is_Status       = CommUtil.BSubstring(strRequest, 71, 1).trim();
//
//		cpm_Id			= srcKey.trim();
		
		try{
			d_Satus        	= new String(strData, 60, 4).trim();
			deal        	= new String(strData, 64, 4).trim();
			dev_Seq       	= new String(strData, 68, 4).trim();	
			dev_Type       	= new String(strData, 72, 6).trim();	
			dev_Id       	= new String(strData, 78, 4).trim();	
			act_Id       	= new String(strData, 82, 8).trim();	
			act_Name      	= new String(strData, 90, 20, "GB2312").trim();	
			status       	= new String(strData, 110, 1).trim();
			is_Status       = new String(strData, 111, 1).trim();
	
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
			sql = " replace into dev_act(cpm_id, dev_Type, dev_Id, act_Id, act_Name, status, is_Status)" + 
					  " values('" + cpm_Id + "','" + dev_Type + "','" + dev_Id + "','" + act_Id + "','" + act_Name + "','" + status + "','" + is_Status + "')";
				
			System.out.println("d_Satus["+d_Satus+"]");
			System.out.println("deal["+deal+"]");
			System.out.println("dev_Seq["+dev_Seq+"]");
			System.out.println("dev_Type["+dev_Type+"]");
			System.out.println("dev_Id["+dev_Id+"]");
			System.out.println("act_Id["+act_Id+"]");
			System.out.println("act_Name["+act_Name+"]");
			System.out.println("status["+status+"]");
			System.out.println("is_Status["+is_Status+"]");
		}
		
		System.out.println("DeviceList Sql[" + sql +"]");
		//CommUtil.LOG("DeviceList Sql[" + sql +"]");
		
		if (m_MsgCtrl.getM_DBUtil().doUpdate(sql))
		{
			ret = Cmd_Sta.STA_SUCCESS;
		}
		//回复
		//Status(CommUtil.IntToStringLeftFillZero(ret, 4));
		//execResponse();
		return ret;
	}
	@SuppressWarnings("unused")
	private String EncodeSendMsg()
	{
		String ret = "";
//		ret += CommUtil.StrBRightFillSpace(getReserve(), 20);		//保留字
//		ret += CommUtil.StrBRightFillSpace(getD_Satus(), 4);		//命令发送状态
//		ret += CommUtil.StrBRightFillSpace(getDeal(), 4);			//处理指令
		
		return ret;
		
//		D_STATUS	4	Octet String	命令发送状态(0000)
//		DEAL		4	Octet String	处理指令(1008)
//		DEV_SEQ		4	Octet String	序号
//		DEV_TYPE	6	Octet String	设备类型，参见附录1
//		ID			4	Octet String	设备编号
//		ACT_ID		8	Octet String	动作编号
//		ACT_NAME	20	Octet String	动作名称
//		STATUS		1	Octet String	是否生效
//		IS_DATA		1	Octet String	是否填值,0:否；1:是
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

	public String getDev_Type()
	{
		return dev_Type;
	}


	public void setDev_Type(String dev_Type)
	{
		this.dev_Type = dev_Type;
	}

	public String getAct_Id()
	{
		return act_Id;
	}


	public void setAct_Id(String act_Id)
	{
		this.act_Id = act_Id;
	}


	public String getAct_Name()
	{
		return act_Name;
	}


	public void setAct_Name(String act_Name)
	{
		this.act_Name = act_Name;
	}


	public String getStatus()
	{
		return status;
	}


	public void setStatus(String status)
	{
		this.status = status;
	}


	public String getIs_Status()
	{
		return is_Status;
	}


	public void setIs_Status(String is_Status)
	{
		this.is_Status = is_Status;
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


}

