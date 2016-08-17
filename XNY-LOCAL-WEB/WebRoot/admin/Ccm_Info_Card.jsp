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
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
	
	String Sid    = CommUtil.StrToGB2312(request.getParameter("Sid"));
	String Crm_Id = CommUtil.StrToGB2312(request.getParameter("Crm_Id"));
	String Id     = CommUtil.StrToGB2312(request.getParameter("Id"));
	String IC     = CommUtil.StrToGB2312(request.getParameter("IC"));
	CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	
%>
<body style="background:#e0e6ed">
<form name="Ccm_Info_Card"  action="" method="post" target="_self">
	<table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
		<tr height='30'>
			<td width='100%' align='center'>
				<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">		
					<tr height='30'>
						<td width='30%' align='center'>卡 号</td>
						<td width='70%' align='left'>
							<input type='text' name='IC' style='width:96%;height:18px;' value='' maxlength='20'>
						</td>
					</tr>
					<tr height='30'>
						<td width='30%' align='center'>状 态</td>
						<td width='70%' align='left'>
							<select name='Status' style='width:98%;height:20px;'>
								<option value='0'>启用</option>
								<option value='1'>注销</option>					
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr height='40'>
			<td width='100%' align='center'>
				<img src="../skin/images/mini_button_submit.gif" style='cursor:hand;' onClick='doEdit()'>
				<img src="../skin/images/button10.gif"           style='cursor:hand;' onclick='doNO()'>
			</td>
		</tr>
	</table>
</div>
</form>
</body>
<SCRIPT LANGUAGE=javascript>
function doNO()
{
	parent.closeDiv();
}

function doEdit()
{
  if(Ccm_Info_Card.IC.value.Trim().length < 1)
  {
    alert("请输入卡号!");
    return;
  }
	if('<%=IC%>'.indexOf(Ccm_Info_Card.IC.value) >= 0)
	{
		alert('当前已绑定此卡号!');
		return;
	}
	var IC = Ccm_Info_Card.IC.value + ',' + Ccm_Info_Card.Status.value + ';';
  if(confirm("信息无误,确定提交?"))
  {
  	if(window.XMLHttpRequest)
		{
			reqEdit = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqEdit = new ActiveXObject('Microsoft.XMLHTTP');
		}
		reqEdit.onreadystatechange = function()
		{
			var state = reqEdit.readyState;
			if(state==4)
			{
				var resp = reqEdit.responseText;
				if(resp != null && resp == "0000")
				{	
					alert("添加成功!");
					parent.doSelect();
					return;
				}
				else
				{
					alert("失败,请重新操作!");
					return;
				}
			}
		};
		var url = 'Ccm_Info_Card.do?Cmd=13&Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>&Id=<%=Id%>&IC='+IC+'&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&currtime='+new Date();
		reqEdit.open("get", url);
		reqEdit.setRequestHeader('If-Modified-Since', '0');
		reqEdit.send(null);
		return true;
  }
}
</SCRIPT>
</html>