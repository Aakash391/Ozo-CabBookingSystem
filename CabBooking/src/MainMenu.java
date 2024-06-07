import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.Random;

public class MainMenu extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JRadioButton SUVRadioButton;
    private JRadioButton sedanRadioButton;
    private JRadioButton autoRadioButton;
    private JRadioButton minivanRadioButton;
    private JButton bookCabButton;
    private JTextField textField3;
    private JButton viewProfileButton;
    private JPanel jpanel;
    private JLabel Amount;
    private JButton calculateFareButton;
    private JButton goToRideButton;

    boolean farebutton=false;
    String cartype=null;
    float fare=0f;
    int seat=0;
    String ploc=null;
    String dloc=null;
    int previd=0;

    public MainMenu(String title,String name,Long cont, String mail,int pass_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        ButtonGroup group=new ButtonGroup();
        group.add(SUVRadioButton);
        group.add(sedanRadioButton);
        group.add(autoRadioButton);
        group.add(minivanRadioButton);
        calculateFareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//

                Random random=new Random();

                if(!Objects.equals(ploc, null)==false && !Objects.equals(dloc, null)==false && farebutton!=true) {
//                    System.out.println("In if");
                    ploc=textField1.getText();
                    dloc=textField2.getText();

                    System.out.println("1"+ploc);
                    System.out.println("2"+dloc);
                    boolean suv = SUVRadioButton.isSelected();
                    boolean sedan = sedanRadioButton.isSelected();
                    boolean auto = autoRadioButton.isSelected();
                    boolean minvan = minivanRadioButton.isSelected();
                    System.out.println(suv);

                    if (suv) {
                        cartype="SUV";
                        fare=random.nextFloat(200,800)+30;
                        String amountFare=""+fare;
                        Amount.setText(amountFare);
                        farebutton=true;
                        seat=7;
                    } else if (sedan) {
                        cartype="Sedan";
                        fare=random.nextFloat(200,800)+20;
                        String amountFare=""+fare;
                        Amount.setText(amountFare);
                        farebutton=true;
                        seat=5;
                    } else if (auto) {
                        cartype="Auto";
                        fare=random.nextFloat(200,800)+10;
                        String amountFare=""+fare;
                        Amount.setText(amountFare);
                        farebutton=true;
                        seat=3;
                    } else if (minvan) {
                        cartype="Minivan";
                        fare=random.nextFloat(200,800)+15;
                        String amountFare=""+fare;
                        Amount.setText(amountFare);
                        farebutton=true;
                        seat=9;
                    }
                }
            }
        });

        bookCabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(farebutton==true){
                    System.out.println("Ride status");
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                        Statement stmt=con.createStatement();
                        Random random=new Random();
                        int time=random.nextInt(20,100);

                        ResultSet rs1=stmt.executeQuery("Select max(request_id) from request");
                        System.out.println("no pro");

                        while (rs1.next()) {
                            previd=rs1.getInt(1);
                        }
                        previd++;
                        System.out.println(previd);
                        String sql="insert into request values (?,?,?,?,?,?,?,?);";
                        PreparedStatement pstmt=con.prepareStatement(sql);
                        pstmt.setInt    (1, previd);
                        pstmt.setString (2, ploc);
                        pstmt.setString (3, dloc);
                        pstmt.setString   (4, cartype);
                        pstmt.setString(5, ""+fare);
                        pstmt.setInt    (6, time);
                        pstmt.setInt(7,pass_id);
                        pstmt.setInt(8,seat);

                        System.out.println(ploc);
                        System.out.println(dloc);
                        pstmt.execute();
                        con.close();
                    } catch (SQLException exception) {
                        System.out.println(exception);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("profile clicked");
                JFrame profile=new PassengerProfile("Profile",name,cont,mail,pass_id);
                profile.setVisible(true);
                profile.setResizable(false);
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

//                    ResultSet rs1=stmt.executeQuery("Select pass_id from ride");
//                    while (rs1.next()){
//                        previd=rs1.getInt(1);
//                    }

                    ResultSet st=stmt.executeQuery("select ride_id,pass_id,driver_id,payment_id from ride where ride_status='Booked';");
                    int ride_id=0;
                    int driver_id=0;
                    int payment_id=0;
                    int pass_idtemp=0;
                    while (st.next()){
                        pass_idtemp=st.getInt(2);
                        if(pass_idtemp==pass_id) {
                            ride_id = st.getInt(1);
                            driver_id = st.getInt(3);
                            payment_id = st.getInt(4);
                        }
                    }
                    ResultSet st1=stmt.executeQuery("select fname from driver where driver_id="+driver_id+";");
                    String fname=null;
                    while (st1.next()){
                        fname=st1.getString(1);
                    }
                    st1=stmt.executeQuery("select payment_amount from payment where payment_id="+payment_id+";");
                    float payment=0;
                    while (st1.next()){
                        payment=st1.getFloat(1);
                    }
                    System.out.println(ride_id);
                    if(ride_id!=0){
                        JFrame ridestatus=new RideStatus("Ride status",fname,payment,ride_id);
                        ridestatus.setVisible(true);
                        ridestatus.setResizable(false);
                        dispose();
                    }
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
