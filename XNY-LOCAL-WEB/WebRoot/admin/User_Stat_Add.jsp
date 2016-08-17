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
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
	CurrStatus currStatus   = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
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
					
%>
<body style="background:#CADFFF">
<form name="User_Stat_Add"  action="User_Info.do" method="post" target="mFrame">
<div id="down_bg_2">
	<div id="cap"><img src="../skin/images/cap_user_stat.gif"></div><br><br><br>
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
							<td width='20%' align='center'>帐&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</td>
							<td width='30%' align='left'>
								<table width='98%'>
									<tr>
										<td width='60%' align='left'><input type='text' name='Id' style='width:98%;height:18px;' value='' maxlength='20' onkeyup="doCheck(this.value)"><td>
										<td width='40%' align='left' id='ErrorMsg' style="color:red;">&nbsp;</td>
									</tr>
								</table>
							</td>
							<td width='20%' align='center'>部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门</td>
							<td width='30%' align='left'>
								<select name="Dept_Id" style="width:97%;height:20px">
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
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</td>
							<td width='30%' align='left'>
								<input type='text' name='CName' style='width:96%;height:20px;' value='' maxlength='6'>
							</td>
							<td width='20%' align='center'>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</td>
							<td width='30%' align='left'>
								<select name='Sex' style='width:97%;height:20px'>
									<option value='0'>男</option>
									<option value='1'>女</option>
								</select>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话</td>
							<td width='30%' align='left'>
								<input type='text' name='Tel' style='width:96%;height:20px;' value='' maxlength='11'>
							</td>
							<td width='20%' align='center'>入职时间</td>
							<td width='30%' align='left'>
								<input type="text" name="Birthday" onClick="WdatePicker({readOnly:true})" class="Wdate" maxlength="10" style='width:97%;'>
							</td>
						</tr>
						<tr height='30'>	
							<td width='20%' align='center'>工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</td>
							<td width='30%' align='left'>
								<input type='text' name='Job_Id' style='width:96%;height:18px;' value='' maxlength='20'>
							</td>
							<td width='20%' align='center'>岗&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位</td>
							<td width='30%' align='left'>
								<select name='Job_Position' style='width:97%;height:20px'>
								<%
								if(User_Position != null && User_Position.size() > 0)
								{
									for(int i=0; i<User_Position.size(); i++)
									{
										UserPositionBean Position = (UserPositionBean)User_Position.get(i);
										if(Position.getStatus().equals("0"))
										{
											String R_Id = Position.getId();
											String R_CName = Position.getCName();
								%>
											<option value='<%=R_Id%>'><%=R_CName%></option>
								<%
										}
									}
								}
								%>
								</select>
							</td>
						</tr>
						<tr height='30'>	
							<td width='20%' align='center'>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态</td>
							<td width='30%' align='left'>
								<select name='Status' style='width:97%;height:20px'>
									<option value='0'>启用</option>
									<option value='1'>注销</option>
								</select>
							</td>
							<td width='20%' align='center'>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址</td>
							<td width='30%' align='left'>
								<input type='text' name='Addr' style='width:96%;height:20px;' value='' maxlength='60'>
							</td>						
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>功能权限</td>
							<td width='30%' align='left'>
								<select name='Fp_Role' style='width:97%;height:20px'>	
								<%
								if(FP_Role != null && FP_Role.size() > 0)
								{
									for(int i=0; i<FP_Role.size(); i++)
									{
										UserRoleBean Role = (UserRoleBean)FP_Role.get(i);
										if(Role.getId().length() == 3)
										{
											String R_Id = Role.getId();
											String R_CName = Role.getCName();
											if(Dept_Id_t.length()>3){
											if(R_Id.equals("003") ||R_Id.equals("004") ||R_Id.equals("005") ||R_Id.equals("006") ||R_Id.equals("007") ||R_Id.equals("008") ||R_Id.equals("009")  ){
								%>
											<option value='<%=R_Id%>'><%=R_CName%></option>
								<%
												}
											}
										else{
								%>								
											<option value='<%=R_Id%>'><%=R_CName%></option>
								<%
											}
											
										}
									}
								}
								%>									
								</select>
							</td>
							<td width='20%' align='center'>管理权限</td>
							<td width='30%' align='left'>
								<select name='Manage_Role' style='width:97%;height:20px'>	
								<%
								if(Manage_Role != null && Manage_Role.size() > 0)
								{
									for(int i=0; i<Manage_Role.size(); i++)
									{
										UserRoleBean Role = (UserRoleBean)Manage_Role.get(i);
										if(Role.getId().length() == 4)
										{
											String R_Id = Role.getId();
											String R_CName = Role.getCName();
								%>
											<option value='<%=R_Id%>'><%=R_CName%></option>
								<%
										}
									}
								}
								%>									
								</select>
							</td>
						</tr>
						<!--
						<tr height='30'>	
							<td width='20%' align='center'>华平账号</td>
							<td width='30%' align='left'>
								<input type='text' name='HP_LoginId'  style='width:96%;height:18px;' value='' maxlength='32'>
							</td>
							<td width='20%' align='center'>华平密码</td>
							<td width='30%' align='left'>
								<input type='text' name='HP_LoginPwd' style='width:96%;height:18px;' value='' maxlength='32'>
							</td>
						</tr>
						<tr height='30'>	
							<td width='20%' align='center'>华平 IP</td>
							<td width='30%' align='left'>
								<input type='text' name='HP_LoginIp'   style='width:96%;height:18px;' value='' maxlength='20'>
							</td>
							<td width='20%' align='center'>华平Port</td>
							<td width='30%' align='left'>
								<input type='text' name='HP_LoginPort' style='width:96%;height:18px;' value='' maxlength='5'>
							</td>
						</tr>
						-->
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
<input name="Cmd" type="hidden" value="10">
<input name="Sid" type="hidden" value="<%=Sid%>">
<input name="Func_Corp_Id" type="hidden" value="<%=currStatus.getFunc_Corp_Id()%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
var Flag = 0;
//自定义帐号检测
function doCheck(pId)
{
	if(pId.Trim().length == 0)
	{
		Flag = 0;
		document.getElementById("ErrorMsg").innerText = " ";
		return;
	}
	if(pId.Trim().length > 0 && pId.Trim().length < 2)
	{
		 document.getElementById("ErrorMsg").innerText = " X 需2-20位!";
		 Flag = 0;
		 return;
	}
	//Ajax登入提交
	if(window.XMLHttpRequest)
  {
			req = new XMLHttpRequest();
  }
  else if(window.ActiveXObject)
  {
			req = new ActiveXObject("Microsoft.XMLHTTP");
  }		
	//设置回调函数
	req.onreadystatechange = callbackCheckName;
	var url = "IdCheck.do?Id="+pId+"&Sid=<%=Sid%>&Func_Corp_Id=<%=currStatus.getFunc_Corp_Id()%>";
	req.open("post",url,true);
	req.send(null);
	return true;
}
function callbackCheckName()
{
		var state = req.readyState;
		if(state==4)
		{
			var resp = req.responseText;			
			var str = "";
			if(resp != null && resp == '0000')
			{
				 document.getElementById("ErrorMsg").innerText = " √ 可用!";
				 Flag = 1;
				 return;
			}
			else if(resp != null && resp == '3006')
			{
				 document.getElementById("ErrorMsg").innerText = " X 已存在!";
				 Flag = 0;
				 return;
			}
		}
}

function doAdd()
{
	if(Flag == 0)
  {
  	alert("帐号有误，请重新输入！");
  	return;
  }
  if(User_Stat_Add.Dept_Id.value.Trim().length < 1)
  {
    alert("请选择部门!");
    return;
  }
  if(User_Stat_Add.CName.value.Trim().length < 1)
  {
    alert("请输入姓名!");
    return;
  }
  if(User_Stat_Add.Tel.value.Trim().length < 1)
  {
    alert("请输入联系电话!");
    return;
  }
  if(User_Stat_Add.Birthday.value.Trim().length < 1)
  {
    alert("请输入入职时间!");
    return;
  }
  if(User_Stat_Add.Job_Position.value.Trim().length < 1)
  {
    alert("请选择岗位!");
    return;
  }
  if(User_Stat_Add.Addr.value.Trim().length < 1)
  {
    alert("请输入地址!");
    return;
  }
  if(User_Stat_Add.Fp_Role.value.Trim().length < 1)
  {
    alert("请赋予当前帐号功能权限角色!");
    return;
  }
  if(User_Stat_Add.Manage_Role.value.Trim().length < 1)
  {
    alert("请赋予当前帐号管理权限角色!");
    return;
  }
  if(confirm("信息无误,确定添加?"))
  {
  	User_Stat_Add.submit();
  }
}

function doNO()
{
	location = "User_Stat.jsp?Sid=<%=Sid%>";
}
</SCRIPT>
</html>