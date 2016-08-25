package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartUpload;

import rmi.Rmi;
import rmi.RmiBean;
import util.*;

/**
 * 站级CPM
 * 
 * @author cui
 * 
 */
public class DeviceDetailBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_DEVICE_DETAIL;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public DeviceDetailBean()
	{
		super.className = "DeviceDetailBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);

		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		switch (currStatus.getCmd())
		{
			case 12:// 删除
			case 11:// 编辑
			case 10:// 添加
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(0, this, 0);
			case 0:// 查询
				request.getSession().setAttribute("Device_Detail_" + Sid, (Object) msgBean.getMsg());
				currStatus.setJsp("Device_Detail.jsp?Sid=" + Sid);
				break;
			case 1:// 视频监控
				request.getSession().setAttribute("User_Device_Detail_" + Sid, (Object) msgBean.getMsg());
				currStatus.setJsp("Dvr_Info.jsp?Sid=" + Sid);
				break;
		}

		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		response.sendRedirect(currStatus.getJsp());
	}

	// 获取状态RealStatus、doDefence、doRightClick
	public void ToPo(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);

		PrintWriter outprint = response.getWriter();
		String Resp = "9999";

		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		if (msgBean.getStatus() == MsgBean.STA_SUCCESS)
		{
			Resp = ((String) msgBean.getMsg());
		}

		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
	}

	// 地图接口doDragging、doAddMarke、doDel
	public void doDragging(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);

		PrintWriter outprint = response.getWriter();
		String Resp = "9999";

		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		if (msgBean.getStatus() == MsgBean.STA_SUCCESS)
		{
			Resp = "0000";
			msgBean = pRmi.RmiExec(0, this, 0);
			request.getSession().setAttribute("Device_Detail_" + Sid, ((Object) msgBean.getMsg()));
		}

		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
	}


	/**
	 * 图片导入
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @throws ServletException
	 * @throws IOException
	 */
	public void DetailSenceUp(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, ServletConfig pConfig) throws ServletException, IOException
	{
		try
		{
			SmartUpload mySmartUpload = new SmartUpload();
			mySmartUpload.initialize(pConfig, request, response);
			mySmartUpload.setAllowedFilesList("jpg,bmp,JPG,BMP,gif,GIF");
			mySmartUpload.upload();

			Sid = mySmartUpload.getRequest().getParameter("Sid");
			currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
			Id = mySmartUpload.getRequest().getParameter("Id");
			
			if (mySmartUpload.getFiles().getCount() > 0 && mySmartUpload.getFiles().getFile(0).getFilePathName().trim().length() > 0)
			{
				if (mySmartUpload.getFiles().getFile(0).getSize() / 1024 <= 3072)// 最大3M
				{
					String FileSaveRoute = "/www/XNY-LOCAL/XNY-LOCAL-WEB/skin/images/CPM_Scene/";
					// 上传现有文档
					com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(0);
					Scene_Img = Id + "." + myFile.getFileExt();
					myFile.saveAs(FileSaveRoute + Scene_Img);
				}
			}
			msgBean = pRmi.RmiExec(13, this, 0);
			currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));

			currStatus.setJsp("Device_Detail_Scene.jsp?Sid=" + Sid + "&Cpm_Id=" + Id + "&Scene_Img=" + Scene_Img);
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);	
		   	response.sendRedirect(currStatus.getJsp());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0:// 查询
				Sql = " select t.id, t.cname, t.brief, t.status, t.onoff, t.ctype, t.ctime, t.memo, t.link_url, t.link_port, t.link_id, t.link_pwd, t.pwd, t.sign, t.longitude, t.latitude, t.unit_price ,t.scene_img" + " from device_detail t " + " order by t.id ";
				break;
			case 1:// 视频监控
				Sql = " select t.id, t.cname, t.brief, t.status, t.onoff, t.ctype, t.ctime, t.memo, t.link_url, t.link_port, t.link_id, t.link_pwd, t.pwd, t.sign, t.longitude, t.latitude, t.unit_price ,t.scene_img" + " from device_detail t " + " order by t.id ";
				break;
			case 10:// 添加
				Sql = " insert into device_detail(id, cname, brief, status, ctype, ctime, memo, link_url, link_port, link_id, link_pwd, pwd ,unit_price)" + "values('" + Id + "', '" + CName + "', '" + Brief + "', '" + Status + "', '" + CType + "', '" + CTime + "', '" + Memo + "', '" + Link_Url + "', '" + Link_Port + "', '" + Link_Id + "', '" + Link_Pwd + "', '" + Pwd + "', '" + Unit_Price + "')";
				break;
			case 11:// 修改
				Sql = " update device_detail t set t.cname = '" + CName + "', t.brief = '" + Brief + "', t.status = '" + Status + "', t.ctype = '" + CType + "', t.ctime = '" + CTime + "', " + " t.memo = '" + Memo + "', t.link_url = '" + Link_Url + "', t.link_port = '" + Link_Port + "', t.link_id = '" + Link_Id + "', t.link_pwd = '" + Link_Pwd + "', t.pwd = '" + Pwd + "' , t.unit_price = '" + Unit_Price + "' " + " where t.id = '" + Id + "'";
				break;
			case 12:// 删除
				Sql = " delete from device_detail where id = '" + Id + "' ";
				break;
			case 13:// 更新场景图
				Sql = " update device_detail t set t.scene_img = '" + Scene_Img + "' " + " where t.id = '" + Id + "'";
				break;
			case 15:// 地图拖拽同步更新
				Sql = " update device_detail t set t.longitude = '" + Longitude + "', t.latitude = '" + Latitude + "' " + " where t.id = '" + Id + "'";
				break;
			case 16:// 删除标注接口
				Sql = " update device_detail t set t.sign = '0' " + " where t.id = '" + Id + "'";
				break;
			case 17:// 添加标注接口
				Sql = " update device_detail t set t.sign = '1', t.longitude = '" + Longitude + "', t.latitude = '" + Latitude + "' " + " where t.id = '" + Id + "'";
				break;
			case 21:// 获取状态
				Sql = "{? = call Func_Status_Get('" + Id + "')}";
				break;
			case 22:// 获取数据
				Sql = "{? = call Func_Data_Get('" + Id + "', " + CType + ")}";
				break;
			case 23:// 获取未标注企业
				Sql = "{? = call Func_UnMarke_Get('')}";
				break;
			case 24:// GIS实时通知
				Sql = "{? = call Func_News_Get('" + Id + "')}";
				break;
		}
		return Sql;
	}

	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setId(pRs.getString(1));
			setCName(pRs.getString(2));
			setBrief(pRs.getString(3));
			setStatus(pRs.getString(4));
			setOnOff(pRs.getString(5));
			setCType(pRs.getString(6));
			setCTime(pRs.getString(7));
			setMemo(pRs.getString(8));
			setLink_Url(pRs.getString(9));
			setLink_Port(pRs.getString(10));
			setLink_Id(pRs.getString(11));
			setLink_Pwd(pRs.getString(12));
			setPwd(pRs.getString(13));
			setSign(pRs.getString(14));
			setLongitude(pRs.getString(15));
			setLatitude(pRs.getString(16));
			setUnit_Price(pRs.getString(17));
			setScene_Img(pRs.getString(18));
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setBrief(CommUtil.StrToGB2312(request.getParameter("Brief")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setOnOff(CommUtil.StrToGB2312(request.getParameter("OnOff")));
			setCType(CommUtil.StrToGB2312(request.getParameter("CType")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setMemo(CommUtil.StrToGB2312(request.getParameter("Memo")));
			setLink_Url(CommUtil.StrToGB2312(request.getParameter("Link_Url")));
			setLink_Port(CommUtil.StrToGB2312(request.getParameter("Link_Port")));
			setLink_Id(CommUtil.StrToGB2312(request.getParameter("Link_Id")));
			setLink_Pwd(CommUtil.StrToGB2312(request.getParameter("Link_Pwd")));
			setPwd(CommUtil.StrToGB2312(request.getParameter("Pwd")));
			setSign(CommUtil.StrToGB2312(request.getParameter("Sign")));
			setLongitude(CommUtil.StrToGB2312(request.getParameter("Longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("Latitude")));
			setUnit_Price(CommUtil.StrToGB2312(request.getParameter("Unit_Price")));
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setScene_Img(CommUtil.StrToGB2312(request.getParameter("Scene_Img")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String	Id;
	private String	CName;
	private String	Brief;
	private String	Status;
	private String	OnOff;
	private String	CType;
	private String	CTime;
	private String	Memo;
	private String	Link_Url;
	private String	Link_Port;
	private String	Link_Id;
	private String	Link_Pwd;
	private String	Pwd;
	private String	Sign;
	private String	Longitude;
	private String	Latitude;
	private String	Unit_Price;
	private String	Scene_Img;

	private String	Sid;

	public String getScene_Img()
	{
		return Scene_Img;
	}

	public void setScene_Img(String scene_Img)
	{
		Scene_Img = scene_Img;
	}

	public String getUnit_Price()
	{
		return Unit_Price;
	}

	public void setUnit_Price(String unit_Price)
	{
		Unit_Price = unit_Price;
	}

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getCName()
	{
		return CName;
	}

	public void setCName(String cName)
	{
		CName = cName;
	}

	public String getBrief()
	{
		return Brief;
	}

	public void setBrief(String brief)
	{
		Brief = brief;
	}

	public String getStatus()
	{
		return Status;
	}

	public void setStatus(String status)
	{
		Status = status;
	}

	public String getOnOff()
	{
		return OnOff;
	}

	public void setOnOff(String onOff)
	{
		OnOff = onOff;
	}

	public String getCType()
	{
		return CType;
	}

	public void setCType(String cType)
	{
		CType = cType;
	}

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	public String getMemo()
	{
		return Memo;
	}

	public void setMemo(String memo)
	{
		Memo = memo;
	}

	public String getLink_Url()
	{
		return Link_Url;
	}

	public void setLink_Url(String linkUrl)
	{
		Link_Url = linkUrl;
	}

	public String getLink_Port()
	{
		return Link_Port;
	}

	public void setLink_Port(String linkPort)
	{
		Link_Port = linkPort;
	}

	public String getLink_Id()
	{
		return Link_Id;
	}

	public void setLink_Id(String linkId)
	{
		Link_Id = linkId;
	}

	public String getLink_Pwd()
	{
		return Link_Pwd;
	}

	public void setLink_Pwd(String linkPwd)
	{
		Link_Pwd = linkPwd;
	}

	public String getPwd()
	{
		return Pwd;
	}

	public void setPwd(String pwd)
	{
		Pwd = pwd;
	}

	public String getSign()
	{
		return Sign;
	}

	public void setSign(String sign)
	{
		Sign = sign;
	}

	public String getLongitude()
	{
		return Longitude;
	}

	public void setLongitude(String longitude)
	{
		Longitude = longitude;
	}

	public String getLatitude()
	{
		return Latitude;
	}

	public void setLatitude(String latitude)
	{
		Latitude = latitude;
	}

	public String getSid()
	{
		return Sid;
	}

	public void setSid(String sid)
	{
		Sid = sid;
	}
}