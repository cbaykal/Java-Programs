import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class Shield implements ActionListener {
	
	private Image image;
	private Timer timer;
	private boolean active;
	private int recharge;
	
	public Shield() {
		active = false;
		timer = new Timer(10, this);
		timer.start();
		recharge = 0;
	}
	
	public int getTimerStatus() {
		return recharge;
	}
	
	public void setActive(boolean active) {
		if(active && recharge == 0) {
			this.active = active;
		} else if(!active){
			this.active = false;
		}
	}
	
	public boolean getStatus() {
		return active;
	}
	
	public Image getImage() {
		return image;
	}
	
	public int getRecharge() {
		return recharge;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		// Only one action event so no need to check which one
		
		if(getStatus()) {
			recharge++;
			if(recharge >= 100 && getStatus())
				setActive(false);
		} else if(recharge > 0 && !getStatus()) {
			recharge--;
		}

	}
}
