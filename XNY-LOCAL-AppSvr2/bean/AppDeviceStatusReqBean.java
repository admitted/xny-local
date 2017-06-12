package bean;

import java.io.UnsupportedEncodingException;

import net.MsgCtrl;
import bean.BaseCmdBean;
import util.CmdUtil;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
public class AppDeviceStatusReqBean extends BaseCmdBean {	
	
	private String Dev_Id = "";
	private String Dev_Name = "";
	private String Dev_Status = "";
	
	public AppDeviceStatusReqBean(int action, String seq) {
		super(action, seq);
		setBeanName("AppDeviceStatusReqBean");
	}

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData) {
		// TODO Auto-generated method stub
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
		this.setAction(Integer.parseInt(strRequest.substring(24, 28)));
		try
		{
			Dev_Id = new String(strData, 68, 10).trim();
			Dev_Name = new String(strData, 78, 30, "GB2312").trim();
			Dev_Status = new String(strData, 108, 1).trim();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Dev_Id = CommUtil.BSubstring(strRequest, 28, 10).trim();
//		Dev_Name = CommUtil.BSubstring(strRequest, 38, 30).trim();
//		Dev_Status = CommUtil.BSubstring(strRequest, 68, 1).trim();
	}

	@Override
	public int execRequest(MsgCtrl m_MsgCtrl)
	{
		// TODO Auto-generated method stub
		int ret = Cmd_Sta.STA_ERROR;
		
		//Ö´ÐÐ
		String Sql = "update device_detail set cname = cname ";
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

	public String getDev_Id() {
		return Dev_Id;
	}

	public void setDev_Id(String devId) {
		Dev_Id = devId;
	}

	public String getDev_Name() {
		return Dev_Name;
	}

	public void setDev_Name(String devName) {
		Dev_Name = devName;
	}

	public String getDev_Status() {
		return Dev_Status;
	}

	public void setDev_Status(String devStatus) {
		Dev_Status = devStatus;
	}
}