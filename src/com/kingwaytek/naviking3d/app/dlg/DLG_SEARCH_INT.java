package com.kingwaytek.naviking3d.app.dlg;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

import com.kingwaytek.naviking3d.app.MapViewActivity;
import com.kingwaytek.naviking3d.app.R;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.IPOINT;
import kr.co.citus.engine.struct.NDB_RESULT;
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
import android.widget.TextView;

public class DLG_SEARCH_INT extends Activity
{
	ListAdapter mListAdapter;
	
	ListView mListView = null;
	ArrayList<NDB_RESULT> mList = null;
	public String m_searchText = null;
	public String[] txtSetTarget = {"bopomopo", "zhuyin"};
	TextView txtSearch1;
	TextView txtSearch2;
	
	String strSearch1 = "", strSearch2 = "";
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_int);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		Button btnSetStart = (Button)findViewById(R.id.btnSetStart);
		
		// add 2014 1. 13. by dckim
		txtSearch1 = (TextView)findViewById(R.id.txtStart);
		txtSearch2 = (TextView)findViewById(R.id.txtDest);
		
		txtSearch1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				citus_search();
			}
		});
		
		txtSearch2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				citus_search();
			}
		});
		
		// add end
		
		btnSetStart.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(DLG_SEARCH_INT.this)
				.setTitle("�� �˻�")
				.setItems(txtSetTarget, 
					new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						EditText txtStart = (EditText) findViewById(R.id.txtStart);
						txtStart.setText(new String(""));
						EditText txtDest = (EditText) findViewById(R.id.txtDest);
						txtDest.setText(new String(""));
						
					}})
				.show();
			}});
		
		Button btnClear1 = (Button) findViewById(R.id.btnClear1);
		btnClear1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((TextView)findViewById(R.id.txtStart)).setText("");
			}
		});
		Button btnClear2 = (Button) findViewById(R.id.btnClear2);
		btnClear2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((TextView)findViewById(R.id.txtDest)).setText("");
			}
		});
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String txt1 = new String(((TextView)findViewById(R.id.txtStart)).getText().toString().toUpperCase());
				String txt2 = new String(((TextView)findViewById(R.id.txtDest)).getText().toString().toUpperCase());
				if(txt1.length() > 0 && txt2.length() > 0)
				{
					Intent intent = new Intent(DLG_SEARCH_INT.this, DLG_SEARCH_INT_LIST.class);
					intent.putExtra("searchTxt1", txt1);
					intent.putExtra("searchTxt2", txt2);
					startActivity(intent);
				}
				else
					return;
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
				Intent intent = new Intent(DLG_SEARCH_INT.this, MapViewActivity.class);
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
	
	private void citus_search()
	{
		// 1) Open db table
		citus_api.NDB_Open(citus_api.NDB_SEARCH_METHOD_BY_INTERS);
		
		// 2) Clear result buffer
		citus_api.NDB_Clear();
		
		// 3) Set search methods
		citus_api.NDB_Param_Reset();
		
		citus_api.NDB_Param_SetName(txtSearch1.getText().toString().toUpperCase(), txtSearch2.getText().toString().toUpperCase());
		
		// 4) set base location for distance
		IPOINT carpos = citus_api.SYS_GetCarPos_ForSearch();
		citus_api.NDB_Param_SetRadius(carpos.x, carpos.y, 0);
		
		// 5) item count per DB_SearchStart() / DB_SearchNext()
		
		// 6) search start
		citus_api.NDB_SearchStart(citus_api.NDB_SEARCH_METHOD_BY_INTERS, 20, 2);
	
		citus_updateList();
	}
	
	private void citus_updateList()
	{
		if(mList == null) mList = new ArrayList<NDB_RESULT>();
		else mList.clear();
		citus_api.NDB_Result_SetType(citus_api.NDB_RES_INTERS);
		
		int cnt = citus_api.NDB_Result_GetCount();
		NDB_RESULT res = new NDB_RESULT();
		
		for(int i=0;i<cnt;i++)
		{
			citus_api.NDB_Result_GetItem(i, res);
			res.db_type = citus_api.NDB_RES_INTERS;
			mList.add(new NDB_RESULT(res));
		}	
		
		mListAdapter.notifyDataSetInvalidated();
	}
	
	private boolean alert(String _msg)
	{
		new AlertDialog.Builder(DLG_SEARCH_INT.this)
		.setTitle("�˸�")
	    .setMessage(_msg)
		.setPositiveButton("Ȯ��", null)
		.show();
		return false;
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

		public ListAdapter(Context context, int textViewResourceId, ArrayList<NDB_RESULT> objects) {
			super(context, textViewResourceId, objects);
			mResource = textViewResourceId;
			mList = objects;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) DLG_SEARCH_INT.this
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
		TextView listText1 = (TextView) v.findViewById(R.id.txtListText1);
		TextView listText2 = (TextView) v.findViewById(R.id.txtListText2);
		ImageView imgPOI = (ImageView) v.findViewById(R.id.imgPOI);
		Button listBtn = (Button) v.findViewById(R.id.btnListButton1);
		
		NDB_RESULT item = mList.get(position);
		WrapString admin = new WrapString();
		citus_api.NDB_Admin_GetName(citus_api.NDB_RES_INTERS, item.admin_idx, admin, 100);
		listText1.setTextSize(14);
//		listText1.setText(item.name1 + " - " + item.name2);
		String itemName = item.name1 + " - " + item.name2;
		listText1.setText(itemName, TextView.BufferType.SPANNABLE);
    	Spannable str = (Spannable) listText1.getText();
    	int i = item.ch_seq;
    	if(txtSearch2.getText().length() > 0 && txtSearch1.getText().length() > 0)
    		str.setSpan(new ForegroundColorSpan(0xffff0000), item.name1.length()+3, item.name1.length()+3+txtSearch2.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	if(txtSearch1.getText().length() > 0)
    		str.setSpan(new ForegroundColorSpan(0xffff0000), 0, txtSearch1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	
		imgPOI.setVisibility(View.INVISIBLE);
		listText2.setVisibility(View.VISIBLE);
		listText2.setText(admin.value+"["+item.distance+"m][dir="+item.angle+"]");
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
		Intent intent = new Intent(DLG_SEARCH_INT.this, DLG_VIEW_MAP_SEARCH.class);
		citus_api.g_res_item = new NDB_RESULT(item);
		startActivity(intent);
	}
}
