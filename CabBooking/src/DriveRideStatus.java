import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.StringTokenizer;

public class DriveRideStatus extends JFrame {

    private JButton cancelRideButton;
    private JButton endRideButton;
    private JPanel jpanel;
    private JLabel label1;
    private JLabel label2;
    private JTextField textField1;
    private JButton enterOTPButton;
    int ot=0;
    boolean otpentered=false;

    public DriveRideStatus(String title,float amount,String name,int ride_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        label1.setText(""+amount);
        label2.setText(name);



        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();

            ResultSet st=stmt.executeQuery("select otp from ride where ride_id="+ride_id+";");

            while (st.next()){
                ot=st.getInt(1);
            }
            con.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

//        cancelRideButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(otpentered==false){
//                    try{
//                        Class.forName("com.mysql.cj.jdbc.Driver");
//                        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
//                        Statement stmt=con.createStatement();
//
//                        ResultSet st=stmt.executeQuery("Update ride set ride_status='Cancelled',cancelledby='Driver' where ride_id="+ride_id+";");
//
//                        ResultSet st1=stmt.executeQuery("delete from ride  where ride_id="+ride_id+";");
//                        con.close();
//                    }
//                    catch (SQLException exception) {
//                        System.out.println(exception);
//                    } catch (ClassNotFoundException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            }
//        });
        enterOTPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int otp=0;
                String otpstring=textField1.getText();
                System.out.println(otpstring);
                if(!otpstring.isEmpty()) {
                    otp = Integer.parseInt(otpstring);
                }
                System.out.println(ot);
                System.out.println(otp);
                if(ot==otp){
                    otpentered=true;
//                    JFrame driverridestatus=new DriveRideStatus("Drive Status",amount,name,ride_id);
//                    driverridestatus.setVisible(true);
//                    driverridestatus.setResizable(false);
//                    dispose();
                }
            }
        });
        cancelRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(otpentered==false) {
                    System.out.println(otpentered);
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
                        Statement stmt = con.createStatement();
                        String str = "update ride set ride_status='Cancelled',cancelledby='Driver' where ride_id=?;";
                        PreparedStatement pstmt = con.prepareStatement(str);
                        pstmt.setInt(1, ride_id);
                        pstmt.execute();

                        String str2 = "delete from ride where ride_id=?;";
                        PreparedStatement pstmt1 = con.prepareStatement(str2);
                        pstmt1.setInt(1, ride_id);
                        pstmt1.execute();

                        con.close();
                    } catch (SQLException exception) {
                        System.out.println(exception);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        endRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();
                    String status=null;
                    ResultSet st=stmt.executeQuery("select payment_status from ride where ride_id="+ride_id+";");
                    while (st.next()){
                        status=st.getString(1);
                    }
                    if(Objects.equals(status, "Completed")) {
                        String str = "update ride set ride_status='Completed' where ride_id=?;";
                        PreparedStatement stmt2 = con.prepareStatement(str);
                        stmt2.setInt(1, ride_id);
                        stmt2.execute();

                        String str1 = "update ride set enddate=current_timestamp where ride_id=?";
                        PreparedStatement pstmt2 = con.prepareStatement(str1);
                        pstmt2.setInt(1, ride_id);
                        pstmt2.execute();
                    }


                    con.close();
                }
                catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
}
