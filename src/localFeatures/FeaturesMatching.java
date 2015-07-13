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

	public static MatOfDMatch match(Mat queryDescriptors, Mat trainDescriptors) {
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(queryDescriptors, trainDescriptors, matches);
		return matches;
	}

}