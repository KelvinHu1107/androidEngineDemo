package kr.co.citus.engine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import kr.co.citus.engine.struct.TMC_INFO;
import kr.co.citus.engine.struct.TMC_INFO.DT_CityCMS;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ReceiveTMCInfo {
/*
	public static String roadIds;
	public static int tmcInfoRefreshCnt = 0;
	public static TMC_INFO tmcInfo = null;
	
	public static void getTMCInfo()
	{
		
		
		if (tmcInfoRefreshCnt > 0)
		{
			tmcInfoRefreshCnt ++;
			return;
		}
		
		tmcInfoRefreshCnt++;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String xmlStr = makeXMLStr();
				if (xmlStr == null) return;
				if (xmlStr.length() == 0) return;
				
				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost("http://services.naviking.com.tw/WS_CHT/WS_Info.asmx");
				httpPost.addHeader("User-Agent", "kSOAP/2.0");
				httpPost.addHeader("SOAPAction", "http://www.gotcha.com.tw/WS_Info/GetRoadInfo");
				httpPost.addHeader("Content-Type", "text/xml");
				httpPost.addHeader("Connection", "close");
//				httpPost.addHeader("Content-Length", ""+xmlStr.length());
				
				String strresponse = null;
				try {
					StringEntity se = new StringEntity(xmlStr, HTTP.UTF_8);
					se.setContentType("text/xml");
					
					httpPost.setEntity(se);
					
					HttpResponse response = httpclient.execute(httpPost);
		            HttpEntity entity = response.getEntity();

		            
		            if(entity!=null) {
		                strresponse = EntityUtils.toString(entity);
		                Log.d("dckim", strresponse);
		            }
		            
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (strresponse == null) return;
				
				int start = strresponse.indexOf("<GetRoadInfoResult>");
				if (start == -1) return;
				int end = strresponse.indexOf("</GetRoadInfoResult>");
				if (end < 1) return;
				start += "<GetRoadInfoResult>".length();
				String jsonStr = strresponse.substring(start, end);
					
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(jsonStr);
				} catch (JSONException e) {
					
					// Json type is array???
					try {
						JSONArray jsonRootArray = new JSONArray(jsonStr);
						if (jsonRootArray.length() == 0) return;
						
						jsonObj = jsonRootArray.getJSONObject(0);
					} catch (JSONException e1) {
						return;
					}
					
					JSONArray jDT_CityCMS = null;
					JSONArray jDT_AccidentProne = null;
					JSONArray jView_RoadSpeed = null;
					JSONArray jDT_CityEvent = null;
					JSONArray jGetCCTVByRoadID = null;
					JSONArray jNote = null;
					JSONArray jNote_Road = null;
					
					if (tmcInfo == null) tmcInfo = new TMC_INFO();
					else tmcInfo.clear();
					
					if (jsonObj.has("DT_CityCMS"))
					{
						try {
							jDT_CityCMS = jsonObj.getJSONArray("DT_CityCMS");
							
							for (int iii=0;iii<jDT_CityCMS.length();iii++)
							{
								JSONObject jObj = jDT_CityCMS.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.DT_CityCMS item = new TMC_INFO.DT_CityCMS();
								item.DC_CMSID = jObj.optString("DC_CMSID");
								item.DC_Location = jObj.optString("DC_Location");
								item.DC_Content = jObj.optString("DC_Content");
								item.DC_UpdateTime = jObj.optString("DC_UpdateTime");
								item.DC_CityID = jObj.optInt("DC_CityID");
								item.DC_px = jObj.optInt("DC_px");
								item.DC_py = jObj.optInt("DC_py");
								item.DC_RoadID = jObj.optInt("DC_RoadID");
								
								if (tmcInfo.cityCMS == null)
								{
									tmcInfo.cityCMS = new ArrayList<TMC_INFO.DT_CityCMS>();
								}
								
								tmcInfo.cityCMS.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (jsonObj.has("DT_AccidentProne"))
					{
						try {
							jDT_AccidentProne = jsonObj.getJSONArray("DT_AccidentProne");
							
							for (int iii=0;iii<jDT_AccidentProne.length();iii++)
							{
								JSONObject jObj = jDT_AccidentProne.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.DT_AccidentProne item = new TMC_INFO.DT_AccidentProne();
								
								item.DC_Content = jObj.optString("DC_Content");
								item.DC_CityID = jObj.optInt("DC_CityID");
								item.DC_px = jObj.optInt("DC_px");
								item.DC_py = jObj.optInt("DC_py");
								item.DC_eType = jObj.optString("DC_eType");
								item.DC_RoadID = jObj.optInt("DC_RoadID");
								item.DC_HappenTime = jObj.optString("DC_HappenTime");
								item.DC_UpdateTime = jObj.optString("DC_UpdateTime");
								item.DC_RoadName = jObj.optString("DC_RoadName");
								
								if (tmcInfo.accidentProne == null)
								{
									tmcInfo.accidentProne = new ArrayList<TMC_INFO.DT_AccidentProne>();
								}
								
								tmcInfo.accidentProne.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (jsonObj.has("View_RoadSpeed"))
					{
						try {
							jView_RoadSpeed = jsonObj.getJSONArray("View_RoadSpeed");
							for (int iii=0;iii<jView_RoadSpeed.length();iii++)
							{
								JSONObject jObj = jView_RoadSpeed.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.View_RoadSpeed item = new TMC_INFO.View_RoadSpeed();
								
								item.RoadID = jObj.optInt("RoadID");
								item.Speed = jObj.optInt("Speed");
								
								if (tmcInfo.viewRoadSpeed == null)
								{
									tmcInfo.viewRoadSpeed = new ArrayList<TMC_INFO.View_RoadSpeed>();
								}
								
								tmcInfo.viewRoadSpeed.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (jsonObj.has("DT_CityEvent"))
					{
						try {
							jDT_CityEvent = jsonObj.getJSONArray("DT_CityEvent");
							for (int iii=0;iii<jDT_CityEvent.length();iii++)
							{
								JSONObject jObj = jDT_CityEvent.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.DT_CityEvent item = new TMC_INFO.DT_CityEvent();
								
								item.DC_Content = jObj.optString("DC_Content");
								item.DC_CityID = jObj.optInt("DC_CityID");
								item.DC_px = jObj.optInt("DC_px");
								item.DC_py = jObj.optInt("DC_py");
								item.DC_eType = jObj.optString("DC_eType");
								item.DC_RoadID = jObj.optInt("DC_RoadID");
								item.DC_HappenTime = jObj.optString("DC_HappenTime");
								item.DC_UpdateTime = jObj.optString("DC_UpdateTime");
								item.DC_RoadName = jObj.optString("DC_RoadName");
								
								if (tmcInfo.cityEvent == null)
								{
									tmcInfo.cityEvent = new ArrayList<TMC_INFO.DT_CityEvent>();
								}
								
								tmcInfo.cityEvent.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (jsonObj.has("GetCCTVByRoadID"))
					{
						try {
							jGetCCTVByRoadID = jsonObj.getJSONArray("GetCCTVByRoadID");
							for (int iii=0;iii<jGetCCTVByRoadID.length();iii++)
							{
								JSONObject jObj = jGetCCTVByRoadID.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.GetCCTVByRoadID item = new TMC_INFO.GetCCTVByRoadID();
								
								item.RoadID = jObj.optInt("RoadID");
								item.CameraID = jObj.optInt("CameraID");
								item.lat = jObj.optInt("Lat");
								item.lon = jObj.optInt("Lon");
								item.Camera_Name = jObj.optString("Camera_Name");
								
								if (tmcInfo.getCCTVByRoadID == null)
								{
									tmcInfo.getCCTVByRoadID = new ArrayList<TMC_INFO.GetCCTVByRoadID>();
								}
								
								tmcInfo.getCCTVByRoadID.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (jsonObj.has("Note"))
					{
						try {
							jNote = jsonObj.getJSONArray("Note");
							for (int iii=0;iii<jNote.length();iii++)
							{
								JSONObject jObj = jNote.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.Note item = new TMC_INFO.Note();
								
								item.NoteID = jObj.optInt("NoteID");
								item.Note = jObj.optString("Note");
								item.UpdateTime = jObj.optString("UpdateTime");
								
								if (tmcInfo.note == null)
								{
									tmcInfo.note = new ArrayList<TMC_INFO.Note>();
								}
								
								tmcInfo.note.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (jsonObj.has("Note_Road"))
					{
						try {
							jNote_Road = jsonObj.getJSONArray("Note_Road");
							for (int iii=0;iii<jNote_Road.length();iii++)
							{
								JSONObject jObj = jNote_Road.optJSONObject(iii);
								if (jObj == null) continue;
								
								TMC_INFO.Note_Road item = new TMC_INFO.Note_Road();
								
								item.RoadID = jObj.optInt("RoadID");
								item.STIME = jObj.optString("STIME");
								item.ETIME = jObj.optString("ETIME");
								item.NoteID = jObj.optInt("NoteID");
								
								if (tmcInfo.noteRoad == null)
								{
									tmcInfo.noteRoad = new ArrayList<TMC_INFO.Note_Road>();
								}
								
								tmcInfo.noteRoad.add(item);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					Log.d("dckim", tmcInfo.toString());
					
					
					return;
					
				}
				
				
				
				
				
				
				
			}
		}).start();
		
		
	    //[urlRequest setHTTPMethod:@"POST"];
//	    [urlRequest setHTTPBody:[xmlStr dataUsingEncoding:NSUTF8StringEncoding]];
	    
//	    NSURLConnection* urlConnection = [[NSURLConnection alloc] initWithRequest:urlRequest delegate:self];
//		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
//		param.add(new BasicNameValuePair("key", "value"));
		
//		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(parameters, encoding)
	}
	
	public static String makeXMLStr()
	{
		
		Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        
        String xmlStr = "<soap:Envelope"+
                            " xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\""+
                            " xmlns:d=\"http://www.w3.org/2001/XMLSchema\""+
                            " xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\""+
                            " xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
                            "<soap:Header>"+
                            "<AuthHeader xmlns=\"http://www.gotcha.com.tw/WS_Info\">"+
                            "<Username>352455052559143</Username>"+
                            "<Password>mngp8y</Password>"+
                            "<HashKey>jfhl7r</HashKey>"+
                            "</AuthHeader>"+
                            "</soap:Header>"+
                            "<soap:Body>"+
                            "<GetRoadInfo xmlns=\"http://www.gotcha.com.tw/WS_Info\">"+
                            "<str_Json>{\"GetRoadInfo\":[{\"Acc_UDT\":\"2019-10-10\",\"Note_UDT\":\""+
                            today+
                            "\",\"RoadID\":\""+
                            roadIds+
                            "\"}]}</str_Json>"+
                            "</GetRoadInfo>"+
                            "</soap:Body>"+
                            "</soap:Envelope>";
         
		return xmlStr;
		
	}
	*/
}
