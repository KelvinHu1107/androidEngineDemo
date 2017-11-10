package com.kingwaytek.naviking3d.app.dlg;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.DB_KIND_INFO;
import kr.co.citus.engine.struct.NDB_KIND_INFO;

import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

public class DLG_SEARCH_NEAR extends Activity
{
	public static final int DB_NEAR_CAR = 0; 
	public static final int DB_NEAR_ROUTE = 1;
	
	RadioButton radioNearCar = null;
	RadioButton radioNearRoute = null;
	public int m_nearType = DB_NEAR_CAR;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_near);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		radioNearCar = (RadioButton)findViewById(R.id.radioName);
		radioNearRoute = (RadioButton)findViewById(R.id.radioDist);
		if(citus_api.RG_IsAble() == false)
			radioNearRoute.setEnabled(false);
		radioNearCar.setChecked(true);
		radioNearCar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_nearType == DB_NEAR_CAR)
					return;
				else
				{
					m_nearType = DB_NEAR_CAR;
					radioNearCar.setChecked(true);
					radioNearRoute.setChecked(false);
				}
			}
		});
		radioNearRoute.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_nearType == DB_NEAR_ROUTE)
					return;
				else
				{
					
					m_nearType = DB_NEAR_ROUTE;
					radioNearCar.setChecked(false);
					radioNearRoute.setChecked(true);
				}
			}
		});
		
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_KIND);
		
		int kind1_num = citus_api.NDB_Kind_GetChildNum(-1);

		for(int i=1;i<17 && i<=kind1_num;i++)
		{
			int obj_id_text = getResources().getIdentifier("Button"+i, "id", getPackageName());
			Button btn = (Button)findViewById(obj_id_text);
			if(i > kind1_num)
			{
				btn.setText(new String(" "));
			}
			else
			{
				NDB_KIND_INFO info = new NDB_KIND_INFO();
				citus_api.NDB_Kind_GetChildInfo(-1, i-1, info);
				btn.setText(new String(info.kind1_name));
				btn.setTag(new Integer(info.kind_idx));
			}
			
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					Integer tag = (Integer)v.getTag();
					int iTag = -1;
					if (tag != null)
						iTag = tag.intValue();
					Intent intent = new Intent(DLG_SEARCH_NEAR.this, DLG_SEARCH_NEAR_LIST.class);
					intent.putExtra("kindKind", iTag);
					intent.putExtra("nearType", m_nearType);
					startActivity(intent); 
				}
			});
		}
		
		// jolee is for POI_0704 ��������
		{
			int i = 15;
			int kind_idx = citus_api.NDB_Kind_FindByCode("070400");
			int obj_id_text = getResources().getIdentifier("Button"+i, "id", getPackageName());
			Button btn = (Button)findViewById(obj_id_text);
			
				btn.setText(new String("��������"));
				btn.setTag(new Integer(kind_idx));
			
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					Integer tag = (Integer)v.getTag();
					int iTag = -1;
					if (tag != null)
						iTag = tag.intValue();
					Intent intent = new Intent(DLG_SEARCH_NEAR.this, DLG_SEARCH_NEAR_LIST.class);
					intent.putExtra("kindKind", iTag);
					intent.putExtra("nearType", m_nearType);
					startActivity(intent); 
				}
			});
		
			
		}
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_NEAR.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	
	private boolean alert(String _msg)
	{
		new AlertDialog.Builder(DLG_SEARCH_NEAR.this)
		.setTitle("�˸�")
	    .setMessage(_msg)
		.setPositiveButton("Ȯ��", null)
		.show();
		return false;
	}
}