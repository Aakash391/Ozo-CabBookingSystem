import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PaymentDetails extends JFrame{
    private JTable table1;
    private JButton homePageButton;
    private JButton viewYearWisePaymentButton;
    private JPanel jpanel;

    public PaymentDetails(String title,String name,long contact,String mailid,int admin_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo", "root", "Aakash1478@");
            Statement stmt = con.createStatement();

            ResultSet st = stmt.executeQuery("select passenger.fname,passenger.lname,driver.fname,driver.lname,payment.payment_amount from ride " +
                    "join passenger on ride.pass_id=passenger.pass_id " +
                    "join driver on ride.driver_id=driver.driver_id " +
                    "join payment on ride.payment_id=payment.payment_id;");
            String pfname = null;
            String plname = null;
            String dfname = null;
            String dlname = null;
            float amount = 0;

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
                amount = st.getFloat(5);

                Object[] row = {pfname, plname, dfname, dlname, amount};
                model.addRow(row);
            }

            con.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        viewYearWisePaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame yearwise = new YearwisePayment("Year Wise", name, contact, mailid, admin_id);
                yearwise.setVisible(true);
                yearwise.setResizable(false);
                dispose();
            }
        });
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
