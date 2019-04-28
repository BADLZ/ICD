import Graphics.SceneManager;
import Server.Server;

public class Main {
	public static void main(String args[]) {
		new Server().start();
		new SceneManager();
		new SceneManager();
	}
}
