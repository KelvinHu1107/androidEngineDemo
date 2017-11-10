package com.kingwaytek.naviking3d.app;

import java.util.ArrayList;

import kr.co.citus.engine.ApiProxy;
import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.RG_GUIDE_INFO;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RouteListActivity extends Activity {

	class RouteItem {
		public String roadName;
		public int dist;
		public int imgid;
		public int x;
		public int y;
		public int road_id;
		
		RouteItem() {
			dist = 0;
			x = 0;
			y = 0;
			road_id = 0;
		}
	}
	
	ListView m_listView = null;
	ListAdapter mListAdapter;
	ArrayList<RouteItem> mTBTDataArray = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route_list);
		
		m_listView = (ListView)findViewById(R.id.listViewTbt);
		buildTbtList();
		mListAdapter = new ListAdapter(this,R.layout.tbt_cell, mTBTDataArray);
		m_listView.setAdapter(mListAdapter);
		m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
				RouteItem item = mTBTDataArray.get(position);
				Intent intent = new Intent(RouteListActivity.this, RouteDetailActivity.class);
				intent.putExtra("posx", item.x);
				intent.putExtra("posy", item.y);
				intent.putExtra("road_id", item.road_id);
				
				startActivityForResult(intent, 0x0101);
		    }
		});
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0x0101 && resultCode == 1)
			finish(); // when setted avoid position.
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_list, menu);
		return true;
	}
	
	class ListAdapter extends ArrayAdapter<RouteItem> {
		int mResource;
		ArrayList<RouteItem> mList;
		Context mContext;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<RouteItem> objects) {
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
				view = inflater.inflate(R.layout.tbt_cell, null);
			} else {
				view = convertView;
			}
			
			setListText(view, position);
			return view;
		}
		
		void setListText(View v, int position)
		{
			RouteItem item = mList.get(position);
			
			TextView listText1 = (TextView) v.findViewById(R.id.tbtText1);
			TextView listText2 = (TextView) v.findViewById(R.id.tbtText2);
			ImageView img = (ImageView) v.findViewById(R.id.tbtImage);
			
			listText1.setText(item.roadName);
			
			String strDist = null;
			if (item.dist > 1000)
				strDist = String.format("%.1f Km", (double)item.dist / 1000.0);
			else
				strDist = String.format("%d m", item.dist);
			
			listText2.setText(strDist);
			img.setImageResource(item.imgid);
			
		}
	}
	
	void buildTbtList() {
		
		RG_GUIDE_INFO info = new RG_GUIDE_INFO();
		
		if (mTBTDataArray == null)
		{
			mTBTDataArray = new ArrayList<RouteItem>();
		}
		
		mTBTDataArray.clear();
		
		int nearCrossDist = 0;
		int npart = citus_api.RG_GetPartNum(-1);
		RouteItem item;
		for (int iii = npart-1; iii>= 0; --iii)
		{
			if (citus_api.RG_GetGuideInfo(iii, info, true, true)) 
			{
				item = null;
				if (info.targIdx == iii)
				{
					// get c_road_id and road speed limit
					
					Log.i("amh", "c_road_id: " + info.kwt_RoadId + " road speed: " + info.kwt_RoadSpeed);
					
					if (info.targDist < 0.) continue;
					int l_r_side = 0; // -1:left, 0:go_12, 1:right
					boolean l_r_turn = false;
					switch(info.snd_dir_code)
					{
					case citus_api.SND_DIR_RIGHT_1:
					case citus_api.SND_DIR_RIGHT_2:
					case citus_api.SND_DIR_RIGHT:
					case citus_api.SND_DIR_R_SIDE:
						l_r_side = 1; break;
						
					case citus_api.SND_DIR_RIGHT_4:
					case citus_api.SND_DIR_RIGHT_3:
					case citus_api.SND_DIR_RIGHT_5:
					case citus_api.SND_DIR_RTURN:
						l_r_turn = true;
						l_r_side = 1; break;
					case citus_api.SND_DIR_LEFT_11:
					case citus_api.SND_DIR_LEFT_10:
					case citus_api.SND_DIR_LEFT:
					case citus_api.SND_DIR_L_SIDE:
						l_r_side = -1; break;
					case citus_api.SND_DIR_LEFT_8:
					case citus_api.SND_DIR_LEFT_9:
					case citus_api.SND_DIR_LEFT_7:
					case citus_api.SND_DIR_LTURN:
						l_r_turn = true;
						l_r_side = -1; break;
					}				
					int imageId = 0;

					if (imageId == 0 && info.snd_info_code > 0) 
					{
						switch (info.snd_info_code) 
						{
						case citus_api.SND_INFO_TO_OVERPASS:		imageId = (l_r_side == 1 ? R.drawable.arrow_overpass_02:R.drawable.arrow_overpass_01);	break;
//						case citus_api.SND_INFO_NOT_TO_OVERPASS:	imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_INFO_TO_UNDERPASS:		
			            	switch(l_r_side)
			            	{
			            	case -1: imageId = R.drawable.arrow_underpass_01; break;
			            	case  1: imageId = R.drawable.arrow_underpass_02; break;
			            	default: imageId = R.drawable.arrow_underpass_00; break;
			            	}
			            	break;
//			            case citus_api.SND_INFO_NOT_TO_UNDERPASS:	imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_INFO_ENTER_HIWAY:
			            case citus_api.SND_INFO_ENTER_EXPRESS:
			            	switch(l_r_side)
			            	{
			            	case -1: imageId = l_r_turn ? R.drawable.arrow_enter_hi_05:R.drawable.arrow_enter_hi_02; break;
			            	case  0: imageId = R.drawable.arrow_enter_hi_00; break;
			            	default: imageId = l_r_turn ? R.drawable.arrow_enter_hi_04:R.drawable.arrow_enter_hi_01; break;
			            	}
			            	//imageId = R.drawable.turnpanel_sign_04; break;
			            	break;
			            case citus_api.SND_INFO_ENTER_IC:
			            	switch(l_r_side)
			            	{
			            	case -1: imageId = l_r_turn ? R.drawable.arrow_enter_hi_05:R.drawable.arrow_enter_hi_02; break;
			            	case  0: imageId = R.drawable.arrow_enter_hi_00; break;
			            	default: imageId = l_r_turn ? R.drawable.arrow_enter_hi_04:R.drawable.arrow_enter_hi_01; break;
			            	}
			            	//imageId = R.drawable.turnpanel_sign_04; break;
			            	break;
			            case citus_api.SND_INFO_ENTER_RAMP:
			            	switch(l_r_side)
			            	{
			            	case -1: imageId = l_r_turn ? R.drawable.arrow_enter_hi_05:R.drawable.arrow_enter_hi_02; break;
			            	case  0: imageId = R.drawable.arrow_enter_hi_00; break;
			            	default: imageId = l_r_turn ? R.drawable.arrow_enter_hi_04:R.drawable.arrow_enter_hi_01; break;
			            	}
			            	//imageId = R.drawable.turnpanel_sign_04; break;
			            	break;
			            case citus_api.SND_INFO_EXIT_HIWAY:
			            case citus_api.SND_INFO_EXIT_EXPRESS:
			            	switch(l_r_side)
			            	{
			            	case -1: imageId = l_r_turn ? R.drawable.arrow_exit_hi_05:R.drawable.arrow_exit_hi_02; break;
			            	case  0: imageId = R.drawable.arrow_exit_hi_00; break;
			            	default: imageId = l_r_turn ? R.drawable.arrow_exit_hi_04:R.drawable.arrow_exit_hi_01; break;
			            	}
			            	break;//imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_INFO_MID_OK:				imageId = R.drawable.turnpanel_sign_waypoint; break;
			            case citus_api.SND_INFO_DEST_OK:			imageId = R.drawable.turnpanel_sign_goal; break;
			            case citus_api.SND_INFO_TO_TUNNEL:			imageId = R.drawable.arrow_tunnel; break;
//			            case citus_api.SND_INFO_NOT_TO_TUNNEL:		imageId = R.drawable.turnpanel_sign_03; break;
			            case citus_api.SND_INFO_HIGHWAY_DIR_JC:
			            case citus_api.SND_INFO_HIGHWAY_DIR_IC: 	imageId = R.drawable.turnpanel_sign_04; break; // IC���占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 ��곤옙揶�占�
			            case citus_api.SND_INFO_LANE_FAST:			imageId = (l_r_side == 1 ? R.drawable.turnpanel_sign_10 : R.drawable.turnpanel_sign_11); break;
			            case citus_api.SND_INFO_LANE_SLOW:			imageId = (l_r_side == -1 ? R.drawable.turnpanel_sign_11 : R.drawable.turnpanel_sign_10); break;
			            case citus_api.SND_INFO_ENTER_BRIDGE:		imageId = R.drawable.arrow_overpass_01; break;
			            case citus_api.SND_INFO_ENTER_ROTARI:
			            	switch(info.snd_dir_code)
			            	{
			            	case citus_api.SND_DIR_ROUNDABOUT_1: imageId = R.drawable.arrow_circle_02; break;
			            	case citus_api.SND_DIR_ROUNDABOUT_3: imageId = R.drawable.arrow_circle_03; break;
			            	case citus_api.SND_DIR_ROUNDABOUT_5: imageId = R.drawable.arrow_circle_04; break;
			            	case citus_api.SND_DIR_ROUNDABOUT_6: imageId = R.drawable.arrow_circle_05; break;
			            	case citus_api.SND_DIR_ROUNDABOUT_8: imageId = R.drawable.arrow_circle_06; break;
			            	case citus_api.SND_DIR_ROUNDABOUT_9: imageId = R.drawable.arrow_circle_07; break;
			            	case citus_api.SND_DIR_ROUNDABOUT_11: imageId = R.drawable.arrow_circle_08; break;
			            	default: imageId = R.drawable.arrow_circle_01; break;			            	
			            	}
			            	break;
			            default:		                			imageId = R.drawable.turnpanel_sign_03; break;
						}
					} 
					if (imageId == 0 && info.snd_dir_code > 0) {
						switch (info.snd_dir_code) {
						case citus_api.SND_DIR_GO_12:
			            case citus_api.SND_DIR_STRAIGHT:	imageId = R.drawable.turnpanel_sign_03; break;
			            case citus_api.SND_DIR_LEFT_7:		imageId = R.drawable.turnpanel_sign_01; break;
			            case citus_api.SND_DIR_LEFT_8:		imageId = R.drawable.turnpanel_sign_01; break;
			            case citus_api.SND_DIR_LEFT_9:		imageId = R.drawable.turnpanel_sign_01; break;
			            case citus_api.SND_DIR_LTURN:		imageId = R.drawable.turnpanel_sign_01; break;
			            case citus_api.SND_DIR_RIGHT_4:		imageId = R.drawable.turnpanel_sign_05; break;
			            case citus_api.SND_DIR_RIGHT_3:		imageId = R.drawable.turnpanel_sign_05; break;
			            case citus_api.SND_DIR_RIGHT_5:		imageId = R.drawable.turnpanel_sign_05; break;
			            case citus_api.SND_DIR_RTURN:		imageId = R.drawable.turnpanel_sign_05; break;
			            case citus_api.SND_DIR_LEFT_11:		imageId = R.drawable.turnpanel_sign_02; break;
			            case citus_api.SND_DIR_LEFT_10:		imageId = R.drawable.turnpanel_sign_02; break;
			            case citus_api.SND_DIR_LEFT:		imageId = R.drawable.turnpanel_sign_02; break;
			            case citus_api.SND_DIR_RIGHT_1:		imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_DIR_RIGHT_2:		imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_DIR_RIGHT:		imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_DIR_UTURN:		imageId = R.drawable.turnpanel_sign_06; break;
			            case citus_api.SND_DIR_PTURN:		imageId = R.drawable.turnpanel_sign_04; break;
			            case citus_api.SND_DIR_ACT_AFTER_RTURN:
			            case citus_api.SND_DIR_RACT_AFTER_RTURN:
			            case citus_api.SND_DIR_LACT_AFTER_RTURN:	imageId = R.drawable.turnpanel_sign_05; break;
			            case citus_api.SND_DIR_ACT_AFTER_LTURN:
			            case citus_api.SND_DIR_RACT_AFTER_LTURN:
			            case citus_api.SND_DIR_LACT_AFTER_LTURN:	imageId = R.drawable.turnpanel_sign_01; break;
			            case citus_api.SND_DIR_L_SIDE:				imageId = R.drawable.turnpanel_sign_02; break;
			            case citus_api.SND_DIR_R_SIDE:				imageId = R.drawable.turnpanel_sign_04; break;
			            default:		                			imageId = R.drawable.turnpanel_sign_03; break;
						}
					} else if (imageId == 0) {
						imageId = R.drawable.turnpanel_sign_03;
					}
					item = new RouteItem();
					item.imgid = imageId;
					RG_GUIDE_INFO next = new RG_GUIDE_INFO();
					
					item.road_id = info.kwt_RoadId;
					if (info.targIdx > 0)
					{
						citus_api.RG_GetGuideInfo(info.targIdx-1, next, false, true);
						item.road_id = next.kwt_RoadId;
					}
					
					item.roadName = next.rname;//info.rname;			
					item.x = info.targX;
					item.y = info.targY;
					
				}
				else if (info.cpDirName != null && info.kwt_kind <= 0)
				{
					if (info.cpDirName.length()>0)
					{
						item = new RouteItem();
						item.imgid = R.drawable.turnpanel_sign_03;
						
						item.roadName = info.cpDirName;//info.rname;			
						item.x = info.nodeX;
						item.y = info.nodeY;
					
						item.road_id = info.kwt_RoadId;
						RG_GUIDE_INFO next = new RG_GUIDE_INFO();
						if (info.targIdx > 0)
						{
							citus_api.RG_GetGuideInfo(info.targIdx-1, next, false, true);
							item.road_id = next.kwt_RoadId;
						}
					}
				}
				
				if (item != null)
				{
					// turn distance

					nearCrossDist = ApiProxy.getInteger(ApiProxy.RG_TOTAL_DIST, 0) 
							- (int)citus_api.RG_GetPartLength(iii) 
							- ApiProxy.getInteger(ApiProxy.RG_PAST_DIST, 0);
					
					item.dist = nearCrossDist;
					if ((info.snd_info_code == citus_api.SND_INFO_DEST_OK) && (item.roadName == null || item.roadName.length() == 0))
						item.roadName = "目的地";
					if ((info.snd_info_code == citus_api.SND_INFO_MID_OK) && (item.roadName == null || item.roadName.length() == 0))
						item.roadName = "經由地";

					if (item.roadName == null || item.roadName.length() == 0)
						item.roadName = "一般道路";
					mTBTDataArray.add(item);
//					iii = info.targIdx;
				}
			}	
		}
	}

}
