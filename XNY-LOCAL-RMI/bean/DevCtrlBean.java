package bean;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import rmi.Rmi;
import rmi.RmiBean;
import util.*;

public class DevCtrlBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_DEV_CTRL;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public DevCtrlBean() 
	{
		super.className = "DevCtrlBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		switch (currStatus.getCmd())
		{
			case 0:// 查询
				request.getSession().setAttribute("Dev_Ctrl_" + Sid, (Object) msgBean.getMsg());
				currStatus.setJsp("Dev_Ctrl.jsp?Sid=" + Sid);
				break;
		}

		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		response.sendRedirect(currStatus.getJsp());
	}
	
	public void Control(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		PrintWriter outprint = response.getWriter();
		String Resp = "3006";
		String data = "";
		//data = "003101000100020001"; //通道1开
		
		data = dev_Type + dev_Id + act_Id;
		System.out.println("data[" + data + "]");
		switch (currStatus.getCmd()) 
		{
				case 0: //查询状态
					
					break;
				case 1:	//远程控制
					Resp = pRmi.Client(3002, cpm_Id, oprator, data);
					break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
		System.out.println(Resp);
	}
	
	public void synchronous(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		PrintWriter outprint = response.getWriter();
		String Resp = "3006";
		switch (currStatus.getCmd()) 
		{
			case 2: // 设备同步
				Resp = pRmi.Client(3003, cpm_Id, oprator, "");
				break;
			case 3:	// 属性同步
				Resp = pRmi.Client(3004, cpm_Id, oprator, "");
				break;
			case 4:	// 动作同步
				Resp = pRmi.Client(3005, cpm_Id, oprator, "");
				break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
		System.out.println(Resp);
	}
	
	
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd) 
		{
				case 0:
					Sql = " select t.cpm_id, t.cpm_brief, t.dev_type, t.dev_id, t.dev_name, t.attr_id, t.attr_name, t.value, t.action " + 
						  " from view_dev_attr t " + 
						  " where instr('"+ currStatus.getFunc_Cpm_Id() +"', t.cpm_id) > 0 ";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setCpm_Id(pRs.getString(1));
			setCpm_Brief(pRs.getString(2));
			setDev_Type(pRs.getString(3));
			setDev_Id(pRs.getString(4));
			setDev_Name(pRs.getString(5));
			setAttr_Id(pRs.getString(6));
			setAttr_Name(pRs.getString(7));
			setValue(pRs.getString(8));
			setAction(pRs.getString(9));
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
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("cpm_Id")));
			setCpm_Brief(CommUtil.StrToGB2312(request.getParameter("cpm_Brief")));
			setDev_Type(CommUtil.StrToGB2312(request.getParameter("dev_Type")));
			setDev_Id(CommUtil.StrToGB2312(request.getParameter("dev_Id")));
			setDev_Name(CommUtil.StrToGB2312(request.getParameter("dev_Name")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("attr_Name")));
			setValue(CommUtil.StrToGB2312(request.getParameter("value")));
			setAction(CommUtil.StrToGB2312(request.getParameter("action")));
			setAct_Id(CommUtil.StrToGB2312(request.getParameter("act_Id")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setOprator(CommUtil.StrToGB2312(request.getParameter("oprator")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Sid;
	
	private String cpm_Id;
	private String cpm_Brief;
	private String dev_Type;
	private String dev_Id;
	private String dev_Name;
	private String attr_Id;
	private String attr_Name;
	private String value;
	private String action;
	
	private String act_Id;
	private String oprator;
	
	public String getAct_Id()
	{
		return act_Id;
	}

	public void setAct_Id(String act_Id)
	{
		this.act_Id = act_Id;
	}

	public String getOprator()
	{
		return oprator;
	}

	public void setOprator(String oprator)
	{
		this.oprator = oprator;
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

	public String getDev_Name()
	{
		return dev_Name;
	}

	public void setDev_Name(String dev_Name)
	{
		this.dev_Name = dev_Name;
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

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getCpm_Brief()
	{
		return cpm_Brief;
	}

	public void setCpm_Brief(String cpm_Brief)
	{
		this.cpm_Brief = cpm_Brief;
	}
	
	public String getCpm_Id()
	{
		return cpm_Id;
	}

	public void setCpm_Id(String cpm_Id)
	{
		this.cpm_Id = cpm_Id;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}
