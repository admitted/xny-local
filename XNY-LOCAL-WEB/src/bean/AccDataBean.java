package bean;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import rmi.Rmi;
import rmi.RmiBean;
import util.*;


/** 
 * 累积流量数据处理bean(每天的流量用量) 
 * AccDataBean数据处理bean
 * @author cui
 * 
 */
public class AccDataBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ACC_DATA;
	
	/* 获得DataBean的 serialVersionUID (non-Javadoc)
	 * @see rmi.RmiBean#getClassId()
	 */
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public AccDataBean()
	{
		super.className = "AccDataBean";
	}
	
	/** 累积流量 查询
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		switch(currStatus.getCmd())
		{
		    case 0://各站点：累积流量
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		    	request.getSession().setAttribute("Acc_Data_Sta_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Data_Sta.jsp?Sid=" + Sid);	
		    	break;
		    	
		    case 1://日用量总表
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	request.getSession().setAttribute("Acc_Data_Day_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Data_Day.jsp?Sid=" + Sid);
		    	break;
		    	
		    case 2://月用量总表
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	request.getSession().setAttribute("Acc_Data_Month_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Data_Month.jsp?Sid=" + Sid);
		    	break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	   
	}
	
	/**
	 * 导出Excel表格
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @param pConfig
	 */
	public void ExportToExcel(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone)
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			SimpleDateFormat SimFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String SheetName = "站点流量统计表";
			String UPLOAD_NAME = SimFormat.format(new Date());
			System.out.println("SheetName [" + SheetName + "]");
			
			int    Cmd        = currStatus.getCmd();
			double Value_All  = 0.0;
			String BTime      = "";
			String StringTime = "";
			
			msgBean = pRmi.RmiExec(Cmd, this, 0);
			switch(Cmd)
			{
				case 2:
					StringTime = "月份";
					BTime = currStatus.getVecDate().get(0).toString().substring(0, 7);
					break;
				default:
					StringTime = "日期";
					break;
			}

			ArrayList<?> tempList = (ArrayList<?>) msgBean.getMsg();
			int row_Index = 0;
			Label cell = null;
			if (null != tempList)
			{
				WritableWorkbook book = Workbook.createWorkbook(new File(UPLOAD_PATH + UPLOAD_NAME + ".xls"));
				WritableSheet sheet = book.createSheet(SheetName, 0); // 生成名为"第一页"的工作表，参数0表示这是第一页

				/*******  字体格式1  ********/
				WritableFont wf = new WritableFont(WritableFont.createFont("normal"), 14, WritableFont.BOLD, false);
				WritableCellFormat font1 = new WritableCellFormat(wf);
				// wf.setColour(Colour.BLACK);                        // 字体颜色
				font1.setAlignment(Alignment.CENTRE);                 // 设置居中
				font1.setVerticalAlignment(VerticalAlignment.CENTRE); // 设置为垂直居中
				font1.setBorder(Border.ALL, BorderLineStyle.THIN);    // 设置边框线
                font1.setBackground(jxl.format.Colour.TURQUOISE);     // 设置单元格的背景颜色
				
				/*******  字体格式2  ********/
				WritableFont wf2 = new WritableFont(WritableFont.createFont("normal"), 10, WritableFont.NO_BOLD, false);
				WritableCellFormat font2 = new WritableCellFormat(wf2);
				wf2.setColour(Colour.BLACK);                          // 字体颜色
				font2.setAlignment(Alignment.CENTRE);                 // 设置居中
				font2.setVerticalAlignment(VerticalAlignment.CENTRE); // 设置为垂直居中
				font2.setBorder(Border.ALL, BorderLineStyle.THIN);    // 设置边框线
				
				/*******  字体格式3  ********/
				WritableFont wf3 = new WritableFont(WritableFont.createFont("normal"), 10, WritableFont.BOLD, false);
				WritableCellFormat font3 = new WritableCellFormat(wf3);
				wf3.setColour(Colour.RED);                            // 字体颜色
				font3.setAlignment(Alignment.CENTRE);                 // 设置居中
				font3.setVerticalAlignment(VerticalAlignment.CENTRE); // 设置为垂直居中
				font3.setBorder(Border.ALL, BorderLineStyle.THIN);    // 设置边框线
				font3.setBackground(jxl.format.Colour.YELLOW);        // 设置单元格的背景颜色

				sheet.setRowView(row_Index, 450);
				sheet.setColumnView(row_Index, 25);
				cell = new Label(0, 0, StringTime, font1);
				sheet.addCell(cell);
				cell = new Label(1, 0, "站点名称", font1);
				sheet.addCell(cell);
				cell = new Label(2, 0, "起始读数", font1);
				sheet.addCell(cell);
				cell = new Label(3, 0, "终止读数", font1);
				sheet.addCell(cell);
				cell = new Label(4, 0, "用气量", font1);
				sheet.addCell(cell);
				cell = new Label(5, 0, "备注", font1);
				sheet.addCell(cell);
				
				Iterator<?> temp_iterator = tempList.iterator();
				while (temp_iterator.hasNext())
				{
					AccDataBean accDataBean = (AccDataBean) temp_iterator.next();
					String Temp_Cpm_Name    = accDataBean.getCpm_Name();
					String Temp_B_Value     = accDataBean.getB_Value();
					String Temp_E_Value     = accDataBean.getE_Value();
					if(2 != Cmd)
					{      
						BTime               = accDataBean.getCTime();
					}
					String Temp_Value       = accDataBean.getValue();
					String Temp_Des         = accDataBean.getDes();
					
					Value_All = Value_All + Double.parseDouble(Temp_Value);
					
					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); // row_Index 列宽度

					cell = new Label(0, row_Index, BTime, font2);
					sheet.addCell(cell);
					cell = new Label(1, row_Index, Temp_Cpm_Name, font2);
					sheet.addCell(cell);
					cell = new Label(2, row_Index, Temp_B_Value, font2);
					sheet.addCell(cell);
					cell = new Label(3, row_Index, Temp_E_Value, font2);
					sheet.addCell(cell);
					cell = new Label(4, row_Index, Temp_Value, font2);
					sheet.addCell(cell);
					cell = new Label(5, row_Index, Temp_Des, font2);
					sheet.addCell(cell);
				}
				
				if(0 == Cmd)
				{
					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); 
					
					cell = new Label(0, row_Index, "本页合计：用气量总计为：" + new BigDecimal(Value_All).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(), font3);
					sheet.addCell(cell);
					for(int i = 1;i<5;i++){
						cell = new Label(i, row_Index, "", font3);
						sheet.addCell(cell);
					}
					sheet.mergeCells(0, row_Index, 5, row_Index); // 合并表格
				}
				book.write();
				book.close();
				try
				{
					PrintWriter out = response.getWriter();
					out.print(UPLOAD_NAME);
				}
				catch (Exception exp)
				{
					exp.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/** 数据图表 Graph
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 */
	public void Graph(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) 
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
		    
			switch(currStatus.getFunc_Sub_Id())
			{
			    case 1://时均值
			    	break;
			    case 2://日均值
			    	request.getSession().setAttribute("Month_" + Sid, Month);
			    	request.getSession().setAttribute("Year_" + Sid, Year);
			    	break;
			}
			
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
			switch(currStatus.getCmd())
			{
				case 20://数据图表
			    	request.getSession().setAttribute("Graph_" + Sid, (Object)msgBean.getMsg());
					currStatus.setJsp("Graph.jsp?Sid=" + Sid);
					break;
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		   	response.sendRedirect(currStatus.getJsp());
		}
		catch(Exception Ex)
		{
			Ex.printStackTrace();
		}
	}

	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://累积流量 (历史) 站点用气表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.b_value, t.e_value, t.value, t.unit,  t.des " +
					 " from view_acc_data_day t " +
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and t.ctime >= date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					 " and t.ctime <= date_format('"+currStatus.getVecDate().get(1).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					 " order by t.ctime desc ";
			   break;
			case 1://日用量总表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.b_value, t.e_value, t.value, t.unit,  t.des " +
					 " from view_acc_data_day t " +
			  	  	 " where t.ctime = date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d')" +
			  	  	 " order by t.cpm_id ";
			   break;
			case 2://月用量总表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, (t.e_value - sum(t.value)) as b_value , t.e_value, sum(t.value) value , t.unit,  t.des " +
					 " from (SELECT * FROM view_acc_data_day WHERE (DATE_FORMAT(ctime, '%Y-%m') = DATE_FORMAT('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m'))  ORDER BY ctime DESC) t " + 
					 " GROUP BY CPM_ID ";
			   break;
			
			case 20://数据图表
			   Sql = " {? = call rmi_graph('"+ Id +"', '"+ currStatus.getFunc_Sub_Id() +"', '"+ currStatus.getVecDate().get(0).toString().substring(0,10) +"')}";
			   break;
		}
		return Sql;
	}
	

	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setCpm_Id(pRs.getString(2));		
			setCpm_Name(pRs.getString(3));		
			setId(pRs.getString(4));
			setCName(pRs.getString(5));		
			setAttr_Id(pRs.getString(6));
			setAttr_Name(pRs.getString(7));			
			setCTime(pRs.getString(8));
			setB_Value(pRs.getString(9));
			setE_Value(pRs.getString(10));
			setValue(pRs.getString(11));
			setUnit(pRs.getString(12));
			setDes(pRs.getString(13));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	
	/** 获取request页面 SN、ID、Cpm_Id、Level 等数据
	 * @param request
	 * @return
	 */
	public boolean getHtmlData(HttpServletRequest request) 
	{
		boolean IsOK = true;
		try 
		{	
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));		
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));		
			setB_Value(CommUtil.StrToGB2312(request.getParameter("B_Value")));
			setE_Value(CommUtil.StrToGB2312(request.getParameter("E_Value")));
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setLevel(CommUtil.StrToGB2312(request.getParameter("Level")));
			setYear(CommUtil.StrToGB2312(request.getParameter("Year")));
			setMonth(CommUtil.StrToGB2312(request.getParameter("Month")));		
			setFunc_Cpm_Id(CommUtil.StrToGB2312(request.getParameter("Func_Cpm_Id")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String SN;
	private String Cpm_Id;
	private String Cpm_Name;
	private String Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String CTime;
	private String B_Value;
	private String E_Value;
	private String Value;
	private String Unit;
	private String Des;

	private String Sid;
	private String Level;
	private String Year;
	private String Month;
	
	private String Func_Cpm_Id;

	public String getSN()
	{
		return SN;
	}

	public void setSN(String sN)
	{
		SN = sN;
	}

	public String getCpm_Id()
	{
		return Cpm_Id;
	}

	public void setCpm_Id(String cpm_Id)
	{
		Cpm_Id = cpm_Id;
	}
	
	public String getCpm_Name()
	{
		return Cpm_Name;
	}

	public void setCpm_Name(String cpm_Name)
	{
		Cpm_Name = cpm_Name;
	}

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getCName()
	{
		return CName;
	}

	public void setCName(String cName)
	{
		CName = cName;
	}

	public String getAttr_Id()
	{
		return Attr_Id;
	}

	public void setAttr_Id(String attr_Id)
	{
		Attr_Id = attr_Id;
	}

	public String getAttr_Name()
	{
		return Attr_Name;
	}

	public void setAttr_Name(String attr_Name)
	{
		Attr_Name = attr_Name;
	}

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	public String getB_Value()
	{
		return B_Value;
	}

	public void setB_Value(String b_Value)
	{
		B_Value = b_Value;
	}

	public String getE_Value()
	{
		return E_Value;
	}

	public void setE_Value(String e_Value)
	{
		E_Value = e_Value;
	}

	public String getValue()
	{
		return Value;
	}

	public void setValue(String value)
	{
		Value = value;
	}

	public String getUnit()
	{
		return Unit;
	}

	public void setUnit(String unit)
	{
		Unit = unit;
	}

	public String getDes()
	{
		return Des;
	}

	public void setDes(String des)
	{
		Des = des;
	}

	public String getSid()
	{
		return Sid;
	}

	public void setSid(String sid)
	{
		Sid = sid;
	}

	public String getLevel()
	{
		return Level;
	}

	public void setLevel(String level)
	{
		Level = level;
	}

	public String getYear()
	{
		return Year;
	}

	public void setYear(String year)
	{
		Year = year;
	}

	public String getMonth()
	{
		return Month;
	}

	public void setMonth(String month)
	{
		Month = month;
	}

	public String getFunc_Cpm_Id()
	{
		return Func_Cpm_Id;
	}

	public void setFunc_Cpm_Id(String func_Cpm_Id)
	{
		Func_Cpm_Id = func_Cpm_Id;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	
	
	
}