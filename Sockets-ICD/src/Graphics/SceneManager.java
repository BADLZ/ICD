package Graphics;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Client.Aluno.ClienteAluno;
import Client.Professor.ClienteProfessor;
import Graphics.Scenes.LoginWindow;
import Graphics.Scenes.MainScreen;
import Graphics.Scenes.RegisterWindow;

public class SceneManager {
	private ClienteAluno c;
	private ClienteProfessor p;

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
		c = new ClienteAluno();
		p = new ClienteProfessor();

		try {
			background = new ImageIcon(ImageIO.read(new File("src/Images/isel.png")).getScaledInstance(screenWidth,
					screenHeight, Image.SCALE_SMOOTH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		loginwindow = new LoginWindow(this, c, p);
		mainscreen = new MainScreen(this);
		registerwindow = new RegisterWindow(this, c, p);

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

	public ImageIcon getBackground() {
		return background;
	}

}
