<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<%@ page import="util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>�����Ŀ��Ϣ</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type="text/javascript" src="../skin/js/util.js"></script>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
	String       Sid            = CommUtil.StrToGB2312(request.getParameter("Sid"));
	CurrStatus   currStatus     = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	
	ArrayList User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
  
%>
<body style="background:#CADFFF">
<form name="Dev_Ctrl_Add"  action="Dev_Ctrl.do" method="post" target="mFrame">
<div id="down_bg_2">
	<div id="cap"></div><br><br><br>
	<div id="right_table_center">
		<table width="60%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30'>
				<td width='100%' align='right'>
					<img src="../skin/images/mini_button_submit.gif" style='cursor:hand;' onclick='doAdd()'>
					<img src="../skin/images/button10.gif"           style='cursor:hand;' onclick='doNO()'>
				</td>
			</tr>
			<tr height='30'>
				<td width='100%' align='center'>
					<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
						<tr height='30'>
							<td width='20%' align='center'>վ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
							<td width='30%' align='left'>
								<select  name='cpm_Id' style='width:100px;height:20px' onChange="doSelect()" >				
									<%								
									if(null != User_Device_Detail)
									{
										Iterator iterator = User_Device_Detail.iterator();
										while(iterator.hasNext())
										{
											DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
									%>
											<option value='<%=statBean.getId()%>'><%=statBean.getBrief()%></option>
									<%
										}
									}
									%>
								</select>
							</td>
							<td width='20%' align='center'>�豸����</td>
							<td width='30%' align='left'>
								<input type='text' name='dev_Name' style='width:96%;height:20px;' value='' maxlength='20'>							        
							</td>
						</tr>					
						<tr height='30'>
							<td width='20%' align='center'>�豸����</td>
							<td width='30%' align='left'>
								<input type='text' name='dev_Type_Name' style='width:96%;height:20px;' value='' maxlength='20'>		
							</td>	
							<td width='20%' align='center'>�豸���</td>
							<td width='30%' align='left'>
								<input type='text' name='dev_Id' style='width:96%;height:20px;' value='' maxlength='20'>
							</td>	
						</tr>			
						<tr height='30'>
							<td width='20%' align='center'>���ͱ��</td>
							<td width='30%' align='left'>
								<input type='text' name='dev_Type' style='width:96%;height:20px;' value='' maxlength='20'>		
							</td>	
							<td width='20%' align='center'>�򿪱���</td>
							<td width='30%' align='left'>
								<input type='text' name='on_Action' style='width:96%;height:20px;' value='' maxlength='20'>
							</td>	
						</tr>	
						<tr height='30'>
							<td width='20%' align='center'>��ѯ����</td>
							<td width='30%' align='left'>
								<input type='text' name='select_Action' style='width:96%;height:20px;' value='' maxlength='20'>		
							</td>	
							<td width='20%' align='center'>�رձ���</td>
							<td width='30%' align='left'>
								<input type='text' name='off_Action' style='width:96%;height:20px;' value='' maxlength='20'>
							</td>	
						</tr>	
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
<input name="Cmd"   type="hidden" value="10">
<input name="Sid"   type="hidden" value="<%=Sid%>">
</form>
</body>

<SCRIPT LANGUAGE=javascript>

function doAdd()
{
	/*if(Flag == 0)
  {
  	alert("��ĿID�����ظ����������������룡");
  	return;
  }*/
  if(Dev_Ctrl_Add.CName.value.Trim().length < 1)
  {
    alert("��������Ŀ����!");
    return;
  }  
  if(confirm("��Ϣ����,ȷ���޸�?"))
  {
  	Dev_Ctrl_Add.submit();
  }
}
function doNO()
{
	location = "Dev_Ctrl.jsp?Sid=<%=Sid%>";
}
</SCRIPT>
</html>