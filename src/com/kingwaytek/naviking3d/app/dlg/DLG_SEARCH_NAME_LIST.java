package com.kingwaytek.naviking3d.app.dlg;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.R;
import com.kingwaytek.naviking3d.app.RegionFilter1;

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

public class DLG_SEARCH_NAME_LIST extends Activity
{
	ListAdapter mListAdapter;
	
	public static boolean _IS_TEST = true;
	public boolean m_isSearchByKeyword 	= false;
	public boolean m_isSearchByDist 	= false;
	public String m_searchText = null;
	
	ListView mListView = null;
	RadioButton radioName = null;
	RadioButton radioDist = null;
	
	int m_admin_filter_idx = -1;
	String m_admin_filter_name = "All";
	ArrayList<NDB_RESULT> mList = null;
	public String[] txtSetTarget = {"bopomopo", "zhuyin"};
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_list);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		setButton();
		
		Intent intent = getIntent();
		m_searchText = intent.getStringExtra("searchText");
		
		mListView = (ListView) findViewById(R.id.listView1);
		mList = new ArrayList<NDB_RESULT>();
		mListAdapter = new ListAdapter(this,R.layout.row_search_list, mList);
		mListView.setAdapter(mListAdapter);
		citus_api.MV3D_ClearSearchMark();
	}
	
	private void setButton()
	{
		radioName = (RadioButton)findViewById(R.id.radioName);
		radioDist = (RadioButton)findViewById(R.id.radioDist);
		radioName.setChecked(true);
		radioName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(m_isSearchByDist == false) return;
				else {
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
				if(m_isSearchByDist) return;
				else {
					radioName.setChecked(false);
					radioDist.setChecked(true);
					m_isSearchByDist = true;
					listUpdate();
					mListAdapter.notifyDataSetInvalidated();
				}
			}
		});
		
		EditText txtSearch = (EditText)findViewById(R.id.txtStart);
		txtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() < 1)
				{
					if(mList == null)	mList = new ArrayList<NDB_RESULT>();
					else				mList.clear();
					mListAdapter.notifyDataSetInvalidated();
					return;
				}
				m_searchText = new String(s.toString());
				search();
				listUpdate();
				mListAdapter.notifyDataSetInvalidated();
			}
		});
		Button btnClear1 = (Button) findViewById(R.id.btnClear1);
		btnClear1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((TextView)findViewById(R.id.txtStart)).setText("");
				mList.clear();
				mListAdapter.notifyDataSetInvalidated();
			}
		});
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_NAME_LIST.this, MapViewActivity.class);
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
		
		Button btnRegionFilter = (Button)findViewById(R.id.btnRegionFilter);
		btnRegionFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_NAME_LIST.this, RegionFilter1.class);   
				startActivityForResult(intent, 0xFF0001);
				
			}
		});
		btnRegionFilter.setText(m_admin_filter_name);
		
		
	}
	
	public void search()
	{
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_KEYWORD);
		citus_api.NDB_Param_Reset();
		citus_api.NDB_Param_AddAdmin(m_admin_filter_idx);
		citus_api.NDB_Param_SetName(m_searchText.toUpperCase(), "");
		IPOINT carPos = citus_api.SYS_GetCarPos_ForSearch();
		citus_api.NDB_Param_SetRadius(carPos.x, carPos.y, 0);
		citus_api.NDB_SearchStart(citus_api.NDB_SEARCH_METHOD_BY_KEYWORD, 50, 11);
	}
	
	public void listUpdate()
	{
		if(mList == null)	mList = new ArrayList<NDB_RESULT>();
		else				mList.clear();
		
		int cnt = 0;
		
		// KindName
		mList.add(new NDB_RESULT("Kindname", citus_api.NDB_RES_NONE));
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_KIND_NAME);
		cnt = citus_api.NDB_Result_GetCount();
		NDB_RESULT res = new NDB_RESULT();
		for (int i = 0; i < cnt && i<10; i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			res.db_type = citus_api.NDB_RES_KIND_NAME;
			mList.add(new NDB_RESULT(res));
		}
		
		// POI
		mList.add(new NDB_RESULT("POI", citus_api.NDB_RES_NONE));
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_POI_BODY);
		cnt = citus_api.NDB_Result_GetCount();
		for (int i = 0; i < cnt; i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			res.db_type = citus_api.NDB_RES_POI_BODY;
			mList.add(new NDB_RESULT(res));
		}
		
		mList.add(new NDB_RESULT("Address", citus_api.NDB_RES_NONE));
		// Admin
		NDB_ADMIN_INFO ainfo = new NDB_ADMIN_INFO();
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_ADDR_ADMIN);
		cnt = citus_api.NDB_Result_GetCount();
		for (int i = 0; i < cnt && i<10; i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			res.db_type = citus_api.NDB_RES_ADDR_ADMIN;
			NDB_RESULT p = new NDB_RESULT(res); 
			
			citus_api.NDB_Admin_GetInfo(citus_api.NDB_RES_ADDR_ADMIN, p.admin_idx, ainfo);
			p.admin1 = ainfo.admin1_name;
			p.admin2 = ainfo.admin2_name;
			p.admin3 = ainfo.admin3_name;
			p.admin1_idx = ainfo.admin1_idx;
			p.admin2_idx = ainfo.admin2_idx;
			p.admin3_idx = ainfo.admin3_idx;
			
			if (p.admin_idx == p.admin3_idx)
				p.admin3 = "";
			else if (p.admin_idx == p.admin2_idx)
				p.admin2 = "";
			mList.add(p);
		}
		
		// Address
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_ADDR);
		cnt = citus_api.NDB_Result_GetCount();
		for (int i = 0; i < cnt; i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			res.db_type = citus_api.NDB_RES_ADDR;
			NDB_RESULT p = new NDB_RESULT(res); 
			
			citus_api.NDB_Admin_GetInfo(citus_api.NDB_RES_ADDR_ADMIN, p.admin_idx, ainfo);
			p.admin1 = ainfo.admin1_name;
			p.admin2 = ainfo.admin2_name;
			p.admin3 = ainfo.admin3_name;
			p.admin1_idx = ainfo.admin1_idx;
			p.admin2_idx = ainfo.admin2_idx;
			p.admin3_idx = ainfo.admin3_idx;
			mList.add(p);
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
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0xFF0001 && resultCode == 0xFF0002)
		{
//			intent.putExtra("admin_idx", data.getIntExtra("admin_idx", -1));
//			intent.putExtra("admin_name", data.getStringExtra("admin_name"));
			int new_idx = data.getIntExtra("admin_idx", -1);
			if (new_idx == m_admin_filter_idx) return;
			m_admin_filter_idx = new_idx;
			
			m_admin_filter_name = data.getStringExtra("admin_name");
			if (m_admin_filter_name == null)
			{
				m_admin_filter_idx = -1;
				m_admin_filter_name = "All";
			}
			
			Button btnRegionFilter = (Button)findViewById(R.id.btnRegionFilter);
			btnRegionFilter.setText(m_admin_filter_name);
			
//			m_searchText = new String(s.toString());
			if (m_searchText == null) return;
			if (m_searchText.length() < 1) return;
			
			search();
			listUpdate();
			mListAdapter.notifyDataSetInvalidated();
		}
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
				LayoutInflater vi = (LayoutInflater) DLG_SEARCH_NAME_LIST.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(mResource, null);
			}
			v.setMinimumWidth(((WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth());
			
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
		
		if(item.db_type == citus_api.NDB_RES_NONE) // Title
		{
			listText1.setText(item.name1);
			imgPOI.setVisibility(View.INVISIBLE);
			listBtn.setVisibility(View.INVISIBLE);
			listText2.setVisibility(View.INVISIBLE);
		}
		else
		{
			if(item.db_type == citus_api.NDB_RES_KIND_NAME)
			{
				//citus_api.setTextViewColorPartial(listText1, item.name1, m_searchText.toUpperCase(), 0xffff0000);
				listText1.setText(item.name1, TextView.BufferType.SPANNABLE);
		    	Spannable str = (Spannable) listText1.getText();
		    	int i = item.ch_seq;
		    	if(i>= 0)
		    		str.setSpan(new ForegroundColorSpan(0xffff0000), i, i+m_searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    	
				listText2.setVisibility(View.INVISIBLE);
				imgPOI.setVisibility(View.INVISIBLE);
				
			}
			else if(item.db_type == citus_api.NDB_RES_POI_BODY)
			{
				String txt1 = null;
				if(item.group_idx > 0)
					txt1 = new String(item.name1 + "(" + item.group_count + ")");
				else
					txt1 = new String(item.name1 + item.name2);
				listText1.setText(txt1);
				//citus_api.setTextViewColorPartial(listText1, item.name1, m_searchText.toUpperCase(), 0xffff0000);
				
		    	listText1.setText(txt1, TextView.BufferType.SPANNABLE);
		    	Spannable str = (Spannable) listText1.getText();
		    	int i = item.ch_seq;
		    	if(i>= 0)
		    		str.setSpan(new ForegroundColorSpan(0xffff0000), i, i+m_searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    	
				// POI Body ��������
				NDB_POI_BODY_INFO info = new NDB_POI_BODY_INFO();
				citus_api.NDB_Poi_GetBody(item.body_idx, info);
				WrapString admin_name = new WrapString();
				citus_api.NDB_Admin_GetName(citus_api.NDB_RES_POI, info.admin_idx, admin_name, 100);
				int brd_code = 0;
				//if(info.brd_idx > 0)
				{
					brd_code = citus_api.NDB_Kind_GetCode(info.brd_idx);
					listText2.setText("(brd:" + brd_code + ", "+item.distance+"m, dir="+item.angle+")" + admin_name.value + info.address);
				}
//				else
//					listText2.setText(admin_name.value + info.address);
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
				
			}
			else if(item.db_type == citus_api.NDB_RES_ADDR_ADMIN || item.db_type == citus_api.NDB_RES_ADDR)
			{
				imgPOI.setVisibility(View.INVISIBLE);
				
				String strName = "";
				int    st_idx[] = new int[20];
				int    ed_idx[] = new int[20];
				int    cnt = 0;
				if (item.addr_has_admin())
				{
					st_idx[cnt] = strName.length();
					ed_idx[cnt] = strName.length() + item.admin1.length();
					strName = strName + item.admin1;
					if (citus_api.NDB_Admin_IsInputed(item.db_type, 0, item.admin1_idx))
						cnt ++;
					
					if (item.admin2.length()>0)
					{
						st_idx[cnt] = strName.length();
						ed_idx[cnt] = strName.length() + item.admin2.length();
						if(ed_idx[cnt] < st_idx[cnt])
							ed_idx[cnt] = st_idx[cnt];
						strName = strName + item.admin2;
						if (citus_api.NDB_Admin_IsInputed(item.db_type, 1, item.admin2_idx))
							cnt ++;
					}
						
					if (item.admin3.length()>0 && item.addr_has_admin3())
					{
						st_idx[cnt] = strName.length();
						ed_idx[cnt] = strName.length() + item.admin3.length();
						if(ed_idx[cnt] < st_idx[cnt])
							ed_idx[cnt] = st_idx[cnt];
						strName = strName + item.admin3;
						if (citus_api.NDB_Admin_IsInputed(item.db_type, 2, item.admin3_idx))
							cnt ++;
					}
				}
				if (item.addr_has_houseno())
				{
					st_idx[cnt] = strName.length();
					ed_idx[cnt] = strName.length() + item.name1.length();
					
	                // 촌에 있는 도로명은 촌 + 도로명 분리해서 한번 더 들어있다.
					if(item.name1.length() > item.addr_search_info3)
					{
						st_idx[cnt] = strName.length() + (item.name1.length() - item.addr_search_info3);
						ed_idx[cnt] = strName.length() + item.name1.length();
					}
					if(ed_idx[cnt] < st_idx[cnt])
						ed_idx[cnt] = st_idx[cnt];
					strName = strName + item.name1;
					cnt ++;
					
					st_idx[cnt] = strName.length()+item.ch_seq;
					ed_idx[cnt] = strName.length()+item.ch_seq+ m_searchText.length() - item.get_total_input_length_without_last();
					if(ed_idx[cnt] < st_idx[cnt])
						ed_idx[cnt] = st_idx[cnt];
					strName = strName + item.name2;
					if (ed_idx[cnt] >= strName.length())
						ed_idx[cnt] = strName.length();
					cnt ++;
					
				}
				else
				{
					st_idx[cnt] = strName.length()+item.ch_seq;
					ed_idx[cnt] = strName.length()+item.ch_seq + m_searchText.length() - item.get_total_input_length_without_last();
					if(ed_idx[cnt] < st_idx[cnt])
						ed_idx[cnt] = st_idx[cnt];
					strName = strName + item.name1;
					cnt ++;					
				}
				
				//String strName = item.name1 + item.name2;
//				citus_api.setTextViewColorPartial(listText1, strName, m_searchText.toUpperCase(), 0xffff0000);
				listText1.setText(strName, TextView.BufferType.SPANNABLE);
		    	Spannable str = (Spannable) listText1.getText();
		    	int i;
		    	for(i=0;i<cnt;i++)
			    	str.setSpan(new ForegroundColorSpan(0xffff0000), st_idx[i], ed_idx[i], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    	//int i = item.ch_seq;
		    	//if(i>= 0)
		    	//	str.setSpan(new ForegroundColorSpan(0xffff0000), i, i+m_searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		    	
				WrapString wrapStr = new WrapString();
				int ret = citus_api.NDB_Admin_GetName(citus_api.NDB_RES_ADDR, item.admin_idx, wrapStr, 20);
				if(ret != 0)
				{
					listText2.setVisibility(View.VISIBLE);
					listText2.setText(wrapStr.value);
				}
				else
					listText2.setVisibility(View.INVISIBLE);
			}

			
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
	}
	
	private void viewDetail(int idx)
	{
		NDB_RESULT item = mList.get(idx);
		if(item.group_count > 0)
		{
			Intent intent = new Intent(DLG_SEARCH_NAME_LIST.this, DLG_SEARCH_GROUP_LIST.class);
			intent.putExtra("groupIdx", item.group_idx); 
			intent.putExtra("groupCnt", item.group_count);
			startActivity(intent);
		}
		else if(item.db_type == citus_api.NDB_RES_KIND_NAME)
		{
			Intent intent = new Intent(DLG_SEARCH_NAME_LIST.this, DLG_SEARCH_KIND_LIST.class);
			NDB_KIND_INFO kinfo = new NDB_KIND_INFO();
			citus_api.NDB_Kind_GetInfo(item.kind_idx, kinfo);
			intent.putExtra("kindcode", (int)(kinfo.kind_code));
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(DLG_SEARCH_NAME_LIST.this, DLG_VIEW_MAP_SEARCH.class);
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
}