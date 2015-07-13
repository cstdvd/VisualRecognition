package objectRecognition;

import java.io.File;

import it.unipi.ing.eim.opencv.VideoObjectRecognition;

import org.opencv.core.Core;

public class VideoObjectRecognitionMain {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	private static final String VIDEO_SRC = "data/img/marlboro2.mov";
	private static final int CAMERA_SRC = 0;
	
	private static final String OBJECT_DIR = "data/img/test/";

	public static void main(String[] args) throws Exception {
		if(!System.getProperty("os.name").equals("Mac OS X")) {
			System.load(new File("lib/bin/opencv_ffmpeg2411_64.dll").getAbsolutePath());
		}
		
		VideoObjectRecognition objectRecognition = new VideoObjectRecognition(VIDEO_SRC, OBJECT_DIR);
		//VideoObjectRecognition objectRecognition = new VideoObjectRecognition(CAMERA_SRC, OBJECT_DIR);

		objectRecognition.start();
	}
	
}