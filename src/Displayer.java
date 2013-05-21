import processing.core.*;
import org.apache.commons.lang3.*;

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
	int _audioFrameCnt, _audioFrameDataCnt;

	/**
	 * Constructor
	 */
	public Displayer(PApplet inpParApp, String inpLoadFilePathTxt, int inpAudioFrameDataCnt) {
		_parApp = inpParApp;
		_loadFilePathTxt = inpLoadFilePathTxt;
		_audioFrameDataCnt = inpAudioFrameDataCnt;

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
	 * Retrieves the data for a given audio frame.
	 * 
	 * @param inpFrameIdx  The zero-based index number of the audio frame to retrieve.
	 * @return An array of float values representing the analysis data for this audio frame.
	 */
	public float[] rtrvAudioFrameData(int inpFrameIdx) {
		float[] rtnFrameData = new float[_audioFrameDataCnt];

		inpFrameIdx = PApplet.constrain(inpFrameIdx, 0, _audioFrameCnt - 1);

		for (int frameIdx = 0; frameIdx < _audioFrameDataCnt; frameIdx++) {
			rtnFrameData[frameIdx] = _musicData[inpFrameIdx][frameIdx];
		}

		return rtnFrameData;
	}


	/**
	 * Loads the data from the specified file into memory.
	 */
	private void loadData() {
		String[] audioFramesDataTxt = _parApp.loadStrings(_loadFilePathTxt);
		_audioFrameCnt = audioFramesDataTxt.length;
		_audioFrameDataCnt = StringUtils.countMatches(audioFramesDataTxt[0], Main.DELIMITER_TXT);
		_musicData = new float[_audioFrameCnt][_audioFrameDataCnt];

		for (int audioFrameIdx = 0; audioFrameIdx < _audioFrameCnt; audioFrameIdx++) {
			String audioFrameDataTxt = audioFramesDataTxt[audioFrameIdx];
			String[] analysisVals = audioFrameDataTxt.split(Main.DELIMITER_TXT);

			for (int frameValsIdx = 0; frameValsIdx < _audioFrameDataCnt; frameValsIdx++) {
				_musicData[audioFrameIdx][frameValsIdx] = Float.parseFloat(analysisVals[frameValsIdx]);
			}
		}
	}
}
