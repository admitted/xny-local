<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<%@ page import="util.*"%>

<HTML><HEAD><TITLE>场景标注</TITLE>

<META content=text/html;charset=gb2312 http-equiv=Content-Type><LINK rel=stylesheet type=text/css href="../skin/css/style.css">
<SCRIPT language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</SCRIPT>

<%
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
	CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	ArrayList Device_Attr = (ArrayList)session.getAttribute("Device_Atrr_" + Sid);
	String Cpm_Id  = CommUtil.StrToGB2312(request.getParameter("Cpm_Id"));
	String Scene_Img  = CommUtil.StrToGB2312(request.getParameter("Scene_Img"));
	int sn=0;	
%>
<SCRIPT type=text/javascript src="../skin/js/util.js"></SCRIPT>
</HEAD>
<BODY style="BACKGROUND: #cadfff">
<DIV id=_contents style="FONT-SIZE: 12px; BORDER-TOP: #777777 1px solid; BORDER-RIGHT: #777777 1px solid; BORDER-BOTTOM: #777777 1px solid; POSITION: absolute; PADDING-BOTTOM: 6px; PADDING-TOP: 6px; PADDING-LEFT: 6px; BORDER-LEFT: #777777 1px solid; Z-INDEX: 1; PADDING-RIGHT: 6px; VISIBILITY: hidden; BACKGROUND-COLOR: #cadfff" Author="_contents">时<SELECT name=_hour Author="_contents"><OPTION selected value=00 Author="_contents">00</OPTION><OPTION value=01 Author="_contents">01</OPTION><OPTION value=02 Author="_contents">02</OPTION><OPTION value=03 Author="_contents">03</OPTION><OPTION value=04 Author="_contents">04</OPTION><OPTION value=05 Author="_contents">05</OPTION><OPTION value=06 Author="_contents">06</OPTION><OPTION value=07 Author="_contents">07</OPTION><OPTION value=08 Author="_contents">08</OPTION><OPTION value=09 Author="_contents">09</OPTION><OPTION value=10 Author="_contents">10</OPTION><OPTION value=11 Author="_contents">11</OPTION><OPTION value=12 Author="_contents">12</OPTION><OPTION value=13 Author="_contents">13</OPTION><OPTION value=14 Author="_contents">14</OPTION><OPTION value=15 Author="_contents">15</OPTION><OPTION value=16 Author="_contents">16</OPTION><OPTION value=17 Author="_contents">17</OPTION><OPTION value=18 Author="_contents">18</OPTION><OPTION value=19 Author="_contents">19</OPTION><OPTION value=20 Author="_contents">20</OPTION><OPTION value=21 Author="_contents">21</OPTION><OPTION value=22 Author="_contents">22</OPTION><OPTION value=23 Author="_contents">23</OPTION></SELECT> 分<SELECT name=_minute Author="_contents"><OPTION selected value=00 Author="_contents">00</OPTION><OPTION value=05 Author="_contents">05</OPTION><OPTION value=10 Author="_contents">10</OPTION><OPTION value=15 Author="_contents">15</OPTION><OPTION value=20 Author="_contents">20</OPTION><OPTION value=25 Author="_contents">25</OPTION><OPTION value=30 Author="_contents">30</OPTION><OPTION value=35 Author="_contents">35</OPTION><OPTION value=40 Author="_contents">40</OPTION><OPTION value=45 Author="_contents">45</OPTION><OPTION value=50 Author="_contents">50</OPTION><OPTION value=55 Author="_contents">55</OPTION></SELECT> <INPUT onclick=_select() style="FONT-SIZE: 12px" type=button value=确定 name=queding Author="_contents"></DIV>
<SCRIPT type=text/javascript src="../skin/js/dom-drag.js"></SCRIPT>

<STYLE>HTML {
	HEIGHT: 100%
}
BODY {
	HEIGHT: 100%; PADDING-BOTTOM: 0px; PADDING-TOP: 0px; PADDING-LEFT: 0px; MARGIN: 0px; PADDING-RIGHT: 0px
}
#container {
	HEIGHT: 100%
}
HTML {
	HEIGHT: 100%; WIDTH: 100%; PADDING-BOTTOM: 0px; PADDING-TOP: 0px; PADDING-LEFT: 0px; MARGIN: 0px; PADDING-RIGHT: 0px
}
BODY {
	HEIGHT: 100%; WIDTH: 100%; PADDING-BOTTOM: 0px; PADDING-TOP: 0px; PADDING-LEFT: 0px; MARGIN: 0px; PADDING-RIGHT: 0px
}
.mesWindow {
	BORDER-TOP: #c7c5c6 1px solid; BORDER-RIGHT: #c7c5c6 1px solid; BACKGROUND: #cadfff; BORDER-BOTTOM: #c7c5c6 1px solid; BORDER-LEFT: #c7c5c6 1px solid
}
.mesWindowTop {
	FONT-SIZE: 12px; BACKGROUND: #3ea3f9; POSITION: relative; FONT-WEIGHT: bold; PADDING-BOTTOM: 5px; TEXT-ALIGN: left; PADDING-TOP: 5px; PADDING-LEFT: 5px; CLEAR: both; MARGIN: 0px; LINE-HEIGHT: 1.5em; PADDING-RIGHT: 5px
}
.mesWindowTop SPAN {
	RIGHT: 5px; POSITION: absolute; TOP: 3px
}
.mesWindowContent {
	FONT-SIZE: 12px; CLEAR: both; MARGIN: 4px
}
.mesWindow .close {
	CURSOR: pointer; TEXT-DECORATION: underline; HEIGHT: 15px; WIDTH: 28px; BACKGROUND: #fff
}
</STYLE>

  <form name="Device_Detail_Scene" action="Device_Detail_Scene.do" method="post" target="childFrame" enctype="multipart/form-data">
	   <TABLE style="MARGIN: auto" borderColor=#3491d6 height="100%" cellSpacing=0 borderColorDark=#ffffff cellPadding=0 width="100%" align=center border=1>
		     <TBODY>
		         <TR height=30>
		            <TD width=700 align=left>低温储罐系统: 
			              <INPUT title='请选择场景图' style="HEIGHT: 20px; WIDTH: 350px" type='file'   name='file'> 
			              <INPUT onclick='doUpload()' style="HEIGHT: 20px; WIDTH: 90px"  type='button' value='更新场景图'> 
			              <INPUT onclick='doReturn()' style="HEIGHT: 20px; WIDTH: 50px"  type='button' value='返回'> 
		            </TD>
		         </TR>
		         <TR height=500>
		            <TD width=700 align=left>
		                <DIV id=mapdiv style="BORDER-TOP: green 0px solid; HEIGHT: 500px; BORDER-RIGHT: green 0px solid; WIDTH: 700px; BORDER-BOTTOM: green 0px solid; POSITION: relative; TEXT-ALIGN: center; LEFT: 0px; BORDER-LEFT: green 0px solid; TOP: 0px">
		                	<IMG id=mapimg style="HEIGHT: 500px; WIDTH: 700px" src="../skin/images/CPM_Scene/<%=Scene_Img%>"> 
		            <%
		            if(null != Device_Attr)
		            {
		            	Iterator iterator = Device_Attr.iterator();
									while(iterator.hasNext())
									{

										DataNowBean statBean = (DataNowBean)iterator.next();			
										String Id = statBean.getId();
										String CName = statBean.getCName();
										String Attr_Id = statBean.getAttr_Id();
										String Attr_Name = statBean.getAttr_Name();
										String Sign = statBean.getSign();
										String Pos_X = statBean.getPos_X();
										String Pos_Y = statBean.getPos_Y();
										if(Sign.equals("1"))
										{
								%>
											<div id='div_<%=sn%>' style='position:absolute;left:<%=Pos_X%>px;top:<%=Pos_Y%>px;width:60px;height:50px;text-align:right;'>
												<img id='img_<%=sn%>' style='width:16px;height:16px;cursor:hand;' src='../skin/images/marke_del.png' onclick=doDel('<%=Id%>,<%=Attr_Id%>')>
												<div id='dev_<%=sn%>' value='<%=Id%>,<%=Attr_Id%>' style='border:1px solid #ffffff;text-align:center;'>
													<img style='width:32px;height:32px;cursor:hand;' src='../skin/images/dev_attr.png'><br><font size=2 color='#ffffff'><%=CName%> > <%=Attr_Name%></font>
												</div>
											</div>
								<%
											sn++;
										}
									}
		            }
		            %>
		                </DIV>
		            </TD>
		         </TR>
		     </TBODY>
	   </TABLE>
	   
	   <input type="hidden" name="Sid"   value="<%=Sid%>">
	   <input type="hidden" name="Id"    value="<%=Cpm_Id%>">
	
  </FORM>
<SCRIPT language=javascript>
if(<%=currStatus.getResult().length()%> > 0)
   alert("<%=currStatus.getResult()%>");
<%
currStatus.setResult("");
session.setAttribute("CurrStatus_" + Sid, currStatus);
%>
function doReturn()
{
  parent.location = "Device_Detail_Edit.jsp?Sid=<%=Sid%>&Id=<%=Cpm_Id%>";
}

function doUpload()
{
	  var pUrl = Device_Detail_Scene.file.value;
	  if(pUrl.indexOf('.jpg') == -1 && pUrl.indexOf('.JPG') == -1 && pUrl.indexOf('.gif') == -1 && pUrl.indexOf('.GIF') == -1 && pUrl.indexOf('.bmp') == -1 && pUrl.indexOf('.BMP') == -1)
	  {
	    alert('请确认图片格式,支持jpg,gif,bmp,JPG,GIF,BMP格式!');
	    return;
	  }
	  if(confirm('确定更新场景图?'))
	  {
	    Device_Detail_Scene.submit();
	  }
}
document.getElementById('mapimg').oncontextmenu = function()
{
	  var pos_x = window.event.offsetX;
	  var pos_y = window.event.offsetY;
	  if(window.XMLHttpRequest){reqChg = new XMLHttpRequest();}
	  else if(window.ActiveXObject){reqChg = new ActiveXObject('Microsoft.XMLHTTP');}
	  reqChg.onreadystatechange = function()
	  {
		    var state = reqChg.readyState;
		    if(state == 4)
		    {
			      var resp = reqChg.responseText;
			      if(null != resp && resp.length >= 4 && resp.substring(0,4) == '0000')
			      {
				        var messContent = '';
				        messContent += "<div style='text-align:center;margin:10px;'>";
				        messContent += "<select id='Dev_Id' name='Dev_Id' style='width:120px;height:20px;'>";
				        var list = resp.substring(4).split(';');
				        for(var i=0; i<list.length && list[i].length>0; i++)
				        {  //0421010001|0002,差压计|差压计;
				           var sublist = list[i].split(',');
				           messContent += "  <option value='"+sublist[0]+"'>"+sublist[1]+"</option>";
				        }
				        messContent += "  </select>";
				        messContent += "  <input type='button' value='标注属性' onclick=doMark('"+pos_x+"','"+pos_y+"')>";
				        messContent += "</div>";
				        showMessageBox('添加标注', messContent , 300, 150);
			      }
		    }
	  };
	  
	  var url = "Device_Detail_Map.do?Cmd=23&Cpm_Id=<%=Cpm_Id%>&Sid=<%=Sid%>&currtime="+new Date();
	  
		reqChg.open('get', url);
	  reqChg.setRequestHeader('If-Modified-Since', '0');
	  reqChg.send(null);
	  return true;
}
for(var i=0; i<'<%=sn%>'; i++)
{
	  Drag.init(document.getElementById('dev_' + i), document.getElementById('div_' + i), 0, 700-60, 0, 500-50, false, false, false, false);
	  document.getElementById('div_' + i).onDragEnd = function(x, y)
	  {
	    	doDragEnd(Drag.obj.attributes['value'].value, x, y);
	  };
}
function doMark(x, y)
{
	  if(document.getElementById('Dev_Id').value.length < 1)
	  {
		    alert('请选择设备');
		    return;
	  }
	  if(confirm('确定添加标注?'))
	  {   //0421010001>0002,差压计 > 差压计;
	  	  var idDetail = document.getElementById('Dev_Id').value.split('>');
	    	location = 'Device_Detail_Draging.do?Sid=<%=Sid%>&Cmd=17&Cpm_Id=<%=Cpm_Id%>&Scene_Img=<%=Scene_Img%>&Id='+ idDetail[0]+"&Attr_Id="+idDetail[1]+'&Pos_X='+x+'&Pos_Y='+y;
	  }
}
function doDel(pId)
{
	  if(confirm('确定取消标注?'))
	  {
	    	var idDetail = pId.split(',');
	    	location = 'Device_Detail_Draging.do?Sid=<%=Sid%>&Cmd=16&Cpm_Id=<%=Cpm_Id%>&Scene_Img=<%=Scene_Img%>&Id='+ idDetail[0]+"&Attr_Id="+idDetail[1];

	  }
}
function doDragEnd(pId, x, y)
{
	  if(confirm('确定调整坐标?'))
	  {
		    var idDetail =pId.split(',');
				location = 'Device_Detail_Draging.do?Sid=<%=Sid%>&Cmd=15&Cpm_Id=<%=Cpm_Id%>&Scene_Img=<%=Scene_Img%>&Id='+ idDetail[0]+"&Attr_Id="+idDetail[1]+'&Pos_X='+x+'&Pos_Y='+y+'&currtime='+new Date();
	  }
}
</SCRIPT>
</BODY></HTML>