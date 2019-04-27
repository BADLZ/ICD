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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import Client.Aluno.ClienteAluno;
import Client.Professor.ClienteProfessor;
import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;
import Graphics.Aesthetics.FancyTextField;
import javax.swing.JList;
import javax.swing.JTree;

public class TeacherDashboard extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	private ClienteAluno c;
	private ClienteProfessor p;
	
	private static JTree tree;
	private JLabel error;
	private JTextField numberfield;
	private ImageIcon loginBtnimg, loginBtnpressedimg, btnVoltarimg, btnVoltarpressedimg, textfieldimg;
	
	
	public static void main(String args[]) {
		TeacherDashboard.TreeExample();
	}
	public TeacherDashboard(SceneManager sm, ClienteAluno c, ClienteProfessor p) {
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
		numberfield = new FancyTextField("Enter Your Number", sm.screenWidth / 2 - 95, sm.screenHeight / 2 - 73, 195,
				30);
		add(numberfield);

		error = new JLabel("", SwingConstants.CENTER);
		Font font = new Font("Consolas", Font.BOLD, 12);
		error.setFont(font);
		error.setForeground(Color.red);
		error.setBounds(sm.screenWidth / 2 - 121, sm.screenHeight / 2 - 12, 270, 30);
		add(error);

		JLabel img1 = new JLabel(textfieldimg);
		img1.setBounds(sm.screenWidth / 2 - 210, sm.screenHeight / 2 - 135, 420, 150);
		add(img1);

		FancyButton btnLogin = new FancyButton("btnLogin", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 17, 200, 67,
				loginBtnimg, loginBtnpressedimg);

		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TreeExample();
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
	  public static void TreeExample()
	    {
		   
	        JFrame j = new JFrame();
	        //create the root node
	        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Categorias");
	        //create the child nodes
	        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
	        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
	        //add the child nodes to the root node
	        root.add(vegetableNode);
	        root.add(fruitNode);
	         
	        //create the tree by passing in the root node
	        tree = new JTree(root);
	        j.add(tree);
	      
	        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        j.setTitle("Lista de Perguntas");       
	        j.setVisible(true);
	    }

	private void tryLogin() {
		String s = numberfield.getText();
		if(s.charAt(0) == 'p') {
			sm.changeCards("TeacherLoginWindow");
		}
		if (s != null && c.Login(s)) {
			sm.changeCards("WaitingRoom");
		} else {

			error.setText("O n�mero que introduziu n�o � v�lido");
			error.setOpaque(true);
		}
	}

}
