import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;


public class GameUI extends JPanel implements ActionListener {
	
	/*General Constants for the Game*/
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 430;
	private static final int SCOREBOARD_HEIGHT = 30;
	private static final int TIMER_DELAY = 4;
	private static final int ALIENS_QUANTITY = 50;
	private static final int START_LEEWAY = 400;
	private static final int INGAME_LEEWAY = 1000;
	private static final int THRESHOLD_SCORE = 100;
	private static final int BOSS_DIMENSIONS = 80;
	private static final int QUANTITY_LIVES = 3;
	private static final int ALIEN_KILLED_POINTS = 10;
	
	/*Constants concerning the Scoreboard*/
	private static final int SCORE_STRING_XPOS = 15;
	private static final int SCORE_STRING_YPOS = 20;
	private static final int SCORE_INT_XPOS = 80;
	private static final int SCORE_INT_YPOS = 20;
	private static final int SCORE_FONT_SIZE = 16;
	private static final int SCORE_LINE_XPOS = 25;
	private static final int LIVES_XPOS = 500;
	private static final int LIVES_YPOS = 3;
	private static final int LIVES_SPACING = 10;	
	private static final int SCOREBOARD_SHIELD_XPOS = 200;
	private static final int SCOREBOARD_SHIELD_YPOS = 6;
	private static final int SCOREBOARD_SHIELD_WIDTH = 15;
	private static final int SCOREBOARD_SHIELD_NAME_XPOS = 130;
	private static final int SCOREBOARD_SHIELD_NAME_YPOS = 20;
	private static final int SCOREBOARD_SHIELD_PERCENTAGE_XPOS = 220;
	private static final int SCOREBOARD_SHIELD_PERCENTAGE_YPOS = 19;
	private static final int SCOREBOARD_OUTER_RECTANGLE_LEEWAY = 1;
	private static final int SCOREBOARD_BOSS_HEALTH_XPOS = 310;
	private static final int SCOREBOARD_BOSS_HEALTH_YPOS = 19;
	private static final int SCOREBOARD_BOSS_HEALTH_PERCENTAGE_XPOS = 430;
	private static final int SCOREBOARD_BOSS_HEALTH_PERCENTAGE_YPOS = 19;
	private static final String SCORE_FONT = "Copperplate Gothic Light";
	
	
	
	private Craft craft;
	private Boss boss;
	private Timer timer;
	private Image craftImage;
	private boolean inGame, bossMode, endGame, initGame;
	private ArrayList<Missile> missileList;
	private ArrayList<Missile> bossMissileList;
	private ArrayList<Alien> alienList;
	private ArrayList<Explosion> explosionList;
	private int score = 0, lives = QUANTITY_LIVES;
	
	public GameUI() {
		
		/* Initializing panel */
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		/* Creating craft */
		craft = new Craft();
		craftImage = craft.getImage();
		
		/*Initialize aliens*/
		alienList = new ArrayList<Alien>();
		explosionList = new ArrayList<Explosion>();
		initAliens(ALIENS_QUANTITY, START_LEEWAY);
		
		bossMode = endGame = inGame = false;
		initGame = true;
		
		timer = new Timer(TIMER_DELAY, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		/* Don't need to check which ActionEvent here since we only have Timer */
		
		if(inGame && craft.isVisible()) // Craft shouldn't move if the game is paused or if the craft is invisible
		craft.move();
		
		if(bossMode && boss.isVisible()) {
			boss.move();
			boss.fire();
			checkBossCollisions();
		}else {
			checkCollision();
		}
		
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		/* Call paint method of the parent class*/
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g; //g2d for more functionality
		
		/*Scoreboard, missiles, and explosions should always be drawn, no matter which state*/
		
		drawScoreboard(g2d);
		drawMissiles(g2d);
		drawScoreboardBars(g2d);
		drawExplosions(g2d);
		
		/* If we haven't started the game yet, then show the intro screen.
		 * else, deal with game graphics.
		 * */
		
		if(initGame) {
			showIntro(g2d);
		}else {
		
		if(inGame && !bossMode)
		drawAliens(g2d);
		
		if(craft.getShieldStatus()) // draw shield if it's activated
			g.drawOval(craft.getX() - 5, craft.getY() - 5, craft.getImage().getWidth(null) + 8, craft.getImage().getHeight(null) + 8);
		
		
		if(craft.isVisible()) // If the craft is visible then draw it
		g2d.drawImage(craftImage, craft.getX(), craft.getY(), null);
		
		
		if(!inGame && !endGame) // If the game is not over yet, then just show paused screen and wait for enter
			showPausedScreen(g2d);
		
		if(endGame) // If endGame is true, the player has beaten the game so display the game over graphics
			gameOver(g2d);
			
		
		if(bossMode && boss.isVisible()) // draw boss if it's bossMode
			g2d.drawImage(boss.getImage(), boss.getX(), boss.getY(), BOSS_DIMENSIONS, BOSS_DIMENSIONS, null);
		
		}
		
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();
	}
	
	private boolean checkOverlap(Alien alien, int pos) {
		
		/* Method to handle placement of aliens so there is no overlap*/
		
		for(int i = 0; i < alienList.size(); i++) {
			Alien currAlien = alienList.get(i);
			if(currAlien.getRectangle().intersects(alien.getRectangle()) && i !=pos)
				return true;
		}
		return false;
	}
	
	public void checkBossCollisions() {
		
		for(int i = 0; i < missileList.size(); i++) {
			Missile currMissile = missileList.get(i);
			if(currMissile.getRectangle().intersects(boss.getRectangle())) {
				boss.decrementHealth();
				currMissile.setVisible(false);
				explosionList.add(new Explosion(currMissile.getX(), currMissile.getY()));
				if(boss.getHealth() == 0) {
					endGame = true;
					inGame = false;
					boss.setVisible(false);
					break;
				}
			}
		}
		
		for(int i = 0; i < bossMissileList.size(); i++) {
			Missile currMissile = bossMissileList.get(i);
			if(currMissile.getRectangle().intersects(craft.getRectangle())) {
				if(!craft.getShieldStatus()) {
				decrementLives();
				currMissile.setVisible(false);
				boss.setVisible(false);
				break;
				}
			}
			
		}
	}
	
	private void checkCollision() {
		
		missileList = craft.getMissiles(); // get missiles ArrayList and store them
		
		/* Code to check for collision between aliens and missiles */
		for(int i = 0; i < missileList.size(); i++) {
			Missile currMissile = missileList.get(i);
			
			if(currMissile != null && currMissile.isVisible()) {
				for(int j = 0; j < alienList.size(); j++) {
					Alien currAlien = alienList.get(j);
						if(currAlien.getRectangle().intersects(currMissile.getRectangle())) {
							currMissile.setVisible(false);
							do {
								currAlien.setNewPosition(INGAME_LEEWAY, currAlien.getY());
							}while(checkOverlap(currAlien, j));
							score += ALIEN_KILLED_POINTS;
							explosionList.add(new Explosion(currMissile.getX(), currMissile.getY()));
							if(score >= THRESHOLD_SCORE)
								initBossMode();
						}
				}
			}
		}
		
		// Code to check whether the aliens intersect the craft
		
		for(int i = 0; i < alienList.size(); i++) {
			Alien currAlien = alienList.get(i);
			if(currAlien.getRectangle().intersects(craft.getRectangle())) {
				if(!craft.getShieldStatus()) {
				decrementLives(i);
				return;
				}
			}
		}
	}
	
	private void decrementLives() {
		inGame = false;
		craft.setVisible(false);
		craft.clearMissiles();
		lives--;
	}
	
	private void decrementLives(int alienPos) {
		inGame = false;
		craft.setVisible(false);
		craft.clearMissiles();
		alienList.remove(alienPos);
		lives--;
	}
	
	private void drawScoreboardBars(Graphics2D g2d) {
		
		
		/* Code to draw shield's status bar */
		
		int status = 100 - craft.getShieldRecharge();
		g2d.setColor(Color.RED);
		g2d.fillRect(SCOREBOARD_SHIELD_XPOS, SCOREBOARD_SHIELD_YPOS, 
				status, SCOREBOARD_SHIELD_WIDTH);
		g2d.setColor(Color.WHITE);
		
		/*Code to draw outer rectangle of the shield status bar*/
		g2d.drawString(Integer.toString(status) + " %", SCOREBOARD_SHIELD_PERCENTAGE_XPOS, SCOREBOARD_SHIELD_PERCENTAGE_YPOS);
		g2d.drawRect(SCOREBOARD_SHIELD_XPOS - SCOREBOARD_OUTER_RECTANGLE_LEEWAY, 
				SCOREBOARD_SHIELD_YPOS - SCOREBOARD_OUTER_RECTANGLE_LEEWAY, 
				status + SCOREBOARD_OUTER_RECTANGLE_LEEWAY * 2,
				SCOREBOARD_SHIELD_WIDTH + SCOREBOARD_OUTER_RECTANGLE_LEEWAY * 2);
		
		/*Code to display the boss health if bossMode is true*/
		
		if(bossMode) {
			int adjustedBossHealth = boss.getHealth()*5;
			g2d.drawString("Boss Health:", SCOREBOARD_BOSS_HEALTH_XPOS, SCOREBOARD_BOSS_HEALTH_YPOS);
			g2d.setColor(Color.RED);
			g2d.drawString(Integer.toString(adjustedBossHealth) + " %", SCOREBOARD_BOSS_HEALTH_PERCENTAGE_XPOS,
					SCOREBOARD_BOSS_HEALTH_PERCENTAGE_YPOS);
			g2d.setColor(Color.WHITE); // Switch the color of the graphics object back to white from red
		}
		
		
	}
	
	
	public void drawMissiles(Graphics2D g2d) {
		
		// Code to handle missiles of craft
		
		missileList = craft.getMissiles();
		
		for(int i = 0; i < missileList.size(); i++) {
			Missile currMissile = missileList.get(i);
			if(currMissile != null && currMissile.isVisible()) {
				currMissile.move();
				g2d.drawImage(currMissile.getImage(), currMissile.getX(), currMissile.getY(), null);
			} else {
				missileList.remove(i);
			}
				
		}
		
		// Code to handle missiles of Boss
		
		if(bossMode) {
			bossMissileList = boss.getMissiles();
			
			for(int i = 0; i < bossMissileList.size(); i++) {
				Missile currMissile = bossMissileList.get(i);
				if(currMissile != null && currMissile.isVisible()) {
					currMissile.move();
					g2d.drawImage(currMissile.getImage(), currMissile.getX(), currMissile.getY(), null);
				} else {
					bossMissileList.remove(i);
				}
			}
		}
		
	}
	public void drawAliens(Graphics2D g2d) {
		// Method to draw aliens on screen when called
		for(int i = 0; i < alienList.size(); i++) {
			Alien currAlien = alienList.get(i);
				currAlien.move();
				g2d.drawImage(currAlien.getImage(), currAlien.getX(), currAlien.getY(), null);
			}
	}
	
	public void drawExplosions(Graphics2D g2d) {
		
		int explosionDimensions = 10;
		if(endGame)
			explosionDimensions = 80;
		for(int i =0; i < explosionList.size(); i++) {
			Explosion currExp = explosionList.get(i);
			if(currExp.isVisible()) {
			
			g2d.drawImage(currExp.getImage(), currExp.getX(), currExp.getY(), explosionDimensions, explosionDimensions, null);
			} else {
				explosionList.remove(i);
			}
		}
	}
	
	public void drawScoreboard(Graphics2D g2d) {
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font(SCORE_FONT, Font.ITALIC, SCORE_FONT_SIZE));
		g2d.drawString("Score: ", SCORE_STRING_XPOS, SCORE_STRING_YPOS);
		g2d.drawString(Integer.toString(score), SCORE_INT_XPOS, SCORE_INT_YPOS);
		g2d.drawLine(0, SCORE_LINE_XPOS, FRAME_WIDTH, SCORE_LINE_XPOS);
		g2d.drawString("Shield: ", SCOREBOARD_SHIELD_NAME_XPOS, SCOREBOARD_SHIELD_NAME_YPOS);
		
		
		for(int i = 0; i < lives; i++) {
			g2d.drawImage(craftImage, LIVES_XPOS + craftImage.getWidth(null)*i + i*LIVES_SPACING, LIVES_YPOS, null);
		}
	}
	
	public void gameOver(Graphics2D g2d) {
		
		inGame = false; //if the player has beaten the game both inGame and bossMode should be set to false
		endGame = true;

		/* Code to center intro text */
		String s = "YOU WON! PRESS ENTER TO PLAY AGAIN";
		FontMetrics fm   = g2d.getFontMetrics(new Font(SCORE_FONT, Font.ITALIC, SCORE_FONT_SIZE));
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(s, g2d);
		int textHeight = (int)(rect.getHeight()); 
		int textWidth  = (int)(rect.getWidth());
		int panelHeight= this.getHeight();
		int panelWidth = this.getWidth();

		// x and y will hold the position for the string
		int x = (panelWidth  - textWidth)  / 2;
		int y = (panelHeight - textHeight) / 2  + fm.getAscent();
		g2d.drawString(s, x, y);
	}
	
	public void initAliens(int quantity, int leeway) {
		
		// Code to generate aliens and add them to alienList
		
		Random rand = new Random();
		
		for(int i = 0; i < quantity; i++) {
			Alien createdAlien = new Alien(rand.nextInt(FRAME_WIDTH - 20) + leeway,
					rand.nextInt(FRAME_HEIGHT - SCOREBOARD_HEIGHT - 20) + SCOREBOARD_HEIGHT + 20);
			
			if(!checkOverlap(createdAlien, i)) {
				alienList.add(createdAlien);
			} else {
				i--;
			}
		}
	}
	
	private void initBossMode() {
		
		alienList.clear();
		boss = new Boss();
		bossMode = true;
		bossMissileList = boss.getMissiles();
	}
	
	public void showIntro(Graphics2D g2d) {
		
		/* Code to center intro text */
		
		String s = "WELCOME! PRESS ENTER TO PLAY";
		
		FontMetrics fm   = g2d.getFontMetrics(new Font(SCORE_FONT, Font.ITALIC, SCORE_FONT_SIZE));
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(s, g2d);
		
		int textHeight = (int)(rect.getHeight()); 
		int textWidth  = (int)(rect.getWidth());
		int panelHeight= this.getHeight();
		int panelWidth = this.getWidth();

		// x and y will hold the position for the string
		int x = (panelWidth  - textWidth)  / 2;
		int y = (panelHeight - textHeight) / 2  + fm.getAscent();
		g2d.drawString(s, x, y);
	}
	
	private void restartGame(boolean bossMode, int lives, int score) {
		/*Method to restart game*/
		
		alienList.clear();
		this.craft = new Craft();
		inGame = true;
		
		this.lives = lives;
		this.score = score;
		
		
		if(!bossMode) {
		// only if !bossMode
		initAliens(ALIENS_QUANTITY, START_LEEWAY);
		} else {
			if(lives == QUANTITY_LIVES || endGame) {
				this.bossMode = false;
				initAliens(ALIENS_QUANTITY, START_LEEWAY);
			} else {
			boss = new Boss();
			}
		}
		
	}
	
	private void showPausedScreen(Graphics2D g2d) {
		String gameOverString = "GAME OVER! PRESS ENTER TO PLAY AGAIN";
		String gameResumeString = "SPACECRAFT TOOK A HIT! PRESS ENTER TO RESUME";
		
		FontMetrics fm   = g2d.getFontMetrics(new Font(SCORE_FONT, Font.ITALIC, SCORE_FONT_SIZE));
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(gameOverString, g2d);
		
		int textHeight = (int)(rect.getHeight()); 
		int textWidth  = (int)(rect.getWidth());
		int panelHeight= this.getHeight();
		int panelWidth = this.getWidth();

		// x and y will hold the position for the string
		int x = (panelWidth  - textWidth)  / 2;
		int y = (panelHeight - textHeight) / 2  + fm.getAscent();
		
		/*Applying the same procedure to center gameResumeString*/
		
		FontMetrics resumeFm   = g2d.getFontMetrics(new Font(SCORE_FONT, Font.ITALIC, SCORE_FONT_SIZE));
		java.awt.geom.Rectangle2D resumeRect = fm.getStringBounds(gameResumeString, g2d);
		
		int resumeTextHeight = (int)(resumeRect.getHeight()); 
		int resumeTextWidth  = (int)(resumeRect.getWidth());
		int resumePanelHeight= this.getHeight();
		int resumePanelWidth = this.getWidth();

		// x and y will hold the position for the string
		int resumeTextX = (resumePanelWidth  - resumeTextWidth)  / 2;
		int resumeTextY = (resumePanelHeight - resumeTextHeight) / 2  + resumeFm.getAscent();
		
		if(lives == 0) {
			g2d.drawString(gameOverString, x, y);
		} else {
			g2d.drawString(gameResumeString, resumeTextX, resumeTextY);
		}
		
	}
	
	
	// Inner class to handle Key Events
	private class TAdapter extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			
			if(inGame) // if we are inGame all the key events should be transferred to the Craft Object
			craft.keyPressed(e);
			
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER && ! inGame){
				
				if(initGame) { 
					
					// the intro screen should fade if  we are here
					initGame = false;
					inGame = true;
					
				} else if(!inGame){
					
					if(!bossMode) {
						
						if(lives == 0) {
							restartGame(bossMode, QUANTITY_LIVES, 0);
						} else {
							restartGame(bossMode, lives, score);
						}
						
					} else if(bossMode) {
						if(endGame) {
							restartGame(bossMode, QUANTITY_LIVES, 0);
							endGame = false;
						} else if(lives == 0){
							restartGame(bossMode, QUANTITY_LIVES, 0);
						} else {
							restartGame(bossMode, lives, score);
						}
					}
					
				}
						
			}	
			
		}
		
		public void keyReleased(KeyEvent e) {
			craft.keyReleased(e);
		}
	}
	
}
