package kr.co.citus.engine.struct;

public class TMC_ROUTE_INFO 
{
	public int 		part_idx;		// route part index
	public int		xpos, ypos;		// coordinate on the map
	public int		dist_from_start;	// distance from start location
	public int		dist_in_part_link;	// distance from start vertex of link
	
	public TMC_ROUTE_INFO()
	{
		init();
	}
	
	void init()
	{
		part_idx = -1;
		xpos = ypos = 0;
		dist_from_start = 0;
		dist_in_part_link = 0;
	}
}
