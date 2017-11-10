package com.kingwaytek.naviking3d.app.dlg;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.R;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.DB_ITEM;
import kr.co.citus.engine.struct.IPOINT;
import kr.co.citus.engine.struct.NDB_KIND_INFO;
import kr.co.citus.engine.struct.NDB_POI_BODY_INFO;
import kr.co.citus.engine.struct.NDB_RESULT;
import kr.co.citus.engine.struct.POI_INFO;
import kr.co.citus.engine.wrap.WrapInt;
import kr.co.citus.engine.wrap.WrapString;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class DLG_SEARCH_MS_LIST extends Activity
{
	ListAdapter mListAdapter;
	ListView mListView = null;
	public String m_searchText = null;
	ArrayList<NDB_RESULT> mList = null;
	
	public int m_bodyIdx = 0;
	public int m_slaveType = 0;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_list_nobtn);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		Intent intent = getIntent();
		m_bodyIdx = intent.getIntExtra("bodyIdx", 0);
		m_slaveType = intent.getIntExtra("slaveType", 0);
		
		mListView = (ListView) findViewById(R.id.listView1);
		mList = new ArrayList<NDB_RESULT>();
		mListAdapter = new ListAdapter(this,R.layout.row_search_list, mList);
		mListView.setAdapter(mListAdapter);
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_MS_LIST.this, MapViewActivity.class);
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
		listUpdate();
	}
	/*
	public void search()
	{
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_UBCODE);
		citus_api.NDB_Param_Reset();
		citus_api.NDB_Param_SetName(m_searchText.toUpperCase(), "");
		IPOINT carPos = citus_api.SYS_GetCarPos_ForSearch();
		citus_api.NDB_Param_SetRadius(carPos.x, carPos.y, 0);
		citus_api.NDB_SearchStart(citus_api.NDB_SEARCH_METHOD_BY_UBCODE, 10, 0);
	}*/
	
	public void listUpdate()
	{
		if(mList == null)	mList = new ArrayList<NDB_RESULT>();
		else				mList.clear();
		
		int slaveCnt = citus_api.NDB_Poi_GetSlave_count(m_bodyIdx);
		
		NDB_POI_BODY_INFO poi_info = new NDB_POI_BODY_INFO();
		for (int i = 0; i < slaveCnt; i++)
		{
			WrapInt wRelation = new WrapInt();
			int slave_idx = citus_api.NDB_Poi_GetSlave(m_bodyIdx, i, wRelation);
			int iRel = wRelation.value;
			switch(m_slaveType)
			{
			case citus_api.POI_MS_INFO_RELATION_ENTRY_EXIT:
				if(iRel != citus_api.POI_MS_INFO_RELATION_ENTRY_EXIT)
					continue;
				break;
			case citus_api.POI_MS_INFO_RELATION_PARKING_LOT:
				if(iRel != citus_api.POI_MS_INFO_RELATION_PARKING_LOT)
					continue;
				break;
			case citus_api.POI_MS_INFO_RELATION_FACILITY:
				if(iRel != citus_api.POI_MS_INFO_RELATION_FACILITY && iRel != citus_api.POI_MS_INFO_RELATION_STORE)
					continue;
				break;
			}
			citus_api.NDB_Poi_GetBody(slave_idx, poi_info);
			NDB_RESULT res = new NDB_RESULT();
			res.body_idx = slave_idx;
			res.name1 = new String(poi_info.name1);
			res.name2 = new String(poi_info.name2);
			res.db_type = citus_api.NDB_RES_POI_BODY;
			res.distance = citus_api.g_res_item.distance;
			res.angle = citus_api.g_res_item.angle;
			res.x = poi_info.x;
			res.y = poi_info.y;
			mList.add(new NDB_RESULT(res));
		}
	}
	
	private final static Comparator<NDB_RESULT> myComparator = new Comparator<NDB_RESULT>()
	{
		private final Collator collator = Collator.getInstance();
		public int compare(NDB_RESULT obj1, NDB_RESULT obj2) {
			return obj1.distance - obj2.distance;
		}
	};
	
	
	class ListAdapter extends ArrayAdapter<NDB_RESULT> {
		int mResource;
		ArrayList<NDB_RESULT> mList;

		public ListAdapter(Context context, int textViewResourceId, ArrayList<NDB_RESULT> objects) {
			super(context, textViewResourceId, objects);
			mResource = textViewResourceId;
			mList = objects;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) DLG_SEARCH_MS_LIST.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(mResource, null);
			}
			v.setMinimumWidth(((WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth());
			setListText(v, position);
			return v;
		}
		void setList(ArrayList<NDB_RESULT> objects){
			mList = objects;
		}
	}
	
	private void setListText(View v, int position)
	{
		NDB_RESULT item = mList.get(position);
		
		TextView listText1 = (TextView) v.findViewById(R.id.txtListText1);
		TextView listText2 = (TextView) v.findViewById(R.id.txtListText2);
		ImageView imgPOI = (ImageView) v.findViewById(R.id.imgPOI);
		Button listBtn = (Button) v.findViewById(R.id.btnListButton1);
		
		//citus_api.setTextViewColorPartial(listText1, item.name2, m_searchText.toUpperCase(), 0xffff0000);
		listText1.setText(item.name1);
		// POI Body ��������
		NDB_POI_BODY_INFO info = new NDB_POI_BODY_INFO();
		citus_api.NDB_Poi_GetBody(item.body_idx, info);
		WrapString admin_name = new WrapString();
		citus_api.NDB_Admin_GetName(citus_api.NDB_RES_POI, info.admin_idx, admin_name, 100);
		listText2.setText("(brd:" + citus_api.NDB_Kind_GetCode(info.brd_idx) + ", "+item.distance+"m, dir="+item.angle+")"+admin_name.value + info.address);
		imgPOI.setVisibility(View.VISIBLE);
		listText2.setVisibility(View.VISIBLE);
		
		// Kind Code ��������
		NDB_KIND_INFO kinfo = new NDB_KIND_INFO();
		citus_api.NDB_Kind_GetInfo(info.kind_idx, kinfo);
		
		int kindIdx = (kinfo.kind_code % 1000000) /10000;
		int iconIdx = kindIdx - 1;
		if(iconIdx >= 0 && iconIdx <= 33)
		{
			int id = getResources().getIdentifier("drawable/listicon_"+iconIdx, null, getPackageName());
			imgPOI.setImageResource(id);
		}
		else
			imgPOI.setVisibility(View.INVISIBLE);
		listBtn.setVisibility(View.VISIBLE);
		listBtn.setTag(position);
		// ����Ʈ �ȿ� �ִ� ��ư�� �̺�Ʈ
		listBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int tagPosition = (Integer) v.getTag();
				viewDetail(tagPosition);
			}
		});
	}
	
	private void viewDetail(int idx)
	{
		NDB_RESULT item = mList.get(idx);
		Intent intent = new Intent(DLG_SEARCH_MS_LIST.this, DLG_VIEW_MAP_SEARCH.class);
		intent.putExtra("fromMS", true);
		citus_api.g_res_item = new NDB_RESULT(item);
		IPOINT npt = citus_api.NET_FindLinkPosWithRoadId(citus_api.g_res_item.x,  citus_api.g_res_item.y,  citus_api.g_res_item.roadid_and_se);
		if (npt != null)
		{
			citus_api.g_res_item.x = npt.x;
			citus_api.g_res_item.y = npt.y;
		}
		startActivity(intent);
		
	}
}