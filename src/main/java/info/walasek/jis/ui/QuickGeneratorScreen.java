package info.walasek.jis.ui;

import info.walasek.jis.logic.DemandConfiguration;
import info.walasek.jis.logic.ProductConfiguration;
import info.walasek.jis.logic.TableGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class QuickGeneratorScreen extends JFrame {

    public final static int MAX_VARIANTS = 30;

    public QuickGeneratorScreen() {
        initUI();
    }

    public void initUI() {

        setTitle("Select number of goods per calloff");

        setLayout(null);

        setSize(300, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        JLabel label = new JLabel("<html>Select the initial product quantities to be scheduled in the calloff preview <u>per calloff</u>:</html>");
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
        button.addActionListener(e -> {

            int calloffSize = Integer.parseInt(textField.getText());
            for (int v = 1; v <= MAX_VARIANTS; v++) {

                int[] base_calloff_quantities = new int[v];
                for (int i = 0; i < v; i++)
                    base_calloff_quantities[i] = 0;
                int j = 0;
                for (int i = 1; i <= calloffSize; i++) {
                    if (j >= base_calloff_quantities.length)
                        j = 0;
                    base_calloff_quantities[j]++;
                    j++;
                }

                List<ProductConfiguration> products = IntStream.range(0, v).boxed()
                        .map(productType -> new ProductConfiguration(productType, base_calloff_quantities[productType], 0, 0, 0
                        )).collect(Collectors.toList());

                new TableGenerator().generateDataTables(new DemandConfiguration(products, 5, 365));
            }
            System.exit(0);
        });
        add(button);
    }
}
