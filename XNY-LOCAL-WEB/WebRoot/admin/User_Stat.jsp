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
	CurrStatus currStatus   = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  ArrayList User_Stat     = (ArrayList)session.getAttribute("User_Stat_" + Sid);
  ArrayList FP_Role       = (ArrayList)session.getAttribute("FP_Role_" + Sid);
  ArrayList Manage_Role   = (ArrayList)session.getAttribute("Manage_Role_" + Sid);
  ArrayList User_Position = (ArrayList)session.getAttribute("User_Position_" + Sid);
  ArrayList Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
  UserInfoBean UserInfo = (UserInfoBean)session.getAttribute("UserInfo_" + Sid); 
  ArrayList User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
  String ManageId = UserInfo.getManage_Role();
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
								String Dept_Id_t = UserInfo.getDept_Id();
								if(Dept_Id_t.length()>3){Manage_List = Dept_Id_t; }
					
  
  
  
  int sn = 0;
  
%>
<body style="background:#CADFFF">
<form name="User_Stat"  action="User_Info.do" method="post" target="mFrame">
<div id="down_bg_2">
	<div id="cap"><img src="../skin/images/cap_user_stat.gif"></div><br><br><br>
	<div id="right_table_center">
		<table width="90%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30'>
				<td width='30%' align='left'>
					<select name='Func_Corp_Id' style='width:120px;height:20px;' onchange="doSelect();">						
						<%
								if(Manage_List.length() > 0 && null != Device_Detail)
								{
									Iterator iterator = Device_Detail.iterator();
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
					人员姓名：
					<input type='text' name='CName' style='width:100px;height:16px;' value=''>	
				</td>
				<td width='70%' align='right'>
					<img src="../skin/images/mini_button_add.gif" style='cursor:hand;' onClick='doAdd()'>
				</td>
			</tr>
			<tr height='30'>
				<td width='100%' align='center' colspan=2>
					<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
						<tr>
							<td width="14%" class="table_deep_blue">帐号</td>
							<td width="14%" class="table_deep_blue">姓名</td>
							<td width="14%" class="table_deep_blue">部门</td>
							<td width="14%" class="table_deep_blue">岗位</td>
							<td width="14%" class="table_deep_blue">功能权限</td>
							<td width="14%" class="table_deep_blue">管理权限</td>
							<td width="14%" class="table_deep_blue">操作</td>
						</tr>
						<%
						if(User_Stat != null)
						{
							Iterator iterator = User_Stat.iterator();
							while(iterator.hasNext())
							{
								UserInfoBean statBean = (UserInfoBean)iterator.next();
								String Id = statBean.getId();
								String CName = statBean.getCName();
								String Status = statBean.getStatus();
								String Dept_Id = statBean.getDept_Id();
								String RoleId = statBean.getManage_Role();
						    String FpId = statBean.getFp_Role();
						    String Job_Position = statBean.getJob_Position();
								
								String Dept_Name = "无";
								if(Dept_Id.length() > 0 && Device_Detail != null)
								{
									for(int i=0; i<Device_Detail.size(); i++)
									{
										DeviceDetailBean Detail = (DeviceDetailBean)Device_Detail.get(i);
										if(Detail.getId().equals(Dept_Id))
										{
											Dept_Name = Detail.getBrief();
											break;
										}
									}
								}
								
							 	String FpName = "无";
								if(FpId.length() > 0 && FP_Role != null)
								{
									for(int i=0; i<FP_Role.size(); i++)
									{
										UserRoleBean Role = (UserRoleBean)FP_Role.get(i);
										if(Role.getId().equals(FpId))
										{
											FpName = Role.getCName();
											break;
										}
									}
								}
								
						    String RoleName = "无";
						    if(RoleId.length() > 0 && Manage_Role != null)
								{
									for(int i=0; i<Manage_Role.size(); i++)
									{
										UserRoleBean Role = (UserRoleBean)Manage_Role.get(i);
										if(Role.getId().equals(RoleId))
										{
											RoleName = Role.getCName();
											break;
										}
									}
								}
								
								String PositionName = "无";
								if(Job_Position.length() > 0 && User_Position != null)
								{
									for(int i=0; i<User_Position.size(); i++)
									{
										UserPositionBean Position = (UserPositionBean)User_Position.get(i);
										if(Position.getId().equals(Job_Position))
										{
											PositionName = Position.getCName();
											break;
										}
									}
								}
								
								sn ++;
						%>
					  <tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
							<td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=Id%></td>
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=CName%></td>  
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=Dept_Name%></td>
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=PositionName%></td>
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=FpName%></td>			
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=RoleName%></td>			
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><a href="#" onClick="doEdit('<%=Id%>')"><font color="red"><U>修改</U></font></a>、<a href="#" onClick="doDel('<%=Id%>')"><font color="red"><U>删除</U></font></a></td>	   
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
<input name="Cmd" type="hidden" value="3">
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
	User_Stat.submit();
}

function doAdd()
{
	location = "User_Stat_Add.jsp?Sid=<%=Sid%>";
}

function doEdit(pId)
{  
	location = "User_Stat_Edit.jsp?Sid=<%=Sid%>&Id="+pId;
}
function doDel(pId)
{
	if(confirm("是否删除本条记录?"))
		{
			location="User_Info.do?Cmd=3&Sid=<%=Sid%>&Func_Corp_Id="+User_Stat.Func_Corp_Id.value+"&Func_Type_Id=333&Id="+pId;
		}
}
</SCRIPT>
</html>