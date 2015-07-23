package objectRecognition;

import java.io.File;

import it.unipi.ing.eim.opencv.Parameters;
import it.unipi.ing.eim.opencv.VideoObjectRecognition;

import org.opencv.core.Core;

public class VideoObjectRecognitionMain {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	
	public static void main(String[] args) throws Exception {
		if(!System.getProperty("os.name").equals("Mac OS X")) {
			System.load(new File("lib/bin/opencv_ffmpeg2411_64.dll").getAbsolutePath());
		}
		
		MyFrame f = new MyFrame();
		
	}
	
}