package Graphics.Scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import Client.Professor.ClienteProfessor;
import Graphics.SceneManager;
import Graphics.Aesthetics.FancyButton;

public class TeacherDashboard extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	private ClienteProfessor p;

	private ImageIcon alunosimg, alunospressedimg, listarimg, listarpressedimg, enviarbtnimg, enviarbtnpressedimg;

	private HashMap<String, String> question = new HashMap<String, String>();

	private JLabel p1,p2, catLabel, alunoLabel;
	public TeacherDashboard(SceneManager sm, ClienteProfessor p) {
		this.sm = sm;
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
			enviarbtnimg = new ImageIcon(ImageIO.read(new File("src/Images/enviarbtn.png")));
			enviarbtnpressedimg = new ImageIcon(ImageIO.read(new File("src/Images/enviarbtnpressed.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Font font = new Font("Consolas", Font.BOLD, 25);
		
		JLabel j = new JLabel();
		j.setBounds(sm.screenWidth / 2 - 200, 30, 400, 200);
		j.setBackground(Color.gray);
		j.setOpaque(true);

		p1 = new JLabel("Pergunta selecionada:");
		p1.setBounds(10, 10, 400, 30);
		p1.setFont(font);
		
		p2 = new JLabel("Aluno(s) selecionado(s):");
		p2.setBounds(10, 100, 400, 30);
		p2.setFont(font);

		catLabel = new JLabel();
		catLabel.setBounds(10, 40, 400, 30);
		catLabel.setForeground(Color.BLUE);
		catLabel.setFont(font);
		
		alunoLabel = new JLabel();
		alunoLabel.setBounds(10, 130, 400, 30);
		alunoLabel.setForeground(Color.BLUE);
		alunoLabel.setFont(font);
	
		j.add(p1);
		j.add(p2);
		j.add(catLabel);
		j.add(alunoLabel);

		add(j);

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
				listarPerguntas();
			}
		});
		add(listarbtn);
		
		FancyButton enviarbtn = new FancyButton("enviarbtn", sm.screenWidth / 2 - 100, sm.screenHeight / 2 + 169, 200,
				67, enviarbtnimg, enviarbtnpressedimg);
		enviarbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(catLabel.getText()!=null && alunoLabel.getText()!=null) {
					p.SendQuestions(catLabel.getText(), alunoLabel.getText());
				}
			}
		});
		add(enviarbtn);
	}

	private void listarPerguntas() {

		String[] perguntas = p.getQuestionsFromServer().split("_");
		for (int i = 0; i < perguntas.length; i++) {
			String[] cp = perguntas[i].split("-");
			question.put(cp[0], cp[1]);
		}
		TreePerguntas("Lista de Perguntas", "Categorias");

	}

	private void listarAlunos() {
		String[] alunos = p.getAlunos().split("-");
		if (alunos != null) {
			TreeAlunos("Lista de Alunos Activos", "Alunos", alunos);
		}
	}

	private void TreePerguntas(String title, String defaultRoot) {

		JFrame j = new JFrame();
		j.setTitle(title);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(defaultRoot);

		ArrayList<String> cat = new ArrayList<String>();

		for (Map.Entry<String, String> entry : question.entrySet()) {
			if (!cat.contains(entry.getValue()))
				cat.add(entry.getValue());
		}
		for (String c : cat) {
			DefaultMutableTreeNode categorias = new DefaultMutableTreeNode(c);
			for (Map.Entry<String, String> entry : question.entrySet()) {
				if (entry.getValue().equals(c)) {
					categorias.add(new DefaultMutableTreeNode(entry.getKey()));
				}
			}
			root.add(categorias);
		}

		JTree tree = new JTree(root);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (!cat.contains(selectedNode.getUserObject().toString())) {
					catLabel.setText(selectedNode.getUserObject().toString());
				}

			}

		});

		j.add(tree);
		j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j.setBounds(sm.screenWidth / 2 - 150, sm.screenHeight / 2 - 250, 300, 500);
		j.setVisible(true);
	}

	private void TreeAlunos(String title, String defaultRoot, String[] numbers) {

		JFrame j = new JFrame();
		j.setTitle(title);
		// create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(defaultRoot);

		for (int i = 0; i < numbers.length; i++) {
			root.add(new DefaultMutableTreeNode(numbers[i]));
		}
		root.add(new DefaultMutableTreeNode("Todos"));
		JTree tree = new JTree(root);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				alunoLabel.setText(selectedNode.getUserObject().toString());
			}

		});

		j.getContentPane().add(tree);
		j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j.setBounds(sm.screenWidth / 2 - 150, sm.screenHeight / 2 - 250, 300, 500);
		j.setVisible(true);
	}

}
