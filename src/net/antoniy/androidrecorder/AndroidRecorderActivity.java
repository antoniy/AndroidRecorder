package net.antoniy.androidrecorder;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidRecorderActivity extends Activity implements MediaRecorder.OnInfoListener {
	/** Called when the activity is first created. */
	
	private static final String TAG = AndroidRecorderActivity.class.getSimpleName();
	private static final String SAVE_DIR = "/sdcard/voicerec/";
	
	private MediaRecorder mediaRecorder;
	private boolean isRecording = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(isRecording) {
        	initStartRecording();
        } else {
        	initStopRecording();
        }

        Button recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
				if(!isRecording) {
					startRecording();
					initStartRecording();
				} else {
					try {
						mediaRecorder.stop();
					} catch (IllegalStateException e) {
						Log.w(TAG, "MediaRecorder already stopped.");
					}
					initStopRecording();
				}
			}
		});
        
    }
	
	private void initStartRecording() {
		TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
		Button recordButton = (Button) findViewById(R.id.recordButton);
		
		statusTextView.setText("Recording...");
		recordButton.setText("Stop");
		
		isRecording = true;
	}
	
	private void initStopRecording() {
		TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
		Button recordButton = (Button) findViewById(R.id.recordButton);
		
		statusTextView.setText("Not recording.");
		recordButton.setText("Record");
		
		isRecording = false;
	}
	
	private boolean isCreateMissingDirsSucceed() {
		File file = new File(SAVE_DIR);
		
		try {
			file.mkdirs();
		} catch (SecurityException e) {
			return false;
		}
		
		return true;
	}
	
	private void startRecording() {
		if(!isCreateMissingDirsSucceed()) {
			Log.e(TAG, "Don't have permissions to create missing directories.");
			return;
		}
		
		Log.i(TAG, "Start voice recording.");
        
		mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(SAVE_DIR + "record" + System.currentTimeMillis() + ".mp4");
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(22050);
        mediaRecorder.setMaxDuration(5000);
        mediaRecorder.setOnInfoListener(this);
        try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        mediaRecorder.start();
	}
	
	private void readAudioFile(File file) {
//		AudioInputStream in = AudioSystem.getAudioInputStream(inputFile);
//		AudioInputStream din = null;
//		AudioFormat baseFormat = in.getFormat();
//		AudioFormat decodedFormat = new AudioFormat(
//				AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
//				8, baseFormat.getChannels(), baseFormat.getChannels(),
//				baseFormat.getSampleRate(), true);
//		din = AudioSystem.getAudioInputStream(decodedFormat, in);
	}

	public void onInfo(MediaRecorder mr, int what, int extra) {
		if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
			TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
			Button recordButton = (Button) findViewById(R.id.recordButton);
			isRecording = false;
			
			statusTextView.setText("Not recording.");
			recordButton.setText("Record");
			
//			try {
//				mediaRecorder.stop();
//			} catch (IllegalStateException e) {
//				Log.w(TAG, "MediaRecorder already stopped.");
//			} finally {
				mediaRecorder.release();
//			}
			Log.i(TAG, "Voice recording stopped.");
		}
	}
}