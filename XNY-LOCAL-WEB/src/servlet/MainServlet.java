package servlet;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.*;
import util.*;
import bean.*;

/** 0全部查询
 *  2插入 
 *  3修改
 *  4删除 
 *  10～19单个查询
 * @author cui
 */
public class MainServlet extends HttpServlet
{
	public  final static long serialVersionUID = 1000;
	private Rmi           m_Rmi   = null;
	private String        rmiUrl  = null;
	private Connect       connect = null;
	public  ServletConfig Config;
	
	/* 获取 ServletConfig (non-Javadoc)
	 * @see javax.servlet.GenericServlet#getServletConfig()
	 */
	public final ServletConfig getServletConfig() 
	{
		return Config;
	}
	
	/*  (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig pConfig) throws ServletException
	{	
		Config   = pConfig;
		connect  = new Connect();
		connect.config = pConfig;
		connect.ReConnect();
	}		
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	if(connect.Test()== false)
    	{   
    		request.getSession().setAttribute("ErrMsg", CommUtil.StrToGB2312("RMI服务端未正常运行，无法登陆！"));
    		response.sendRedirect(getUrl(request) + "error.jsp");
    		return;
    	}
    	
        response.setContentType("text/html; charset=gbk");
        String strUrl = request.getRequestURI();
        String strSid = request.getParameter("Sid");
        String[] str = strUrl.split("/");
        strUrl = str[str.length - 1];
        
        System.out.println("Sid:" + strSid);
        System.out.println("====================" + strUrl);
        
        //首页
        if(strUrl.equals("index.do"))
        {
        	CheckCode.CreateCheckCode(request, response, strSid);
        	return;
        }
        else if(strUrl.equalsIgnoreCase("AdminILogout.do"))                      //第二层:admin安全退出
        {
        	request.getSession().removeAttribute("CurrStatus_" + strSid);
        	request.getSession().removeAttribute("Admin_" + strSid);
        	request.getSession().removeAttribute("Corp_Info_" + strSid);
        	request.getSession().removeAttribute("Device_Detail_" + strSid);
        	request.getSession().removeAttribute("User_Info_" + strSid);
        	request.getSession().removeAttribute("User_Stat_" + strSid);
        	request.getSession().removeAttribute("FP_Role_" + strSid);
        	request.getSession().removeAttribute("Manage_Role_" + strSid);
        	request.getSession().removeAttribute("FP_Info_" + strSid);
        	request.getSession().removeAttribute("User_Position_" + strSid);
        	request.getSession().removeAttribute("Crm_Info_" + strSid);
        	request.getSession().removeAttribute("Ccm_Info_" + strSid);
        	//request.getSession().invalidate();
        	response.getWriter().write("<script language = javascript>window.parent.location.href='../index.jsp'</script>");
        }
        else if(strUrl.equalsIgnoreCase("ILogout.do"))                           //第二层:user安全退出
        {
        	request.getSession().removeAttribute("CurrStatus_" + strSid);
        	request.getSession().removeAttribute("UserInfo_" + strSid);
        	request.getSession().removeAttribute("User_Corp_Info_" + strSid);
        	request.getSession().removeAttribute("User_Device_Detail_" + strSid);
        	request.getSession().removeAttribute("User_Data_Device_" + strSid);
        	request.getSession().removeAttribute("User_Data_Attr_" + strSid);
        	request.getSession().removeAttribute("User_User_Info_" + strSid);
        	request.getSession().removeAttribute("User_FP_Role_" + strSid);
        	request.getSession().removeAttribute("User_Manage_Role_" + strSid);
        	request.getSession().removeAttribute("Env_" + strSid);
        	request.getSession().removeAttribute("Env_His_" + strSid);
        	request.getSession().removeAttribute("Week_" + strSid);
        	request.getSession().removeAttribute("Month_" + strSid);
        	request.getSession().removeAttribute("Year_" + strSid);
        	request.getSession().removeAttribute("Graph_" + strSid);
        	request.getSession().removeAttribute("Alarm_Info_" + strSid);
        	request.getSession().removeAttribute("Alert_Info_" + strSid);    	
        	
        	request.getSession().removeAttribute("BYear_" + strSid);
        	request.getSession().removeAttribute("BMonth_" + strSid);
        	request.getSession().removeAttribute("BWeek_" + strSid);
        	request.getSession().removeAttribute("EYear_" + strSid);
        	request.getSession().removeAttribute("EMonth_" + strSid);
        	request.getSession().removeAttribute("EWeek_" + strSid);
        	request.getSession().removeAttribute("BDate_" + strSid);
        	request.getSession().removeAttribute("EDate_" + strSid);
        	
        	
        	//request.getSession().invalidate();
        	response.getWriter().write("<script language = javascript>window.parent.location.href='../index.jsp'</script>");
        }
        else if(strUrl.equalsIgnoreCase("IILogout.do"))                          //第三层:user安全退出
        {
        	request.getSession().removeAttribute("CurrStatus_" + strSid);
        	request.getSession().removeAttribute("UserInfo_" + strSid);
        	request.getSession().removeAttribute("User_Corp_Info_" + strSid);
        	request.getSession().removeAttribute("User_Device_Detail_" + strSid);
        	request.getSession().removeAttribute("User_Data_Device_" + strSid);
        	request.getSession().removeAttribute("User_Data_Attr_" + strSid);
        	request.getSession().removeAttribute("User_User_Info_" + strSid);
        	request.getSession().removeAttribute("User_FP_Role_" + strSid);
        	request.getSession().removeAttribute("User_Manage_Role_" + strSid);
        	request.getSession().removeAttribute("Env_" + strSid);
        	request.getSession().removeAttribute("Env_His_" + strSid);
        	request.getSession().removeAttribute("Week_" + strSid);
        	request.getSession().removeAttribute("Month_" + strSid);
        	request.getSession().removeAttribute("Year_" + strSid);
        	request.getSession().removeAttribute("Graph_" + strSid);
        	request.getSession().removeAttribute("Alarm_Info_" + strSid);
        	request.getSession().removeAttribute("Alert_Info_" + strSid);
        	
        	request.getSession().removeAttribute("BYear_" + strSid);
        	request.getSession().removeAttribute("BMonth_" + strSid);
        	request.getSession().removeAttribute("BWeek_" + strSid);
        	request.getSession().removeAttribute("EYear_" + strSid);
        	request.getSession().removeAttribute("EMonth_" + strSid);
        	request.getSession().removeAttribute("EWeek_" + strSid);
        	request.getSession().removeAttribute("BDate_" + strSid);
        	request.getSession().removeAttribute("EDate_" + strSid);
        	
        	//request.getSession().invalidate();
        	response.getWriter().write("<script language = javascript>window.parent.location.href='../../index.jsp'</script>");
        }
        
        /***************************************公用***************************************************/
        else if (strUrl.equalsIgnoreCase("Login.do"))						     //登录
        	new UserInfoBean().Login(request, response, m_Rmi);
        else if (strUrl.equalsIgnoreCase("PwdEdit.do"))						 	 //密码修改
        	new UserInfoBean().PwdEdit(request, response, m_Rmi);
        
        /**************************************admin**************************************************/
        else if (strUrl.equalsIgnoreCase("Corp_Info.do"))				     	 //公司信息
        	new CorpInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Device_Detail.do"))				     //站级信息
        	new DeviceDetailBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Device_doDragging.do"))				 //站级信息-地图拖拽接口
        	new DeviceDetailBean().doDragging(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_Info.do"))				         //人员信息
        	new UserInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("IdCheck.do"))						 	 //人员信息-帐号检测
        	new UserInfoBean().IdCheck(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_Role.do"))				         //人员权限
        	new UserRoleBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_RoleOP.do"))				     	 //人员权限-编辑
        	new UserRoleBean().RoleOP(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_Position.do"))				 	 //人员信息-岗位管理
        	new UserPositionBean().ExecCmd(request, response, m_Rmi, false);
        
        /**************************************user-ToPo*********************************************/
        else if (strUrl.equalsIgnoreCase("ToPo.do"))						     //GIS监控
        	new DeviceDetailBean().ToPo(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("GIS_Deal.do"))	                     //GIS监控-告警处理
        	new AlertInfoBean().GISDeal(request, response, m_Rmi, false);
        
        /**************************************user-log***************************************************/ 
       	else if (strUrl.equalsIgnoreCase("linkage_Info.do"))	                 //联动日志
        	new LinkageInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("linkage_Info_Export.do"))	             //联动日志-导出
        	new LinkageInfoBean().ExportToExcel(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alert_Info.do"))	                 	 //告警日志
        	new AlertInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alert_Info_Export.do"))	             //告警日志-导出
        	new AlertInfoBean().ExportToExcel(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alert_Deal.do"))	                 	 //告警处理
        	new AlertInfoBean().Deal(request, response, m_Rmi, false);
        
        /**************************************user-env**********************************************/
        else if (strUrl.equalsIgnoreCase("Env.do"))						     	 //实时数据
        	new DataBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Env_His_Export.do"))	             	 //历史明细-导出
        	new DataBean().ExportToExcel(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Graph.do"))	                         //数据图表
        	new DataBean().Graph(request, response, m_Rmi, false);
        
        /**************************************user-Acc***************************************************/
        else if (strUrl.equalsIgnoreCase("Acc_Data.do"))				         //累积流量数据
            new AccDataBean().ExecCmd(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Acc_Data_Export.do"))				     //累积流量导出
        	new AccDataBean().ExportToExcel(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Acc_Sale.do"))				         //销售数据
        	new AccSaleBean().ExecCmd(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Acc_Sale_Export.do"))				     //销售数据导出
        	new AccSaleBean().ExportToExcel(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Device_Detail_Scene.do"))				 //场景图上传
        	new DeviceDetailBean().DetailSenceUp(request, response, m_Rmi, false ,Config);  
        else if (strUrl.equalsIgnoreCase("Device_Detail_Map.do"))				 //设备属性查找
        	new DataNowBean().ToPo(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Device_Detail_Draging.do"))			 //设备属性-添加修改删除查找
        	new DataNowBean().ExecCmd(request, response, m_Rmi, false);  
    }
    
    /** 
     * 链接测试类
     * @author iwant
     */
    private class Connect extends Thread
	{
    	private ServletConfig config = null;
    	public boolean Test()
    	{
    		int i = 0;
        	boolean ok = false;
        	while(3 > i)
    		{        		
    	    	try
    			{   
    	    		if(i != 0) sleep(500);
    	    		ok = m_Rmi.Test();     //与RMI交互 ，成功则返回 true
    	    		i = 3;
    	    		ok = true;
    			}
    	    	catch(Exception e)
    			{    	    		
    	    		ReConnect();
    	    		i++;
    			}
    		}
    		return ok;
    	}
    	private void ReConnect()
    	{
    		try
    		{
    			rmiUrl = config.getInitParameter("rmiUrl");
    			Context context = new InitialContext(); //初始化jndi
    			m_Rmi = (Rmi) context.lookup(rmiUrl);
    		}
    		catch(Exception e)
    		{	
    			e.printStackTrace();
    		}
    	}
    }
	
    /** 
     * 得到当前项目的 URL 
     * @param request
     * @return
     */
    public final static String getUrl(HttpServletRequest request)
	{
    	// http://121.41.52.236/xny/
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		return url;
	}
	
} 