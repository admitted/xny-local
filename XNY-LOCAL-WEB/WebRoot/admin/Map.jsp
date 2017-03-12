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
<script type='text/javascript' src='../skin/js/util.js' charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/browser.js' charset='gb2312'></script>
<script src="http://api.map.baidu.com/api?v=1.2&services=true" type="text/javascript"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
<style>
	html{height:100%}
	body{height:100%; margin:0px; padding:0px}
	#container{height:100%}
  html,body{width:100%; height:100%; margin:0; padding:0;}/*必须将最外层设置一个高度*/
  .mesWindow{border:#C7C5C6 1px solid;background:#CADFFF;}
  .mesWindowTop{background:#3ea3f9;padding:5px;margin:0;font-weight:bold;text-align:left;font-size:12px; clear:both; line-height:1.5em; position:relative; clear:both;}
  .mesWindowTop span{ position:absolute; right:5px; top:3px;}
  .mesWindowContent{margin:4px;font-size:12px; clear:both;}
  .mesWindow .close{height:15px;width:28px; cursor:pointer;text-decoration:underline;background:#fff}
</style>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	ArrayList Device_Detail = (ArrayList)session.getAttribute("Device_Detail_" + Sid);
	double Longitude = 120.117392;
	double Latitude = 30.340112;
	
	int sn = 0;
	if(null != Device_Detail)
	{	
		Iterator iterator = Device_Detail.iterator();
		while(iterator.hasNext())
		{
			DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
			if(statBean.getSign().equals("1") && 0 == sn)
			{
			  sn++;
				Longitude = Double.parseDouble(statBean.getLongitude());
				Latitude  = Double.parseDouble(statBean.getLatitude());
				break;
			}
		}
	}
	
%>
<body style="background:#CADFFF">
	<div id="container"></div>
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
}

//载入地图
var map = new BMap.Map("container");                      //创建地图实例
//map.setMapType(BMAP_HYBRID_MAP);                        //默认类型为卫星、路网一体
var point = new BMap.Point(<%=Longitude%>, <%=Latitude%>);//创建中心点坐标，默认为第一家企业
map.centerAndZoom(point, 10);                             //初始化地图，设置中心点坐标和地图级别
map.addControl(new BMap.NavigationControl());             //添加一个平移缩放控件，位置可偏移、形状可改变
map.addControl(new BMap.ScaleControl());                  //添加一个比例尺控件，位置可偏移[var opts = {offset: new BMap.Size(150, 5)};map.addControl(new BMap.ScaleControl(opts));]
map.addControl(new BMap.OverviewMapControl());            //添加一个缩略图控件，位置可偏移
//map.addControl(new BMap.MapTypeControl());              //添加地图类型变换(地图-卫星-三维)，位置可偏移
map.enableScrollWheelZoom();                              //启用滚轮放大缩小

//1.添加地图右击添加标注
map.addEventListener("rightclick", function(e)
{
 	doRightClick(e);
});

//2.添加定义标注图标
function addMarker(point, pCorp_Id, pCName, pIcon, pX, pY, pType)
{
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
 		doDefence(pCorp_Id, pCName, pType);
	});
	marker.enableDragging();  
	marker.addEventListener("dragend", function(e)
	{  
		doDragging(pCorp_Id, e.point.lng, e.point.lat, pType);  	
	});
}

//添加定义标注图标
<%
String Device_All = "";
if(Device_Detail != null)
{
	for(int i=0; i<Device_Detail.size(); i++)
	{
		DeviceDetailBean Device = (DeviceDetailBean)Device_Detail.get(i);
		if(Device.getSign().equals("1"))
		{
			Device_All += Device.getId() + ",";
		}
	}
}
%>

//状态监控
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
					map.clearOverlays();
	      	//2.添加
	      	var list = Resp.substring(4).split(";");
	      	for(var i=0; i<list.length && list[i].length>0; i++)
	      	{
	      		var sublist = list[i].split(",");
						switch(parseInt(sublist[2]))
	      		{
	      			case 0://正常
	      					var point = new BMap.Point(sublist[3], sublist[4]);
	 								addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_green.gif', '28', '28', '1');
	      				break;
	      			case 1://离线
	      					var point = new BMap.Point(sublist[3], sublist[4]);
	 								addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_green.gif', '28', '28', '1');
	      				break;
	      			case 2://异常
	      					var point = new BMap.Point(sublist[3], sublist[4]);
	 								addMarker(point, sublist[0], sublist[1], '../skin/images/mapcorp_green.gif', '28', '28', '1');
	      				break;
	      		}
	      	}
	      }
	    }
	  }
	};
	var url = 'ToPo.do?Cmd=21&Id=<%=Device_All%>&Sid=<%=Sid%>&currtime='+new Date();
	reqSend.open('POST',url,false);
	reqSend.send(null);
}
setTimeout("RealStatus()", 1000);

//右点击事件
var reqUnMarke = null;
function doRightClick(e)
{
	//获取未标记
	if(window.XMLHttpRequest)
  {
		reqUnMarke = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
		reqUnMarke = new ActiveXObject("Microsoft.XMLHTTP");
	}
	reqUnMarke.onreadystatechange = function()
	{
	  var state = reqUnMarke.readyState;
	  if(state == 4)
	  {
	    if(reqUnMarke.status == 200)
	    {
	      var Resp = reqUnMarke.responseText;
	      if(null != Resp && Resp.substring(0,4) == '0000')
	      {
	      	//站点
	      	var list = Resp.substring(4).split(';');
					var content = "<select id='Id' name='Id' style='width:220px;height:20px;'>";
	      	for(var i=0; i<list.length && list[i].length>0; i++)
	      	{
	      		var sublist = list[i].split(',');
	      		content += "<option value='"+ list[i] +"'>"+ sublist[1] +"</option>";
	      	}
					content += "</select>";
					content += "<input type='button' value='标注站点' onClick=\"doAddMarke('1', "+e.point.lng+", "+e.point.lat+")\">";
					var opts = 
					{
					  width : 350, // 信息窗口宽度  
					  height: 60, // 信息窗口高度  
					  title : ""  // 信息窗口标题
					}
					var infoWindow = new BMap.InfoWindow(content, opts);//创建信息窗口对象  
					map.openInfoWindow(infoWindow, e.point);            //打开信息窗口
	      }  
	      else
	      {
	    		return;
	      }   
	    }
	    else
	    {
	    	return;
	    }
	  }
	};
	var url = "ToPo.do?Cmd=23&Sid=<%=Sid%>&currtime="+new Date();
	reqUnMarke.open("POST",url,true);
	reqUnMarke.send(null);
	return true;
}

//添加标注
var reqAdd = null;
function doAddMarke(pType, Lng, Lat)
{
	if(document.getElementById('Id').value.length < 1)
	{
		alert('请选择要标注的站点!');
		return;
	}
	var Id = document.getElementById('Id').value.split(',')[0];
	var CName = document.getElementById('Id').value.split(',')[1];
	if(confirm('确定添加标注?'))
	{
		if(window.XMLHttpRequest)
	  {
			reqAdd = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqAdd = new ActiveXObject("Microsoft.XMLHTTP");
		}
		reqAdd.onreadystatechange = function()
		{
		  var state = reqAdd.readyState;
		  if(state == 4)
		  {
		    if(reqAdd.status == 200)
		    {
		      var Resp = reqAdd.responseText;
		      if(null != Resp && Resp.substring(0,4) == '0000')
		      {
		      	map.closeInfoWindow();
		      	var point = new BMap.Point(Lng, Lat);
						addMarker(point, Id, CName, '../skin/images/mapcorp_green.gif', '28', '28', pType);
		      	alert('添加标注成功!');
		    		return;
		      }
		      else
		      {
		      	alert('添加标注失败!');
		    		return;
		      }
		    }
		    else
		    {
		    	alert('添加标注失败!');
		    	return;
		    }
		  }
		};
		var url = "Device_doDragging.do?Cmd=17&Sid=<%=Sid%>&Id="+Id+"&Longitude="+Lng+"&Latitude="+Lat+"&currtime="+new Date();
		reqAdd.open("POST",url,true);
		reqAdd.send(null);
		return true;
	}
}

//拖拽坐标更新接口
var reqDrg = null;
function doDragging(pId, pLng, pLat, pType)
{
	if(confirm('同步更新当前站点坐标?'))
	{
		if(window.XMLHttpRequest)
	  {
			reqDrg = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqDrg = new ActiveXObject("Microsoft.XMLHTTP");
		}
		reqDrg.onreadystatechange = function()
		{
		  var state = reqDrg.readyState;
		  if(state == 4)
		  {
		    if(reqDrg.status == 200)
		    {
		      var Resp = reqDrg.responseText;
		      if(null != Resp && Resp.substring(0,4) == '0000')
		      {
		      	alert('坐标同步更新成功!');
		    		return;
		      }  
		      else
		      {
		      	alert('坐标同步更新失败!');
		    		return;
		      }   
		    }
		    else
		    {
		    	alert('坐标同步更新失败!');
		    	return;
		    }
		  }
		};
		var url = "Device_doDragging.do?Cmd=15&Sid=<%=Sid%>&Id="+pId+"&Longitude="+pLng+"&Latitude="+pLat+"&currtime="+new Date();
		reqDrg.open("POST",url,true);
		reqDrg.send(null);
		return true;
	}
}

//左点击查看接口
var reqInfo = null;
function doDefence(pId, pCName, pType)
{
	map.closeInfoWindow();
	var messContent = "";
	messContent += "<div style='text-align:center;margin:10px;'>";
	messContent += "  <a href='#' onClick=\"doDel('"+ pId +"', '"+ pType +"')\"><U>取消[<font color=red>"+pCName+"</font>]标注</U></a>";
	messContent += "</div>";
	showMessageBox('取消站点标注', messContent , 300, 150);
}

//删除标注接口
var reqDel = null;
function doDel(pId, pType)
{
	if(confirm('确定删除当前站点标注?'))
	{
		if(window.XMLHttpRequest)
	  {
			reqDel = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqDel = new ActiveXObject("Microsoft.XMLHTTP");
		}
		reqDel.onreadystatechange = function()
		{
		  var state = reqDel.readyState;
		  if(state == 4)
		  {
		    if(reqDel.status == 200)
		    {
		      var Resp = reqDel.responseText;
		      if(null != Resp && Resp.substring(0,4) == '0000')
		      {
		      	closeWindow();
		      	map.clearOverlays();
		      	RealStatus();
		      	alert('删除标注成功!');
		    		return;
		      }  
		      else
		      {
		      	alert('删除标注失败!');
		    		return;
		      }   
		    }
		    else
		    {
		    	alert('删除标注失败!');
		    	return;
		    }
		  }
		};
		var url = "Device_doDragging.do?Cmd=16&Sid=<%=Sid%>&Id="+pId+"&currtime="+new Date();
		reqDel.open("POST",url,true);
		reqDel.send(null);
		return true;
	}
}
</SCRIPT>
</html>