package com.kingwaytek.naviking3d.app;

import kr.co.citus.engine.citus_api;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ScaleView extends View {

	public ScaleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ScaleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScaleView(Context context) {
		super(context);
		init();
	}
	
	private void init()
	{
		
		setWillNotDraw(false);
	}

	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		
double meter = citus_api.MV_Scr2MapWidth(canvas.getWidth());
	    
	    
	    int barMeter = 5;
	    if (meter < 50)
	    {
	        barMeter = 25;
	    }
	    else if (meter < 100.)
	    {
	        barMeter = 50;
	    }
	    else if (meter < 200)
	    {
	        barMeter = 100;
	    }
	    else if (meter < 500)
	    {
	        barMeter = 200;
	    }
	    else if (meter < 1000)
	    {
	        barMeter = 500;
	    }
	    else if (meter < 2000)
	    {
	        barMeter = 1000;
	    }
	    else if (meter < 5000)
	    {
	        barMeter = 2000;
	    }
	    else if (meter < 10000)
	    {
	        barMeter = 5000;
	    }
	    else if (meter < 20000)
	    {
	        barMeter = 10000;
	    }
	    else if (meter < 50000)
	    {
	        barMeter = 20000;
	    }
	    else if (meter < 100000)
	    {
	        barMeter = 50000;
	    }
	    else if (meter < 200000)
	    {
	        barMeter = 100000;
	    }
	    else if (meter < 500000)
	    {
	        barMeter = 200000;
	    }
	    else if (meter >= 500000)
	    {
	        barMeter = 500000;
	    }
	    
	    Paint rectPaint_fill = new Paint();
	    rectPaint_fill.setColor(Color.BLACK);
	    rectPaint_fill.setStrokeWidth(2.0f);
	    float blockWidth = (float)canvas.getWidth() / (float)((double)meter / (double)barMeter);
	    
	    float blockTail = blockWidth;
	    
	    boolean isFill = true;
	    int cnt = 1;
	    
	    int posBarY = canvas.getHeight()/2;
	    int heightBar = canvas.getHeight()/2;
	    
	    
	    rectPaint_fill.setTextAlign(Paint.Align.RIGHT);
	    rectPaint_fill.setTextSize(posBarY);
	    
	    RectF rect = new RectF();
	    while (blockTail < canvas.getWidth())
	    {
	    	
	    	rectPaint_fill.setStyle(Paint.Style.FILL);
	    	if (barMeter < 1000)
	    	{
	    		canvas.drawText((barMeter*cnt)+"m", blockTail, posBarY-2, rectPaint_fill);
	    	}
	    	else
	    	{
	    		canvas.drawText(((barMeter/1000)*cnt)+"km", blockTail, posBarY-2, rectPaint_fill);
	    	}
	    	
	    	
	    	rect.left = blockTail - blockWidth;
	    	rect.top = posBarY;
	    	rect.bottom = posBarY + heightBar;
	    	rect.right = blockTail;
	    	
	    	
	    	if (isFill)
	    	{
	    		rectPaint_fill.setStyle(Paint.Style.FILL);
	    		canvas.drawRect(rect, rectPaint_fill);
	    	}
	    	
	    	rectPaint_fill.setStyle(Paint.Style.STROKE);
	    	canvas.drawRect(rect, rectPaint_fill);
	    	
	    	isFill = !isFill;
	        blockTail += blockWidth;
	        if (blockTail > canvas.getWidth())
	        {
	            //rectBlock = CGRectMake(blockTail-blockWidth, 15, rect.size.width -(blockTail-blockWidth), 15);
	        	rect.left = blockTail - blockWidth;
		    	rect.top = posBarY;
		    	rect.bottom = posBarY + heightBar;
		    	rect.right = canvas.getWidth();
	            
	            if (isFill)
	            {
		    		rectPaint_fill.setStyle(Paint.Style.FILL);
		    		canvas.drawRect(rect, rectPaint_fill);

	            }
	        
	            rectPaint_fill.setStyle(Paint.Style.STROKE);
		    	canvas.drawRect(rect, rectPaint_fill);
	            
	            isFill = !isFill;
	            
	        }
	        cnt++;
	    }
	}

	@Override
	public void draw(Canvas canvas) {

		super.draw(canvas);
		
		invalidate();
	    /*double meter = citus_api.MV_Scr2MapWidth(canvas.getWidth());
	    
	    
	    int barMeter = 5;
	    if (meter < 20)
	    {
	        barMeter = 5;
	    }
	    else if (meter < 100.)
	    {
	        barMeter = 20;
	    }
	    else if (meter < 200)
	    {
	        barMeter = 100;
	    }
	    else if (meter < 500)
	    {
	        barMeter = 200;
	    }
	    else if (meter < 1000)
	    {
	        barMeter = 500;
	    }
	    else if (meter < 2000)
	    {
	        barMeter = 1000;
	    }
	    else if (meter < 5000)
	    {
	        barMeter = 2000;
	    }
	    else if (meter < 10000)
	    {
	        barMeter = 5000;
	    }
	    else if (meter < 20000)
	    {
	        barMeter = 10000;
	    }
	    else if (meter < 50000)
	    {
	        barMeter = 20000;
	    }
	    else if (meter < 100000)
	    {
	        barMeter = 50000;
	    }
	    else if (meter < 200000)
	    {
	        barMeter = 100000;
	    }
	    else if (meter < 500000)
	    {
	        barMeter = 200000;
	    }
	    else if (meter >= 500000)
	    {
	        barMeter = 500000;
	    }
	    
	    Paint rectPaint_fill = new Paint();
	    rectPaint_fill.setColor(Color.BLACK);
	    rectPaint_fill.setStrokeWidth(2.0f);
	    float blockWidth = (float)canvas.getWidth() / (float)((double)meter / (double)barMeter);
	    
	    float blockTail = blockWidth;
	    
	    boolean isFill = true;
	    int cnt = 1;
	    
	    int posBarX = canvas.getHeight()/2;
	    int heightBar = canvas.getHeight()/2;
	    
	    
	    RectF rect = new RectF();
	    while (blockTail < canvas.getWidth())
	    {
	    	rect.left = blockTail - blockWidth;
	    	rect.top = posBarX;
	    	rect.bottom = posBarX + heightBar;
	    	rect.right = blockTail;
	    	
	    	if (isFill)
	    	{
	    		rectPaint_fill.setStyle(Paint.Style.FILL);
	    		canvas.drawRect(rect, rectPaint_fill);
	    	}
	    	
	    	rectPaint_fill.setStyle(Paint.Style.STROKE);
	    	canvas.drawRect(rect, rectPaint_fill);
	    	
	    	isFill = !isFill;
	        blockTail += blockWidth;
	        if (blockTail > canvas.getWidth())
	        {
	            //rectBlock = CGRectMake(blockTail-blockWidth, 15, rect.size.width -(blockTail-blockWidth), 15);
	        	rect.left = blockTail - blockWidth;
		    	rect.top = posBarX;
		    	rect.bottom = posBarX + heightBar;
		    	rect.right = canvas.getWidth();
	            
	            if (isFill)
	            {
		    		rectPaint_fill.setStyle(Paint.Style.FILL);
		    		canvas.drawRect(rect, rectPaint_fill);

	            }
	        
	            rectPaint_fill.setStyle(Paint.Style.STROKE);
		    	canvas.drawRect(rect, rectPaint_fill);
	            
	            isFill = !isFill;
	            
	        }
	        cnt++;
	    }*/
	    		
	}

	
	
}
