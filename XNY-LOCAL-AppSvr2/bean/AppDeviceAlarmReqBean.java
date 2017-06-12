package bean;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import net.MsgCtrl;
import bean.BaseCmdBean;
import util.CmdUtil;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
public class AppDeviceAlarmReqBean extends BaseCmdBean 
{	
	
	private String Dev_S_Id = "";
	private String Dev_S_Name = "";
	private String Dev_S_Attr_Id = "";
	private String Dev_S_Attr_Name = "";
	private String Dev_S_Attr_Value = "";
	private String Dev_D_Id = "";
	private String Dev_D_Name = "";
	private String Dev_D_Act_Id = "";
	private String Dev_D_Act_Name = "";
	private String Dev_D_Value = "";
	private String Dev_CTime = "";
	private String Dev_Operator = "";
	private String Dev_Status = "";
	
	
	public AppDeviceAlarmReqBean(int action, String seq) {
		super(action, seq);
		setBeanName("AppDeviceAlarmReqBean");
	}

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData) 
	{
		// TODO Auto-generated method stub
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
		this.setAction(Integer.parseInt(strRequest.substring(24, 28)));
		try
		{
			Dev_S_Id         = new String(strData, 68, 10).trim();
			Dev_S_Name       = new String(strData, 78, 30, "GB2312").trim();
			Dev_S_Attr_Id    = new String(strData, 108, 4).trim();
			Dev_S_Attr_Name  = new String(strData, 112, 20, "GB2312").trim();
			Dev_S_Attr_Value = new String(strData, 132, 128, "GB2312").trim();
			Dev_D_Id         = new String(strData, 260, 10).trim();
			Dev_D_Name       = new String(strData, 270, 30, "GB2312").trim();
			Dev_D_Act_Id     = new String(strData, 300, 8).trim();
			Dev_D_Act_Name   = new String(strData, 308, 20, "GB2312").trim();
			Dev_D_Value      = new String(strData, 328, 256, "GB2312").trim();
			Dev_CTime        = new String(strData, 584, 20).trim();
			Dev_Operator     = new String(strData, 604, 10, "GB2312").trim();
			Dev_Status       = new String(strData, 614, 4).trim();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Dev_S_Id         = CommUtil.BSubstring(strRequest, 28, 10).trim();
//		Dev_S_Name       = CommUtil.BSubstring(strRequest, 38, 30).trim();
//		Dev_S_Attr_Id    = CommUtil.BSubstring(strRequest, 68, 4).trim();
//		Dev_S_Attr_Name  = CommUtil.BSubstring(strRequest, 72, 20).trim();
//		Dev_S_Attr_Value = CommUtil.BSubstring(strRequest, 92, 128).trim();
//		Dev_D_Id         = CommUtil.BSubstring(strRequest, 220, 10).trim();
//		Dev_D_Name       = CommUtil.BSubstring(strRequest, 230, 30).trim();
//		Dev_D_Act_Id     = CommUtil.BSubstring(strRequest, 260, 8).trim();
//		Dev_D_Act_Name   = CommUtil.BSubstring(strRequest, 268, 20).trim();
//		Dev_D_Value      = CommUtil.BSubstring(strRequest, 288, 256).trim();
//		Dev_CTime        = CommUtil.BSubstring(strRequest, 544, 20).trim();
//		Dev_Operator     = CommUtil.BSubstring(strRequest, 564, 10).trim();
//		Dev_Status       = CommUtil.BSubstring(strRequest, 574, 4).trim();
	}

	@Override
	public int execRequest(MsgCtrl m_MsgCtrl)
	{
		// TODO Auto-generated method stub
		int ret = Cmd_Sta.STA_ERROR;
		
		//Ö´ÐÐ
	    if(null != Dev_Status && !Dev_Status.equals("0000") && !Dev_Status.equals("3000"))
	    {
	    	Dev_Status = "3006";
	    }
	    String Sql = "insert into alarm_info(cpm_id, s_id, s_cname, s_attr_id, s_attr_name, s_attr_value, d_id, d_cname, d_act_id, d_act_name, cdata, ctime, operator, status)" +
			  		 "values('"+ this.getActionSource().trim() +"', " +
			  		 	    "'"+ Dev_S_Id +"', " +
			  		 	    "'"+ Dev_S_Name +"', " +
			  		 	    "'"+ Dev_S_Attr_Id +"', " +
			  		 	    "'"+ Dev_S_Attr_Name +"', " +
			  		 	    "'"+ Dev_S_Attr_Value +"', " +
			  		 	    "'"+ Dev_D_Id +"', " +
			  		 	    "'"+ Dev_D_Name +"', " +
			  		 	    "'"+ Dev_D_Act_Id +"', " +
			  		 	    "'"+ Dev_D_Act_Name +"', " +
			  		 	    "'"+ Dev_D_Value +"', " +
			  		 	    "date_format('"+ Dev_CTime +"', '%Y-%m-%d %H-%i-%S'), " +
			  		 	    "'"+ Dev_Operator +"', " +
			  		 	    "'"+ Dev_Status +"')";
		if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
		{
			ret = Cmd_Sta.STA_SUCCESS;
		}
		
		//»Ø¸´
		//setStatus(CommUtil.IntToStringLeftFillZero(ret, 4));
		//execResponse();
		return ret;
	}

	public void noticeTimeOut()
	{
		
	}
	@SuppressWarnings("unused")
	private String EncodeSendMsg()
	{
		String ret = null;
		return ret;
	}
	
	@Override
	public void parseReponse(String strResponse){
		// TODO Auto-generated method stub
		this.setStatus(strResponse.substring(20, 24));
	}

	@Override
	public void execResponse(MsgCtrl m_MsgCtrl) {
		// TODO Auto-generated method stub
		String sendStr = EncodeRespMsg();
		if(null != sendStr)
		{
			String key = getReserve();
			m_MsgCtrl.getM_TcpSvr().DisPatch(CmdUtil.COMM_DELIVER, this.getActionSource(), key + sendStr);
		}
	}	

	private String EncodeRespMsg()
	{
		String ret = null;
		ret = getStatus() + getAction();
		return ret;
	}

	public String getDev_S_Id() {
		return Dev_S_Id;
	}

	public void setDev_S_Id(String devSId) {
		Dev_S_Id = devSId;
	}

	public String getDev_S_Name() {
		return Dev_S_Name;
	}

	public void setDev_S_Name(String devSName) {
		Dev_S_Name = devSName;
	}

	public String getDev_S_Attr_Id() {
		return Dev_S_Attr_Id;
	}

	public void setDev_S_Attr_Id(String devSAttrId) {
		Dev_S_Attr_Id = devSAttrId;
	}

	public String getDev_S_Attr_Name() {
		return Dev_S_Attr_Name;
	}

	public void setDev_S_Attr_Name(String devSAttrName) {
		Dev_S_Attr_Name = devSAttrName;
	}

	public String getDev_S_Attr_Value() {
		return Dev_S_Attr_Value;
	}

	public void setDev_S_Attr_Value(String devSAttrValue) {
		Dev_S_Attr_Value = devSAttrValue;
	}

	public String getDev_D_Id() {
		return Dev_D_Id;
	}

	public void setDev_D_Id(String devDId) {
		Dev_D_Id = devDId;
	}

	public String getDev_D_Name() {
		return Dev_D_Name;
	}

	public void setDev_D_Name(String devDName) {
		Dev_D_Name = devDName;
	}

	public String getDev_D_Act_Id() {
		return Dev_D_Act_Id;
	}

	public void setDev_D_Act_Id(String devDActId) {
		Dev_D_Act_Id = devDActId;
	}

	public String getDev_D_Act_Name() {
		return Dev_D_Act_Name;
	}

	public void setDev_D_Act_Name(String devDActName) {
		Dev_D_Act_Name = devDActName;
	}

	public String getDev_D_Value() {
		return Dev_D_Value;
	}

	public void setDev_D_Value(String devDValue) {
		Dev_D_Value = devDValue;
	}

	public String getDev_CTime() {
		return Dev_CTime;
	}

	public void setDev_CTime(String devCTime) {
		Dev_CTime = devCTime;
	}

	public String getDev_Operator() {
		return Dev_Operator;
	}

	public void setDev_Operator(String devOperator) {
		Dev_Operator = devOperator;
	}

	public String getDev_Status() {
		return Dev_Status;
	}

	public void setDev_Status(String devStatus) {
		Dev_Status = devStatus;
	}
}