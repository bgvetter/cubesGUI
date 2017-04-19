import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sylentbv on 4/17/2017.
 */
public class UpdateRecord extends JFrame{
    private JPanel rootPanel;
    private JTextField nameTF;
    private JTextField timeTF;
    private JButton updateButton;
    private JButton cancelButton;

    private int updateID;
    cubesModel cubesModelLocal;

    UpdateRecord(final cubesModel cubesDataTableModel, int id) {

        setContentPane(rootPanel);
        pack();
        setTitle("Rubicks Cube Tracker Update");

        setSize(new Dimension(400, 300));

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //set values for current record
        updateID = id;
        cubesModelLocal = cubesDataTableModel;

        nameTF.setText(cubesDataTableModel.getValueAt(id,1).toString());
        timeTF.setText(cubesDataTableModel.getValueAt(id,2).toString());

        addListeners();
    }

    protected void addListeners(){
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(UpdateRecord.this,"Are you sure you want to cancel the update?",
                        "Cancel",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    //open main form and close current form
                    GUI tableGUI = new GUI(cubesModelLocal);
                    UpdateRecord.this.dispose();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTF.getText();

                double timeData;

                try {
                    timeData = Double.parseDouble(timeTF.getText());

                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(rootPane,
                            "Time needs to be a numeric value.");
                    return;
                }
                //update record and reload jtable data set
                cubesModelLocal.updateRow(updateID,name,timeData);
                cubesModelLocal.loadAll();

                //open main form and close current form
                GUI tableGUI = new GUI(cubesModelLocal);
                UpdateRecord.this.dispose();
            }
        });
    }
}
