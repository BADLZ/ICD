package Graphics.Scenes;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;
import Graphics.Aesthetics.FancyTextField;
public class LoginWindow extends JLabel{
	
	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	
	private JTextField numberfield;
	private ImageIcon loginBtnimg, loginBtnpressedimg, registerBtnimg, registerBtnpressedimg, textfieldimg;
	
	public LoginWindow(SceneManager sm) {
		this.sm = sm;
		initialize();
	}

	private void initialize() {
		
		try {
			loginBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtn.png")));
			loginBtnpressedimg = new ImageIcon(
					ImageIO.read(new File("src/Images/loginBtnpressed.png")));
			registerBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/registerBtn.png")));
			registerBtnpressedimg = new ImageIcon(
					ImageIO.read(new File("src/Images/registerBtnpressed.png")));
			textfieldimg = new ImageIcon(ImageIO.read(new File("src/Images/textfieldimg.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setIcon(sm.getBackground());
		numberfield = new FancyTextField("Enter Your Number", sm.screenWidth / 2 - 121, sm.screenHeight / 2 - 73, 240,
				30);
		add(numberfield);

		JLabel img1 = new JLabel(textfieldimg);
		img1.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 135, 420, 150);
		add(img1);

		FancyButton btnLogin = new FancyButton("btnLogin", sm.screenWidth / 2 - 100, sm.screenHeight / 2 , 200, 67,
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
		if (numberfield.getText() != null) {
			String number = numberfield.getText();

			//caso conseguir fazer login
			if(sm.Login(Integer.parseInt(number)))
				sm.changeCards("Room");
		}
	}
	
	
}
