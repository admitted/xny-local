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
<title>中海油LNG加气站公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type='text/javascript' src='../skin/js/browser.js' charset='gb2312'></script>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
	ArrayList User_FP_Role = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	UserInfoBean UserInfo = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	ArrayList User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	ArrayList User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
	String ManageId = UserInfo.getManage_Role();
	String FpId = UserInfo.getFp_Role();
	String FpList = "";
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
  String BDate = currStatus.getVecDate().get(0).toString().substring(0,10);
	String EDate = currStatus.getVecDate().get(1).toString().substring(0,10);
	
  ArrayList Alarm_Info = (ArrayList)session.getAttribute("Alarm_Info_" + Sid);         
  int sn = 0; 
  
%>
<body style=" background:#CADFFF">
<form name="Alarm_Info"  action="Alarm_Info.do" method="post" target="mFrame">
<div id="down_bg_2">
	<table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
		<tr height='25px' class='sjtop'>
			<td width='70%' align='left'>
					加气站点:
				<select  name='Func_Cpm_Id' style='width:100px;height:20px' onChange="doSelect()" >					
						<%
								String Manage_List = "";
								if(ManageId.length() > 0 && null != User_Manage_Role)
								{
									Iterator iterator = User_Manage_Role.iterator();
									while(iterator.hasNext())
									{
										UserRoleBean statBean = (UserRoleBean)iterator.next();
										if(statBean.getId().substring(0,4).equals(ManageId) && statBean.getId().length() == 8)
										{
											String R_Point = statBean.getPoint();
											if(null == R_Point){R_Point = "";}
											Manage_List += R_Point;
										}
									}
								}							
								if(Manage_List.length() > 0 && null != User_Device_Detail)
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
				记录状态:
				<select name='Func_Sub_Id' style='width:90px;height:20px' onChange="doSelect()">
					<option value='9'    <%=currStatus.getFunc_Sub_Id() == 9 ? "selected":""%>   >全部</option>
					<option value='0000' <%=currStatus.getFunc_Sub_Id() == 0 ? "selected":""%>   >成功</option>
					<option value='3000' <%=currStatus.getFunc_Sub_Id() == 3000 ? "selected":""%>>提交成功</option>
					<option value='3006' <%=currStatus.getFunc_Sub_Id() == 3006 ? "selected":""%>>失败</option>	
				</select>
				<input name='BDate' type='text' style='width:90px;height:18px;' value='<%=BDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
				-
				<input name='EDate' type='text' style='width:90px;height:18px;' value='<%=EDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
			</td>
			<td width='30%' align='right'>
				<img id="img1" src="../skin/images/mini_button_search.gif" onClick='doSelect()' style='cursor:hand;'>
				<img id="img2" src="../skin/images/excel.gif"              onClick='doExport()' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='070102' ctype='1'/>">
			</td>
		</tr>
		<tr height='30' valign='middle'>
			<td width='100%' align='center' colspan=2>
				<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
					<tr height='25' valign='middle'>
						<td width='3%'  align='center' class="table_deep_blue">序号</td>
						<td width='8%'  align='center' class="table_deep_blue">站点</td>
						<td width='8%'  align='center' class="table_deep_blue">设备</td>
						<td width='8%'  align='center' class="table_deep_blue">动作</td>
						<td width='12%' align='center' class="table_deep_blue">内容</td>
						<td width='12%' align='center' class="table_deep_blue">时间</td>
						<td width='20%' align='center' class="table_deep_blue">原因</td>
						<td width='8%'  align='center' class="table_deep_blue">操作员</td>
						<td width='8%'  align='center' class="table_deep_blue">结果</td>
					</tr>
					<%
						if(Alarm_Info != null)
								 {
									Iterator iterator = Alarm_Info.iterator();
									while(iterator.hasNext())
									{
										LinkageInfoBean Bean = (LinkageInfoBean)iterator.next();
										String Cpm_Name = Bean.getCpm_Name();
										String S_Id = Bean.getS_Id();
										String S_CName = Bean.getS_CName();
										String Attr_Id = Bean.getS_Attr_Id();
										String Attr_Name = Bean.getS_Attr_Name();
										String S_Attr_Value = Bean.getS_Attr_Value();
										String D_CName = Bean.getD_CName();
										String D_Act_Name = Bean.getD_Act_Name();
										String CData = Bean.getCData();
										String CTime = Bean.getCTime();
										String Operator = Bean.getOperator();
										String Status = Bean.getStatus();
										
										if(null == S_Id){S_Id = "";}
										if(null == S_CName){S_CName = "";}
										if(null == Attr_Id){Attr_Id = "";}
										if(null == Attr_Name){Attr_Name = "";}
										if(null == S_Attr_Value){S_Attr_Value = "";}						
										if(null == D_CName){D_CName = "";}
										if(null == D_Act_Name){D_Act_Name = "";}
										if(null == CData){CData = "";}
										if(null == CTime){CTime = "";}
										if(null == Operator){Operator = "";}
										if(null == Status){Status = "";}
										
										String str_Status = "";
										switch(Integer.parseInt(Status))
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
										String str_CData = CData;
										if(str_CData.length() > 25)
										{
											str_CData = str_CData.substring(0,25) + "...";
										}
										
										String Reason = "";
										if(S_Id.trim().length() < 1 && !Operator.equals("TIMING"))
										{
											Reason = "人为远程手工控制";
										}
										else if(S_Id.trim().length() < 1 && Operator.equals("TIMING"))
										{
											Reason = "定时自动执行任务";
										}	
										else
										{
											if(Attr_Id.equals("0000"))
											{
												Reason = "[" + S_CName + "]离线触发";
											}
											else
											{
												Reason = "[" + S_CName + "]因采集[" + Attr_Name + " " + S_Attr_Value + "]数据异常自动触发";
											}
										}	
										sn ++;
					%>
				  <tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
						<td align=center><%=sn%></td>
						<td align=left><%=Cpm_Name%></td>
				    <td align=left><%=D_CName%></td>
				    <td align=left><%=D_Act_Name%></td>		    
				    <td align=left title='<%=CData%>'><%=str_CData%>&nbsp;</td>   
						<td align=left><%=CTime%></td>	
						<td align=left><%=Reason%></td>
						<td align=left><%=Operator%></td>
						<td align=left><%=str_Status%></td>
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
				      	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
				      </tr>  
					<%
						}
					  else
					  {
					%>				
		          <tr <%=((i%2)==0?"class='table_white_l'":"class='table_blue'")%>>
			          <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
			        </tr>      
					<%
       			}
     			}
					%> 
     		 	<tr>
						<td colspan="9" class="table_deep_blue" >
				 			<table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
			    			<tr valign="bottom">
			      			<td nowrap><%=currStatus.GetPageHtml("Alarm_Info")%></td>
			    			</tr>			    		
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
<input type="hidden" name="Cmd"   value="0">
<input type="hidden" name="Sid"   value="<%=Sid%>">
<input type="hidden" name="Id"    value="">
<input type="hidden" name="Level" value="">
<input type="hidden" name="BTime" value="">
<input type="hidden" name="ETime" value="">
<input type="hidden" name="CurrPage" value="<%=currStatus.getCurrPage()%>">
<input type="button" id="CurrButton" onClick="doSelect()" style="display:none">
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
	var days = new Date(Alarm_Info.EDate.value.replace(/-/g, "/")).getTime() - new Date(Alarm_Info.BDate.value.replace(/-/g, "/")).getTime();
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
	Alarm_Info.Id.value = Alarm_Info.Func_Cpm_Id.value;
	//Alarm_Info.Level.value = window.parent.frames.lFrame.document.getElementById('level').value;
	Alarm_Info.BTime.value = Alarm_Info.BDate.value + " 00:00:00";
	Alarm_Info.ETime.value = Alarm_Info.EDate.value + " 23:59:59";
	Alarm_Info.submit();
}

function GoPage(pPage)
{
	var days = new Date(Alarm_Info.EDate.value.replace(/-/g, "/")).getTime() - new Date(Alarm_Info.BDate.value.replace(/-/g, "/")).getTime();
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
	Alarm_Info.Id.value = window.parent.frames.lFrame.document.getElementById('id').value;
	Alarm_Info.Level.value = window.parent.frames.lFrame.document.getElementById('level').value;
	Alarm_Info.BTime.value = Alarm_Info.BDate.value + " 00:00:00";
	Alarm_Info.ETime.value = Alarm_Info.EDate.value + " 23:59:59";
	Alarm_Info.CurrPage.value = pPage;
	Alarm_Info.submit();
}

var req = null;
function doExport()
{
	var days = new Date(Alarm_Info.EDate.value.replace(/-/g, "/")).getTime() - new Date(Alarm_Info.BDate.value.replace(/-/g, "/")).getTime();
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
		var url = "Alarm_Info_Export.do?Sid=<%=Sid%>&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&CurrPage=<%=currStatus.getCurrPage()%>&BTime="+Alarm_Info.BDate.value+" 00:00:00"+"&ETime="+Alarm_Info.EDate.value+" 23:59:59"+"&Id="+window.parent.frames.lFrame.document.getElementById('id').value+"&Level="+window.parent.frames.lFrame.document.getElementById('level').value;
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