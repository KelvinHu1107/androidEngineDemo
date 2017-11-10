package kr.co.citus.engine.struct;

public class MM_IPC_CAND_INFO {

	public int x=0,y=0;			// position of the road
	public int zlevel = 0;		// level 
	public int mesh_idx = 0;	// mesh index
	public int link_idx = 0;	// link index
	public int se = 0;			// direction 1:se, 0:es
	public int is_curr = 0;		// this is current road
	public int is_fast_lane = 0; // this road is fast lane
	public int is_slow_lane = 0; // this road is slow lane
	public int side = 0;		// side from car location, -1:left, 1:right
	public String roadname = "";
	
	public MM_IPC_CAND_INFO()	{ init(); }
	public void init()
	{
		x = 0;
		y = 0;
		zlevel = 0;
		mesh_idx = 0;
		link_idx = 0;
		se = 0;
		is_curr = 0;
		is_fast_lane =0;
		is_slow_lane =0;
		side =0;
		roadname = "";
	}
	
}
