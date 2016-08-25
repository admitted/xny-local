package bean;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
 * 实时数据与历史数据 DataBean数据处理bean
 * 
 * @author cui
 */
public class DataBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_DATA;

	/*
	 * 获得DataBean的 serialVersionUID (non-Javadoc)
	 * 
	 * @see rmi.RmiBean#getClassId()
	 */
	public long getClassId()
	{
		return serialVersionUID;
	}

	public DataBean()
	{
		super.className = "DataBean";
	}

	/**
	 * 查询数据 实时数据与历史数据
	 * 
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
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);

		if (2 == currStatus.getCmd()) // 分页历史数据
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		else
			// 实时数据
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);

		switch (currStatus.getCmd())
		{
			case 0:// 实时数据
				request.getSession().setAttribute("Env_" + Sid, ((Object) msgBean.getMsg()));
				currStatus.setJsp("Env.jsp?Sid=" + Sid);

				msgBean = pRmi.RmiExec(5, this, 0);
				request.getSession().setAttribute("Env_Cpm_" + Sid, ((Object) msgBean.getMsg()));

				msgBean = pRmi.RmiExec(6, this, 0);
				request.getSession().setAttribute("Env_Sheb_" + Sid, ((Object) msgBean.getMsg()));
				break;

			case 2:// 历史数据
				request.getSession().setAttribute("Env_His_" + Sid, ((Object) msgBean.getMsg()));
				currStatus.setTotalRecord(msgBean.getCount());
				currStatus.setJsp("Env_His.jsp?Sid=" + Sid);
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
			// String BT = currStatus.getVecDate().get(0).toString().substring(5, 10);
			// String ET = currStatus.getVecDate().get(1).toString().substring(5, 10);
			String SheetName = "站点历史数据表";
			String UPLOAD_NAME = SimFormat.format(new Date());
			System.out.println("SheetName [" + SheetName + "]");
			msgBean = pRmi.RmiExec(2, this, 0);
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
				cell = new Label(0, 0, "站点", font1);
				sheet.addCell(cell);
				cell = new Label(1, 0, "设备", font1);
				sheet.addCell(cell);
				cell = new Label(2, 0, "参数", font1);
				sheet.addCell(cell);
				cell = new Label(3, 0, "时间", font1);
				sheet.addCell(cell);
				cell = new Label(4, 0, "数值", font1);
				sheet.addCell(cell);
				cell = new Label(5, 0, "级别", font1);
				sheet.addCell(cell);
				cell = new Label(6, 0, "描述", font1);
				sheet.addCell(cell);

				Iterator<?> temp_iterator = tempList.iterator();
				while (temp_iterator.hasNext())
				{
					DataBean dataBean     = (DataBean) temp_iterator.next();
					String Temp_Cpm_Name  = dataBean.getCpm_Name();
					String Temp_CName     = dataBean.getCName();
					String Temp_Attr_Name = dataBean.getAttr_Name();
					String Temp_CTime     = dataBean.getCTime();
					String Temp_Value     = dataBean.getValue();
					String Temp_Lev       = dataBean.getLev();
					String Temp_Des       = dataBean.getDes();

					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); // row_Index 列宽度

					cell = new Label(0, row_Index, Temp_Cpm_Name, font2);
					sheet.addCell(cell);
					cell = new Label(1, row_Index, Temp_CName, font2);
					sheet.addCell(cell);
					cell = new Label(2, row_Index, Temp_Attr_Name, font2);
					sheet.addCell(cell);
					cell = new Label(3, row_Index, Temp_CTime, font2);
					sheet.addCell(cell);
					cell = new Label(4, row_Index, Temp_Value, font2);
					sheet.addCell(cell);
					cell = new Label(5, row_Index, Temp_Lev, font2);
					sheet.addCell(cell);
					cell = new Label(6, row_Index, Temp_Des, font2);
					sheet.addCell(cell);
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

	/**
	 * 数据图表 Graph
	 * 
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
			currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);

			switch (currStatus.getFunc_Sub_Id())
			{
				case 1:// 时均值
					break;
				case 2:// 日均值
					request.getSession().setAttribute("Month_" + Sid, Month);
					request.getSession().setAttribute("Year_" + Sid, Year);
					break;
			}

			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
			switch (currStatus.getCmd())
			{
				case 20:// 数据图表
					request.getSession().setAttribute("Graph_" + Sid, (Object) msgBean.getMsg());
					currStatus.setJsp("Graph.jsp?Sid=" + Sid);
					break;
			}

			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
			response.sendRedirect(currStatus.getJsp());
		}
		catch (Exception Ex)
		{
			Ex.printStackTrace();
		}
	}

	/*
	 * 根据相应 pCmd 值获得相应的SQL查询语句(non-Javadoc)
	 * 
	 * @see rmi.RmiBean#getSql(int)
	 */
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0:// 实时数据
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.value, t.unit, t.lev, t.des " + " from view_data_now t " + " where instr('" + Id + "', t.cpm_id) > 0 " + " and t.ctime in (select max(a.ctime) from view_data_now a where a.cpm_id = t.cpm_id and a.id = t.id and a.attr_id = t.attr_id group by a.cpm_id, a.id, a.attr_id) " + " order by t.cpm_id, t.id, t.attr_id , t.ctime desc ";
				break;
			case 2:// 历史数据
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.value, t.unit, t.lev, t.des " + " from view_data t " + " where instr('" + Id + "', t.cpm_id) > 0 " + " and t.ctime >= date_format('" + currStatus.getVecDate().get(0).toString() + "', '%Y-%m-%d %H-%i-%S')" + " and t.ctime <= date_format('" + currStatus.getVecDate().get(1).toString() + "', '%Y-%m-%d %H-%i-%S')" + " order by t.ctime desc ";
				break;
			case 3:// 站点设备
				Sql = " select '' AS sn, t.cpm_id, t.cpm_name, t.id, t.cname, '' AS attr_id, '' AS attr_name, '' AS ctime, '' AS VALUE, '' AS unit, '' AS lev, '' AS des " + " FROM view_data_now t" + " GROUP BY t.cpm_id, t.cpm_name, t.id, t.cname" + " ORDER BY t.cpm_id, t.id";
				break;
			case 4:// 站点参数
				Sql = " select '' AS sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, '' AS ctime, '' AS VALUE, '' AS unit, '' AS lev, '' AS des " + " FROM view_data_now t" + " GROUP BY t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name" + " ORDER BY t.cpm_id, t.id, attr_id";
				break;
			case 5:// 实时数据查询站点[有多少站点]
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.value, t.unit, t.lev, t.des " + " from view_data_now t " + " where instr('" + Id + "', t.cpm_id) > 0 " + " and t.ctime in (select max(a.ctime) from view_data_now a where a.cpm_id = t.cpm_id   group by a.cpm_id) " + " GROUP BY t.cpm_id " + " ORDER BY t.cpm_id, t.ctime desc ";
				break;
			case 6:// 实时数据查询设备[有多少设备]
				Sql = " select '' AS sn, t.cpm_id, t.cpm_name, t.id, t.cname, '' AS attr_id, '' AS attr_name, '' AS ctime, '' AS VALUE, '' AS unit, '' AS lev, '' AS des " + " FROM view_data_now t" + " where instr('" + Id + "', t.cpm_id) > 0 " + " GROUP BY t.cpm_id, t.id" + " ORDER BY t.cpm_id, t.id";
				break;
			case 20:// 数据图表
				Sql = " {? = call rmi_graph('" + Id + "', '" + currStatus.getFunc_Sub_Id() + "', '" + currStatus.getVecDate().get(0).toString().substring(0, 10) + "')}";
				break;
		}
		return Sql;
	}

	/*
	 * 从数据库中获取相应 字段 数据(non-Javadoc)
	 * 
	 * @see rmi.RmiBean#getData(java.sql.ResultSet)
	 */
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
			setValue(pRs.getString(9));
			setUnit(pRs.getString(10));
			setLev(pRs.getString(11));
			setDes(pRs.getString(12));
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	/**
	 * 获取request页面 SN、ID、Cpm_Id、Level 等数据
	 * 
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
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
			setLev(CommUtil.StrToGB2312(request.getParameter("Lev")));
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

	private String	SN;
	private String	Cpm_Id;
	private String	Cpm_Name;
	private String	Id;
	private String	CName;
	private String	Attr_Id;
	private String	Attr_Name;
	private String	CTime;
	private String	Value;
	private String	Unit;
	private String	Lev;
	private String	Des;

	private String	Sid;
	private String	Level;
	private String	Year;
	private String	Month;

	private String	Func_Cpm_Id;

	public String getFunc_Cpm_Id()
	{
		return Func_Cpm_Id;
	}

	public void setFunc_Cpm_Id(String func_Cpm_Id)
	{
		Func_Cpm_Id = func_Cpm_Id;
	}

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

	public void setCpm_Id(String cpmId)
	{
		Cpm_Id = cpmId;
	}

	public String getCpm_Name()
	{
		return Cpm_Name;
	}

	public void setCpm_Name(String cpmName)
	{
		Cpm_Name = cpmName;
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

	public void setAttr_Id(String attrId)
	{
		Attr_Id = attrId;
	}

	public String getAttr_Name()
	{
		return Attr_Name;
	}

	public void setAttr_Name(String attrName)
	{
		Attr_Name = attrName;
	}

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
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

	public String getLev()
	{
		return Lev;
	}

	public void setLev(String lev)
	{
		Lev = lev;
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
}