package net.antoniy.androidrecorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidRecorderActivity extends Activity {// implements MediaRecorder.OnInfoListener {
	/** Called when the activity is first created. */
	
	private static final String TAG = AndroidRecorderActivity.class.getSimpleName();
//	private static final String SAVE_DIR = "/sdcard/voicerec/";
//	private static final int CHUNK_SIZE = 4096;
	
	private MediaRecorder mediaRecorder;
	private boolean isRecording = false;
	private MicrophoneRecorderAsyncTask microphoneRecorderAsyncTask;
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
					microphoneRecorderAsyncTask = new MicrophoneRecorderAsyncTask();
					microphoneRecorderAsyncTask.execute(v.getContext());
					initStartRecording();
				} else {
					mediaRecorder.stop();
					initStopRecording();
				}
			}
		});
        
    }
	
	public void initStartRecording() {
		TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
		Button recordButton = (Button) findViewById(R.id.recordButton);
		
		statusTextView.setText("Recording...");
		recordButton.setText("Stop");
		
		isRecording = true;
	}
	
	public void initStopRecording() {
		TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
		Button recordButton = (Button) findViewById(R.id.recordButton);
		
		statusTextView.setText("Not recording.");
		recordButton.setText("Record");
		
		isRecording = false;
	}
	
//	private boolean isCreateMissingDirsSucceed() {
//		File file = new File(SAVE_DIR);
//		
//		try {
//			file.mkdirs();
//		} catch (SecurityException e) {
//			return false;
//		}
//		
//		return true;
//	}
	
//	private void startRecording() {
//		int bufferSize = AudioRecord.getMinBufferSize(
//				8000, 
//				AudioFormat.CHANNEL_CONFIGURATION_MONO, 
//				AudioFormat.ENCODING_PCM_8BIT) * 3;
//
//		AudioRecord audioRecord = new AudioRecord(
//				MediaRecorder.AudioSource.VOICE_RECOGNITION, 
//				8000, 
//				AudioFormat.ENCODING_PCM_8BIT, 
//				AudioFormat.CHANNEL_CONFIGURATION_MONO, 
//				bufferSize);
//		
//		System.out.println("");
//		
//		
//		byte[] sampleBuffer = new byte[bufferSize];
//		double[] fftBuffer = new double[bufferSize * 2];
////		double[][] fftResult = new double[][];
////		Vector<Double[]> chunks = new Vector<Double[]>();
//		
//		DoubleFFT_1D fft = new DoubleFFT_1D(bufferSize);
//
//		boolean isRecording = true;
//		audioRecord.startRecording();
//
//		long startTime = System.currentTimeMillis();
//		while(isRecording) {
//			audioRecord.read(sampleBuffer, 0, bufferSize);
//			
//			for (int i = 0; i < fftBuffer.length; i++) {
//				fftBuffer[i * 2] = sampleBuffer[i];
//				fftBuffer[i * 2 + 1] = 0;
//			}
//		
//			fft.complexForward(fftBuffer);
//			
//			for (int i = 0; i < 100; i++) {
//				Log.d(TAG, String.valueOf(fftBuffer[i]));
//			}
//			
//			long endTime = System.currentTimeMillis();
//			if((endTime - startTime) > 5000L) {
//				break;
//			}
//		}
//		
//		if(audioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
//			audioRecord.stop();
//		}
//		
//		audioRecord.release();
//	}
	
//	private void startRecording() {
//		if(!isCreateMissingDirsSucceed()) {
//			Log.e(TAG, "Don't have permissions to create missing directories.");
//			return;
//		}
//		
//		Log.i(TAG, "Start voice recording.");
//        
//		mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        mediaRecorder.setOutputFile(SAVE_DIR + "record" + System.currentTimeMillis() + ".3gp");
//        mediaRecorder.setAudioChannels(1);
//        mediaRecorder.setAudioSamplingRate(22050);
//        mediaRecorder.setMaxDuration(5000);
//        mediaRecorder.setOnInfoListener(this);
//        try {
//			mediaRecorder.prepare();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        
//        mediaRecorder.start();
//	}
	
//	public void onInfo(MediaRecorder mr, int what, int extra) {
//		if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
//			TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
//			Button recordButton = (Button) findViewById(R.id.recordButton);
//			isRecording = false;
//			
//			statusTextView.setText("Not recording.");
//			recordButton.setText("Record");
//			
//			mediaRecorder.release();
//			
//			Log.i(TAG, "Voice recording stopped.");
//		}
//	}
}