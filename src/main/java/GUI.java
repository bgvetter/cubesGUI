import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;

/**
 * Created by sylentbv on 4/12/2017.
 */
public class GUI extends  JFrame implements WindowListener{
    private JTable resultList;
    private JPanel rootPanel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField nameTF;
    private JTextField timeTF;

    GUI(final cubesModel cubesDataTableModel) {

        //set up window
        setContentPane(rootPanel);
        pack();
        setTitle("Rubicks Cube Tracker");
        addWindowListener(this);

        setSize(new Dimension(700, 250));

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //add data to jtable
        resultList.setModel(cubesDataTableModel);
        addListeners(cubesDataTableModel);
    }

    //set up button listeners
    private void addListeners(cubesModel cubesM){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String name = nameTF.getText();

                if (name == null || name.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a name");
                    return;
                }


                double timeData;
                //verify time is a number
                try {
                    timeData = Double.parseDouble(timeTF.getText());

                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(rootPane,
                            "Time needs to be a numeric value.");
                    return;
                }


                System.out.println("Adding " + name + " " + timeData);
                boolean insertedRow = cubesM.insertRow(name, timeData);

                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new record");
                }
                // If insertedRow is true and the data was added, it should show up in the table, so no need for confirmation message.
            }

        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = resultList.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a record to delete");
                }
                //call the delete function
                boolean deleted = cubesM.deleteRow(currentRow);
                if (deleted) {
                    //reload data for jtable
                    cubesM.loadAll();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting record");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = resultList.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a record to delete");
                    return;
                }

                //open update form
                UpdateRecord ur = new UpdateRecord(cubesM,currentRow);
                //close current form
                GUI.this.dispose();

            }
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
    }

    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
