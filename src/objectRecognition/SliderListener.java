package objectRecognition;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderListener implements ChangeListener {
	
	public SliderListener() {}

	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();

		if(!slider.getValueIsAdjusting())
			slider.setToolTipText(Integer.toString(slider.getValue()));
	}
}
