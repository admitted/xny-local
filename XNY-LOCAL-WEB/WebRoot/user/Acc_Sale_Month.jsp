<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%@ taglib uri="/WEB-INF/limitvalidatetag.tld" prefix="Limit"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type='text/javascript' src='../skin/js/browser.js' charset='gb2312'></script>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
</head>


<%
	String       Sid                = CommUtil.StrToGB2312(request.getParameter("Sid"));
	ArrayList    User_FP_Role       = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	UserInfoBean UserInfo           = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	ArrayList    User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	ArrayList    User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
	String       ManageId           = UserInfo.getManage_Role();
	String       FpId               = UserInfo.getFp_Role();
	String       FpList             = "";
	if(null != FpId && FpId.length() > 0 && null != User_FP_Role)
	{
		Iterator roleiter = User_FP_Role.iterator();
		while(roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean)roleiter.next();
			if(roleBean.getId().equals(FpId) && null != roleBean.getPoint())
			{
				FpList = roleBean.getPoint();
			}
		}
	}
	
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  String     BDate      = currStatus.getVecDate().get(0).toString().substring(0,10);
  ArrayList  Acc_Sale_Month    = (ArrayList)session.getAttribute("Acc_Sale_Month_" + Sid);
  int        sn         = 0; 
  String     Manage_List = "";
  
	if(ManageId.length() > 0 && null != User_Manage_Role)
	{
			Iterator iterator = User_Manage_Role.iterator();
			while(iterator.hasNext())
			{
					UserRoleBean statBean = (UserRoleBean)iterator.next();
					if(statBean.getId().equals(ManageId))
					{
							String R_Point = statBean.getPoint();
							if(null == R_Point){R_Point = "";}
							Manage_List += R_Point;
					}
			}
	}
	String Dept_Id = UserInfo.getDept_Id();
	if(Dept_Id.length()>3){Manage_List = Dept_Id; }
%>
<body style=" background:#CADFFF">
<form name="Acc_Sale_Month"  action="Acc_Sale.do" method="post" target="mFrame">
<div id="down_bg_2">
		<table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
				<tr height='25px' class='sjtop'>
						   <td width='70%' align='left'>
								  &nbsp;
									<!--
									场站站点: 全部 &nbsp;
									<select  name='Func_Cpm_Id' style='width:100px;height:20px' onChange="doSelect()" >					
											    <%					
													if( Manage_List.length() > 0 && null != User_Device_Detail)
													{
														Iterator iterator = User_Device_Detail.iterator();
														while(iterator.hasNext())
														{
															DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
															if(Manage_List.contains(statBean.getId()))
															{
													%>
																<option value='<%=statBean.getId()%>' <%=currStatus.getFunc_Cpm_Id().equals(statBean.getId())?"selected":""%>><%=statBean.getBrief()%></option>
													<%
															}
														}
													}
													%>
									</select>
									-->
								  <input name='BDate' type='text' style='width:90px;height:18px;' value='<%=BDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
								  
							 </td>
							 <td width='30%' align='right'>		
								  <img id="img1" src="../skin/images/mini_button_search.gif" onClick='doSelect()' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='020301' ctype='1'/>">
								  <img id="img2" src="../skin/images/excel.gif"              onClick='doExport()' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='020302' ctype='1'/>">
							 </td>
				</tr>
				<tr height='30' valign='middle'>
							 <td width='100%' align='center' colspan=2>
										<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
												<tr height='30'>
															<td width='5%'  align='center' class="table_deep_blue">序号</td>
															<td width='15%' align='center' class="table_deep_blue">站点名称</td>
															<td width='15%' align='center' class="table_deep_blue">月份</td>
															<td width='15%' align='center' class="table_deep_blue">用气量(方)</td>
															<td width='15%' align='center' class="table_deep_blue">单价(元/方)</td>
															<td width='15%' align='center' class="table_deep_blue">金额(元)</td>
															<td width='25%' align='center' class="table_deep_blue">备注</td>
												</tr>
												<%
												 if(Acc_Sale_Month != null)
												 {
														Iterator iterator = Acc_Sale_Month.iterator();
														while(iterator.hasNext())
														{
																AccSaleBean Bean = (AccSaleBean)iterator.next();
																String Cpm_Name   = Bean.getCpm_Name();
																String CTime      = Bean.getCTime().substring(0, 7);
																String Value      = Bean.getValue();
																String Unit_Price = Bean.getUnit_Price();
																String Salary     = Bean.getSalary();
																String Des        = Bean.getDes();
																
																if(null == Value){Value = "";}
																if(null == Des){Des = "";}
																
																String str_Des = "无";
																if(Des.length() > 0)
																{
																	str_Des = Des;
																}
																sn++;
												%>
											  <tr height='30' <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
															<td width='5%'  align=center><%=sn%></td>
															<td width='15%' align=center><%=Cpm_Name%></td>
															<td width='15%' align=center><%=CTime%></td>
															<td width='15%' align=center><%=Value%></td>
															<td width='15%' align=center><%=Unit_Price%></td>			
															<td width='15%' align=center><%=Salary%></td>			
															<td width='25%' align=center><%=str_Des%></td>
												</tr>
										 <%
												}
											}
											for(int i=0;i<(MsgBean.CONST_PAGE_SIZE - sn);i++)
											{
												if(sn % 2 != 0)
											  {
											%>				  
										      <tr <%=((i%2)==0?"class='table_blue'":"class='table_white_l'")%>>
										      	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
										      </tr>
											<%
												}
											  else
											  {
											%>				
							            <tr <%=((i%2)==0?"class='table_white_l'":"class='table_blue'")%>>
								            <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
								          </tr>	        
											<%
						       			}
						     			}
										  %> 
							     		 	<tr>
													 <td colspan="7" class="table_deep_blue" >
											 			  <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
										    			    <tr valign="bottom">
										      			       <td nowrap><%=currStatus.GetPageHtml("Acc_Sale_Month")%></td>
										    			    </tr>			    		
														  </table>
													 </td>
												</tr>
								   </table>
						   </td>
				</tr>
		</table>
</div>
<input type="hidden"  name="Cmd"       value="2">
<input type="hidden"  name="Sid"       value="<%=Sid%>"/>
<input type="hidden"  name="Cpm_Id"    value=""/>
<input type="hidden"  name="BTime"     value="">
<input type="hidden"  name="CurrPage"  value="<%=currStatus.getCurrPage()%>">
<input type="button"  id="CurrButton"  onClick="doSelect()" style="display:none">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
//ipad禁掉导出
if(1 == fBrowserRedirect() || 2 == fBrowserRedirect())
{
		document.getElementById('img2').style.display = 'none';
}

function doSelect()
{
		Acc_Sale_Month.BTime.value = Acc_Sale_Month.BDate.value + " 00:00:00";
		Acc_Sale_Month.submit();
}

function GoPage(pPage)
{
	
	if(pPage == "")
	{
		 alert("请输入目标页面的数值!");
		 return;
	}
	if(pPage < 1)
	{
	   	alert("请输入页数大于1");
		  return;	
	}
	if(pPage > <%=currStatus.getTotalPages()%>)
	{
		pPage = <%=currStatus.getTotalPages()%>;
	}
	Acc_Sale_Month.BTime.value = Acc_Sale_Month.BDate.value + " 00:00:00";
	Acc_Sale_Month.CurrPage.value = pPage;
	Acc_Sale_Month.submit();
}

var req = null;
function doExport()
{	
	if(0 == <%=sn%>)
	{
		alert('当前无记录!');
		return;
	}
	if(65000 <= <%=currStatus.getTotalRecord()%>)
	{
		alert('记录过多，请分批导出!');
		return;
	}
	if(confirm("确定导出?"))
  {
		if(window.XMLHttpRequest)
	  {
			req = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}		
		//设置回调函数
		req.onreadystatechange = callbackForName;
		var url = "Acc_Sale_Export.do?Cmd=2&Sid=<%=Sid%>&BTime="+Acc_Sale_Month.BDate.value+" 00:00:00"+"&CurrPage=<%=currStatus.getCurrPage()%>";
		req.open("post",url,true);
		req.send(null);
		return true;
	}
}
function callbackForName()
{
	var state = req.readyState;
	if(state==4)
	{
		var resp = req.responseText;			
		var str = "";
		if(resp != null)
		{
			location.href = "../files/excel/" + resp + ".xls";
		}
	}
}
</SCRIPT>
</html>