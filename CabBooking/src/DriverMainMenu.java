import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.Random;

public class DriverMainMenu extends JFrame{
    private JRadioButton activeRadioButton;
    private JRadioButton deactiveRadioButton;
    private JButton driveButton;
    private JPanel jpanel;
    private JLabel pickup;
    private JLabel dropoff;
    private JButton changeStatusButton;
    JLabel name;
    private JButton goToRideButton;
    float amount=0;
    String fname=null;
    String ploc=null;
    String dloc=null;
    String amounttemp=null;
    int passid=0;

    public DriverMainMenu(String title,String name,Long cont,String mail,int driver_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        ButtonGroup group=new ButtonGroup();
        group.add(activeRadioButton);
        group.add(deactiveRadioButton);
        String status=null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();

            ResultSet st1=stmt.executeQuery("select ride_status,ride_id,payment_id,pass_id from ride where driver_id="+driver_id+";");
            int count=0;
            int ride_id=0;
            int payment_id=0;
            int pass_id=0;
            String status1=null;
            while (st1.next()){
                status1=st1.getString(1);
                System.out.println(status1);
                if(Objects.equals(status1, "Booked")) {
                    ride_id=st1.getInt(2);
                    payment_id=st1.getInt(3);
                    passid=st1.getInt(4);
                    count++;
                }
            }
            System.out.println(count);
            ResultSet st2=stmt.executeQuery("select payment_amount from payment where payment_id="+payment_id+";");

            while (st2.next()){
                amount=st2.getFloat(1);
            }

            ResultSet st4=stmt.executeQuery("select fname from passenger where pass_id="+passid+";");
            while(st4.next()){
                fname=st4.getString(1);
            }
            if(count==0) {

                ResultSet st = stmt.executeQuery("select driver_id from ride where ride_status='Booked';");
                int driveridtemp = 0;
                boolean flag = false;
                while (st.next()) {
                    if (driveridtemp == driver_id) {
                        flag = true;
                    }
                }

                ResultSet rs = stmt.executeQuery("select driver_status from driver where driver_id=" + driver_id + ";");

                while (rs.next()) {
                    status = rs.getString(1);
                }
                System.out.println(status);
                if (Objects.equals(status, "active")) {
                    rs = stmt.executeQuery("select pickuplocation, dropofflocation, rate,pass_id from request where request_id=(select min(request_id) from request);");


                    while (rs.next()) {
                        ploc = rs.getString(1);
                        dloc = rs.getString(2);
                        amounttemp = rs.getString(3);
                        passid = rs.getInt(4);
                    }
//                String rem=amounttemp.substring(1);
                    amount = Float.parseFloat(amounttemp);

                    rs = stmt.executeQuery("select fname from passenger where pass_id=" + passid + ";");

                    while (rs.next()) {
                        fname = rs.getString(1);
                    }

                    pickup.setText(ploc);
                    dropoff.setText(dloc);

                }
            }


            con.close();
        } catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean active=activeRadioButton.isSelected();
                boolean deactive=deactiveRadioButton.isSelected();

                if(active){
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                        Statement stmt=con.createStatement();
                        String sql="update driver set driver_status=? where driver_id=?;";

                        PreparedStatement pstmt=con.prepareStatement(sql);
                        pstmt.setString(1,"active");
                        pstmt.setInt(2,driver_id);
                        pstmt.execute();

                        JFrame newDrivermenu=new DriverMainMenu("Driver Main Menu",name,cont,mail,driver_id);
                        newDrivermenu.setVisible(true);
                        newDrivermenu.setResizable(false);
                        dispose();

                        con.close();
                    } catch (SQLException exception) {
                        System.out.println(exception);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if(deactive){
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                        Statement stmt=con.createStatement();
                        String sql="update driver set driver_status=? where driver_id=?;";

                        PreparedStatement pstmt=con.prepareStatement(sql);
                        pstmt.setString(1,"deactive");
                        pstmt.setInt(2,driver_id);
                        pstmt.execute();

                        JFrame newDrivermenu=new DriverMainMenu("Driver Main Menu",name,cont,mail,driver_id);
                        newDrivermenu.setVisible(true);
                        newDrivermenu.setResizable(false);
                        dispose();

                        con.close();
                    } catch (SQLException exception) {
                        System.out.println(exception);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        driveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int payment_id=0;
                int ride_id=0;
                int request_id=0;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();

                    ResultSet rs=stmt.executeQuery("select max(payment_id) from payment;");

                    while(rs.next()){
                        payment_id=rs.getInt(1);
                    }
                    payment_id++;
                    System.out.println(payment_id);


                    String str2="insert into payment(payment_id,payment_amount) values(?,?);";
                    PreparedStatement stmt2=con.prepareStatement(str2);

                    stmt2.setInt(1,payment_id);
                    stmt2.setFloat(2,amount);
                    stmt2.execute();

                    ResultSet rs1=stmt.executeQuery("select max(ride_id) from ride;");
                    while(rs1.next()){
                        ride_id=rs1.getInt(1);
                    }
                    ride_id++;
                    String str="insert into ride(ride_id,pickuplocation,dropofflocation,distance,pass_id,driver_id,payment_id,otp,ride_status,payment_status) " +
                            "values(?,?,?,?,?,?,?,?,?,?);";
                    PreparedStatement pstmt=con.prepareStatement(str);
                    Random random=new Random();
                    pstmt.setInt(1,ride_id);
                    pstmt.setString(2,ploc);
                    pstmt.setString(3,dloc);
                    pstmt.setFloat(4,random.nextFloat());
                    pstmt.setInt(5,passid);
                    pstmt.setInt(6,driver_id);

                    System.out.println("Driver_id "+driver_id);

                    pstmt.setInt(7,payment_id);
                    pstmt.setInt(8,random.nextInt(1000,9999));
                    pstmt.setString(9,"Booked");
                    pstmt.setString(10,"Pending");
                    pstmt.execute();

                    str="update ride set startdate=current_timestamp where ride_id=?";
                    pstmt=con.prepareStatement(str);
                    pstmt.setInt(1,ride_id);
                    pstmt.execute();

                    ResultSet rs2=stmt.executeQuery("select min(request_id) from request;");
                    while(rs2.next()){
                        request_id=rs2.getInt(1);
                    }
                    str="delete from request where request_id="+request_id+";";
                    stmt.executeUpdate(str);


                    con.close();
                } catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }


                JFrame driverstatus=new DriveRideStatus("Drive Status",amount,fname,ride_id);
                driverstatus.setVisible(true);
                driverstatus.setResizable(false);
                dispose();
            }
        });
        goToRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();

                    ResultSet st1=stmt.executeQuery("select ride_status,ride_id,payment_id,pass_id from ride where driver_id="+driver_id+";");
                    int count=0;
                    int ride_id=0;
                    int payment_id=0;
                    int pass_id=0;
                    String status1=null;
                    while (st1.next()){
                        status1=st1.getString(1);
                        System.out.println(status1);
                        if(Objects.equals(status1, "Booked")) {
                            ride_id=st1.getInt(2);
                            payment_id=st1.getInt(3);
                            passid=st1.getInt(4);
                            count++;
                        }
                    }
                    System.out.println(count);
                    ResultSet st2=stmt.executeQuery("select payment_amount from payment where payment_id="+payment_id+";");

                    while (st2.next()){
                        amount=st2.getFloat(1);
                    }

                    ResultSet st4=stmt.executeQuery("select fname from passenger where pass_id="+passid+";");
                    while(st4.next()){
                        fname=st4.getString(1);
                    }
                    if(count==1){
                        JFrame driverstatus=new DriveRideStatus("Drive Status",amount,fname,ride_id);
                        driverstatus.setVisible(true);
                        driverstatus.setResizable(false);
                        dispose();
                    }
                    con.close();
                } catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
