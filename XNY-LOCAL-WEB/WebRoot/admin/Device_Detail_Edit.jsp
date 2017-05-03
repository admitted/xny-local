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
<script type='text/javascript' src='../skin/js/zDrag.js' charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/zDialog.js' charset='gb2312'></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
<STYLE>
	.mydiv {
	FONT-SIZE: 12px; BORDER-TOP: #3491d6 1px solid; HEIGHT: 535px; BORDER-RIGHT: #3491d6 1px solid; WIDTH: 705px; BORDER-BOTTOM: #3491d6 1px solid; POSITION: absolute; FONT-WEIGHT: bold; TEXT-ALIGN: center; LEFT: 20%; BORDER-LEFT: #3491d6 1px solid; Z-INDEX: 999; LINE-HEIGHT: 40px; TOP: 5%; BACKGROUND-COLOR: #3491d6
	}
</STYLE>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
	CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	CorpInfoBean Corp_Info = (CorpInfoBean)session.getAttribute("Corp_Info_" + Sid);
 	String Station_Info = "";
	if(null != Corp_Info)
	{
		Station_Info = Corp_Info.getStation_Info();
		if(null == Station_Info)
		{
			Station_Info = "";
		}
	}
	
	ArrayList Device_Detail = (ArrayList)session.getAttribute("Device_Detail_" + Sid);
 	String Id = request.getParameter("Id");
  String CName = "";
	String Brief = "";
	String Status = "0";
	String CType = "";
	String CTime = "";
	String Memo = "";
	String Link_Url = "";
	String Link_Port = "";
	String Link_Id = "";
	String Link_Pwd = "";
	String Pwd = "";
	String Unit_Price = "";
	String Scene_Img = "";
	
	if(Device_Detail != null)
	{
		Iterator iterator = Device_Detail.iterator();
		while(iterator.hasNext())
		{
			DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
			if(statBean.getId().equals(Id))
			{
				CName = statBean.getCName();
				Brief = statBean.getBrief();
				Status = statBean.getStatus();
				CType = statBean.getCType();
				CTime = statBean.getCTime();
				Memo = statBean.getMemo();
				Link_Url = statBean.getLink_Url();
				Link_Port = statBean.getLink_Port();
				Link_Id = statBean.getLink_Id();
				Link_Pwd = statBean.getLink_Pwd();
				Pwd = statBean.getPwd();	
				Unit_Price = statBean.getUnit_Price();	
				Scene_Img = statBean.getScene_Img();	
				
				if(null == CType){CType = "";}
				if(null == Memo){Memo = "";}
				if(null == Link_Url){Link_Url = "";}
				if(null == Link_Port){Link_Port = "";}
				if(null == Link_Id){Link_Id = "";}
				if(null == Link_Pwd){Link_Pwd = "";}
				if(null == Pwd){Pwd = "";}
				if(null == Scene_Img){Scene_Img = "";}
				if(null == Unit_Price){Unit_Price = "";}
			}
		}
 	}
 	
%>
<body style="background:#CADFFF">
<form name="Device_Detail_Edit" action="Device_Detail.do" method="post" target="mFrame">
<DIV id="detail_Div" class=mydiv style="MARGIN: auto; DISPLAY: none">
	<IFRAME id=childFrame style="HEIGHT: 100%; WIDTH: 100%" src="Device_Detail_Draging.do?Sid=<%=Sid%>&Cpm_Id=<%=Id%>&Scene_Img=<%=Scene_Img%>" frameBorder=0 allowTransparency name=childFrame scrolling=no></IFRAME>
</DIV>
<div id="down_bg_2">
	<div id="cap"><img src="../skin/images/device_detail.gif"></div><br><br><br>
	<div id="right_table_center">
		<table width="60%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='30'>
				<td width='100%' align='right'>
					<img src="../skin/images/mini_button_equip.gif"       style="cursor:pointer;" onClick="doScene()">
					<img src="../skin/images/mini_button_equip_alert.gif" style="cursor:pointer;" onClick="doAlert('<%=Id%>')">
					<img src="../skin/images/mini_button_submit.gif"      style="cursor:pointer;" onClick="doEdit()">
					<img src="../skin/images/button10.gif"                style="cursor:pointer;" onclick="doNO()">
				</td>
			</tr>
			<tr height='30'>
				<td width='100%' align='center'>
					<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">		
						<tr height='30'>
							<td width='20%' align='center'>编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</td>
							<td width='30%' align='left'>
								<%=Id%><input type='hidden' name='Id' value='<%=Id%>'>
							</td>
							<td width='20%' align='center'>名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</td>
							<td width='30%' align='left'>
								<input type='text' name='CName' style='width:90%;height:18px;' value='<%=CName%>' maxlength='15'>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</td>
							<td width='30%' align='left'>
								<input type='text' name='Pwd' style='width:90%;height:18px;' value='<%=Pwd%>' maxlength='20'>
							</td>
							<td width='20%' align='center'>简&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</td>
							<td width='30%' align='left'>
								<input type='text' name='Brief' style='width:90%;height:18px;' value='<%=Brief%>' maxlength='5'>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态</td>
							<td width='30%' align='left'>
								<select name="Status" style="width:92%;height:20px;">
							  	<option value='0' <%=Status.equals("0")?"selected":""%>>启用</option>
							  	<option value='1' <%=Status.equals("1")?"selected":""%>>注销</option>
							  </select>
							</td>
							<td width='20%' align='center'>类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型</td>
							<td width='30%' align='left'>
								<select name="CType" style="width:92%;height:20px"> 
								<%
								if(Station_Info.trim().length() > 0)
								{
									String[] StationList = Station_Info.split(",");
								  String pCType_Id = "";
								  String pCType_Name = "";
								  for(int i=0; i<StationList.length; i++)
								  {
										pCType_Id = CommUtil.IntToStringLeftFillZero(i+1, 4);
										pCType_Name = StationList[i];
								%>
								    <option value="<%=pCType_Id%>" <%=pCType_Id.equals(CType)?"selected":""%>><%=pCType_Name%></option>
								<%
		    					}
								}
								%>
								</select>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>投运时间</td>
							<td width='30%' align='left'>
								<input name='CTime' type='text' style='width:90%;height:18px;' value='<%=CTime%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
							</td>
							<td width='20%' align='center'>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</td>
							<td width='30%' align='left'>
								<input type='text' name='Memo' style='width:90%;height:18px;' value='<%=Memo%>' maxlength='64'>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>链接IP</td>
							<td width='30%' align='left'>
								<input type='text' name='Link_Url'  style='width:90%;height:18px;' value='<%=Link_Url%>' maxlength='20'>
							</td>
							<td width='20%' align='center'>链接Port</td>
							<td width='30%' align='left'>
								<input type='text' name='Link_Port' style='width:90%;height:18px;' value='<%=Link_Port%>' maxlength='6'>
							</td>
						</tr>
						<tr height='30'>
							<td width='20%' align='center'>链接账户</td>
							<td width='30%' align='left'>
								<input type='text' name='Link_Id' style='width:90%;height:18px;' value='<%=Link_Id%>' maxlength='20'>
							</td>
							<td width='20%' align='center'>加气价格</td>
							<td width='30%' align='left'>
								<input type='text' name='Unit_Price' style='width:90%;height:18px;' value='<%=Unit_Price%>' maxlength='6'>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
<input name="Cmd" type="hidden" value="11">
<input name="Sid" type="hidden" value="<%=Sid%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
function doNO()
{
	location = "Device_Detail.jsp?Sid=<%=Sid%>";
}
function doScene()
{
	document.getElementById('detail_Div').style.display = 'block';
}

//站级信息-告警信息配置
function doAlert(pCpm_Id)
{
	  //alert(pCpm_Id);
    var Pdiag = new Dialog();
    Pdiag.Top = "50%";
    Pdiag.Width = 900;
    Pdiag.Height = 500;
    Pdiag.Title = "告警信息配置";
    Pdiag.URL = 'Device_Detail_Alert.do?Sid=<%=Sid%>&Cpm_Id=' + pCpm_Id+'&Cmd=1';
    Pdiag.show();
}

function doEdit()
{
  if(Device_Detail_Edit.Id.value.Trim().length != 10)
  {
    alert("请输入10位编号!");
    return;
  }
  if(Device_Detail_Edit.CName.value.Trim().length < 1)
  {
    alert("请输入名称!");
    return;
  }
  if(Device_Detail_Edit.Brief.value.Trim().length < 1)
  {
    alert("请输入简称!");
    return;
  }
  if(Device_Detail_Edit.CType.value.Trim().length < 1)
  {
    alert("请选择类型!");
    return;
  }
  if(Device_Detail_Edit.CTime.value.Trim().length < 1)
  {
    alert("请输入投运时间!");
    return;
  }
  if(Device_Detail_Edit.Unit_Price.value.Trim().length < 1)
  {
    alert("请输入加气价格!");
    return;
  }
  if(confirm("信息无误,确定提交?"))
  {
  	Device_Detail_Edit.submit();
  }
}
</SCRIPT>
</html>