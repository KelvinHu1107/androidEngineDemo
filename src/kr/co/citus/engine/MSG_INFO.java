package kr.co.citus.engine;


public class MSG_INFO 
{
	public int msg, param1, param2, param3;
	boolean redraw;
	public MSG_INFO(int _msg, int _param1, int _param2, int _param3) 
	{
		msg = _msg;
		param1 = _param1;
		param2 = _param2;
		param3 = _param3;
		
	}
	
	public MSG_INFO(int _msg, int _param1, int _param2, int _param3, boolean need_redraw_screen) 
	{
		msg = _msg;
		param1 = _param1;
		param2 = _param2;
		param3 = _param3;
		redraw = need_redraw_screen;
		
	}

}

