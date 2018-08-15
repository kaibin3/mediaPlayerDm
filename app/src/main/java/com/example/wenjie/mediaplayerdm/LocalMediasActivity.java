package com.example.wenjie.mediaplayerdm;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.data.local.database.MediaDao;
import com.example.wenjie.mediaplayerdm.data.local.file.ScannerSDcard;
import com.example.wenjie.mediaplayerdm.data.model.Files;
import com.example.wenjie.mediaplayerdm.data.model.Media;
import com.example.wenjie.mediaplayerdm.ui.viewHolder.FileWrap;
import com.example.wenjie.mediaplayerdm.ui.viewHolder.ViewWrap;
import com.example.wenjie.mediaplayerdm.util.MediaUtil;

import java.io.File;
import java.util.List;

public class LocalMediasActivity extends AppCompatActivity {
	private ListView mMediaLv = null;
	private Button tv_back = null;
	private TextView tv_path = null;
	private ListView lv_file = null;
	private Button btn_media = null;
	private Button btn_file = null;
	private List<Media> medias = null;
	private Intent intent = null;
	private String durate = null;
	private long durateTime;
	private String path = null;
	private int recode = -1;
	private int mediaId = -1;
	private String[] themes = null;// 背景数组
	private String[] playModes = null;// 播放数组
	private String theme = null;
	private String playMode = null;
	private int themeCheckedId = -1;
	private int playModeCheckedId = -1;
	private RelativeLayout rl_bg = null;
	private SharedPreferences preferences = null;
	private MediaDao mediaDao = null;
	private List<Files> fileList = null;
	private FileAdapt fileAdapt = null;
	private String currentPath;
	private MyAdapt myAdapt = null;

	/**
	 * MainActivity创建时找到控件 ，填充listview,注册监听事件
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_list_activity);

		Log.i("MainActivity", "onCreate");
		/*ActionBar bar = getActionBar();
		bar.setDisplayShowTitleEnabled(false);*/
		themes = getResources().getStringArray(R.array.theme);
		playModes = getResources().getStringArray(R.array.themes_play);
		findView();
		fillAdapt();
		regist();

	}

	@Override
	protected void onRestart() {
		Log.i("MainActivity", "onRestart");
		super.onRestart();
	}

	/**
	 * Activity开始是preferences的数据
	 */
	@Override
	protected void onStart() {
		Log.i("MainActivity", "onStart");
		preferences = getSharedPreferences("theme", MODE_PRIVATE);
		theme = preferences.getString("selectedTheme", null);
		themeCheckedId = preferences.getInt("themeCheckedId", -1);
		Log.i("themeCheckedId", themeCheckedId + "");
		setThemes(theme);

		playMode = preferences.getString("playMode", null);
		playModeCheckedId = preferences.getInt("playModeCheckedId", -1);

		super.onStart();
	}

	/**
	 * 填充listview
	 */
	private void fillAdapt() {
		mediaDao = new MediaDao(this);
		mediaDao.getSerivesMedia();
		medias = mediaDao.getAllMedia();

		myAdapt = new MyAdapt();
		mMediaLv.setAdapter(myAdapt);

		File file = Environment.getExternalStorageDirectory();
		fileList = ScannerSDcard.findFiles(file);
		fileAdapt = new FileAdapt();
		lv_file.setAdapter(fileAdapt);
		currentPath = "/storage/sdcard";
		tv_path.setText("/storage/sdcard");

	}

	/**
	 * listview注册监听
	 */
	private void regist() {
		mMediaLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				path = (String) mMediaLv.getItemAtPosition(position);
				TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
				durate = (String) tv_time.getText();
				durateTime = medias.get(position).getDurate();
				mediaId = medias.get(position).getId();

				mediaDao = new MediaDao(LocalMediasActivity.this);
				recode = (int) mediaDao.getMedia(mediaId).getRecord();

				Log.i("Main recode", recode + "");

				if (intent == null) {
					intent = new Intent("android.intent.action.mPlay");
				}
				intent.putExtra("path", path);
				intent.putExtra("durate", durate);
				intent.putExtra("durateTime", durateTime);
				intent.putExtra("recode", recode);
				intent.putExtra("id", mediaId);
				intent.putExtra("playModeCheckedId", playModeCheckedId);
				startActivity(intent);

			}
		});

		lv_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String path = (String) lv_file.getItemAtPosition(position); // 得到点击项的路径
				File file = new File(path);
				if (file.isDirectory()) {
					fileList.removeAll(fileList);
					fileList = ScannerSDcard.findFiles(file);
					fileAdapt.notifyDataSetChanged();
					tv_path.setText(path);
					currentPath = path;
				}

			}
		});
		mMediaLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {

				int mediaId = medias.get(position).getId();
				mediaDao.delete(mediaId);

				medias.remove(position);
				myAdapt.notifyDataSetChanged();
				return false;
			}
		});
	}

	/**
	 * 点击事件
	 *
	 * @param v
	 */
	public void ClickMe(View v) {

		switch (v.getId()) {
			case R.id.btn_media:
				mMediaLv.setVisibility(View.VISIBLE);
				tv_path.setVisibility(View.INVISIBLE);
				tv_back.setVisibility(View.INVISIBLE);
				lv_file.setVisibility(View.INVISIBLE);
				break;
			case R.id.btn_file:
				mMediaLv.setVisibility(View.INVISIBLE);
				tv_path.setVisibility(View.VISIBLE);
				tv_back.setVisibility(View.VISIBLE);
				lv_file.setVisibility(View.VISIBLE);
				break;
			case R.id.bt_back:
				if (currentPath.endsWith("/")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalMediasActivity.this);
					alertDialog.setTitle("");
					alertDialog.setMessage("是否退出");
					alertDialog.setIcon(null);
					alertDialog.setPositiveButton("取消", null);
					alertDialog.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									System.exit(0);
								}
							});
					alertDialog.show();
				} else {
					File file = new File(currentPath);
					currentPath = file.getParent(); // 当前路径变为父目录的路径
					File files = file.getParentFile(); // 得到父目录文件夹
					if (files != null) {
						fileList.removeAll(fileList);
						fileList = ScannerSDcard.findFiles(files);
						fileAdapt.notifyDataSetChanged();
						tv_path.setText(currentPath);
					}
				}
				break;
			default:

				break;
		}

	}

	/**
	 * lv_file的适配器
	 *
	 * @author Administrator
	 *
	 */
	private class FileAdapt extends BaseAdapter {

		@Override
		public int getCount() {
			return fileList.size();
		}

		@Override
		public Object getItem(int position) {

			return fileList.get(position).getF_path();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			FileWrap viewWrap = null;
			if (view == null) {
				view = View.inflate(LocalMediasActivity.this, R.layout.iten_file, null);
				viewWrap = new FileWrap(view);
				view.setTag(viewWrap);
			}
			viewWrap = (FileWrap) view.getTag();
			TextView tv_fileName = viewWrap.getTv_fileName();
			tv_fileName.setText(fileList.get(position).getF_name());
			tv_fileName.setTextSize(15);
			return view;

		}

	}

	/**
	 * lv_media的适配器
	 *
	 * @author Administrator
	 *
	 */
	private class MyAdapt extends BaseAdapter {

		@Override
		public int getCount() {
			return medias.size();
		}

		@Override
		public Object getItem(int position) {
			return medias.get(position).getPath();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewWrap viewWrap = null;
			if (view == null) {
				view = View.inflate(LocalMediasActivity.this, R.layout.item_listview,
						null);
				viewWrap = new ViewWrap(view);
				view.setTag(viewWrap);
			}
			viewWrap = (ViewWrap) view.getTag();
			TextView tv_name = viewWrap.getTv_name();
			TextView tv_size = viewWrap.getTv_size();
			TextView tv_time = viewWrap.getTv_time();
			ImageView iv_isplay = viewWrap.getIv_isplay();
			ImageView iv_show = viewWrap.getIv_show();
			tv_name.setText(medias.get(position).getName());
			String time = MediaUtil.formatTime(medias.get(position)
					.getDurate());
			String size = MediaUtil.formatSize(medias.get(position)
					.getSize());
			tv_size.setText(size + "M ");
			tv_time.setText(time);
			Bitmap bitmap = getVideoThumbnail(
					medias.get(position).getPath(), 128, 96,
					MediaStore.Images.Thumbnails.MINI_KIND);

			iv_show.setImageBitmap(bitmap);

			return view;
		}

	}

	/**
	 * 获取缩略图
	 *
	 * @param videoPath
	 *            视频路径
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param kind
	 *            文件种类可以是 Images.Thumbnails.MINI_KIND 或 MICRO_KIND
	 * @return Bitmap
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
									 int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind); // 创建一张视频的缩略图
		// 创建一个指定大小的缩略图
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 查找控件
	 */
	private void findView() {
		mMediaLv = (ListView) findViewById(R.id.lv_media);
		rl_bg = (RelativeLayout) findViewById(R.id.RelativeLayout_bg);
		tv_back = (Button) findViewById(R.id.bt_back);
		tv_path = (TextView) findViewById(R.id.tv_path);
		lv_file = (ListView) findViewById(R.id.lv_file);
		btn_media = (Button) findViewById(R.id.btn_media);
		btn_file = (Button) findViewById(R.id.btn_file);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 菜单点击监听
	 */
	/*@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_setTheme:
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				alertDialog.setTitle("主题设置");
				alertDialog.setSingleChoiceItems(themes, themeCheckedId,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.i("setSingleChoiceItems", themeCheckedId + "");
								themeCheckedId = which;
							}
						});

				alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (themeCheckedId != -1) {
							theme = themes[themeCheckedId];// 取出你所点击的背景

							preferences = getSharedPreferences("theme",
									MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("selectedTheme", theme);
							editor.putInt("themeCheckedId", themeCheckedId);
							editor.commit();
							setThemes(theme);
						}
					}
				});
				alertDialog.show();
				break;
			case R.id.action_playMode:
				AlertDialog.Builder dialog_play = new AlertDialog.Builder(this);
				dialog_play.setTitle("选择播放模式");
				dialog_play.setSingleChoiceItems(playModes, playModeCheckedId,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								playModeCheckedId = which;

							}
						});
				dialog_play.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (playModeCheckedId != -1) {
							playMode = playModes[playModeCheckedId];// 取出你所点击的播放模式
							preferences = getSharedPreferences("theme",
									MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("playMode", playMode);
							editor.putInt("playModeCheckedId", playModeCheckedId);
							editor.commit();
						}

					}
				});
				dialog_play.show();
				break;
			case R.id.action_update:// 刷新
				mediaDao.getSerivesMedia();
				listMedia = mediaDao.getAllMedia();
				myAdapt.notifyDataSetChanged();

				break;
			case R.id.action_exit:// 退出
				AlertDialog.Builder dialog = new AlertDialog.Builder(MediaListActivity.this);
				dialog.setMessage("是否退出");
				dialog.setIcon(null);// PositiveButton
				dialog.setPositiveButton("取消", null);
				dialog.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								System.exit(0);
							}
						});
				dialog.show();
				break;

			default:
				break;
		}

		return super.onMenuItemSelected(featureId, item);
	}*/




	/**
	 * 设置主题
	 *
	 * @param theme
	 */
	public void setThemes(String theme) {
		Log.i("setThemes", theme + "");
		Log.i("setThemes", rl_bg + "");
		if ("彩色".equals(theme)) {
			rl_bg.setBackgroundResource(R.drawable.bg_color);
		} else if ("绿色".equals(theme)) {
			rl_bg.setBackgroundResource(R.drawable.bg_green);
		} else if ("蓝色".equals(theme)) {
			rl_bg.setBackgroundResource(R.drawable.bg_blur);
		} else if ("雪景".equals(theme)) {
			rl_bg.setBackgroundResource(R.drawable.bg_snow);
		}

	}

	@Override
	protected void onPause() {
		Log.i("MainActivity", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i("MainActivity", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("MainActivity", "onDestroy");
		super.onDestroy();
	}
}