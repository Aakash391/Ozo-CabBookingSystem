import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PassengerProfile extends JFrame {
    private JTable table1;
    private JPanel jpanel;
    private JLabel text2;
    private JLabel phone;
    private JLabel mail;
    private JButton homePageButton;

    public PassengerProfile(String title, String name,Long cont,String mailid,int passid){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();
        System.out.println(name);
        text2.setText(name);
        phone.setText(""+cont);
        mail.setText(mailid);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/ozo","root","Aakash1478@");
            Statement stmt=con.createStatement();
            ResultSet rs1= stmt.executeQuery("Select pass_id from passenger where contact="+cont+";");

            int pass_id=0;

            while(rs1.next()){
                pass_id=rs1.getInt(1);
            }

            ResultSet st=stmt.executeQuery("select ride.pickuplocation,ride.dropofflocation,driver.fname,payment.payment_amount from ride " +
                                            "inner join driver on ride.driver_id=driver.driver_id " +
                                            "join payment on ride.payment_id=payment.payment_id where ride.pass_id="+pass_id+";");

            String ploc = null;
            String dloc = null;

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

                dname = st.getString(3);

                payment = st.getFloat(4);

                System.out.println(ploc);
                System.out.println(dloc);

                System.out.println(dname);
                System.out.println(payment);

                Object[] row = {ploc, dloc, dname, payment};
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
                JFrame homepage=new MainMenu("Main Menu",name,cont,mailid,passid);
                homepage.setVisible(true);
                homepage.setResizable(false);
                dispose();
            }
        });
    }


}
