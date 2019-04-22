package Graphics.Aesthetics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class FancyTextField extends JTextField{

	private static final long serialVersionUID = 1L;

	public FancyTextField(String placeholder, int posX, int posY, int width, int height) {
		setBorder(BorderFactory.createEmptyBorder());
		setBounds(posX, posY, width, height);
		setText(placeholder);
		setForeground(new Color(255,255,255));
		setFont(new Font("Consolas", 0, 20));
		setOpaque(false);
		
		addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (getText().equals(placeholder)) {
		            setText("");
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (getText().isEmpty()) {
		            setText(placeholder);
		        }
		    }
		    });
	}
	
	

}
