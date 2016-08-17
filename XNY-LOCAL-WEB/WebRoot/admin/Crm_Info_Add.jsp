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
<script type="text/javascript" src="../skin/js/util.js"></script>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%

	String Sid   = CommUtil.StrToGB2312(request.getParameter("Sid"));
	String BDate = CommUtil.getDate();
	CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	
%>
<body style="background:#CADFFF">
<form name="Crm_Info_Add" action="Crm_Info.do" method="post" target="mFrame">
<div id="down_bg_2">
	<div id="cap"><img src="../skin/images/crm_info.gif"></div><br><br><br>
	<div id="right_table_center">
		<table width="60%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30'>
				<td width='100%' align='right'>
					<img src="../skin/images/mini_button_submit.gif" style='cursor:hand;' onClick='doAdd()'>
					<img src="../skin/images/button10.gif"           style='cursor:hand;' onclick='doNO()'>
				</td>
			</tr>
			<tr height='30'>
				<td width='100%' align='center'>
					<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">		
						<tr height='30'>
							<td width='20%' align='center'>编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</td>
							<td width='30%' align='left'>
								***
							</td>
							<td width='20%' align='center'>名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</td>
							<td width='30%' align='left'>
								<input type='text' name='CName' style='width:90%;height:18px;' value='' maxlength='32'>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话</td>
							<td width='30%' align='left'>
								<input type='text' name='Tel' style='width:90%;height:18px;' value='' maxlength='15'>
							</td>
							<td width='20%' align='center'>简&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</td>
							<td width='30%' align='left'>
								<input type='text' name='Brief' style='width:90%;height:18px;' value='' maxlength='10'>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>登记日期</td>
							<td width='30%' align='left'>
								<input type="text" name="CTime" onClick="WdatePicker({readOnly:true})" class="Wdate" maxlength="10" style='width:90%;' value='<%=BDate%>'>
							</td>
							<td width='20%' align='center'>类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型</td>
							<td width='30%' align='left'>
								<select name="CType" style="width:92%;height:20px;">
									<option value='1'>单位客户</option>
									<option value='2'>个人客户</option>
									<!--
									<option value='3'>内部员工</option>
									-->
								</select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
<input name="Cmd" type="hidden" value="10">
<input name="Id"  type="hidden" value="***">
<input name="Sid" type="hidden" value="<%=Sid%>">
<input name="Func_Sub_Id" type="hidden" value="<%=currStatus.getFunc_Sub_Id()%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
function doNO()
{
	location = "Crm_Info.jsp?Sid=<%=Sid%>";
}

function doAdd()
{
  if(Crm_Info_Add.CName.value.Trim().length < 1)
  {
    alert("请输入名称!");
    return;
  }
  if(Crm_Info_Add.Tel.value.Trim().length < 1)
  {
    alert("请输入联系电话!");
    return;
  }
  if(Crm_Info_Add.Brief.value.Trim().length < 1)
  {
    alert("请输入简称!");
    return;
  }
  if(Crm_Info_Add.CTime.value.Trim().length < 1)
  {
    alert("请选择登记日期!");
    return;
  }
  if(confirm("信息无误,确定提交?"))
  {
  	Crm_Info_Add.submit();
  }
}
</SCRIPT>
</html>