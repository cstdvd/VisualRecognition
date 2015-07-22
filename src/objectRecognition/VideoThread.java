package objectRecognition;

import it.unipi.ing.eim.opencv.Parameters;
import it.unipi.ing.eim.opencv.VideoObjectRecognition;

public class VideoThread extends Thread{
	public void run() {
		VideoObjectRecognition objectRecognition;
		try {
			objectRecognition = new VideoObjectRecognition(Parameters.VIDEO_SRC, Parameters.OBJECT_DIR);
			objectRecognition.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
