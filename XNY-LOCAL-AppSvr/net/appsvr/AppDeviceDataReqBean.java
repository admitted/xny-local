package net.appsvr;


import bean.BaseCmdBean;
import util.CmdUtil;
import util.Cmd_Sta;
import util.CommUtil;
import util.DBUtil;

public class AppDeviceDataReqBean extends BaseCmdBean
{

	private String	Dev_Id			= "";
	private String	Dev_Name		= "";
	private String	Dev_Attr_Id		= "";
	private String	Dev_Attr_Name	= "";
	private String	Dev_CTime		= "";
	private String  Dev_CData       = "";
	private String	Dev_Unit		= "";

	public AppDeviceDataReqBean(int action, String seq, DBUtil dbUtil)
	{
		super(action, seq, dbUtil);
	}

	@Override
	public void parseReqest(String srcKey, String strRequest, byte[] strData)
	{
		// TODO Auto-generated method stub
		this.setActionSource(srcKey);
		this.setReserve(strRequest.substring(0, 20));
		this.setAction(Integer.parseInt(strRequest.substring(24, 28)));
		Dev_Id        = CommUtil.BSubstring(strRequest, 28, 10).trim();
		Dev_Name      = CommUtil.BSubstring(strRequest, 38, 30).trim();
		Dev_Attr_Id   = CommUtil.BSubstring(strRequest, 68, 4).trim();
		Dev_Attr_Name = CommUtil.BSubstring(strRequest, 72, 20).trim();
		Dev_CTime     = CommUtil.BSubstring(strRequest, 92, 20).trim();
        Dev_CData     = CommUtil.BSubstring(strRequest, 112, 128).trim();
		Dev_Unit      = CommUtil.BSubstring(strRequest, 240, 10).trim();
	}

	@Override
	public int execRequest()
	{
		// TODO Auto-generated method stub
		int ret = Cmd_Sta.STA_ERROR;
		try
		{
			/************************** 瑞烨法兰    斯密特流量计 **************************/
			if ((Dev_Id.substring(0, 6) + Dev_Attr_Id).equals(Cmd_Sta.DATA_1011_28))
			{
				// 数据解析  421DF247 42D40000 00000000 00000000 44210C29 42B27EE4
				//         温度           压力           脉冲频率    标况流量    累计百上    累计百下
//				float Temperature     = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(0,8), 16));
//				float Stress          = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(8,16), 16));
//				float Standard_Flow   = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(24,32), 16))  * 3600;
				float Cumulative_High = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(32,36) + "0000", 16));
				float Cumulative_Low  = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(40,48), 16));
				float Cumulative      = Cumulative_High * 100 + Cumulative_Low; 
//				System.out.println("Temperature:" + Temperature);
//				System.out.println("Stress:" + Stress);
//				System.out.println("Standard_Flow:" + Standard_Flow);
				System.out.println("Cumulative:" + Cumulative);

				String Sql = "insert into data(cpm_id, id, cname, attr_id, attr_name, ctime, value, unit)" + "values('" + this.getActionSource().trim() + "', " + "'" + Dev_Id + "', " + "'" + Dev_Name + "', " + "'" + Dev_Attr_Id + "', " + "'" + Dev_Attr_Name + "', " + "date_format('" + Dev_CTime + "', '%Y-%m-%d %H-%i-%S'), " + "'" + Cumulative + "', " + "'" + Dev_Unit + "')";
//				System.out.println("BSql-1[" + Sql + "]");
				
				if (m_DbUtil.doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}

			}
			/************************** 伊莱特    天信流量计 **************************/
			else if ((Dev_Id.substring(0, 6) + Dev_Attr_Id).equals(Cmd_Sta.DATA_1011_29))
			{
				// 数据解析  421DF247 42D40000 00000000 00000000 44210C29 42B27EE4
				//         累计高位    累计地位    标况流量    工况流量    温度           压力         
				float Cumulative_High = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(0,8), 16));
				float Cumulative_Low  = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(8,16), 16));
//				float Standard_Flow   = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(16,24), 16));
//				float Temperature     = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(32,40), 16));
//				float Stress          = Float.intBitsToFloat(Integer.parseInt(Dev_CData.substring(40,48), 16));
				
				float Cumulative      = Cumulative_High * 1000000 + Cumulative_Low; 

				System.out.println("Cumulative:" + Cumulative);
//				System.out.println("Standard_Flow:" + Standard_Flow);
//				System.out.println("Temperature:" + Temperature);
//				System.out.println("Stress:" + Stress);

				String Sql = "insert into data(cpm_id, id, cname, attr_id, attr_name, ctime, value, unit)" + "values('" + this.getActionSource().trim() + "', " + "'" + Dev_Id + "', " + "'" + Dev_Name + "', " + "'" + Dev_Attr_Id + "', " + "'" + Dev_Attr_Name + "', " + "date_format('" + Dev_CTime + "', '%Y-%m-%d %H-%i-%S'), " + "'" + Cumulative + "', " + "'" + Dev_Unit + "')";
//				System.out.println("BSql-2[" + Sql + "]");
				
				if (m_DbUtil.doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}

			}
			/************************** 普通环境数据上传 **************************/
			else if (!Dev_CData.equalsIgnoreCase("NULL") && Dev_CData.length() > 0)
			{
				String Sql = "insert into data(cpm_id, id, cname, attr_id, attr_name, ctime, value, unit)" + "values('" + this.getActionSource().trim() + "', " + "'" + Dev_Id + "', " + "'" + Dev_Name + "', " + "'" + Dev_Attr_Id + "', " + "'" + Dev_Attr_Name + "', " + "date_format('" + Dev_CTime + "', '%Y-%m-%d %H-%i-%S'), " + "'" + Dev_CData.toString() + "', " + "'" + Dev_Unit + "')";
//				System.out.println("BSql-3[" + Sql + "]");
				if (m_DbUtil.doUpdate(Sql))
				{
					ret = Cmd_Sta.STA_SUCCESS;
				}

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		// 回复
		// setStatus(CommUtil.IntToStringLeftFillZero(ret, 4));
		// execResponse();
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
	public void parseReponse(String strResponse)
	{
		// TODO Auto-generated method stub
		this.setStatus(strResponse.substring(20, 24));
	}

	@Override
	public void execResponse()
	{
		// TODO Auto-generated method stub
		String sendStr = EncodeRespMsg();
		if (null != sendStr)
		{
			String key = getReserve();
			TcpSvrAppGateWay.DisPatch(CmdUtil.COMM_DELIVER, this.getActionSource(), key + sendStr);
		}
	}

	private String EncodeRespMsg()
	{
		String ret = null;
		ret = getStatus() + getAction();
		return ret;
	}

	public String getDev_Id()
	{
		return Dev_Id;
	}

	public void setDev_Id(String devId)
	{
		Dev_Id = devId;
	}

	public String getDev_Name()
	{
		return Dev_Name;
	}

	public void setDev_Name(String devName)
	{
		Dev_Name = devName;
	}

	public String getDev_Attr_Id()
	{
		return Dev_Attr_Id;
	}

	public void setDev_Attr_Id(String devAttrId)
	{
		Dev_Attr_Id = devAttrId;
	}

	public String getDev_Attr_Name()
	{
		return Dev_Attr_Name;
	}

	public void setDev_Attr_Name(String devAttrName)
	{
		Dev_Attr_Name = devAttrName;
	}

	public String getDev_CTime()
	{
		return Dev_CTime;
	}

	public void setDev_CTime(String devCTime)
	{
		Dev_CTime = devCTime;
	}

	public String getDev_CData()
	{
		return Dev_CData;
	}

	public void setDev_CData(String dev_CData)
	{
		Dev_CData = dev_CData;
	}

	public String getDev_Unit()
	{
		return Dev_Unit;
	}

	public void setDev_Unit(String devUnit)
	{
		Dev_Unit = devUnit;
	}
}