package Graphics.Aesthetics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class FancyButton extends JButton{
	
	private static final long serialVersionUID = 5214107909544213465L;

	/**
	 * @param name - Button name
	 * @param posX - X position of the button
	 * @param posY - Y postion of the button
	 * @param width - Width of the button
	 * @param height - Height of the button
	 * @param imgPath - BackGround Image path
	 * 
	 * Creates a button without border and content
	 */
	public FancyButton(String name, int posX, int posY, int width, int height, ImageIcon img, ImageIcon pressedImg) {
		setName(name);
		setBounds(posX, posY, width, height);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder());
		setFocusable(false);
		
		FancyButton f = this;
		
		f.setIcon(img);
		MouseListener startgamelistner = new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
					f.setIcon(img);
			}
			public void mouseEntered(MouseEvent e) {
					f.setIcon(pressedImg);
			}
		};
		addMouseListener(startgamelistner);
	}


	
	public void testeListener() {
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Foi clicado");
			}
		});
	}


}
