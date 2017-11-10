package kr.co.citus.engine;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.citus.engine.struct.ROUTE_ITEM;
import kr.co.citus.engine.struct.TMC_INFO;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TBTGraph extends View {

	public boolean isHiwayMode = false;
	public ArrayList<ROUTE_ITEM> mTBTDataArray = null;
	public TBTGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TBTGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TBTGraph(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		
		if (mTBTDataArray == null) return;
		int count  = mTBTDataArray.size();
		if (getHeight() == 0 || count < 2) return;
		
		int cellHeight = getHeight() / (count-1);
		int itemCount = count;
		
		// Draw Color Line
		Paint linePaint = new Paint();
		int ypos = 0;
		boolean isMode2 = false;
		
		int totalRemainDist;
		if (isMode2 || isHiwayMode)
		{
			ROUTE_ITEM last = mTBTDataArray.get(mTBTDataArray.size()-1);
			totalRemainDist = last.dist_from_start - citus_api.RG_GetPastDist();
		}
		else
			totalRemainDist = getHeight();
		
		double ZeroPos = 0;
		for (int iii=1; iii<itemCount; iii++)
		{
			
			double dist1, dist2;
			if (iii == 0)
			{
				dist1 = 0;
			}
			else if (isMode2 || isHiwayMode)
				dist1 = (double)mTBTDataArray.get(iii-1).dist_from_start - citus_api.RG_GetPastDist();
			else
				dist1 = (iii-1)*(totalRemainDist/(mTBTDataArray.size()-1));
			
			if (iii==1 && !isHiwayMode)
			{
				if (isMode2)
				{
					ZeroPos = dist1;
					totalRemainDist -= ZeroPos;
				}
				else
				{
					ZeroPos = (totalRemainDist/(mTBTDataArray.size()-1)) / 2;
				}
			}
			if (isHiwayMode && iii==1)
				dist1 = 0;
			
			if (isMode2 || isHiwayMode)
				dist2 = (double)mTBTDataArray.get(iii).dist_from_start - citus_api.RG_GetPastDist();
			else
				dist2 = (iii)*(totalRemainDist/(mTBTDataArray.size()-1));
			
			dist1 -= ZeroPos;
			dist2 -= ZeroPos;

			int roadId = mTBTDataArray.get(iii).road_id;
			int roadSpd = 0;
			/*
			if (ReceiveTMCInfo.tmcInfo != null && ReceiveTMCInfo.tmcInfo.viewRoadSpeed != null && roadId != 0)
			{
				for (int kkk = 0; kkk < ReceiveTMCInfo.tmcInfo.viewRoadSpeed.size(); kkk++)
				{
					TMC_INFO.View_RoadSpeed spdItem = ReceiveTMCInfo.tmcInfo.viewRoadSpeed.get(kkk);
					if (roadId == spdItem.RoadID)
					{
						roadSpd = spdItem.Speed;
						break;
					}
				}
			}
			*/
			
			linePaint.setStrokeWidth(10);
			linePaint.setAntiAlias(true);
			
			if (roadSpd == 0)
			{
				linePaint.setColor(Color.argb(255, 128, 128, 128));
			}
			else if (roadSpd >= 60)
			{
				linePaint.setColor(Color.argb(255, 0, 255, 0));
			}
			else if (roadSpd > 30)
			{
				linePaint.setColor(Color.argb(255, 255, 255, 0));
			}else if (roadSpd > 10)
			{
				linePaint.setColor(Color.argb(255, 255, 128, 0));
			}else
			{
				linePaint.setColor(Color.argb(255, 255, 0, 0));
			}
//			switch(iii%4)
//			{
//			case 0:
//				linePaint.setColor(Color.argb(255, 255, 0, 0));
//				break;
//			case 1:
//				linePaint.setColor(Color.argb(255, 255, 128, 0));
//				break;
//			case 2:
//				linePaint.setColor(Color.argb(255, 255, 255, 0));
//				break;
//			case 3:
//				linePaint.setColor(Color.argb(255, 0, 255, 0));
//			default:
//				linePaint.setColor(Color.argb(255, 0, 255, 0));
//				break;
//
//			}
//			linePaint.setColor(Color.DKGRAY);
			
			canvas.drawLine(getWidth()/2, (int)(getHeight()*dist1/totalRemainDist), getWidth()/2, (int)(getHeight()*dist2/totalRemainDist), linePaint);
			
		}
		
		// Draw Point Circle
		Paint circlePaint = new Paint();
		ypos = 0;
		for (int iii=1; iii<itemCount; iii++)
		{
			if (isMode2 || isHiwayMode)
				ypos = (int) (getHeight() * (mTBTDataArray.get(iii).dist_from_start - citus_api.RG_GetPastDist()-ZeroPos) / totalRemainDist);
			else
				ypos = (int) (getHeight() * ((iii)*(totalRemainDist/(mTBTDataArray.size()-1))-ZeroPos) / totalRemainDist);

			circlePaint.setStrokeWidth(5);
			circlePaint.setAntiAlias(true);
			circlePaint.setColor(Color.WHITE);
			circlePaint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(getWidth()/2, ypos, getWidth()/2-2, circlePaint);
			canvas.drawLine(getWidth(), ((iii-1)*cellHeight)+(cellHeight/2), getWidth()/2, ypos, circlePaint);
			circlePaint.setColor(Color.DKGRAY);
			circlePaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(getWidth()/2, ypos, getWidth()/2-2, circlePaint);
		}
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
	

}
