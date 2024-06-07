import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NumberOfRidecancelled extends JFrame{
    private JTable table1;
    private JButton homePageButton;
    private JPanel jpanel;
    private JTable table2;
    private JButton noOfRideDriverButton;
    private JButton noOfRideDriverButton1;
    private JTable table3;
    int comleted=0;
    int cancelled=0;

    public NumberOfRidecancelled(String title,String name,long cont,String mailid,int admin_id) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
            Statement stmt = con.createStatement();

            ResultSet st = stmt.executeQuery("select cancelled_ride.cancelledby,count(*) from cancelled_ride group by cancelledby with rollup;");
            ResultSetMetaData rsmd = st.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            int col = rsmd.getColumnCount();

            String[] colname = new String[col];
            for (int i = 0; i < col; i++) {
                colname[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colname);

            String cancelledby = null;
            int count = 0;

            while (st.next()) {
                cancelledby = st.getString(1);
                count = st.getInt(2);

                Object[] row = {cancelledby, count};
                model.addRow(row);
            }
            con.close();
        } catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        homePageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame ridedetails=new RideDetails("Ride Details",name,cont,mailid,admin_id);
                ridedetails.setVisible(true);
                ridedetails.setResizable(false);
                dispose();
            }
        });

        noOfRideDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comleted==0) {
                    comleted=1;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
                        Statement stmt = con.createStatement();
                        ResultSet st = stmt.executeQuery("select driver.fname,count(*) as 'Comleted Ride' from ride join driver on driver.driver_id=ride.driver_id group by driver.fname with rollup;");

                        ResultSetMetaData rsmd = st.getMetaData();
                        DefaultTableModel model = (DefaultTableModel) table2.getModel();

                        int col = rsmd.getColumnCount();

                        String[] colname = new String[col];
                        for (int i = 0; i < col; i++) {
                            colname[i] = rsmd.getColumnName(i + 1);
                        }
                        model.setColumnIdentifiers(colname);

                        String fname = null;
                        int count = 0;
                        while (st.next()) {
                            fname = st.getString(1);
                            count = st.getInt(2);

                            Object[] row = {fname, count};
                            model.addRow(row);
                        }
                        con.close();
                    } catch (SQLException exception) {
                        System.out.println(exception);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
        noOfRideDriverButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cancelled==0){
                    cancelled=1;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
                        Statement stmt = con.createStatement();
                        ResultSet st = stmt.executeQuery("select driver.fname,count(*) as 'Cancelled Ride' from cancelled_ride join driver on cancelled_ride.driver_id=driver.driver_id where cancelledby='Driver' group by driver.fname with rollup;");

                        ResultSetMetaData rsmd = st.getMetaData();
                        DefaultTableModel model = (DefaultTableModel) table3.getModel();

                        int col = rsmd.getColumnCount();

                        String[] colname = new String[col];
                        for (int i = 0; i < col; i++) {
                            colname[i] = rsmd.getColumnName(i + 1);
                        }
                        model.setColumnIdentifiers(colname);

                        String fname = null;
                        int count = 0;
                        while (st.next()) {
                            fname = st.getString(1);
                            count = st.getInt(2);

                            Object[] row = {fname, count};
                            model.addRow(row);
                        }
                        con.close();
                    } catch (SQLException exception) {
                        System.out.println(exception);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });
    }
}
