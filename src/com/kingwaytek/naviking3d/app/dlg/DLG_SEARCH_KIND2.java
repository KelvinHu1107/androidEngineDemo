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

public class DLG_SEARCH_KIND2 extends Activity
{
	public int m_kind1Idx = 0;
	public int m_kind1Code = 0;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_kind);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		Intent intent = getIntent();
		m_kind1Idx = intent.getIntExtra("kind1Idx", 0);
		m_kind1Code = intent.getIntExtra("kind1Code", 0);
		makeBtnText();
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_KIND2.this, MapViewActivity.class);
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
	
	private void makeBtnText()
	{
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_KIND);
		int kindNum = citus_api.NDB_Kind_GetChildNum(m_kind1Idx);

		int btnIdx = 0;
		for(int i=0;i<16;i++)
		{
			btnIdx++;
			int obj_id_text = getResources().getIdentifier("Button"+ btnIdx, "id", getPackageName());
			Button btn = (Button)findViewById(obj_id_text);
			if(i > kindNum)
			{
				btn.setText(new String(" "));
				btn.setTag(0);
			}
			else if(i == 0)
			{
				btn.setText(new String("All"));
				btn.setTag(0);
			}
			else
			{
				NDB_KIND_INFO info = new NDB_KIND_INFO();
				citus_api.NDB_Kind_GetChildInfo(m_kind1Idx, i-1, info);
				btn.setText(new String(info.kind2_name));
				btn.setTag(new Integer(info.kind_code));
			}
			
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{
					Integer tag = (Integer)v.getTag();
					int iTag = -1;
					if (tag != null)
						iTag = tag.intValue();
					
					if(tag == 0)
					{
						Intent intent = new Intent(DLG_SEARCH_KIND2.this, DLG_SEARCH_KIND_LIST.class);
						intent.putExtra("kindcode", m_kind1Code);
						startActivity(intent);
					}
					else
					{
						Intent intent = new Intent(DLG_SEARCH_KIND2.this, DLG_SEARCH_KIND3.class);
						intent.putExtra("kind1Idx", m_kind1Idx);
						intent.putExtra("kind1Code", m_kind1Code);
						int idx = citus_api.NDB_Kind_FindByCode(iTag);
						intent.putExtra("kind2Idx", idx);
						intent.putExtra("kind2Code", iTag);
						startActivity(intent);
					}
				}
			});
		}
		
	}
	
	private boolean alert(String _msg)
	{
		new AlertDialog.Builder(DLG_SEARCH_KIND2.this)
		.setTitle("알림")
	    .setMessage(_msg)
		.setPositiveButton("확인", null)
		.show();
		return false;
	}
}