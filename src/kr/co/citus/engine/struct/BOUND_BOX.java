package kr.co.citus.engine.struct;

public class BOUND_BOX {
	public int xmin, ymin;
	public int xmax, ymax;

	public BOUND_BOX(int _xmin, int _ymin, int _xmax, int _ymax) {xmin=_xmin;ymin=_ymin;xmax=_xmax;ymax=_ymax;}
	public BOUND_BOX()	{ init(); }
	
	void init()
	{
		xmin = ymin = xmax = ymax = 0;
	}
}
