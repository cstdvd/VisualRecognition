package objectRecognition;

import it.unipi.ing.eim.opencv.Parameters;
import it.unipi.ing.eim.opencv.Ransac;
import it.unipi.ing.eim.opencv.Tools;
import localFeatures.FeaturesExtraction;
import localFeatures.FeaturesMatching;
import localFeatures.FeaturesMatchingFiltered;
import localFeatures.KeyPointsDetector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;

public class RansacMain {
		
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception {
		
		String image1 = "data/img/2014-05-14 14.52.14.jpg";
		String image2 = "data/img/20140512_105751.jpg";

		Mat imgObject = Highgui.imread(image1, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		Mat imgScene = Highgui.imread(image2, Highgui.CV_LOAD_IMAGE_GRAYSCALE);


		MatOfKeyPoint keypointsObject = KeyPointsDetector.detectKeypoints(imgObject);
		Mat descriptorObject = FeaturesExtraction.extractDescriptor(imgObject,
				keypointsObject);

		MatOfKeyPoint keypointsScene = KeyPointsDetector.detectKeypoints(imgScene);
		Mat descriptorScene = FeaturesExtraction.extractDescriptor(imgScene, keypointsScene);

		MatOfDMatch matches = FeaturesMatching.match(descriptorObject, descriptorScene);

		MatOfDMatch goodMatches = FeaturesMatchingFiltered.matchWithFiltering(matches, Parameters.DISTANCE_THRESHOLD);
		System.out.println("goodMatches: " + goodMatches.total());


		Mat imgMatches = new Mat();

		Features2d.drawMatches(imgObject, keypointsObject, imgScene, keypointsScene, goodMatches, imgMatches);
		
		Ransac ransac = new Ransac();

		ransac.computeHomography(goodMatches.toList(),	keypointsObject, keypointsScene);
		
		System.out.println(ransac.countNumInliers());
		
		Tools.displayBoundingBox2(imgMatches, imgObject, ransac.getHomography());
	}

}