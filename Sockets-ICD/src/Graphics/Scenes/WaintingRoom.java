package Graphics.Scenes;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Client.Aluno.ClienteAluno;
import Graphics.SceneManager;

public class WaintingRoom extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	private ClienteAluno c; 
	
	private JLabel p,o1,o2,o3;
	
	public WaintingRoom(SceneManager sm, ClienteAluno c) {
		this.sm = sm;
		this.c = c;
		initialize();
	}

	private void initialize() {
		setIcon(sm.getBackground());
		
		
		
		JLabel j = new JLabel("Waiting for teacher...", SwingConstants.CENTER);
		Font f1 = new Font("Consolas", Font.BOLD,40);
		j.setBounds(0, 30, sm.screenWidth, 200);
		j.setBackground(Color.gray);
		j.setOpaque(true);
		

		Font font = new Font("Consolas", Font.PLAIN,20);
		Font font1 = new Font("Consolas", Font.BOLD,20);
		JLabel j1 = new JLabel();
		j.setBounds(sm.screenWidth / 2 - 200, 30, 400, 200);
		j.setBackground(Color.gray);
		j.setOpaque(true);
		
		p = new JLabel();
		p.setBounds(10, 10, 400, 30);
		p.setFont(font1);
		
		o1 = new JLabel();
		o1.setBounds(10, 40, 400, 30);
		o1.setFont(font);

		o2 = new JLabel();
		o2.setBounds(10, 70, 400, 30);
		o2.setForeground(Color.BLUE);
		o2.setFont(font);
		
		o3 = new JLabel();
		o3.setBounds(10, 100, 400, 30);
		o3.setForeground(Color.BLUE);
		o3.setFont(font);
	
		j1.add(p);
		j1.add(o1);
		j1.add(o2);
		j1.add(o3);

		add(j);
		add(j1);
		
		String rec = c.ReceiveQuestion();
		if (rec != null) {
			System.out.println(rec);
		}
	}



}
