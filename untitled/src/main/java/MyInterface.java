import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyInterface {
    public static void main(String[] args) {
        JFrame frame = new JFrame("My Interface");

        // Create labels
        JLabel timeLabel = new JLabel("Time:");
        JLabel numQueuesLabel = new JLabel("Number of Queues:");
        JLabel numClientsLabel = new JLabel("Number of Clients:");
        JLabel minServiceTimeLabel = new JLabel("Min Service Time:");
        JLabel maxServiceTimeLabel = new JLabel("Max Service Time:");

        // Create text fields
        JTextField timeField = new JTextField(10);
        JTextField numQueuesField = new JTextField(10);
        JTextField numClientsField = new JTextField(10);
        JTextField minServiceTimeField = new JTextField(10);
        JTextField maxServiceTimeField = new JTextField(10);

        // Create panel and add components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(5, 5, 5, 5);
        panel.add(timeLabel, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(timeField, c);

        // Add other components to the panel...

        // Create a "Start" button
        JButton startButton = new JButton("Start");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(startButton, c);

        // Add action listener to the "Start" button
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve values from the text fields
                    int numQueues = Integer.parseInt(numQueuesField.getText());
                    int numClients = Integer.parseInt(numClientsField.getText());
                    int timeSimulation = Integer.parseInt(timeField.getText());
                    int serviceTimeMin = Integer.parseInt(minServiceTimeField.getText());
                    int serviceTimeMax = Integer.parseInt(maxServiceTimeField.getText());

                    // Call the runSimulation method with the retrieved values
                    Main.runSimulation(numQueues, numClients, timeSimulation, serviceTimeMin, serviceTimeMax, 0, 0);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please enter numeric values.");
                }
            }
        });


        // Add panel to frame and set frame properties
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
