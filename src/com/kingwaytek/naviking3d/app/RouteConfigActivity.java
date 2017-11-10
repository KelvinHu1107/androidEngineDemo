package com.kingwaytek.naviking3d.app;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.wrap.WrapInt;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class RouteConfigActivity extends Activity {

	
	int m_routeIdx = -1;
	int m_vehicle = 0;
	int m_routeMethod = 0;
	boolean m_onlyFreeRoad = false;
	Button m_btnCar;
	String[] items = {"Car", "Bicycle", "Motor", "Heavey Motor", "Truck", "Heavey Truck", "Cancel"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route_config);
		
		Intent intent = getIntent();
		m_routeIdx = intent.getIntExtra("newRouteIdx", -1);
		
		m_btnCar = (Button)findViewById(R.id.buttonRouteCar);
		m_btnCar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(RouteConfigActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 6)
						{
							dialog.dismiss();
							return;
						}
						
						m_vehicle = which;
						m_btnCar.setText(items[which]);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Map color mode");
				adSelect.show();
			}
		});
		
		RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroupRouteConfig);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioOptimalRoute:
					m_routeMethod = citus_api.RP_METHOD_OPTIMAL;
					break;
				case R.id.radioShortestRoute:
					m_routeMethod = citus_api.RP_METHOD_SHORT;
					break;
				case R.id.radioNational1Route:
					m_routeMethod = citus_api.RP_METHOD_1ST_HIGHWAY;
					break;
				case R.id.radioNational3Route:
					m_routeMethod = citus_api.RP_METHOD_3RD_HIGHWAY;
					break;
				case R.id.radioAvoidHighway:
					m_routeMethod = citus_api.RP_METHOD_NO_HIGHWAY;
					break;
				}
				
			}
		});
		
		Switch sw = (Switch)findViewById(R.id.switchOnlyFreeRoad);
		sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				m_onlyFreeRoad = isChecked;
			}
		});
		
		Button btnAdd = (Button)findViewById(R.id.buttonNewRoute);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (m_routeIdx < 0) {
					return;
				}
				citus_api.UI_SetRpIdx(m_routeIdx);
				Log.i("amh", "route id: " + m_routeIdx + " vehicle: " + m_vehicle + " method: " + m_routeMethod);
				citus_api.UI_SetRoutePlanOption(m_routeIdx, m_vehicle, m_routeMethod, !m_onlyFreeRoad, true);
				WrapInt errCode = new WrapInt();
				citus_api.UI_RoutePlan((citus_api.SYS_IsStartPos()==false), false, true, 0, errCode, true);
				finish();
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
		getMenuInflater().inflate(R.menu.route_config, menu);
		return true;
	}

}
