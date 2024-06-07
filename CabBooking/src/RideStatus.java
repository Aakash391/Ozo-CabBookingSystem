import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

public class RideStatus extends JFrame {
    private JButton cancelRideButton;
    private JPanel jpanel;
    private JLabel text;
    private JLabel text1;
    private JLabel text2;
    private JButton makePaymentButton;
    private JLabel text3;


    public RideStatus(String title,String fname,float amount,int ride_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();
        text1.setText(String.valueOf(amount));
        text2.setText(fname);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();

            ResultSet st=stmt.executeQuery("select otp from ride where ride_id="+ride_id+";");
            String otp=null;
            while (st.next()){
                otp=st.getString(1);
            }

            text3.setText(otp);

            con.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        cancelRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();

                    String str="update ride set ride_status='Cancelled',cancelledby='Passenger' where ride_id=?;";
                    PreparedStatement pstmt=con.prepareStatement(str);
                    pstmt.setInt(1,ride_id);
                    pstmt.execute();

                    String str1="delete from ride where ride_id=?;";
                    PreparedStatement pstmt1=con.prepareStatement(str1);
                    pstmt1.setInt(1,ride_id);
                    pstmt1.execute();

                    con.close();
                }
                catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        makePaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();
                    String[] mode={"card","cash","UPI"};
                    Random rand= new Random();
                    int val=rand.nextInt(0,3);
                    int payment_id=0;
                    ResultSet st=stmt.executeQuery("Select payment_id from ride where ride_id="+ride_id+";");
                    while (st.next()){
                        payment_id=st.getInt(1);
                    }
                    int driver_id=0;
                    ResultSet st1=stmt.executeQuery("Select driver_id from ride where ride_id="+ride_id+";");
                    while (st1.next()){
                        driver_id=st1.getInt(1);
                    }
                    String str="update payment set payment_amount=?,paymentmode=? where payment_id=?";
                    PreparedStatement pstmt=con.prepareStatement(str);
                    pstmt.setString(2,mode[val]);
                    pstmt.setFloat(1,amount);
                    pstmt.setInt(3,payment_id);
                    pstmt.execute();

                    String str2="update driver set salary=((select salary from (select salary from driver where driver_id=?) as temp)+?) where driver_id=?;";
                    PreparedStatement stmt2=con.prepareStatement(str2);
                    stmt2.setInt(1,driver_id);
                    stmt2.setFloat(2,amount/2);
                    stmt2.setInt(3,driver_id);
                    stmt2.execute();

                    String str1="Update ride set payment_status='Completed' where ride_id="+ride_id;
                    PreparedStatement pstmt1=con.prepareStatement(str1);
                    pstmt1.execute();
                    System.out.println("Payment done Successfully");
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
