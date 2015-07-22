package objectRecognition;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import it.unipi.ing.eim.opencv.Parameters;

public class MyFrame extends JFrame {
	
	private JPanel dx = new JPanel();
	private JPanel sx = new JPanel();
	
	private ButtonGroup group = new ButtonGroup();
	private JRadioButton videoRadio = new JRadioButton("Video");
	private JRadioButton camRadio = new JRadioButton("WebCam");
		
	private JButton startButton = new JButton("Start");
	
	public MyFrame() {
		
		// Filter .mov or .mp4 extension
		FilenameFilter fileNameFilter = new FilenameFilter() {
			  
	           @Override
	           public boolean accept(File dir, String name) {
	              if(name.lastIndexOf('.')>0)
	              {
	                 int lastIndex = name.lastIndexOf('.');
	                 
	                 String str = name.substring(lastIndex);
	                 
	                 if(str.equals(".mov") || str.equals(".MOV") || str.equals(".mp4") || str.equals(".MP4"))
	                 {
	                    return true;
	                 }
	              }
	              return false;
	           }
	        };
	        
		Container c = this.getContentPane();
		c.setLayout(new GridLayout(1, 2));
		
		c.add(sx);
		c.add(dx);
		
		camRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
		videoRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
		camRadio.setSelected(true);
		
		// Group of radio button:
		// choose between webcam or video source
		group.add(camRadio);
		group.add(videoRadio);
		
		Box radioBox = Box.createVerticalBox();
		radioBox.add(camRadio);
		radioBox.add(videoRadio);
		radioBox.setBorder(new EmptyBorder(20, 40, 40, 20));
		sx.setLayout(new BorderLayout());
		sx.add(radioBox, BorderLayout.NORTH);

		// Sliders to set parameters
		JSlider sliderKeyframe = new JSlider(0, 30, 10);
		sliderKeyframe.setBorder(BorderFactory.createTitledBorder("Keyframe Frequency"));
		sliderKeyframe.setMinorTickSpacing(2);
		sliderKeyframe.setMajorTickSpacing(10);
		sliderKeyframe.setPaintTicks(true);
		sliderKeyframe.setPaintLabels(true);
		JSlider sliderRansacInliers = new JSlider(0, 30, 12);
		sliderRansacInliers.setBorder(BorderFactory.createTitledBorder("Ransac Inliers Threshold"));
		sliderRansacInliers.setMinorTickSpacing(2);
		sliderRansacInliers.setMajorTickSpacing(10);
		sliderRansacInliers.setPaintTicks(true);
		sliderRansacInliers.setPaintLabels(true);
		JSlider sliderGoodMatches = new JSlider(0, 30, 15);
		sliderGoodMatches.setBorder(BorderFactory.createTitledBorder("Good Matches Threshold"));
		sliderGoodMatches.setMinorTickSpacing(2);
		sliderGoodMatches.setMajorTickSpacing(10);
		sliderGoodMatches.setPaintTicks(true);
		sliderGoodMatches.setPaintLabels(true);
		JSlider sliderDistance = new JSlider(0, 80, 40);
		sliderDistance.setBorder(BorderFactory.createTitledBorder("Distance Threshold"));
		sliderDistance.setMinorTickSpacing(5);
		sliderDistance.setMajorTickSpacing(20);
		sliderDistance.setPaintTicks(true);
		sliderDistance.setPaintLabels(true);
		
		dx.add(Box.createRigidArea(new Dimension(100,10)));
		dx.add(sliderKeyframe);
		dx.add(sliderRansacInliers);
		dx.add(sliderGoodMatches);
		dx.add(sliderDistance);
		
		dx.add(startButton);
		
		// Load all the video files in the source directory
		File dir = new File(Parameters.VIDEO_DIR);
		File[] listOfFiles = dir.listFiles(fileNameFilter);
		String[] listOfNames = new String[listOfFiles.length];
		for(int i = 0; i < listOfFiles.length; i++)
			listOfNames[i] = listOfFiles[i].getName();
		
		// List of all the video files
		JList<String> list = new JList<String>(listOfNames);
		list.setFixedCellWidth(150);
		list.setFixedCellHeight(20);
		list.setVisibleRowCount(10);
		list.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		list.setEnabled(false);
		JPanel p = new JPanel();
		JScrollPane scroll = new JScrollPane(list);
		p.add(scroll);
		sx.add(p, BorderLayout.CENTER);
		
		// Set listener for radio button
		RadioListener radioListener = new RadioListener(list);
		camRadio.addActionListener(radioListener);
		videoRadio.addActionListener(radioListener);
		
		// Set listener for sliders
		SliderListener sliderListener = new SliderListener();
		sliderKeyframe.addChangeListener(sliderListener);
		sliderRansacInliers.addChangeListener(sliderListener);
		sliderGoodMatches.addChangeListener(sliderListener);
		sliderDistance.addChangeListener(sliderListener);
		
		// Set listener for button
		ButtonListener buttonListener = new ButtonListener(camRadio, videoRadio,
				list, sliderKeyframe, sliderRansacInliers, sliderGoodMatches, sliderDistance);
		startButton.addActionListener(buttonListener);
		
		setSize(470, 405);
		setVisible(true);
		setResizable(false);
		setTitle("Video Object Recognition");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
