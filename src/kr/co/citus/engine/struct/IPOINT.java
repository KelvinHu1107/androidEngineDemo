package kr.co.citus.engine.struct;

public class IPOINT {
	public int x;
	public int y;

	public IPOINT(int _x, int _y) {x=_x;y=_y;}
	public IPOINT()	{ init(); }
	
	void init()
	{
		x = 0;
		y = 0;
	}
}
