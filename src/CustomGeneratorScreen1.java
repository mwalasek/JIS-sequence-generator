import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; 
import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date; 
import java.util.GregorianCalendar;
import java.util.Random;











import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
//import jxl.*; 
//import jxl.write.*;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class CustomGeneratorScreen1 extends JFrame
{	
	
	public int numProds = 3;
	public final static int[] BASE_CALLOFF_QUANTITIES = {25, 15, 10};
	public final static int CALLOFFS_PER_DAY = 5;
	public final static int DAY_COUNT = 365;
	public final static int MAX_DAILY_FLUCTUATION = 10;
	public final static int MAX_DEVIATION_FROM_PREVIEW_PER_CALLOFF = 2;
	
	public CustomGeneratorScreen1(){
		
		initUI();
		
	}
	
	public void initUI(){
		setTitle("Select the number of products");

        setLayout(null);
        
        JLabel label = new JLabel("Select the number of variants in your JIS model:");
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setLocation(new Point(20, 10));
        label.setSize(300, 30);
        label.setForeground(new Color(50, 50, 25));
        label.setPreferredSize(new Dimension(100, 20));
        label.setOpaque(true);
        add(label);
        
        for (int i = 1; i <= 30; i++){
        	JButton button = new JButton(Integer.toString(i));
        	button.setBounds(20 + ((i-1) % 5)*55, 50 + 40 * Math.round((i-1)/5), 50, 25);
        	ActionListener listener = new SelectProdNumListener();
            button.addActionListener(listener);
            add(button);
        }

        setSize(425, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

	}
	
	public class SelectProdNumListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			numProds = Integer.parseInt(((JButton) e.getSource()).getText());
			System.out.println("Number products: " + numProds);
			
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                CustomGeneratorScreen2 ex = new CustomGeneratorScreen2(numProds);
	                ex.setVisible(true);
	                setVisible(false);
	            }
	        });
		}
	}
	
}
