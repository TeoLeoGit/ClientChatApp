package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class LoginGUI extends JFrame {
    private JPanel mainPanel;
    private JButton loginBtn;
    public LoginGUI() {
        mainPanel = new JPanel();
        loginBtn = new JButton("Login");
        mainPanel .setLayout(null);

        //panel ministry setting
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(10, 20, 80, 25);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        JTextField usernameText = new JTextField();
        usernameText.setBounds(80, 20, 165, 25);
        JTextField passwordText = new JTextField();
        passwordText.setBounds(80, 50, 165, 25);
        loginBtn.setBounds(260, 30, 80, 30);
        mainPanel.add(usernameLabel);
        mainPanel.add(passwordLabel);
        mainPanel.add(usernameText);
        mainPanel.add(passwordText);
        mainPanel.add(loginBtn);
        loginBtn.setBackground(Color.WHITE);
        add(mainPanel);

        //panel student setting
        this.setSize(380, 150);
        this.setPreferredSize(new Dimension(380, 150));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //event handle
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatappuser", "root", "teo15102000");
                    String query = "select username from accounts where username='" + usernameText.getText() +
                            "' and password= '" + passwordText.getText() + "';";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next()) {
                        JFrame clientGUI = new ClientMainGUI(usernameText.getText());
                        clientGUI.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginBtn, "No account found");
                    }
                    stmt.close();

                } catch (ClassNotFoundException | SQLException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }

            }
        });
    }
}
