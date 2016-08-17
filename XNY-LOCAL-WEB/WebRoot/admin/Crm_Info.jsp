<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<%@ page import="util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type='text/javascript' src='../skin/js/zDrag.js'   charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/zDialog.js' charset='gb2312'></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	ArrayList Crm_Info    = (ArrayList)session.getAttribute("Crm_Info_" + Sid);
	
%>
<body style="background:#CADFFF">
<form name="Crm_Info" action="Crm_Info.do" method="post" target="mFrame">
<div id="down_bg_2">
  <div id="cap"><img src="../skin/images/crm_info.gif"></div><br><br><br>
  <div id="right_table_center">
  	<table width="90%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30' valign='middle'>
				<td width='70%' align='left'>
					<select name='Func_Sub_Id' style='width:120px;height:20px;' onchange="doSelect()">
						<option value='9' <%=currStatus.getFunc_Sub_Id() == 9?"selected":""%>>全部</option>
						<option value='1' <%=currStatus.getFunc_Sub_Id() == 1?"selected":""%>>单位客户</option>
						<option value='2' <%=currStatus.getFunc_Sub_Id() == 2?"selected":""%>>个人客户</option>
						<!--
						<option value='3' <%=currStatus.getFunc_Sub_Id() == 3?"selected":""%>>内部员工</option>
						-->
					</select>
				</td>
				<td width='30%' align='right'>
					<img src="../skin/images/mini_button_add.gif" style='cursor:hand;' onClick='doAdd()'>
				</td>
			</tr>
			<tr height='30' valign='middle'>
				<td width='100%' align='center' colspan=2>
					<table width="100%" style='margin:auto;' border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			 			<tr>
							<td width="10%" class="table_deep_blue">编 号</td>
							<td width="20%" class="table_deep_blue">简 称</td>
							<td width="10%" class="table_deep_blue">电 话</td>
							<td width="20%" class="table_deep_blue">登记日期</td>
							<td width="10%" class="table_deep_blue">类 型</td>
							<td width="10%" class="table_deep_blue">车辆管理</td>
						</tr>
						<%
						if(Crm_Info != null)
						{
							int sn = 0;
							Iterator iterator = Crm_Info.iterator();
							while(iterator.hasNext())
							{
								CrmInfoBean statBean = (CrmInfoBean)iterator.next();
								String Id = statBean.getId();
								String Brief = statBean.getBrief();
								String Tel = statBean.getTel();
								String CTime = statBean.getCTime();
								String CType = statBean.getCType();
															
								String str_CType = "";
								switch(Integer.parseInt(CType))
								{								
									case 1:
											str_CType = "单位客户";
										break;
									case 2:
											str_CType = "个人客户";
										break;
									case 3:
											str_CType = "内部员工";
										break;
								}
								
								sn++;
						%>
								<tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
									<td align=center><a href="#" title="点击编辑" onClick="doEdit('<%=Id%>')"><U><%=Id%></U></a></td>
									<td align=left><%=Brief%></td>
									<td align=left><%=Tel%></td>
									<td align=center><%=CTime%></td>
								  <td align=center><%=str_CType%></td>
								  <td align=center><a href="#" title="点击查看" onClick="doCar('<%=Id%>', '<%=Brief%>', '<%=str_CType%>')"><U>车辆管理</U></a></td>
								</tr>
						<%
							}
						}
						%>
			 		</table>
				</td>
			</tr>
		</table>
	</div>   
</div>
<input name="Cmd" type="hidden" value="0">
<input name="Sid" type="hidden" value="<%=Sid%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
if(<%=currStatus.getResult().length()%> > 0)
   alert("<%=currStatus.getResult()%>");
<%
currStatus.setResult("");
session.setAttribute("CurrStatus_" + Sid, currStatus);
%>

function doSelect()
{
	Crm_Info.submit();
}

function doAdd()
{
	location = "Crm_Info_Add.jsp?Sid=<%=Sid%>";
}

function doEdit(pId)
{
	location = "Crm_Info_Edit.jsp?Sid=<%=Sid%>&Id="+pId;
}

function doCar(pId, pBrief, pCType)
{
	var diag = new Dialog();
	diag.Top = "50%";
	diag.Width = 850;
	diag.Height = 450;
	diag.Title = "[客户简称: " + pBrief + "][客户类型: " + pCType + "]";
	diag.URL = "Ccm_Info.do?Cmd=0&Sid=<%=Sid%>&Crm_Id="+pId+"&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>";
	diag.show();
}
</SCRIPT>
</html>