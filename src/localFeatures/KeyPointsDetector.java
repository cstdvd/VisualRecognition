package localFeatures;

import it.unipi.ing.eim.opencv.Tools;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

public class KeyPointsDetector {
	
	public static FeatureDetector detector;
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		detector = FeatureDetector.create(FeatureDetector.ORB);
//		detector.read("data/detectorPar.xml");
	}	

	public static void main(String[] args) throws Exception {
		
		String query = "data/img/jeanne-hebuterne.jpg";
		
		Mat img = Highgui.imread(query, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		MatOfKeyPoint keypoints = detectKeypoints(img);
		printKeyPointsValues(keypoints);

		Tools.displayKeyPoints(img, keypoints);

	}
	
	public static MatOfKeyPoint detectKeypoints(Mat img) {
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		detector.detect(img, keypoints);
		return keypoints;
	}
	
	public static void printKeyPointsValues(MatOfKeyPoint keypoints) {
		KeyPoint[] kpArray = keypoints.toArray();
		
		//keypoint data
		for (int i = 0; i < kpArray.length; i++) {
			System.out.println("x: " + kpArray[i].pt.x + ", y: " + kpArray[i].pt.y + " angle: " + kpArray[i].angle + " size: " + kpArray[i].size);
		}

		System.out.println("Number of keypoints: " + keypoints.rows());
	}
		
}
