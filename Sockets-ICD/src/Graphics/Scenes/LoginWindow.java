package Graphics.Scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;
import Graphics.Aesthetics.FancyTextField;
import Server.Login;

public class LoginWindow extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;

	private JTextArea error;
	private JTextField numberfield;
	private ImageIcon loginBtnimg, loginBtnpressedimg, textfieldimg;

	public LoginWindow(SceneManager sm) {
		this.sm = sm;
		initialize();
	}

	private void initialize() {

		try {
			loginBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtn.png")));
			loginBtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtnpressed.png")));
			textfieldimg = new ImageIcon(ImageIO.read(new File("src/Images/textfieldimg.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		setIcon(sm.getBackground());
		numberfield = new FancyTextField("Enter Your Number", sm.screenWidth / 2 - 121, sm.screenHeight / 2 - 73, 240,
				30);
		add(numberfield);

		error = new JTextArea();
		error.setBounds(sm.screenWidth / 2 - 121, sm.screenHeight / 2 - 150, 240,
				30);
		Font font = new Font("Consolas", Font.PLAIN, 12);
		error.setFont(font);
		error.setForeground(Color.red);
		
		error.setBounds(sm.screenWidth / 2 - 121, sm.screenHeight / 2 - 150, 240, 30);
		add(error);
		
		
		JLabel img1 = new JLabel(textfieldimg);
		img1.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 135, 420, 150);
		add(img1);

		FancyButton btnLogin = new FancyButton("btnLogin", sm.screenWidth / 2 - 100, sm.screenHeight / 2, 200, 67,
				loginBtnimg, loginBtnpressedimg);

		
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tryLogin();
			}
		});
		btnLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					tryLogin();
			}
		});
		add(btnLogin);
	}

	private void tryLogin() {
		if(!(numberfield.getText().contentEquals("Enter Your Number")) 
				&& numberfield.getText() != null 
				&& Login.alunoExiste(sm.getdocLoad(), numberfield.getText())) {
			sm.changeCards("WaitingQuestions");
		}else {
			error.setText("O utilizador não existe");
		}
	}

}
