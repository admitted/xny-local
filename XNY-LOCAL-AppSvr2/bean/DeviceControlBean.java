package bean;

import net.MsgCtrl;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
import bean.BaseCmdBean;

public class DeviceControlBean extends BaseCmdBean {	
	private String BTime      	= "";	    // 当前时间
	
	private String d_Satus 		= "";		// *命令发送状态
	private String deal   		= "";		// *处理的指令
	private String cpm_Id   	= "";		// Cpm_Id
	private String dev_Type   	= "";		// *设备类型
	private String dev_Id     	= "";		// *设备编号 Id
	private String act_Id     	= "";		// *设备动作属性编号
	private String act_Value  	= "";		// *发送指令的内容
	
	private String oprator    	= "";		// *操作用户
	
	
	public DeviceControlBean(int action, String seq)
	{
		super(action, seq);
		setBeanName("DeviceControlBean");
	}
	

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData) {
		// TODO Auto-generated method stub

		//String strCData = CommUtil.BytesToHexString(CommUtil.BSubstring(strRequest, 48, 2).getBytes(), 2);	
		//System.out.println("strCData[" + strCData + "]");
		
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
		d_Satus        	= CommUtil.BSubstring(strRequest, 20, 4).trim();
		deal        	= CommUtil.BSubstring(strRequest, 24, 4).trim();
		cpm_Id    		= CommUtil.BSubstring(strRequest, 28, 10).trim();
		oprator       	= CommUtil.BSubstring(strRequest, 38, 10).trim();	
		dev_Type       	= CommUtil.BSubstring(strRequest, 48, 6).trim();	
		dev_Id       	= CommUtil.BSubstring(strRequest, 54, 4).trim();	
		act_Id       	= CommUtil.BSubstring(strRequest, 58, 8).trim();	
		

		//System.out.println(d_Satus + "][" + deal + "][" + cpm_Id + "][" + oprator + "][" + dev_Type + "][" + dev_Id + "][" + act_Id );
		//BTime = CommUtil.getTime();		//取到当前系统时间  (yyyyMMddHHmmss)
		
		//byte[] byteData = new byte[2];
		//byteData[0] = strData[85];
		//byteData[1] = strData[86];
	}

	@Override
	public int execRequest(MsgCtrl m_MsgCtrl)
	{
		// TODO Auto-generated method stub
		int ret = Cmd_Sta.STA_ERROR;
		if(!d_Satus.equalsIgnoreCase("NULL") && d_Satus.length() > 0)
		{
			if(m_MsgCtrl.getM_TcpSvr().DisPatch(Cmd_Sta.COMM_DELIVER, CommUtil.StrBRightFillSpace(cpm_Id, 20), EncodeSendMsg()))
			{
				//m_MsgCtrl.InsertAction(CommUtil.StrBRightFillSpace(cpm_Id, 20), this);
				ret = Cmd_Sta.STA_SUCCESS;
			}
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
		switch (Integer.valueOf(deal))
		{
			case Cmd_Sta.CMD_DEVICE_CTRL: // 3002
				ret += CommUtil.StrBRightFillSpace(getReserve(), 20);		//保留字
				ret += CommUtil.StrBRightFillSpace(getD_Satus(), 4);		//命令发送状态
				ret += CommUtil.StrBRightFillSpace(getDeal(), 4);			//处理指令
				ret += CommUtil.StrBRightFillSpace(getDev_Type(), 6);		//设备类型
				ret += CommUtil.StrBRightFillSpace(getDev_Id(), 4);			//设备编号
				ret += CommUtil.StrBRightFillSpace(getAct_Id(), 8);			//设备动作属性编号
				ret += CommUtil.StrBRightFillSpace(getOprator(), 10);		//操作用户
				ret += CommUtil.StrBRightFillSpace(getAct_Value(), 256);	//动作数据值
				break;
			default: // 3003 3004 3005 三个动作格式一样
				ret += CommUtil.StrBRightFillSpace(getReserve(), 20);		//保留字
				ret += CommUtil.StrBRightFillSpace(getD_Satus(), 4);		//命令发送状态
				ret += CommUtil.StrBRightFillSpace(getDeal(), 4);			//处理指令
				break;
		}
		return ret;
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
//		if(Integer.valueOf(deal).equals(Cmd_Sta.CMD_DEVICE_SYN))
//		{
//			String sendStr = EncodeRespMsg();
//			if(null != sendStr)
//			{
//				System.out.println("sendStr["+sendStr+"]");
//				//String key = getReserve();
//				//m_MsgCtrl.getM_TcpSvr().DisPatch(Cmd_Sta.COMM_RESP, this.getActionSource(), key + sendStr);
//			}
//		}
	}	

	private String EncodeRespMsg()
	{
		String ret = null;
		ret = getStatus() + getAction();
		return ret;
	}


	public String getBTime()
	{
		return BTime;
	}


	public void setBTime(String bTime)
	{
		BTime = bTime;
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


	public String getAct_Id()
	{
		return act_Id;
	}


	public void setAct_Id(String act_Id)
	{
		this.act_Id = act_Id;
	}


	public String getAct_Value()
	{
		return act_Value;
	}


	public void setAct_Value(String act_Value)
	{
		this.act_Value = act_Value;
	}


	public String getOprator()
	{
		return oprator;
	}


	public void setOprator(String oprator)
	{
		this.oprator = oprator;
	}

	
}

