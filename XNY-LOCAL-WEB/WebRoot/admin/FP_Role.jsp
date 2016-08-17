<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<link rel="stylesheet" href="../skin/css/zTreeStyle2.css" type="text/css">
<script type="text/javascript" src="../skin/js/util.js"></script>
<script type="text/javascript" src="../skin/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../skin/js/jquery.ztree.core-3.4.js"></script>
<script type="text/javascript" src="../skin/js/jquery.ztree.excheck-3.4.js"></script>
<script type="text/javascript" src="../skin/js/jquery.ztree.exedit-3.4.js"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
	
	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  ArrayList FP_Role     = (ArrayList)session.getAttribute("FP_Role_" + Sid);
  ArrayList FP_Info     = (ArrayList)session.getAttribute("FP_Info_" + Sid);
  
%>
<body style="background:#CADFFF">
<form name="FP_Role"  action="User_Role.do" method="post" target="mFrame">
<div id="down_bg_2">
	<div id="cap"><img src="../skin/images/fp_role.gif"></div><br><br><br>
	<div id="right_table_center">
		<table width="70%" height='100%' style='margin:auto;' border=0 cellPadding=0 cellSpacing=0>
			<tr height='30px'>
				<td width='100%' align='right'>
					<img style="cursor:hand" onClick="doSubmit()"   src="../skin/images/mini_button_submit.gif">
				</td>
			</tr>
			<tr height='30px'>
				<td width='100%' align=center>
					<table cellpadding="0" cellspacing="0" border="1" width="100%" bordercolor="#3491D6" borderColorDark="#ffffff">
						<tr valign="top" height='350px'>
							<td width="45%" align='center'>
								<div class="zTreeDemoBackground" style="width:100%;border:0px solid #0068a6"><ul id="areaTree" class="ztree"></ul></div>
							</td>
							<td width="55%" align='center'>
								<div class="zTreeDemoBackground" style="width:100%;border:0px solid #0068a6"><ul id="devTree" class="ztree"></ul></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
<input type='hidden' name='id' value=''>
<input type='hidden' name='cname' value=''>
<input type='hidden' name='point' value=''>	
</form>
</body>
<SCRIPT LANGUAGE=javascript>
var setting = 
{
	view: 
	{
		addHoverDom: addHoverDom,
		removeHoverDom: removeHoverDom,
		selectedMulti: false
	},
	edit: 
	{
		enable: true,
		drag: {isMove: false,isCopy: false},
		showRenameBtn: setRenameBtn,
		showRemoveBtn: setRemoveBtn
	},
	data: 
	{
		simpleData:{enable: true}
	},
	callback:{onClick: zTreeOnClick, beforeRemove: zTreeBeforeDel}
};

var setting2 = 
{
	check: {
		enable: true
	},
	data: {
		simpleData:{enable: true}
	}
};

function addHoverDom(treeId, treeNode) 
{
	if(treeNode.tId.substring(0,7) == 'devTree')
	{
		return false;
	}
	if(0 == treeNode.level)
	{	
		var sObj = $('#' + treeNode.tId + '_span');
		if (treeNode.editNameFlag || $('#addBtn_'+treeNode.id).length>0) 
			return;
		var addStr = "<span class='button add' id='addBtn_" + treeNode.id+ "' title='add node' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $('#addBtn_'+treeNode.id);
		if (btn) btn.bind('click', function()
		{
			var zTree = $.fn.zTree.getZTreeObj('areaTree');
			var newId = parseInt(treeNode.id + '00', 10);
			var childNodes = zTree.transformToArray(treeNode);
      for(var i=0; i<childNodes.length; i++)
      {
      	if(treeNode.id == childNodes[i].pId)
      	{
          if(parseInt(childNodes[i].id, 10) > newId)
					{
						newId = parseInt(childNodes[i].id, 10);
					}
       	}
      }
      
      var _newId = newId.toString();
			if(parseInt(_newId.substring(_newId.length-2, _newId.length)) >= 99)
			{
			  alert('当前角色或区域超99个，请精简!');
			  return;
			}
			
			var strNewId = StrLeftFillZero((newId + 1).toString(), 3);
			doAdd(strNewId, 'fq'+strNewId);
			zTree.addNodes(treeNode, {id:strNewId, name:'fp'+strNewId, value:strNewId, pId:treeNode.id});
			return false;
		});
	}
};

function removeHoverDom(treeId, treeNode) 
{
	if(0 == treeNode.level)
	{
		$('#addBtn_'+treeNode.id).unbind().remove();
	}
};

function zTreeBeforeDel(treeId, treeNode) 
{ 
  if(treeNode)
	{
	 	doDel(treeNode.id);
		return true;
	}
}

function setRenameBtn(treeId, treeNode)
{
	switch(treeNode.level)	
	{
	case 0:
		return false;
		break;
	case 1:
		return true;
		break;
	}
}

function setRemoveBtn(treeId, treeNode)
{
	 switch(treeNode.level)	
	 {
		 case 0:
		     return false;
			 break;
		 case 1:
				 return true;
			 break;
	 }
}

function zTreeOnClick(event, treeId, treeNode) 
{
	var zTree2 = $.fn.zTree.getZTreeObj("devTree");
	zTree2.checkAllNodes(false);
  if(0 == treeNode.level)
  {
    return;
  }
  if(1 == treeNode.level)
  {
  	var point = treeNode.value;
  	var point_iter = point.split(',');
  	for(i=0; i<point_iter.length; i++)
  	{
  		var node = zTree2.getNodeByParam('id', point_iter[i], null);
  		if(null != node)
  		{
  			zTree2.checkNode(node, true, true);
  		}
  	}
	}
}

var dataMaker = function(count)
{
	var nodes = [];
	var n;
	n = {id:'0', name:'功能权限', value:'', pId:'-1', isParent:false, open:true};
	nodes.push(n);	
	<%
	if(null != FP_Role)
	{
		Iterator iterator = FP_Role.iterator();
		while(iterator.hasNext())
		{
			UserRoleBean statBean = (UserRoleBean)iterator.next();
			String R_Id = statBean.getId();
			String R_CName = statBean.getCName();
			String R_Point = statBean.getPoint();
			if(null == R_Point)
			{
				R_Point = "";
			}
	%>
			n = {id:'<%=R_Id%>', name:'<%=R_CName%>', value:'<%=R_Point%>', pId:'0', isParent:false, open:true};
			nodes.push(n);
	<%
		}
	}
	%>
	return nodes;
}

var dataMaker2 = function(count)
{
	var nodes = [];
	var n;
	<%
	if(null != FP_Info)
	{
		Iterator iterator = FP_Info.iterator();
		while(iterator.hasNext())
		{
			UserRoleBean statBean = (UserRoleBean)iterator.next();
			String Id = statBean.getId();
			String CName = statBean.getCName();
			int parent_len = Id.length()-2;
			if(parent_len < 2)
			{
	%>
				n = {id:'<%=Id%>', name:'<%=CName%>', value:'',pId:'<%=Id.substring(0, parent_len)%>', isParent:false, open:true};
				nodes.push(n);
	<%
			}
			else
			{
	%>
				n = {id:'<%=Id%>', name:'<%=CName%>', value:'',pId:'<%=Id.substring(0, parent_len)%>', isParent:false, open:false};
				nodes.push(n);
	<%		
			}
		}
	}
	%>
	return nodes;
}	

function doSubmit()
{
	var zTree1 = $.fn.zTree.getZTreeObj("areaTree");
	var zTree2 = $.fn.zTree.getZTreeObj("devTree");
	var role = zTree1.getSelectedNodes();
  if(role.length < 1 || role[0].id == '0')
  {
    alert('请先选择权限节点!');
    return;
  }
  
  if((role[0].name).Trim().length < 1)
  {
  	alert('权限名称不能为空');
  	return;
  }
  
  var point = '';
  var tempArray = zTree2.getCheckedNodes(true);
  for(var i=0; i<tempArray.length; i++)
  {
    if(!tempArray[i].isParent)
      point += tempArray[i].id + ',';
  }
  
	if(confirm("确定提交?"))
	{
		m_Edit = createXHR();
		if(m_Edit)
		{
			m_Edit.onreadystatechange=callbackForEdit;
			var url = 'User_RoleOP.do?Cmd=11&Sid=<%=Sid%>&Id='+role[0].id+'&CName='+role[0].name+'&Point='+point+'&currtime='+new Date();
			m_Edit.open("get", url);
			m_Edit.send(null);
			role[0].value = point;
		}
		else
		{
			alert("浏览器不支持，请更换浏览器！");
		}
	}
}

function doAdd(pId, pCName)
{
	m_Add = createXHR();
  if(m_Add)
  {
     m_Add.onreadystatechange = callbackForAdd;
     var url = 'User_RoleOP.do?Cmd=10&Sid=<%=Sid%>&Id='+pId+'&CName='+pCName+'&currtime='+new Date();
     m_Add.open('get', url);
     m_Add.send(null);
  }
  else
  {
     alert('浏览器不支持，请更换浏览器！');
  }
}

function doDel(pId)
{
	m_Del = createXHR();
  if(m_Del)
  {
     m_Del.onreadystatechange = callbackForDel;
     var url = 'User_RoleOP.do?Cmd=12&Sid=<%=Sid%>&Id='+pId+'&currtime='+new Date();
     m_Del.open('get', url);
     m_Del.send(null);
  }
  else
  {
     alert('浏览器不支持，请更换浏览器！');
  }
}

function callbackForAdd()
{
  if(m_Add.readyState == 4)
  {
  	 if(m_Add.status == 200)
    {
      var returnValue = m_Add.responseText;
      if(null != returnValue && returnValue == '0000')
      {
      }
    }
    else
    {
      alert('页面出现异常！');
    }
  }
}	

function callbackForDel()
{
  if(m_Del.readyState == 4)
  {
  	 if(m_Del.status == 200)
    {
      var returnValue = m_Del.responseText;
      if(null != returnValue && returnValue == '0000')
      {
      }
    }
    else
    {
      alert('页面出现异常！');
    }
  }
}
	
function createXHR()
{
	var xhr;
	try
	{
		xhr = new ActiveXObject("Msxml2.XMLHTTP");
	}
	catch(e)
	{
		try
		{
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
		catch(E)
		{
			xhr = false;
		}
	}
	if(!xhr && typeof XMLHttpRequest != 'undefined')
	{
		xhr = new XMLHttpRequest();
	}
	return xhr;
}

function callbackForEdit()
{
  if(m_Edit.readyState == 4)
  {
  	 if(m_Edit.status == 200)
    {
      var returnValue = m_Edit.responseText;
      if(null != returnValue && returnValue == '0000')
      {
        alert('编辑成功!');
      }
      else
      {
        alert('编辑失败!');
      }
    }
    else
    {
      alert("页面出现异常！");
    }
  }
}

//初始化 树
function createTree()
{
	var zNodes = dataMaker(500);
	$("#areaTree").empty();
	$.fn.zTree.init($("#areaTree"), setting, zNodes);
	
	var zNodes2 = dataMaker2(500);
	$("#devTree").empty();
	$.fn.zTree.init($("#devTree"), setting2, zNodes2);
}
createTree();
</SCRIPT>
</html>