import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMainMenu extends JFrame {
    private JButton viewPassengerDetailButton;
    private JButton viewRideDetailButton;
    private JButton viewDriverDetailButton;
    private JButton viewPaymentDetailButton;
    private JButton viewRemovedDriverButton;
    private JButton viewCancelledRideButton;
    private JPanel jpanel;

    public AdminMainMenu(String title,String name,long cont,String mailid,int admin_id){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();
        viewPassengerDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame pdetail=new Details("Passenger Details",name,cont,mailid,admin_id);
                pdetail.setVisible(true);
                pdetail.setResizable(false);
                dispose();
            }
        });
        viewDriverDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame driverdetail=new DriverDetail("Driver Detail",name,cont,mailid,admin_id);
                driverdetail.setVisible(true);
                driverdetail.setResizable(false);
                dispose();
            }
        });
        viewRideDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame rideDetails=new RideDetails("Ride Details",name,cont,mailid,admin_id);
                rideDetails.setVisible(true);
                rideDetails.setResizable(false);
                dispose();
            }
        });
        viewPaymentDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame payment=new PaymentDetails("Payment Details",name,cont,mailid,admin_id);
                payment.setVisible(true);
                payment.setResizable(false);
                dispose();
            }
        });
        viewRemovedDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame pdetail=new Details("Removed Driver Details",name,cont,mailid,admin_id);
                pdetail.setVisible(true);
                pdetail.setResizable(false);
                dispose();
            }
        });
        viewCancelledRideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame pdetail=new Details("Cancelled Ride Details",name,cont,mailid,admin_id);
                pdetail.setVisible(true);
                pdetail.setResizable(false);
                dispose();
            }
        });
    }


}
