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
	ArrayList User_Position = (ArrayList)session.getAttribute("User_Position_" + Sid);
	ArrayList Aqsc_Card_Type = (ArrayList)session.getAttribute("Aqsc_Card_Type_" + Sid);
	
%>
<body  style=" background:#CADFFF">
<form name="User_Position" action="User_Position.do" method="post" target="mFrame">
<div id="down_bg_2">
  <div id="cap"><img src="../skin/images/cap_position_info.gif"></div><br><br><br>
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
							<td width="20%" class="table_deep_blue">名 称</td>
							<td width="10%" class="table_deep_blue">状 态</td>
							<td width="40%" class="table_deep_blue">必备证件</td>
						</tr>
						<%
						if(User_Position != null)
						{
							int sn = 0;
							Iterator iterator = User_Position.iterator();
							while(iterator.hasNext())
							{
								UserPositionBean statBean = (UserPositionBean)iterator.next();
								String Id = statBean.getId();
								String CName = statBean.getCName();
								String Status = statBean.getStatus();
								String Des = statBean.getDes();
								String Card_List = statBean.getCard_List();
								if(null == Des){Des = "";}
								if(null == Card_List){Card_List = "";}
								
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
								
								String str_Card_List = "";
								if(Card_List.length() > 0 && Aqsc_Card_Type != null)
								{
									int index = 0;
									for(int i=0; i<Aqsc_Card_Type.size(); i++)
									{
										AqscCardTypeBean CardType = (AqscCardTypeBean)Aqsc_Card_Type.get(i);
										if(Card_List.contains(CardType.getId()))
										{
											index++;
											if(1 == index)
												str_Card_List += index + "、" +  CardType.getCName();
											else
												str_Card_List += "<br>" + index + "、" +  CardType.getCName();
										}
									}
								}
								
								sn++;
						%>
								<tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%> title='<%=Des%>'>
									<%
									if(Status.equals("0"))
									{
									%>
										<td align=center><a href="#" title="点击编辑" onClick="doEdit('<%=Id%>')"><U><%=Id%></U></a></td>
										<td align=left><%=CName%></td>
									  <td align=center><%=str_Status%></td>								  
									  <td align=left><%=str_Card_List%>&nbsp;</td>
									<%
									}
									else
									{
									%>
										<td align=center><a href="#" title="点击编辑" onClick="doEdit('<%=Id%>')"><font color=gray><U><%=Id%></U></font></a></td>
										<td align=left><font color=gray><%=CName%></font></td>
									  <td align=center><font color=gray><%=str_Status%></font></td>					
									  <td align=left><font color=gray><%=str_Card_List%>&nbsp;</font></td>
									<%
									}
									%>
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
	location = "User_Position_Add.jsp?Sid=<%=Sid%>";
}

function doEdit(pId)
{
	location = "User_Position_Edit.jsp?Sid=<%=Sid%>&Id="+pId;
}
</SCRIPT>
</html>