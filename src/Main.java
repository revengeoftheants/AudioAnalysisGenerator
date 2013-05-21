import org.omg.CORBA.Current;

import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import pitaru.sonia_v2_9.*;
import net.beadsproject.beads.*;

/**
 * Analyze mode: Analyzes a sound file and creates a text file containing the analysis values.
 * Display mode: Loads the text file and displays the sound analysis values.
 * 
 * Based on the work of Diana Lange.
 * http://www.openprocessing.org/sketch/33855
 * 
 * @author Kevin Dean
 *
 */
public class Main extends PApplet {

	/*
	 * Global constants
	 */
	String SONG_NM = "Song of Los";
	String SONG_FILE_EXT_TXT = ".wav";
	int FRAME_RATE_NBR = 24;
	int BUFFER_SIZE_NBR = 1024;
	String SOUND_LIB_NM = "Minim";
	String ANALYSIS_TYP_TXT = "FFTFreqBand";
	int FFT_LOG_AVG_MIN_OCTAVE_NBR = 85;
	int FFT_LOG_AVG_BANDS_PER_OCTAVE_NBR = 5;
	float FFT_BAND_LOW_FREQ_NBR = 2000f;
	float FFT_BAND_HI_FREQ_NBR = 4000f;
	Boolean FILE_SAVED_IND = false;

	public static String DELIMITER_TXT = "~";

	/*
	 * Global variables
	 */
	Minim _minim;
	AudioPlayer _player;
	FFT _fft;
	PFont _font;
	Saver _saver;
	Displayer _displayer;


	/**
	 * Creates a Processing PApplet.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		PApplet.main(new String[] { "Main" });
	}


	public void setup() {
		size(512, 200);
		frameRate(FRAME_RATE_NBR);

		_font = createFont("Arial", 12);

		_minim = new Minim(this);
		_player = _minim.loadFile(SONG_NM + SONG_FILE_EXT_TXT, BUFFER_SIZE_NBR);
		_fft = new FFT(_player.bufferSize(), _player.sampleRate());
		// A Hamming window can be used to shape the sample buffer that is passed to the FFT.
		// This can reduce the amount of noise in the spectrum.
		_fft.window(FFT.HAMMING);
		
		int fftFreqBandCnt;
		
		if (ANALYSIS_TYP_TXT == "FFTFullSpec") {
			_fft.logAverages(FFT_LOG_AVG_MIN_OCTAVE_NBR, FFT_LOG_AVG_BANDS_PER_OCTAVE_NBR);
			fftFreqBandCnt = _fft.specSize();
		} else {
			fftFreqBandCnt = 1;
		}

		_saver = new Saver(this, BUFFER_SIZE_NBR, fftFreqBandCnt, SONG_NM, SOUND_LIB_NM, ANALYSIS_TYP_TXT, FRAME_RATE_NBR);

		if (FILE_SAVED_IND) {
			_displayer = new Displayer(this, _saver.rtrvPathTxt(), BUFFER_SIZE_NBR);
		}

		_player.play();
	}


	public void draw() {
		background(0);

		if (FILE_SAVED_IND) {
			displAnalysisData();
		} else {
			if (SOUND_LIB_NM == "Minim" && ANALYSIS_TYP_TXT == "Wave") {
				analyzeMinimWave();
			} else if (SOUND_LIB_NM == "Minim" && ANALYSIS_TYP_TXT == "FFTFreqBand") {
				analyzeMinimFFTFreqBand();
			} else if (SOUND_LIB_NM == "Minim" && ANALYSIS_TYP_TXT == "FFTFullSpec") {
				analyzeMinimFFTFullSpectrum();
			}
		}
	}


	/**
	 * Handles keyboard button presses.
	 */
	public void keyPressed() {
		if (key==ESC) {
			key=0;
			cleanupResources();
		}
	}


	public void stop() {
		_player.close();
		_minim.stop();
		super.stop();
	}


	/**
	 * Creates a wave analysis using Minim.
	 */
	private void analyzeMinimWave() {
		// Only perform this while the music is playing.
		if (_player.isPlaying()) {
			fill(247);
			text("Saving wave. Current frame: " + str(frameCount), 20, 20);

			for (int idx = 0; idx < _player.bufferSize(); idx++) {
				float bufferValNbr = (_player.left.get(idx) + _player.right.get(idx));
				_saver.storeAnalysisVal(bufferValNbr);
			}

			_saver.incrAudioFrameIdx();
		} else {
			_saver.saveWaveAnalysisFile();
		}
	}


	/**
	 * Creates a Fast Fourier Transform analysis for a single frequency band using Minim.
	 */
	private void analyzeMinimFFTFreqBand() {
		// Only perform this while music is playing.
		if (_player.isPlaying()) {
			fill(247);
			text("Saving FFT. Current frame: " + str(frameCount), 20, 20);

			// Perform a Forward FFT on the samples in the buffer.
			_fft.forward(_player.mix);

			rect(width/2, height, 20, -10 * _fft.calcAvg(1800f, 4000f));
			_saver.storeAnalysisVal(_fft.calcAvg(FFT_BAND_LOW_FREQ_NBR, FFT_BAND_HI_FREQ_NBR));

			_saver.incrAudioFrameIdx();
		} else {
			_saver.saveFFTAnalysisFile();
		}
	}
	
	
	/**
	 * Creates a Fast Fourier Transform analysis for the full frequency spectrum using Minim.
	 */
	private void analyzeMinimFFTFullSpectrum() {
		// Only perform this while music is playing.
		if (_player.isPlaying()) {
			fill(247);
			text("Saving FFT. Current frame: " + str(frameCount), 20, 20);

			// Perform a Forward FFT on the samples in the buffer.
			_fft.forward(_player.mix);

			for(int idx = 0; idx < _fft.specSize(); idx++) {
				float freqBandVal = _fft.getBand(idx);
				_saver.storeAnalysisVal(freqBandVal);
			}
			
			/*
			int w = (int)(width/_fft.avgSize());
			for(int i = 0; i < _fft.avgSize(); i++)
			{
				// draw a rectangle for each average, multiply the value by 5 so we can see it better
				rect(i*w, height, w, height - _fft.getAvg(i)*2);
			}
			*/

			_saver.incrAudioFrameIdx();
		} else {
			_saver.saveFFTAnalysisFile();
		}
	}


	/**
	 * Visualizes a file of sound analysis data.
	 */
	private void displAnalysisData() {
		fill(247);
		stroke(247);

		// Only perform this while the music is playing.
		if (_player.isPlaying()) {
			text("Displaying music. Current frame: " + str(frameCount), 20, 20);

			// Modulus of the total audio frame count ensures that it's okay if the frame count exceeds the audio frame count
			// (e.g., we're looping the song). We subtract 1 because the method expects a zero-based index number.
			float[] audioFrameData = _displayer.rtrvAudioFrameData((frameCount % _displayer.rtrvAudioFrameCnt()) - 1);

			for (int frameDataIdx = 0; frameDataIdx < audioFrameData.length; frameDataIdx++) {
				if (ANALYSIS_TYP_TXT == "Wave") {
					int xCoordNbr = (int)(frameDataIdx * (width / (float)audioFrameData.length));
					int yCoordNbr = (int)(20 * audioFrameData[frameDataIdx] + height/2);

					point(xCoordNbr, yCoordNbr);	
				} else if (ANALYSIS_TYP_TXT == "FFTFreqBand") {
					rect(width/2, height, 20, -10 * audioFrameData[frameDataIdx]);
				} else {
					line(frameDataIdx, height, frameDataIdx, height - audioFrameData[frameDataIdx] * 3);
				}
			}
		} else {
			exit();
		}
	}


	/**
	 * Performs cleanup when the sketch is terminated.
	 */
	private void cleanupResources() {
		_player.close();
		_minim.stop();

		try {
			// Add in a short sleep time to give minim threads a chance to end; otherwise, you will hear a heinous sound.
			Thread.sleep(50);
		} catch (InterruptedException excp) {
			excp.printStackTrace();
		}
		exit();
	}
}
