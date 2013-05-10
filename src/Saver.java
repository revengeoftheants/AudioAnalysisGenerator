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
	int _currAudioFrameIdx = 0;
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
	public int rtrvCurrFrameIdx() {
		return _currAudioFrameIdx;
	}
	
	public String rtrvPathTxt() {
		return _savePathTxt;
	}
	
	
	
	public void recordBufferVal(float inpBufferDatumNbrDatumNbr) {
		_songDataNbrs.add(new Float(inpBufferDatumNbrDatumNbr));
	}

	
	public void incrAudioFrameIdx() {
		_currAudioFrameIdx++;
	}
	
	
	public void saveFile() {
		String sampleDataTxt; // Save the floats as a long text string.
		String exportList[] = new String[_currAudioFrameIdx];
		
		for (int audioFrameIdx = 0; audioFrameIdx < _currAudioFrameIdx; audioFrameIdx++) {
			sampleDataTxt = Main.DELIMITER_TXT;
			
			for (int bufferLenIdx = 0; bufferLenIdx < _bufferSizeNbr; bufferLenIdx++) {
				int arrayIdx = (audioFrameIdx * _bufferSizeNbr) + bufferLenIdx;
				
				float sampleDatumNbr = (Float) _songDataNbrs.get(arrayIdx);
				
				sampleDataTxt += sampleDatumNbr + Main.DELIMITER_TXT;
			}
			
			exportList[audioFrameIdx] = sampleDataTxt;
		}
		
		_parApp.saveStrings(_savePathTxt, exportList);
		
		_parApp.exit();
	}
}
