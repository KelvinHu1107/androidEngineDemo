package com.kingwaytek.naviking3d.app;

import java.nio.ByteBuffer;

import com.kingwaytek.naviking3d.app.dlg.DLG_MAIN_MENU;

import kr.co.citus.engine.citus_api;
import kr.co.citus.engine.struct.ICON_INFO;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PoiIconTest extends Activity {
	
	EditText m_editName;
	ImageView m_viewIcon;
	EditText m_editCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_icon_test);
		
		m_editName = (EditText)findViewById(R.id.editTextName);
		m_viewIcon = (ImageView)findViewById(R.id.imageViewIcon);
		m_editCategory = (EditText)findViewById(R.id.editTextPoiCategory);
		
		Button btnSearch = (Button)findViewById(R.id.buttonSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String input = m_editName.getText().toString();
				ICON_INFO iconInfo = new ICON_INFO();
				byte [] bits = null;
				bits = citus_api.GetIconBits(input, iconInfo);
				if (bits != null) {
					Log.i("amh", "width: " + iconInfo.width + "height: " + iconInfo.height);
					Bitmap bitmap = Bitmap.createBitmap(iconInfo.width, iconInfo.height, Config.ARGB_8888);
					if (bitmap == null)
						Log.i("amh", "bitmap fail");
					
					int intBits[] = new int[iconInfo.width * iconInfo.height];
					for (int i=0; i<iconInfo.width * iconInfo.height; i++) {
						intBits[i] = (bits[i*4 + 2] & 0xFF) | ((bits[i*4 + 1] & 0xFF) << 8)
								| ((bits[i*4 + 0] & 0xFF) << 16) | ((bits[i*4 + 3] & 0xFF) << 24);
					}
					bitmap.setPixels(intBits, 0, iconInfo.width, 0, 0, iconInfo.width, iconInfo.height);
					m_viewIcon.setImageBitmap(bitmap);
				} else {
					Toast toast = Toast.makeText(PoiIconTest.this, "Failed to get bits", Toast.LENGTH_SHORT);
					toast.show();
				}
				
			}
		});
		
		Button btnPoiOff = (Button)findViewById(R.id.buttonPoiCategoryOff);
		btnPoiOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int cate = getInputPoiCategory();
				if (cate > 0)
					citus_api.MV3D_ShowPoiCategory(cate, false);
			}
		});
		
		Button btnPoiOn = (Button)findViewById(R.id.buttonPoiCategoryOn);
		btnPoiOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int cate = getInputPoiCategory();
				if (cate > 0)
					citus_api.MV3D_ShowPoiCategory(cate, true);
			}
		});
		
		Button btnHome = (Button)findViewById(R.id.btnMap);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PoiIconTest.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private int getInputPoiCategory() {
		int cate = Integer.parseInt(m_editCategory.getText().toString());
		if (cate < 10000)
			return 0;
		return cate;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi_icon_test, menu);
		return true;
	}

}
