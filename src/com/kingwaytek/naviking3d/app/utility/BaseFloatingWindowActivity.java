package com.kingwaytek.naviking3d.app.utility;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Timer;


/**
 * Created by kelvinhu1107 on 2017/11/13.
 */

public abstract class BaseFloatingWindowActivity extends Activity {

    final static String TAG = "BaseFloatingWindow";
    final static int OVERLAY_PERMISSION_REQ_CODE = 99;
    final static int REQUEST_CODE_FINE_GPS = 100;
    private WindowManager mWindowManager;
    private LinearLayout currentSpeedLayout, eventLayout;
    private Button closeBtn, activateBtn;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private TextView currentSpeedTv, speedLimitTv, latitudeTv, longitudeTv, eventTv;
    LinearLayout mLayCam = null;
    TextView mCamDist = null;
    TextView mCamSpeed = null;
    ImageView mCamImage = null, mDigitalImage100, mDigitalImage10, mDigitalImage1;

    public abstract int getLayoutResId();

    public abstract Drawable getCurrentSpeedResId();

    public abstract Drawable getEventWindowResId();

    public abstract Drawable getCloseBtnResId();

    public abstract int getActivateBtnId();

    public void setCamSpeed(String string){
        if(string != null) {
            mCamSpeed.setText(string);
        }
        else{
            mCamSpeed.setVisibility(View.GONE);
        }
    }

    public void setCamImage(int drawable){

        mCamImage.setImageResource(drawable);
    }

    public void setCamDist(String string){
        if(string != null) {
            mCamDist.setText(string);
        }
        else{
            mCamDist.setVisibility(View.GONE);
        }
    }

    public void removeSpeedImage(){
        mCamImage.setVisibility(View.GONE);
        mCamSpeed.setVisibility(View.VISIBLE);
    }

    public void revealSpeedImage(){
        mCamImage.setVisibility(View.VISIBLE);
        mCamSpeed.setVisibility(View.GONE);
    }

    public void removeCamLayout(){
        if(mCamImage!= null)
        mCamImage.setVisibility(View.GONE);

        if(mCamDist!= null)
            mCamDist.setVisibility(View.GONE);

        if(mCamSpeed!= null)
            mCamSpeed.setVisibility(View.GONE);
    }

    public void revealCamLayout(){
        if(mCamImage!= null)
            mCamImage.setVisibility(View.VISIBLE);

        if(mCamDist!= null)
            mCamDist.setVisibility(View.VISIBLE);

        if(mCamSpeed!= null)
            mCamSpeed.setVisibility(View.VISIBLE);
    }

    public void setDigRes100(int res){
        if(mDigitalImage100!=null){
            mDigitalImage100.setBackgroundResource(res);
        }
    }

    public void setDigRes10(int res){
        if(mDigitalImage10!=null){
            mDigitalImage10.setBackgroundResource(res);
        }
    }

    public void setDigRes1(int res){
        if(mDigitalImage1!=null){
            mDigitalImage1.setBackgroundResource(res);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(getLayoutResId());
        //findViews();
        //checkPermission();
    }

    private void setClickListener(final WindowManager.LayoutParams paramsCurrentSpeed, final WindowManager.LayoutParams paramsEvent, final WindowManager.LayoutParams paramsSpeedLimit){
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.removeViewImmediate(currentSpeedLayout);
                mWindowManager.removeViewImmediate(mLayCam);
                //mWindowManager.removeViewImmediate(eventLayout);
                mWindowManager.removeViewImmediate(closeBtn);
                mWindowManager = null;
            }
        });

        currentSpeedLayout.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParams = paramsCurrentSpeed;
            int x,y;
            float touchX,touchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x= updatedParams.x;
                        y=updatedParams.y;
                        touchX = motionEvent.getRawX();
                        touchY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatedParams.x = (int)(x+(motionEvent.getRawX() - touchX));
                        updatedParams.y = (int)(y+(motionEvent.getRawY() - touchY));
                        mWindowManager.updateViewLayout(currentSpeedLayout,updatedParams);
                        break;
                    default:break;
                }
                return false;
            }
        });

        mLayCam.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParams = paramsSpeedLimit;
            int x,y;
            float touchX,touchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x= updatedParams.x;
                        y=updatedParams.y;
                        touchX = motionEvent.getRawX();
                        touchY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatedParams.x = (int)(x+(motionEvent.getRawX() - touchX));
                        updatedParams.y = (int)(y+(motionEvent.getRawY() - touchY));
                        mWindowManager.updateViewLayout(mLayCam,updatedParams);
                        break;
                    default:break;
                }
                return false;
            }
        });
    }

    public void findViews(Button activateBtn){

        if(activateBtn!= null) {
            activateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initComponent();
                }
            });
        }
    }

//    private void checkPermission(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(BaseFloatingWindowActivity.this)) {
//                Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BaseFloatingWindowActivity.this.getPackageName()));
//                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
//            } else {
//                activateBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(mWindowManager == null) {
//                            initComponent();
//                        }
//                    }
//                });
//            }
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_CODE_FINE_GPS);
//        } else {
//            initGps();
//        }
//    }

//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == OVERLAY_PERMISSION_REQ_CODE){
//            if (!Settings.canDrawOverlays(this)) {
//                Toast.makeText(this, "Permission is denied by user. Please Check it in Settings", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permission Allowed", Toast.LENGTH_SHORT).show();
//                activateBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        initComponent();
//                    }
//                });
//            }
//        }
//        if (requestCode == REQUEST_CODE_FINE_GPS) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "REQUEST_CODE_FINE_GPS failed");
//            }
//            else {
//                initGps();
//            }
//        }
//    }

    @SuppressLint("MissingPermission")
    private void initGps() {
        mLocationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(currentSpeedTv != null) {
                    currentSpeedTv.setText(String.valueOf(updateSpeedByLocation(location))+" Km/h");
                }

                if(speedLimitTv != null) {
                    speedLimitTv.setText(String.valueOf(updateSpeedByLocation(location))+" Km/h");
                }

                if(latitudeTv != null){
                    latitudeTv.setText("Lat: "+String.valueOf(location.getLatitude()));
                }

                if(longitudeTv != null){
                    longitudeTv.setText("Lon: "+String.valueOf(location.getLongitude()));
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initComponent(){

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        currentSpeedLayout = new LinearLayout(this);
        mLayCam = new LinearLayout(this);
        eventLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParamsMatchHeight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParamsTextWarpContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsTextMatchParent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        currentSpeedLayout.setBackgroundColor(Color.argb(66, 255, 0, 0));
        mLayCam.setBackgroundColor(Color.argb(66, 255, 0, 255));
        eventLayout.setBackgroundColor(Color.argb(100, 255, 255, 0));

        currentSpeedLayout.setLayoutParams(layoutParamsTextWarpContent);
        mLayCam.setLayoutParams(layoutParamsTextWarpContent);
        eventLayout.setLayoutParams(layoutParamsTextWarpContent);

        mLayCam.setOrientation(LinearLayout.VERTICAL);
        currentSpeedLayout.setOrientation(LinearLayout.HORIZONTAL);

//        if(getSpeedLimitResId() != null) {
//            speedLimitLayout.setBackground(getSpeedLimitResId());
//        }

        if(getEventWindowResId() != null) {
            eventLayout.setBackground(getEventWindowResId());
        }

        if(getCloseBtnResId() != null) {
            closeBtn.setBackground(getCloseBtnResId());
        }

        closeBtn = new Button(this);
        closeBtn.setText("Close");
        closeBtn.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        currentSpeedTv = new TextView(this);
        speedLimitTv = new TextView(this);
        latitudeTv = new TextView(this);
        longitudeTv = new TextView(this);
        eventTv = new TextView(this);
        mCamDist = new TextView(this);
        mCamSpeed = new TextView(this);
        mCamImage = new ImageView(this);
        mDigitalImage100 = new ImageView(this);
        mDigitalImage10 = new ImageView(this);
        mDigitalImage1 = new ImageView(this);

        currentSpeedTv.setTextColor(Color.BLUE);
        speedLimitTv.setTextColor(Color.BLUE);
        latitudeTv.setTextColor(Color.BLUE);
        longitudeTv.setTextColor(Color.BLUE);
        eventTv.setTextColor(Color.BLUE);

        currentSpeedTv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        speedLimitTv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        latitudeTv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        longitudeTv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        eventTv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mLayCam.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);


        currentSpeedTv.setLayoutParams(layoutParamsMatchHeight);
        speedLimitTv.setLayoutParams(layoutParamsTextMatchParent);
        latitudeTv.setLayoutParams(layoutParamsTextWarpContent);
        longitudeTv.setLayoutParams(layoutParamsTextWarpContent);
        eventTv.setLayoutParams(layoutParamsTextMatchParent);
        mCamSpeed.setLayoutParams(layoutParamsTextMatchParent);
        //mCamDist.setLayoutParams(layoutParamsTextMatchParent);
        mDigitalImage100.setLayoutParams(layoutParamsMatchHeight);
        mDigitalImage10.setLayoutParams(layoutParamsMatchHeight);
        mDigitalImage1.setLayoutParams(layoutParamsMatchHeight);

        //event test
        eventTv.setText("前有測速照相");
        currentSpeedTv.setText(" km/h");

        mLayCam.addView(mCamDist);
        mLayCam.addView(mCamImage);
        mLayCam.addView(mCamSpeed);
        currentSpeedLayout.addView(mDigitalImage100);
        currentSpeedLayout.addView(mDigitalImage10);
        currentSpeedLayout.addView(mDigitalImage1);
        currentSpeedLayout.addView(currentSpeedTv);

        eventLayout.addView(eventTv);

        final WindowManager.LayoutParams currentSpeedParams = new WindowManager.LayoutParams(700,300,WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        final WindowManager.LayoutParams speedLimitParams = new WindowManager.LayoutParams(500,500,WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        final WindowManager.LayoutParams eventParams = new WindowManager.LayoutParams(600,200,WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        currentSpeedParams.x = 0;
        currentSpeedParams.y = 0;
        currentSpeedParams.gravity = Gravity.RIGHT;
        mWindowManager.addView(closeBtn, currentSpeedParams);
        currentSpeedParams.gravity = Gravity.CENTER | Gravity.LEFT;
        mWindowManager.addView(currentSpeedLayout, currentSpeedParams);

        speedLimitParams.x = 100;
        speedLimitParams.y = 200;
        speedLimitParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowManager.addView(mLayCam, speedLimitParams);

        eventParams.x = 0;
        eventParams.y = 300;
        eventParams.gravity = Gravity.TOP;
        //mWindowManager.addView(eventLayout, eventParams);

        setClickListener(currentSpeedParams, eventParams, speedLimitParams);

    }

    private int updateSpeedByLocation(Location location) {
        int tempSpeed = (int) (location.getSpeed() * 3.6); // m/s --> Km/h
        return tempSpeed;
    }

}
