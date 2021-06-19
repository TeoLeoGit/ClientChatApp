package GUI;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginGUI extends JFrame {
    private JPanel mainPanel;
    private JButton loginBtn;
    private JButton registBtn;
    public LoginGUI() {
        mainPanel = new JPanel();
        loginBtn = new JButton("Login");
        registBtn = new JButton("Sign up");
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
        loginBtn.setBounds(260, 20, 80, 25);
        registBtn.setBounds(260, 50, 80, 25);
        mainPanel.add(usernameLabel);
        mainPanel.add(passwordLabel);
        mainPanel.add(usernameText);
        mainPanel.add(passwordText);
        mainPanel.add(loginBtn);
        mainPanel.add(registBtn);
        loginBtn.setBackground(Color.WHITE);
        registBtn.setBackground(Color.WHITE);
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
                //Login : Login>username##password
                String username = usernameText.getText();
                String password = passwordText.getText();
                if (username.equals("") || password.equals(""))
                    JOptionPane.showMessageDialog(loginBtn, "Username or password not given");
                else
                    if (username.contains("@") || username.contains("=>") || username.contains(", ")
                            || username.contains("##") || username.contains(">"))
                        JOptionPane.showMessageDialog(loginBtn, "Username cannot contains '@', '>', '=>', ', ', '##' characters");
                    else {
                        String userSignature = "Login>" + username + "##" + password;
                        System.out.println(userSignature);
                        //by default will connect to server 3500
                        Client loginCl = new Client(userSignature, 3500);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        if (loginCl.getSocket() == null)
                            JOptionPane.showMessageDialog(loginBtn, "Server is not responding");
                        else {
                            if(loginCl.getSocket().isClosed())
                                JOptionPane.showMessageDialog(loginBtn, "Login failed");
                            else {
                                System.out.println("OK1");
                                JFrame clientGUI = null;
                                try {
                                    clientGUI = new ClientMainGUI(loginCl);
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                                clientGUI.setVisible(true);
                                dispose();
                            }
                        }
                    }
            }
        });

        registBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Login : Register>username##password
                String username = usernameText.getText();
                String password = passwordText.getText();
                if (username.equals("") || password.equals(""))
                    JOptionPane.showMessageDialog(loginBtn, "Username or password not given");
                else
                if (username.contains("@") || username.contains("=>") || username.contains(", ")
                        || username.contains("##") || username.contains(">"))
                    JOptionPane.showMessageDialog(loginBtn, "Username cannot contains '@', '>', '=>', ', ', '##' characters");
                else {
                    String userSignature = "Register>" + usernameText.getText() + "##" + passwordText.getText();
                    System.out.println(userSignature);
                    //by default will connect to server 3500
                    Client loginCl = new Client(userSignature, 3500);
                    if (loginCl.getSocket() == null)
                        JOptionPane.showMessageDialog(loginBtn, "Server is not responding");
                    else {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        if(loginCl.getSocket().isClosed())
                            JOptionPane.showMessageDialog(loginBtn, "You can try log in now");
                    }
                }
            }
        });
    }
}
