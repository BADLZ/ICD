package legacyCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class CountDownProgressBar {

    Timer timer;
    JProgressBar progressBar;

    
    private int MAX_VALUE = 5;
    private int counter = MAX_VALUE;
    public CountDownProgressBar() {
    	
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, MAX_VALUE);
        progressBar.setValue(0);
        ActionListener listener = new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                counter--;
                progressBar.setValue(counter);
                if (counter<0) {
                    timer.stop();
                    progressBar.setVisible(false);
                }
                progressBar.setString("Seconds Remaining: " + String.valueOf(counter));
            }
        };
        timer = new Timer(1000, listener);
        timer.start();
        JLabel l = new JLabel();
        Font d = new Font("Consolas", Font.PLAIN, 15);
        
        progressBar.setFont(d);
        progressBar.setForeground(Color.GREEN);
        progressBar.setString("Seconds Remaining: " + String.valueOf(counter));
        progressBar.setStringPainted(true);
        progressBar.setBounds(150, 225, 200, 50);
        l.add(progressBar);
        
        JFrame frame = new JFrame();
        frame.add(l);

        frame.setBackground(Color.BLACK);
        frame.setBounds(100,100,1024,720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                CountDownProgressBar cdpb = new CountDownProgressBar();
            }
        });
    }
}