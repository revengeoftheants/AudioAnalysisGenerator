import processing.core.*;
import ddf.minim.*;
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
	int FRAME_RATE_NBR = 30;
	int BUFFER_SIZE_NBR = 1024;
	Boolean FILE_SAVED_IND = true;
	public static String DELIMITER_TXT = "~";
	
	/*
	 * Global variables
	 */
	Minim _minim;
	AudioPlayer _player;
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
		_saver = new Saver(this, BUFFER_SIZE_NBR, SONG_NM + "_FrameRate" + FRAME_RATE_NBR + "_BufferSize" + BUFFER_SIZE_NBR + ".txt");
		
		_minim = new Minim(this);
		_player = _minim.loadFile(SONG_NM + SONG_FILE_EXT_TXT, BUFFER_SIZE_NBR);
		
		if (FILE_SAVED_IND) {
			_displayer = new Displayer(this, BUFFER_SIZE_NBR, _saver.rtrvPathTxt());
		}
		
		_player.play();	
	}
	
	
	public void draw() {
		background(0);

		if (FILE_SAVED_IND) {
			displAnalysisData();
		} else {
			analyzeSound();
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
	 * Analyzes a sound file and saves the analysis to a text file.
	 */
	private void analyzeSound() {
		// Only perform this while the music is playing.
		if (_player.isPlaying()) {
			fill(247);
			text("Saving music. Current frame: " + str(frameCount), 20, 20);
			
			for (int idx = 0; idx < _player.bufferSize(); idx++) {
				float bufferValNbr = (_player.left.get(idx) + _player.right.get(idx));
				_saver.recordBufferVal(bufferValNbr);
				_saver.incrAudioFrameIdx();
			}
		} else {
			_saver.saveFile();
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
			
			float[] audioFrameBuffer = _displayer.rtrvAudioFrameBuffer(frameCount % _displayer.rtrvAudioFrameCnt());
		
			for (int bufferIdx = 0; bufferIdx < audioFrameBuffer.length; bufferIdx++) {
				int xCoordNbr = (int)(bufferIdx * (width / (float)BUFFER_SIZE_NBR));
				int yCoordNbr = (int)(20 * audioFrameBuffer[bufferIdx] + height/2);
				
				point(xCoordNbr, yCoordNbr);
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
