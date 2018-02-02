import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class QuickGeneratorScreen extends JFrame {
	
	public final static int MAX_VARIANTS = 30;
	public final static int TOTAL_PRODUCTS = 50;
	
	public QuickGeneratorScreen(){
		initUI();
	}
	
	public void initUI(){
		
		setTitle("Select number of goods per calloff");

        setLayout(null);
        
        setSize(300, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        JLabel label = new JLabel("<html>Select the initial product quantities to be scheduled in the calloff preview <u>per calloff</u>:</html>");
//      label.setFont(new Font("Georgia", Font.PLAIN, 14));
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setLocation(new Point(20, 5));
        label.setSize(250, 30);
        label.setForeground(new Color(50, 50, 25));
        label.setPreferredSize(new Dimension(100, 20));
        label.setOpaque(true);
        add(label);
        
        final JTextField textField = new JTextField("1");
        textField.setBounds(45, 45, 30, 25);
        add(textField);
        
        JButton button = new JButton("GO!");
        button.setBounds(85, 45, 60, 25);
        button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int numProds = Integer.parseInt(textField.getText());
				for (int v = 1; v <= MAX_VARIANTS; v++){
					
					int[] base_calloff_quantities = new int[v];
					for (int i = 0; i < v; i++)
						base_calloff_quantities[i] = 0;
					int numProdsTemp = 0;
					int j = 0;
					for (int i = 1; i <= TOTAL_PRODUCTS; i++){
						if (j >= base_calloff_quantities.length)
							j = 0;
						base_calloff_quantities[j]++;
						j++;
					}
					new TableGenerator().generateDataTables(numProds, v, base_calloff_quantities, 
							new int[v], new int[v], new int[v], 5);
				}
				System.exit(0);
			}
		});
        add(button);
	}
}
