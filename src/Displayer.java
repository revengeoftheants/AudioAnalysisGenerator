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
	public int rtrvAudioFrameCnt() {
		return _audioFrameCnt;
	}
	
	public float[] rtrvAudioFrameBuffer(int inpFrameIdx) {
		float[] rtnFrameData = new float[_bufferSizeNbr];
		
		inpFrameIdx = PApplet.constrain(inpFrameIdx, 0, _audioFrameCnt - 1);
		
		for (int bufferIdx = 0; bufferIdx < _bufferSizeNbr; bufferIdx++) {
			rtnFrameData[bufferIdx] = _musicData[inpFrameIdx][bufferIdx];
		}
		
		return rtnFrameData;
	}

	private void loadData() {
		String[] audioFrameBuffersTxt = _parApp.loadStrings(_loadFilePathTxt);
		_audioFrameCnt = audioFrameBuffersTxt.length;
		_musicData = new float[_audioFrameCnt][_bufferSizeNbr];
		
		for (int audioFrameIdx = 0; audioFrameIdx < _audioFrameCnt - 1; audioFrameIdx++) {
			String bufferTxt = audioFrameBuffersTxt[audioFrameIdx];
			String[] bufferVals = PApplet.splitTokens(bufferTxt, Main.DELIMITER_TXT);
			
			for (int bufferIdx = 0; bufferIdx < _bufferSizeNbr; bufferIdx++) {
				_musicData[audioFrameIdx][bufferIdx] = Float.parseFloat(bufferVals[bufferIdx]);
			}
		}
	}
}
