<%@ page contentType="text/html; charset=gbk" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="rmi.*" %>
<%@ page import="util.*" %>
<%@page import="java.awt.*, java.awt.image.*, java.io.*, com.sun.image.codec.jpeg.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type='text/javascript' src='../skin/js/jquery.min.js' charset='gb2312'></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%

    String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
    CurrStatus currStatus = (CurrStatus) session.getAttribute("CurrStatus_" + Sid);
    ArrayList Device_Atrr = (ArrayList) session.getAttribute("Device_Atrr_" + Sid);

%>
<body style="background:#CADFFF">

<form name="Device_Detail_Alert" action="Device_Detail_Alert_Update.do" method="post" target="_self">
    <table width="100%" style='margin:auto;' border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
        <tr>
            <td width="3%" class="table_deep_blue">
                序号
            </td>
            <td width="10%" class="table_deep_blue">设备名称</td>
            <td width="10%" class="table_deep_blue">属性名称</td>
            <td width="10%" class="table_deep_blue">时间</td>
            <td width="8%"  class="table_deep_blue">数值</td>
            <td width="4%"  class="table_deep_blue">单位</td>
            <td width="8%"  class="table_deep_blue">最小值</td>
            <td width="8%"  class="table_deep_blue">最大值</td>
            <td width="20%" class="table_deep_blue">告警说明</td>
            <td width="5%" class="table_deep_blue">操作</td>
        </tr>
        <%
            if (Device_Atrr != null)
            {
                int sn = 0;
                Iterator iterator = Device_Atrr.iterator();
                while (iterator.hasNext())
                {
                    DataNowBean statBean = (DataNowBean) iterator.next();
                    String Cpm_Id     = statBean.getCpm_Id();
                    String Cpm_Name   = statBean.getCpm_Name();
                    String Id         = statBean.getId();
                    String CName      = statBean.getCName();
                    String Attr_Id    = statBean.getAttr_Id();
                    String Attr_Name  = statBean.getAttr_Name();
                    String CTime      = statBean.getCTime();
                    String Value      = statBean.getValue();
                    String Unit       = statBean.getUnit();
                    String Stand_Low  = statBean.getStand_Low();
                    String Stand_High = statBean.getStand_High();
                    String Abn_Des    = statBean.getAbn_Des();
                    sn++;
        %>
        <tr <%=((sn % 2) == 0 ? "class='table_blue'" : "class='table_white_l'")%>>  
            <td align=center><%=sn%></td>
            <td align=left><%=CName%></td>
            <td align=left><%=Attr_Name%></td>
            <td align=left><%=CTime%></td>
            <td align=center><%=Value%></td>
            <td align=center><%=Unit%></td>
            <td align=center><input type='text' id='Stand_Low_<%=sn%>'  name='Stand_Low_<%=sn%>'  style='width:90%;height:18px;' value='<%="-888".equals(Stand_Low)?"":Stand_Low%>'  maxlength='20'></td>
            <td align=center><input type='text' id='Stand_High_<%=sn%>' name='Stand_High_<%=sn%>' style='width:90%;height:18px;' value='<%="-888".equals(Stand_High)?"":Stand_High%>' maxlength='20'></td>
            <td align=center><input type='text' id='Abn_Des_<%=sn%>'    name='Abn_Des_<%=sn%>'    style='width:90%;height:18px;' value='<%=Abn_Des%>'    maxlength='128'></td>
            <td align=center>
            	  <a href="#" title="点击编辑" onClick="doEdit('<%=Cpm_Id%>','<%=Id%>','<%=Attr_Id%>','<%=sn%>')"><font color=gray><U>提交</U></font></a>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
    <input name="Cmd" type="hidden" value="14">
    <input name="Sid" type="hidden" value="<%=Sid%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
    if (<%=currStatus.getResult().length()%> >0)
    	alert("<%=currStatus.getResult()%>");
    <%
    	currStatus.setResult("");
    	session.setAttribute("CurrStatus_" + Sid, currStatus);
    %>

    function doEdit(pCpm_Id,pId,pAttr_Id,psn)
    {   
    	 var low  = document.getElementById("Stand_Low_"+psn).value.trim();
    	 var high = document.getElementById("Stand_High_"+psn).value.trim();
    	 var abn  = document.getElementById("Abn_Des_"+psn).value.trim();
    	 if(+low != low ) {
    	 	  alert("最小值不是数字!");
    	 	  return;
    	 }
    	 if(+high != high) {
    	 	  alert("最大值不是数字!");
    	 	  return;
    	 }
//     if(abn.length < 1) {
//    	 	alert(abn.length + "简要请输入此项异常的提示!");
//    	  return;
//     }
    	 if("" == low)  low=-888;
    	 if("" == high) high=-888;
    	 
        $.ajax({
            type: "post",
            url: "Device_Detail_Alert_Update.do?Sid=<%=Sid%>&Cpm_Id="+pCpm_Id+"&Id="+pId+"&Attr_Id="+pAttr_Id+"&Stand_Low="+low+"&Stand_High="+high+"&Abn_Des="+document.getElementById("Abn_Des_"+psn).value,
            data:"",
            dataType: "text",
            success: function (data) {
                if (data == "0000") {
                    alert("提交成功!");
                }else {
                   alert("编辑: 提交失败!");
                }
            },
            error: function (XMLHttpRequest, textStatus,errorThrown) {
                alert(errorThrown);
                alert("错误: 提交失败!");
            }
        });
    }
</SCRIPT>
</html>