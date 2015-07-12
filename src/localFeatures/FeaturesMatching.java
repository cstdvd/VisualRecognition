package localFeatures;

import it.unipi.ing.eim.opencv.Tools;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;

public class FeaturesMatching {

	public static DescriptorMatcher matcher;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		matcher = DescriptorMatcher.create(org.opencv.features2d.DescriptorMatcher.BRUTEFORCE_HAMMING);
	}

	public static void main(String[] args) throws Exception {

		String image1 = "data/img/mixage.jpg";
		String image2 = "data/img/jeanne-hebuterne.jpg";

		Mat img1 = Highgui.imread(image1, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		Mat img2 = Highgui.imread(image2, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		MatOfKeyPoint keypoints1 = KeyPointsDetector.detectKeypoints(img1);
		Mat descriptor1 = FeaturesExtraction.extractDescriptor(img1, keypoints1);

		MatOfKeyPoint keypoints2 = KeyPointsDetector.detectKeypoints(img2);
		Mat descriptor2 = FeaturesExtraction.extractDescriptor(img2, keypoints2);

		MatOfDMatch matches1 = match(descriptor1, descriptor2);

		Mat img_matches1 = new Mat();

		Features2d.drawMatches(img1, keypoints1, img2, keypoints2, matches1, img_matches1);

		Tools.imageDisplay(img_matches1, "Features Matching");

	}

	public static MatOfDMatch match(Mat queryDescriptors, Mat trainDescriptors) {
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(queryDescriptors, trainDescriptors, matches);
		return matches;
	}

}