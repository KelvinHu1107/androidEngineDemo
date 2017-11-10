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
import kr.co.citus.engine.struct.NDB_ADMIN_INFO;
import kr.co.citus.engine.struct.NDB_KIND_INFO;
import kr.co.citus.engine.struct.NDB_POI_BODY_INFO;
import kr.co.citus.engine.struct.NDB_RESULT;
import kr.co.citus.engine.struct.POI_INFO;
import kr.co.citus.engine.wrap.WrapString;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class DLG_SEARCH_GROUP_LIST extends Activity
{
	ListAdapter mListAdapter;
	
	public boolean m_isSearchByKeyword 	= false;
	public boolean m_isSearchByDist 	= false;
	public String m_searchText = null;
	
	ListView mListView = null;
	RadioButton radioName = null;
	RadioButton radioDist = null;
	ArrayList<NDB_RESULT> mList = null;
	public String[] txtSetTarget = {"bopomopo", "zhuyin"};
	
	public int m_group_idx = 0;
	public int m_group_cnt = 0;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_group_list);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		setButton();
		
		Intent intent = getIntent();
		m_group_idx = intent.getIntExtra("groupIdx", 0);
		m_group_cnt = intent.getIntExtra("groupCnt", 0);
		mListView = (ListView) findViewById(R.id.listView1);
		mList = new ArrayList<NDB_RESULT>();
		mListAdapter = new ListAdapter(this,R.layout.row_search_list, mList);
		mListView.setAdapter(mListAdapter);
		listUpdate();
	}
	
	public void listUpdate()
	{
		if(mList == null)	mList = new ArrayList<NDB_RESULT>();
		else				mList.clear();
		int cnt = 0;
		// Group
		NDB_RESULT res = new NDB_RESULT();
		for (int i = 0; i < m_group_cnt; i++)
		{
			citus_api.NDB_Result_Group_GetItem(m_group_idx, i, res);
			res.db_type = citus_api.NDB_RES_POI_BODY;
			mList.add(new NDB_RESULT(res));
		}
	}
	
	private final static Comparator<NDB_RESULT> myComparator = new Comparator<NDB_RESULT>()
	{
		private final Collator collator = Collator.getInstance();
		public int compare(NDB_RESULT obj1, NDB_RESULT obj2)
		{
			return obj1.distance - obj2.distance;
		}
	};
	
	private void setButton()
	{
		radioName = (RadioButton)findViewById(R.id.radioName);
		radioDist = (RadioButton)findViewById(R.id.radioDist);
		radioName.setChecked(true);
		radioName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_isSearchByDist == false)
					return;
				else
				{
					radioName.setChecked(true);
					radioDist.setChecked(false);
					m_isSearchByDist = false;
					listUpdate();
					mListAdapter.notifyDataSetInvalidated();
				}
			}
		});
		radioDist.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_isSearchByDist)
					return;
				else
				{
					radioName.setChecked(false);
					radioDist.setChecked(true);
					m_isSearchByDist = true;
					listUpdate();
					mListAdapter.notifyDataSetInvalidated();
				}
			}
		});
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_GROUP_LIST.this, MapViewActivity.class);
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
	
	
	class ListAdapter extends ArrayAdapter<NDB_RESULT> {
		int mResource;
		ArrayList<NDB_RESULT> mList;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<NDB_RESULT> objects) {
			super(context, textViewResourceId, objects);
			mResource = textViewResourceId;
			mList = objects;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) DLG_SEARCH_GROUP_LIST.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(mResource, null);
			}
			v.setMinimumWidth(((WindowManager) v.getContext().getSystemService(
					Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth());
			
			setListText(v, position);
			return v;
		}
		void setList(ArrayList<NDB_RESULT> objects)
		{
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
		String txt1 = null;
		txt1 = new String(item.name1 + item.name2);
		listText1.setText(txt1);
		// POI Body ��������
		NDB_POI_BODY_INFO info = new NDB_POI_BODY_INFO();
		citus_api.NDB_Poi_GetBody(item.body_idx, info);
		WrapString admin_name = new WrapString();
		citus_api.NDB_Admin_GetName(citus_api.NDB_RES_POI, info.admin_idx, admin_name, 100);
		listText2.setText(admin_name.value + info.address);
		imgPOI.setVisibility(View.VISIBLE);
		listText2.setVisibility(View.VISIBLE);
		
		// Kind Code ��������
		NDB_KIND_INFO kinfo = new NDB_KIND_INFO();
		citus_api.NDB_Kind_GetInfo(info.kind_idx, kinfo);
		
		int kindIdx = (kinfo.kind_code % 1000000) /10000;
		int iconIdx = kindIdx - 1;
		if(iconIdx >= 0)
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
		Intent intent = new Intent(DLG_SEARCH_GROUP_LIST.this, DLG_VIEW_MAP_SEARCH.class);
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