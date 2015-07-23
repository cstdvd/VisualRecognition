package objectRecognition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import it.unipi.ing.eim.opencv.Parameters;
import it.unipi.ing.eim.opencv.VideoObjectRecognition;

public class ButtonListener implements ActionListener{
	private JRadioButton camRadio, videoRadio;
	private JList list;
	private JSlider sliderKeyframe, sliderRansacInliers, sliderGoodMatches, sliderDistance;
	private VideoObjectRecognition objectRecognition;
	
	public ButtonListener(JRadioButton camRadio, JRadioButton videoRadio, JList list, 
			JSlider sliderKeyframe, JSlider sliderRansacInliers, 
			JSlider sliderGoodMatches, JSlider sliderDistance) {
		this.camRadio = camRadio;
		this.videoRadio = videoRadio;
		this.list = list;
		this.sliderKeyframe = sliderKeyframe;
		this.sliderRansacInliers = sliderRansacInliers;
		this.sliderGoodMatches = sliderGoodMatches;
		this.sliderDistance = sliderDistance;
	}
	
	public void actionPerformed(ActionEvent e){
		if(!e.getActionCommand().equals("Start"))
			return;
		
		// Set keyframe parameter
		if(sliderKeyframe.getValue() == 0){
			System.out.println("Zero as keyframe value is not ammitted");
			return;
		} else
			Parameters.KEYFRAME_FREQ = sliderKeyframe.getValue();
		
		// Set thresholds
		Parameters.RANSAC_INLIERS_THRESHOLD = sliderRansacInliers.getValue();
		Parameters.GOOD_MATCHES_THRESHOLD = sliderGoodMatches.getValue();
		Parameters.DISTANCE_THRESHOLD = sliderDistance.getValue();
		
		System.out.println("PARAMETERS\nKeyframe Frequency: "+
				Parameters.KEYFRAME_FREQ+"\nRansac Inliers Threshold: "+
				Parameters.RANSAC_INLIERS_THRESHOLD+"\nGood Matches Threshold: "+
				Parameters.GOOD_MATCHES_THRESHOLD+"\nDistance Threshold: "+
				Parameters.DISTANCE_THRESHOLD+"\n");
		if(camRadio.isSelected()){
			CameraThread cameraThread = new CameraThread();
			cameraThread.start();
			return;
		}else if(videoRadio.isSelected()){
			String videoName = (String) list.getSelectedValue();
			if(videoName == null){
				System.out.println("No video selected");
				return;
			}
			Parameters.VIDEO_SRC = Parameters.VIDEO_DIR+videoName;
			VideoThread videoThread = new VideoThread();
			videoThread.start();
		}
	}

}
