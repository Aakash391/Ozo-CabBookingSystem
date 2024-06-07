import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DriverDetail extends JFrame{
    private JTable table1;
    private JButton removeDriverButton;
    private JButton homePageButton;
    private JPanel jpanel;
    private JButton showRideCancelledByButton;

    public DriverDetail(String title,String name,long contact,String mailid,int admin_id) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();

            ResultSet st=stmt.executeQuery("select fname,lname,contact,mail_id,gender,house_no,street_no,streetname,city,state,pincode,rating,salary from driver;");
            String fname=null;
            String lname=null;
            long cont=0;
            String mail=null;
            String gender=null;
            int house_no=0;
            int street_no=0;
            String streetname=null;
            String city=null;
            String state=null;
            int pin=0;
            int rating=0;
            float salary=0;
            String saltemp=null;

            ResultSetMetaData rsmd = st.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            int col = rsmd.getColumnCount();

            String[] colname = new String[col];
            for (int i = 0; i < col; i++) {
                colname[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colname);

            while (st.next()){
                fname=st.getString(1);
                lname=st.getString(2);
                cont=st.getLong(3);
                mail=st.getString(4);
                gender=st.getString(5);
                house_no=st.getInt(6);
                street_no=st.getInt(7);
                streetname=st.getString(8);
                city=st.getString(9);
                state=st.getString(10);
                pin=st.getInt(11);
                rating=st.getInt(12);
                saltemp=st.getString(13);

                String sal=saltemp.substring(1);
                salary=Float.parseFloat(sal);

                Object[] row = {fname, lname, cont, mail, gender,house_no,street_no,streetname,city,state,pin,rating,salary};
                model.addRow(row);
            }



            con.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        removeDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
                    Statement stmt=con.createStatement();

                    String str="delete from driver where working_hrs<20";
                    PreparedStatement stmt1=con.prepareStatement(str);
                    stmt1.execute();


                    con.close();
                }
                catch (SQLException exception) {
                    System.out.println(exception);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        homePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame homepage=new AdminMainMenu("Admin Main Menu",name,contact,mailid,admin_id);
                homepage.setVisible(true);
                homepage.setResizable(false);
                dispose();
            }
        });
        showRideCancelledByButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame rideDriver=new DriverCancelRide("Ride Details",name,contact,mailid,admin_id);
                rideDriver.setVisible(true);
                rideDriver.setResizable(false);
                dispose();

            }
        });
    }
}
