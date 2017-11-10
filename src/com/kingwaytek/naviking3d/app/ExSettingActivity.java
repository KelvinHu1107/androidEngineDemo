package com.kingwaytek.naviking3d.app;

import com.kingwaytek.naviking3d.app.dlg.DLG_SEARCH_KIND;

import kr.co.citus.engine.citus_api;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ExSettingActivity extends Activity {
	static final int SETTING_EXT_JUNCTIONVIEW = 0;
	static final int SETTING_EXT_JUNCTIONDRAW = 1;
	static final int SETTING_EXT_TMC = 2;
	static final int SETTING_EXT_CAMERA = 3;
	static final int SETTING_EXT_SPEED_LIMIT = 4;
	static final int SETTING_EXT_CCTV = 5;
	
	static final int SETTING_TYPE_DISPLAY = 0;
	static final int SETTING_TYPE_RING = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ex_setting);
		
		Button btn1 = (Button)findViewById(R.id.buttonMapColorSet); // 
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Day", "Night", "Auto", "Cancel"};
				int curMode = citus_api.SYS_GetMapColorMode();
				items[curMode] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						
						citus_api.SYS_SetMapColorMode(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Map color mode");
				adSelect.show();
			}
		});
		///
		Button btn2 = (Button)findViewById(R.id.buttonInformationTypeSet); // 
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Distance", "Time", "Remaining time", "Cancel"};
				int curMode = citus_api.SYS_GetArriveInfoType();
				items[curMode] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetArriveInfoTpe(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Display information type");
				adSelect.show();
			}
		});
		
		Button btn3 = (Button)findViewById(R.id.buttonViaFirst); // 
		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetShowMiddleStationFirst())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetShowMiddleStationFirst(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Show via first");
				adSelect.show();
			}
		});
		
		Button btn4 = (Button)findViewById(R.id.buttonJunctionViewPic); // 
		btn4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_JUNCTIONVIEW, SETTING_TYPE_DISPLAY))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_JUNCTIONVIEW, SETTING_TYPE_DISPLAY, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Junction view : show picture");
				adSelect.show();
			}
		});
		
		Button btn5 = (Button)findViewById(R.id.buttonJunctionViewRing); // 
		btn5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_JUNCTIONVIEW, SETTING_TYPE_RING))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_JUNCTIONVIEW, SETTING_TYPE_RING, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Junction view : RING");
				adSelect.show();
			}
		});
		
		Button btn6 = (Button)findViewById(R.id.buttonJunctionDrawPic); // 
		btn6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_JUNCTIONDRAW, SETTING_TYPE_DISPLAY))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_JUNCTIONDRAW, SETTING_TYPE_DISPLAY, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Junction DRAW : show picture");
				adSelect.show();
			}
		});
		
		Button btn7 = (Button)findViewById(R.id.buttonJunctionDrawRing); // 
		btn7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_JUNCTIONDRAW, SETTING_TYPE_RING))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_JUNCTIONDRAW, SETTING_TYPE_RING, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Junction DRAW : RING");
				adSelect.show();
			}
		});
		
		Button btn8 = (Button)findViewById(R.id.buttonTMCPic); // 
		btn8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_TMC, SETTING_TYPE_DISPLAY))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_TMC, SETTING_TYPE_DISPLAY, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("TMC DRAW : show picture");
				adSelect.show();
			}
		});
		
		Button btn9 = (Button)findViewById(R.id.buttonTMCRing); // 
		btn9.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_TMC, SETTING_TYPE_RING))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_TMC, SETTING_TYPE_RING, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("TMC : RING");
				adSelect.show();
			}
		});
		
		Button btn10 = (Button)findViewById(R.id.buttonSpeedCameraIcon); // 
		btn10.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_CAMERA, SETTING_TYPE_DISPLAY))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_CAMERA, SETTING_TYPE_DISPLAY, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Speed camera : show icon");
				adSelect.show();
			}
		});
		
		Button btn11 = (Button)findViewById(R.id.buttonSpeedCameraRing); // 
		btn11.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"No", "Ring", "Voice Prompt", "Cancel"};
//				if (citus_api.SYS_GetExtInfo(SETTING_EXT_CAMERA, SETTING_TYPE_RING))
//					items[0] += "(*)";
//				else
					items[citus_api.SYS_GetExtInfo2(SETTING_EXT_CAMERA, SETTING_TYPE_RING)] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_CAMERA, SETTING_TYPE_RING, which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Speed camera : ring");
				adSelect.show();
			}
		});
		
		Button btn12 = (Button)findViewById(R.id.buttonSpeedLimitIcon); // 
		btn12.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_SPEED_LIMIT, SETTING_TYPE_DISPLAY))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_SPEED_LIMIT, SETTING_TYPE_DISPLAY, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Speed limit : show icon");
				adSelect.show();
			}
		});
		
		Button btn13 = (Button)findViewById(R.id.buttonSpeedLimitRing); // 
		btn13.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"No", "Ring", "Voice Prompt", "Cancel"};
//				if (citus_api.SYS_GetExtInfo(SETTING_EXT_SPEED_LIMIT, SETTING_TYPE_RING))
//					items[0] += "(*)";
//				else
					items[citus_api.SYS_GetExtInfo2(SETTING_EXT_SPEED_LIMIT, SETTING_TYPE_RING)] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_SPEED_LIMIT, SETTING_TYPE_RING, which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Speed limit : ring");
				adSelect.show();
			}
		});
		
		Button btn14 = (Button)findViewById(R.id.buttonSpeedLimitRule); // 
		btn14.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"limit", ">=5", ">=10", "Cancle"};
				int rule = citus_api.SYS_GetShowRoadLimitRuleRingType();
				if (rule >= 10)
					items[2] += "(*)";
				else if (rule >= 5)
					items[1] += "(*)";
				else
					items[0] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						int rule = 0;
						if (which == 0)
							rule = 0;
						else if (which == 1)
							rule = 5;
						else if (which == 2)
							rule = 10;
						citus_api.SYS_SetShowRoadLimitRuleRingType(rule);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Speed limit warning rule");
				adSelect.show();
			}
		});
		
		Button btn15 = (Button)findViewById(R.id.buttonSpeedWarning); // 
		btn15.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetRemindWarningWithCamera())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetRemindWarningWithCamera(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Speed warn");
				adSelect.show();
			}
		});
		
		Button btn16 = (Button)findViewById(R.id.buttonSignboard); // 
		btn16.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetUsingRoadSign())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetUsingRoadSign(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Show signboard");
				adSelect.show();
			}
		});
		
		Button btn17 = (Button)findViewById(R.id.buttonLaneGuide); // 
		btn17.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_IsShowLaneInfo())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetShowLaneInfo(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Show lane info");
				adSelect.show();
			}
		});
		
		Button btn18 = (Button)findViewById(R.id.buttonHighway); // 
		btn18.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_IsHighwaySplit())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetHighwayMode(which == 0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Show highway facility");
				adSelect.show();
			}
		});
		
		Button btn19 = (Button)findViewById(R.id.buttonCCTVvideo); // 
		btn19.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_CCTV, SETTING_TYPE_DISPLAY))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_CCTV, SETTING_TYPE_DISPLAY, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("CCTV : show video");
				adSelect.show();
			}
		});
		
		Button btn20 = (Button)findViewById(R.id.buttonCCTVring); // 
		btn20.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetExtInfo(SETTING_EXT_CCTV, SETTING_TYPE_RING))
					items[0] += "(*)";
				else
					items[1] += "(*)";
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetExtInfo(SETTING_EXT_CCTV, SETTING_TYPE_RING, which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("CCTV : ring");
				adSelect.show();
			}
		});
		
		Button btn21 = (Button)findViewById(R.id.buttonDirectionGuide); // 
		btn21.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Hide", "to Via", "to Dest.", "Cancel"};
				int mode = citus_api.SYS_GetGuideLine();
				items[mode] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetDrawGuideLine(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Direction guide");
				adSelect.show();
			}
		});
		
		Button btn22 = (Button)findViewById(R.id.buttonLanguage); // 
		btn22.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Mandarin", "Taiyu", "Hakka", "English", "Cancel"};
				int mode = citus_api.SYS_GetVoiceLanguageType();
				items[mode] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 4)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetVoiceLanguageType(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Void language");
				adSelect.show();
			}
		});
		
		Button btn23 = (Button)findViewById(R.id.buttonVoiceWhenCall); // 
		btn23.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"0", "Normal", "Pause voice prompt", "Cancel"};
				int mode = 0;//citus_api.SYS_GetMediaPlayerType();
				items[mode] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 3)
						{
							dialog.dismiss();
							return;
						}
						//citus_api.SYS_SetMediaPlayerType(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Voice prompt when call");
				adSelect.show();
			}
		});
		
		Button btn24 = (Button)findViewById(R.id.buttonNaviBackground); // 
		btn24.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.SYS_GetUseNaviInBackground())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SYS_SetUseNaviInBackground(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Navigation in background");
				adSelect.show();
			}
		});
		
		Button btn25 = (Button)findViewById(R.id.buttonCityModel); // 
		btn25.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.MV3D_IsShowBuilding())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.MV3D_ShowBuilding(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Show 3D city model");
				adSelect.show();
			}
		});
		
		Button btn26 = (Button)findViewById(R.id.buttonLandmark); // 
		btn26.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Yes", "No", "Cancel"};
				if (citus_api.MV3D_IsShowLandmark())
					items[0] += "(*)";
				else
					items[1] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 2)
						{
							dialog.dismiss();
							return;
						}
						citus_api.MV3D_ShowLandmark(which==0);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Show Landmark");
				adSelect.show();
			}
		});
		
		Button btnMap = (Button)findViewById(R.id.buttonBackToMap);
		btnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ExSettingActivity.this, MapViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		Button btnTheme = (Button)findViewById(R.id.buttonTheme); // 
		btnTheme.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"Default", "Theme1", "Theme2", "Theme3", "Cancel"};
				int curSel = citus_api.MV3D_GetTheme();
				items[curSel] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 4)
						{
							dialog.dismiss();
							return;
						}
						citus_api.MV3D_SetTheme(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("Select Theme");
				adSelect.show();
			}
		});
		
		Button btnTTSVolumn = (Button)findViewById(R.id.buttonTTSVolumn); // 
		btnTTSVolumn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] items = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Cancel"};
				int curSel = citus_api.SND_TTS_GetVolumn();
				items[curSel] += "(*)";
				
				AlertDialog.Builder adSelect = new AlertDialog.Builder(ExSettingActivity.this);
				adSelect.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 10)
						{
							dialog.dismiss();
							return;
						}
						citus_api.SND_TTS_SetVolumn(which);
						dialog.dismiss();
						
					}
				});
				adSelect.setTitle("TTS Volumn");
				adSelect.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex_setting, menu);
		return true;
	}

}
