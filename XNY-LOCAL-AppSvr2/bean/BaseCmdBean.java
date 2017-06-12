package bean;

import net.MsgCtrl;
import util.*;

public abstract class BaseCmdBean
{
	public static long m_SessionId = (new java.util.Date().getTime()/1000);
	private String actionSource = "";
	private String Reserve = "";
	private String Status = "0000";
	private int Action = 0;	
	private int TestTime = (int)(new java.util.Date().getTime()/1000);
	private String Seq = "";
	public DBUtil m_DbUtil = null;
	private String BeanName = "";

	public BaseCmdBean(int action, String seq){
		Action = action;
		Seq = seq;
	}
	
	public static BaseCmdBean getBean(int Cmd, String Seq)
	{
		BaseCmdBean retBean = null;
		switch(Cmd)
		{
			case Cmd_Sta.CMD_SUBMIT_1000:  //1000
				retBean = new AppDeviceStatusReqBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_SUBMIT_1001:  //1001 包含 集合数据解析
				retBean = new AppDeviceDataReqBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_SUBMIT_1003:  //1003
				retBean = new AppDeviceAlarmReqBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_SUBMIT_1004:  //1004
				retBean = new AppDeviceAlertReqBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_SUBMIT_1006:  //1006
				retBean = new DeviceListBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_SUBMIT_1007:  //1007
				retBean = new DeviceAttrBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_SUBMIT_1008:  //1008
				retBean = new DeviceActBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_DEVICE_CTRL:  //3002 动作控制
				retBean = new DeviceControlBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_DEVICE_3003:  //3003 设备同步
				retBean = new DeviceControlBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_DEVICE_3004:  //3004 属性同步
				retBean = new DeviceControlBean(Cmd, SessionId());
				break;
			case Cmd_Sta.CMD_DEVICE_3005:  //3005 动作同步
				retBean = new DeviceControlBean(Cmd, SessionId());
				break;
		}
		return retBean;
	}
	public static synchronized String SessionId()
	{
		long ret = m_SessionId++;
		return CommUtil.LongToStringLeftFillZero(ret, 20);
	}
	public String GetSessionId()
	{
		return Seq;
	}
	public abstract void parseReqest(String key, String strRequest, byte[] strData);
	public abstract int execRequest(MsgCtrl msgCtrl);

	public abstract void parseReponse(String strResponse);
	public abstract void execResponse(MsgCtrl msgCtrl);
	
	public abstract void noticeTimeOut();
	public String getActionSource() {
		return actionSource;
	}

	public void setActionSource(String actionSource) {
		this.actionSource = actionSource;
	}

	public String getReserve() {
		return Reserve;
	}

	public void setReserve(String reserve) {
		Reserve = reserve;
	}

	public int getAction() {
		return Action;
	}

	public void setAction(int action) {
		Action = action;
	}

	public int getTestTime() {
		return TestTime;
	}

	public void setTestTime(int testTime) {
		TestTime = testTime;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSeq() {
		return Seq;
	}

	public void setSeq(String seq) {
		Seq = seq;
	}

	public int execRequest()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public String getBeanName() {
		return BeanName;
	}

	public void setBeanName(String beanName) {
		BeanName = beanName;
	}
}
