import java.util.ArrayList;
import processing.core.*;

/**
 * Saves sound analysis data.
 * 
 * @author Kevin Dean
 *
 */
public class Saver {
	
	
	/*
	 * Global variables
	 */
	PApplet _parApp;
	int _bufferSizeNbr, _fftFreqBandCnt, _frameRateNbr;
	int _audioFrameCnt = 0;
	String _songNm, _soundLibNm, _analysisTypTxt, _savePathTxt;
	// We'll use a one-dimensional array that will contain two dimensions: the buffer
	// for each audio frame we record.
	ArrayList<Float> _songAnalysisVals = new ArrayList<Float>();
	
	/**
	 * Constructor.
	 * 
	 * @param inpParApp
	 */
	public Saver(PApplet inpParApp, int inpBufferSizeNbr, int inpFreqBandCnt, String inpSongNm, String inpSoundLibNm, 
				 String inpAnalysisTypTxt, int inpFrameRateNbr) {
		
		_parApp = inpParApp;
		_bufferSizeNbr = inpBufferSizeNbr;
		_fftFreqBandCnt = inpFreqBandCnt;
		_songNm = inpSongNm;
		_soundLibNm = inpSoundLibNm;
		_analysisTypTxt = inpAnalysisTypTxt;
		_frameRateNbr = inpFrameRateNbr;
		_savePathTxt = _songNm + "_" + _soundLibNm + "_" + _analysisTypTxt + "_FrameRate" + _frameRateNbr
					   + "_BufferSize" + _bufferSizeNbr + ".txt";
	}
	
	
	/*
	 * Getters
	 */
	public int rtrvCurrFrameCnt() {
		return _audioFrameCnt;
	}
	
	public String rtrvPathTxt() {
		return _savePathTxt;
	}
	
	
	
	public void storeAnalysisVal(float inpAnalysisVal) {
		_songAnalysisVals.add(new Float(inpAnalysisVal));
	}

	
	public void incrAudioFrameIdx() {
		_audioFrameCnt++;
	}
	
	
	/**
	 * Saves wave analysis data to a text file.
	 */
	public void saveWaveAnalysisFile() {
		String audioFrameBuffersData[] = new String[_audioFrameCnt];
		
		for (int audioFrameIdx = 0; audioFrameIdx < _audioFrameCnt; audioFrameIdx++) {
			String audioFrameBufferDataTxt = ""; // Save the floats as a delimited string of text.
			
			for (int bufferIdx = 0; bufferIdx < _bufferSizeNbr; bufferIdx++) {
				int arrayIdx = (audioFrameIdx * _bufferSizeNbr) + bufferIdx;
				
				float bufferValNbr = (Float) _songAnalysisVals.get(arrayIdx);
				
				audioFrameBufferDataTxt += bufferValNbr + Main.DELIMITER_TXT;
			}
			
			audioFrameBuffersData[audioFrameIdx] = audioFrameBufferDataTxt;
		}
		
		_parApp.saveStrings(_savePathTxt, audioFrameBuffersData);
		
		_parApp.exit();
	}
	
	
	/**
	 * Saves FFT analysis data to a text file.
	 */
	public void saveFFTAnalysisFile() {
		String audioFrameFFTData[] = new String[_audioFrameCnt];
		
		for (int audioFrameIdx = 0; audioFrameIdx < _audioFrameCnt; audioFrameIdx++) {
			String audioFrameFFTDataTxt = ""; // Save the floats as a delimited string of text.
			
			for (int fftIdx = 0; fftIdx < _fftFreqBandCnt; fftIdx++) {
				int arrayIdx = (audioFrameIdx * _fftFreqBandCnt) + fftIdx;
				
				float freqBandValNbr = (Float) _songAnalysisVals.get(arrayIdx);
				
				audioFrameFFTDataTxt += freqBandValNbr + Main.DELIMITER_TXT;
			}
			
			audioFrameFFTData[audioFrameIdx] = audioFrameFFTDataTxt;
		}
		
		_parApp.saveStrings(_savePathTxt, audioFrameFFTData);
		
		_parApp.exit();
	}
}
