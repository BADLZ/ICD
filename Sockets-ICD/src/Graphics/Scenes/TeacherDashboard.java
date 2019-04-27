package Graphics.Scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
	private ImageIcon alunosimg, alunospressedimg, listarimg, listarpressedimg;

	public static void main(String args[]) {

	}

	public TeacherDashboard(SceneManager sm, ClienteAluno c, ClienteProfessor p) {
		this.sm = sm;
		this.c = c;
		this.p = p;
		initialize();
	}

	private void initialize() {
		setIcon(sm.getBackground());
		try {
			alunosimg = new ImageIcon(ImageIO.read(new File("src/Images/alunosimg.png")));
			alunospressedimg = new ImageIcon(ImageIO.read(new File("src/Images/alunosimgpressed.png")));
			listarimg = new ImageIcon(ImageIO.read(new File("src/Images/listarimg.png")));
			listarpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/listarimgpressed.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		FancyButton alunosbtn = new FancyButton("alunosbtn", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 17, 200,
				67, alunosimg, alunospressedimg);

		alunosbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				listarAlunos();
			}
		});
		add(alunosbtn);

		FancyButton listarbtn = new FancyButton("listarbtn", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 93, 200,
				67, listarimg, listarpressedimg);
		listarbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				listarAlunos();
			}
		});
		add(listarbtn);
	}

	private void listarAlunos() {
		String[] alunos = p.getAlunos().split("-");
		if (alunos != null) {
			TreeAlunos("Lista de Alunos Activos", "Alunos", alunos);
		}
	}

	public void TreeAlunos(String title,String defaultRoot, String[] numbers) {

		JFrame j = new JFrame();
		j.setTitle(title);
		// create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(defaultRoot);
		
		for(int i = 0 ; i < numbers.length ; i++) {
			root.add(new DefaultMutableTreeNode(numbers[i]));
		}

		JTree tree = new JTree(root);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
		    @Override
		    public void valueChanged(TreeSelectionEvent e) {
		        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		        System.out.println(selectedNode.getUserObject().toString());
		    }

		});
		
		j.add(tree);
		j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j.setBounds(sm.screenWidth/2 - 150, sm.screenHeight/2 - 250, 300, 500);
		j.setVisible(true);
	}

	private void tryLogin() {
		String s = numberfield.getText();
		if (s.charAt(0) == 'p') {
			sm.changeCards("TeacherLoginWindow");
		}
		if (s != null && c.Login(s)) {
			sm.changeCards("WaitingRoom");
		} else {

			error.setText("O número que introduziu não é válido");
			error.setOpaque(true);
		}
	}

}
