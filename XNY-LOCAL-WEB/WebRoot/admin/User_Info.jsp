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
  String Dept = "";
  if(Corp_Info != null)
	{
		Dept = Corp_Info.getDept();
    if(Dept == null)
    {
    	Dept = "";
    }
 	}
 	
  ArrayList User_Info     = (ArrayList)session.getAttribute("User_Info_" + Sid);
  ArrayList FP_Role       = (ArrayList)session.getAttribute("FP_Role_" + Sid);
  ArrayList Manage_Role   = (ArrayList)session.getAttribute("Manage_Role_" + Sid);
  ArrayList User_Position = (ArrayList)session.getAttribute("User_Position_" + Sid);
  
  int sn = 0;
  
%>
<body style="background:#CADFFF">
<form name="User_Info"  action="User_Info.do" method="post" target="mFrame">
<div id="down_bg_2">
	<div id="cap"><img src="../skin/images/cap_user_info.gif"></div><br><br><br>
	<div id="right_table_center">
		<table width="90%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30'>
				<td width='30%' align='left'>
					<select name='Func_Corp_Id' style='width:120px;height:20px;' onchange="doSelect();">
						<option value='99' <%=currStatus.getFunc_Corp_Id().equals("99")?"selected":""%>>全部</option>
						<%
						if(Dept.trim().length() > 0)
						{
							String[] DeptList = Dept.split(",");
						  String pDept_Id = "";
						  String pDept_Name = "";
						  for(int i=0; i<DeptList.length; i++)
						  {
								pDept_Id = CommUtil.IntToStringLeftFillZero(i+1, 2);
								pDept_Name = DeptList[i];
						%>
						    <option value="<%=pDept_Id%>" <%=currStatus.getFunc_Corp_Id().equals(pDept_Id)?"selected":""%>><%=pDept_Name%></option>
						<%
    					}
						}
						%>
					</select>

				</td>				
				<td width='70%' align='right'>
					<img src="../skin/images/mini_button_add.gif" style='cursor:hand;' onClick='doAdd()'>
				</td>
			</tr>
			<tr height='30'>
				<td width='100%' align='center' colspan=2>
					<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
						<tr>
							<td width="10%" class="table_deep_blue">帐号</td>
							<td width="10%" class="table_deep_blue">姓名</td>
							<td width="10%" class="table_deep_blue">部门</td>
							<td width="10%" class="table_deep_blue">岗位</td>
							<td width="10%" class="table_deep_blue">功能权限</td>
							<td width="10%" class="table_deep_blue">管理权限</td>
						</tr>
						<%
						if(User_Info != null)
						{
							Iterator iterator = User_Info.iterator();
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
							  if(null != Dept_Id && Dept.trim().length() > 0)
							 	{
							 		String[] DeptList = Dept.split(",");
									for(int i=0; i<DeptList.length; i++)
									{
							    	if(Dept_Id.equals(CommUtil.IntToStringLeftFillZero(i+1, 2)))
							    	{
									  	Dept_Name = DeptList[i];
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
							<td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center style="cursor:hand " onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';"  title="点击查看" onClick="doEdit('<%=Id%>')"><U><%=Id%></U></td>
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=CName%></td>  
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=Dept_Name%></td>
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=PositionName%></td>
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=FpName%></td>			
					    <td <%=((!Status.equals("0"))?"class='font_gray'":"")%> align=center><%=RoleName%></td>			    
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
<input name="Cmd" type="hidden" value="1">
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
	User_Info.submit();
}

function doAdd()
{
	location = "User_Info_Add.jsp?Sid=<%=Sid%>";
}

function doEdit(pId)
{  
	location = "User_Info_Edit.jsp?Sid=<%=Sid%>&Id="+pId;
}
</SCRIPT>
</html>