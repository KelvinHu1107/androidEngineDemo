package com.kingwaytek.naviking3d.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import kr.co.citus.engine.ReceiveTMCInfo;
import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.citus_listener;
import kr.co.citus.engine.struct.RG_GUIDE_INFO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinovoice.hcicloudsdk.api.HciCloudSys;
import com.sinovoice.hcicloudsdk.common.HciErrorCode;
import com.sinovoice.hcicloudsdk.common.InitParam;

public class UI_Caution extends Activity {

	public static final int EVT_SHOW_SPLASH = 0;
	public static final int EVT_INITIALIZE_ENGINE = 1;
	public static final int EVT_HIDE_SPLASH = 2;
	Handler mSplashHandler = null;
	
	public static boolean m_gpsAct;
	public static LocationManager locationManager = null;
	public static DisplayLocationListener locationListener=null;

	public static int 		isActive = 0;
	static double 	speed = .0;
	static double 	direction = .0;
	static double 	longitude = .0;
	static double 	latitude = .0;
	static double 	HDOP = .0;
	static double 	altitude = .0;
	static int 		satelliteNum = 0;
	String m_RousenSavePath ;
	String m_RousenSaveUserPath ;
	static citus_listener listener ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_caution);
        
        ShowSplash();
        
        TextView viewVersion = (TextView)findViewById(R.id.textViewEngineVersion);
        viewVersion.setText(String.format("engine version = (%s)", citus_api.SYS_GetEngineVersion()));
        
        Button btnAccept = (Button)findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UI_Caution.this, MapViewActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
    }
    
    void showDialogForDbNotExist(){
    	AlertDialog.Builder ab = new AlertDialog.Builder(UI_Caution.this);
	    ab.setTitle("Error");
	    ab.setMessage("No Rousen Path!!!!");
	    ab.setNegativeButton("Close", new DialogInterface.OnClickListener()
	    {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				UI_Caution.this.finish();
				
			}
	    });
	    ab.show();
    }
    
    void ShowSplash()
    {
    	if (mSplashHandler == null)
    	{
    		mSplashHandler = new Handler()
    		{
    			public void handleMessage(Message msg)
    			{
    				switch (msg.what) {
    				case EVT_SHOW_SPLASH:
    				{
    					LinearLayout splashLayer = (LinearLayout)UI_Caution.this.findViewById(R.id.layerSplash);
    					splashLayer.setVisibility(View.VISIBLE);
    					
    					mSplashHandler.sendEmptyMessageDelayed(EVT_INITIALIZE_ENGINE, 0);
    				}
    					break;
    				case EVT_INITIALIZE_ENGINE:
    				{
    					// Setting Data Path
    					String path = getRousenPath();
    					if(path == null){
    						showDialogForDbNotExist();
    						return ;
    					}    					
    					citus_api.setEnginePath(path, path);
    					    					
    					/*
    					// Write TTS Auth file to rousen path
    					String ttsAuthPath = m_RousenPath+"/SAVE";
//    					File ttsAuthDir = new File(ttsAuthPath);
//    					if (!ttsAuthDir.exists())
//    					{
//    						ttsAuthDir.mkdir();
//    					}
//    					
    					File ttsAuthFile = new File(ttsAuthPath+"/HCI_AUTH_FOREVER");
    					if (!ttsAuthFile.exists())		// if TTS Auth file not found, write file frm assets
    					{
    						InputStream myInput;
							try {
								myInput = UI_Caution.this.getAssets().open("HCI_AUTH_FOREVER.dat");
	    				        OutputStream myOutput = new FileOutputStream(ttsAuthPath+"/HCI_AUTH_FOREVER");

	    				        byte[] buffer = new byte[1024];
	    				        int length;
	    				        while ((length = myInput.read(buffer)) > 0) {
	    				                myOutput.write(buffer, 0, length);
	    				        }

	    				        myOutput.flush();
	    				        myOutput.close();
	    				        myInput.close();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

    					}
    					
    					
    					// Initialize TTS Module
    					//String strConfig = "logFileSize=1024,logLevel=5,logFilePath=/storage/sdcard0/HciCloudTtsExample/log,cloudUrl=http://test.api.hcicloud.com:8888,autoCloudAuth=no,authPath=/data/data/com.sinovoice.example.tts/files,developerKey=13375f6b3f069eb5437744e1f30d8e8f,logFileCount=5,appKey=0c5d548f";
    					//String authDirPath = ttsAuthPath;//UI_Caution.this.getFilesDir().getAbsolutePath() + "/HCI_AUTH_FOREVER.dat";
    					String authDirPath = ttsAuthPath;//UI_Caution.this.getFilesDir().getAbsolutePath();
    					
    					// ?�諭꾩�?��?��?��?룞�?�セ?�썲?��?��?
    					InitParam initparam = new InitParam();	
    					// ?��?룞�?��?��좎럩?��?��?�쇽?��좎�?�諭꾩�?��?��좎�?��?쳸�?��?��?룞�?��?�ı�??��좑�?
    					initparam.addParam(InitParam.PARAM_KEY_AUTH_PATH, authDirPath);
    					// ?��?��?룞�?��?�뀐�??��?�럩?��좎�?��?걹�??��?��?����?룞�?�걹?�퐴��?? ?�쨪��?�썲?��?��?筌�??�썽?��?룞�?��?��좎럥?��?��좎�?�쇿?��?��?�?    					initparam.addParam(InitParam.PARAM_KEY_AUTO_CLOUD_AUTH, "no");
    					// ?�썲?��?��?��좎�?�쇿?��?�썲?��?��?��?��?룞�?�썹몭�??��?輿삼?��좎뜴?�썲?��?��?��?룞�?�쳸�우?��좎�?�쇿?��?��?ı?��?궍�??��??
    					initparam.addParam(InitParam.PARAM_KEY_CLOUD_URL, "http://api.hcicloud.com:8888");	
    					// 岳�??��좎뜴?�썩뫜�?��?�뀐�??�퓩?�썲?��?��?�쇿?��?��?紐꾬?��?꾬�??��?��좎�?��?쳸�?��?��?룞�?��?�ı�??�궪�쇿?��?��?��?�·�??��?�뀐�??��?�먯?��좎뜴?��좑�?
    					initparam.addParam(InitParam.PARAM_KEY_APP_KEY, "0c5d548f");
    					// ?�キ?��?��좎뜴?��?챷�??�챷?��?��?룞�?��?��?�쇿?��?��?��?룞�?��?硫ο�?�쇱?��좎�?�뼨��?��?濡ㅿ?��좎뜴?��?��?��?�뀐�??�썲?��?�?    					initparam.addParam(InitParam.PARAM_KEY_DEVELOPER_KEY, "13375f6b3f069eb5437744e1f30d8e8f");

    					String logDirPath = "";
    					// ?�諭꾩�?�챶?��?챷�??��좑�?
    					String sdcardState = Environment.getExternalStorageState();
    					if(Environment.MEDIA_MOUNTED.equals(sdcardState)){
    						//?��?�쇿?��?��?�뇦?�럩?�썲?��?�?    						String logDirPath = null;
    						if(Environment.MEDIA_MOUNTED.equals(sdcardState)){
//    							logDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NAVI_SAMPLE/SAVE/defaultuser";
    							logDirPath = m_RousenSavePath + "/SAVE/defaultuser";
    							File fileDir = new File(logDirPath);
    							if (!fileDir.getParentFile().exists()) {
    								fileDir.getParentFile().mkdir();
    							}
    							if (!fileDir.exists()) {
    								fileDir.mkdir();
    							}
    						}
    					
    						initparam.addParam(InitParam.PARAM_KEY_LOG_FILE_PATH, logDirPath);    					
    						initparam.addParam(InitParam.PARAM_KEY_LOG_FILE_COUNT, "5");    					
    						initparam.addParam(InitParam.PARAM_KEY_LOG_FILE_SIZE, "1024");
    					
    					}
    					String strConfig = initparam.getStringConfig();
    					
    			        int nRet = HciCloudSys.hciInit(strConfig, UI_Caution.this);
    			        if(nRet != HciErrorCode.HCI_ERR_NONE){
    			        	Toast.makeText(UI_Caution.this, "TTS Initialize falied", Toast.LENGTH_SHORT).show();
    			        }else{
    			        	//Toast.makeText(UI_Caution.this, "TTS Initialize success", Toast.LENGTH_SHORT).show();
    			        }
    			        */
    			        // Other Part of TTS Init function in Rousen Engine Code(GNS_SOUND.cpp)
    			        // 2014. 01 .21 by dckim
    					
    					// Initialize Engine
//    					getRousenPath();
    					
						DisplayMetrics displayMetrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(
								displayMetrics);
						int deviceWidth = displayMetrics.widthPixels;
						int deviceHeight =displayMetrics.heightPixels;
    					 
						listener = getCitusListner();
    					int ret = citus_api.UI_InitializeAll(UI_Caution.this, listener, deviceWidth, deviceHeight);
    					
    					if (ret != 0)
    					{
    						// initialize fail
    						AlertDialog.Builder ab = new AlertDialog.Builder(UI_Caution.this);
    					    ab.setTitle("Error");
    					    ab.setMessage("Cannot initialize engine, please download map data again !!!!");
    					    ab.setNegativeButton("Close", new DialogInterface.OnClickListener()
    					    {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									UI_Caution.this.finish();
									
								}
    					    });
    					    ab.show();
    						return;
    					}
    					
    					citus_api.SYS_SetAutoZoom(citus_api.AUTO_ZOOM_OFF);
    					GPS_Connect();
    					
    					// if ret value is not 0, is error
    					Log.i("map3d", "init");
    					mSplashHandler.sendEmptyMessageDelayed(EVT_HIDE_SPLASH, 1000);
    				}
    					break;
    				case EVT_HIDE_SPLASH:
    				{
    					LinearLayout splashLayer = (LinearLayout)UI_Caution.this.findViewById(R.id.layerSplash);
    					splashLayer.setVisibility(View.GONE);
    				}
    					break;

    				}
    			}
    		};
    	}
    	mSplashHandler.sendEmptyMessageDelayed(EVT_SHOW_SPLASH, 0);
    	
    }
    
    citus_listener getCitusListner(){
    	return new citus_listener(){

			public AudioManager SNDmanager;
			public MediaPlayer SNDplayer;
				
			@Override
			public int SNDplayer_GetVolume() {
				SNDmanager = (AudioManager)getSystemService(AUDIO_SERVICE);
				
				int maxVal = SNDmanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				int nCurrentVol = SNDmanager.getStreamVolume(AudioManager.STREAM_MUSIC);	// ?��?�ㅼ?��좎�?�쇿칰�??��??
				
				if (((nCurrentVol < maxVal) && (maxVal % 2)==1 && (nCurrentVol % 2)==0)
					|| ((nCurrentVol < maxVal) && (maxVal % 2)==0 && (nCurrentVol % 2)==1))
					nCurrentVol++;
				
//				if(nCurrentVol < maxVal - 8)
//					nCurrentVol = 0;
				
				return nCurrentVol;
			}

			@Override
			public void SNDplayer_SetVolume(int value) {
				SNDmanager = (AudioManager)getSystemService(AUDIO_SERVICE);
				if(value == 0)
					value++;
				
				if(value < 1)	// Mute
				{
					SNDmanager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0   ); //AudioManager.FLAG_SHOW_UI
				}
				else
				{
					int maxVal = SNDmanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					SNDmanager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVal-(10-(value*2)), 0  ); /// * AudioManager.FLAG_SHOW_UI * /
				}								
			}

			@Override
			public void SNDplayer_Play(String nSndFile) {
				if(SNDplayer_GetVolume() == 0)
					return;
				
				if (SNDfileLoad(nSndFile))
				{
					int nLoopChk = 0;
//					m_isplaysnd = true;
					while (true) 
					{
						if (!SNDplayer.isPlaying()) 
						{
							SNDplayer.start();

							int nPlayChk = 0;
							while (SNDplayer.isPlaying()) 
							{
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									SNDplayer.reset();
									break;
								}
								nPlayChk++;
								
								if(nPlayChk > 50)	break;
							}
							break;
						} 
						else 
						{
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								SNDplayer.reset();
								break;
							}
							nLoopChk++;
						}

						if (nLoopChk > 50)	break;
					}
				} 
				else
				{
					SNDplayer.stop();
				}
				
			}

			@Override
			public boolean SNDfileLoad(String SNDfile) {
				if (SNDplayer==null)
					SNDplayer = new MediaPlayer();
				
				do {
					if (!SNDplayer.isPlaying())
						SNDplayer.reset();
				} while (SNDplayer.isPlaying());
				
				try
				{
					try
					{
						if(SNDplayer.isPlaying())
							return false;
					
						SNDplayer.setDataSource(SNDfile);	
					}
					catch(Exception e)
					{
						return false;
					}
					try
					{
						SNDplayer.prepare();	
					}
					catch(Exception e)
					{
						return false;
					}	
					return true;
				}
				catch(Exception e)
				{
					return false;
				}
			}

			@Override
			public boolean RouteSearchFinished() {
				// get TMC info, when route search finished.
				
				// get road ids...
				if (!citus_api.RG_IsAble()) return false;
				
				RG_GUIDE_INFO info = new RG_GUIDE_INFO();
				
				StringBuffer roadids_buf = new StringBuffer();
				int npart = citus_api.RG_GetPartNum(-1);
				
				for (int iii = npart-1; iii>= 0; --iii)
				{
					if (citus_api.RG_GetGuideInfo(iii, info, false, true)) {
						if (roadids_buf.length() == 0)
							roadids_buf.append(info.kwt_RoadId);
						else
						{
							roadids_buf.append(',');
							roadids_buf.append(info.kwt_RoadId);
						}
					}
				}
				
				if (roadids_buf.length() == 0) return false;
				
				
//				ReceiveTMCInfo.roadIds = roadids_buf.toString();
//				ReceiveTMCInfo.tmcInfoRefreshCnt = 0;
//				ReceiveTMCInfo.getTMCInfo();
				
				return true;
			}
			 
		 };
    }
    
    // ************** Get Rousen Path ****************
    public String getRousenPath()
    {
		String path = "/mnt/sdcard/LocalKingMap" ;
		File file = new File(path);
		if(file.exists()){
			m_RousenSavePath = path ;
			return m_RousenSavePath ;
		}		
		
		path = "/mnt/sdcard/LocalKingMapN5" ;
		file = new File(path);
		if(file.exists()){
			m_RousenSavePath = path ;
			return m_RousenSavePath ;
		}		
        return null;	
     }
    
	public boolean GPS_Connect()
	{
		m_gpsAct = false;
		
		if(locationManager == null)
		{
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
		
		
	        locationListener = new DisplayLocationListener();
	        
			{
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1000, 0F, locationListener);
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 5000, 0F,
						locationListener);
				locationManager.addGpsStatusListener(gpsStatusListener);
			}
		}
		
		return false;
	}	
	
	public static void GPS_Close()
	{
			locationManager.removeUpdates(locationListener);
	}
	
	public final GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener(){
		
		public void onGpsStatusChanged(int event) {
			
			switch( event )
			{
				case GpsStatus.GPS_EVENT_STARTED:
				// Started...
				break ;
				
				case GpsStatus.GPS_EVENT_FIRST_FIX:
				// First Fix...
				break ;
				
				case GpsStatus.GPS_EVENT_STOPPED:
				// Stopped...
				break ;
				
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					m_gpsAct = true;
					
					GpsStatus status = locationManager.getGpsStatus(null);
					Iterable<GpsSatellite> Satellites = status.getSatellites();
					Iterator<GpsSatellite> iSatell = Satellites.iterator();
					
					int iSats = 0;
					int isReset=1;
					
					while(iSatell.hasNext())
					{
						iSats ++;
						GpsSatellite oSat = (GpsSatellite)iSatell.next();
						
						citus_api.GPS_Android_setSatellite(isReset, 1,oSat.getPrn(), (int)oSat.getElevation(), (int)oSat.getAzimuth(), (int)oSat.getSnr());
						isReset=0;
					}
					Satellites = null;
					iSatell = null;
					
					satelliteNum = iSats;
				break;
			}
		}
    };
    
public class DisplayLocationListener implements LocationListener { 
    	
		public void onLocationChanged(Location location) {
			
			m_gpsAct = true;
			
			int isFromGPS = 0;
			if (LocationManager.GPS_PROVIDER.equals(location.getProvider()))
			{
				isFromGPS = 1;
				isActive = 10;
			}
			else
			{
//				if (isActive > 0)
//					--isActive;
//				else
//					isActive = 0;
			}
			
			speed = location.getSpeed();
			direction = location.getBearing();
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			HDOP = location.getAccuracy();
			altitude = location.getAltitude();
			
			citus_api.GPS_Android_SetInfo(isFromGPS, isActive, speed, direction, longitude, latitude, HDOP, altitude);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
    }

}
