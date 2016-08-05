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

/** 0ȫ����ѯ
 *  2���� 
 *  3�޸�
 *  4ɾ�� 
 *  10��19������ѯ
 * @author cui
 */
public class MainServlet extends HttpServlet
{
	public  final static long serialVersionUID = 1000;
	private Rmi           m_Rmi   = null;
	private String        rmiUrl  = null;
	private Connect       connect = null;
	public  ServletConfig Config;
	
	/* ��ȡ ServletConfig (non-Javadoc)
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
    		request.getSession().setAttribute("ErrMsg", CommUtil.StrToGB2312("RMI�����δ�������У��޷���½��"));
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
        
        //��ҳ
        if(strUrl.equals("index.do"))
        {
        	CheckCode.CreateCheckCode(request, response, strSid);
        	return;
        }
        else if(strUrl.equalsIgnoreCase("AdminILogout.do"))                      //�ڶ���:admin��ȫ�˳�
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
        else if(strUrl.equalsIgnoreCase("ILogout.do"))                           //�ڶ���:user��ȫ�˳�
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
        else if(strUrl.equalsIgnoreCase("IILogout.do"))                          //������:user��ȫ�˳�
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
        
        /***************************************����***************************************************/
        else if (strUrl.equalsIgnoreCase("Login.do"))						     //��¼
        	new UserInfoBean().Login(request, response, m_Rmi);
        else if (strUrl.equalsIgnoreCase("PwdEdit.do"))						 	 //�����޸�
        	new UserInfoBean().PwdEdit(request, response, m_Rmi);
        
        /**************************************admin**************************************************/
        else if (strUrl.equalsIgnoreCase("Corp_Info.do"))				     	 //��˾��Ϣ
        	new CorpInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Device_Detail.do"))				     //վ����Ϣ
        	new DeviceDetailBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Device_doDragging.do"))				 //վ����Ϣ-��ͼ��ק�ӿ�
        	new DeviceDetailBean().doDragging(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_Info.do"))				         //��Ա��Ϣ
        	new UserInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("IdCheck.do"))						 	 //��Ա��Ϣ-�ʺż��
        	new UserInfoBean().IdCheck(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_Role.do"))				         //��ԱȨ��
        	new UserRoleBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_RoleOP.do"))				     	 //��ԱȨ��-�༭
        	new UserRoleBean().RoleOP(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("User_Position.do"))				 	 //��Ա��Ϣ-��λ����
        	new UserPositionBean().ExecCmd(request, response, m_Rmi, false);
        
        /**************************************user-ToPo*********************************************/
        else if (strUrl.equalsIgnoreCase("ToPo.do"))						     //GIS���
        	new DeviceDetailBean().ToPo(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("GIS_Deal.do"))	                     //GIS���-�澯����
        	new AlertInfoBean().GISDeal(request, response, m_Rmi, false);
        
        /**************************************user-log***************************************************/ 
       	else if (strUrl.equalsIgnoreCase("Alarm_Info.do"))	                 	 //������־
        	new AlarmInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alarm_Info_Export.do"))	             //������־-����
        	new AlarmInfoBean().ExportToExcel(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alert_Info.do"))	                 	 //�澯��־
        	new AlertInfoBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alert_Info_Export.do"))	             //�澯��־-����
        	new AlertInfoBean().ExportToExcel(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Alert_Deal.do"))	                 	 //�澯����
        	new AlertInfoBean().Deal(request, response, m_Rmi, false);
        
        /**************************************user-env**********************************************/
        else if (strUrl.equalsIgnoreCase("Env.do"))						     	 //ʵʱ����
        	new DataBean().ExecCmd(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Env_His_Export.do"))	             	 //��ʷ��ϸ-����
        	new DataBean().ExportToExcel(request, response, m_Rmi, false);
        else if (strUrl.equalsIgnoreCase("Graph.do"))	                         //����ͼ��
        	new DataBean().Graph(request, response, m_Rmi, false);
//        else if (strUrl.equalsIgnoreCase("Env_File.do"))						 //ͼƬ�ϴ�
//        	new DataBean().DaoFile(request, response, m_Rmi, false);
        
        /**************************************user-Acc***************************************************/
        else if (strUrl.equalsIgnoreCase("Acc_Data.do"))				         //�ۻ���������
            new AccDataBean().ExecCmd(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Acc_Sale.do"))				         //��������
        	new AccSaleBean().ExecCmd(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Device_Detail_Scene.do"))				 //����ͼ�ϴ�
        	new DeviceDetailBean().DetailSenceUp(request, response, m_Rmi, false ,Config);  
        else if (strUrl.equalsIgnoreCase("Device_Detail_Map.do"))				 //�豸���Բ���
        	new DataNowBean().ToPo(request, response, m_Rmi, false);  
        else if (strUrl.equalsIgnoreCase("Device_Detail_Draging.do"))			 //�豸����-����޸�ɾ������
        	new DataNowBean().ExecCmd(request, response, m_Rmi, false);  
    }
    
    /** 
     * ���Ӳ�����
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
    	    		ok = m_Rmi.Test(); //��RMI���� ���ɹ��򷵻� true
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
    			Context context = new InitialContext(); //��ʼ��jndi
    			m_Rmi = (Rmi) context.lookup(rmiUrl);
    		}
    		catch(Exception e)
    		{	
    			e.printStackTrace();
    		}
    	}
    }
	
    /** 
     * �õ���ǰ��Ŀ�� URL 
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