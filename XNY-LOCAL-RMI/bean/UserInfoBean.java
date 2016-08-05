package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.CurrStatus;
import util.MsgBean;

public class UserInfoBean extends RmiBean 
{
	public final static long serialVersionUID =RmiBean.RMI_USER_INFO;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public UserInfoBean()
	{
		super.className = "UserInfoBean";
	}
	
	//登入
	public void Login(HttpServletRequest request, HttpServletResponse response, Rmi pRmi)
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			if(null == currStatus)
				currStatus = new CurrStatus();
			currStatus.getHtmlData(request, false);
			
			String Url = "index.jsp";
			msgBean = pRmi.RmiExec(21, this, 0);
			if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
			{
				if(StrMd5.substring(0,20).trim().equalsIgnoreCase("system"))
				{
					
				}
				else if(StrMd5.substring(0,20).trim().equalsIgnoreCase("admin"))
				{
					//登入信息
					msgBean = pRmi.RmiExec(0, this, 0);
					request.getSession().setAttribute("Admin_" + Sid, (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					
					//基础信息
					CorpInfoBean corpInfoBean = new CorpInfoBean();
					msgBean = pRmi.RmiExec(0, corpInfoBean, 0);
					if(null != msgBean.getMsg() && ((ArrayList<?>)msgBean.getMsg()).size() > 0)
					{
						request.getSession().setAttribute("Corp_Info_" + Sid, (CorpInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					}
					
					//设备配置
					DeviceDetailBean deviceDetailBean = new DeviceDetailBean();
					msgBean = pRmi.RmiExec(0, deviceDetailBean, 0);
					request.getSession().setAttribute("Device_Detail_" + Sid, (Object)msgBean.getMsg());
					
					Url = "admin/Main.jsp?Sid=" + Sid;
				}
				else
				{
					//登入信息  某个人的登录信息 UserInfo_
					msgBean = pRmi.RmiExec(0, this, 0);
					request.getSession().setAttribute("UserInfo_" + Sid, (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					
					//基础信息
					CorpInfoBean corpInfoBean = new CorpInfoBean();
					msgBean = pRmi.RmiExec(0, corpInfoBean, 0);
					if(null != msgBean.getMsg() && ((ArrayList<?>)msgBean.getMsg()).size() > 0)
					{
						request.getSession().setAttribute("User_Corp_Info_" + Sid, (CorpInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					}
					
					//设备配置
					DeviceDetailBean deviceDetailBean = new DeviceDetailBean();
					msgBean = pRmi.RmiExec(0, deviceDetailBean, 0);
					request.getSession().setAttribute("User_Device_Detail_" + Sid, (Object)msgBean.getMsg());	
					
					//人员信息
			    	msgBean = pRmi.RmiExec(4, this, 0);
					request.getSession().setAttribute("User_User_Info_" + Sid, (Object)msgBean.getMsg());
					
					//所有功能权限
					UserRoleBean roleBean = new UserRoleBean();
			    	msgBean = pRmi.RmiExec(0, roleBean, 0);
			    	request.getSession().setAttribute("User_FP_Role_" + Sid, ((Object)msgBean.getMsg()));
			    	
			    	//所有管理权限 
			    	msgBean = pRmi.RmiExec(1, roleBean, 0);
			    	request.getSession().setAttribute("User_Manage_Role_" + Sid, ((Object)msgBean.getMsg()));
					
					Url = "user/MapMain.jsp?Sid=" + Sid;
				}
			}
			else
			{
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
			response.sendRedirect(Url);
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
	}
	
	//密码修改
	public void PwdEdit(HttpServletRequest request, HttpServletResponse response, Rmi pRmi)
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request,false);
			
			PrintWriter outprint = response.getWriter();
			String Resp = "9999";
			
			int pCmd = currStatus.getCmd();
			switch(currStatus.getCmd())
			{
				case 12://user 个人信息
					pCmd = currStatus.getCmd();
					break;
				case 22://user 密码修改
					pCmd = currStatus.getCmd();
					break;
				case 23://admin 密码修改
					pCmd = 22;
					break;
				case 24://admin 密码重置
					pCmd = 22;
					break;
			}
			
			msgBean = pRmi.RmiExec(pCmd, this, 0);
			switch(msgBean.getStatus())
			{
				case 0://成功
					Resp = "0000";
					break;
				case 1001://用户不存在或密码错误
					Resp = "1001";
					break;
				default://失败
					Resp = "9999";
					break;
			}
			
			switch(currStatus.getCmd())
			{
				case 12://user 个人信息
				case 22://user 密码修改
					msgBean = pRmi.RmiExec(2, this, 0);
					request.getSession().setAttribute("UserInfo_" + Sid, (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					break;
				case 23://admin 密码修改
					msgBean = pRmi.RmiExec(2, this, 0);
					request.getSession().setAttribute("Admin_" + Sid, (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					break;
				case 24://admin 密码重置
					break;
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
			outprint.write(Resp);
		} 
		catch (Exception Ex)
		{
			Ex.printStackTrace();
		}
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		//部门
		Func_Corp_Id = currStatus.getFunc_Corp_Id();
		if(Func_Corp_Id.equals("99") || Func_Corp_Id.equals("9999999999"))
		{
			Func_Corp_Id = "";
		}
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		switch(currStatus.getCmd())
		{
			case 10://添加
			case 11://修改
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				switch(currStatus.getFunc_Corp_Id().length())
				{
					case 2:
						msgBean = pRmi.RmiExec(1, this, 0);
						request.getSession().setAttribute("User_Info_" + Sid, ((Object)msgBean.getMsg()));
				    	currStatus.setJsp("User_Info.jsp?Sid=" + Sid);
						break;
					case 10:
						msgBean = pRmi.RmiExec(3, this, 0);
						request.getSession().setAttribute("User_Stat_" + Sid, ((Object)msgBean.getMsg()));
				    	currStatus.setJsp("User_Stat.jsp?Sid=" + Sid);
						break;
				}
				break;
		    case 1://公司人员
		    	request.getSession().setAttribute("User_Info_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("User_Info.jsp?Sid=" + Sid);
		    	break;
		    case 3://站级人员
		    	System.out.println(Func_Type_Id);
		    	if(Func_Type_Id.equals("333"))
		    	{
		    		msgBean = pRmi.RmiExec(13, this, 0);
		    	}
		    	msgBean = pRmi.RmiExec(3, this, 0);
		    	request.getSession().setAttribute("User_Stat_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("User_Stat.jsp?Sid=" + Sid);
		    	break;
		}
		
		//功能权限
    	UserRoleBean roleBean = new UserRoleBean();
    	msgBean = pRmi.RmiExec(0, roleBean, 0);
    	request.getSession().setAttribute("FP_Role_" + Sid, ((Object)msgBean.getMsg()));
    	
    	//管理权限
    	msgBean = pRmi.RmiExec(1, roleBean, 0);
    	request.getSession().setAttribute("Manage_Role_" + Sid, ((Object)msgBean.getMsg()));
    	
    	//岗位管理
    	UserPositionBean positionBean = new UserPositionBean();
    	msgBean = pRmi.RmiExec(0, positionBean, 0);
    	request.getSession().setAttribute("User_Position_" + Sid, ((Object)msgBean.getMsg()));
    	
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	}
	
	//帐号检测
	public void IdCheck(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone)
	{
		try 
		{
			getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			
			PrintWriter outprint = response.getWriter();
			String Resp = "3006";
			
			msgBean = pRmi.RmiExec(2, this, 0);//查找是否有该用户存在
			switch(msgBean.getStatus())
			{
				case 0://已存在
					Resp = "3006";
					break;
				default://可用
					Resp = "0000";
					break;
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
			outprint.write(Resp);
		}
		catch (Exception Ex)
		{
			Ex.printStackTrace();
		}
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://登陆信息
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
					  " from user_info " +
					  " where upper(Id) = '"+ StrMd5.substring(0,20).trim() +"' ";
				break;
			case 1://公司人员
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
					  " from user_info " +
					  " where Dept_Id like '"+ Func_Corp_Id +"%' " +
					  "   and length(Dept_Id) = 2 " +
					  "   and Id <> 'system' and Id <> 'admin' order by Sys_Id asc";
				break;
			case 3://站级人员
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
					  " from user_info " +
					  " where Dept_Id like '"+ Func_Corp_Id +"%' " +
					  " and length(Dept_Id) = 10 " +
					  " and CName like '"+ CName +"%' " +
					  " and Id <> 'system' and Id <> 'admin' order by Sys_Id asc";
				break;
			case 4://全部人员
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
				  	  " from user_info " +
				  	  " where Id <> 'system' and Id <> 'admin' order by Sys_Id asc";
				break;
			case 5://根据部门查询人员
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
					  " from user_info t" +
					  " where instr('"+ Func_Cpm_Id +"', t.Sys_Id) > 0 " +
					  " and Dept_Id = '"+ Func_Type_Id +"' " ;
				break;
			case 6:
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
					  " from user_info t" +
					  " where instr('"+ Func_Cpm_Id +"', t.Sys_Id) > 0 " +
					  " and Dept_Id like '"+ Func_Type_Id +"%' " ;
				break;
			case 2://帐号检测
				Sql = " select Id, CName, Sex, Birthday, Addr, Tel, Status, Pwd, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, Sys_Id, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort " +
					  " from user_info " +
					  " where upper(Id) = upper('"+ Id +"') ";
				break;
			case 10://员工添加
				Sql = " insert into user_info(Id, CName, Sex, Birthday, Addr, Tel, Status, Dept_Id, Manage_Role, Job_Id, Job_Position, Fp_Role, HP_LoginId, HP_LoginPwd, HP_LoginIp, HP_LoginPort)" +
					  " values('"+Id+"', '"+CName+"', '"+Sex+"', '"+Birthday+"', '"+Addr+"', '"+Tel+"', '"+Status+"', '"+Dept_Id+"', '"+Manage_Role+"', '"+Job_Id+"', '"+Job_Position+"', '"+Fp_Role+"', '"+HP_LoginId+"', '"+HP_LoginPwd+"', '"+HP_LoginIp+"', '"+HP_LoginPort+"')";
				break;
			case 11://员工修改
				Sql = " update user_info set cname = '"+CName+"', sex = '"+Sex+"', birthday = '"+Birthday+"', addr = '"+Addr+"', tel = '"+Tel+"', status = '"+Status+"', " +
					  " dept_id = '"+Dept_Id+"', manage_role = '"+Manage_Role+"', job_id = '"+Job_Id+"', job_position = '"+Job_Position+"', fp_role = '"+Fp_Role+"', HP_LoginId = '"+HP_LoginId+"', HP_LoginPwd = '"+HP_LoginPwd+"', HP_LoginIp = '"+HP_LoginIp+"', HP_LoginPort = '"+HP_LoginPort+"' " +
					  " where id = '"+Id+"' ";
				break;
			case 12://个人修改
				Sql = " update user_info set cname = '"+CName+"', sex = '"+Sex+"', birthday = '"+Birthday+"', addr = '"+Addr+"', tel = '"+Tel+"' " +
				  	  " where id = '"+Id+"' ";
				break;
			case 13:
				Sql = "delete from user_info where id = '"+ Id +"'";
				break;
			case 21://登录验证
				Sql = "{? = call RMI_LOGIN('"+StrMd5+"', '"+currStatus.getCheckCode()+"')}";
				break;
			case 22://密码修改
				Sql = "{? = call RMI_PWD_EDIT('"+getId()+"','" + getPwd() +"', '"+getNewPwd()+"')}";
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
			setSex(pRs.getString(3));
			setBirthday(pRs.getString(4));
			setAddr(pRs.getString(5));
			setTel(pRs.getString(6));
			setStatus(pRs.getString(7));
			setPwd(pRs.getString(8));
			setDept_Id(pRs.getString(9));
			setManage_Role(pRs.getString(10));
			setJob_Id(pRs.getString(11));
			setJob_Position(pRs.getString(12));
			setFp_Role(pRs.getString(13));
			setSys_Id(pRs.getString(14));			
			setHP_LoginId(pRs.getString(15));
			setHP_LoginPwd(pRs.getString(16));
			setHP_LoginIp(pRs.getString(17));
			setHP_LoginPort(pRs.getString(18));
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
			setSex(CommUtil.StrToGB2312(request.getParameter("Sex")));
			setBirthday(CommUtil.StrToGB2312(request.getParameter("Birthday")));
			setAddr(CommUtil.StrToGB2312(request.getParameter("Addr")));
			setTel(CommUtil.StrToGB2312(request.getParameter("Tel")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setPwd(CommUtil.StrToGB2312(request.getParameter("Pwd")));
			setDept_Id(CommUtil.StrToGB2312(request.getParameter("Dept_Id")));
			setManage_Role(CommUtil.StrToGB2312(request.getParameter("Manage_Role")));
			setJob_Id(CommUtil.StrToGB2312(request.getParameter("Job_Id")));
			setJob_Position(CommUtil.StrToGB2312(request.getParameter("Job_Position")));
			setFp_Role(CommUtil.StrToGB2312(request.getParameter("Fp_Role")));
			setSys_Id(CommUtil.StrToGB2312(request.getParameter("Sys_Id")));
			setHP_LoginId(CommUtil.StrToGB2312(request.getParameter("HP_LoginId")));
			setHP_LoginPwd(CommUtil.StrToGB2312(request.getParameter("HP_LoginPwd")));
			setHP_LoginIp(CommUtil.StrToGB2312(request.getParameter("HP_LoginIp")));
			setHP_LoginPort(CommUtil.StrToGB2312(request.getParameter("HP_LoginPort")));
			
			setFunc_Type_Id(CommUtil.StrToGB2312(request.getParameter("Func_Type_Id")));
			setStrMd5(CommUtil.StrToGB2312(request.getParameter("StrMd5")));
			setNewPwd(CommUtil.StrToGB2312(request.getParameter("NewPwd")));
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
	private String Sex;
	private String Birthday;
	private String Addr;
	private String Tel;
	private String Status;
	private String Pwd;
	private String Dept_Id;
	private String Manage_Role;
	private String Job_Id;
	private String Job_Position;
	private String Fp_Role;
	private String Sys_Id;
	private String HP_LoginId;
	private String HP_LoginPwd;
	private String HP_LoginIp;
	private String HP_LoginPort;
	
	private String StrMd5;
	private String NewPwd;
	private String Func_Corp_Id;
	private String Func_Type_Id;
	private String Func_Cpm_Id;
	private String Sid;
	
	public String getFunc_Cpm_Id() {
		return Func_Cpm_Id;
	}

	public void setFunc_Cpm_Id(String func_Cpm_Id) {
		Func_Cpm_Id = func_Cpm_Id;
	}

	public String getFunc_Type_Id() {
		return Func_Type_Id;
	}

	public void setFunc_Type_Id(String func_Type_Id) {
		Func_Type_Id = func_Type_Id;
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

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String addr) {
		Addr = addr;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getPwd() {
		return Pwd;
	}

	public void setPwd(String pwd) {
		Pwd = pwd;
	}

	public String getDept_Id() {
		return Dept_Id;
	}

	public void setDept_Id(String deptId) {
		Dept_Id = deptId;
	}

	public String getManage_Role() {
		return Manage_Role;
	}

	public void setManage_Role(String manageRole) {
		Manage_Role = manageRole;
	}

	public String getJob_Id() {
		return Job_Id;
	}

	public void setJob_Id(String jobId) {
		Job_Id = jobId;
	}

	public String getJob_Position() {
		return Job_Position;
	}

	public void setJob_Position(String jobPosition) {
		Job_Position = jobPosition;
	}
	
	public String getFp_Role() {
		return Fp_Role;
	}

	public void setFp_Role(String fpRole) {
		Fp_Role = fpRole;
	}
	
	public String getSys_Id() {
		return Sys_Id;
	}

	public void setSys_Id(String sysId) {
		Sys_Id = sysId;
	}

	public String getHP_LoginId() {
		return HP_LoginId;
	}

	public void setHP_LoginId(String hPLoginId) {
		HP_LoginId = hPLoginId;
	}

	public String getHP_LoginPwd() {
		return HP_LoginPwd;
	}

	public void setHP_LoginPwd(String hPLoginPwd) {
		HP_LoginPwd = hPLoginPwd;
	}

	public String getHP_LoginIp() {
		return HP_LoginIp;
	}

	public void setHP_LoginIp(String hPLoginIp) {
		HP_LoginIp = hPLoginIp;
	}

	public String getHP_LoginPort() {
		return HP_LoginPort;
	}

	public void setHP_LoginPort(String hPLoginPort) {
		HP_LoginPort = hPLoginPort;
	}

	public String getStrMd5() {
		return StrMd5;
	}

	public void setStrMd5(String strMd5) {
		StrMd5 = strMd5;
	}

	public String getNewPwd() {
		return NewPwd;
	}

	public void setNewPwd(String newPwd) {
		NewPwd = newPwd;
	}

	public String getFunc_Corp_Id() {
		return Func_Corp_Id;
	}

	public void setFunc_Corp_Id(String funcCorpId) {
		Func_Corp_Id = funcCorpId;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}