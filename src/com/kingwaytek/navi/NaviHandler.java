package com.kingwaytek.navi;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.BOUND_BOX;
import android.os.Handler;
import android.os.Message;

public class NaviHandler extends Handler {

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		int v = msg.what ; 
		switch(v)
    	{
    	case citus_api.ENGINE_CALLBACK_ROUTE_DELETE:
    		break;
    	
    	// ROUTE PLAN
    	case citus_api.ENGINE_CALLBACK_ROUTE_START:
    		break;
    	case citus_api.ENGINE_CALLBACK_ROUTE_END:
    		break;
    	// RE-ROUTE PLAN
    	case citus_api.ENGINE_CALLBACK_RE_ROUTE_START:
    		break;
    	case citus_api.ENGINE_CALLBACK_RE_ROUTE_DONE:
    		break;
    	// SIMULATION
    	case citus_api.ENGINE_CALLBACK_SIMULATION_START:
    		break;
    	case citus_api.ENGINE_CALLBACK_SIMULATION_END:
    		break;
    	// TOUCH DOEN
    	case citus_api.ENGINE_CALLBACK_TOUCH_DOWN_END:
    		break;
    	case citus_api.ENGINE_CALLBACK_TOUCH_DOWN_VIA:
    		break;
    	}
    	System.out.println("Called");
	}
}