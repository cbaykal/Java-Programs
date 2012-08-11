import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;


public class Explosion implements ActionListener{
	
	private static final int TIMER_DELAY = 35;
	private Image image;
	private Timer timer;
	private int x,y;
	private boolean visible;
	
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		try {
			ImageIcon ii = new ImageIcon(this.getClass().getResource("explosion.png"));
			image = ii.getImage();
		} catch(Exception e) {
			System.err.println("Could not find explosion.png");
		}
		
		timer = new Timer(TIMER_DELAY, this);
		timer.start();
		visible = true;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public Image getImage() {
		return image;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void actionPerformed(ActionEvent e) {
		// Don't have to deal with anything else other than timer so no need to check for ActionEvent source
		setVisible(false);
		timer.stop();
	}
}
	

