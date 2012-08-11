import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
/*Craft object of the game*/

public class Craft {
	
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 430;
	
	private int x, y;
	private int dx, dy;
	private int imageWidth, imageHeight;
	private boolean visible, shieldStatus;
	private Image image;
	private Shield shield;
	private ArrayList<Missile> missileList;
	
	
	public Craft() {
		
		try {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("craft.png"));
		image = ii.getImage();
		} catch(Exception e) {
			System.err.println("Error: Could not find craft.png");
			System.exit(-1);
		}
		
		imageWidth = image.getWidth(null);
		imageHeight = image.getHeight(null);
		
		x = y = 25;
		visible = true;
		shieldStatus = false;
		
		shield = new Shield();
		missileList = new ArrayList<Missile>();
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
	
	public boolean isVisible() {
		return visible;
	}
	
	public void move() {
		x += dx;
		y += dy;
		checkBounds(); // To see if we are inside the frame
	}
	
	public void clearMissiles() {
		missileList.clear();
	}
	
	public ArrayList<Missile> getMissiles() {
		return missileList;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	private void checkBounds() {
		if(x < 0) {
			x = 0;
		} else if(x > FRAME_WIDTH - imageWidth) {
			x = FRAME_WIDTH - imageWidth;
		}
		
		if(y < 30) {
			y = 30;
		} else if(y > FRAME_HEIGHT - imageHeight) {
			y = FRAME_HEIGHT - imageHeight;
		}
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, imageWidth, imageHeight);
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP) {
			dy = -1;
		}
		
		if(key == KeyEvent.VK_DOWN){
			dy = 1;
		}
		
		if(key == KeyEvent.VK_RIGHT) {
			dx = 1;
		}
		
		if(key == KeyEvent.VK_LEFT){
			dx = -1;
		}
		
		if(key == KeyEvent.VK_E){
			setShield(true);
		}
		
		if(key == KeyEvent.VK_SPACE){
			missileList.add(new Missile(x + imageWidth, y + imageHeight/2, 2));
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP) {
			dy = 0;
		}
		
		if(key == KeyEvent.VK_DOWN) {
			dy = 0;
		}
		
		if(key == KeyEvent.VK_RIGHT){
			dx = 0;
		}
		
		if(key == KeyEvent.VK_LEFT) {
			dx = 0;
		}
		
		if(key == KeyEvent.VK_E) {
			setShield(false);
		}
		
	}
	
	public void setShield(boolean status) {
		shieldStatus = status;
		shield.setActive(shieldStatus);
	}
	
	public boolean getShieldStatus() {
		return shield.getStatus();
	}
	
	public int getShieldRecharge() {
		return shield.getRecharge();
	}
	
}
