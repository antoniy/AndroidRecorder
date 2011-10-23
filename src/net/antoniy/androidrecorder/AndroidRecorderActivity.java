package net.antoniy.androidrecorder;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

public class AndroidRecorderActivity extends Activity implements MediaRecorder.OnInfoListener {
	/** Called when the activity is first created. */
	
	private MediaRecorder mediaRecorder;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i("asd", "************************ START!");
        
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(44000);
        mediaRecorder.setMaxDuration(5000);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setOutputFile("/sdcard/record.3gp");
        mediaRecorder.setOnInfoListener(this);
        try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        mediaRecorder.start();
        
        setContentView(R.layout.main);
    }

	public void onInfo(MediaRecorder mr, int what, int extra) {
		if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
			mediaRecorder.stop();
			Log.i("asd", "********************** STOP!");
		}
	}
}