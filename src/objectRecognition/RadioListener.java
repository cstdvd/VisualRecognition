package objectRecognition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JRadioButton;

public class RadioListener implements ActionListener{

	private JList list;
	
	public RadioListener(JList list) {
		this.list = list;
	}
	
	public void actionPerformed(ActionEvent e){
		JRadioButton button = (JRadioButton) e.getSource();
		
		if(button.getText().equals("Video"))
			list.setEnabled(true);
		else if(button.getText().equals("WebCam"))
			list.setEnabled(false);
	}
}
