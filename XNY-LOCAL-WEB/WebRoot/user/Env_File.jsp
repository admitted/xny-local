<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<%@ page import="util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../skin/js/util.js"></script>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  
	UserInfoBean UserInfo = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	ArrayList User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	ArrayList User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
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
								String Dept_Id = UserInfo.getDept_Id();
								if(Dept_Id.length()>3){Manage_List = Dept_Id; }
%>
<body  style=" background:#CADFFF">
<form name="Env_File" action="Env_File.do" method="post" target="mFrame" enctype="multipart/form-data">
  <table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
		<tr height='30'>
			<td align=center>
				<strong>图片大小：300*128格式：.jpg </strong>
			</td>
		</tr>		
		<tr height='30'>
			<td align=center>
				<strong>上传站点：</strong>
				<select  name='Func_Cpm_Id' style='width:40%;height:20px'  >									
						<%					
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
				
			</td>
		</tr>		
		<tr height='30'>			
			<td align=center>
				&nbsp;&nbsp;&nbsp;<input name='file' type='file' style='width:90%;height:20px;' title='文档上传'>
			</td>
		</tr>	
		<tr height='30'>
		<td width='100%' align='center' >
			<img src="../skin/images/mini_button_submit.gif" style='cursor:hand;' onClick='doEdit()'>
		</td>
	</tr>
	</table> 
</form>
</body>
<script LANGUAGE="javascript">
	
function doEdit()
{	
	if(Env_File.file.value.Trim().length > 0)
  {
  	if(Env_File.file.value.indexOf('.jpg') == -1 )  	
		{
			alert('请确认图片格式,支持.jpg格式!');
			return;
		}
  }				
	if(confirm('信息无误,确定提交?'))
  { 	
		Env_File.submit();
  }	
}



</script>
</html>