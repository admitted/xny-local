package bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CnoocStationBean
{
  private String CPM_Id = "";
  private String CPM_Name = "";
  private String CPM_Brief = "";
  private String CPM_Status = "";
  private String CPM_OnOff = "";
  private String CPM_Type = "";
  private String CPM_Time = "";
  private String Link_Url = "";
  private String Link_Port = "";
  private String Link_Id = "";
  private String Link_Pwd = "";
  private String Gis_Sign = "";
  private String Longitude = "";
  private String Latitude = "";

  public String getSql(int Cmd)
  {
    String Sql = "";
    switch (Cmd)
    {
    case 0:
      Sql = " select t.ID, t.CNAME, t.BRIEF, t.STATUS, t.ONOFF, t.CTYPE, t.CTIME, t.LINK_URL, t.LINK_PORT, t.LINK_ID, t.LINK_PWD, t.SIGN, t.LONGITUDE, t.LATITUDE  from device_detail t where t.ONOFF='0'";
    }

    return Sql;
  }

  public boolean getData(ResultSet pRs)
  {
    boolean IsOK = true;
    try
    {
      setCPM_Id(pRs.getString(1));
      setCPM_Name(pRs.getString(2));
      setCPM_Brief(pRs.getString(3));
      setCPM_Status(pRs.getString(4));
      setCPM_OnOff(pRs.getString(5));
      setCPM_Type(pRs.getString(6));
      setCPM_Time(pRs.getString(7));
      setLink_Url(pRs.getString(8));
      setLink_Port(pRs.getString(9));
      setLink_Id(pRs.getString(10));
      setLink_Pwd(pRs.getString(11));
      setGis_Sign(pRs.getString(12));
      setLongitude(pRs.getString(13));
      setLatitude(pRs.getString(14));
    }
    catch (SQLException sqlExp)
    {
      sqlExp.printStackTrace();
    }
    return IsOK;
  }

  public String getCPM_Id() {
    return this.CPM_Id;
  }

  public void setCPM_Id(String cPM_Id) {
    this.CPM_Id = cPM_Id;
  }

  public String getCPM_Name() {
    return this.CPM_Name;
  }

  public void setCPM_Name(String cPM_Name) {
    this.CPM_Name = cPM_Name;
  }

  public String getCPM_Brief() {
    return this.CPM_Brief;
  }

  public void setCPM_Brief(String cPM_Brief) {
    this.CPM_Brief = cPM_Brief;
  }

  public String getCPM_Status() {
    return this.CPM_Status;
  }

  public void setCPM_Status(String cPM_Status) {
    this.CPM_Status = cPM_Status;
  }

  public String getCPM_OnOff() {
    return this.CPM_OnOff;
  }

  public void setCPM_OnOff(String cPM_OnOff) {
    this.CPM_OnOff = cPM_OnOff;
  }

  public String getCPM_Type() {
    return this.CPM_Type;
  }

  public void setCPM_Type(String cPM_Type) {
    this.CPM_Type = cPM_Type;
  }

  public String getCPM_Time() {
    return this.CPM_Time;
  }

  public void setCPM_Time(String cPM_Time) {
    this.CPM_Time = cPM_Time;
  }

  public String getLink_Url() {
    return this.Link_Url;
  }

  public void setLink_Url(String link_Url) {
    this.Link_Url = link_Url;
  }

  public String getLink_Port() {
    return this.Link_Port;
  }

  public void setLink_Port(String link_Port) {
    this.Link_Port = link_Port;
  }

  public String getLink_Id() {
    return this.Link_Id;
  }

  public void setLink_Id(String link_Id) {
    this.Link_Id = link_Id;
  }

  public String getLink_Pwd() {
    return this.Link_Pwd;
  }

  public void setLink_Pwd(String link_Pwd) {
    this.Link_Pwd = link_Pwd;
  }

  public String getGis_Sign() {
    return this.Gis_Sign;
  }

  public void setGis_Sign(String gis_Sign) {
    this.Gis_Sign = gis_Sign;
  }

  public String getLongitude() {
    return this.Longitude;
  }

  public void setLongitude(String longitude) {
    this.Longitude = longitude;
  }

  public String getLatitude() {
    return this.Latitude;
  }

  public void setLatitude(String latitude) {
    this.Latitude = latitude;
  }
}