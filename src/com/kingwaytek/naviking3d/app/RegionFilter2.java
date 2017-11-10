package com.kingwaytek.naviking3d.app;

import java.util.ArrayList;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.NDB_ADMIN_INFO;

import com.kingwaytek.naviking3d.app.RegionFilter1.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RegionFilter2 extends Activity {
	
	ArrayList<NDB_ADMIN_INFO> listAdmin = null;
	int level1_idx = -1;
	String level1_name = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_only);
		
		Intent lv1_intent = getIntent();
		level1_idx = lv1_intent.getIntExtra("admin_idx", -1);
		level1_name = lv1_intent.getStringExtra("admin_name");
		
		listAdmin = new ArrayList<NDB_ADMIN_INFO>();
		NDB_ADMIN_INFO item_all_admin = new NDB_ADMIN_INFO();
		item_all_admin.admin2_name = "All";
		listAdmin.add(item_all_admin);
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_KEYWORD);
		int total_cnt = citus_api.NDB_Admin_GetChildNum(citus_api.NDB_RES_POI, level1_idx);
		for (int iii=0;iii<total_cnt;iii++)
		{
			NDB_ADMIN_INFO item = new NDB_ADMIN_INFO();
			
			int idx = citus_api.NDB_Admin_GetChildInfo(citus_api.NDB_RES_POI, level1_idx, iii, item);
			item.admin_idx = idx;
			listAdmin.add(item);
		}
		
		ListView listView = (ListView)findViewById(R.id.listview);
		ListAdapter adapter = new ListAdapter(this, android.R.layout.simple_list_item_1, listAdmin);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				if (arg2 == 0)
				{
					intent.putExtra("admin_idx", level1_idx);
					intent.putExtra("admin_name", level1_name);
					
				}
				else
				{
					NDB_ADMIN_INFO item = listAdmin.get(arg2);
					
					intent.putExtra("admin_idx", item.admin_idx);
					intent.putExtra("admin_name", item.admin2_name);
					
				}
				setResult(0xFF0004, intent);
				finish();

			}
		});
	}
	
	class ListAdapter extends ArrayAdapter<NDB_ADMIN_INFO> {
		int mResource;
		ArrayList<NDB_ADMIN_INFO> mList;
		Context mContext;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<NDB_ADMIN_INFO> objects) {
			super(context, textViewResourceId, objects);
			mResource = textViewResourceId;
			mList = objects;
			mContext = context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
				view = inflater.inflate(android.R.layout.simple_list_item_1, null);
			} else {
				view = convertView;
			}
			
			TextView label = (TextView)view.findViewById(android.R.id.text1);
			
			NDB_ADMIN_INFO item = mList.get(position);
			label.setText(item.admin2_name);
			
			return view;
		}
	}
}
