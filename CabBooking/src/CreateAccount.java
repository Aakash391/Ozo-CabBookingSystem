import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateAccount extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField6;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JRadioButton othersRadioButton;
    private JButton createAccountButton;
    private JPanel jpanel;
    private JPasswordField passwordField1;
    private JButton signInButton;

    public CreateAccount(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        ButtonGroup group=new ButtonGroup();
        group.add(maleRadioButton);
        group.add(femaleRadioButton);
        group.add(othersRadioButton);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Acoount clicked");
                String f_name=textField1.getText();
                Long cont= Long.valueOf(0);
                int Pass_age=0;
                String pass=null;
                String Pass_gender=null;
                String mail=null;
                int previd=0;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();


                    String l_name=textField2.getText();
                    cont=Long.parseLong(textField3.getText());
                    mail=textField4.getText();
                    Pass_age=Integer.parseInt(textField6.getText());
                    pass= String.valueOf(passwordField1.getPassword());
                    boolean male=maleRadioButton.isSelected();
                    boolean female=femaleRadioButton.isSelected();
                    boolean others=othersRadioButton.isSelected();
                    Pass_gender=null;
                    if(male){
                        Pass_gender="Male";
                    }
                    else if(female){
                        Pass_gender="Female";
                    }
                    else if(others){
                        Pass_gender="Others";
                    }
                    System.out.println(Pass_gender);
                    ResultSet rs1=stmt.executeQuery("Select max(pass_id) from passenger");
                    System.out.println("no pro");

                    while (rs1.next()) {
                        previd=rs1.getInt(1);
                    }
                    previd++;
                    System.out.println(previd);
                    String sql="insert into passenger(pass_id,fname,lname,contact,mail_id,password,age,gender) " +
                            "values(?,?,?,?,?,?,?,?)";
                    PreparedStatement pstmt=con.prepareStatement(sql);
                    pstmt.setInt    (1, previd);
                    pstmt.setString (2, f_name);
                    pstmt.setString (3, l_name);
                    pstmt.setLong   (4, cont);
                    pstmt.setString(5, mail);
                    pstmt.setString    (6, pass);
                    pstmt.setInt(7,Pass_age);
                    pstmt.setString(8,Pass_gender);
//                  Add login date

                    pstmt.execute();


                    con.close();
                } catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                JFrame mainmenu=new MainMenu("Main Menu",f_name,cont,mail,previd);
                mainmenu.setVisible(true);
                mainmenu.setResizable(false);
                dispose();
            }
        });
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame signin=new LoginFinal("Login page");
                signin.setVisible(true);
                signin.setResizable(false);
                dispose();
            }
        });
    }
}
