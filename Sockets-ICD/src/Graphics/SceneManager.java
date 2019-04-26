package Graphics;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Document;

import Graphics.Scenes.LoginWindow;
import Graphics.Scenes.MainScreen;
import Graphics.Scenes.RegisterWindow;
import Server.DocumentLoader;

public class SceneManager {
	private DocumentLoader docLoad;
	private JFrame frame;
	private CardLayout cl;
	private JPanel cards;
	
	private LoginWindow loginwindow;
	private MainScreen mainscreen;
	private RegisterWindow registerwindow;
	
	private ImageIcon background;
	private Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	public final int screenWidth = size.width;
	public final int screenHeight = size.height;
	public SceneManager() {
		docLoad = new DocumentLoader();
		
		try {
			background = new ImageIcon(ImageIO.read(new File("src/Images/isel.png")).getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH));
		}catch (Exception e) {
			e.printStackTrace();
		}
		loginwindow = new LoginWindow(this);
		mainscreen = new MainScreen(this);
		registerwindow = new RegisterWindow(this);
		
		frame = new JFrame();
		cl = new CardLayout();
		cards = new JPanel(cl);
		frame.add(cards);
		

		frame.setTitle("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
		cards.add(mainscreen, "MainScreen");
		cards.add(loginwindow, "LoginWindow");
		cards.add(registerwindow, "RegisterWindow");
		
	}
	
	public boolean Login(int numero) {
		return false;
	}
	
	public boolean createPersonagem(String category) {
		return false;
	}
	
	public boolean setRegisterInfo(String email, String name, char[] password) {
		return false;
	}
	
	
	public void changeCards(String cardName) {
		cl.show(cards, cardName);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public Document getdocLoad() {
		return docLoad.getAlunosDoc();
	}
	
	public ImageIcon getBackground() {
		return background;
	}
	
}
