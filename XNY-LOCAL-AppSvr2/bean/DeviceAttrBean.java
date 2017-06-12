package bean;

import java.io.UnsupportedEncodingException;

import net.MsgCtrl;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
import bean.BaseCmdBean;

public class DeviceAttrBean extends BaseCmdBean {	
	
	private String d_Satus 		= "";		// *命令发送状态
	private String deal   		= "";		// *处理的指令
	private String dev_Seq   	= "";		// *序号
	private String dev_Type     = "";		// *设备类型
	private String dev_Id	    = "";		// *设备编号 Id
	private String attr_Id	    = "";		// *属性编号
	private String attr_Name	= "";		// *属性名称
	private String status	    = "";		// *是否采集
	
	private String cpm_Id   	= "";		// Cpm_Id
	
	public DeviceAttrBean(int action, String seq)
	{
		super(action, seq);
		setBeanName("DeviceAttrBean");
	}
	

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData) {
		// TODO Auto-generated method stub

		//String strCData = CommUtil.BytesToHexString(CommUtil.BSubstring(strRequest, 48, 2).getBytes(), 2);	
		//System.out.println("strCData[" + strCData + "]");
		
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
		try
		{
			d_Satus        	= new String(strData, 60, 4).trim();
			deal        	= new String(strData, 64, 4).trim();
			dev_Seq       	= new String(strData, 68, 4).trim();	
			dev_Type       	= new String(strData, 72, 6).trim();	
			dev_Id       	= new String(strData, 78, 4).trim();	
			attr_Id       	= new String(strData, 82, 4).trim();	
			attr_Name      	= new String(strData, 86, 20, "GB2312").trim();	
			status       	= new String(strData, 106, 1).trim();
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
			sql = " replace into dev_attr(cpm_id, dev_Type, dev_Id, attr_Id, attr_Name, status)" + 
					  " values('" + cpm_Id + "','" + dev_Type + "','" + dev_Id + "','" + attr_Id + "','" + attr_Name + "','" + status + "')";
				
			System.out.println("d_Satus["+d_Satus+"]");
			System.out.println("deal["+deal+"]");
			System.out.println("dev_Seq["+dev_Seq+"]");
			System.out.println("dev_Type["+dev_Type+"]");
			System.out.println("dev_Id["+dev_Id+"]");
			System.out.println("attr_Id["+attr_Id+"]");
			System.out.println("attr_Name["+attr_Name+"]");
			System.out.println("status["+status+"]");
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
//		DEAL		4	Octet String	处理指令(1007)
//		DEV_SEQ		4	Octet String	序号
//		DEV_TYPE	6	Octet String	设备类型，参见附录1
//		ID			4	Octet String	设备编号
//		ATTR_ID		4	Octet String	属性编号
//		ATTR_NAME	20	Octet String	属性名称
//		STATUS		1	Octet String	是否采集
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


	public String getAttr_Id()
	{
		return attr_Id;
	}


	public void setAttr_Id(String attr_Id)
	{
		this.attr_Id = attr_Id;
	}


	public String getAttr_Name()
	{
		return attr_Name;
	}


	public void setAttr_Name(String attr_Name)
	{
		this.attr_Name = attr_Name;
	}


	public String getStatus()
	{
		return status;
	}


	public void setStatus(String status)
	{
		this.status = status;
	}
}

