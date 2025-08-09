import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PayrollManagementSystem extends JFrame {
    // JDBC URL, username and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Myak@op27";

    // JDBC variables for opening, closing and managing connection
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    // UI
    private JTextField nameField, salaryField;
    private JButton addButton, viewButton, removeButton;
    private JTextArea outputArea;

    public PayrollManagementSystem() {
        super("Payroll Management System");

        // Initialize UI components
        JLabel nameLabel = new JLabel("Name:");
        JLabel salaryLabel = new JLabel("Salary:");
        nameField = new JTextField(20);
        salaryField = new JTextField(20);
        addButton = new JButton("Add Employee");
        viewButton = new JButton("View Employees");
        removeButton = new JButton("Remove Employee");
        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);

        // Add action listeners 
        addButton.addActionListener(new AddButtonListener());
        viewButton.addActionListener(new ViewButtonListener());
        removeButton.addActionListener(new RemoveButtonListener());

        // Layout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(nameLabel, gbc);
        gbc.gridy++;
        inputPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(nameField, gbc);
        gbc.gridy++;
        inputPanel.add(salaryField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);
        gbc.gridy++;
        inputPanel.add(viewButton, gbc);
        gbc.gridy++;
        inputPanel.add(removeButton, gbc);

        JLabel outputLabel = new JLabel("Employee List:");

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(outputPanel, BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            try {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                String query = "INSERT INTO employee (name, salary) VALUES ('" + name + "', " + salary + ")";
                int rowsAffected = statement.executeUpdate(query);
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Employee added successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add employee.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ViewButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            outputArea.setText(""); // Clear output area
            try {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                String query = "SELECT * FROM employee";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double salary = resultSet.getDouble("salary");
                    outputArea.append("Name: " + name + ", Salary: " + salary + "\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class RemoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            try {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                String query = "DELETE FROM employee WHERE name = '" + name + "'";
                int rowsAffected = statement.executeUpdate(query);
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Employee removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "No employee found with that name.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PayrollManagementSystem());
    }
}
