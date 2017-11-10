package com.kingwaytek.naviking3d.app;

import kr.co.citus.engine.citus_api;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CarTestActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

	int carType = -1;
	int carIdx = -1;
	
	EditText editCarIdx;
	RadioButton radioShowOff;
	RadioButton radioVia;
	RadioButton radioDest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_test);
		
		RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroupCarType);
		rg.setOnCheckedChangeListener(this);
		
		editCarIdx = (EditText)findViewById(R.id.editTextCarIdx);
		Log.i("car", "onCreate");
		Button btnApply = (Button)findViewById(R.id.buttonApplyCar);
		btnApply.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("car", "car on Click");
				Log.i("car", "carType:" + carType + " carIdx:" + carIdx);
				if (carType < 0 || carIdx < 0) {
					Toast toast = Toast.makeText(CarTestActivity.this, "Input something", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					carIdx = Integer.parseInt(editCarIdx.getText().toString());
					Log.i("car", "carType:" + carType + " carIdx:" + carIdx);
					citus_api.MV3D_SetVehicleMark(carType, carIdx);
					Intent intent = new Intent(CarTestActivity.this, MapViewActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				
			}
		});
		
		RadioGroup rg2 = (RadioGroup)findViewById(R.id.radioGroup2);
		rg2.setOnCheckedChangeListener(this);
		
		int guideLine = citus_api.SYS_GetGuideLine();
		radioShowOff = (RadioButton)findViewById(R.id.radioGuildeOff);
		radioVia = (RadioButton)findViewById(R.id.radioGuideVia);
		radioDest = (RadioButton)findViewById(R.id.radioGuideDest);
		if (guideLine == 0)
			radioShowOff.setChecked(true);
		else if (guideLine == 1)
			radioVia.setChecked(true);
		else
			radioDest.setChecked(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.car_test, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		Log.i("car", "check changed");
		Log.i("car", "tag:" + arg0.getTag().toString());
		if (arg0.getTag().toString().equals("car_type")) {
			Log.i("car", "check changed fucck");
			switch (arg1) {
			case R.id.radioCar:
				carType = 0;
				break;
			case R.id.radioScooter:
				carType = 1;
				break;
			case R.id.radioBigBike:
				carType = 2;
				break;
			case R.id.radioTruck:
				carType = 3;
				break;
			case R.id.radioBigTruck:
				carType = 4;
				break;
			}
			carIdx = citus_api.MV3D_GetVehicleMark(carType);
			editCarIdx.setText(Integer.toString(carIdx));	
		} else {
			Log.i("car", "what the hell");
			switch (arg1) {
			case R.id.radioGuildeOff:
				citus_api.SYS_SetDrawGuideLine(0);
				break;
			case R.id.radioGuideVia:
				citus_api.SYS_SetDrawGuideLine(1);
				break;
			case R.id.radioGuideDest:
				citus_api.SYS_SetDrawGuideLine(2);
				break;
			}
		}
	}

}
