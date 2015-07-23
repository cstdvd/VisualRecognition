package it.unipi.ing.eim.opencv;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class Tools {
	
	private static JLabel lblimage;
	
	public static void updateFrame(Mat mat, String title) {
		BufferedImage image = mat2BufferedImage(mat);

		if (lblimage == null) {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setTitle(title);
			frame.setSize(image.getWidth(), image.getHeight());
			lblimage = new JLabel(new ImageIcon(image));
			frame.getContentPane().add(lblimage, BorderLayout.CENTER);

		} else {
			lblimage.setIcon(new ImageIcon(image));
		}
	}

	public static void imageDisplay(Mat mat, String title) {
		BufferedImage image = mat2BufferedImage(mat);

		// Use a label to display the image
		JFrame frame = new JFrame();
		frame.setTitle(title);
		JLabel lblimage = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static BufferedImage mat2BufferedImage(Mat mat) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (mat.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = mat.channels() * mat.cols() * mat.rows();
		byte[] b = new byte[bufferSize];
		mat.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}

	public static final void displayKeyPoints(Mat image, MatOfKeyPoint keypoints) {
		Mat outImage = new Mat();
		org.opencv.features2d.Features2d.drawKeypoints(image, keypoints, outImage);
		imageDisplay(outImage, "Keypoints");
	}
	
	public static final void displayBoundingBox(Mat imgScene, Mat imgObject, Mat homography) {
		Mat objCorners = new Mat(4, 1, CvType.CV_32FC2);
		Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);

		objCorners.put(0, 0, new double[] {0, 0});
		objCorners.put(1, 0, new double[] {imgObject.cols(), 0});
		objCorners.put(2, 0,
				new double[] {imgObject.cols(), imgObject.rows()});
		objCorners.put(3, 0, new double[] {0, imgObject.rows()});
		// obj_corners:input
		Core.perspectiveTransform(objCorners, sceneCorners, homography);

		Scalar scalar = new Scalar(0, 255, 0);

		double[] x = sceneCorners.get(0, 0);
		double[] y = sceneCorners.get(1, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(2, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(3, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(0, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		imageDisplay(imgScene, "object recognition");
	}
	
	public static final void displayBoundingBox2(Mat imgMatches, Mat imgObject, Mat homography) {
		Mat objCorners = new Mat(4, 1, CvType.CV_32FC2);
		Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);

		objCorners.put(0, 0, new double[] {0, 0});
		objCorners.put(1, 0, new double[] {imgObject.cols(), 0});
		objCorners.put(2, 0,
				new double[] {imgObject.cols(), imgObject.rows()});
		objCorners.put(3, 0, new double[] {0, imgObject.rows()});
		// obj_corners:input
		Core.perspectiveTransform(objCorners, sceneCorners, homography);

		Scalar scalar = new Scalar(0, 255, 0);
		int shift = imgObject.cols();

		double[] x = sceneCorners.get(0, 0);
		x[0] += shift;
		double[] y = sceneCorners.get(1, 0);
		y[0] += shift;
		Core.line(imgMatches, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(2, 0);
		y[0] += shift;
		Core.line(imgMatches, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(3, 0);
		y[0] += shift;
		Core.line(imgMatches, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(0, 0);
		y[0] += shift;
		Core.line(imgMatches, new Point(x), new Point(y), scalar, 4);

		Tools.imageDisplay(imgMatches, "ransac");
	}

	
	public static void addBoundingBox(Mat imgScene, Mat imgObject, Mat homography) {
		Mat objCorners = new Mat(4, 1, CvType.CV_32FC2);
		Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);

		objCorners.put(0, 0, new double[] {0, 0});
		objCorners.put(1, 0, new double[] {imgObject.cols(), 0});
		objCorners.put(2, 0,
				new double[] {imgObject.cols(), imgObject.rows()});
		objCorners.put(3, 0, new double[] {0, imgObject.rows()});
		// obj_corners:input
		Core.perspectiveTransform(objCorners, sceneCorners, homography);

		Scalar scalar = new Scalar(0, 255, 0);

		double[] x = sceneCorners.get(0, 0);
		double[] y = sceneCorners.get(1, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(2, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(3, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(0, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);
	}
	
	public static void addBoundingBox(Mat imgScene, Mat imgObject, Mat homography, String name) {
		Mat objCorners = new Mat(4, 1, CvType.CV_32FC2);
		Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);

		objCorners.put(0, 0, new double[] {0, 0});
		objCorners.put(1, 0, new double[] {imgObject.cols(), 0});
		objCorners.put(2, 0,
				new double[] {imgObject.cols(), imgObject.rows()});
		objCorners.put(3, 0, new double[] {0, imgObject.rows()});
		// obj_corners:input
		Core.perspectiveTransform(objCorners, sceneCorners, homography);

		Scalar scalar = new Scalar(255, 100, 0);
		Scalar scalar2 = new Scalar(255, 255, 255);

		double[] x = sceneCorners.get(0, 0);
		double[] y = sceneCorners.get(1, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(2, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(3, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);

		x = y;
		y = sceneCorners.get(0, 0);
		Core.line(imgScene, new Point(x), new Point(y), scalar, 4);
		
		Core.putText(imgScene, name, new Point(x), Core.FONT_HERSHEY_DUPLEX, 1.5, scalar2, 4);
	}
	
	public static void printMatInfo(Mat mat) {
		System.out.println("cols: " + mat.cols());
		System.out.println("width: " + mat.width());
		System.out.println("rows: " + mat.rows());
		System.out.println("height: " + mat.height());
		
		System.out.println("size: " + mat.size());
		
		System.out.println("total: " + mat.total());
		
		System.out.println("type: " + CvType.typeToString(mat.type()));

		System.out.println("channels: " + mat.channels());
		
		System.out.println("elemSize: " + mat.elemSize());
		System.out.println("elemSize1: " + mat.elemSize1());
		
		System.out.println("dims: " + mat.dims());

		System.out.println("depth:dsdfdsf " + CvType.depth(mat.depth()));
	}
}
