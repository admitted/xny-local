<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<%@ page import="util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  CorpInfoBean Corp_Info = (CorpInfoBean)session.getAttribute("Corp_Info_" + Sid);
	ArrayList Device_Detail = (ArrayList)session.getAttribute("Device_Detail_" + Sid);
	String Station_Info = "";
	if(null != Corp_Info)
	{
		Station_Info = Corp_Info.getStation_Info();
		if(null == Station_Info)
		{
			Station_Info = "";
		}
	}
	
%>
<body  style=" background:#CADFFF">
<form name="Device_Detail" action="Device_Detail.do" method="post" target="mFrame">

<div id="down_bg_2">
  <div id="cap"><img src="../skin/images/device_detail.gif"></div><br><br><br>
  <div id="right_table_center">
  	<table width="90%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30' valign='middle'>
				<td width='100%' align='right' colspan=2>
					<img src="../skin/images/mini_button_add.gif" style='cursor:hand;' onClick='doAdd()'>
				</td>
			</tr>
			<tr height='30' valign='middle'>
				<td width='100%' align='center' colspan=2>
					<table width="100%" style='margin:auto;' border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			 			<tr>
							<td width="10%" class="table_deep_blue">编 号</td>
							<td width="10%" class="table_deep_blue">简 称</td>
							<td width="10%" class="table_deep_blue">类 型</td>
							<td width="10%" class="table_deep_blue">状 态</td>
							<td width="10%" class="table_deep_blue">在线状态</td>
						</tr>
						<%
						if(Device_Detail != null)
						{
							int sn = 0;
							Iterator iterator = Device_Detail.iterator();
							while(iterator.hasNext())
							{
								DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();				
								String Id = statBean.getId();
								String Brief = statBean.getBrief();							
								String Status = statBean.getStatus();
								String CType = statBean.getCType();
								String OnOff = statBean.getOnOff();
								
								String str_Status = "";
								switch(Integer.parseInt(Status))
								{
									case 0:
											str_Status = "启用";
										break;
									case 1:
											str_Status = "注销";
										break;
								}
								
								String str_CType = "无";
								if(null != CType && Station_Info.trim().length() > 0)
								{
									String[] StationList = Station_Info.split(",");
									for(int i=0; i<StationList.length; i++)
									{
							    	if(CType.equals(CommUtil.IntToStringLeftFillZero(i+1, 4)))
							    	{
									  	str_CType = StationList[i];
									  }
									}
								}
								
								String str_OnOff = "";
								switch(Integer.parseInt(OnOff))
								{
									case 0:
											str_OnOff = "在线";
										break;
									case 1:
											str_OnOff = "离线";
										break;
								}
								
								sn++;
						%>
								<tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>		
									<td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><a href="#" title="点击编辑" onClick="doEdit('<%=Id%>')"><U><%=Id%></U></a></td>
									<td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=Brief%></td>		  
								  <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=str_CType%>&nbsp;</td>
								  <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=str_Status%></td>
								  <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=str_OnOff%></td>
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
</form>
</body>
<SCRIPT LANGUAGE=javascript>	
if(<%=currStatus.getResult().length()%> > 0)
   alert("<%=currStatus.getResult()%>");
<%
currStatus.setResult("");
session.setAttribute("CurrStatus_" + Sid, currStatus);
%>

function doAdd()
{
	location = "Device_Detail_Add.jsp?Sid=<%=Sid%>";
}

function doEdit(pId)
{
	location = "Device_Detail_Edit.jsp?Sid=<%=Sid%>&Id="+pId;
}
</SCRIPT>
</html>