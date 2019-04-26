package Graphics.Scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Client.Aluno.ClienteAluno;
import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;
import Graphics.Aesthetics.FancyTextField;

public class RegisterWindow extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	private ClienteAluno c;
	
	private JLabel error;
	private JTextField numberfield, birthdayfield, namefield;
	private ImageIcon registerBtnimg, registerBtnpressedimg, btnVoltarimg, btnVoltarpressedimg, textfieldimg;

	public RegisterWindow(SceneManager sm) {
		this.sm = sm;
		c = new ClienteAluno();
		initialize();
	}

	private void initialize() {

		try {
			registerBtnimg = new ImageIcon(ImageIO.read(new File("src/Images/registerBtn.png")));
			registerBtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/registerBtnpressed.png")));
			btnVoltarimg = new ImageIcon(ImageIO.read(new File("src/Images/voltarBtn.png")));
			btnVoltarpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/voltarBtnpressed.png")));
			textfieldimg = new ImageIcon(ImageIO.read(new File("src/Images/textfieldimg.png")));

		} catch (Exception e) {
			e.printStackTrace();
		}

		setIcon(sm.getBackground());
		
		namefield = new FancyTextField("Enter Your Name", sm.screenWidth / 2 - 95, sm.screenHeight / 2 - 250, 195,
				30);
		add(namefield);
		
		JLabel img1 = new JLabel(textfieldimg);
		img1.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 310, 420, 150);
		add(img1);
		
		numberfield = new FancyTextField("Enter Your Number", sm.screenWidth / 2 - 95, sm.screenHeight / 2 - 150, 195,
				30);
		add(numberfield);
		
		JLabel img2 = new JLabel(textfieldimg);
		img2.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 210, 420, 150);
		add(img2);
		
		birthdayfield = new FancyTextField("Birthday(dd/mm/yyyy)", sm.screenWidth / 2 - 95, sm.screenHeight / 2 - 50, 198,
				30);
		birthdayfield.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(birthdayfield.getText().length() == 2) {
					birthdayfield.setText(birthdayfield.getText() + "/");
				}
				if(birthdayfield.getText().length() == 5) {
					birthdayfield.setText(birthdayfield.getText() + "/");
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		add(birthdayfield);
		
		JLabel img3 = new JLabel(textfieldimg);
		img3.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 110, 420, 150);
		add(img3);


		error = new JLabel("", SwingConstants.CENTER);
		Font font = new Font("Consolas", Font.BOLD, 12);
		error.setFont(font);
		error.setForeground(Color.red);
		error.setBounds(sm.screenWidth / 2 - 121, sm.screenHeight / 2 - 12, 270, 30);
		add(error);



		FancyButton btnRegisto = new FancyButton("btnRegisto", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 35, 200, 67,
				registerBtnimg, registerBtnpressedimg);

		btnRegisto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tryLogin();
			}
		});
		btnRegisto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					tryLogin();
			}
		});
		add(btnRegisto);

		FancyButton btnVoltar = new FancyButton("btnVoltar", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 115, 200,
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
		
		if (s != null && c.Login(s)) {
			sm.changeCards("WaitingRoom");
		} else {

			error.setText("O número que introduziu não é válido");
			error.setOpaque(true);
		}
	}

}
