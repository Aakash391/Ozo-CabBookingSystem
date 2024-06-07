import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class YearwisePayment extends JFrame{
    private JTable table1;
    private JButton homePageButton;
    private JPanel jpanel;
    private JButton moreDetailedButton;
    private JTable table2;
    int moredetail=0;

    public YearwisePayment(String title,String name,long cont,String mailid,int admin_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
            Statement stmt = con.createStatement();

            ResultSet st=stmt.executeQuery("select year(startdate),sum(payment_amount) from ride join payment on payment.payment_id=ride.payment_id group by year(startdate) with rollup;");
            ResultSetMetaData rsmd = st.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            int col = rsmd.getColumnCount();

            String[] colname = new String[col];
            for (int i = 0; i < col; i++) {
                colname[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colname);
            String year=null;
            float amt=0;
            while (st.next()){
                year=st.getString(1);
                amt=st.getFloat(2);

                Object[] row = {year,amt};
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
                JFrame payment=new PaymentDetails("Payment Details",name,cont,mailid,admin_id);
                payment.setVisible(true);
                payment.setResizable(false);
                dispose();
            }
        });
        moreDetailedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(moredetail==0) {
                    moredetail=1;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
                        Statement stmt = con.createStatement();

                        ResultSet st = stmt.executeQuery("select * from (select year(ride.startdate) as yr,count(*) as 'Ride Completed' from ride group by year(startdate) with rollup) as t1 " +
                                "right join (select year(cancelled_ride.startdate) as yr,count(*) as 'Ride Cancelled' from cancelled_ride group by year(startdate) with rollup) as t2 " +
                                "on t1.yr=t2.yr;");
                        ResultSetMetaData rsmd = st.getMetaData();
                        DefaultTableModel model = (DefaultTableModel) table2.getModel();

                        int col = rsmd.getColumnCount();

                        String[] colname = new String[col];
                        for (int i = 0; i < col; i++) {
                            colname[i] = rsmd.getColumnName(i + 1);
                        }
                        model.setColumnIdentifiers(colname);
                        String year = null;
                        int count = 0;
                        String year2=null;
                        int count2 = 0;
                        while (st.next()) {
                            year = st.getString(1);
                            count = st.getInt(2);
                            year2=st.getString(3);
                            count2 = st.getInt(4);
                            Object[] row = {year, count, year2,count2};
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
