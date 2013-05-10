import processing.core.*;

/**
 * Loads sound analysis data for display.
 * 
 * @author Kevin Dean
 *
 */
public class Displayer {
	
	/*
	 * Global variables
	 */
	PApplet _parApp;
	float[][] _musicData;
	String _loadFilePathTxt;
	int _audioFrameCnt, _bufferSizeNbr;
	
	/**
	 * Constructor
	 */
	public Displayer(PApplet inpParApp, int inpBufferSizeNbr, String inpLoadFilePathTxt) {
		_parApp = inpParApp;
		_loadFilePathTxt = inpLoadFilePathTxt;
		_bufferSizeNbr = inpBufferSizeNbr;
		
		loadData();
	}
	
	/*
	 * Getters
	 */
	
	/**
	 * Retrieves the total count of audio frames represented in the text file.
	 */
	public int rtrvAudioFrameCnt() {
		return _audioFrameCnt;
	}
	
	/**
	 * Retrieves the buffer for a given audio frame.
	 * 
	 * @param inpFrameIdx  The zero-based index number of the audio frame to retrieve.
	 * @return An array of float values representing the buffer for this audio frame.
	 */
	public float[] rtrvAudioFrameBuffer(int inpFrameIdx) {
		float[] rtnFrameData = new float[_bufferSizeNbr];
		
		inpFrameIdx = PApplet.constrain(inpFrameIdx, 0, _audioFrameCnt - 1);
		
		for (int bufferIdx = 0; bufferIdx < _bufferSizeNbr; bufferIdx++) {
			rtnFrameData[bufferIdx] = _musicData[inpFrameIdx][bufferIdx];
		}
		
		return rtnFrameData;
	}

	
	/**
	 * Loads the data from the specified file into memory.
	 */
	private void loadData() {
		String[] audioFrameBuffersTxt = _parApp.loadStrings(_loadFilePathTxt);
		_audioFrameCnt = audioFrameBuffersTxt.length;
		_musicData = new float[_audioFrameCnt][_bufferSizeNbr];
		
		for (int audioFrameIdx = 0; audioFrameIdx < _audioFrameCnt - 1; audioFrameIdx++) {
			String bufferTxt = audioFrameBuffersTxt[audioFrameIdx];
			String[] bufferVals = bufferTxt.split(Main.DELIMITER_TXT);
			
			for (int bufferIdx = 0; bufferIdx < _bufferSizeNbr - 1; bufferIdx++) {
				try {
					_musicData[audioFrameIdx][bufferIdx] = Float.parseFloat(bufferVals[bufferIdx]);
				} catch (Exception excp) {
					PApplet.println(excp.getMessage());
				}
			}
		}
	}
}
