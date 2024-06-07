import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RideDetails extends JFrame{
    private JTable table1;
    private JButton homePageButton;
    private JPanel jpanel;
    private JButton viewNunberOfRideButton;

    public RideDetails(String title,String name,long cont,String mailid, int admin_id){

        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
            Statement stmt = con.createStatement();

            ResultSet st = stmt.executeQuery("select ride.pickuplocation, ride.dropofflocation, passenger.fname, driver.fname,payment.payment_amount from ride " +
                    "join passenger on ride.pass_id=passenger.pass_id " +
                    "join driver on ride.driver_id=driver.driver_id " +
                    "join payment on ride.payment_id=payment.payment_id;");
            System.out.println("After ");
            String ploc = null;
            String dloc = null;

            String pname = null;

            String dname = null;

            float payment = 0;

            ResultSetMetaData rsmd = st.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            int col = rsmd.getColumnCount();

            String[] colname = new String[col];
            for (int i = 0; i < col; i++) {
                colname[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colname);
            while (st.next()) {
                ploc = st.getString(1);
                dloc = st.getString(2);

                pname = st.getString(3);

                dname = st.getString(4);

                payment = st.getFloat(5);

                System.out.println(ploc);
                System.out.println(dloc);
                System.out.println(pname);
                System.out.println(dname);
                System.out.println(payment);

                Object[] row = {ploc, dloc, pname, dname, payment};
                model.addRow(row);
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
                JFrame adminmain=new AdminMainMenu("Admin Main Menu",name,cont,mailid,admin_id);
                adminmain.setVisible(true);
                adminmain.setResizable(false);
                dispose();
            }
        });
        viewNunberOfRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame cancelled=new NumberOfRidecancelled("Ride Cancelled",name,cont,mailid,admin_id);
                cancelled.setVisible(true);
                cancelled.setResizable(false);
                dispose();
            }
        });
    }
}
