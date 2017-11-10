package kr.co.citus.engine.struct;

public class ROUTE_ITEM 
{
	public int 		part_idx;
	public int		xpos, ypos;
	public int		dist_from_start;
	
	public String 	next_road_name;
	public int		image_id;
	public int		road_id;
	public int 		group_count; // for highway info
	public int		idx;
	
	public ROUTE_ITEM()
	{
		init();
	}
	
	void init()
	{
		part_idx = -1;
		xpos = ypos = 0;
		dist_from_start = 0;
		next_road_name = "";
		image_id = -1;
		road_id = 0;
		group_count = 0;
		idx = 0;
	}
}
