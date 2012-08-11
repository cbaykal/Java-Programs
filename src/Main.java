import javax.swing.JApplet;

/*Code to use an applet to launch the application by adding the panel */

public class Main extends JApplet{
	public void init() {
		add(new GameUI());
	}

}
