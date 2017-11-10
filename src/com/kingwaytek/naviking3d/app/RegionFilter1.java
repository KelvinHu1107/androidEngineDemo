package com.kingwaytek.naviking3d.app;

import java.util.ArrayList;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.NDB_ADMIN_INFO;

import com.kingwaytek.naviking3d.app.RouteListActivity.RouteItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RegionFilter1 extends Activity {

	ArrayList<NDB_ADMIN_INFO> listAdmin = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_only);
		
		listAdmin = new ArrayList<NDB_ADMIN_INFO>();
		NDB_ADMIN_INFO item_all_admin = new NDB_ADMIN_INFO();
		item_all_admin.admin1_name = "All";
		listAdmin.add(item_all_admin);
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_KEYWORD);
		int total_cnt = citus_api.NDB_Admin_GetChildNum(citus_api.NDB_RES_POI, -1);
		for (int iii=0;iii<total_cnt;iii++)
		{
			NDB_ADMIN_INFO item = new NDB_ADMIN_INFO();
			
			int idx = citus_api.NDB_Admin_GetChildInfo(citus_api.NDB_RES_POI, -1, iii, item);
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
				if (arg2 == 0)
				{
					Intent intent = new Intent();
					intent.putExtra("admin_idx", -1);
					intent.putExtra("admin_name", "All");
					setResult(0xFF0002, intent);
					finish();
					return;
				}
				
				NDB_ADMIN_INFO item = listAdmin.get(arg2);
				
				Intent intent = new Intent(RegionFilter1.this, RegionFilter2.class);
				intent.putExtra("admin_idx", item.admin_idx);
				intent.putExtra("admin_name", item.admin1_name);
				
				startActivityForResult(intent, 0xFF0003);
			}
		});
		
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0xFF0003 && resultCode == 0xFF0004)
		{
			Intent intent = new Intent();
			intent.putExtra("admin_idx", data.getIntExtra("admin_idx", -1));
			intent.putExtra("admin_name", data.getStringExtra("admin_name"));
			setResult(0xFF0002, intent);
			finish();
			return;
		}
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
			label.setText(item.admin1_name);
			
			return view;
		}
	}
}
