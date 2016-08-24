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
	double       Value_All          = 0.0;	
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
	String     EDate      = currStatus.getVecDate().get(1).toString().substring(0,10);
  ArrayList  Acc_Data_Sta    = (ArrayList)session.getAttribute("Acc_Data_Sta_" + Sid);
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
<form name="Acc_Data_Sta"  action="Acc_Data.do" method="post" target="mFrame">
<div id="down_bg_2">
		<table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
				<tr height='25px' class='sjtop'>
						   <td width='70%' align='left'>
								  加气站点:
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
								  <input name='BDate' type='text' style='width:90px;height:18px;' value='<%=BDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
								  <input name='EDate' type='text' style='width:90px;height:18px;' value='<%=EDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
							 </td>
							 <td width='30%' align='right'>		
								  <img id="img1" src="../skin/images/mini_button_search.gif" onClick='doSelect()' style="cursor:hand;">
							 </td>
				</tr>
				<tr height='30' valign='middle'>
							 <td width='100%' align='center' colspan=2>
										<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
												<tr height='30'>
															<td width='5%'  align='center' class="table_deep_blue">序号</td>
															<td width='10%' align='center' class="table_deep_blue">日期</td>
															<td width='20%' align='center' class="table_deep_blue">起始读数</td>
															<td width='20%' align='center' class="table_deep_blue">终止读数</td>
															<td width='20%' align='center' class="table_deep_blue">用气量</td>
															<td width='25%' align='center' class="table_deep_blue">备注</td>
												</tr>
												<%
												 if(Acc_Data_Sta != null)
												 {
														Iterator iterator = Acc_Data_Sta.iterator();
														while(iterator.hasNext())
														{
																AccDataBean Bean = (AccDataBean)iterator.next();
																String CTime   = Bean.getCTime();
																String B_Value = Bean.getB_Value();
																String E_Value = Bean.getE_Value();
																String Value   = Bean.getValue();
																String Unit    = Bean.getUnit();
																String Des     = Bean.getDes();
																Value_All      = Value_All + Double.parseDouble(Value);
																if(null == Value){Value = "";}
																if(null == Unit){Unit = "";}
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
															<td width='10%' align=center><%=CTime%></td>
															<td width='20%' align=center><%=B_Value+Unit%></td>
															<td width='20%' align=center><%=E_Value+Unit%></td>			
															<td width='20%' align=center><%=Value+Unit%></td>			
															<td width='25%' align=center><%=str_Des%></td>
												</tr>
										 <%
												}
											}
										 %>
										<tr height='25'>
				                  <td colspan='6' align='center'>
				  	                  <strong>本页合计：用气量总计为：<font color='red'><%=new BigDecimal(Value_All).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()%></font>t</strong>
					                </td>
			              </tr> 
										<%
											for(int i=0;i<(31 - sn);i++)
											{
												if(sn % 2 != 0)
											  {
										%>				  
										      <tr <%=((i%2)==0?"class='table_blue'":"class='table_white_l'")%>>
										      	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
										      </tr>
											<%
												}
											  else
											  {
											%>				
							            <tr <%=((i%2)==0?"class='table_white_l'":"class='table_blue'")%>>
								            <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
								          </tr>	        
											<%
						       			}
						     			}
										  %> 
							     		 	<tr>
													 <td colspan="6" class="table_deep_blue" >
											 			  <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
										    			    <tr valign="bottom">
										      			       <td nowrap></td>
										    			    </tr>			    		
														  </table>
													 </td>
												</tr>
								   </table>
						   </td>
				</tr>
		</table>
</div>
<input type="hidden"  name="Cmd"       value="0">
<input type="hidden"  name="Sid"       value="<%=Sid%>"/>
<input type="hidden"  name="Cpm_Id"    value=""/>
<input type="hidden"  name="BTime"     value="">
<input type="hidden"  name="ETime"     value="">
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
		var days = new Date(Acc_Data_Sta.EDate.value.replace(/-/g, "/")).getTime() - new Date(Acc_Data_Sta.BDate.value.replace(/-/g, "/")).getTime();
		var dcnt = parseInt(days/(1000*60*60*24));
		if(dcnt < 0)
		{
			alert('截止日期需大于开始日期');
			return;
		}
		if((dcnt + 1) > 31)
		{
			alert('日期跨越不超过31天');
			return;
		}
		
		Acc_Data_Sta.Cpm_Id.value = Acc_Data_Sta.Func_Cpm_Id.value;
		Acc_Data_Sta.BTime.value = Acc_Data_Sta.BDate.value + " 00:00:00";
		Acc_Data_Sta.ETime.value = Acc_Data_Sta.EDate.value + " 23:59:59";
		Acc_Data_Sta.submit();
}

function GoPage(pPage)
{
	var days = new Date(Acc_Data_Sta.EDate.value.replace(/-/g, "/")).getTime() - new Date(Acc_Data_Sta.BDate.value.replace(/-/g, "/")).getTime();
	var dcnt = parseInt(days/(1000*60*60*24));
	if(dcnt < 0)
	{
		alert('截止日期需大于开始日期');
		return;
	}
	if((dcnt + 1) > 31)
	{
		alert('日期跨越不超过31天');
		return;
	}
	
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
	Acc_Data_Sta.Cpm_Id.value = Acc_Data_Sta.Func_Cpm_Id.value;
	Acc_Data_Sta.BTime.value = Acc_Data_Sta.BDate.value + " 00:00:00";
	Acc_Data_Sta.ETime.value = Acc_Data_Sta.EDate.value + " 23:59:59";
	Acc_Data_Sta.CurrPage.value = pPage;
	Acc_Data_Sta.submit();
}

var req = null;
function doExport()
{	
	var days = new Date(Acc_Data_Sta.EDate.value.replace(/-/g, "/")).getTime() - new Date(Acc_Data_Sta.BDate.value.replace(/-/g, "/")).getTime();
	var dcnt = parseInt(days/(1000*60*60*24));
	if(dcnt < 0)
	{
		alert('截止日期需大于开始日期');
		return;
	}
	if((dcnt + 1) > 31)
	{
		alert('日期跨越不超过31天');
		return;
	}
	
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
		var url = "Acc_Data_Sta_Export.do?Sid=<%=Sid%>&Id="+window.parent.frames.lFrame.document.getElementById('id').value+"&Level="+window.parent.frames.lFrame.document.getElementById('level').value+"&BTime="+Acc_Data_Sta.BDate.value+" 00:00:00"+"&ETime="+Acc_Data_Sta.EDate.value+" 23:59:59"+"&CurrPage=<%=currStatus.getCurrPage()%>";
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
			location.href = "../../files/excel/" + resp + ".xls";
		}
	}
}
</SCRIPT>
</html>