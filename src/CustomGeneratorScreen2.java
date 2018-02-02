import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class CustomGeneratorScreen2 extends JFrame {
	
	int numProds;
	JTextField[] qtyFields, growthFields, flucFields, devFields;
	
	JButton button;
	
	public int[] base_calloff_quantities, growth, fluctuation, deviation;
	
	public CustomGeneratorScreen2(int numProds){
		super();
		this.numProds = numProds;
		initUI();
	}
	
	public void initUI(){
		setTitle("Select initial calloff quantites");

        setLayout(null);
        
        int moreSize = 0;
        if (numProds > 5)
        	moreSize = 70 * (numProds - 5);
        
        setSize(500 + moreSize, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        base_calloff_quantities = new int[numProds];
        growth = new int[numProds];
        fluctuation = new int[numProds];
        deviation = new int[numProds];
        
        qtyFields = new JTextField[numProds];
        growthFields = new JTextField[numProds];
        flucFields = new JTextField[numProds];
        devFields = new JTextField[numProds];
        
        JLabel label = new JLabel("<html>Select the initial variant quantities to be scheduled in the calloff preview <u>per calloff</u>:</html>");
//      label.setFont(new Font("Georgia", Font.PLAIN, 14));
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setLocation(new Point(20, 5));
        label.setSize(400, 30);
        label.setForeground(new Color(50, 50, 25));
        label.setPreferredSize(new Dimension(100, 20));
        label.setOpaque(true);
        add(label);
        
        JLabel label4 = new JLabel("<html>Select the linear trend, i.e. by how many percent will the scheduled and called off quantities will grow or shrink by the end of the simulation period:</html>");
        label4.setFont(new Font("Arial", Font.BOLD, 11));
        label4.setLocation(new Point(20, 80));
        label4.setSize(400, 60);
        label4.setForeground(new Color(50, 50, 25));
        label4.setPreferredSize(new Dimension(100, 20));
        label4.setOpaque(true);
        add(label4);
        
        JLabel label2 = new JLabel("<html>Select the bound for the <u>per calloff</u> fluctuation in the called off quantities, i.e. by how many percent may the quantities scheduled per calloff vary from the trend:</html>");
        label2.setFont(new Font("Arial", Font.BOLD, 11));
        label2.setLocation(new Point(20, 175));
        label2.setSize(400, 60);
        label2.setForeground(new Color(50, 50, 25));
        label2.setPreferredSize(new Dimension(100, 20));
        label2.setOpaque(true);
        add(label2);
        
        JLabel label3 = new JLabel("<html>Select bound for the deviation of the called off quantities from the calloff preview, i.e. by how many percent may the actual called off quantities vary from the quantities scheduled in the preview:</html>");
        label3.setFont(new Font("Arial", Font.BOLD, 11));
        label3.setLocation(new Point(20, 280));
        label3.setSize(450, 60);
        label3.setForeground(new Color(50, 50, 25));
        label3.setPreferredSize(new Dimension(100, 20));
        label3.setOpaque(true);
        add(label3);
        
        JLabel label5 = new JLabel("<html>Select the sequence granularity, i.e. how many units of the same variant may occur one after another at maximum. The random variable bounded by this number will be then equally distributed. If you wish a \"real\" random sequence, just set this field to zero:</html>");
        label5.setFont(new Font("Arial", Font.BOLD, 11));
        label5.setLocation(new Point(20, 385));
        label5.setSize(450, 60);
        label5.setForeground(new Color(50, 50, 25));
        label5.setPreferredSize(new Dimension(100, 20));
        label5.setOpaque(true);
        add(label5);
        
        JLabel label6 = new JLabel("<html>You might also want to specify a constant calloff size (N). Leave this field to zero, and the respective calloffsize will result from the sum of the variant quantities:</html>");
        label6.setFont(new Font("Arial", Font.BOLD, 11));
        label6.setLocation(new Point(20, 477));
        label6.setSize(450, 50);
        label6.setForeground(new Color(50, 50, 25));
        label6.setPreferredSize(new Dimension(100, 20));
        label6.setOpaque(true);
        add(label6);
        
        JLabel granLabel = new JLabel("G fix:");
        granLabel.setFont(new Font("Arial", Font.BOLD, 13));
        granLabel.setLocation(new Point(20, 447));
        granLabel.setSize(35, 30);
        granLabel.setForeground(new Color(50, 50, 25));
        granLabel.setPreferredSize(new Dimension(100, 20));
        granLabel.setOpaque(true);
        granLabel.setVisible(true);
        add(granLabel);
        
        final JTextField granField = new JTextField("0");
    	granField.setBounds(60, 450, 30, 25);
    	add(granField);
    	
    	JLabel vLabel = new JLabel("N max:");
        vLabel.setFont(new Font("Arial", Font.BOLD, 13));
        vLabel.setLocation(new Point(20, 527));
        vLabel.setSize(55, 30);
        vLabel.setForeground(new Color(50, 50, 25));
        vLabel.setPreferredSize(new Dimension(100, 20));
        vLabel.setOpaque(true);
        vLabel.setVisible(true);
        add(vLabel);
    	
    	final JTextField vField = new JTextField("0");
    	vField.setBounds(80, 532, 30, 25);
    	add(vField);
        
        int i = 0;
        for (; i < numProds; i++){
        	
        	JLabel pLabel = new JLabel("V" + i + ":");
            pLabel.setFont(new Font("Arial", Font.BOLD, 13));
            pLabel.setLocation(new Point(20 + i*65, 42));
            pLabel.setSize(20, 30);
            pLabel.setForeground(new Color(50, 50, 25));
            pLabel.setPreferredSize(new Dimension(100, 20));
            pLabel.setOpaque(true);
            pLabel.setVisible(true);
            add(pLabel);
            
        	qtyFields[i] = new JTextField("0");
        	qtyFields[i].setBounds(45 + i*65, 45, 30, 25);
        	add(qtyFields[i]);
        	
        	JLabel gLabel = new JLabel("V" + i + ":");
            gLabel.setFont(new Font("Arial", Font.BOLD, 13));
            gLabel.setLocation(new Point(20 + i*65, 137));
            gLabel.setSize(20, 30);
            gLabel.setForeground(new Color(50, 50, 25));
            gLabel.setPreferredSize(new Dimension(100, 20));
            gLabel.setOpaque(true);
            gLabel.setVisible(true);
            add(gLabel);
            
        	growthFields[i] = new JTextField("0");
        	growthFields[i].setBounds(45 + i*65, 140, 30, 25);
        	add(growthFields[i]);
        	
        	JLabel fLabel = new JLabel("V" + i + ":");
            fLabel.setFont(new Font("Arial", Font.BOLD, 13));
            fLabel.setLocation(new Point(20 + i*65, 242));
            fLabel.setSize(20, 30);
            fLabel.setForeground(new Color(50, 50, 25));
            fLabel.setPreferredSize(new Dimension(100, 20));
            fLabel.setOpaque(true);
            fLabel.setVisible(true);
            add(fLabel);
            
        	flucFields[i] = new JTextField("0");
        	flucFields[i].setBounds(45 + i*65, 245, 30, 25);
        	add(flucFields[i]);
        	
        	JLabel dLabel = new JLabel("V" + i + ":");
            dLabel.setFont(new Font("Arial", Font.BOLD, 13));
            dLabel.setLocation(new Point(20 + i*65, 342));
            dLabel.setSize(20, 30);
            dLabel.setForeground(new Color(50, 50, 25));
            dLabel.setPreferredSize(new Dimension(100, 20));
            dLabel.setOpaque(true);
            dLabel.setVisible(true);
            add(dLabel);
            
        	devFields[i] = new JTextField("0");
        	devFields[i].setBounds(45 + i*65, 345, 30, 25);
        	add(devFields[i]);
        }
        
        
        
        button = new JButton("OK");
        button.setBounds(40 + i*65, 532, 60, 25);
        button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int j = 0; j < numProds; j++){
					base_calloff_quantities[j] = Integer.parseInt(qtyFields[j].getText());
					growth[j] = Integer.parseInt(growthFields[j].getText());
					fluctuation[j] = Integer.parseInt(flucFields[j].getText());
					deviation[j] = Integer.parseInt(devFields[j].getText());
				}
				new TableGenerator().generateDataTables(Integer.parseInt(vField.getText()),
						numProds, base_calloff_quantities, growth, fluctuation, deviation, 
						Integer.parseInt(granField.getText()));
				System.exit(0);
			}
		});
        add(button);
	}
	
	
}
