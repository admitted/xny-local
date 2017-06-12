package util;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class CurrStatus implements Serializable
{
	private static final long serialVersionUID = 100;
  	private String CheckCode = "";
	private int Cmd;
	private int CurrPage;
	private int TotalRecord;
	private int TotalPages;
	private int Func_Id;
	private int Func_Sub_Id;
	private int Func_Sel_Id;
	private String Func_Cpm_Id;
	private String Func_Corp_Id;
	private String Func_Type_Id;
	private String Func_Name_Id;
	private Vector<Object> VecDate;
	private String Result = null;
	private int Curr_Status;
	private String Jsp;

	public String getFunc_Name_Id() {
		return Func_Name_Id;
	}
	public void setFunc_Name_Id(String func_Name_Id) {
		Func_Name_Id = func_Name_Id;
	}
	public String getCheckCode() {
		return CheckCode;
	}
	public void setCheckCode(String checkCode) {
		CheckCode = checkCode;
	}
	public int getCmd() {
		return Cmd;
	}
	public void setCmd(int cmd) {
		Cmd = cmd;
	}
	public int getCurrPage() {
		return CurrPage;
	}
	public void setCurrPage(int currPage) {
		CurrPage = currPage;
	}
	public int getTotalRecord() {
		return TotalRecord;
	}
	
	/** 计算总页数
	 * @param totalRecord
	 */
	public void setTotalRecord(int totalRecord) {
		TotalRecord = totalRecord;
		TotalPages = (totalRecord + MsgBean.CONST_PAGE_SIZE -1)/MsgBean.CONST_PAGE_SIZE;
	}
	public int getTotalPages() {
		return TotalPages;
	}
	public void setTotalPages(int totalPages) {
		TotalPages = totalPages;
	}
	public int getFunc_Id() {
		return Func_Id;
	}
	public void setFunc_Id(int func_Id) {
		Func_Id = func_Id;
	}
	public int getFunc_Sub_Id() {
		return Func_Sub_Id;
	}
	public void setFunc_Sub_Id(int func_Sub_Id) {
		Func_Sub_Id = func_Sub_Id;
	}
	public int getFunc_Sel_Id() {
		return Func_Sel_Id;
	}
	public void setFunc_Sel_Id(int func_Sel_Id) {
		Func_Sel_Id = func_Sel_Id;
	}
	public String getFunc_Corp_Id() {
		return Func_Corp_Id;
	}
	public void setFunc_Corp_Id(String funcCorpId) {
		Func_Corp_Id = funcCorpId;
	}
	public String getFunc_Type_Id() {
		return Func_Type_Id;
	}
	public void setFunc_Type_Id(String funcTypeId) {
		Func_Type_Id = funcTypeId;
	}
	public Vector<Object> getVecDate() {
		return VecDate;
	}
	public void setVecDate(Vector<Object> vecDate) {
		VecDate = vecDate;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public int getCurr_Status() {
		return Curr_Status;
	}
	public void setCurr_Status(int curr_Status) {
		Curr_Status = curr_Status;
	}
	public String getJsp() {
		return Jsp;
	}
	public void setJsp(String jsp) {
		Jsp = jsp;
	}
	
	/** 获取request中 Cmd,CurrPage,VecDate等数据 封装到 CurrStatus对象中的页面数据
	 * @param request
	 * @param pFromZone
	 *     是一个boolean值, true表示: 从其他页面 到 当前页面 
	 */
	public void getHtmlData(HttpServletRequest request, boolean pFromZone) 
	{	
		try
		{
			if(pFromZone)
			{
				CurrPage = 1;
			}
			else
			{
				Cmd = CommUtil.StrToInt(request.getParameter("Cmd"));
				CurrPage = CommUtil.StrToInt(request.getParameter("CurrPage"));
				CurrPage = CurrPage==0?1:CurrPage;			
				VecDate = CommUtil.getDate(request.getParameter("BTime"),request.getParameter("ETime"));
				//INT
				Func_Id = CommUtil.StrToInt(request.getParameter("Func_Id"));
				Func_Sub_Id = CommUtil.StrToInt(request.getParameter("Func_Sub_Id"));
				Func_Sel_Id = CommUtil.StrToInt(request.getParameter("Func_Sel_Id"));	
				//String
				Func_Corp_Id = CommUtil.StrToGB2312(request.getParameter("Func_Corp_Id"));
				Func_Type_Id = CommUtil.StrToGB2312(request.getParameter("Func_Type_Id"));
				Func_Cpm_Id = CommUtil.StrToGB2312(request.getParameter("Func_Cpm_Id"));
				Func_Name_Id = CommUtil.StrToGB2312(request.getParameter("Func_Name_Id"));				
				
			}
		}catch(Exception Ex)
		{
			Ex.printStackTrace();
		}
	}
	
	/** 获取分页条: 当前页/总页,首页,上一页,下一页 , 跳入页
	 * @param pForm 字符串  表示: JSP页面的表单 的 name值
	 * @return 返回 HTML字符串 输出到浏览器 
	 */
	public String GetPageHtml(String pForm) 
	{
		String s = "";
		int TotalPages;		
		 TotalPages = (TotalRecord + MsgBean.CONST_PAGE_SIZE -1)/MsgBean.CONST_PAGE_SIZE;
		if(0 == TotalRecord)
		{
			CurrPage = 0;
		}
		s += "页数：<strong>" + CurrPage + "</strong>/<strong>" + TotalPages + "</strong><span>[共<b>" + TotalRecord +"</b>条记录]</span>";
		s += "<a href=# onclick='GoPage(1)'>&nbsp;首页</a> ";		
		s += "<a href=# onclick='GoPage(" + (CurrPage - 1) +")'>&nbsp;上一页</a> ";	 	
		
		int beginPage = CurrPage - 4 < 1 ? 1 : CurrPage - 4; 
		if(beginPage <= TotalPages) 
		{
			for(int i=beginPage, j=0; i<=TotalPages&&j<6; i++,j++)
			{
				if(i == CurrPage)
				   s+="<a href=# onclick='GoPage("+i+")'><strong>"+i+"</strong></a> ";
				else
		           s+="<a href=# onclick='GoPage("+i+")'>"+i+"</a> ";
			}
		}
		if(CurrPage == TotalPages)
		{
			s+="<a >&nbsp;下一页</a>";		
			s+="<a >&nbsp;末页</a> ";
		}
		else
		{
			s+="<a href=# onclick='GoPage("+(CurrPage+1)+")'>&nbsp;下一页</a> ";
			s+="<a href=# onclick='GoPage("+TotalPages+")'>&nbsp;末页</a> ";
			
		}
		s+="到第<input name='ToPage' type='text' size='2'>页";
		s+="<input type='button' style='width:40px;height:20px' onClick='GoPage(" + pForm + ".ToPage.value)' value='确定'/>";
		return s; 
	}
	public String getFunc_Cpm_Id() {
		return Func_Cpm_Id;
	}
	public void setFunc_Cpm_Id(String func_Cpm_Id) {
		Func_Cpm_Id = func_Cpm_Id;
	}
}
