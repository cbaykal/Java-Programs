import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;


public class Alien {

	private static final int FRAME_WIDTH = 600;
	private static final int SPEED = -1;
	
	private int x, y;
	private int imageWidth, imageHeight;
	private Image image;
	
	public Alien(int x, int y) {
		
		try{
			ImageIcon ii = new ImageIcon(this.getClass().getResource("alien.png"));
			image = ii.getImage();
		} catch(Exception e) {
			System.err.println("Could not find alien.png");
		}
		
		imageWidth = image.getWidth(null);
		imageHeight = image.getHeight(null);
		
		this.x = x;
		this.y = y;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setNewPosition(int x, int y) {
		Random rand = new Random();
		this.x = x;
		if(y <= 40) {
			this.y = rand.nextInt(20) + y;
		} else {
			this.y = rand.nextInt(40) - 20 + y;
		}
		
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void move() {
		
		x += SPEED;
		if(x < 0)
			x = FRAME_WIDTH;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, imageWidth, imageHeight);
	}
	
}
