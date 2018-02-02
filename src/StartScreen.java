import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class StartScreen extends JFrame {
	
	JButton button1;
    JButton button2;
    
    public StartScreen(){
    	initUI();
    }
    
    public void initUI(){
    	setTitle("What would you like to do?");

        setLayout(null);
        
        JLabel label = new JLabel("Select the generator type:");
//        label.setFont(new Font("Georgia", Font.PLAIN, 14));
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setLocation(new Point(20, 10));
        label.setSize(300, 30);
        label.setForeground(new Color(50, 50, 25));
        label.setPreferredSize(new Dimension(100, 20));
        label.setOpaque(true);
        add(label);
        
        JButton button1 = new JButton("Quick Multi-Table Generator");
    	button1.setBounds(20, 50, 240, 50);
        button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
    			SwingUtilities.invokeLater(new Runnable() {
    	            @Override
    	            public void run() {
    	                QuickGeneratorScreen ex = new QuickGeneratorScreen();
    	                ex.setVisible(true);
    	                setVisible(false);
    	            }
    	        });
			}
		});
        add(button1);
        
        JButton button2 = new JButton("Custom Data Table Generator");
    	button2.setBounds(20, 110, 240, 50);
        button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
    			SwingUtilities.invokeLater(new Runnable() {
    	            @Override
    	            public void run() {
    	                CustomGeneratorScreen1 ex = new CustomGeneratorScreen1();
    	                ex.setVisible(true);
    	                setVisible(false);
    	            }
    	        });
			}
		});
        add(button2);
        
        setSize(300, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
	
	public static void main(String[] args){
    	
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartScreen ex = new StartScreen();
                ex.setVisible(true);
            }
        });
    	
        
    }

}
