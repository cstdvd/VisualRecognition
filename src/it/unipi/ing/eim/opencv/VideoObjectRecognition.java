package it.unipi.ing.eim.opencv;

import java.io.File;
import java.io.FilenameFilter;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import localFeatures.FeaturesExtraction;
import localFeatures.FeaturesMatching;
import localFeatures.FeaturesMatchingFiltered;
import localFeatures.KeyPointsDetector;

public class VideoObjectRecognition {

	private VideoCapture videoCapture;

	private File dir;
	private File[] objectDir;
	private File[][] listOfFiles;
	private Mat[][] imgObject;
	private MatOfKeyPoint[][] keypointsObject;
	private Mat[][] descriptorObject;

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
                 if(str.equals(".jpg") || str.equals(".JPG") || str.equals(".png") || str.equals(".PNG"))
                 {
                    return true;
                 }
              }
              return false;
           }
        };
        
		dir = new File(objectFile);
		objectDir = dir.listFiles();
		listOfFiles = new File[objectDir.length][];
		imgObject = new Mat[objectDir.length][];
		keypointsObject = new MatOfKeyPoint[objectDir.length][];
		descriptorObject = new Mat[objectDir.length][];
		
		for(int i=0; i<objectDir.length; i++){
			listOfFiles[i]=objectDir[i].listFiles(fileNameFilter);
			imgObject[i] = new Mat[listOfFiles[i].length];
			keypointsObject[i] = new MatOfKeyPoint[listOfFiles[i].length];
			descriptorObject[i] = new Mat[listOfFiles[i].length];
			for(int j=0; j<listOfFiles[i].length; j++) {
				imgObject[i][j] = Highgui.imread(objectFile+listOfFiles[i][j].getName(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
				keypointsObject[i][j] = KeyPointsDetector.detectKeypoints(imgObject[i][j]);
				descriptorObject[i][j] = FeaturesExtraction.extractDescriptor(imgObject[i][j], keypointsObject[i][j]);
			}
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
		Mat resFrame = new Mat();
		String name;
		
		videoCapture.read(frame);
		Size size = resizeTo480p(frame);
		System.out.println("Video size: "+size.height+" x "+size.width);
		
		while (videoCapture.read(frame) == true) {
			if (keyframeCounter++ % Parameters.KEYFRAME_FREQ == 0) {
				Imgproc.resize(frame, resFrame, size);
				for(int i = 0; i < imgObject.length; i++) {
					boolean found = false;
					for(int j=0; j<imgObject[i].length; j++) {
						Mat homography = computeHomography(resFrame, i, j);

						if (homography != null) {
							name = objectDir[i].getName();
							//name = name.split("[0-9]")[0];
							Tools.addBoundingBox(resFrame, imgObject[i][j], ransac.getHomography(), name);
							found = true;
						}
						Tools.updateFrame(resFrame, "Object Recognition");
						if(found)
							break;
					}
				}
			}
		}
		System.exit(0);
	}

	private Mat computeHomography(Mat frame, int i, int j) {

		Mat homography = null;
		MatOfKeyPoint keypointsScene = KeyPointsDetector.detectKeypoints(frame);
		Mat descriptorScene = FeaturesExtraction.extractDescriptor(frame, keypointsScene);

		MatOfDMatch matches = FeaturesMatching.match(descriptorObject[i][j], descriptorScene);
		MatOfDMatch goodMatches = FeaturesMatchingFiltered.matchWithFiltering(matches, Parameters.DISTANCE_THRESHOLD);

		if (goodMatches.total() > Parameters.GOOD_MATCHES_THRESHOLD) {
			System.out.println("goodMatches: "+goodMatches.total());

			ransac.computeHomography(goodMatches.toList(), keypointsObject[i][j], keypointsScene);
			System.out.println("numCountInliers: "+ransac.countNumInliers());
			if (ransac.countNumInliers() > Parameters.RANSAC_INLIERS_THRESHOLD)
				homography = ransac.getHomography();
		}
		return homography;
	}
	
	// Takes the frame size and returns the size in 480p maintaining the aspect ratio
	private Size resizeTo480p(Mat frame) {
		int width = frame.width();
		int height = frame.height();
		
		return new Size((width*480)/height, 480);
	}
}