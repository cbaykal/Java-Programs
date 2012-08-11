import javax.swing.JFrame;


public class Game extends JFrame{
	
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 450;
	
	public Game() {
		
		/*Initializing Game Frame and adding GameUI */
		
		add(new GameUI());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setTitle("Space Game");
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
