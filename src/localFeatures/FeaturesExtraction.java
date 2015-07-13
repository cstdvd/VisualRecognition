package localFeatures;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.highgui.Highgui;

public class FeaturesExtraction {

	public static DescriptorExtractor descExtractor;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		descExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
	}

	public static void main(String[] args) throws Exception {

		String query = "data/img/jeanne-hebuterne.jpg";

		Mat img = Highgui.imread(query, Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		MatOfKeyPoint keypoints = KeyPointsDetector.detectKeypoints(img);
		Mat descQuery = extractDescriptor(img, keypoints);

		printFeatureValues(descQuery);
	}

	public static Mat extractDescriptor(Mat img, MatOfKeyPoint keypoints) {
		Mat descriptor = new Mat();
		descExtractor.compute(img, keypoints, descriptor);
		return descriptor;
	}

	public static void printFeatureValues(Mat descQuery) {
		// show features data
		for (int i = 0; i < descQuery.rows(); i++) {
			for (int j = 0; j < descQuery.cols(); j++) {
				byte[] bytes = new byte[1];
				descQuery.get(i, j, bytes);
				int vla = ((int) bytes[0] & 0xFF);
				System.out.print(vla + " ");
			}
			System.out.println();
		}
	}

}