package com.kingwaytek.naviking3d.app.dlg;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.R;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.NDB_KIND_INFO;
import kr.co.citus.engine.struct.NDB_POI_BODY_INFO;
import kr.co.citus.engine.struct.NDB_RESULT;
import kr.co.citus.engine.struct.IPOINT;
import kr.co.citus.engine.struct.POI_INFO;
import kr.co.citus.engine.wrap.WrapString;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DLG_SEARCH_KIND_LIST extends Activity
{
	ListAdapter mListAdapter;
	
	public boolean m_isSearchByKeyword 	= false;
	public boolean m_isSearchByDist 	= false;
	public int m_kindcode = 0;
	
	ListView mListView = null;
	ArrayList<NDB_RESULT> mList = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_kind_list);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		mListView = (ListView) findViewById(R.id.listView1);
		Intent intent = getIntent();
		m_kindcode = intent.getIntExtra("kindcode", 0);
		mList = new ArrayList<NDB_RESULT>();
		mListAdapter = new ListAdapter(this,R.layout.row_search_list, mList);
		
		search();
		listUpdate();
		mListView.setAdapter(mListAdapter);
		citus_api.MV3D_ClearSearchMark();
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_KIND_LIST.this, DLG_VIEW_MAP_SEARCH.class);
				citus_api.MV3D_ClearSearchMark();
				int count = Math.min(50, mList.size());
				for (int i=count-1; i>=0; i--)
				{
					NDB_RESULT item = mList.get(i);
					citus_api.MV3D_AddSearchMark(i, item.x, item.y, false);
				}
				intent.putExtra("isMultiSearch", true);
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
		new AlertDialog.Builder(DLG_SEARCH_KIND_LIST.this)
		.setTitle("�˸�")
	    .setMessage(_msg)
		.setPositiveButton("Ȯ��", null)
		.show();
		return false;
	}
	
	public void search()
	{
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_KIND);
		citus_api.NDB_Param_Reset();
		int idx = citus_api.NDB_Kind_FindByCode(m_kindcode);
		citus_api.NDB_Param_AddKind(idx);
		IPOINT carPos = citus_api.SYS_GetCarPos_ForSearch();
		citus_api.NDB_Param_SetRadius(carPos.x, carPos.y, 0);
		citus_api.NDB_SearchStart(citus_api.NDB_SEARCH_METHOD_BY_KIND, 50, 0);
	}
	
	public void listUpdate()
	{
		if(mList == null) mList = new ArrayList<NDB_RESULT>();
		else mList.clear();
		
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_POI);
		int cnt = citus_api.NDB_Result_GetCount();
		NDB_RESULT res = new NDB_RESULT();
		NDB_POI_BODY_INFO pinfo = new NDB_POI_BODY_INFO();
		for(int i = 0; i < cnt; i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			res.db_type = citus_api.NDB_RES_POI_BODY;
			mList.add(new NDB_RESULT(res));
		}
		if(m_isSearchByDist)
			Collections.sort(mList, myComparator);
	}
	private final static Comparator<NDB_RESULT> myComparator = new Comparator<NDB_RESULT>()
	{
		private final Collator collator = Collator.getInstance();
		public int compare(NDB_RESULT obj1, NDB_RESULT obj2)
		{
			//return collator.compare(new Integer(obj1.dist),new Integer(obj2.dist));  
			return obj1.distance - obj2.distance;
		}
	};
	
	
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
				LayoutInflater vi = (LayoutInflater) DLG_SEARCH_KIND_LIST.this
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
		citus_api.MV3D_ClearSearchMark();
		NDB_RESULT item = mList.get(idx);
		Intent intent = new Intent(DLG_SEARCH_KIND_LIST.this, DLG_VIEW_MAP_SEARCH.class);
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