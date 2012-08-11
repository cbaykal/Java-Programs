import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;


public class Missile {

	private static final int FRAME_WIDTH = 600;
	
	private int x, y, speed;
	private int imageWidth, imageHeight;
	private boolean visible;
	private Image image;
	
	public Missile(int x, int y, int speed) {
		
		try {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("missile.png"));
		image = ii.getImage();
		} catch(Exception e) {
			System.err.println("Error: could not find missile.png");
		}
		
		imageWidth = image.getWidth(null);
		imageHeight = image.getHeight(null);
		
		this.x = x;
		this.y = y;
		this.speed = speed;
		visible = true;
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
	
	public void move() {
		x += speed;
		if(x > FRAME_WIDTH)
			visible = false;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, imageWidth, imageHeight);
	}
	
	
}
