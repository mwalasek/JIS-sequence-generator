package info.walasek.jis.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomGeneratorScreen1 extends JFrame {

    public int numProds = 3;

    public CustomGeneratorScreen1() {

        initUI();

    }

    public void initUI() {
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

        for (int i = 1; i <= 30; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setBounds(20 + ((i - 1) % 5) * 55, 50 + 40 * Math.round((i - 1) / 5), 50, 25);
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
