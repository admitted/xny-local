package bean;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rmi.Rmi;
import rmi.RmiBean;
import util.*;

public class CorpInfoBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_CORP_INFO;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public CorpInfoBean() 
	{ 
		super.className = "CorpInfoBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		switch(currStatus.getCmd())
		{
			case 10://添加
			case 11://修改
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(0, this, 0);
		    case 0://查询
		    	if(null != msgBean.getMsg() && ((ArrayList<?>)msgBean.getMsg()).size() > 0)
				{
					request.getSession().setAttribute("Corp_Info_" + Sid, (CorpInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
				}
		    	currStatus.setJsp("Corp_Info.jsp?Sid=" + Sid);
		    	break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd) 
		{
			case 0://查询
				Sql = " select t.id, t.cname, t.brief, t.contact, t.tel, t.addr, t.dept, t.station_info, t.car_info, t.oil_info, t.rate " +
					  " from corp_info t " +
					  " order by substr(t.id,3,4)";
				break;
			case 10://添加
				Sql = " insert into corp_info(id, cname, brief, contact, tel, addr, dept, station_info, car_info, oil_info, rate)" +
					  " values('"+Id+"', '"+CName+"', '"+Brief+"', '"+Contact+"', '"+Tel+"', '"+Addr+"', '"+Dept+"', '"+Station_Info+"', '"+Car_Info+"', '"+Oil_Info+"', '"+Rate+"')";
				break;
			case 11://修改
				Sql = " update corp_info set id='"+ Id +"', cname = '"+ CName +"', brief = '"+ Brief +"', contact = '"+ Contact +"', tel = '"+ Tel +"', " +
					  " addr = '"+ Addr +"', dept = '"+ Dept +"', station_info = '"+ Station_Info +"', car_info = '"+ Car_Info +"', oil_info = '"+ Oil_Info +"', rate = '"+ Rate +"' ";		  
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
			setContact(pRs.getString(4));
			setTel(pRs.getString(5));
			setAddr(pRs.getString(6));
			setDept(pRs.getString(7));
			setStation_Info(pRs.getString(8));
			setCar_Info(pRs.getString(9));
			setOil_Info(pRs.getString(10));
			setRate(pRs.getString(11));
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
			setContact(CommUtil.StrToGB2312(request.getParameter("Contact")));
			setTel(CommUtil.StrToGB2312(request.getParameter("Tel")));
			setAddr(CommUtil.StrToGB2312(request.getParameter("Addr")));
			setDept(CommUtil.StrToGB2312(request.getParameter("Dept")));
			setStation_Info(CommUtil.StrToGB2312(request.getParameter("Station_Info")));
			setCar_Info(CommUtil.StrToGB2312(request.getParameter("Car_Info")));
			setOil_Info(CommUtil.StrToGB2312(request.getParameter("Oil_Info")));
			setRate(CommUtil.StrToGB2312(request.getParameter("Rate")));
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Id;
	private String CName;
	private String Brief;
	private String Contact;
	private String Tel;
	private String Addr;
	private String Dept;
	private String Station_Info;
	private String Car_Info;
	private String Oil_Info;
	private String Rate;
	private String Sid;
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String cName) {
		CName = cName;
	}

	public String getBrief() {
		return Brief;
	}

	public void setBrief(String brief) {
		Brief = brief;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String addr) {
		Addr = addr;
	}

	public String getDept() {
		return Dept;
	}

	public void setDept(String dept) {
		Dept = dept;
	}

	public String getStation_Info() {
		return Station_Info;
	}

	public void setStation_Info(String stationInfo) {
		Station_Info = stationInfo;
	}

	public String getCar_Info() {
		return Car_Info;
	}

	public void setCar_Info(String carInfo) {
		Car_Info = carInfo;
	}

	public String getOil_Info() {
		return Oil_Info;
	}

	public void setOil_Info(String oilInfo) {
		Oil_Info = oilInfo;
	}

	public String getRate() {
		return Rate;
	}

	public void setRate(String rate) {
		Rate = rate;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}
