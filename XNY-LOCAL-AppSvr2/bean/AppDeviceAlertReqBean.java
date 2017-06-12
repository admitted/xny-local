package bean;

import java.io.UnsupportedEncodingException;

import net.MsgCtrl;
import bean.BaseCmdBean;
import util.CmdUtil;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;
public class AppDeviceAlertReqBean extends BaseCmdBean {	
	
	private String Dev_Id = "";
	private String Dev_Name = "";
	private String Attr_Id = "";
	private String Attr_Name = "";
	private String Level = "";
	private String CTime = "";
	private String CData = "";
	
	public AppDeviceAlertReqBean(int action, String seq) {
		super(action, seq);
		setBeanName("AppDeviceAlertReqBean");
	}

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData) {
		// TODO Auto-generated method stub
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
		this.setAction(Integer.parseInt(strRequest.substring(24, 28)));
		try
		{
			Dev_Id 		= new String(strData, 68, 10).trim();
			Dev_Name 	= new String(strData, 78, 30, "GB2312").trim();
			Attr_Id 	= new String(strData, 108, 4).trim();
			Attr_Name 	= new String(strData, 112, 20, "GB2312").trim();
			Level 		= new String(strData, 132, 1).trim();
			CTime 		= new String(strData, 133, 20).trim();
			CData 		= new String(strData, 153, 128, "GB2312").trim();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Dev_Id = CommUtil.BSubstring(strRequest, 28, 10).trim();
//		Dev_Name = CommUtil.BSubstring(strRequest, 38, 30).trim();
//		Attr_Id = CommUtil.BSubstring(strRequest, 68, 4).trim();
//		Attr_Name = CommUtil.BSubstring(strRequest, 72, 20).trim();
//		Level = CommUtil.BSubstring(strRequest, 92, 1).trim();
//		CTime = CommUtil.BSubstring(strRequest, 93, 20).trim();
//		CData = CommUtil.BSubstring(strRequest, 113, 128).trim();
	}

	@Override
	public int execRequest(MsgCtrl m_MsgCtrl)
	{
		// TODO Auto-generated method stub
		int ret = Cmd_Sta.STA_ERROR;
		
		//ִ��
		/*
		  ע:level(1,2,3,4,5,6,7)
		 1:CPM�豸���ߣ�Ҫ����level=2��AppSvr�ﴦ��
		 2:CPM���ݸ澯���վ�
		 3:CPM�豸�ָ����ߣ���¼����⣬�����豸���ߵĻָ�ʱ�估״̬��AppSvr�ﴦ��
		 4:�ֹ�˾�������ߣ�Ҫ����level=1��AppSvr�ﴦ��
		 5:�ֹ�˾���ػָ����ߣ���¼����⣬���·ֹ�˾�������ߵĻָ�ʱ�估״̬��AppSvr�ﴦ��
		 6:CPM�������ߣ�Ҫ����level=1��AppSvr�ﴦ��
		 7:CPM���ػָ����ߣ���¼����⣬����CPM�������ߵĻָ�ʱ�估״̬��AppSvr�ﴦ��
		*/
		
		String Sql = "";
		switch(Integer.parseInt(Level))
		{
			case 1://CPM�豸����
			{
				Sql = "insert into alert_info(cpm_id, id, cname, attr_id, attr_name, level, ctime, cdata)" +
				  	  "values('"+ this.getActionSource().trim() +"', " +
				         	 "'"+ Dev_Id +"', " +
				         	 "'"+ Dev_Name +"', " +
				         	 "'"+ Attr_Id +"', " +
				         	 "'"+ Attr_Name +"', " +
				         	 "'2', " +
				         	 "date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), " +
				         	 "'"+ CData +"')";
				if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}
				break;
			}
			case 2://CPM���ݸ澯
			{
				if(null != CData && CData.length() > 0)
				{
					String L_Lev = "";
					String L_Des = CData;
					if(CData.contains(":"))
					{
						L_Lev = CData.substring(0, CData.indexOf(":"));
					    L_Des = CData.substring(CData.indexOf(":")+1);
						if(L_Lev.length() > 4)
						{
							L_Lev = "";
							L_Des = CData;
						}
					}
					
					//data�����
					Sql = "update data t set t.lev = '"+L_Lev+"', t.des = '"+L_Des+"' " +
						  "where t.cpm_id = '"+ this.getActionSource().trim() +"' " +
						  "  and t.id = '"+ Dev_Id +"' " +
						  "  and t.attr_id = '"+ Attr_Id +"' " +
						  "  and t.ctime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S') ";
					m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
					
					//data_now�����
					Sql = "update data_now t set t.lev = '"+L_Lev+"', t.des = '"+L_Des+"' " +
					  	  "where t.cpm_id = '"+ this.getActionSource().trim() +"' " +
					  	  "  and t.id = '"+ Dev_Id +"' " +
					  	  "  and t.attr_id = '"+ Attr_Id +"' " +
					  	  "  and t.ctime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S') ";
					m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
					
					//alert_info������ָ��澯
					if(L_Lev.equalsIgnoreCase("N"))
					{
						Sql = "update alert_info t set t.status = '2', t.etime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), t.operator = 'AUTO' " +
							  "where t.status = '0' " +
							  "  and t.level = '2' " +
							  "  and t.cpm_id = '"+ this.getActionSource().trim() +"' " +
							  "  and t.id = '"+ Dev_Id +"' " +
							  "  and t.attr_id = '"+ Attr_Id +"'";
						m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
						
						Sql = "update alert_now t set t.status = '2', t.etime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), t.operator = 'AUTO' " +
						  	  "where t.status = '0' " +
						  	  "  and t.level = '2' " +
						  	  "  and t.cpm_id = '"+ this.getActionSource().trim() +"' " +
						  	  "  and t.id = '"+ Dev_Id +"' " +
						  	  "  and t.attr_id = '"+ Attr_Id +"'";
						if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
						{
							ret = Cmd_Sta.STA_SUCCESS;
						}
					}
					else
					{
						Sql = "insert into alert_info(cpm_id, id, cname, attr_id, attr_name, level, ctime, cdata, lev)" +
					  	  	  "values('"+ this.getActionSource().trim() +"', " +
					         	     "'"+ Dev_Id +"', " +
					         	     "'"+ Dev_Name +"', " +
					         	     "'"+ Attr_Id +"', " +
					         	     "'"+ Attr_Name +"', " +
					         	     "'2', " +
					         	     "date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), " +
					         	     "'"+ L_Des +"'," +
					         	     "'"+ L_Lev +"')";
						if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
						{
							ret = Cmd_Sta.STA_SUCCESS;
						}
					}
				}
				else
				{
					//alert_info�����
					Sql = "insert into alert_info(cpm_id, id, cname, attr_id, attr_name, level, ctime, cdata)" +
				  	  	  "values('"+ this.getActionSource().trim() +"', " +
				         	 	 "'"+ Dev_Id +"', " +
				         	 	 "'"+ Dev_Name +"', " +
				         	 	 "'"+ Attr_Id +"', " +
				         	 	 "'"+ Attr_Name +"', " +
				         	 	 "'2', " +
				         	 	 "date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), " +
				         	 	 "'"+ CData +"')";
					if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
					{
						ret = Cmd_Sta.STA_SUCCESS;
					}
				}
				break;
			}
			case 3://CPM�豸�ָ�����
			{
				Sql = "update alert_info t set t.status = '2', t.etime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), t.operator = 'AUTO' " +
					  "where t.status = '0' " +
					  "  and t.level = '2' " +
					  "  and length(t.attr_id) < 1 " +
					  "  and t.cpm_id = '"+ this.getActionSource().trim() +"' " +
					  "  and t.id = '"+ Dev_Id +"' ";
				m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
				
				Sql = "update alert_now t set t.status = '2', t.etime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), t.operator = 'AUTO' " +
				  	  "where t.status = '0' " +
				  	  "  and t.level = '2' " +
				  	  "  and length(t.attr_id) < 1 " +
				  	  "  and t.cpm_id = '"+ this.getActionSource().trim() +"' " +
				  	  "  and t.id = '"+ Dev_Id +"' ";
				if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}
				break;
			}
			case 6://CPM��������
			{
				//״̬����
				Sql = "update device_detail t set t.onoff = '1' where t.id = '"+ this.getActionSource().trim() +"'";
				m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
				
				//���ݲ���
				Sql = "insert into alert_info(cpm_id, id, cname, attr_id, attr_name, level, ctime, cdata)" +
			  	  	  "values('"+ this.getActionSource().trim() +"', " +
			         	 	 "'"+ Dev_Id +"', " +
			         	 	 "'"+ Dev_Name +"', " +
			         	 	 "'"+ Attr_Id +"', " +
			         	 	 "'"+ Attr_Name +"', " +
			         	 	 "'1', " +
			         	 	 "date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), " +
			         	 	 "'"+ CData +"')";
				if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}
				break;
			}
			case 7://CPM���ػָ�����
			{
				Sql = "update device_detail t set t.onoff = '0' where t.id = '"+ this.getActionSource().trim() +"'";
				m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
				
				//���ݲ���
				Sql = "update alert_info t set t.status = '2', t.etime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), t.operator = 'AUTO' " +
				  	  "where t.status = '0' " +
				  	  "  and t.level = '1' " +
				  	  "  and length(t.id) < 1 " +
				  	  "  and length(t.attr_id) < 1 " +
				  	  "  and t.cpm_id = '"+ this.getActionSource().trim() +"' ";
				m_MsgCtrl.getM_DBUtil().doUpdate(Sql);
				
				Sql = "update alert_now t set t.status = '2', t.etime = date_format('"+ CTime +"', '%Y-%m-%d %H-%i-%S'), t.operator = 'AUTO' " +
			  	  	  "where t.status = '0' " +
			  	  	  "  and t.level = '1' " +
			  	  	  "  and length(t.id) < 1 " +
			  	  	  "  and length(t.attr_id) < 1 " +
			  	  	  "  and t.cpm_id = '"+ this.getActionSource().trim() +"' ";
				if(m_MsgCtrl.getM_DBUtil().doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}
				break;
			}
		}
		
		//�ظ�
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

	public String getAttr_Id() {
		return Attr_Id;
	}

	public void setAttr_Id(String attrId) {
		Attr_Id = attrId;
	}

	public String getAttr_Name() {
		return Attr_Name;
	}

	public void setAttr_Name(String attrName) {
		Attr_Name = attrName;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getCData() {
		return CData;
	}

	public void setCData(String cData) {
		CData = cData;
	}
}