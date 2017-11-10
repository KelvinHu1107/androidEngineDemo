package com.kingwaytek.naviking3d.app.dlg;

import kr.co.citus.engine.citus_api;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.kingwaytek.naviking3d.app.CarTestActivity;
import com.kingwaytek.naviking3d.app.ExSettingActivity;
import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.PoiIconTest;
import com.kingwaytek.naviking3d.app.R;

public class DLG_MAIN_MENU extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainmenu);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		Button btn1 = (Button)findViewById(R.id.Button01); // Search Name / Addr / Kind
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_SEARCH_NAME_LIST.class);
				startActivity(intent); 
			}
		});
		Button btn2 = (Button)findViewById(R.id.Button02); // Search Intersection
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_SEARCH_INT.class);
				startActivity(intent);
				
			}
		});
		Button btn3 = (Button)findViewById(R.id.Button03); // Search Tel
		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_SEARCH_TEL_LIST.class);
				startActivity(intent);
			}
		});
		Button btn4 = (Button)findViewById(R.id.Button04); // Search Kind
		btn4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_SEARCH_KIND.class);
				startActivity(intent);
			}
		});
		Button btn5 = (Button)findViewById(R.id.Button05); // Search Near
		btn5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_SEARCH_NEAR.class);
				startActivity(intent);
			}
		});
		
		Button btn6 = (Button)findViewById(R.id.Button06); // UBCode
		btn6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_SEARCH_UBCODE_LIST.class);
				startActivity(intent);
			}
		});
		
		Button btn7 = (Button)findViewById(R.id.Button07); // car setting test
		btn7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, CarTestActivity.class);
				startActivity(intent);
			}
		});
		
		Button btn8 = (Button)findViewById(R.id.Button08); // Voice Language Change
		btn8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Lang 01", "Lang 02", "Lang 03", "Lang 04", "Cancel"};
				int curVoice = citus_api.SYS_GetVoiceLanguageType();
				items[curVoice] += "(*)";
				AlertDialog.Builder adSelectVoiceLang = new AlertDialog.Builder(DLG_MAIN_MENU.this);
				adSelectVoiceLang.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 4)
						{
							dialog.dismiss();
							return;
						}
						
						citus_api.SYS_SetVoiceLanguageType(which);
						dialog.dismiss();
						
					}
				});
				adSelectVoiceLang.setTitle("Select Language");
				adSelectVoiceLang.show();
			}
		});
		
		Button btn9 = (Button)findViewById(R.id.Button09);
		btn9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, ExSettingActivity.class);
				startActivity(intent);	
			}
		});
		
		Button btn10 = (Button)findViewById(R.id.Button10); // poi icon search test
		btn10.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, PoiIconTest.class);
				startActivity(intent);
			}
		});
		
		Button btn11 = (Button)findViewById(R.id.Button11); // poi icon search test
		btn11.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, DLG_ROUTE_LIST.class);
				startActivity(intent);
			}
		});
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setVisibility(View.GONE);
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_MAIN_MENU.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
	}
	
	private boolean alert(String _msg)
	{
		new AlertDialog.Builder(DLG_MAIN_MENU.this)
		.setTitle("알림")
	    .setMessage(_msg)
		.setPositiveButton("확인", null)
		.show();
		return false;
	}
}