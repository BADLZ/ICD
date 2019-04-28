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
import Graphics.Scenes.TeacherDashboard;
import Graphics.Scenes.TeacherLoginWindow;
import Graphics.Scenes.WaintingRoom;

public class SceneManager {
	private ClienteAluno c;
	private ClienteProfessor p;

	private JFrame frame;
	private CardLayout cl;
	private JPanel cards;

	private LoginWindow loginwindow;
	private MainScreen mainscreen;
	private RegisterWindow registerwindow;
	private TeacherDashboard teacherdashboard;
	private TeacherLoginWindow teacherloginwindow;
	private WaintingRoom waitingroom;

	private ImageIcon background;
	private Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	public final int screenWidth = size.width;
	public final int screenHeight = size.height;
	
	private String activeCard = "";

	public SceneManager() {
		c = new ClienteAluno();
		p = new ClienteProfessor();

		try {
			background = new ImageIcon(ImageIO.read(new File("src/Images/isel.png")).getScaledInstance(screenWidth,
					screenHeight, Image.SCALE_SMOOTH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		loginwindow = new LoginWindow(this, c);
		mainscreen = new MainScreen(this);
		registerwindow = new RegisterWindow(this, c, p);
		teacherdashboard = new TeacherDashboard(this, p);
		teacherloginwindow = new TeacherLoginWindow(this);
		waitingroom = new WaintingRoom(this, c);

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
		cards.add(teacherdashboard, "TeacherDashboard");
		cards.add(teacherloginwindow, "TeacherLoginWindow");
		cards.add(waitingroom, "WaitingRoom");
	}

	public String getActive() {
		return activeCard;
	}
	public void changeCards(String cardName) {
		activeCard = cardName;
		cl.show(cards, cardName);
	}

	public JFrame getFrame() {
		return frame;
	}

	public ImageIcon getBackground() {
		return background;
	}

}
