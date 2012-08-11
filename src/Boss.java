import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Boss {
	
	private static final double FIRE_PROBABILITY = 0.98;
	private static final int MISSILE_SPEED = -2;
	
	private int x, y;
	private int health;
	private Image image;
	private boolean visible, up;
	private ArrayList<Missile> missileList;
	
	public Boss() {
		
		try {
		ImageIcon ii = new ImageIcon(this.getClass().getResource("boss.png"));
		image = ii.getImage();
		} catch(Exception e) {
			System.err.println("boss.png not found");
		}
		
		missileList = new ArrayList<Missile>();
		
		x = 500;
		y = 250;
		
		health = 20;
		up = visible = true;
	}
	
	public void move() {
		
		if(up) {
			y++;
			if(y > 350)
				up = false;
		} else {
			y--;
			if(y < 30)
			up = true;
		}

	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void fire() {
		
		if(Math.random() > FIRE_PROBABILITY) {
			missileList.add(new Missile(x, y + 80/2, MISSILE_SPEED));
		}
	}
	
	public void decrementHealth() {
		health = health - 1;
	}
	
	public int getHealth() {
		return health;
	}
	
	public ArrayList<Missile> getMissiles() {
		return missileList;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, 80, 80);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		
	}
	
	public boolean isVisible() {
		return visible;
	}
	
}
