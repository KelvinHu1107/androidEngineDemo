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

public class DLG_SEARCH_TEL_LIST extends Activity
{
	ListAdapter mListAdapter;
	
	public boolean m_isSearchByKeyword 	= false;
	public boolean m_isSearchByDist 	= false;
	public String m_searchText = null;
	
	ListView mListView = null;
	ArrayList<NDB_RESULT> mList = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_list);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		EditText txtSearch = (EditText)findViewById(R.id.txtStart);
		txtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() < 1)
					return;
				m_searchText = new String(s.toString());
				//dbOpenNSearch(citus_api.DB_TYPE_TEL);
				search();
				listUpdate_new();
				mListAdapter.notifyDataSetInvalidated();
			}
		});
		Button btnClear1 = (Button) findViewById(R.id.btnClear1);
		btnClear1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((TextView)findViewById(R.id.txtStart)).setText("");
			}
		});
		
		mListView = (ListView) findViewById(R.id.listView1);
		Intent intent = getIntent();
		m_searchText = intent.getStringExtra("searchText");
		mList = new ArrayList<NDB_RESULT>();
		mListAdapter = new ListAdapter(this,R.layout.row_search_list, mList);
		
		mListView.setAdapter(mListAdapter);
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DLG_SEARCH_TEL_LIST.this, MapViewActivity.class);
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
		new AlertDialog.Builder(DLG_SEARCH_TEL_LIST.this)
		.setTitle("�˸�")
	    .setMessage(_msg)
		.setPositiveButton("Ȯ��", null)
		.show();
		return false;
	}
	
	public void search()
	{
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_TEL);
		citus_api.NDB_Param_Reset();
		citus_api.NDB_Param_SetName(m_searchText.toUpperCase(), "");
		IPOINT carPos = citus_api.SYS_GetCarPos_ForSearch();
		citus_api.NDB_Param_SetRadius(carPos.x, carPos.y, 0);
		citus_api.NDB_SearchStart(citus_api.NDB_SEARCH_METHOD_BY_TEL, 10, 0);
	}
	
	public void listUpdate_new()
	{
		if(mList == null)	mList = new ArrayList<NDB_RESULT>();
		else				mList.clear();
		int len = m_searchText.length();
		
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_TEL);
		int cnt = citus_api.NDB_Result_GetCount();
		NDB_RESULT res = new NDB_RESULT();
		for (int i = 0; i < cnt; i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			NDB_POI_BODY_INFO pinfo = new NDB_POI_BODY_INFO();
			citus_api.NDB_Poi_GetBody(res.body_idx, pinfo);
//			WrapString st_ch = new WrapString();
//			WrapString ed_ch = new WrapString();
			//citus_api.NDB_Reult_StringMark(pinfo.tel, res.ch_seq, len, st_ch, ed_ch);
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
				LayoutInflater vi = (LayoutInflater) DLG_SEARCH_TEL_LIST.this
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
		
//		citus_api.setTextViewColorPartial(listText1, item.name2, m_searchText.toUpperCase(), 0xffff0000);
		listText1.setText(item.name2);
		
		// POI Body ��������
		NDB_POI_BODY_INFO info = new NDB_POI_BODY_INFO();
		citus_api.NDB_Poi_GetBody(item.body_idx, info);
		
		listText2.setText(item.name1 + "(brd:" + citus_api.NDB_Kind_GetCode(info.brd_idx) + ", "+item.distance+"m, dir="+item.angle+")"+"(" + info.address + ")", TextView.BufferType.SPANNABLE);
		imgPOI.setVisibility(View.VISIBLE);
		listText2.setVisibility(View.VISIBLE);
		
		
		Spannable str = (Spannable) listText2.getText();
		WrapInt ch_st = new WrapInt();
		WrapInt ch_ed = new WrapInt();
		citus_api.NDB_Reult_StringMark(item.name1, 0, m_searchText.length(), ch_st, ch_ed);
				
//    	int i = item.name1.indexOf(m_searchText.toUpperCase());
//    	if(i>= 0)
    	{
        	str.setSpan(new ForegroundColorSpan(0xffff0000), ch_st.value, ch_ed.value, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);    		
    	}
    	
    	
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
		Intent intent = new Intent(DLG_SEARCH_TEL_LIST.this, DLG_VIEW_MAP_SEARCH.class);
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