package it.unipi.ing.eim.opencv;

import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.KeyPoint;

public class Ransac {

	private Mat homography;
	private Mat inliers;

	public void computeHomography(List<DMatch> goodMatches,
			MatOfKeyPoint keypointsObject, MatOfKeyPoint keypointsScene) {

		MatOfPoint2f obj = new MatOfPoint2f();
		MatOfPoint2f scene = new MatOfPoint2f();

		List<KeyPoint> keypointsObjectList = keypointsObject.toList();
		List<KeyPoint> keypointsSceneList = keypointsScene.toList();
		List<Point> pointsObjectList = new ArrayList<Point>();
		List<Point> pointsSceneList = new ArrayList<Point>();

		for (int i = 0; i < goodMatches.size(); i++) {
			pointsObjectList
					.add(keypointsObjectList.get(goodMatches.get(i).queryIdx).pt);
			pointsSceneList
					.add(keypointsSceneList.get(goodMatches.get(i).trainIdx).pt);
		}
		obj.fromList(pointsObjectList);
		scene.fromList(pointsSceneList);

		inliers = new Mat();

		homography = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 1.0,
				inliers);

	}

	public Mat getHomography() {
		return homography;
	}

	public Mat getInliers() {
		return inliers;
	}

	public int countNumInliers() {

		int inliersPoints = 0;
		int rows = inliers.rows();
		for (int i = 0; i < rows; i++) {
			int cols = inliers.cols();
			for (int j = 0; j < cols; j++) {
				double[] values = inliers.get(i, j);
					if (values[0] == 1.0)
						inliersPoints++;
			}
		}
		return inliersPoints;
	}

}
