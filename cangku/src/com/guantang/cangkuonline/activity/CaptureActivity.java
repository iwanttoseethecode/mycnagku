package com.guantang.cangkuonline.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.guantang.cangkuonline.R;
import com.guantang.cangkuonline.ZXing.View.ViewfinderView;
import com.guantang.cangkuonline.ZXing.camera.CameraManager;
import com.guantang.cangkuonline.ZXing.decoding.CaptureActivityHandler;
import com.guantang.cangkuonline.ZXing.decoding.InactivityTimer;
import com.guantang.cangkuonline.eventbusBean.ObjectThree;

import de.greenrobot.event.EventBus;

public class CaptureActivity extends Activity implements Callback,
		OnClickListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	ImageButton light;
	Parameters parameter;
	boolean flag = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		// ��ʼ�� CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		
		light = (ImageButton) findViewById(R.id.light);
		light.setOnClickListener(this);
		light.setImageResource(R.drawable.flash_off);
		
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return ;
		}catch (RuntimeException e) {
			e.printStackTrace();
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		// viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		
		//-------------------����popwindow��Ҫ���ص�ֵ------------------------
//		Intent intent = getIntent();
//		if(intent.getBooleanExtra("SearchPopupWindow_tm", false)){
//			EventBus.getDefault().post(new ObjectThree(obj.getText()));
//		}
		//----------------------------------------------------------------
		Intent intent = new Intent();
		if(obj.getText().matches("^(http|https|ftp)://(.)*$")){//�������ַ
			Uri uri = Uri.parse(obj.getText());
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(uri);
			startActivity(intent);
			finish();
		}else{
			intent.putExtra("scan_tm", obj.getText());
			setResult(1, intent);
			finish();
		}
		
		
	}


	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.light:// ��������ͷ�ƹ�
			Camera camera = CameraManager.get().getCamera();
			if (camera != null) {
				camera.startPreview();
				parameter = camera.getParameters();
				if (flag) {
					parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
					light.setImageResource(R.drawable.flash_off);
					flag = false;
				} else {
					parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
					light.setImageResource(R.drawable.flash_on);
					flag = true;
				}
				camera.setParameters(parameter);
			}

			break;
		default:
			break;
		}
	}

}