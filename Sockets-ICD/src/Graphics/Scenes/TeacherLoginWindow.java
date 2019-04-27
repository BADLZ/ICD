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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Client.Aluno.ClienteAluno;
import Client.Professor.ClienteProfessor;
import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;
import Graphics.Aesthetics.FancyTextField;

public class TeacherLoginWindow extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	private ClienteAluno c;
	private ClienteProfessor p;

	private JLabel error;
	private JTextField numberfield;
	private ImageIcon loginBtnimg, loginBtnpressedimg, btnVoltarimg, btnVoltarpressedimg, textfieldimg;

	public TeacherLoginWindow(SceneManager sm, ClienteAluno c, ClienteProfessor p) {
		this.sm = sm;
		this.c = c;
		this.p = p;
		initialize();
	}

	private void initialize() {

		try {
			loginBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtn.png")));
			loginBtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/loginBtnpressed.png")));
			btnVoltarimg = new ImageIcon(ImageIO.read(new File("src/Images/voltarBtn.png")));
			btnVoltarpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/voltarBtnpressed.png")));
			textfieldimg = new ImageIcon(ImageIO.read(new File("src/Images/textfieldimg.png")));

		} catch (Exception e) {
			e.printStackTrace();
		}

		setIcon(sm.getBackground());

		JLabel warning = new JLabel("", SwingConstants.CENTER);
		warning.setForeground(Color.red);
		warning.setFont(new Font("Consolas", Font.BOLD, 19));
		warning.setText("If you are not a teacher please return to the mainscreen");
		warning.setBounds(sm.screenWidth / 2, 40, sm.screenWidth, 30);

		numberfield = new FancyTextField("Enter Your Password", sm.screenWidth / 2 - 95, sm.screenHeight / 2 - 73, 195,
				30);
		add(numberfield);

		JLabel img1 = new JLabel(textfieldimg);
		img1.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 135, 420, 150);
		add(img1);

		FancyButton btnLogin = new FancyButton("btnLogin", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 17, 200, 67,
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

		FancyButton btnVoltar = new FancyButton("btnVoltar", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 93, 200,
				67, btnVoltarimg, btnVoltarpressedimg);
		btnVoltar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sm.changeCards("MainScreen");
			}
		});
		add(btnVoltar);
	}

	private void tryLogin() {
		String s = numberfield.getText();
		if (s.equals("12345")) {
			sm.changeCards("TeacherDashboard");
		}
	}

}
