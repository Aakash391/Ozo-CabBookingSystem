import com.mysql.cj.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class LoginFinal extends JFrame {
//    private JPanel jpanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    JButton createAccountButton;
    private JRadioButton passengerRadioButton;
    private JRadioButton driverRadioButton;
    private JRadioButton adminRadioButton;
    private JPanel jpanel;

    public LoginFinal(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        ButtonGroup group=new ButtonGroup();
        group.add(passengerRadioButton);
        group.add(driverRadioButton);
        group.add(adminRadioButton);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Clicked");
                JFrame createAccount=new CreateAccount("Create Account");
                createAccount.setVisible(true);
                createAccount.setResizable(false);
                dispose();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login Clicked");
                long cont=Long.parseLong(textField1.getText());
                String pass= String.valueOf(passwordField1.getPassword());
                boolean passenger=passengerRadioButton.isSelected();
                boolean driver=driverRadioButton.isSelected();
                boolean admin=adminRadioButton.isSelected();
                int count=0;
                String name=null;
                String mail=null;
                int pass_id=0;
                int driver_id=0;
                int admin_id=0;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();
                    if(passenger) {
                        ResultSet rs = stmt.executeQuery("select * from passenger ");

                        while (rs.next()) {

                            if (rs.getLong(4) == cont && Objects.equals(rs.getString(6), pass)) {
                                count++;
                                name = rs.getString(2);
                                mail = rs.getString(5);
                                pass_id = rs.getInt(1);
                            }
//                        System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
                        }
                    }
                    else if(driver){
                        ResultSet rs = stmt.executeQuery("select * from driver ");

                        while (rs.next()) {

                            if (rs.getLong(4) == cont && Objects.equals(rs.getString(6), pass)) {
                                count++;
                                name = rs.getString(2);
                                mail = rs.getString(5);
                                driver_id = rs.getInt(1);
                            }
//                        System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
                        }
                    }
                    else{
                        ResultSet rs = stmt.executeQuery("select * from admin ");

                        while (rs.next()) {

                            if (rs.getLong(12) == cont) {
                                count++;
                                name = rs.getString(2);
                                mail = rs.getString(5);
                                admin_id = rs.getInt(1);
                            }
//                        System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
                        }
                    }


                    con.close();
                } catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                if(count==0){
                    System.out.println("No info");
                }
                else{
                    if(passenger) {

                        JFrame mainmenu = new MainMenu("Main Menu", name, cont, mail, pass_id);
                        dispose();
                        mainmenu.setVisible(true);
                        mainmenu.setResizable(false);
                    }
                    else if(driver){
                        JFrame mainmenu = new DriverMainMenu("Main Menu", name, cont, mail, driver_id);
                        dispose();
                        mainmenu.setVisible(true);
                        mainmenu.setResizable(false);
                    }
                    else{
                        JFrame Adminmainmenu = new AdminMainMenu("Main Menu", name, cont, mail, admin_id);
                        Adminmainmenu.setVisible(true);
                        Adminmainmenu.setResizable(false);
                        dispose();
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        JFrame LoginFinal=new LoginFinal("Login Page");
        LoginFinal.setVisible(true);
        LoginFinal.setResizable(false);
//        LoginFinal.getContentPane().setBackground(new Color(0x123456));


    }

}
