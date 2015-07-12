package localFeatures;

import java.util.ArrayList;
import java.util.List;

import it.unipi.ing.eim.opencv.Tools;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;

public class FeaturesMatchingFiltered {

	public static final int GOOD_MATCHES_THRESHOLD = 35;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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

		MatOfDMatch matches = FeaturesMatching.match(descriptor1, descriptor2);

		MatOfDMatch matches2 = matchWithFiltering(matches, GOOD_MATCHES_THRESHOLD);

		Mat filteredMatches = new Mat();

		Features2d.drawMatches(img1, keypoints1, img2, keypoints2, matches2, filteredMatches);

		Tools.imageDisplay(filteredMatches, "Filtered Matching");
	}

	public static MatOfDMatch matchWithFiltering(MatOfDMatch matches, int threshold) {
		List<DMatch> filteredMatch = getGoodMatches(matches, threshold);
		MatOfDMatch filteredMatches = new MatOfDMatch();
		filteredMatches.fromList(filteredMatch);
		return filteredMatches;
	}

	private static List<DMatch> getGoodMatches(MatOfDMatch matches, double threshold) {
		List<DMatch> goodMatches = new ArrayList<DMatch>();
		List<DMatch> matchesList = matches.toList();
		for (int i = 0; i < matchesList.size(); i++) {
			DMatch curr = matchesList.get(i);
			if (curr.distance < threshold) {
				goodMatches.add(curr);
			}
		}
		return goodMatches;
	}

}