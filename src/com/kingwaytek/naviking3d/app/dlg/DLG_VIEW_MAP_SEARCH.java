package com.kingwaytek.naviking3d.app.dlg;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.IPOINT;
import kr.co.citus.engine.struct.NDB_ADMIN_INFO;
import kr.co.citus.engine.struct.NDB_KIND_INFO;
import kr.co.citus.engine.struct.NDB_POI_BODY_INFO;
import kr.co.citus.engine.struct.NDB_RESULT;
import kr.co.citus.engine.struct.POI_INFO;
import kr.co.citus.engine.wrap.WrapInt;
import kr.co.citus.engine.wrap.WrapString;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.R;
import com.kingwaytek.naviking3d.app.RouteActivity;

public class DLG_VIEW_MAP_SEARCH extends Activity implements SurfaceHolder.Callback 
{	
	boolean m_isMultiSearch = false;
	public int m_searchType = 4;
	public int m_bodyIdx = 0;
	NDB_RESULT res = null;
	
	String poi = "";
	String kindname = "";
	String addr = "";
	String tel = "";
	String roadid_and_se = "";
	String ukcode = "";
	
	TextView txtPOI = null;
	TextView txtKind = null;
	TextView txtAddr = null;
	TextView txtTel = null;
	TextView txtCROADID = null;
	TextView txtUKCODE = null;
	boolean m_oneTouch = false;
	
	ScaleGestureDetector mScaleDetector;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_map);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		txtPOI = (TextView)findViewById(R.id.txtPOI);
		txtKind = (TextView)findViewById(R.id.txtKind);
		txtAddr = (TextView)findViewById(R.id.txtAddr);
		txtTel = (TextView)findViewById(R.id.txtTel);
		txtCROADID = (TextView)findViewById(R.id.txtCROADID);
		txtUKCODE = (TextView)findViewById(R.id.txtUKCODE);
		
		res = citus_api.g_res_item;
		
		// POI, tel, addr, addr_admin
		setInfoText();
		
		Intent intent = getIntent();
		boolean isFromMSDlg = intent.getBooleanExtra("fromMS", false);
		m_isMultiSearch = intent.getBooleanExtra("isMultiSearch", false);
		//m_bodyIdx = intent.getIntExtra("bodyIdx", -1);
		
		Button btnSetMyPOI = (Button) findViewById(R.id.btnSetMyPOI);
		LinearLayout layoutMS = (LinearLayout) findViewById(R.id.layoutMS);
		//Button btnSearchMS = (Button) findViewById(R.id.btnSearchMS);
		int slaveCnt = 0;
		m_bodyIdx = citus_api.g_res_item.body_idx;
		if(res.db_type == citus_api.NDB_RES_POI_BODY)
			slaveCnt = citus_api.NDB_Poi_GetSlave_count(m_bodyIdx);
		if(slaveCnt <= 0 || isFromMSDlg)
			layoutMS.setVisibility(View.GONE);
		else
		{
			layoutMS.setVisibility(View.VISIBLE);
			int ent_cnt = 0;
			int park_cnt = 0;
			int shop_cnt = 0;
			
			for(int i=0;i<slaveCnt;i++)
			{
				WrapInt wRelation = new WrapInt();
				int slave_idx = citus_api.NDB_Poi_GetSlave(m_bodyIdx, i, wRelation);
				NDB_RESULT res = new NDB_RESULT();
				int iRel = wRelation.value;
				switch(iRel)
				{
				case citus_api.POI_MS_INFO_RELATION_ENTRY_EXIT:
					ent_cnt++;
					break;
				case citus_api.POI_MS_INFO_RELATION_PARKING_LOT:
					park_cnt++;
					break;
				case citus_api.POI_MS_INFO_RELATION_FACILITY:
				case citus_api.POI_MS_INFO_RELATION_STORE:
					shop_cnt++;
					break;
				default:
					break;
				}
			}
			Button btnEntrance = (Button) findViewById(R.id.btnEntrance);
			Button btnParking = (Button) findViewById(R.id.btnParking);
			Button btnShop = (Button) findViewById(R.id.btnShop);
			btnEntrance.setText("Entrance(" + ent_cnt + ")");
			btnParking.setText("Parking(" + park_cnt + ")");
			btnShop.setText("Shop(" + shop_cnt + ")");
			if(ent_cnt > 0)
				btnEntrance.setVisibility(View.VISIBLE);
			else
				btnEntrance.setVisibility(View.GONE);
			if(park_cnt > 0)
				btnParking.setVisibility(View.VISIBLE);
			else
				btnParking.setVisibility(View.GONE);
			if(shop_cnt > 0)
				btnShop.setVisibility(View.VISIBLE);
			else
				btnShop.setVisibility(View.GONE);
			btnEntrance.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(DLG_VIEW_MAP_SEARCH.this, DLG_SEARCH_MS_LIST.class);
					intent.putExtra("bodyIdx", m_bodyIdx);
					intent.putExtra("slaveType", citus_api.POI_MS_INFO_RELATION_ENTRY_EXIT);
					startActivity(intent);
				}
			});
			btnParking.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(DLG_VIEW_MAP_SEARCH.this, DLG_SEARCH_MS_LIST.class);
					intent.putExtra("bodyIdx", m_bodyIdx);
					intent.putExtra("slaveType", citus_api.POI_MS_INFO_RELATION_PARKING_LOT);
					startActivity(intent);
				}
			});
			btnShop.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(DLG_VIEW_MAP_SEARCH.this, DLG_SEARCH_MS_LIST.class);
					intent.putExtra("bodyIdx", m_bodyIdx);
					intent.putExtra("slaveType", citus_api.POI_MS_INFO_RELATION_FACILITY);
					startActivity(intent);
				}
			});
		}
		
		Button btnSetStart = (Button) findViewById(R.id.btnSetStart);
		Button btnSetVia = (Button) findViewById(R.id.btnSetVia);
		Button btnSetEnd = (Button) findViewById(R.id.btnSetEnd);
		Button btnFindRoute = (Button) findViewById(R.id.btnFindRoute);
		switch(m_searchType)
		{
		case 0:
			btnSetStart.setVisibility(View.VISIBLE);
			break;
		case 4:
			btnSetEnd.setVisibility(View.VISIBLE);
			break;
		case 1:
		case 2:
		case 3:
			btnSetVia.setVisibility(View.VISIBLE);
			break;
		}
		btnSetStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int ret = citus_api.UI_CheckStartPos();
				if (ret != 0) {
					if (ret == 1)
						alert("Too close to start position.");
					else if (ret == 2)
						alert("Too close to destination.");
					else if (ret < 0)
						alert("Too close to via.");
					else if (ret == 999)
						alert("Out of service area.");	
				} else {
					citus_api.RG_GuideRemoveAll();
					citus_api.UI_SetMapMoveMode((byte)2, true, true);
					citus_api.SYS_PauseMoveCenter();
					citus_api.UI_SetStartPos(false, false);
				}
			}});
		btnSetEnd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				POI_INFO info = new POI_INFO();
				citus_api.SYS_GetSearchPos(info);
				int ret = citus_api.UI_CheckEndPos();
				if (ret != 0) {
					if (ret == 1)
						alert("Too close to start position.");
					else if (ret == 2)
						alert("Too close to destination.");
					else if (ret < 0)
						alert("Too close to via.");
					else if (ret == 999)
						alert("Out of service area.");	
				} else {
					citus_api.RG_GuideRemoveAll();
					citus_api.UI_SetMapMoveMode((byte)2, true, true);
					citus_api.SYS_PauseMoveCenter();
					citus_api.UI_SetEndPos(false, false);
				}
			}});
		btnFindRoute.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// initialize avoid information
				citus_api.avoid_reset();
				POI_INFO info = new POI_INFO();
				citus_api.SYS_GetSearchPos(info);
				int ret = citus_api.UI_CheckEndPos();
				if (ret != 0) {
					alert("Set destination point is not set");
				} 
				
				// jolee - prevent re-route by GPS in route plan dialog 
				citus_api.UI_EnableAutoReroute(false);
				
				citus_api.UI_SetRpIdx(0);
				// default route option here..
				citus_api.UI_SetRoutePlanOption(0, citus_api.RP_VEHICLE_CAR, citus_api.RP_METHOD_OPTIMAL, true, true);
				WrapInt errCode = new WrapInt();
				citus_api.UI_RoutePlan((citus_api.SYS_IsStartPos()==false), false, true, 0, errCode, true);
				Intent intent = new Intent(DLG_VIEW_MAP_SEARCH.this, RouteActivity.class);
				startActivity(intent);
			}});
		btnSetVia.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setPosition();
			}});
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// jolee - prevent re-route by GPS in route plan dialog 
				citus_api.UI_EnableAutoReroute(true);
				citus_api.UI_GoCurrent();
				Intent intent = new Intent(DLG_VIEW_MAP_SEARCH.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// jolee - prevent re-route by GPS in route plan dialog 
				citus_api.UI_EnableAutoReroute(true);
				citus_api.UI_GoCurrent();
				finish();
			}
		});
		
		SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceViewDetailMap);
		surface.getHolder().addCallback(this);
		surface.setOnTouchListener(new View.OnTouchListener() {
			float downX = 0;
			float downY = 0;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScaleDetector.onTouchEvent(event);
				int action = event.getAction();
				int ptcount = event.getPointerCount();
				if (ptcount == 1) {
					float ptrX = event.getX();
					float ptrY = event.getY();
					
					switch(action)                                                 
					{
					case MotionEvent.ACTION_DOWN:
						m_oneTouch = true;
						citus_api.MV3D_OnMouseDown((int)ptrX, (int)ptrY);
						downX = ptrX;
						downY = ptrY;
						break;
					case MotionEvent.ACTION_MOVE:
						if (m_oneTouch)
							citus_api.MV3D_OnMouseMove((int)ptrX, (int)ptrY);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_OUTSIDE:
						if (m_oneTouch) {
							citus_api.MV3D_OnMouseUp((int)ptrX, (int)ptrY);
							if (m_isMultiSearch) {
								float sqrLen = (ptrX - downX) * (ptrX - downX) + (ptrY - downY) * (ptrY - downY);
								if (sqrLen < 25) {
									int resid = citus_api.MV3D_GetSearchResultOnPick((int)ptrX, (int)ptrY);
									if (resid >= 0) {
										NDB_RESULT item = new NDB_RESULT();
										citus_api.NDB_Result_GetItem(resid, item);
										item.db_type = citus_api.NDB_RES_POI_BODY;
										citus_api.g_res_item = new NDB_RESULT(item);
										res = citus_api.g_res_item;
										m_bodyIdx = resid;
										int road_id = 0; 
										setInfoText();
										citus_api.SYS_SetSearchPos(res.x, res.y,road_id, poi, addr);
										citus_api.UI_SetMapMoveMode((byte)2, true, true);
										citus_api.SYS_PauseMoveCenter();
									}
								}
							}
						}
						break;
					} 
				}
				return true;
			}
		});

		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
	}
	
	private void setInfoText() {
		if (res == null)
			return;
		if(res.db_type == citus_api.NDB_RES_POI_BODY)
		{
			NDB_POI_BODY_INFO pinfo = new NDB_POI_BODY_INFO();
			NDB_KIND_INFO kinfo = new NDB_KIND_INFO();
			NDB_ADMIN_INFO ainfo = new NDB_ADMIN_INFO();
			WrapString admin_name = new WrapString();
			citus_api.NDB_Poi_GetBody(res.body_idx, pinfo);
			citus_api.NDB_Kind_GetInfo(pinfo.kind_idx, kinfo);
			citus_api.NDB_Admin_GetName(citus_api.NDB_RES_POI, pinfo.admin_idx, admin_name, 100);
			int brd_code = 0;
			if(pinfo.brd_idx > 0)
			{
				brd_code = citus_api.NDB_Kind_GetCode(pinfo.brd_idx);
				kindname = "(brd:" + brd_code + ")" + kinfo.kind1_name + " > " + kinfo.kind2_name + " > " + kinfo.kind3_name;
			}
			else
			{
				kindname = kinfo.kind1_name + " > " + kinfo.kind2_name + " > " + kinfo.kind3_name;
			}
			
			poi = pinfo.name1 + pinfo.name2;
			
			addr = admin_name.value + pinfo.address;
			tel = pinfo.tel;
			
			ukcode = pinfo.ubcode;
			roadid_and_se = ""+pinfo.roadid_and_se;
			
		}
		else if(res.db_type == citus_api.NDB_RES_ADDR || res.db_type == citus_api.NDB_RES_ADDR_ADMIN)
		{
			NDB_ADMIN_INFO ainfo = new NDB_ADMIN_INFO();
			WrapString admin_name = new WrapString();
			citus_api.NDB_Admin_GetName(citus_api.NDB_RES_POI, res.admin_idx, admin_name, 100);
			poi = res.name1 + res.name2;
			addr = admin_name.value + poi;
		}
		txtPOI.setText(poi);
		txtKind.setText(kindname);
		txtAddr.setText(addr);
		txtTel.setText(tel);
		txtCROADID.setText(roadid_and_se);
		txtUKCODE.setText(ukcode);
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		float prevScale = 1.0f;
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			m_oneTouch = false;
			citus_api.MV3D_SetPanning(true);
			prevScale = detector.getScaleFactor();
			return super.onScaleBegin(detector);
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			citus_api.MV3D_SetPanning(false);
			super.onScaleEnd(detector);
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float curScale = detector.getScaleFactor();
			float scale = curScale / prevScale;
			citus_api.MV_OnScale(scale, 1.0f);
			prevScale = curScale;	
			return super.onScale(detector);
		}
	}
	
	private void setPosition()
	{
		int ret = 0;
		boolean isError = false;
		switch(m_searchType)
		{
		case 0:
			ret = citus_api.UI_CheckStartPos();
			if(ret != 0)
				isError = true;
			else
				citus_api.UI_SetStartPos(true, false);
			break;
		case 4:
			ret = citus_api.UI_CheckEndPos();
			if(ret != 0)
				isError = true;
			else
				citus_api.UI_SetEndPos(true, false);
			break;
		case 1:
		case 2:
		case 3:
			ret = citus_api.UI_CheckMidPos();
			if(ret != 0)
				isError = true;
			else
				citus_api.UI_SetMidPos(true, false,false);
			break;
		}
		if(isError)
		{
			// Error
			if (ret == 1)
				alert("This position is too close from start location.");
			else if (ret == 2)
				alert("This position is too close from destination.");
			else if (ret < 0)
				alert("This position is too close from via position.");
			else if (ret == 9999)
				alert("There is no search positon");
			return;
		}
		Intent intent = new Intent(DLG_VIEW_MAP_SEARCH.this, MapViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// jolee - prevent re-route by GPS in route plan dialog 
		citus_api.UI_EnableAutoReroute(true);
		citus_api.UI_GoCurrent();
		startActivity(intent);
	}
	public boolean alert(String _msg)
	{
		new AlertDialog.Builder(DLG_VIEW_MAP_SEARCH.this)
		.setTitle("Error")
	    .setMessage(_msg)
		.setPositiveButton("OK", null)
		.show();
		return false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		citus_api.MV3D_OnSurfaceChanged(holder.getSurface(), width, height);
		citus_api.MVS_SetScreenSize(width, height);
		if (m_isMultiSearch)
		{
			citus_api.MV3D_ZoomAllSearchMark();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		citus_api.MV3D_OnSurfaceCreated(holder.getSurface());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	@Override
	protected void onResume() {
		if (m_isMultiSearch == false && res != null) {
			int road_id = 0;
			citus_api.SYS_SetSearchPos(res.x, res.y, road_id,poi, addr);
			citus_api.UI_SetMapMoveMode((byte)2, true, true);
			citus_api.SYS_PauseMoveCenter();
			citus_api.MVS_SetZoom(res.x, res.y);
		}
		citus_api.MV3D_WaitRendering(false);
		super.onResume();
	}

	@Override
	protected void onPause() {
		citus_api.MV3D_WaitRendering(true);
		super.onPause();
	}
}
