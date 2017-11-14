package com.kingwaytek.naviking3d.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import kr.co.citus.engine.ApiProxy;
import kr.co.citus.engine.TBTGraph;
import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.win_define;
import kr.co.citus.engine.struct.IPOINT;
import kr.co.citus.engine.struct.MAP_MATCH_RESULT_INFO;
import kr.co.citus.engine.struct.MAP_PICK_INFO;
import kr.co.citus.engine.struct.MM_IPC_CAND_INFO;
import kr.co.citus.engine.struct.POI_INFO;
import kr.co.citus.engine.struct.RG_GUIDE_INFO;
import kr.co.citus.engine.struct.RG_HIWAY_INFO;
import kr.co.citus.engine.struct.RG_REMAIN_INFO;
import kr.co.citus.engine.struct.ROUTE_ITEM;
import kr.co.citus.engine.struct.SIGNPOST_GUIDEINFO;
import kr.co.citus.engine.struct.TMC_ROUTE_INFO;
import kr.co.citus.engine.wrap.WrapDouble;
import kr.co.citus.engine.wrap.WrapInt;
import kr.co.citus.engine.wrap.WrapString;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.kingwaytek.naviking3d.app.RouteListActivity.RouteItem;
import com.kingwaytek.naviking3d.app.dlg.DLG_MAIN_MENU;
import com.kingwaytek.naviking3d.app.utility.BaseFloatingWindowActivity;

public class MapViewActivity extends BaseFloatingWindowActivity implements SurfaceHolder.Callback {
	
	private static Timer mTimer = null;
	private static boolean mIsNowTimerProc = false;
	static MapViewActivity mThis = null;
	ScaleGestureDetector mScaleDetector;
	static boolean m_isStart ;
	View mLayerMove = null;
	View mGroupTop = null;
	View mGroupBottom = null;
	View mGroupDemoControl = null;
	ImageView mViewEntryInfo = null;
	ImageView mViewExtInfo = null;
	ImageView mViewUnderPass = null;
	Bitmap mViewUnderPassImg = null;
	WrapInt mViewUnderPassImg_Width = new WrapInt();
	WrapInt mViewUnderPassImg_Height = new WrapInt();
	
	SeekBar mProgressDemo = null;
	ImageButton mButtonDemoControlShow = null;
	boolean mDemoControlTranslated = false;
	Bitmap mBitmapExtView = null;
	Bitmap mBitmapEntryView = null;
	VerticalProgressBar mProgressNearCross = null;
	VerticalProgressBar mProgressNearCross2 = null;
	Button mButtonNorthup = null;
	Button mButtonHeadup = null;
	Button mButtonSpeedCamera = null;
	ImageView mViewCompass = null;
	ImageView mViewCompassMove = null;
	Button mButtonPickGo = null;
	TextView mTextViewPick = null;
	boolean mIsCarCenter = true;
	LinearLayout mTBTView = null;
	TBTGraph	mTBTGraph = null;
	ArrayList<ROUTE_ITEM> mTBTDataArray = null;
	LinearLayout mTBTContents = null;
	MAP_MATCH_RESULT_INFO mm_result = new MAP_MATCH_RESULT_INFO();

	public static final int VIEW_MODE_3D      = 0;
	public static final int VIEW_MODE_DRIVE   = 1;
	public static final int VIEW_MODE_NORTHUP = 2;
	public static final int VIEW_MODE_HEADUP  = 3;

	static int mViewMode = VIEW_MODE_3D;
	
	public static void setMapViewMode(int mode){
		mViewMode = mode;
		switch (mode){		
		case VIEW_MODE_3D:
			citus_api.MV3D_SetStandardView();
			break;
		case VIEW_MODE_DRIVE:
			citus_api.MV3D_SetDriveView();
			break;
		case VIEW_MODE_NORTHUP:	
			ApiProxy.setFloat(ApiProxy.MV_LAYDOWN, 0);
			citus_api.MV3D_SetNorthup();
			//citus_api.MV_Redraw();
			//citus_api.MV_DirtyDraw();
			break;
		case VIEW_MODE_HEADUP:
			ApiProxy.setFloat(ApiProxy.MV_LAYDOWN, 0);
			citus_api.MV3D_SetHeadingup();
			//citus_api.MV_Redraw();
			//citus_api.MV_DirtyDraw();
			break;
	
		}
	}	
	int					  route_sequence_no = -1;
	ArrayList<ROUTE_ITEM> storage_RouteList = null;
	ArrayList<ROUTE_ITEM> storage_HiwayList = null;
	
	TextView mSignPostView[] = null;
	
	LinearLayout mLayCam = null;
	TextView mCamDist = null;
	TextView mCamSpeed = null;
	ImageView mCamImage = null;
	
	ScaleView mScaleView = null;
	SurfaceView mMapView = null;
	
	Button mBtnZoomIn = null;
	Button mBtnZoomOut = null;
	
	boolean mIsPortrait = true;
	
	private final static int GUIDE_MODE_NONE = 0;
	private final static int GUIDE_MODE_DEMO = 1;
	private final static int GUIDE_MODE_GUIDE = 2;
	
	private final static int EXT_MODE_NONE = 0;
	private final static int EXT_MODE_ZOOM_CROSS = 1;	///< ��댐옙筌△�ㅿ옙占쏙옙占쏙옙占쏙옙占썸에占�
	private final static int EXT_MODE_HIWAY		=	2;	///< ��⑨옙占쏙옙占쏙옙占쏙옙嚥∽옙 筌���ㅿ옙占�
	private final static int EXT_MODE_SPLIT		=	3;	///< SPLIT筌���ㅿ옙占�
	private final static int EXT_MODE_TEXT		=	4;	///< TEXT筌���ㅿ옙占�
	private final static int EXT_MODE_TBT1		=	5;	///< TBT筌���ㅿ옙占�
	private final static int EXT_MODE_ENTRY		=	6;	///< ��⑨옙占쏙옙占쏙옙占쏙옙嚥∽옙 筌�占썹�곤옙占쏙옙占� 
	private final static int EXT_MODE_LITE_VIEW	=	7;   // lite view
	private final static int EXT_MODE_NEAR_POI	=	7;   // lite view
	
	int mGuideMode = GUIDE_MODE_NONE;
	int mExtMode = EXT_MODE_NONE;	
	
	// for IPC(Inteligent Position Coretion)
	private static MM_IPC_CAND_INFO[] m_ipc_info = null;;
	private static long m_currentIPCActiveId = 0;
	private static AlertDialog m_ipc_selector = null;
	private static int m_ipc_info_count = 0;
	

	
	private static boolean mTbtToggle = true;

	@Override
	public int getLayoutResId() {
		return R.id.mainMapView;
	}

	@Override
	public Drawable getCurrentSpeedResId() {
		return null;
	}

	@Override
	public Drawable getEventWindowResId() {
		return null;
	}

	@Override
	public Drawable getCloseBtnResId() {
		return null;
	}

	@Override
	public int getActivateBtnId() {
		return R.id.buttonSpeedCamera;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// jolee - prevent re-route by GPS in route plan dialog 
		citus_api.UI_EnableAutoReroute(true);

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mThis = this;
		
		
		citus_api.SetCallbackClass("com/kingwaytek/naviking3d/app/MapViewActivity"); //UIArriveTarget
		
		setContentView(R.layout.map_activity);
		mGroupTop = findViewById(R.id.groupTop);
		mGroupBottom = findViewById(R.id.groupBottom);
		mGroupDemoControl = findViewById(R.id.groupDemoControl);
		mViewEntryInfo = (ImageView)findViewById(R.id.imageViewEntryInfo);
		mViewExtInfo = (ImageView)findViewById(R.id.imageViewExtInfo);
		mViewUnderPass = (ImageView)findViewById(R.id.imgUnderPass);
		mLayerMove = findViewById(R.id.layerMove);
		mProgressDemo = (SeekBar)findViewById(R.id.seekBarDemo);
		mButtonDemoControlShow = (ImageButton)findViewById(R.id.imageButtonShowDemo);
		mButtonDemoControlShow.setVisibility(View.GONE);
		mProgressNearCross = (VerticalProgressBar)findViewById(R.id.progressNearCross);
		mProgressNearCross.setMax(500);
		mProgressNearCross.setVisibility(View.GONE);
		mProgressNearCross2 = (VerticalProgressBar)findViewById(R.id.progressNearCross2);
		mProgressNearCross2.setMax(500);
		mProgressNearCross2.setVisibility(View.GONE);
		mButtonNorthup = (Button)findViewById(R.id.buttonNorthUp);
		mButtonHeadup = (Button)findViewById(R.id.buttonHeadUp);
		mButtonSpeedCamera = (Button)findViewById(R.id.buttonSpeedCamera);
		mViewCompass = (ImageView)findViewById(R.id.imageViewCompass);
		mViewCompassMove = (ImageView)findViewById(R.id.imageViewCompassMove);
		mButtonPickGo = (Button)findViewById(R.id.buttonPickDetail);
		mTextViewPick = (TextView)findViewById(R.id.textViewPick);
		
		mTBTView = (LinearLayout)findViewById(R.id.tbtView);
		mTBTGraph = (TBTGraph)findViewById(R.id.tbtGraph);
		mTBTContents = (LinearLayout)findViewById(R.id.tbtContents);
		
		mButtonPickGo.setVisibility(View.GONE);
		mTextViewPick.setVisibility(View.GONE);
		
		// SignPost
		mSignPostView = new TextView[4];
		mSignPostView[0] = (TextView)findViewById(R.id.txtSignPost1);
		mSignPostView[1] = (TextView)findViewById(R.id.txtSignPost2);
		mSignPostView[2] = (TextView)findViewById(R.id.txtSignPost3);
		mSignPostView[3] = (TextView)findViewById(R.id.txtSignPost4);
		mSignPostView[0].setVisibility(View.GONE);
		mSignPostView[1].setVisibility(View.GONE);
		mSignPostView[2].setVisibility(View.GONE);
		mSignPostView[3].setVisibility(View.GONE);
		
		// Speed Camera Alert View
		mLayCam = (LinearLayout)findViewById(R.id.layCam);
		mCamDist = (TextView)findViewById(R.id.txtCamDist);
		mCamSpeed = (TextView)findViewById(R.id.txtCamSpeed);
		mCamImage = (ImageView)findViewById(R.id.imgCam);
		
		mIsPortrait = Configuration.ORIENTATION_PORTRAIT  == getResources().getConfiguration().orientation;
		
		// set surface holder callback
		mMapView = (SurfaceView) findViewById(R.id.mainMapView);
		mMapView.getHolder().addCallback(this);
		
		// create bitmap for ext view
		WrapInt wrapWidth = new WrapInt();
		WrapInt wrapHeight = new WrapInt();
		citus_api.MV_GetExtViewSize(wrapWidth, wrapHeight);
		mBitmapExtView = Bitmap.createBitmap(wrapWidth.value, wrapHeight.value, Bitmap.Config.ARGB_8888);
		mBitmapEntryView = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888); // init with dummy value
		Drawable drawableExt = new BitmapDrawable(getResources(), mBitmapExtView);
		Drawable drawableEntry = new BitmapDrawable(getResources(), mBitmapEntryView);
		mViewEntryInfo.setBackgroundDrawable(drawableEntry);
		mViewExtInfo.setBackgroundDrawable(drawableExt);
		
		mViewUnderPassImg = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888); // init with dummy value
		findViews(mButtonSpeedCamera);

		mViewUnderPass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewUnderPass.setVisibility(View.GONE);
				//mViewUnderPass.setImageResource(R.drawable.underpass_01);
				//mViewUnderPass.setVisibility(View.VISIBLE);
			}
		});
		
		// When fist start, call Main menu activity.
		if (!m_isStart) {
			if (mTimer == null)
			{
				mTimer = new Timer();
				int timeInterval = citus_api.SYS_GetMainTimerInterval();
				mTimer.scheduleAtFixedRate(mTimerTask, 0, timeInterval);
			}
			
			// decide resolution
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
			{
				display.getSize(size);
			}
			else
			{
				size.x = display.getWidth();
				size.y = display.getHeight();
			}
			
			int resolution = 1;
			if (size.x * size.y <= 480 * 854)
				resolution = 1 ;
			else if (size.x * size.y <= 720 * 1280)
				resolution = 2;
			else
				resolution = 3;
			citus_api.SYS_SetResolution(resolution);
			Log.i("metrics", "Resolution: " + resolution + " " + size.x + "x" + size.y);
			
			DisplayMetrics metrics = getResources().getDisplayMetrics();
//			Log.i("metrics", "Density: " + metrics.density);
//			Log.i("metrics", "ScaleDensity: " + metrics.scaledDensity); // float
//			Log.i("metrics", "DensityDpi: " + metrics.densityDpi); // DENSITY_LOW, DENSITY_MEDIUM, DENSITY_HIGH
//			Log.i("metrics", "WidthPixels: " + metrics.widthPixels); // int
//			Log.i("metrics", "HeightPixels: " + metrics.heightPixels); // int
//			Log.i("metrics", "Xdpi: " + metrics.xdpi); // float 
//			Log.i("metrics", "Ydpi: " + metrics.ydpi); // float
			citus_api.MV3D_SetPixelPerCentimeter(metrics.densityDpi * 0.39f);
			
			Intent intent = new Intent(MapViewActivity.this,
					DLG_MAIN_MENU.class);
			startActivity(intent);
			m_isStart = true;
		}

		//citus_api.setContext(this);
		
		// Go current button click
		Button buttonGoCurrent = (Button)findViewById(R.id.buttonGoCurrent);
		buttonGoCurrent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				citus_api.UI_SetMapMoveMode((byte) 0, true, true);
				citus_api.UI_GoCurrent();
				//citus_api.MV_Redraw();
				//citus_api.MV_DirtyDraw();
			}
		});
		
		// laydown seekbar change listener
		VerticalSeekBar seekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBarLaydown);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				citus_api.MV3D_SetPanning(false);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seeBar) {
				citus_api.MV3D_SetPanning(true);
			}
			
			@Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				if (fromUser) {
					float laydown = 0.7f * (20 - progress) / 20;
					ApiProxy.setFloat(ApiProxy.MV_LAYDOWN, laydown);
				}
		    }       
		});
		
		// demo seekbar change listener
		mProgressDemo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser && mGuideMode == GUIDE_MODE_DEMO) {
					int total = seekBar.getMax();
					citus_api.RG_DemoScroll(progress, total);
					citus_api.LANE_Reset();
					citus_api.CPF_Reset();
					
					if (mExtMode == EXT_MODE_ZOOM_CROSS)
						citus_api.SYS_SetExtMode(EXT_MODE_NONE);
					
					citus_api.UI_GoCurrent();
					citus_api.SYS_PauseAutoLevel(10);
					//citus_api.MV_Redraw();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
		});
		
		Button btnDriveView = (Button)findViewById(R.id.buttonDriveView);
		btnDriveView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setMapViewMode(VIEW_MODE_DRIVE);
				//citus_api.MV3D_SetDriveView();				
				mBtnZoomOut.setEnabled(citus_api.MV3D_IsCanZoomOut());
				mBtnZoomIn.setEnabled(citus_api.MV3D_IsCanZoomIn());
			}
		});
		
		Button btn3DView = (Button)findViewById(R.id.buttonStandardView);
		btn3DView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setMapViewMode(VIEW_MODE_3D);
				//citus_api.MV3D_SetStandardView();	
				mBtnZoomOut.setEnabled(citus_api.MV3D_IsCanZoomOut());
				mBtnZoomIn.setEnabled(citus_api.MV3D_IsCanZoomIn());
			}
		});
		
		mBtnZoomIn = (Button)findViewById(R.id.buttonZoomIn);
		mBtnZoomIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				citus_api.UI_ZoomIn();
				mBtnZoomIn.setEnabled(citus_api.MV3D_IsCanZoomIn());
				mBtnZoomOut.setEnabled(citus_api.MV3D_IsCanZoomOut());
			}
		});
		
		mBtnZoomOut = (Button)findViewById(R.id.buttonZoomOut);
		mBtnZoomOut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				citus_api.UI_ZoomOut();
				mBtnZoomOut.setEnabled(citus_api.MV3D_IsCanZoomOut());
				mBtnZoomIn.setEnabled(citus_api.MV3D_IsCanZoomIn());
			}
		});
		
		// Pinch Gesture Detector setting
		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
		
		mGroupTop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTbtToggle = !mTbtToggle;
				
			}
		});
		
		TextView currentRoadView = (TextView)findViewById(R.id.textViewCurrentRoad);
		currentRoadView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				int no = 0;
				
				// check current road has near road information
				// and find near road
				if (citus_api.IPC_HasNearRoad())
					no = citus_api.IPC_FindNearRoad();
				
				// if there is no near road then close selector  
				if (no == 0)
				{
					if (m_ipc_selector != null && m_ipc_selector.isShowing())
					{
						m_ipc_selector.dismiss();
						m_ipc_selector = null;
					}
					return;
				}
				
				// initialize
				for (int iii=0; iii<6; iii++)
					m_ipc_info[iii].init();
				
				// get near road information from IPC
				m_ipc_info_count = citus_api.IPC_GetCandidates(m_ipc_info, 6);
				
				if (m_ipc_selector != null && m_ipc_selector.isShowing())
				{
					m_ipc_selector.dismiss();
					m_ipc_selector = null;
				}
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MapViewActivity.this);
				alertBuilder.setTitle("Select Current Road");
				String[] items = new String[m_ipc_info_count+1];
				for (int iii=0;iii<m_ipc_info_count;iii++)
				{
					// must fix
					// get road name, use mesh_idx, link_idx;
					String rname = m_ipc_info[iii].roadname;//citus_api.NET_GetRoadName(m_ipc_info[iii].mesh_idx, m_ipc_info[iii].link_idx);
					// test code
					
					items[iii] = rname;
					
					// if is_curr = 1 then this item is current road link, it should display actived/selected.
					if (m_ipc_info[iii].is_curr != 0)
						items[iii] += "(*)";
				}
				items[m_ipc_info_count] = "Cancel";
				
				// show selector for selecting other near road
				alertBuilder.setItems(items, new android.content.DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						if (m_ipc_info_count == which)
						{
							// Selected Cancel item
							return;
						}
						
						// user select other near road then change map matching location to that.
						citus_api.IPC_ChangeRoad(which);
						
						Toast.makeText(MapViewActivity.this, "Select IPC Item Idx:"+which, Toast.LENGTH_SHORT).show();
					}
				});
				m_ipc_selector = alertBuilder.create();
				m_ipc_selector.show();
				
			}
		});
		
		// for IPC
		if (m_ipc_info == null)
		{
			m_ipc_info = new MM_IPC_CAND_INFO[6];
			for (int iii=0;iii<6;iii++)
				m_ipc_info[iii] = new MM_IPC_CAND_INFO();
		}
		
		
		// Scale Bar
		mScaleView = new ScaleView(this);
	
	}
	
	public void onClickTest(View view) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle("TestMode - select test function");
		String [] items = {"Set Start Pos", "Set Via Pos", "Set End Pos", "Route Search", "Start/Stop Demo", "Route Cancel", citus_api.SYS_IsUseVoiceGuide()?"OFF Voice Guide":"ON Voice Guide", "Get CCP Info","Hide","Close"};
		alertBuilder.setItems(items, new android.content.DialogInterface.OnClickListener() {					
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0)		// set start pos
				{
					POI_INFO info = new POI_INFO();
					if (citus_api.SYS_GetSearchPos(info))
					{
						citus_api.UI_SetStartPos(true, true);
					}
					
				}
				else if (which == 1)	// set via pos
				{
					POI_INFO info = new POI_INFO();
					if (citus_api.SYS_GetSearchPos(info))
					{
						citus_api.UI_SetMidPos(true, false, true);
					}
					
				}
				else if (which == 2)	// set end pos
				{
					POI_INFO info = new POI_INFO();
					if (citus_api.SYS_GetSearchPos(info))
					{
						citus_api.UI_SetEndPos(true, true);
					}
					
				}
				else if (which == 3) {	// set route search
					
					// initialize avoid information
					citus_api.avoid_reset();
					citus_api.UI_SetRpIdx(0);
					WrapInt errCode = new WrapInt();
					citus_api.UI_RoutePlan((citus_api.SYS_IsStartPos()==false), false, true, 0, errCode, true);
					
					//ApiProxy.execute(ApiProxy.TEST_ROUTE_SEARCH);
				} else if (which == 4) {  // demo mode
					if (ApiProxy.getInteger(ApiProxy.SYS_GUIDE_MODE, 0) == 1) {
						ApiProxy.execute(ApiProxy.GUIDE_STOP_DEMO);
						citus_api.RG_GuideRemoveAll();
						citus_api.UI_RouteCancel(true);
						citus_api.SYS_ResetRPPos(citus_api.RPP_MID_ALL, true);
						citus_api.SYS_ResetRPPos(citus_api.RPP_START, true);
						citus_api.SYS_ResetRPPos(citus_api.RPP_END, true);
					} else {
						ApiProxy.execute(ApiProxy.GUIDE_START_DEMO);
					}
				}
				else if (which == 5)	// route cancel
				{
					citus_api.RG_GuideRemoveAll();
					citus_api.UI_RouteCancel(true);
					citus_api.SYS_ResetRPPos(citus_api.RPP_MID_ALL, true);
					citus_api.SYS_ResetRPPos(citus_api.RPP_START, true);
					citus_api.SYS_ResetRPPos(citus_api.RPP_END, true);
				}
				else if (which == 6) 	// Toggle Voice Guide
				{
					citus_api.SYS_SetUseVoiceGuide(!citus_api.SYS_IsUseVoiceGuide());
				}
				else if (which == 7) // get ccp info
				{
					AlertDialog.Builder ab = new AlertDialog.Builder(MapViewActivity.this);
				    ab.setTitle("CCP Information");
				    MAP_MATCH_RESULT_INFO info = new MAP_MATCH_RESULT_INFO();
				    citus_api.MM_GetResult(info);
				    String str = new String();
				    WrapDouble lat = new WrapDouble();
				    WrapDouble lon = new WrapDouble();
				    str = "ccp information\n";
					
				    double ccp_x = info.x, ccp_y = info.y;
				    IPOINT car_pt = citus_api.SYS_GetCarPos();
				    str += "is_match = " + info.is_match + "\n";
				    if (info.is_match != 1) // not matched yet.
				    {
				    	ccp_x = car_pt.x;
				    	ccp_y = car_pt.y;
				    	info.roadName = citus_api.SYS_GetRoadName((int)ccp_x, (int)ccp_y, 50);
				    	info.kwt_roadId = citus_api.SYS_GetRoadId((int)ccp_x, (int)ccp_y, 50);
				    }
				    
				    str += "ccp cy = (" + ccp_x + "," + ccp_y + ")";
				    
				    citus_api.PROJ_MAPtoWGS84((int)ccp_x, (int)ccp_y, lat, lon);
				    str += "\nWGS84 = (" + lat.value + "," + lon.value + ")";
				    str += "\nroadname = " + info.roadName;
				    str += "\nroad id = " + info.kwt_roadId;
				    ab.setMessage(str);
				    ab.setNegativeButton("Close", new DialogInterface.OnClickListener()
				    {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
				    });
				    ab.show();

				}
				else if (which == 8)//hide
				{
					mGroupBottom.setVisibility(View.GONE);
					mGroupTop.setVisibility(View.GONE);					
					mViewEntryInfo.setVisibility(View.GONE);
					mViewExtInfo.setVisibility(View.GONE);
					mTBTView.setVisibility(View.GONE);
					bHideUi = true ;
				}
			}
		});
		AlertDialog alert = alertBuilder.create();
		alert.show();
	}
	static boolean bHideUi = false ;
	
	public void onClickMenu(View view) {
		Intent intent = new Intent(MapViewActivity.this,
				DLG_MAIN_MENU.class);
		startActivity(intent);
	}
	
	public void onClickNorthup(View view) { // set to head up
		
		MapViewActivity.setMapViewMode(MapViewActivity.VIEW_MODE_HEADUP);
		citus_api.MV3D_SetHeadingup();
		//citus_api.MV_Redraw();
		//citus_api.MV_DirtyDraw();
	}
	
	public void onClickHeadup(View view) { // set to north up
		setMapViewMode(VIEW_MODE_NORTHUP);
//		ApiProxy.setFloat(ApiProxy.MV_LAYDOWN, 0);
//		citus_api.MV3D_SetNorthup();
//		citus_api.MV_Redraw();
//		citus_api.MV_DirtyDraw();
	}
	
	public void onClickDemoButton(View view) {
		String tag = (String)view.getTag();
		int itag = Integer.parseInt(tag);
		if (itag == 0) { // slow
			ApiProxy.execute(ApiProxy.SYS_DEMO_SLOWER);
		} else if (itag == 1) { // fast
			ApiProxy.execute(ApiProxy.SYS_DEMO_FASTER);
		} else if (itag == 2) { // pause
			ApiProxy.execute(ApiProxy.SYS_DEMO_PAUSE);
		} else if (itag == 3) { // stop
			ApiProxy.execute(ApiProxy.GUIDE_STOP_DEMO);
			citus_api.RG_GuideRemoveAll();
			citus_api.UI_RouteCancel(true);
			citus_api.SYS_ResetRPPos(citus_api.RPP_MID_ALL, true);
			citus_api.SYS_ResetRPPos(citus_api.RPP_START, true);
			citus_api.SYS_ResetRPPos(citus_api.RPP_END, true);
		} else if (itag == 4) { // hide control
			Animation ani = AnimationUtils.loadAnimation(this, R.anim.trans_left);
			ani.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
					mGroupDemoControl.setVisibility(View.GONE);
					mButtonDemoControlShow.setVisibility(View.VISIBLE);
					mDemoControlTranslated = true;
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationStart(Animation animation) {}
				
			});
			mGroupDemoControl.startAnimation(ani);
		} else if (itag == 5) { // show control
			Animation ani = AnimationUtils.loadAnimation(this, R.anim.trans_right);
			ani.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
					mGroupDemoControl.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationStart(Animation animation) {}
				
			});
			mButtonDemoControlShow.setVisibility(View.GONE);
			mGroupDemoControl.startAnimation(ani);
			mDemoControlTranslated = false;
		}
	}
	
	@Override
	public void onPause() {
		citus_api.MV3D_WaitRendering(true);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
//		if (mTimer == null)
//		{
//			mTimer = new Timer();
//			int timeInterval = citus_api.SYS_GetMainTimerInterval();
//			mTimer.scheduleAtFixedRate(mTimerTask, 0, timeInterval);
//		}
		Log.i("map3d", "OnResume");

		citus_api.MV_SetCameraNormal();
		citus_api.UI_SetMapMoveMode((byte) 0, true, true);
		citus_api.MV3D_DrawRouteSelected();
		citus_api.UI_GoCurrent();
		//citus_api.MV_Redraw();
		//citus_api.MV_DirtyDraw();
		citus_api.MV3D_WaitRendering(false);
		
		SeekBar laydownBar = (SeekBar)findViewById(R.id.verticalSeekBarLaydown);
		int seekBarMax = laydownBar.getMax();
		float laydown = ApiProxy.getFloat(ApiProxy.MV_LAYDOWN, 0.f);
		float ratio = seekBarMax / 0.7f;
		int progress = (int)(laydown * ratio);
		if (progress > seekBarMax)
			progress = seekBarMax;
		progress = seekBarMax - progress;
		laydownBar.setProgress(progress);
		
		mScaleView.refreshDrawableState();
		mScaleView.invalidate();
		
		mBtnZoomIn.setEnabled(citus_api.MV3D_IsCanZoomIn());
		mBtnZoomOut.setEnabled(citus_api.MV3D_IsCanZoomOut());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		citus_api.MV3D_OnSurfaceChanged(holder.getSurface(), width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("map3d", "==================> surface created");
		citus_api.MV3D_OnSurfaceCreated(holder.getSurface());
		citus_api.MV3D_ClearSearchMark();
		
		int err = citus_api.SND_TTS_IsError(0);
		String msg = null;
		if (err > 0 && err < 10) {
			citus_api.SND_TTS_IsError(1);
			if (err == 2)
				msg = "TTS engine time expired...";
			else
				msg = "TTS engine error...";
		}
		if (err == 0)
			msg = "TTS engine OK.";
		if (msg != null) {
			//Toast.makeText(this, msg, 1).show();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	// Touch Event
	boolean onePointDown = false;
	double prevAngle = 0;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		m_timerlock = true;
		int action = event.getAction();
		int ptcount = event.getPointerCount();
		
		if (ptcount == 1) {
			float ptrX = event.getX();
			float ptrY = event.getY();
			
			int[] mapLocation = new int[2];
			mMapView.getLocationOnScreen(mapLocation);
			ptrX -= mapLocation[0];
			ptrY -= mapLocation[1];
			
			switch(action)                                                 
			{
			case MotionEvent.ACTION_DOWN:
				onePointDown = true;
				citus_api.SYS_PauseMoveCenter();
				citus_api.UI_OnTouch(win_define.WM_LBUTTONDOWN, (int)ptrX, (int)ptrY, 0);
				break;
			case MotionEvent.ACTION_MOVE:
				if (onePointDown && !mScaleDetector.isInProgress()) {
					citus_api.UI_OnTouch(win_define.WM_MOUSEMOVE, (int)ptrX, (int)ptrY, 0);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				if (onePointDown) {
					citus_api.UI_OnTouch(win_define.WM_LBUTTONUP, (int)ptrX, (int)ptrY, 0);
					onMapPick((int)ptrX, (int)ptrY);
				}
				onePointDown = false;
				break;
			}
		} else if (ptcount == 2) {
			onePointDown = false;
			switch (action)
			{
			case MotionEvent.ACTION_POINTER_2_DOWN:
				prevAngle = Math.atan2((event.getX(0) - event.getX(1)), (event.getY(0) - event.getY(1)));
				citus_api.MV3D_BeginRotate((int)((event.getX(0) + event.getX(1)) / 2), (int)((event.getY(0) + event.getY(1))/2));
				citus_api.UI_OnRotate(win_define.WM_LBUTTONDOWN, 0);
				break;
			case MotionEvent.ACTION_MOVE:
				int historySize = event.getHistorySize();
				if (historySize > 0) {
					double deltaX = (event.getX(0) - event.getX(1));
					double deltaY = (event.getY(0) - event.getY(1));
					double angle = Math.atan2(deltaX, deltaY);
					double deltaRad = angle - prevAngle;
					citus_api.UI_OnRotate(win_define.WM_MOUSEWHEEL, deltaRad);
					prevAngle = angle;
				}
				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				citus_api.UI_OnRotate(win_define.WM_LBUTTONUP, 0);
				citus_api.MV3D_EndRotate();
				break;
			default:
				citus_api.UI_OnRotate(win_define.WM_LBUTTONUP, 0);
				citus_api.MV3D_EndRotate();
				break;
			}
		}
		
		mScaleView.refreshDrawableState();
		mScaleView.invalidate();
		m_timerlock = false;
		return super.onTouchEvent(event);
	}
	
	static boolean m_timerlock ;
	// Timer Procesor
	private static TimerTask mTimerTask = new  TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (m_timerlock || mIsNowTimerProc)
				return;
			
			//citus_api.UI_OnTimer();
			mIsNowTimerProc = true;
			mTimerHandler.sendEmptyMessage(0xFFFF);
		}
	};
	
	private static Handler mTimerHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mThis.onUITimer(msg);
		}	
	};
	
	// Location Listener
	LocationListener mLocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// Location Changed
			// input data to Rousen Engine
			
		}
	};
	
	// Scale Gesture Detector for pinch zoom
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		float prevScale = 1.0f;
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			citus_api.MV3D_SetPanning(true);
			citus_api.MV3D_BeginRotate((int)detector.getFocusX(), (int)detector.getFocusY());
			prevScale = detector.getScaleFactor();
			return super.onScaleBegin(detector);
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			citus_api.MV3D_SetPanning(false);
			citus_api.MV3D_EndRotate();
			super.onScaleEnd(detector);
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {		
			float curScale = detector.getScaleFactor();
			float scale = curScale / prevScale;
			citus_api.MV_OnScale(scale, 1.0f);
			prevScale = curScale;	
			
			// test code for changine bird angle of each zoomfactor
			if (true)
			{
				double zf_min   = 512;
			
				double zf_drive = 90;
				double zf_3d    = 1536; 
				double zf_max   = 50000;
				double ang_drive = 13;
				double ang_3d    = 27;
				double ang_2d    = 90;
							
				double zf = citus_api.MV3D_GetZf();
				double ang = 90;
				if (zf <= zf_drive)
					ang = ang_drive;
				else if (zf <= zf_3d)
				{
					ang = ang_drive + (ang_3d-ang_drive) * (zf-zf_drive) / (zf_3d - zf_drive);
				}
				else if (zf <= zf_max)
				{
					ang = ang_3d + (ang_2d-ang_3d) * (zf-zf_3d) / (zf_max - zf_3d);
				}
				else 
					ang = ang_2d;
				
			    double laydown = 0.9 - ang / 100.0;
	
			    citus_api.MV3D_SetTrans(laydown);
			}

			
					
			return super.onScale(detector);
		}
	}
	
	public static void UIArriveTarget(int val)
	{
		//Toast.makeText(mThis, "End Touch down", 5).show();
	}
	
	private int timerCount = 0;
	private void onUITimer(Message msg)
	{
		if (msg.what != 0xFFFF)		// Timer Event
			return;
		
		int rg_past_dist = citus_api.RG_GetPastDist();

		timerCount++;

		if (UI_Caution.isActive > 0)
		{
			if (timerCount % (int)(1000.f/(float)citus_api.SYS_GetMainTimerInterval()) == 0)
				--UI_Caution.isActive;
		}
		else
			UI_Caution.isActive = 0;
		
		
		if (timerCount % (int)(1000.f/(float)citus_api.SYS_GetMainTimerInterval()) != 0)
		if (timerCount > 100000) timerCount = 0;
		
		citus_api.UI_OnTimer();
		
		if (timerCount % (int)(1000.f/(float)citus_api.SYS_GetMainTimerInterval()) != 0)
		{
			citus_api.MM_GetResult(mm_result);
			mIsNowTimerProc = false;
			return;
		}
//		else if (mm_result.x == 0)
	//		citus_api.MM_GetResult(mm_result);
		
		
		if (citus_api.RG_IsEndTouchDown()) // it shoud be just 1 time true, and next reset 
		{
			//Toast.makeText(this, "End Touch down", 5).show();
		}
		
		mIsCarCenter = citus_api.SYS_IsCarToCenter(); 
		if (mIsCarCenter) {
			
			int underpass = citus_api.GUIDE_Underpass_IsOkay();
			if (underpass > 0)
			{
				int ret = citus_api.GUIDE_Underpass_GetNextImage(100, mViewUnderPassImg, mViewUnderPassImg_Width, mViewUnderPassImg_Height);
				if (ret == 0 && mViewUnderPassImg_Width.value != 0 && mViewUnderPassImg_Height.value != 0)
				{
					mViewUnderPassImg = Bitmap.createBitmap(mViewUnderPassImg_Width.value, mViewUnderPassImg_Height.value, Bitmap.Config.ARGB_8888); // init with dummy value
					ret = citus_api.GUIDE_Underpass_GetNextImage(100, mViewUnderPassImg, mViewUnderPassImg_Width, mViewUnderPassImg_Height);
				}
				if (ret == 1)
				{
					Drawable drawableEntry = new BitmapDrawable(getResources(), mViewUnderPassImg);
					mViewUnderPass.setBackgroundDrawable(drawableEntry);
					setSpeedDigit();
					//mViewUnderPass.setVisibility(View.VISIBLE);
				}
				else if (ret == 2) // keep use
				{
					//mViewUnderPass.setVisibility(View.VISIBLE);
				}
				
				
//				if (UnderPassId != underpass)
//				{
//					
//					
//					UnderPassId = underpass;
//					mViewUnderPass.setImageResource(R.drawable.underpass_01);
//					mViewUnderPass.setVisibility(View.VISIBLE);
//				}
			}
			else
			{
				//UnderPassId = 0;
				mViewUnderPass.setVisibility(View.GONE);
			}
			
			if(!bHideUi){
				mGroupBottom.setVisibility(View.VISIBLE);
			}
			mLayerMove.setVisibility(View.GONE);
			
			// Show/Hide Speed Camera View
			int cam_dist = citus_api.UI_GetDisplayCPF_Dist();
			int cam_type = citus_api.UI_GetDisplayCPF_Type();
			int cam_limit = citus_api.UI_GetDisplayCPF_Limit();
			
			if (cam_dist > 0 && citus_api.SYS_GetExtInfo(citus_api.SETTING_EXT_CAMERA, citus_api.SETTING_TYPE_DISPLAY))
			{
				mLayCam.setVisibility(View.VISIBLE);
				revealCamLayout();
				
				if (cam_dist >= 1000) {
					mCamDist.setText(String.format("%.1fkm", (float) cam_dist / 1000.f));
					setCamDist(String.format("%.1fkm", (float) cam_dist / 1000.f)+" m");
				}
				else {
					mCamDist.setText("" + cam_dist);
					setCamDist("" + cam_dist+" m");
				}
				
				mCamSpeed.setText(""+cam_limit);
				setCamSpeed(" "+cam_limit+ " /km/h");
				
				switch (cam_type) {
				case citus_api.CPF_TYPE_MOVING: {
					// imgCameraType.image = [UIImage
					// imageNamed:@"camera_type_cop.png"];
					mCamImage.setImageResource(R.drawable.camera_type_cop);
					setCamImage(R.drawable.camera_type_cop);
				}
					break;
				case citus_api.CPF_TYPE_FIXED: {
					// imgCameraType.image = [UIImage
					// imageNamed:@"camera_type_fixed.png"];
					mCamImage.setImageResource(R.drawable.camera_type_fixed);
					setCamImage(R.drawable.camera_type_fixed);
				}
					break;
				case citus_api.CPF_TYPE_SIGN: {
					// imgCameraType.image = [UIImage
					// imageNamed:@"camera_type_crossline.png"];
					mCamImage.setImageResource(R.drawable.camera_type_crossline);
					setCamImage(R.drawable.camera_type_crossline);
					break;
				}
				case citus_api.KWT_CPF_TYPE_DRV_DIST: {
					// imgCameraType.image = [UIImage
					// imageNamed:@"camera_type_tooclose.png"];
					mCamImage.setImageResource(R.drawable.camera_type_tooclose);
					setCamImage(R.drawable.camera_type_tooclose);
					break;
				}

				default: {
					// imgCameraType.image = [UIImage
					// imageNamed:@"camera_type_cop.png"];
					mCamImage.setImageResource(R.drawable.camera_type_cop);
					setCamImage(R.drawable.camera_type_cop);
				}
					break;
				}
				
				int changeCount = 0;
				if (timerCount != 0)
					changeCount = timerCount % 4;
				
				if (changeCount < 2 && cam_limit != 0)
				{
					mCamImage.setVisibility(View.GONE);
					mCamSpeed.setVisibility(View.VISIBLE);
					removeSpeedImage();
				}
				else
				{
					mCamImage.setVisibility(View.VISIBLE);
					mCamSpeed.setVisibility(View.GONE);
					revealSpeedImage();
				}
				
				
			}
			else
			{
				mLayCam.setVisibility(View.GONE);
				removeCamLayout();
			}
			
			// show/hide demo control group
			mGuideMode = ApiProxy.getInteger(ApiProxy.SYS_GUIDE_MODE, 0); 	
			
			if (mGuideMode == GUIDE_MODE_DEMO) {
				if (mDemoControlTranslated) {
					mGroupDemoControl.setVisibility(View.GONE);
					mButtonDemoControlShow.setVisibility(View.VISIBLE);
				} else {
					mGroupDemoControl.setVisibility(View.VISIBLE);
					mButtonDemoControlShow.setVisibility(View.GONE);
				}
			} else
				mGroupDemoControl.setVisibility(View.GONE);
			
			
			// make route list and highway list
			if (storage_RouteList == null) storage_RouteList = new ArrayList<ROUTE_ITEM>();
			if (storage_HiwayList == null) storage_HiwayList = new ArrayList<ROUTE_ITEM>();
			
			int current_route_seq = citus_api.RG_GetSequenceNo();
			if (route_sequence_no != current_route_seq)
			{
				route_sequence_no = current_route_seq;
				storage_RouteList.clear();
				storage_HiwayList.clear();
				
				
				if (current_route_seq>0 && citus_api.RG_IsAble()) // route exist
				{
					
//					TMC_ROUTE_INFO tmc = new TMC_ROUTE_INFO();
//					if (citus_api.TMC_FindInRouteByRoadId(44, 121.524632, 25.025316, tmc))
//					{
//						
//					}
					
					// update new route list
					// top guide
					
					RG_GUIDE_INFO info = new RG_GUIDE_INFO();
					RG_GUIDE_INFO next = new RG_GUIDE_INFO();
					int part_num = citus_api.RG_GetPartNum(-1);
					for(int iii=part_num-1; iii>=0; iii--)
					{
						if (!citus_api.RG_GetGuideInfo(iii, info, true, true)) continue;
						
						if (info.targIdx == iii)
						{
							if (info.targDist < 0.) continue;
							int l_r_side = 0; // -1:left, 0:go_12, 1:right
							boolean l_r_turn = false;
							switch(info.snd_dir_code)
							{
							case citus_api.SND_DIR_RIGHT_1:
							case citus_api.SND_DIR_RIGHT_2:
							case citus_api.SND_DIR_RIGHT:
							case citus_api.SND_DIR_R_SIDE:
								l_r_side = 1; break;
								
							case citus_api.SND_DIR_RIGHT_4:
							case citus_api.SND_DIR_RIGHT_3:
							case citus_api.SND_DIR_RIGHT_5:
							case citus_api.SND_DIR_RTURN:
								l_r_turn = true;
								l_r_side = 1; break;
							case citus_api.SND_DIR_LEFT_11:
							case citus_api.SND_DIR_LEFT_10:
							case citus_api.SND_DIR_LEFT:
							case citus_api.SND_DIR_L_SIDE:
								l_r_side = -1; break;
							case citus_api.SND_DIR_LEFT_8:
							case citus_api.SND_DIR_LEFT_9:
							case citus_api.SND_DIR_LEFT_7:
							case citus_api.SND_DIR_LTURN:
								l_r_turn = true;
								l_r_side = -1; break;
							}				
							int imageId = 0;
							if (info.snd_info_code > 0) 
							{
								switch (info.snd_info_code) 
								{
								case citus_api.SND_INFO_TO_OVERPASS:		imageId = (l_r_side == 1 ? R.drawable.arrow_overpass_02:R.drawable.arrow_overpass_01);	break;
//								case citus_api.SND_INFO_NOT_TO_OVERPASS:	imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_INFO_TO_UNDERPASS:		
					            	switch(l_r_side)
					            	{
					            	case -1: imageId = R.drawable.arrow_underpass_01; break;
					            	case  1: imageId = R.drawable.arrow_underpass_02; break;
					            	default: imageId = R.drawable.arrow_underpass_00; break;
					            	}
					            	break;
//					            case citus_api.SND_INFO_NOT_TO_UNDERPASS:	imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_INFO_ENTER_HIWAY:
					            case citus_api.SND_INFO_ENTER_EXPRESS:
					            	switch(l_r_side)
					            	{
					            	case -1: imageId = l_r_turn ? R.drawable.arrow_enter_hi_05:R.drawable.arrow_enter_hi_02; break;
					            	case  0: imageId = R.drawable.arrow_enter_hi_00; break;
					            	default: imageId = l_r_turn ? R.drawable.arrow_enter_hi_04:R.drawable.arrow_enter_hi_01; break;
					            	}
					            	//imageId = R.drawable.turnpanel_sign_04; break;
					            	break;
					            case citus_api.SND_INFO_ENTER_IC:
					            	switch(l_r_side)
					            	{
					            	case -1: imageId = l_r_turn ? R.drawable.arrow_enter_hi_05:R.drawable.arrow_enter_hi_02; break;
					            	case  0: imageId = R.drawable.arrow_enter_hi_00; break;
					            	default: imageId = l_r_turn ? R.drawable.arrow_enter_hi_04:R.drawable.arrow_enter_hi_01; break;
					            	}
					            	//imageId = R.drawable.turnpanel_sign_04; break;
					            	break;
					            case citus_api.SND_INFO_ENTER_RAMP:
					            	switch(l_r_side)
					            	{
					            	case -1: imageId = l_r_turn ? R.drawable.arrow_enter_hi_05:R.drawable.arrow_enter_hi_02; break;
					            	case  0: imageId = R.drawable.arrow_enter_hi_00; break;
					            	default: imageId = l_r_turn ? R.drawable.arrow_enter_hi_04:R.drawable.arrow_enter_hi_01; break;
					            	}
					            	//imageId = R.drawable.turnpanel_sign_04; break;
					            	break;
					            case citus_api.SND_INFO_EXIT_HIWAY:
					            case citus_api.SND_INFO_EXIT_EXPRESS:
					            	switch(l_r_side)
					            	{
					            	case -1: imageId = l_r_turn ? R.drawable.arrow_exit_hi_05:R.drawable.arrow_exit_hi_02; break;
					            	case  0: imageId = R.drawable.arrow_exit_hi_00; break;
					            	default: imageId = l_r_turn ? R.drawable.arrow_exit_hi_04:R.drawable.arrow_exit_hi_01; break;
					            	}
					            	break;//imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_INFO_MID_OK:				imageId = R.drawable.turnpanel_sign_waypoint; break;
					            case citus_api.SND_INFO_DEST_OK:			imageId = R.drawable.turnpanel_sign_goal; break;
					            case citus_api.SND_INFO_TO_TUNNEL:			imageId = R.drawable.arrow_tunnel; break;
//					            case citus_api.SND_INFO_NOT_TO_TUNNEL:		imageId = R.drawable.turnpanel_sign_03; break;
					            case citus_api.SND_INFO_HIGHWAY_DIR_JC:
					            case citus_api.SND_INFO_HIGHWAY_DIR_IC: 	imageId = R.drawable.turnpanel_sign_04; break; // IC���占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 ��곤옙揶�占�
					            case citus_api.SND_INFO_LANE_FAST:			imageId = (l_r_side == 1 ? R.drawable.turnpanel_sign_10 : R.drawable.turnpanel_sign_11); break;
					            case citus_api.SND_INFO_LANE_SLOW:         	imageId = (l_r_side == -1 ? R.drawable.turnpanel_sign_11 : R.drawable.turnpanel_sign_10); break; // right
					            case citus_api.SND_INFO_ENTER_BRIDGE:		imageId = R.drawable.arrow_overpass_01; break;
					            case citus_api.SND_INFO_ENTER_ROTARI:
					            	switch(info.snd_dir_code)
					            	{
					            	case citus_api.SND_DIR_ROUNDABOUT_1: imageId = R.drawable.arrow_circle_02; break;
					            	case citus_api.SND_DIR_ROUNDABOUT_3: imageId = R.drawable.arrow_circle_03; break;
					            	case citus_api.SND_DIR_ROUNDABOUT_5: imageId = R.drawable.arrow_circle_04; break;
					            	case citus_api.SND_DIR_ROUNDABOUT_6: imageId = R.drawable.arrow_circle_05; break;
					            	case citus_api.SND_DIR_ROUNDABOUT_8: imageId = R.drawable.arrow_circle_06; break;
					            	case citus_api.SND_DIR_ROUNDABOUT_9: imageId = R.drawable.arrow_circle_07; break;
					            	case citus_api.SND_DIR_ROUNDABOUT_11: imageId = R.drawable.arrow_circle_08; break;
					            	default: imageId = R.drawable.arrow_circle_01; break;			            	
					            	}
					            	break;
					            default:		                			imageId = R.drawable.turnpanel_sign_03; break;
								}
							} 
							if (imageId == 0 && info.snd_dir_code > 0) {
								switch (info.snd_dir_code) {
								case citus_api.SND_DIR_GO_12:
					            case citus_api.SND_DIR_STRAIGHT:	imageId = R.drawable.turnpanel_sign_03; break;
					            case citus_api.SND_DIR_LEFT_7:		imageId = R.drawable.turnpanel_sign_01; break;
					            case citus_api.SND_DIR_LEFT_8:		imageId = R.drawable.turnpanel_sign_01; break;
					            case citus_api.SND_DIR_LEFT_9:		imageId = R.drawable.turnpanel_sign_01; break;
					            case citus_api.SND_DIR_LTURN:		imageId = R.drawable.turnpanel_sign_01; break;
					            case citus_api.SND_DIR_RIGHT_4:		imageId = R.drawable.turnpanel_sign_05; break;
					            case citus_api.SND_DIR_RIGHT_3:		imageId = R.drawable.turnpanel_sign_05; break;
					            case citus_api.SND_DIR_RIGHT_5:		imageId = R.drawable.turnpanel_sign_05; break;
					            case citus_api.SND_DIR_RTURN:		imageId = R.drawable.turnpanel_sign_05; break;
					            case citus_api.SND_DIR_LEFT_11:		imageId = R.drawable.turnpanel_sign_02; break;
					            case citus_api.SND_DIR_LEFT_10:		imageId = R.drawable.turnpanel_sign_02; break;
					            case citus_api.SND_DIR_LEFT:		imageId = R.drawable.turnpanel_sign_02; break;
					            case citus_api.SND_DIR_RIGHT_1:		imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_DIR_RIGHT_2:		imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_DIR_RIGHT:		imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_DIR_UTURN:		imageId = R.drawable.turnpanel_sign_06; break;
					            case citus_api.SND_DIR_PTURN:		imageId = R.drawable.turnpanel_sign_04; break;
					            case citus_api.SND_DIR_ACT_AFTER_RTURN:
					            case citus_api.SND_DIR_RACT_AFTER_RTURN:
					            case citus_api.SND_DIR_LACT_AFTER_RTURN:	imageId = R.drawable.turnpanel_sign_05; break;
					            case citus_api.SND_DIR_ACT_AFTER_LTURN:
					            case citus_api.SND_DIR_RACT_AFTER_LTURN:
					            case citus_api.SND_DIR_LACT_AFTER_LTURN:	imageId = R.drawable.turnpanel_sign_01; break;
					            case citus_api.SND_DIR_L_SIDE:				imageId = R.drawable.turnpanel_sign_02; break;
					            case citus_api.SND_DIR_R_SIDE:				imageId = R.drawable.turnpanel_sign_04; break;
					            default:		                			imageId = R.drawable.turnpanel_sign_03; break;
								}
							} else if (imageId == 0){
								imageId = R.drawable.turnpanel_sign_03;
							}
							// set guide image
//							ImageView turnImage = (ImageView)findViewById(R.id.imageViewGuideTurn);
//							turnImage.setBackgroundResource(imageId);
							//HashMap<String, String> item = new HashMap<String, String>();
							ROUTE_ITEM item = new ROUTE_ITEM();
							item.part_idx = info.targIdx;
							//item.put("imageid", Integer.toString(imageId));
							item.image_id = imageId;
							// turn distance
							int nearCrossDist = ApiProxy.getInteger(ApiProxy.RG_TOTAL_DIST, 0) 
									- (int)citus_api.RG_GetPartLength(info.targIdx) ;
									//- ApiProxy.getInteger(ApiProxy.RG_PAST_DIST, 0);
//							if (nearCrossDist > 999)
//								turnDistView.setText(String.valueOf((double)nearCrossDist/1000.0) + "Km");
//							else
//								turnDistView.setText(String.valueOf(nearCrossDist) + "m");
							
							item.dist_from_start = nearCrossDist;
							//item.put("dist", Integer.toString(nearCrossDist));
							
							// road name
							//TextView turnRnameView = (TextView)findViewById(R.id.textViewGuideCurrentRoad);
							//turnRnameView.setText(String.valueOf(info.rname));
							next.rname = null;
							if (info.targIdx >= 0)
							{
								citus_api.RG_GetGuideInfo(info.targIdx-1, next, false, true);
							}
							
							if ((info.snd_info_code == citus_api.SND_INFO_DEST_OK) && (next.rname == null || next.rname.length() == 0))
								next.rname = "目的地";
							if ((info.snd_info_code == citus_api.SND_INFO_MID_OK) && (next.rname == null || next.rname.length() == 0))
								next.rname = "經由地";

							if (next.rname == null || next.rname.length() == 0)
								next.rname = "一般道路";
							
							item.next_road_name = next.rname;
							item.road_id = info.kwt_RoadId;
//							item.put("rname", String.valueOf(next.rname));
//							item.put("roadId", Integer.toString(info.kwt_RoadId));
//							item.put("rname", String.valueOf(info.rname));
							storage_RouteList.add(item);
						}						
						else if (info.cpDirName != null && info.kwt_kind <= 1) // highway 정보 추가
						{
							if (info.cpDirName.length()>0)
							{
								ROUTE_ITEM item = new ROUTE_ITEM();
								item.part_idx = iii;
								item.image_id = R.drawable.turnpanel_sign_03;
								
								item.next_road_name = info.cpDirName;//info.rname;			
								item.xpos = info.nodeX;
								item.ypos = info.nodeY;
								int nearCrossDist = ApiProxy.getInteger(ApiProxy.RG_TOTAL_DIST, 0) 
										- (int)citus_api.RG_GetPartLength(iii) ;
								item.dist_from_start = nearCrossDist;
								storage_RouteList.add(item);
							}
						}
					}
					
					// update highway info list
					/*
					RG_HIWAY_INFO hiInfo = new RG_HIWAY_INFO();
					
					int hi_num = citus_api.RG_GetHiwayInfoNum();
					for(int iii=0; iii<hi_num; iii++)
					{
						hiInfo.init();
						citus_api.RG_GetHiwayInfo(iii, hiInfo);
							
						ROUTE_ITEM item = new ROUTE_ITEM();
						item.dist_from_start = hiInfo.distFromStart + rg_past_dist;
						item.next_road_name = hiInfo.szText;
						item.part_idx = hiInfo.partIdx;
						item.idx = iii;
						if (citus_api.RG_GetGuideInfo(hiInfo.partIdx, info, false, true))
						{
							item.road_id = info.kwt_RoadId;
						}
			
						if (hiInfo.kind == 0)	// ic
							item.image_id = R.drawable.map_highway_ic_01;
						else if (hiInfo.kind == 1)	// jc
							item.image_id = R.drawable.map_highway_jc_01;					
						else if (hiInfo.kind == 2)	// sa
							item.image_id = R.drawable.map_highway_rest_01;
						else						// tg
							item.image_id = R.drawable.map_highway_tg_01;
						storage_HiwayList.add(item);					
					} // end of hiway list					
					*/
				} // end of route exist		
			} // end of route changed
			
			
			
			ROUTE_ITEM top_guide_info = null;
			
			if (mTBTDataArray == null)	mTBTDataArray = new ArrayList<ROUTE_ITEM>();
			mTBTDataArray.clear();
			
			// top guide
			if (storage_RouteList.size()>0)
			{
				int find_in_stroage = -1;

				int ipart = citus_api.RG_GetCurrentPart();
				RG_GUIDE_INFO info = new RG_GUIDE_INFO();
				if (ipart>=0 && citus_api.RG_GetGuideInfo(ipart, info, false, true)) 
				{
					// storage는 part 0 부서 n으로 싸였있음.
					int s = 0, e = storage_RouteList.size()-1, m;
					while(s<=e)
					{
						m = (s+e)/2;
						ROUTE_ITEM item = storage_RouteList.get(m);
						if (item.part_idx == info.targIdx)
						{
							top_guide_info = item;
							find_in_stroage = m;
							// 고속도로에 올라가면 고속도로 모드가 나오므로, 아래 찾는 기능은 필요 없음.
							for(m--; m>=s; m--)
							{
								item = storage_RouteList.get(m);
								if (item.part_idx >= ipart)
									break;
								find_in_stroage = m;
							}
							break;
						}
						else if (item.part_idx > info.targIdx) s=m+1;
						else e=m-1;
					}
				
					if (find_in_stroage>=0)
					{
						for(int ii=find_in_stroage; ii<storage_RouteList.size(); ii++)
							mTBTDataArray.add(storage_RouteList.get(ii));
					}
				}				
			} // exist route list
			
			
			
			int nearCrossDist = 0;
			if (top_guide_info != null)//mTBTDataArray.size() > 0)
			{
				ROUTE_ITEM item = top_guide_info;//mTBTDataArray.get(0);
				ImageView turnImage = (ImageView)findViewById(R.id.imageViewGuideTurn);
				turnImage.setBackgroundResource(item.image_id);
				TextView turnDistView = (TextView)findViewById(R.id.textViewTurnDistance);
				nearCrossDist = item.dist_from_start - rg_past_dist;
				if (nearCrossDist > 999)
					turnDistView.setText(String.valueOf((double)nearCrossDist/1000.0) + "Km");
				else
					turnDistView.setText(String.valueOf(nearCrossDist) + "m");
				TextView turnRnameView = (TextView)findViewById(R.id.textViewGuideCurrentRoad);
				turnRnameView.setText(item.next_road_name);					
			}
			
			// External view mode
			mExtMode = ApiProxy.getInteger(ApiProxy.SYS_EXT_MODE, 0);
			
			if (mExtMode == EXT_MODE_ZOOM_CROSS && citus_api.RG_IsAble()) {
				mViewEntryInfo.setVisibility(View.GONE);
				
				if(!bHideUi){
					mViewExtInfo.setVisibility(View.VISIBLE);
					mGroupTop.setVisibility(View.VISIBLE);
				}
				citus_api.BitmapCopy(mBitmapExtView);
				mProgressNearCross.setVisibility(View.GONE);
				if(!bHideUi){
					mProgressNearCross2.setVisibility(View.VISIBLE);
				}
				mProgressNearCross2.setProgress(500-nearCrossDist);
				mTBTView.setVisibility(View.GONE);
			} else if (mExtMode == EXT_MODE_ENTRY && citus_api.RG_IsAble()){
				
				mViewExtInfo.setVisibility(View.GONE);
				if(!bHideUi){
					mViewEntryInfo.setVisibility(View.VISIBLE);
					mGroupTop.setVisibility(View.VISIBLE);
				}
//				citus_api.BitmapCopy(mBitmapExtView);
				WrapInt wrapedWidth = new WrapInt();
				WrapInt wrapedHeight = new WrapInt();
				if (!citus_api.BitmapCopy2(mBitmapEntryView, wrapedWidth, wrapedHeight))
				{
					if (wrapedWidth.value != 0 && wrapedHeight.value != 0)
					{
						mBitmapEntryView = Bitmap.createBitmap(wrapedWidth.value, wrapedHeight.value, Bitmap.Config.ARGB_8888); // init with dummy value
						citus_api.BitmapCopy2(mBitmapEntryView, wrapedWidth, wrapedHeight);
						
						Drawable drawableEntry = new BitmapDrawable(getResources(), mBitmapEntryView);
						mViewEntryInfo.setBackgroundDrawable(drawableEntry);
					}
				}
				if(!bHideUi){
					mProgressNearCross.setVisibility(View.VISIBLE);
				}
				mProgressNearCross.setProgress(500-citus_api.GUIDE_GetEntryRemainDist());
				mProgressNearCross2.setVisibility(View.GONE);
				mTBTView.setVisibility(View.GONE);
			} else {
				mViewEntryInfo.setVisibility(View.GONE);
				mViewExtInfo.setVisibility(View.GONE);
				mGroupTop.setVisibility(View.GONE);
				mProgressNearCross.setVisibility(View.GONE);
				mProgressNearCross2.setVisibility(View.GONE);
				
				if (citus_api.RG_IsAble())
				{
					if(!bHideUi){
						mGroupTop.setVisibility(View.VISIBLE);
						mTBTView.setVisibility(View.VISIBLE);
					}
					makeTBTList();
				}
				else
					mTBTView.setVisibility(View.GONE);
			}
			
			if (!mTbtToggle)
				mTBTView.setVisibility(View.GONE);
			
//			if (citus_api.RG_IsAble())
//			{
//				if (mTbtToggle) {
//					if (mIsPortrait)
//						citus_api.MV3D_SetScreenCenter(0.5f, -0.7f);
//					else
//						citus_api.MV3D_SetScreenCenter(-0.2f, -0.7f);
//				} else {
//					citus_api.MV3D_SetScreenCenter(0.f, -0.7f);
//				}
//			}
			if (mTBTView.getVisibility() == View.VISIBLE ||
					mViewEntryInfo.getVisibility() == View.VISIBLE ||
					mViewExtInfo.getVisibility() == View.VISIBLE)
			{
				if (citus_api.MV_GetNorthUP()) {
					if (mIsPortrait)
						citus_api.MV3D_SetScreenCenter(0.5f, -0.2f);
					else
						citus_api.MV3D_SetScreenCenter(-0.2f, -0.2f);
				} else {
					if (mIsPortrait)
						citus_api.MV3D_SetScreenCenter(0.5f, -0.7f);
					else
						citus_api.MV3D_SetScreenCenter(-0.2f, -0.7f);
				}
			}
			else
			{
				if (citus_api.MV_GetNorthUP())
					citus_api.MV3D_SetScreenCenter(0.f, 0.f);
				else
					citus_api.MV3D_SetScreenCenter(0.f, -0.7f);
			}

			// set admin name
			IPOINT pnt = citus_api.SYS_GetCarPos();
			WrapString adminName = new WrapString();
			citus_api.ADMIN_GetName(pnt.x, pnt.y, adminName);
			TextView adminView = (TextView)findViewById(R.id.textViewCurrentLocation);
			adminView.setText(adminName.value);
			
			WrapString hn_str = new WrapString();
			WrapString hn_str2 = new WrapString();
			int hsn = citus_api.SYS_GetHouseNumber_CurrMMPos(hn_str,hn_str2);
			float mileage = citus_api.SYS_SearchRoadMileageNumber_CurrentPos(); 
			// set road name
			String roadName = ApiProxy.getString(ApiProxy.UI_CURRENT_ROAD_NAME, "");
			TextView currentRoadView = (TextView)findViewById(R.id.textViewCurrentRoad);
			
			if (hsn > 0)
				currentRoadView.setText(roadName+hn_str.value);
			else if (mileage == -1)
				currentRoadView.setText(roadName);
			else
				currentRoadView.setText(roadName+"["+String.format("%.1f", mileage) +"]");

			setSpeedDigit();
			
			// set SignPost
			mSignPostView[0].setVisibility(View.GONE);
			mSignPostView[1].setVisibility(View.GONE);
			mSignPostView[2].setVisibility(View.GONE);
			mSignPostView[3].setVisibility(View.GONE);
			if (citus_api.SYS_GetUsingRoadSign())
			{
				int iSignPostCnt = citus_api.GUIDE_GetCurrSignpostInfoCount(300);
				SIGNPOST_GUIDEINFO gi = new SIGNPOST_GUIDEINFO();
				for (int iii=0; iii<iSignPostCnt && iii<4; iii++)
				{
					citus_api.GUIDE_GetCurrSignpostInfoAt(iii,  gi);
					mSignPostView[iii].setVisibility(View.VISIBLE);
					if (gi.exit_num != null && gi.exit_num.length() != 0)
					{
						mSignPostView[iii].setText("["+gi.exit_num+"]"+String.valueOf(gi.info));
					}
					else
					{
						mSignPostView[iii].setText(String.valueOf(gi.info));
					}
					
					if (gi.is_active)
					{
						mSignPostView[iii].setTextColor(Color.WHITE);
					}
					else
					{
						mSignPostView[iii].setTextColor(Color.GRAY);
					}
				}
			}
			
			// for ipc
			int ipc_count = 0;
			// check current road link has near road information 
			if (citus_api.IPC_HasNearRoad())
			{
				// get current near road information
				// if your press roadname in UI then find near road information.
				// and near road information has live time and live distance.
				// it takes some time and it goes some distance then automatically clear the near road information 
				ipc_count = citus_api.IPC_GetCandidateCount();
				currentRoadView.setTextColor(Color.WHITE);				
			}
			else
			{
				currentRoadView.setTextColor(Color.CYAN);
				citus_api.IPC_Clear();
			}

			// it is time-out then close selector.
			if (ipc_count ==0 && m_ipc_selector != null && m_ipc_selector.isShowing())
			{
				m_ipc_selector.dismiss();
				m_ipc_selector = null;
			}
			
		}
		else
		{
			// Map Moved
			bHideUi = false ;
			mGroupBottom.setVisibility(View.GONE);
			mGroupTop.setVisibility(View.GONE);
			mGroupDemoControl.setVisibility(View.GONE);
			mViewEntryInfo.setVisibility(View.GONE);
			mViewExtInfo.setVisibility(View.GONE);
			mTBTView.setVisibility(View.GONE);
			mLayCam.setVisibility(View.GONE);
			
			mLayerMove.setVisibility(View.VISIBLE);
			mProgressNearCross.setVisibility(View.GONE);
			mProgressNearCross2.setVisibility(View.GONE);
			
			if (citus_api.MV_GetNorthUP()) {
				mButtonHeadup.setVisibility(View.GONE);
				mButtonNorthup.setVisibility(View.VISIBLE);
			} else {
				mButtonHeadup.setVisibility(View.VISIBLE);
				mButtonNorthup.setVisibility(View.GONE);
			}
		}
		
		if (citus_api.RG_IsAble())
		{
			RG_REMAIN_INFO remain_info = new RG_REMAIN_INFO();
			citus_api.RG_GetRemainInfo(remain_info);

			String strVal, strLabel;
			switch (citus_api.SYS_GetArriveInfoType())
			{
			case 1: // arrival time
				strLabel = "Arrival time";
				strVal = String.format("ETA %02d:%02d", 
						remain_info.etaTime/10000, (remain_info.etaTime/100)%100);
				break;
			case 2: // remain time
				strLabel = "Remain time";
				strVal = String.format("%02d:%02d",
						remain_info.time/60, remain_info.time%60);
				break;
			case 0: // distance
			default:
				strLabel = "Remain dist";
				if (remain_info.dist > 1000)
					strVal = String.format("%.1fkm", remain_info.dist/1000.f);
				else
					strVal = String.format("%dm", remain_info.dist);
				break;
			}
			TextView remainView = (TextView)findViewById(R.id.textViewRemainDist);
			TextView labelView = (TextView)findViewById(R.id.textViewCarIdx);
			labelView.setText(strLabel);
			remainView.setText(strVal);
			
			// demo progressbar
			if (mGuideMode == GUIDE_MODE_DEMO) {
				int total = ApiProxy.getInteger(ApiProxy.RG_TOTAL_DIST, 0);
				int past = ApiProxy.getInteger(ApiProxy.RG_PAST_DIST, 0);
				mProgressDemo.setMax(total);
				mProgressDemo.setProgress(past);
			}
		}
		else
		{
			mViewEntryInfo.setVisibility(View.GONE);
			mViewExtInfo.setVisibility(View.GONE);
			mGroupTop.setVisibility(View.GONE);
			mProgressNearCross.setVisibility(View.GONE);
			mProgressNearCross2.setVisibility(View.GONE);
			mTBTView.setVisibility(View.GONE);

			TextView remainView = (TextView)findViewById(R.id.textViewRemainDist);
			remainView.setText("0m");
		}
		rotateCompass();
		
		mScaleView.invalidate();
		mIsNowTimerProc = false;
	}
	
	private void makeTBTList()
	{
		int rg_past_dist = citus_api.RG_GetPastDist();
		mTBTGraph.mTBTDataArray = mTBTDataArray;
//		mTBTContents.removeAllViews();
		boolean isHiwayMode = false;
		/*
		if (citus_api.RG_IsHiway() && citus_api.RG_IsAble() && citus_api.SYS_IsHighwaySplit() && citus_api.RG_GetCurrentHiwayInfoIdx() != -1)
		{
			RG_GUIDE_INFO rg_guide_info = new RG_GUIDE_INFO();
			int rg_nearCrossDist = -999;
			if (citus_api.RG_IsAble())
			{
				int ipart = citus_api.RG_GetCurrentPart();
				
				citus_api.RG_GetGuideInfo(ipart, rg_guide_info, false, true);
				

				rg_nearCrossDist = ApiProxy.getInteger(ApiProxy.RG_TOTAL_DIST, 0) 
						- (int)citus_api.RG_GetPartLength(rg_guide_info.targIdx) 
						- ApiProxy.getInteger(ApiProxy.RG_PAST_DIST, 0);

			}
			
			ROUTE_ITEM topGuide = null;//
			ArrayList<ROUTE_ITEM> highwatTBTList = new ArrayList<ROUTE_ITEM>();
			if (mTBTDataArray.size() > 0)
				topGuide = mTBTDataArray.get(0);
//			mTBTDataArray.clear();
			if (topGuide != null)
				highwatTBTList.add(topGuide);
			
		
			int hIdx = citus_api.RG_GetCurrentHiwayInfoIdx();
			int prv_group_count = -1;
			while (hIdx >= 0)
			{
				ROUTE_ITEM hiInfo = storage_HiwayList.get(hIdx);
				
				
				hiInfo.dist_from_start = citus_api.RG_GetHiwayInfoDistanceFromCar(hiInfo.idx) + rg_past_dist;
				if (prv_group_count == -1)					
					prv_group_count = hiInfo.group_count;
				
				if (prv_group_count != hiInfo.group_count) // 고속도로가 다른 그룹이면 여기서 끊는다.
				{
					break;
				}
				
				if (rg_nearCrossDist != -999)
				{
					if (rg_nearCrossDist < (hiInfo.dist_from_start-rg_past_dist) && highwatTBTList.size() == 1 && rg_guide_info.snd_info_code == citus_api.SND_INFO_MID_OK)	// 고속도로 중간에 경유지가 있고 바로 다음 안내가 경유지 일때 끊는다.
						break;																																// 이유는 그룹0과 그룹1 사이에 경유지가 있고, 그룹 0이 지나면서 그룹 1이 리스트로 생성되기 때문에 여기서 걸러낸다.
					
				}
				
				--hIdx;
				
				highwatTBTList.add(hiInfo);
			}

			if (citus_api.RG_IsAble()&& rg_nearCrossDist != -999 && highwatTBTList.size() > 0)
			{				
				ROUTE_ITEM item = highwatTBTList.get(highwatTBTList.size()-1);
				
				if (highwatTBTList.size() < 3) // jolee - 마지막 1개가 나올 경우에만...
				{
					int nLastPartIdx = item.part_idx;
					
					if (rg_guide_info.targIdx == nLastPartIdx)
					{
						highwatTBTList.remove(highwatTBTList.size()-1);	// 마지막 아이템이 현재 안내 되고 있으면 리스트에서 삭제한다.
					}
				}
			}
			
			

			//if (mTBTDataArray.size()==0) return;
			
			if ((topGuide != null &&highwatTBTList.size() > 1) || (topGuide == null &&highwatTBTList.size() > 0))
			{
				mTBTDataArray.clear();
				mTBTDataArray.addAll(highwatTBTList);
				highwatTBTList.clear();
				isHiwayMode = true;
				
			}
			else
				isHiwayMode = false;
			
			
		}
		*/
		//else
		{
			if (mTBTDataArray.size() < 2){
				mTBTView.setVisibility(View.GONE);
				return;
			}
		}
		
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for (int iii = 1; iii < mTBTDataArray.size(); iii++)
        {
			LinearLayout tbtCell = null;
			if ((iii-1) >= mTBTContents.getChildCount())
			{
				tbtCell = (LinearLayout)inflater.inflate(R.layout.tbt_cell, null);
	        	mTBTContents.addView(tbtCell);
	        	
	        	if (iii % 2 == 0)
	        		tbtCell.setBackgroundColor(0xFF108080);
	        	else
	        		tbtCell.setBackgroundColor(0xFF108010);

			}
			else 
			{
				tbtCell = (LinearLayout)mTBTContents.getChildAt(iii-1);
				tbtCell.setVisibility(View.VISIBLE);
			}
     
        	TextView tbtText1 = (TextView)tbtCell.findViewById(R.id.tbtText1);
        	TextView tbtText2 = (TextView)tbtCell.findViewById(R.id.tbtText2);
        	ImageView tbtImageView = (ImageView)tbtCell.findViewById(R.id.tbtImage);
        	
        	
        	ROUTE_ITEM item = mTBTDataArray.get(iii);
        	
			int nearCrossDist = item.dist_from_start - rg_past_dist;
			if (nearCrossDist > 999)
				tbtText1.setText(String.valueOf((double)nearCrossDist/1000.0) + "Km");
			else
				tbtText1.setText(String.valueOf(nearCrossDist) + "m");

        	tbtText2.setText(item.next_road_name);
        	tbtImageView.setImageResource(item.image_id);


        }
		
		if (mTBTContents.getChildCount() > (mTBTDataArray.size()-1))
		{
			for (int iii = (mTBTDataArray.size()-1); iii < mTBTContents.getChildCount(); iii++)
			{
				LinearLayout tbtCell = (LinearLayout)mTBTContents.getChildAt(iii);
				tbtCell.setVisibility(View.GONE);

			}
		}
		mTBTGraph.isHiwayMode = isHiwayMode;
		if (isHiwayMode)
		{
			mTBTGraph.invalidate();
		}
		
	}
	static int oldAngle = 0;
	private void rotateCompass() {
		
		int angle = (int)ApiProxy.getFloat(ApiProxy.SYS_CAR_ANGLE, 0.f);
		//angle *= -1;
		if (angle > 360)
			angle -= 360;
		if (angle < 0)
			angle += 360;
		
		ImageView view = mViewCompassMove;
		
		if (mIsCarCenter)
			view = mViewCompass;
		
		if (UI_Caution.isActive == 0 && mIsCarCenter && mGuideMode != GUIDE_MODE_DEMO) 
		{
			angle = 0;
			if (view.getTag() == null || ((Integer)view.getTag()).intValue() != R.drawable.icon_sat)
			{
				view.setImageResource(R.drawable.icon_sat);
				view.setTag(R.drawable.icon_sat);
			}
		}
		else
		{
			if (view.getTag() == null || ((Integer)view.getTag()).intValue() != R.drawable.map_vbtn_compass)
			{
				view.setImageResource(R.drawable.map_vbtn_compass);
				view.setTag(R.drawable.map_vbtn_compass);
			}
		}
		
		 ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		 int centerX = (view.getWidth()/2);
		 int centerY = (view.getHeight()/2);
		 RotateAnimation animation = new RotateAnimation(oldAngle, angle, centerX, centerY);
		 animation.setDuration(0);
		 animation.setRepeatCount(2);
		 animation.setFillAfter(true);
		 view.startAnimation(animation);

		 oldAngle = angle;
	}
	
	private void setSpeedDigit()
	{
		int cameraDistance = ApiProxy.getInteger(ApiProxy.CPF_CAMERA_DISTANCE, 0);
//		int cameraType = ApiProxy.getInteger(ApiProxy.CPF_CAMERA_TYPE, 0);
		int cameraLimit = ApiProxy.getInteger(ApiProxy.CPF_CAMERA_LIMIT, 0);
		int roadSpeed = citus_api.MM_GetCurrentRoadSpeed();
		
		int curSpeed = ApiProxy.getInteger(ApiProxy.SYS_CURRENT_SPEED, 0);
		if (curSpeed > 999 || curSpeed < 0)
			curSpeed = 0;
		boolean alert = false;//((curSpeed > /*cameraLimit*/roadSpeed) &&;
		if (citus_api.SYS_GetExtInfo(citus_api.SETTING_EXT_SPEED_LIMIT, citus_api.SETTING_TYPE_DISPLAY))
		{
			int limit_rule = citus_api.SYS_GetShowRoadLimitRuleRingType();
			if (limit_rule == 100) limit_rule = 0;
			
			if (roadSpeed > 0 && curSpeed > roadSpeed + limit_rule)
				alert = true;
		}
		else if (citus_api.SYS_GetExtInfo(citus_api.SETTING_EXT_CAMERA, citus_api.SETTING_TYPE_DISPLAY))
		{
			if (cameraLimit > 0 && cameraDistance > 0 && curSpeed > cameraLimit)
				alert = true;
		}
		
		
		int digit100 = curSpeed / 100;
		int digit10 = (curSpeed % 100) / 10;
		int digit1 = (curSpeed % 10);
		
		ImageView digitView100 = (ImageView)findViewById(R.id.imageViewDigit1);
		ImageView digitView10 = (ImageView)findViewById(R.id.imageViewDigit2);
		ImageView digitView1 = (ImageView)findViewById(R.id.imageViewDigit3);
		
		int idResource = getSpeedDigitResourceId(digit100, alert, digit100 == 0);
		digitView100.setBackgroundResource(idResource);
		setDigRes100(idResource);
		
		idResource = getSpeedDigitResourceId(digit10, alert, digit100 == 0 && digit10 == 0);
		digitView10.setBackgroundResource(idResource);
		setDigRes10(idResource);
		
		idResource = getSpeedDigitResourceId(digit1, alert, false);
		digitView1.setBackgroundResource(idResource);
		setDigRes1(idResource);


	}
	
	private int getSpeedDigitResourceId(int value, boolean alert, boolean none) // pot means power of ten
	{
		if (UI_Caution.isActive == 0 && mGuideMode != GUIDE_MODE_DEMO) return R.drawable.navi_speed_digit_none;
		
		if (none)
			return R.drawable.navi_speed_digit_none;
		if (alert) {
			switch (value)
			{
			case 1:
				return R.drawable.navi_speed_digit_alert_1;
			case 2:
				return R.drawable.navi_speed_digit_alert_2;
			case 3:
				return R.drawable.navi_speed_digit_alert_3;
			case 4:
				return R.drawable.navi_speed_digit_alert_4;
			case 5:
				return R.drawable.navi_speed_digit_alert_5;
			case 6:
				return R.drawable.navi_speed_digit_alert_6;
			case 7:
				return R.drawable.navi_speed_digit_alert_7;
			case 8:
				return R.drawable.navi_speed_digit_alert_8;
			case 9:
				return R.drawable.navi_speed_digit_alert_9;
			case 0:
				return R.drawable.navi_speed_digit_alert_0;
			}
		} else {
			switch (value)
			{
			case 1:
				return R.drawable.navi_speed_digit_1;
			case 2:
				return R.drawable.navi_speed_digit_2;
			case 3:
				return R.drawable.navi_speed_digit_3;
			case 4:
				return R.drawable.navi_speed_digit_4;
			case 5:
				return R.drawable.navi_speed_digit_5;
			case 6:
				return R.drawable.navi_speed_digit_6;
			case 7:
				return R.drawable.navi_speed_digit_7;
			case 8:
				return R.drawable.navi_speed_digit_8;
			case 9:
				return R.drawable.navi_speed_digit_9;
			case 0:
				return R.drawable.navi_speed_digit_0;
			}
		}
		return 0;
	}
	
	private void onMapPick(int sx, int sy) {
		if (mIsCarCenter)
			return;
//		mButtonPickGo.setVisibility(View.VISIBLE);
//		mTextViewPick.setVisibility(View.VISIBLE);
//		
//		String str = citus_api.MV3D_GetPoiOnPick(sx, sy);
//		if (str == null)
//			str = citus_api.SYS_GetRoadNameOnPick(sx, sy);
//		if (str == null)
//			str = citus_api.SYS_GetPositionOnPick(sx, sy);
//		mTextViewPick.setText(str);
		
		MAP_PICK_INFO info = new MAP_PICK_INFO();
		if (citus_api.MV_GetPickOnMap(sx, sy, 5, info))
		{
			mButtonPickGo.setVisibility(View.VISIBLE);
			mTextViewPick.setVisibility(View.VISIBLE);
			
			mTextViewPick.setText(info.name);
		}
		
/*		
		// testing PROJ_xxx functions
		WrapInt mx = new WrapInt();
		WrapInt my = new WrapInt();
			
		citus_api.MV3D_Scr2Map(sx, sy, mx, my);
		String strVal = String.format("screen to map: %d-%d => %d-%d", sx, sy, mx.value, my.value);
		Log.i("amh", strVal);
		
		WrapDouble lon = new WrapDouble();
		WrapDouble lat = new WrapDouble();
		
		citus_api.PROJ_MAPtoWGS84(mx.value, my.value, lat, lon);
		strVal = String.format("map to wgs84: %d-%d => %.6f-%.6f", mx.value, my.value, lon.value, lat.value);
		Log.i("amh", strVal);
		
		citus_api.PROJ_WGS84toMAP(lat.value, lon.value, mx, my);
		strVal = String.format("wgs84 to map: %.6f-%.6f => %d-%d", lon.value, lat.value, mx.value, my.value);
		Log.i("amh", strVal);
*/		
		
	}
}
