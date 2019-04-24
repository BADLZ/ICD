package Graphics.Scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;

public class MainScreen extends JLabel {

	final String host = "localhost";
	final int port = 5025;
	
	private SceneManager sm;
	private JLabel server;
	private ImageIcon quitBtnimg, quitBtnpressedimg, loginBtnimg, loginBtnpressedimg, registerBtnimg,
			registerBtnpressedimg, serveron, serveroff;

	private Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	private final int screenWidth = size.width;
	private final int screenHeight = size.height;

	private boolean flag;
	public MainScreen(SceneManager sm) {
		this.sm = sm;
		initialize();
		

		
		Timer time = new Timer();
		time.schedule(new TimerTask() {
			
			@Override
			public void run() {
				ping();
			}
		}, 0, 5000);
	}

	private void ping() {
		try {
			Socket s = new Socket(host, port);
			flag = true;
			server.setIcon(serveron);
		} catch (IOException e) {
			flag = false;
			server.setIcon(serveroff);
		}
		
	}
	private void initialize() {
		initializeImages();
		setIcon(sm.getBackground());
		
		server = new JLabel();
		server.setBounds(screenWidth - 200 , 10, 150, 150);
		add(server);
		ping();
		
		
		
		FancyButton quitBtn = new FancyButton("Meu Botao", screenWidth / 2 - 100, screenHeight / 2 + 100, 200, 67,
				quitBtnimg, quitBtnpressedimg);
		add(quitBtn);
		quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sm.getFrame().dispose();
			}
		});

		FancyButton loginbtn = new FancyButton("loginbtn", screenWidth / 2 - 100, screenHeight / 2 - 100, 200, 67,
				loginBtnimg, loginBtnpressedimg);
		loginbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sm.changeCards("LoginWindow");
			}
		});
		add(loginbtn);

		FancyButton registerbtn = new FancyButton("registerbtn", screenWidth / 2 - 100, screenHeight / 2, 200, 67,
				registerBtnimg, registerBtnpressedimg);
		registerbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sm.changeCards("RegisterWindow");
			}
		});
		add(registerbtn);
		
		FancyButton quitbtn = new FancyButton("quitbtn", screenWidth / 2 - 100, screenHeight / 2 + 100, 200, 67,
				quitBtnimg, quitBtnpressedimg);
		quitbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sm.getFrame().dispose();
			}
		});
		add(quitbtn);
		
		
	}

	private void initializeImages() {
		try {
			loginBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtn.png")));
			loginBtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtnpressed.png")));
			registerBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/registerBtn.png")));
			registerBtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/registerBtnpressed.png")));
			quitBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/quitBtn.png")));
			quitBtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/quitBtnpressed.png")));
			serveron =  new ImageIcon(ImageIO.read(new File("src/Images/server_ok.png")));
			serveroff =  new ImageIcon(ImageIO.read(new File("src/Images/server_error.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
