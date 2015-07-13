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