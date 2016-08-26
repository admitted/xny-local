package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rmi.Rmi;
import rmi.RmiBean;
import util.*;

public class DataNowBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_DATA_NOW;
	
	/* 获得DataBean的 serialVersionUID (non-Javadoc)
	 * @see rmi.RmiBean#getClassId()
	 */
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public DataNowBean()
	{
		super.className = "DataNowBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);

		switch(currStatus.getCmd())
		{
			case 16:// 删除
			case 15:// 编辑
			case 17:// 添加
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(0, this, 0);
			case 0:// 查询
				request.getSession().setAttribute("Device_Atrr_" + Sid, ((Object)msgBean.getMsg()));
				currStatus.setJsp("Device_Detail_Scene.jsp?Sid=" + Sid + "&Cpm_Id=" + Cpm_Id + "&Scene_Img=" + Scene_Img);
				break;			
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	   
	}
	
	
	/** 获取状态 RealStatus、doDefence、doRightClick
	 * RealStatus:当前状态     doDefence:左点击查看接口    doRightClick:右点击事件
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ToPo(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		System.out.println("["+currStatus.getCmd()+"]");
		PrintWriter outprint = response.getWriter();
		String Resp = "9999";
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
		{
			Resp = ((String)msgBean.getMsg());
		}
		
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
	}



	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{		
			case 0:
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.value, t.unit, t.lev, t.des, t.sign, t.pos_x, t.pos_y " +
				  	  " from view_data_now t " +
				  	  " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
				  	  " order by t.id, t.attr_id , t.ctime desc ";
				break;
			case 15://地图拖拽同步更新
				Sql = " update data_now t set t.pos_x = '"+ Pos_X +"', t.pos_y = '"+ Pos_Y +"' " +
					  " where t.cpm_id = '"+ Cpm_Id +"' and t.id = '"+ Id +"'  and  t.attr_id = '"+ Attr_Id +"' ";
				break;
			case 16://删除标注接口
				Sql = " update data_now t set t.sign = '0' ,t.pos_x = 0 , t.pos_y = 0 " +
					  " where t.cpm_id = '"+ Cpm_Id +"' and t.id = '"+ Id +"'  and  t.attr_id = '"+ Attr_Id +"' ";
				break;
			case 17://添加标注接口
				Sql = " update data_now t set t.sign = '1', t.pos_x = '"+ Pos_X +"', t.pos_y = '"+ Pos_Y +"' " +
					  " where t.cpm_id = '"+ Cpm_Id +"' and t.id = '"+ Id +"'  and  t.attr_id = '"+ Attr_Id +"' ";
				break;
			case 21://获取已标注属性
				Sql = "{? = call Func_Attr_Get('"+ Cpm_Id +"')}";
				break;
			case 23://获取未标注属性
				Sql = "{? = call Func_UnMarke_Attr_Get('"+ Cpm_Id +"')}";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setCpm_Id(pRs.getString(2));
			setCpm_Name(pRs.getString(3));			
			setId(pRs.getString(4));
			setCName(pRs.getString(5));		
			setAttr_Id(pRs.getString(6));
			setAttr_Name(pRs.getString(7));			
			setCTime(pRs.getString(8));
			setValue(pRs.getString(9));
			setUnit(pRs.getString(10));
			setLev(pRs.getString(11));
			setDes(pRs.getString(12));
			setSign(pRs.getString(13));
			setPos_X(pRs.getString(14));
			setPos_Y(pRs.getString(15));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	
	/** 获取request页面 SN、ID、Cpm_Id、Level 等数据
	 * @param request
	 * @return
	 */
	public boolean getHtmlData(HttpServletRequest request) 
	{
		boolean IsOK = true;
		try 
		{	
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));		
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));		
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
			setLev(CommUtil.StrToGB2312(request.getParameter("Lev")));
			setSign(CommUtil.StrToGB2312(request.getParameter("Sign")));
			setPos_X(CommUtil.StrToGB2312(request.getParameter("Pos_X")));
			setPos_Y(CommUtil.StrToGB2312(request.getParameter("Pos_Y")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setLevel(CommUtil.StrToGB2312(request.getParameter("Level")));
			setYear(CommUtil.StrToGB2312(request.getParameter("Year")));
			setMonth(CommUtil.StrToGB2312(request.getParameter("Month")));		
			setFunc_Cpm_Id(CommUtil.StrToGB2312(request.getParameter("Func_Cpm_Id")));
			setScene_Img(CommUtil.StrToGB2312(request.getParameter("Scene_Img")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String SN;
	private String Cpm_Id;
	private String Cpm_Name;
	private String Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String CTime;
	private String Value;
	private String Unit;
	private String Lev;
	private String Des;
	private String Sign;
	private String Pos_X;
	private String Pos_Y;
	private String Scene_Img;
	

	private String Sid;
	private String Level;
	private String Year;
	private String Month;
		
	public String getScene_Img()
	{
		return Scene_Img;
	}

	public void setScene_Img(String scene_Img)
	{
		Scene_Img = scene_Img;
	}

	public String getSign()
	{
		return Sign;
	}

	public void setSign(String sign)
	{
		Sign = sign;
	}

	public String getPos_X()
	{
		return Pos_X;
	}

	public void setPos_X(String pos_X)
	{
		Pos_X = pos_X;
	}

	public String getPos_Y()
	{
		return Pos_Y;
	}

	public void setPos_Y(String pos_Y)
	{
		Pos_Y = pos_Y;
	}

	private String Func_Cpm_Id;
	
	public String getFunc_Cpm_Id() {
		return Func_Cpm_Id;
	}

	public void setFunc_Cpm_Id(String func_Cpm_Id) {
		Func_Cpm_Id = func_Cpm_Id;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getCpm_Id() {
		return Cpm_Id;
	}

	public void setCpm_Id(String cpmId) {
		Cpm_Id = cpmId;
	}

	public String getCpm_Name() {
		return Cpm_Name;
	}

	public void setCpm_Name(String cpmName) {
		Cpm_Name = cpmName;
	}

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

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getLev() {
		return Lev;
	}

	public void setLev(String lev) {
		Lev = lev;
	}

	public String getDes() {
		return Des;
	}

	public void setDes(String des) {
		Des = des;
	}
	
	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getMonth() {
		return Month;
	}

	public void setMonth(String month) {
		Month = month;
	}
}