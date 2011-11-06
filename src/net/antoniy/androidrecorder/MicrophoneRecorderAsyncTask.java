package net.antoniy.androidrecorder;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

public class MicrophoneRecorderAsyncTask extends AsyncTask<Context, Void, Void> {

	private static final String TAG = MicrophoneRecorderAsyncTask.class.getSimpleName();
	
	private static final int RECORDING_DURATION_IN_MILLIS = 5000;
	
	private AndroidRecorderActivity context;
	
	@Override
	protected Void doInBackground(Context... params) {
		if(params.length == 0) {
			throw new IllegalArgumentException("You should provide a context.");
		}
		
		if(!(params[0] instanceof AndroidRecorderActivity)) {
			throw new IllegalArgumentException("You should provide a correct context.");
		}
		this.context = (AndroidRecorderActivity) params[0];
		
		startRecording();
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		context.initStopRecording();
	}

	private void startRecording() {
		int bufferSize = AudioRecord.getMinBufferSize(
				8000, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO, 
				AudioFormat.ENCODING_PCM_16BIT) * 3;

		AudioRecord audioRecord = new AudioRecord(
				MediaRecorder.AudioSource.VOICE_RECOGNITION, 
				8000, 
				AudioFormat.ENCODING_PCM_16BIT, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO, 
				bufferSize);
		
		System.out.println("");
		
		
		byte[] sampleBuffer = new byte[bufferSize];
		double[] fftBuffer = new double[bufferSize * 2];
//		double[][] fftResult = new double[][];
//		Vector<Double[]> chunks = new Vector<Double[]>();
		
		DoubleFFT_1D fft = new DoubleFFT_1D(bufferSize);

		boolean isRecording = true;
		audioRecord.startRecording();

		long startTime = System.currentTimeMillis();
		StringBuffer outputBuffer = new StringBuffer(1024);
		while(isRecording) {
			audioRecord.read(sampleBuffer, 0, bufferSize);
			
			for (int i = 0; i < bufferSize; i++) {
				fftBuffer[i * 2] = sampleBuffer[i];
				fftBuffer[i * 2 + 1] = 0;
			}
		
			fft.complexForward(fftBuffer);
			
			outputBuffer.delete(0, outputBuffer.length());
			for (int i = 0; i < 20; i++) {
				double magnitude = Math.log(Math.log(Math.hypot(fftBuffer[i * 2], fftBuffer[i * 2 + 1]) + 1));
				outputBuffer.append(Math.round(magnitude)).append(' ');
			}
			Log.d(TAG, outputBuffer.toString());
			
			long endTime = System.currentTimeMillis();
			if((endTime - startTime) > RECORDING_DURATION_IN_MILLIS) {
				break;
			}
		}
		
		if(audioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
			audioRecord.stop();
		}
		
		audioRecord.release();
	}
}
