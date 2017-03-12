<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%@ taglib uri="/WEB-INF/limitvalidatetag.tld" prefix="Limit"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<meta http-equiv='x-ua-compatible' content='ie=7'/>
<link type='text/css' href='../skin/css/style.css' rel='stylesheet'/>
<script type='text/javascript' src='../skin/js/browser.js' charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/util.js' charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/des.js'></script>
<script type='text/javascript' src='http://api.map.baidu.com/api?v=1.2&services=true'></script>
<script type='text/javascript' src='../skin/js/changeMore.js'></script>
<!--Zdialog-->
<script type='text/javascript' src='../skin/js/zDrag.js'   charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/zDialog.js' charset='gb2312'></script>

<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
<style type='text/css'>
html,body{width:100%; height:100%; margin:0; padding:0;}
#container{height:100%;}
.box{height:100%; background:#0B80CC; position:absolute; width:100%;}
.mesWindow{border:#C7C5C6 1px solid;background:#CADFFF;}
.mesWindowTop{background:#3ea3f9;padding:5px;margin:0;font-weight:bold;text-align:left;font-size:12px; clear:both; line-height:1.5em; position:relative; clear:both;}
.mesWindowTop span{ position:absolute; right:5px; top:3px;}
.mesWindowContent{margin:4px;font-size:12px; clear:both;}
.mesWindow .close{height:15px;width:28px; cursor:pointer;text-decoration:underline;background:#fff}
</style>
</head>
<%
	
	String Sid                   = CommUtil.StrToGB2312(request.getParameter("Sid"));
  UserInfoBean UserInfo        = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
  ArrayList User_FP_Role       = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	ArrayList User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	ArrayList User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
	
	//权限设置
	String FpId     = UserInfo.getFp_Role();
	String ManageId = UserInfo.getManage_Role();
	String FpList   = "";
	String IdList   = "";
	
	if(null != FpId && FpId.length() > 0 && null != User_FP_Role)
	{
		Iterator roleiter = User_FP_Role.iterator();
		while(roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean)roleiter.next();
			if(roleBean.getId().equals(FpId) && null != roleBean.getPoint())
			{
				FpList = roleBean.getPoint();
			}
		}
	}
	
	if(null != ManageId && ManageId.length() > 0 && null != User_Manage_Role)
	{
		Iterator roleiter = User_Manage_Role.iterator();
		while(roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean)roleiter.next();
			if(roleBean.getId().equals(ManageId) && roleBean.getPoint() != null)
			{
				IdList += roleBean.getPoint();
			}
		}
	}
	
	//初始坐标
	double Longitude = 117.592666;
	double Latitude  = 36.670606;

	ArrayList User_User_Info  = (ArrayList)session.getAttribute("User_User_Info_" + Sid);
 	String SYS_List = "";
	if( null != User_User_Info )
	{
		Iterator iterator = User_User_Info.iterator();
		while(iterator.hasNext())
		{
			UserInfoBean usertBean = (UserInfoBean)iterator.next();		
			String sys = 	usertBean.getSys_Id();							
			SYS_List  = SYS_List+sys;
		}
	}
%>
<body style='background:#bbbdbb'>
<form name='Map' action='Map.do' method='post' target='mFrame'>
	<div id='container'></div>
	<div id='news_info' style='position:absolute;width:284px;height:100%;right:16px;top:0px;filter:alpha(Opacity=80);-moz-opacity:0.5;opacity:0.5;background-color:#bbbdbb;'>
		&nbsp;
	</div>
	<div id='menu_info' style='position:absolute;width:16px;height:100%;right:0px;top:0px;filter:alpha(Opacity=80);-moz-opacity:0.5;opacity:0.5;background-color:#bbbdbb;'>
		<img id='news_img' src='../skin/images/map2close.gif' style='width:16px;height:16px;cursor:hand;' title='收起' onclick='doOpen()'>
	</div>
</form>
</body>
<SCRIPT LANGUAGE=javascript>
//兼容性
if(1 == fBrowserRedirect() || 2 == fBrowserRedirect())
{
	window.addEventListener('onorientationchange' in window ? 'orientationchange' : 'resize', setHeight, false);
	setHeight();
}
function setHeight()
{
	document.getElementById('container').style.height = document.body.offsetHeight + 'px';
	
	if(document.getElementById('news_info').style.display == '')
		document.getElementById('container').style.width = document.body.offsetWidth - 300 + 'px';
	else
		document.getElementById('container').style.width = document.body.offsetWidth - 16  + 'px';
}
setHeight();

function doOpen()
{
	if(document.getElementById('news_info').style.display == '')
	{
		document.getElementById('news_info').style.display = 'none';
		document.getElementById('news_img').src = '../skin/images/map2open.gif';
		document.getElementById('news_img').title = '展开';
	}
	else
	{
		document.getElementById('news_info').style.display = '';
		document.getElementById('news_img').src = '../skin/images/map2close.gif';
		document.getElementById('news_img').title = '收起';
	}
	setHeight();
}



//载入地图
var map = new BMap.Map("container");                      //创建地图实例
//map.setMapType(BMAP_HYBRID_MAP);                        //默认类型为卫星、路网一体
var point = new BMap.Point(<%=Longitude%>, <%=Latitude%>);//创建中心点坐标，默认为第一家企业
map.centerAndZoom(point, 12);                             //初始化地图，设置中心点坐标和地图级别
map.addControl(new BMap.NavigationControl());             //添加一个平移缩放控件，位置可偏移、形状可改变
map.addControl(new BMap.ScaleControl());                  //添加一个比例尺控件，位置可偏移[var opts = {offset: new BMap.Size(150, 5)};map.addControl(new BMap.ScaleControl(opts));]
map.addControl(new BMap.OverviewMapControl());            //添加一个缩略图控件，位置可偏移
//map.addControl(new BMap.MapTypeControl());              //添加地图类型变换(地图-卫星-三维)，位置可偏移
map.enableScrollWheelZoom();                              //启用滚轮放大缩小
map.disableDoubleClickZoom();                             //禁止地图双击
var mkrs = new Array();

//1.添加全屏控件
function ZoomControl()
{
  this.defaultAnchor = BMAP_ANCHOR_BOTTOM_RIGHT;
 	this.defaultOffset = new BMap.Size(16, 1);
}
ZoomControl.prototype = new BMap.Control();
ZoomControl.prototype.initialize = function(map)
{
	var div = document.createElement("div");
  div.id = 'FullScreen';
  img = document.createElement("img");
	img.src = '../skin/images/map_fullscreen.gif';
	div.appendChild(img);
  div.style.cursor = "pointer";
  div.style.height = 14;
  div.style.width = 16;
  div.onclick = function(e)
  {
  	parent.window.parent.location = 'MapFullScreen.jsp?Sid=<%=Sid%>';
  }
  map.getContainer().appendChild(div);
  return div;
}
var myZoomCtrl = new ZoomControl(); 
map.addControl(myZoomCtrl);

//2.添加定义标注图标
function addMarker(point, pId, pCName, pIcon, pStatus, pX, pY, pType)
{
	switch(parseInt(pType))
	{
		case 1://站点
				var myIcon = new BMap.Icon(pIcon, new BMap.Size(pX, pY));
			 	var marker = new BMap.Marker(point, {icon: myIcon});
			 	var myLabel= new BMap.Label(pCName, {offset:new BMap.Size(0, pY)});
			 	myLabel.setStyle
			 	({    
			 		fontSize:"11px",
			 		font:"bold 10pt/12pt",
			 		border:"0",
			 		color:"#ffffff",
			 		textAlign:"center",
			 		background:"#1f76f8",
			 		cursor:"pointer"
			 	});
			 	marker.setLabel(myLabel);	 	
			 	map.addOverlay(marker);		 	
			 	marker.addEventListener("click", function()
			 	{
			 		 doEnv(pId, pCName);
				});
				mkrs.push(marker);
				if('2' == pStatus)
				{
					//marker.setAnimation(BMAP_ANIMATION_BOUNCE);
				}
			break;
	}
}

//状态更新
var reqSend = null;
function RealStatus()
{
	if(window.XMLHttpRequest)
  {
    reqSend = new XMLHttpRequest();
  }
	else if(window.ActiveXObject)
	{
    reqSend = new ActiveXObject('Microsoft.XMLHTTP');
  }
	reqSend.onreadystatechange = function()
	{
	  var state = reqSend.readyState;
	  if(state == 4)
	  {
	    if(reqSend.status == 200)
	    {
	      var Resp = reqSend.responseText;
	      if(null != Resp && Resp.length >= 4 && Resp.substring(0,4) == '0000')
	      {
	      	//1.删除
	      	for(var i=0; i<mkrs.length; i++)
	      	{
	      		map.removeOverlay(mkrs[i]);
	      	}
	      	mkrs.length = 0;
	      	
	      	//2.添加
	      	var list = Resp.substring(4).split(";");
	      	for(var i=0; i<list.length && list[i].length>0; i++)
	      	{
	      		var sublist = list[i].split(",");
	      		switch(parseInt(sublist[2]))
	      		{
	      			case 0://正常
	      					var point = new BMap.Point(sublist[3], sublist[4]);
	 								addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_green.gif', '0', '28', '28', '1');
	      				break;
	      			case 1://离线
	      					var point = new BMap.Point(sublist[3], sublist[4]);      					
	 								addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_gray.gif',  '1', '28', '28', '1'); 														
	      				break;
	      			case 2://异常
	      					if('<Limit:limitValidate userrole='<%=FpList%>' fpid='0101' ctype='1'/>' == '')
	      					{//领导模式
	      						var point = new BMap.Point(sublist[3], sublist[4]);
	      						addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_green.gif', '0', '28', '28', '1');
	      					}
	      					else
	      					{//普通模式
	      						var point = new BMap.Point(sublist[3], sublist[4]);
	      						addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_red.gif',   '2', '28', '28', '1');
	      					}
	      				break;
	      		}
	      	}
	      }     
	    }
	  }
	};
	var url = 'ToPo.do?Cmd=21&Sid=<%=Sid%>&Id=<%=IdList%>&currtime='+new Date();
	reqSend.open('post',url,false);
	reqSend.send(null);
}
setTimeout("RealStatus()", 1000);
setInterval("RealStatus()", 3*1000);

//CPM离线
function doCpmOnOff(pId, pCName, pStatus)
{
	var messContent = "<div style='width:100%;height:403px;text-align:center;border:1px solid #0068a6;overflow-x:no;overflow-y:auto;'>";
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      if(null != Resp && Resp.length >= 4 && Resp.substring(0,4) == '0000')
	      {
	      	var list = Resp.substring(4).split('^');
  				messContent += "<table align='center' style='margin:auto' cellpadding='0' cellspacing='0' border='0' width='97%'>";
  				messContent += "  <tr height='25px'>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>站点</td>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>类型</td>";
  				messContent += "    <td width='15%' align='center' style='background:#a1d1fa;'>时间</td>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>级别</td>";
  				messContent += "    <td width='15%' align='center' style='background:#a1d1fa;'>描述</td>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>";
  				messContent += "      <img src='../skin/images/cmddel.gif'>";
  				messContent += "    </td>";
  				messContent += "  </tr>";
  				for(var i=0; i<list.length && list[i].length>0; i++)
					{
						var sublist = list[i].split('~');
						var str_CType = '';
						switch(parseInt(sublist[7]))
						{
							case 1:
									str_CType = '系统告警';
								break;
							case 2:
									str_CType = '数据告警';
								break;
						}
						messContent += "<tr height='25px'>";
    				messContent += "  <td width='10%' align='center'>"+ sublist[2] +"</td>";
    				messContent += "  <td width='10%' align='center'>"+ str_CType  +"</td>";
    				messContent += "  <td width='15%' align='center'>"+ sublist[8] +"</td>";
    				messContent += "  <td width='10%' align='center'>"+ sublist[10]+"</td>";
    				messContent += "  <td width='15%' align='center'>"+ sublist[9] +"</td>";
    				messContent += "  <td width='10%' align='center'>";
    				messContent += "    <img src='../skin/images/cmddel.gif' title='人工处理' style='cursor:hand' onClick=\"doIgnored('"+pId+"', '"+pCName+"', '"+pStatus+"', '"+sublist[0]+"', '<%=UserInfo.getId()%>', '"+pId+"', '', '')\">";
    				messContent += "  </td>";
    				messContent += "</tr>";
					}
					messContent += "</table>";			
	      }  
	      else
	      {
	      	messContent += "查询失败...";
	      }   
	    }
	    else
	    {
	    	messContent += "查询失败...";
	    }
	  }
	};
	var url = 'ToPo.do?Cmd=22&Sid=<%=Sid%>&Id='+pId+'&CType=0&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
	
	messContent += "</div>";
	messContent += "<div style='width:100%;height:60px;text-align:center;border:1px solid #0068a6;overflow-x:no;overflow-y:auto;'>";
	messContent += "  <img src='../skin/images/alarm_low.gif' style='cursor:hand' onClick='doAla()'>";
	messContent += "</div>";
	var pHead = "<font color='red'>["+pCName+"]</font>";
	showMessageBox(pHead, messContent , 600, 500);
}

//链接告警管理
function doAla()
{
	if('<Limit:limitValidate userrole='<%=FpList%>' fpid='05' ctype='1'/>' == 'none')
	{
		alert('您无权限查看告警管理!');
		return;
	}
	parent.location = 'log/Main.jsp?Sid=<%=Sid%>&p3=1';
}

//链接销售统计
/**function doPro()
{
	if('<Limit:limitValidate userrole='<%=FpList%>' fpid='02' ctype='1'/>' == 'none')
	{
		alert('您无权限查看销售统计!');
		return;
	}
	parent.location = 'pro/Main.jsp?Sid=<%=Sid%>&p3=1';
}
**/

//链接生产数据
function doDat()
{
	if('<Limit:limitValidate userrole='<%=FpList%>' fpid='04' ctype='1'/>' == 'none')
	{
		alert('您无权限查看生产数据!');
		return;
	}
	parent.location = 'env/Main.jsp?Sid=<%=Sid%>&p3=1';
}

//人工处理
var reqIgnore = null;
function doIgnored(pId, pCName, pStatus, pSN, pOperator, pCpm_Id, pDId, pAttr_Id)
{	
	if(confirm("确定已人工处理当前告警?"))
	{
		if(window.XMLHttpRequest)
	  {
			reqIgnore = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqIgnore = new ActiveXObject("Microsoft.XMLHTTP");
		}		
		//设置回调函数
		reqIgnore.onreadystatechange = function()
		{
			var state = reqIgnore.readyState;
			if(state == 4)
			{
				if(reqIgnore.status == 200)
				{
					var resp = reqIgnore.responseText;			
					if(resp != null && resp.substring(0,4) == '0000')
					{
						alert('成功');
						//doDefence(pId, pCName, pStatus);
						closeWindow();
						return;
					}
					else
					{
						alert('失败，请重新操作');
						return;
					}
				}
				else
				{
					alert("失败，请重新操作");
					return;
				}
			}
		};
		var url = "GIS_Deal.do?Sid=<%=Sid%>&SN="+pSN+"&Operator="+pOperator+"&Cpm_Id="+pCpm_Id+"&Id="+pDId+"&Attr_Id="+pAttr_Id+"&currtime="+new Date();
		reqIgnore.open("post",url,true);
		reqIgnore.send(null);
		return true;
	}
}

//实时告警
var reqInfo = null;
function doDefence(pId, pCName, pStatus)
{	
	var messContent = "<div style='width:100%;height:403px;text-align:center;border:1px solid #0068a6;overflow-x:no;overflow-y:auto;'>";
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      if(null != Resp && Resp.length >= 4 && Resp.substring(0,4) == '0000')
	      {
	      	var list = Resp.substring(4).split('^');
  				messContent += "<table align='center' style='margin:auto' cellpadding='0' cellspacing='0' border='0' width='97%'>";
  				messContent += "  <tr height='25px'>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>站点</td>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>设备</td>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>类型</td>";
  				messContent += "    <td width='15%' align='center' style='background:#a1d1fa;'>时间</td>";
  				messContent += "    <td width='10%' align='center' style='background:#a1d1fa;'>级别</td>";
  				messContent += "    <td width='15%' align='center' style='background:#a1d1fa;'>描述</td>";
  				messContent += "    <td width='5%'  align='center' style='background:#a1d1fa;'>";
  				messContent += "      <img src='../skin/images/cmddel.gif'>";
  				messContent += "    </td>";
  				messContent += "  </tr>";
  				for(var i=0; i<list.length && list[i].length>0; i++)
					{
						var sublist = list[i].split('~');
						var str_CType = '';
						switch(parseInt(sublist[7]))
						{
							case 1:
									str_CType = '系统告警';
								break;
							case 2:
									str_CType = '数据告警';
								break;
						}
						messContent += "<tr height='25px'>";
    				messContent += "  <td width='10%' align='center'>"+ sublist[2] +"</td>";
    				messContent += "  <td width='10%' align='center'>"+ sublist[4] +"</td>";
    				messContent += "  <td width='10%' align='center'>"+ str_CType  +"</td>";
    				messContent += "  <td width='15%' align='center'>"+ sublist[8] +"</td>";
    				messContent += "  <td width='10%' align='center'>"+ sublist[10]+"</td>";
    				messContent += "  <td width='15%' align='center'>"+ sublist[9] +"</td>";
    				messContent += "  <td width='5%'  align='center'>";
    				messContent += "    <img src='../skin/images/cmddel.gif' title='人工处理' style='cursor:hand' onClick=\"doIgnored('"+pId+"', '"+pCName+"', '"+pStatus+"', '"+sublist[0]+"', '<%=UserInfo.getId()%>', '"+pId+"', '"+sublist[3]+"', '"+sublist[5]+"')\">";
    				messContent += "  </td>";
    				messContent += "</tr>";
					}
					messContent += "</table>";
	      }
	      else
	      {
	      	messContent += "查询失败...";
	      }
	    }
	    else
	    {
	    	messContent += "查询失败...";
	    }
	  }
	};
	var url = 'ToPo.do?Cmd=22&Sid=<%=Sid%>&Id='+pId+'&CType=1&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
	
	messContent += "</div>";
	messContent += "<div style='width:100%;height:60px;text-align:center;border:1px solid #0068a6;overflow-x:no;overflow-y:auto;'>";
	messContent += "  <img src='../skin/images/alarm_low.gif' style='cursor:hand' onClick='doAla()'>";
	messContent += "</div>";
	
	showMessageBox(pHead, messContent , 600, 500);
}

//实时数据
function doEnv(pCpm_Id,pCpm_Name)
{
	  var Pdiag    = new Dialog();
		Pdiag.Top    = "50%";
		Pdiag.Width  = 704;
		Pdiag.Height = 504;
		Pdiag.Title  = pCpm_Name;
		Pdiag.URL    = "Device_Detail_Draging.do?Sid=<%=Sid%>&Cpm_Id=" + pCpm_Id +"&Scene_Img="+ pCpm_Id +'.jpg';
		Pdiag.CancelEvent=function()
		{
			Pdiag.close();	  //关闭窗口
//			RealStatus();		//页面刷新
		};
		Pdiag.show();
}

</SCRIPT>
</html>