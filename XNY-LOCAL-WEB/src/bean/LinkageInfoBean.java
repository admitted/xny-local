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

public class LinkageInfoBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_LINKAGE;
	public long getClassId()
	{
		return serialVersionUID;
	}

	public LinkageInfoBean() 
	{
		super.className = "AlarmInfoBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		//类型
		Func_Sub_Id = currStatus.getFunc_Sub_Id()+"";
		if(Func_Sub_Id.equals("9"))
		{
			Func_Sub_Id = "";
		}
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		switch(currStatus.getCmd())
		{
		    case 0://查询
		    	request.getSession().setAttribute("Alarm_Info_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	currStatus.setJsp("Alarm_Info.jsp?Sid="+ Sid);
		    	break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	}
	
	//明细导出
	public void ExportToExcel(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) 
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			
			//类型
			Func_Sub_Id = currStatus.getFunc_Sub_Id()+"";
			if(Func_Sub_Id.equals("9"))
			{
				Func_Sub_Id = "";
			}
			
			//清除历史
			
			//生成当前
			SimpleDateFormat SimFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String BT = currStatus.getVecDate().get(0).toString().substring(5,10);
			String ET = currStatus.getVecDate().get(1).toString().substring(5,10);
			String SheetName = "_" + BT + "," + ET;
			String UPLOAD_NAME = SimFormat.format(new Date()) + "_" + BT + "," + ET;
			
			msgBean = pRmi.RmiExec(0, this, 0);
			ArrayList<?> temp = (ArrayList<?>)msgBean.getMsg();
			if(temp != null)
			{
				WritableWorkbook book = Workbook.createWorkbook(new File(UPLOAD_PATH + UPLOAD_NAME + ".xls"));
	            WritableSheet sheet = book.createSheet(SheetName, 0);
	            sheet.setColumnView(0, 20);
	            Label label1 = new Label(0, 0, "站点");
	            Label label2 = new Label(1, 0, "设备");
	            Label label3 = new Label(2, 0, "动作");
	            Label label4 = new Label(3, 0, "内容");
	            Label label5 = new Label(4, 0, "时间");
	            Label label6 = new Label(5, 0, "原因");
	            Label label7 = new Label(6, 0, "操作员");
	            Label label8 = new Label(7, 0, "结果");
	            sheet.addCell(label1);
	            sheet.addCell(label2);
	            sheet.addCell(label3);
	            sheet.addCell(label4);
	            sheet.addCell(label5);
	            sheet.addCell(label6);
	            sheet.addCell(label7);
	            sheet.addCell(label8);
	            
	            Iterator<?> iterator = (Iterator<?>)temp.iterator();
				int i = 0;
				while(iterator.hasNext())
				{
					i++;
					LinkageInfoBean Bean = (LinkageInfoBean)iterator.next();
					String A_Cpm_Name = Bean.getCpm_Name();
					String A_S_Id = Bean.getS_Id();
					String A_S_CName = Bean.getS_CName();
					String A_Attr_Id = Bean.getS_Attr_Id();
					String A_Attr_Name = Bean.getS_Attr_Name();
					String A_S_Attr_Value = Bean.getS_Attr_Value();
					String A_D_CName = Bean.getD_CName();
					String A_D_Act_Name = Bean.getD_Act_Name();
					String A_CData = Bean.getCData();
					String A_CTime = Bean.getCTime();
					String A_Operator = Bean.getOperator();
					String A_Status = Bean.getStatus();
					
					if(null == A_S_Id){A_S_Id = "";}
					if(null == A_S_CName){A_S_CName = "";}
					if(null == A_Attr_Id){A_Attr_Id = "";}
					if(null == A_Attr_Name){A_Attr_Name = "";}
					if(null == A_S_Attr_Value){A_S_Attr_Value = "";}
					if(null == A_D_CName){A_D_CName = "";}
					if(null == A_D_Act_Name){A_D_Act_Name = "";}
					if(null == A_CData){A_CData = "";}
					if(null == A_CTime){A_CTime = "";}
					if(null == A_Operator){A_Operator = "";}
					if(null == A_Status){A_Status = "";}
					
					String str_Status = "";
					switch(Integer.parseInt(A_Status))
					{
						case 0:
							str_Status = "成功";
							break;
						case 3000:
							str_Status = "提交成功";
							break;
						default:
							str_Status = "失败";
							break;
					}
					
					String Reason = "";
					if(A_S_Id.trim().length() < 1 && !A_Operator.equals("TIMING"))
					{
						Reason = "人为远程手工控制";
					}
					else if(A_S_Id.trim().length() < 1 && A_Operator.equals("TIMING"))
					{
						Reason = "定时自动执行任务";
					}	
					else
					{
						if(A_Attr_Id.equals("0000"))
						{
							Reason = "[" + A_S_CName + "]离线触发";
						}
						else
						{
							Reason = "[" + A_S_CName + "]因采集[" + A_Attr_Name + " " + A_S_Attr_Value + "]数据异常自动触发";
						}
					}
					
					sheet.setColumnView(i, 20);
		            Label label = new Label(0,i,A_Cpm_Name); //站点
		            sheet.addCell(label);
		            label = new Label(1,i,A_D_CName);        //设备
		            sheet.addCell(label);
		            label = new Label(2,i,A_D_Act_Name);     //动作
		            sheet.addCell(label);
		            label = new Label(3,i,A_CData);          //内容
		            sheet.addCell(label);
		            label = new Label(4,i,A_CTime);          //时间
		            sheet.addCell(label);
		            label = new Label(5,i,Reason);           //原因
		            sheet.addCell(label);
		            label = new Label(6,i,A_Operator);       //操作员
		            sheet.addCell(label);
		            label = new Label(7,i,str_Status);       //结果
		            sheet.addCell(label);
				}
				book.write();
	            book.close();
	            try
	    		{
	    			PrintWriter out = response.getWriter();	    			
	    			out.print(UPLOAD_NAME);
	    		}
	    		catch(Exception exp)
	    		{
	    		   exp.printStackTrace();	
	    		}	            
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://查询
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.s_id, t.s_cname, t.s_attr_id, t.s_attr_name, t.s_attr_value, t.d_id, t.d_cname, t.d_act_id, t.d_act_name, t.cdata, t.ctime, t.operator, t.status " +
					  " from view_alarm_info t " +
					  " where instr('"+ Id +"', t.cpm_id) > 0 " +
					  "   and t.status like '"+ Func_Sub_Id +"%'" +
					  "   and t.ctime >= date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					  "   and t.ctime <= date_format('"+currStatus.getVecDate().get(1).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					  "   order by t.ctime desc ";
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
			setS_Id(pRs.getString(4));
			setS_CName(pRs.getString(5));
			setS_Attr_Id(pRs.getString(6));
			setS_Attr_Name(pRs.getString(7));
			setS_Attr_Value(pRs.getString(8));
			setD_Id(pRs.getString(9));
			setD_CName(pRs.getString(10));
			setD_Act_Id(pRs.getString(11));
			setD_Act_Name(pRs.getString(12));
			setCData(pRs.getString(13));
			setCTime(pRs.getString(14));
			setOperator(pRs.getString(15));
			setStatus(pRs.getString(16));
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
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setS_Id(CommUtil.StrToGB2312(request.getParameter("S_Id")));
			setS_CName(CommUtil.StrToGB2312(request.getParameter("S_CName")));
			setS_Attr_Id(CommUtil.StrToGB2312(request.getParameter("S_Attr_Id")));
			setS_Attr_Name(CommUtil.StrToGB2312(request.getParameter("S_Attr_Name")));
			setS_Attr_Value(CommUtil.StrToGB2312(request.getParameter("S_Attr_Value")));
			setD_Id(CommUtil.StrToGB2312(request.getParameter("D_Id")));
			setD_CName(CommUtil.StrToGB2312(request.getParameter("D_CName")));
			setD_Act_Id(CommUtil.StrToGB2312(request.getParameter("D_Act_Id")));
			setD_Act_Name(CommUtil.StrToGB2312(request.getParameter("D_Act_Name")));
			setCData(CommUtil.StrToGB2312(request.getParameter("CData")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setOperator(CommUtil.StrToGB2312(request.getParameter("Operator")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
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
	private String S_Id;
	private String S_CName;
	private String S_Attr_Id;
	private String S_Attr_Name;
	private String S_Attr_Value;
	private String D_Id;
	private String D_CName;
	private String D_Act_Id;
	private String D_Act_Name;
	private String CData;
	private String CTime;
	private String Operator;
	private String Status;
	
	private String Sid;
	private String Func_Sub_Id;
	private String Id;
	
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

	public String getS_Id() {
		return S_Id;
	}

	public void setS_Id(String sId) {
		S_Id = sId;
	}

	public String getS_CName() {
		return S_CName;
	}

	public void setS_CName(String sCName) {
		S_CName = sCName;
	}

	public String getS_Attr_Id() {
		return S_Attr_Id;
	}

	public void setS_Attr_Id(String sAttrId) {
		S_Attr_Id = sAttrId;
	}

	public String getS_Attr_Name() {
		return S_Attr_Name;
	}

	public void setS_Attr_Name(String sAttrName) {
		S_Attr_Name = sAttrName;
	}

	public String getS_Attr_Value() {
		return S_Attr_Value;
	}

	public void setS_Attr_Value(String sAttrValue) {
		S_Attr_Value = sAttrValue;
	}

	public String getD_Id() {
		return D_Id;
	}

	public void setD_Id(String dId) {
		D_Id = dId;
	}

	public String getD_CName() {
		return D_CName;
	}

	public void setD_CName(String dCName) {
		D_CName = dCName;
	}

	public String getD_Act_Id() {
		return D_Act_Id;
	}

	public void setD_Act_Id(String dActId) {
		D_Act_Id = dActId;
	}

	public String getD_Act_Name() {
		return D_Act_Name;
	}

	public void setD_Act_Name(String dActName) {
		D_Act_Name = dActName;
	}

	public String getCData() {
		return CData;
	}

	public void setCData(String cData) {
		CData = cData;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getOperator() {
		return Operator;
	}

	public void setOperator(String operator) {
		Operator = operator;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}

	public String getFunc_Sub_Id() {
		return Func_Sub_Id;
	}

	public void setFunc_Sub_Id(String funcSubId) {
		Func_Sub_Id = funcSubId;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
	
}
