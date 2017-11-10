package com.kingwaytek.naviking3d.app;

import kr.co.citus.engine.citus_api;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class RouteActivity extends Activity implements SurfaceHolder.Callback {

	static final int MAX_ROUTE = 3;
	
	Button m_btnRoute[] = {null, null, null};
	Button m_btnAdd = null;
	TextView m_textViewTotalDistance = null;
	TextView m_textViewTotalTime = null;
	TextView m_textViewTotalFare = null;
	
	int m_newIdx = 0;
	int m_curRoute = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route);
		
		SurfaceView surface = (SurfaceView)findViewById(R.id.surfaceViewRoute);
		surface.getHolder().addCallback(this);
		
		m_textViewTotalDistance = (TextView)findViewById(R.id.textViewTotalDistance);
		m_textViewTotalTime = (TextView)findViewById(R.id.textViewTotalTime);
		m_textViewTotalFare = (TextView)findViewById(R.id.textViewTotalCost);
		
		m_btnRoute[0] = (Button)findViewById(R.id.buttonRoute1);
		m_btnRoute[1] = (Button)findViewById(R.id.buttonRoute2);
		m_btnRoute[2] = (Button)findViewById(R.id.buttonRoute3);
		
		m_btnAdd = (Button)findViewById(R.id.buttonRouteAdd);
		m_btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (m_newIdx >= MAX_ROUTE)
					return;
				Intent intent = new Intent(RouteActivity.this, RouteConfigActivity.class);
				intent.putExtra("newRouteIdx", m_newIdx);
				startActivity(intent);
				
			}
		});
		
		Button btnGuide = (Button)findViewById(R.id.buttonRouteGuide);
		btnGuide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0; i<MAX_ROUTE; i++) {
					if (i==m_curRoute)
						continue;
					citus_api.RG_GuideRemove(i);
				}
				Intent intent = new Intent(RouteActivity.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// jolee - prevent re-route by GPS in route plan dialog 
				citus_api.UI_EnableAutoReroute(true);		
				MapViewActivity.setMapViewMode(MapViewActivity.mViewMode);
			}
		});
		
		Button btnDemo = (Button)findViewById(R.id.buttonRouteDemo);
		btnDemo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i=0; i<MAX_ROUTE; i++) {
					if (i==m_curRoute)
						continue;
					citus_api.RG_GuideRemove(i);
				}
				Intent intent = new Intent(RouteActivity.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// jolee - prevent re-route by GPS in route plan dialog 
				citus_api.UI_EnableAutoReroute(true);
				citus_api.UI_GuideDemo();
				
			}
		});
		
		Button btnList = (Button)findViewById(R.id.buttonRouteList);
		btnList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RouteActivity.this, RouteListActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		citus_api.MV3D_OnSurfaceChanged(holder.getSurface(), width, height);
		citus_api.MVR_SetScreenSize(width, height);
		citus_api.MV3D_DrawRouteAll();
		citus_api.MVR_SetZoomRouteAll();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		citus_api.MV3D_OnSurfaceCreated(holder.getSurface());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		citus_api.MV3D_OnSurfaceDestroyed(holder.getSurface());
	}

	@Override
	protected void onPause() {
		citus_api.MV3D_WaitRendering(true);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		int oldIdx = citus_api.RG_GetIndex();
		for (int i=0; i<MAX_ROUTE; i++) {
			citus_api.RG_SetIndex(i);
			if (citus_api.RG_IsAble()) {
				m_btnRoute[i].setVisibility(View.VISIBLE);
				m_newIdx = i+1;
			} else {
				m_btnRoute[i].setVisibility(View.GONE);
			}
		}
		if (m_newIdx >= MAX_ROUTE)
			m_btnAdd.setVisibility(View.GONE);
		else
			m_btnAdd.setVisibility(View.VISIBLE);
		citus_api.RG_SetIndex(oldIdx);
		m_curRoute = oldIdx;
		showRouteInfo();
		citus_api.MV3D_WaitRendering(false);
	}

	public void onClickRoute(View view) {
		String tag = (String)view.getTag();
		int itag = Integer.parseInt(tag);
		m_curRoute = itag;
		citus_api.RG_SetIndex(itag);
		citus_api.MV3D_DrawRouteAll();
		showRouteInfo();
	}
	
	void showRouteInfo() {
		int time = citus_api.RG_GetTotalTime();
		int dist = citus_api.RG_GetTotalDist(-1);
		int fare = citus_api.RG_GetTotalFare();
		
		String stDist = null;
		if (dist > 1000) {
			stDist = String.valueOf(dist/1000) + "." + String.valueOf(dist % 1000) + " Km";
		} else {
			stDist = String.valueOf(dist) + " m";
		}
		
		String stTime = String.valueOf(time/60) + "H" + String.valueOf(time%60) + "M";
		String stFare = "NTD " + fare;
		
		m_textViewTotalTime.setText(stTime);
		m_textViewTotalDistance.setText(stDist);
		m_textViewTotalFare.setText(stFare);
	}
}
