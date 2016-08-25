package rmi;

import java.io.Serializable;
import java.sql.ResultSet;
import util.*;

/**RmiBean 实现了 serializable 可序列化接口
 * @author Cui
 * bean包里的类都继承 RmiBean
 * 为什么要可序列化?
 *     如果要通过远程的方法调用（RMI）去调用一个远程对象的方法，如在计算机A中调用
 *     另一台计算机B的对象的方法，那么你需要通过JNDI服务获取计算机B目标对象的引用，
 *     将对象从B传送到A，就需要实现序列化接口
 */
public abstract class RmiBean implements Serializable
{	
	public final static String UPLOAD_PATH = "/www/XNY-LOCAL/XNY-LOCAL-WEB/files/excel/";
	
	/**************************system**********************/
	public static final int RMI_MANUFACTURER            = 1;
	public static final int RMI_DEVICE_INFO             = 2;
	public static final int RMI_DEVICE_CMD              = 3;
	public static final int RMI_DEVICE_ATTR             = 4;
	public static final int RMI_DATA_TYPE               = 5;
	
	/**************************admin***********************/
	public static final int RMI_CORP_INFO			    = 11;
	public static final int RMI_DEVICE_DETAIL			= 12;
	public static final int RMI_USER_INFO			 	= 13;
	public static final int RMI_USER_ROLE		   	    = 14;
	public static final int RMI_USER_POSITION			= 18;
	public static final int RMI_CRM_INFO		    	= 27;
	public static final int RMI_CCM_INFO		    	= 28;
	
	/**************************user-data*******************/
	public static final int RMI_DATA			        = 30;
	public static final int RMI_LINKAGE			        = 31;
	public static final int RMI_ALERT			        = 32;
	public static final int RMI_ACC_DATA    	        = 33;//累积流量数据
	public static final int RMI_ACC_SALE    	        = 34;//销售数据
	public static final int RMI_DATA_NOW			    = 35;
	

	public MsgBean msgBean = null;
	public String className;
	public CurrStatus currStatus = null;
	
	public RmiBean()
	{
		msgBean = new MsgBean(); 		
	}
	public String getClassName()
	{
		return className;
	}
	
	public abstract long getClassId();
	public abstract String getSql(int pCmd);
	public abstract boolean getData(ResultSet pRs);
}
