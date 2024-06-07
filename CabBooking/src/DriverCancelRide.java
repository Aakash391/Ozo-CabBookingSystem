import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DriverCancelRide extends JFrame{
    private JTable table1;
    private JButton homePageButton;
    private JPanel jpanel;

    public DriverCancelRide(String title,String name,long contact,String mailid,int admin_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();

            ResultSet st=stmt.executeQuery("select * from " +
                    "(select driver.fname,count(*) as 'Comleted Ride' from ride join driver on driver.driver_id=ride.driver_id " +
                    " group by driver.fname with rollup) as t1 " +
                    "left join (select driver.fname,count(*) as 'Cancelled Ride' from cancelled_ride " +
                    "join driver on cancelled_ride.driver_id=driver.driver_id where cancelledby='Driver' group by driver.fname with rollup) as t2 " +
                    "on t1.fname=t2.fname " +
                    "union " +
                    "select * from " +
                    "(select driver.fname,count(*) as 'Comleted Ride' from ride join driver on driver.driver_id=ride.driver_id " +
                    " group by driver.fname with rollup) as t1 " +
                    "right join (select driver.fname,count(*) as 'Cancelled Ride' from cancelled_ride " +
                    "join driver on cancelled_ride.driver_id=driver.driver_id where cancelledby='Driver' group by driver.fname with rollup) as t2 " +
                    "on t1.fname=t2.fname;");

            System.out.println("After query");
            ResultSetMetaData rsmd = st.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            int col = rsmd.getColumnCount();
            System.out.println(col);
            String[] colname = new String[col];
            for (int i = 0; i < col; i++) {
                colname[i] = rsmd.getColumnName(i + 1);
                System.out.println(colname[i]);
            }
            model.setColumnIdentifiers(colname);
            String fname=null;
            String fname1=null;
            int cancel=0;
            int complete=0;
            while (st.next()){

                fname=st.getString(1);
                complete=st.getInt(2);
                fname1=st.getString(3);
                cancel=st.getInt(4);

                Object[] row = {fname,complete,fname1,cancel};
                model.addRow(row);
            }

            System.out.println("Working");

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
                JFrame adminmain=new AdminMainMenu("Admin Main Menu",name,contact,mailid,admin_id);
                adminmain.setVisible(true);
                adminmain.setResizable(false);
                dispose();

            }
        });
    }
}
