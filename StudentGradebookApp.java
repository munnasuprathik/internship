import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentGradebookApp {
    private List<Student> students = new ArrayList<>();
    private DefaultTableModel tableModel;

    private JFrame frame;
    private JPanel inputPanel;
    private JTextField nameField;
    private JTextField assignmentField;
    private JTextField examField;
    private JButton addButton;
    private JButton calculateButton;
    private JButton calculateStatisticsButton;
    private JButton sortByNameButton;
    private JButton sortByGradeButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JTable studentTable;

    public StudentGradebookApp() {
        frame = new JFrame("Student Gradebook App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Name", "Assignment Score", "Exam Score", "Final Grade"}, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        nameField = new JTextField(15);
        assignmentField = new JTextField(5);
        examField = new JTextField(5);

        addButton = new JButton("Add Student");
        addButton.addActionListener(new AddStudentListener());

        calculateButton = new JButton("Calculate Grades");
        calculateButton.addActionListener(new CalculateGradesListener());

        calculateStatisticsButton = new JButton("Calculate Statistics");
        calculateStatisticsButton.addActionListener(new CalculateStatisticsListener());

        sortByNameButton = new JButton("Sort by Name");
        sortByNameButton.addActionListener(new SortByNameListener());

        sortByGradeButton = new JButton("Sort by Grade");
        sortByGradeButton.addActionListener(new SortByGradeListener());

        editButton = new JButton("Edit Student");
        editButton.addActionListener(new EditStudentListener());

        deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(new DeleteStudentListener());

        exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(new ExportToCSVListener());

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Assignment Score:"));
        inputPanel.add(assignmentField);
        inputPanel.add(new JLabel("Exam Score:"));
        inputPanel.add(examField);
        inputPanel.add(addButton);
        inputPanel.add(calculateButton);
        inputPanel.add(calculateStatisticsButton);
        inputPanel.add(sortByNameButton);
        inputPanel.add(sortByGradeButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(exportButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private class AddStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            double assignmentScore = Double.parseDouble(assignmentField.getText());
            double examScore = Double.parseDouble(examField.getText());
            Student student = new Student(name, assignmentScore, examScore);
            students.add(student);
            tableModel.addRow(new Object[]{name, assignmentScore, examScore, ""});
            nameField.setText("");
            assignmentField.setText("");
            examField.setText("");
        }
    }

    private class CalculateGradesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                student.calculateFinalGrade();
                studentTable.setValueAt(student.getFinalGrade(), i, 3);
            }
        }
    }

    private class CalculateStatisticsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double totalFinalGrade = 0;
            double highestFinalGrade = Double.MIN_VALUE;
            double lowestFinalGrade = Double.MAX_VALUE;

            for (Student student : students) {
                totalFinalGrade += student.getFinalGrade();
                highestFinalGrade = Math.max(highestFinalGrade, student.getFinalGrade());
                lowestFinalGrade = Math.min(lowestFinalGrade, student.getFinalGrade());
            }

            double classAverage = totalFinalGrade / students.size();
            JOptionPane.showMessageDialog(frame, "Class Average: " + classAverage +
                    "\nHighest Final Grade: " + highestFinalGrade +
                    "\nLowest Final Grade: " + lowestFinalGrade, "Class Statistics", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class SortByNameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Collections.sort(students, (s1, s2) -> s1.getName().compareTo(s2.getName()));
            updateTable();
        }
    }

    private class SortByGradeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Collections.sort(students, (s1, s2) -> Double.compare(s2.getFinalGrade(), s1.getFinalGrade()));
            updateTable();
        }
    }

    private class EditStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                String name = nameField.getText();
                double assignmentScore = Double.parseDouble(assignmentField.getText());
                double examScore = Double.parseDouble(examField.getText());

                Student student = students.get(selectedRow);
                student.setName(name);
                student.setAssignmentScore(assignmentScore);
                student.setExamScore(examScore);

                studentTable.setValueAt(name, selectedRow, 0);
                studentTable.setValueAt(assignmentScore, selectedRow, 1);
                studentTable.setValueAt(examScore, selectedRow, 2);

                nameField.setText("");
                assignmentField.setText("");
                examField.setText("");
            }
        }
    }

    private class DeleteStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                students.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private class ExportToCSVListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                File csvFile = new File("student_data.csv");
                FileWriter csvWriter = new FileWriter(csvFile);
                csvWriter.write("Name,Assignment Score,Exam Score,Final Grade\n");
                for (Student student : students) {
                    csvWriter.write(student.getName() + "," + student.getAssignmentScore() + "," +
                            student.getExamScore() + "," + student.getFinalGrade() + "\n");
                }
                csvWriter.close();
                JOptionPane.showMessageDialog(frame, "Data exported to student_data.csv", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error exporting data to CSV", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{student.getName(), student.getAssignmentScore(), student.getExamScore(), student.getFinalGrade()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGradebookApp());
    }
}

class Student {
    private String name;
    private double assignmentScore;
    private double examScore;
    private double finalGrade;

    public Student(String name, double assignmentScore, double examScore) {
        this.name = name;
        this.assignmentScore = assignmentScore;
        this.examScore = examScore;
    }

    public void calculateFinalGrade() {
        finalGrade = (assignmentScore * 0.4) + (examScore * 0.6);
    }

    public double getAssignmentScore() {
        return assignmentScore;
    }

    public void setAssignmentScore(double assignmentScore) {
        this.assignmentScore = assignmentScore;
    }

    public double getExamScore() {
        return examScore;
    }

    public void setExamScore(double examScore) {
        this.examScore = examScore;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
