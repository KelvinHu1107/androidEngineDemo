package com.kingwaytek.naviking3d.app;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.wrap.WrapInt;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class RouteDetailActivity extends Activity implements SurfaceHolder.Callback {

	int m_x = 0;
	int m_y = 0;
	int m_road_id = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route_detail);
		
		SurfaceView surface = (SurfaceView)findViewById(R.id.surfaceViewRouteDetail);
		surface.getHolder().addCallback(this);
		
		Intent intent = getIntent();
		m_x = intent.getIntExtra("posx", 0);
		m_y = intent.getIntExtra("posy", 0);
		m_road_id = intent.getIntExtra("road_id", 0);
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Set Avoid Area & Re-route search.
				// set avoid area
				// must fix.
				
				// Reroute search part.
				//citus_api.UI_SetRpIdx(0);
				//citus_api.RP_AddAvoidanceWithNodePosition(m_x, m_y);
				//citus_api.avoid_add_position(m_x, m_y);
				//WrapInt errCode = new WrapInt();
				//citus_api.UI_RoutePlan((citus_api.SYS_IsStartPos()==false), false, true, 0, errCode, true);
				//setResult(1);
				finish();
			}
		});
		
		
		Button btnAvoidArea = (Button)findViewById(R.id.btnAvoidArea);
		btnAvoidArea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Set Avoid Area & Re-route search.
				// set avoid area
				// must fix.
				
				// Reroute search part.
				citus_api.UI_SetRpIdx(0);
				int gap = 500;
				//citus_api.RP_AddAvoidanceWithArea(m_x-gap, m_y-gap, m_x+gap, m_y+gap);
				citus_api.avoid_add_area(m_x-gap, m_y-gap, m_x+gap, m_y+gap);
				WrapInt errCode = new WrapInt();
				citus_api.UI_RoutePlan((citus_api.SYS_IsStartPos()==false), false, true, 0, errCode, true);
				setResult(1);
				finish();
			}
		});
		
		Button btnAvoidLink = (Button)findViewById(R.id.btnAvoidLink);
		btnAvoidLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Set Avoid Link & Re-route search.
				// set avoid area
				// must fix.
				//citus_api.RP_AddAvoidanceWithNodePosition(m_x, m_y);
				//citus_api.avoid_add_position(m_x, m_y);

				if (m_road_id != 0) citus_api.avoid_add_road_id(m_road_id);
				// Reroute search part.
				citus_api.UI_SetRpIdx(0);
				WrapInt errCode = new WrapInt();
				citus_api.UI_RoutePlan((citus_api.SYS_IsStartPos()==false), false, true, 0, errCode, true);
				setResult(1);
				finish();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_detail, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		citus_api.MV3D_OnSurfaceChanged(holder.getSurface(), width, height);
		citus_api.MVR_SetScreenSize(width, height);
		citus_api.MVR_SetZoom(m_x, m_y);	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		citus_api.MV3D_OnSurfaceCreated(holder.getSurface());
		citus_api.MV3D_WaitRendering(false);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	@Override
	protected void onPause() {
		citus_api.MV3D_WaitRendering(true);
		super.onPause();
	}

}
