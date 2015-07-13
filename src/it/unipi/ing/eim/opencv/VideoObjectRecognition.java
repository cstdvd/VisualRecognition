package it.unipi.ing.eim.opencv;

import java.io.File;
import java.io.FilenameFilter;

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

	private Mat[] imgObject;
	private MatOfKeyPoint[] keypointsObject;
	private Mat[] descriptorObject;

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

		// create new filename filter
        FilenameFilter fileNameFilter = new FilenameFilter() {
  
           @Override
           public boolean accept(File dir, String name) {
              if(name.lastIndexOf('.')>0)
              {
                 // get last index for '.' char
                 int lastIndex = name.lastIndexOf('.');
                 
                 // get extension
                 String str = name.substring(lastIndex);
                 
                 // match path name extension
                 if(str.equals(".jpg") || str.equals(".JPG") || str.equals(".jpeg") || str.equals(".png"))
                 {
                    return true;
                 }
              }
              return false;
           }
        };
        
		File dir = new File(objectFile);
		File listOfFiles[] = dir.listFiles(fileNameFilter);
		imgObject = new Mat[listOfFiles.length];
		keypointsObject = new MatOfKeyPoint[listOfFiles.length];
		descriptorObject = new Mat[listOfFiles.length];
		
		for(int i=0; i<listOfFiles.length; i++){
			System.out.println(objectFile+listOfFiles[i].getName());
			imgObject[i] = Highgui.imread(objectFile+listOfFiles[i].getName(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
			keypointsObject[i] = KeyPointsDetector.detectKeypoints(imgObject[i]);
			descriptorObject[i] = FeaturesExtraction.extractDescriptor(imgObject[i], keypointsObject[i]);

			Tools.displayKeyPoints(imgObject[i], keypointsObject[i]);
		}
		
		ransac = new Ransac();
		videoCapture = new VideoCapture();
	}

	public void start() {

		int keyframeCounter = 0;

		if (!videoCapture.isOpened()) {
			System.out.println("Error opening Videocapture");
		} else {
			System.out.println("Videocapture opened");
		}

		Mat frame = new Mat();
		while (videoCapture.read(frame) == true) {
//			if (keyframeCounter++ % Parameters.KEYFRAME_FREQ == 0) {
				for(int i = 0; i < imgObject.length; i++){
					Mat homography = computeHomography(frame, i);

					if (homography != null) {
						Tools.addBoundingBox(frame, imgObject[i], ransac.getHomography());
					}
					Tools.updateFrame(frame, "Object Recognition");
				}
//			}
		}
		System.exit(0);
	}

	private Mat computeHomography(Mat frame, int i) {

		Mat homography = null;
		MatOfKeyPoint keypointsScene = KeyPointsDetector.detectKeypoints(frame);
		Mat descriptorScene = FeaturesExtraction.extractDescriptor(frame, keypointsScene);

		MatOfDMatch matches = FeaturesMatching.match(descriptorObject[i], descriptorScene);
		MatOfDMatch goodMatches = FeaturesMatchingFiltered.matchWithFiltering(matches, Parameters.DISTANCE_THRESHOLD);

		if (goodMatches.total() > Parameters.GOOD_MATCHES_THRESHOLD) {
			System.out.println("goodMatches: "+goodMatches.total());

			ransac.computeHomography(goodMatches.toList(), keypointsObject[i], keypointsScene);
			System.out.println("numCountInliers: "+ransac.countNumInliers());
			if (ransac.countNumInliers() > Parameters.RANSAC_INLIERS_THRESHOLD)
				homography = ransac.getHomography();
			if(homography == null)
				System.out.println("homography = null");
		}
		return homography;
	}
}