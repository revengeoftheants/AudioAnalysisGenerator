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
	int _bufferSizeNbr;
	int _audioFrameCnt = 0;
	String _savePathTxt;
	// We'll use a one-dimensional array that will contain two dimensions: the buffer
	// for each audio frame we record.
	ArrayList<Float> _songDataNbrs = new ArrayList<Float>();
	
	/**
	 * Constructor.
	 * 
	 * @param inpParApp
	 */
	public Saver(PApplet inpParApp, int inpBufferSizeNbr, String inpSavePathTxt) {
		
		_parApp = inpParApp;
		_bufferSizeNbr = inpBufferSizeNbr;
		_savePathTxt = inpSavePathTxt;
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
	
	
	
	public void recordBufferVal(float inpBufferValNbr) {
		_songDataNbrs.add(new Float(inpBufferValNbr));
	}

	
	public void incrAudioFrameIdx() {
		_audioFrameCnt++;
	}
	
	
	public void saveFile() {
		String audioFrameBuffersData[] = new String[_audioFrameCnt];
		
		for (int audioFrameIdx = 0; audioFrameIdx < _audioFrameCnt; audioFrameIdx++) {
			String audioFrameBufferDataTxt = ""; // Save the floats as a delimited string of text.
			
			for (int bufferIdx = 0; bufferIdx < _bufferSizeNbr; bufferIdx++) {
				int arrayIdx = (audioFrameIdx * _bufferSizeNbr) + bufferIdx;
				
				float bufferValNbr = (Float) _songDataNbrs.get(arrayIdx);
				
				audioFrameBufferDataTxt += bufferValNbr + Main.DELIMITER_TXT;
			}
			
			audioFrameBuffersData[audioFrameIdx] = audioFrameBufferDataTxt;
		}
		
		_parApp.saveStrings(_savePathTxt, audioFrameBuffersData);
		
		_parApp.exit();
	}
}
