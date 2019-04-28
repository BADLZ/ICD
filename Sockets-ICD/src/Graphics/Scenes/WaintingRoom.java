package Graphics.Scenes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import Client.Aluno.ClienteAluno;
import Graphics.SceneManager;

public class WaintingRoom extends JLabel {

	private static final long serialVersionUID = 1L;

	private SceneManager sm;
	private ClienteAluno c;

	private int MAX_VALUE = 30;
	private int counter = MAX_VALUE;

	private javax.swing.Timer timer;
	private JProgressBar progressBar;
	private JLabel p, o1, o2, o3, j;

	public WaintingRoom(SceneManager sm, ClienteAluno c) {
		this.sm = sm;
		this.c = c;
		initialize();
	}

	private void initialize() {
		setIcon(sm.getBackground());

		j = new JLabel("Waiting for teacher...", SwingConstants.CENTER);
		Font f1 = new Font("Consolas", Font.BOLD, 40);
		j.setBounds(0, 30, sm.screenWidth, 100);
		j.setFont(f1);
		j.setBackground(Color.gray);
		j.setOpaque(true);

		Font font = new Font("Consolas", Font.PLAIN, 20);
		Font font1 = new Font("Consolas", Font.BOLD, 20);
		JLabel j1 = new JLabel();
		j1.setBounds(sm.screenWidth / 2 - 200, sm.screenHeight / 2 - 100, 400, 200);
		j1.setBackground(Color.gray);
		j1.setOpaque(true);

		p = new JLabel();
		p.setBounds(10, 10, 400, 30);
		p.setFont(font1);

		o1 = new JLabel();
		o1.setBounds(10, 50, 400, 30);
		o1.setFont(font);
		o1.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				Font font = new Font("Consolas", Font.PLAIN, 20);
				o1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				o1.setFont(font);
				o1.setText(o2.getText());

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Font font = new Font("Consolas", Font.BOLD, 22);
				o1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				o1.setFont(font);
				o1.setText(o2.getText());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		o2 = new JLabel();
		o2.setBounds(10, 80, 400, 30);
		o2.setFont(font);
		o2.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				Font font = new Font("Consolas", Font.PLAIN, 20);
				o2.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				o2.setFont(font);
				o2.setText(o2.getText());

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Font font = new Font("Consolas", Font.BOLD, 22);
				o2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				o2.setFont(font);
				o2.setText(o2.getText());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		o3 = new JLabel();
		o3.setBounds(10, 110, 400, 30);
		o3.setFont(font);
		o3.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				Font font = new Font("Consolas", Font.PLAIN, 20);
				o3.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				o3.setFont(font);
				o3.setText(o2.getText());

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Font font = new Font("Consolas", Font.BOLD, 22);
				o3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				o3.setFont(font);
				o3.setText(o2.getText());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		j1.add(p);
		j1.add(o1);
		j1.add(o2);
		j1.add(o3);

		add(j);
		add(j1);

		Timer time = new Timer();
		time.schedule(new TimerTask() {

			@Override
			public void run() {
				if (sm.getActive().equals("WaitingRoom")) {
					String rec = c.ReceiveQuestion();
					if (rec != null) {
						j.setText("Time Remaining");
						String[] sep = rec.split("-");
						p.setText(sep[0] + ":");
						o1.setText("-" + sep[1]);
						o2.setText("-" + sep[2]);
						o3.setText("-" + sep[3]);
						this.cancel();
					}
				}

			}
		}, 0, 500);

	}

	public void CountDownProgressBar() {

		progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, MAX_VALUE);
		progressBar.setVisible(true);
		progressBar.setValue(0);
		ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				counter--;
				progressBar.setValue(counter);
				if (counter < 0) {
					timer.stop();
					progressBar.setVisible(false);
				}
				progressBar.setString("Seconds Remaining: " + String.valueOf(counter));
			}
		};
		javax.swing.Timer timer = new javax.swing.Timer(1000, listener);
		timer.start();
		JLabel l = new JLabel();
		Font d = new Font("Consolas", Font.PLAIN, 15);
		progressBar.setFont(d);
		progressBar.setForeground(Color.GREEN);
		progressBar.setString("Seconds Remaining: " + String.valueOf(counter));
		progressBar.setStringPainted(true);
		progressBar.setBounds(sm.screenWidth/2 - sm.screenWidth/8, 50, sm.screenWidth/4, 100);
		l.add(progressBar);
		add(l);

	}

}
