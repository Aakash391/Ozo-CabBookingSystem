import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class Details extends JFrame{
    private JTable table1;
    private JPanel jpanel;
    private JButton homePageButton;
    private JButton viewYearWisePaymentButton;
    private JButton viewDateWisePaymentButton;
    private JTable table2;
    private JTable table3;
    int dclicked=0;
    int yclicked=0;

    public Details(String title,String name,long contact,String mail_id,int admin_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();

            if(Objects.equals(title, "Passenger Details")) {
                ResultSet st = stmt.executeQuery("select fname,lname,contact,mail_id,gender from passenger;");
                String fname = null;
                String lname = null;
                long cont = 0;
                String mailid = null;
                String gender = null;
                int k = 0;
                ResultSetMetaData rsmd = st.getMetaData();
                DefaultTableModel model = (DefaultTableModel) table1.getModel();

                int col = rsmd.getColumnCount();

                String[] colname = new String[col];
                for (int i = 0; i < col; i++) {
                    colname[i] = rsmd.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(colname);
                while (st.next()) {
                    fname = st.getString(1);
                    lname = st.getString(2);
                    cont = st.getLong(3);
                    mailid = st.getString(4);
                    gender = st.getString(5);

                    Object[] row = {fname, lname, cont, mailid, gender};
                    model.addRow(row);
                }
            }

            else if(Objects.equals(title, "Removed Driver Details")){
                ResultSet st = stmt.executeQuery("select fname,lname,contact,house_no,street_no,streetname,city,state,pincode from removed_driver;");
                String fname = null;
                String lname = null;
                long cont=0;
                int houseno = 0;
                int streetno=0;
                String streetname = null;
                String city = null;
                String state=null;
                int pincode = 0;
                ResultSetMetaData rsmd = st.getMetaData();
                DefaultTableModel model = (DefaultTableModel) table1.getModel();

                int col = rsmd.getColumnCount();

                String[] colname = new String[col];
                for (int i = 0; i < col; i++) {
                    colname[i] = rsmd.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(colname);
                while (st.next()) {
                    fname = st.getString(1);
                    lname = st.getString(2);
                    cont = st.getLong(3);
                    houseno = st.getInt(4);
                    streetno = st.getInt(5);
                    streetname=st.getString(6);
                    city=st.getString(7);
                    state=st.getString(8);
                    pincode=st.getInt(9);

                    Object[] row = {fname, lname, cont, houseno, streetno,streetname,city,state,pincode};
                    model.addRow(row);
                }

            }
            else if(Objects.equals(title, "Cancelled Ride Details")){
                ResultSet st = stmt.executeQuery("select pickup,dropoff,passenger.fname,passenger.lname,driver.fname,driver.lname from cancelled_ride " +
                        "join passenger on cancelled_ride.pass_id=passenger.pass_id " +
                        "join driver on cancelled_ride.driver_id=driver.driver_id; ");

                String pfname = null;
                String plname = null;
                String dfname=null;
                String dlname=null;
                String ploc=null;
                String dloc=null;

                ResultSetMetaData rsmd = st.getMetaData();
                DefaultTableModel model = (DefaultTableModel) table1.getModel();

                int col = rsmd.getColumnCount();

                String[] colname = new String[col];
                for (int i = 0; i < col; i++) {
                    colname[i] = rsmd.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(colname);
                while (st.next()) {
                    pfname = st.getString(1);
                    plname = st.getString(2);
                    dfname = st.getString(3);
                    dlname = st.getString(4);
                    ploc = st.getString(5);
                    dloc=st.getString(6);


                    Object[] row = {pfname, plname, dfname, dlname, ploc,dloc};
                    model.addRow(row);
                }
            }
            con.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        homePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame homepage=new AdminMainMenu("Driver Main Menu",name,contact,mail_id,admin_id);
                homepage.setVisible(true);
                homepage.setResizable(false);
                dispose();
            }
        });


    }
}
