package it.unipi.ing.eim.opencv;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import localFeatures.FeaturesExtraction;
import localFeatures.FeaturesMatching;
import localFeatures.FeaturesMatchingFiltered;
import localFeatures.KeyPointsDetector;

public class VideoObjectRecognition {

	private VideoCapture videoCapture;

	private Mat imgObject;
	private MatOfKeyPoint keypointsObject;
	private Mat descriptorObject;

	private Ransac ransac;

	public VideoObjectRecognition(String streamSource, String objectFile) throws Exception {
		init(objectFile);
		System.out.println("Opening video " + streamSource);
		videoCapture.open(streamSource);
	}

	public VideoObjectRecognition(int streamSource, String objectFile) throws Exception {
		init(objectFile);
		System.out.println("Opening camera");
		videoCapture.open(streamSource);
	}

	private void init(String objectFile) {
		//TODO
		imgObject = Highgui.imread(objectFile, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		keypointsObject = KeyPointsDetector.detectKeypoints(imgObject);
		descriptorObject = FeaturesExtraction.extractDescriptor(imgObject, keypointsObject);
		ransac = new Ransac();
		videoCapture = new VideoCapture();
	}

	public void start() {
		//TODO
		int keyframeCounter = 0;

		if (!videoCapture.isOpened()) {
			System.out.println("Error opening Videocapture");
		} else {
			System.out.println("Videocapture opened");
		}

		Mat frame = new Mat();
		while (videoCapture.read(frame) == true) {
//			if (keyframeCounter++ % Parameters.KEYFRAME_FREQ == 0) {
				Mat homography = computeHomography(frame);

				if (homography != null) {
					Tools.addBoundingBox(frame, imgObject, ransac.getHomography());
				}
				Tools.updateFrame(frame, "Object Recognition");
			}
//		}
		System.exit(0);
	}

	private Mat computeHomography(Mat frame) {
		//TODO
		Mat homography = null;
		MatOfKeyPoint keypointsScene = KeyPointsDetector.detectKeypoints(frame);
		Mat descriptorScene = FeaturesExtraction.extractDescriptor(frame, keypointsScene);

		MatOfDMatch matches = FeaturesMatching.match(descriptorObject, descriptorScene);
		MatOfDMatch goodMatches = FeaturesMatchingFiltered.matchWithFiltering(matches, Parameters.DISTANCE_THRESHOLD);

		if (goodMatches.total() > 5) {
			System.out.println("goodMatches: "+goodMatches.total());

			ransac.computeHomography(goodMatches.toList(), keypointsObject, keypointsScene);
			System.out.println("numCountInliers: "+ransac.countNumInliers());
			if (ransac.countNumInliers() > 5)
				homography = ransac.getHomography();
			if(homography == null)
				System.out.println("homography = null");
		}
		return homography;
	}
}