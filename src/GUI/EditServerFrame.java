package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditServerFrame extends JFrame {
    public EditServerFrame(DefaultTableModel model, int modelIndex) {
        JPanel mainPanel = new JPanel();
        JButton editBtn = new JButton("Edit");
        mainPanel.setLayout(new FlowLayout());

        //panel ministry setting
        JLabel serverPortLabel = new JLabel("Port");
        serverPortLabel.setPreferredSize(new Dimension(80, 25));
        JTextField serverPortText = new JTextField();
        serverPortText.setPreferredSize(new Dimension(165, 25));

        editBtn.setPreferredSize(new Dimension(80, 30));
        mainPanel.add(serverPortLabel);
        mainPanel.add(serverPortText);
        mainPanel.add(editBtn);
        editBtn.setBackground(Color.WHITE);
        add(mainPanel);

        //panel student setting
        this.setSize(380, 150);
        this.setPreferredSize(new Dimension(380, 60));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //event handle
        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int portNumber = Integer.valueOf(serverPortText.getText());
                    model.setValueAt(serverPortText.getText(), modelIndex, 0);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editBtn, "Illegal port format");
                }
            }
        });
    }
}
