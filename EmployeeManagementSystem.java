import java.io.*;
import java.util.*;

/**
 * Employee Management System
 * Beginner-friendly Java console project.
 * Data is stored in employees.txt in the current working directory.
 */
public class EmployeeManagementSystem {
    private static final String FILE_NAME = "employees.txt";
    private static final Scanner SCANNER = new Scanner(System.in);

    static class Employee {
        int id;
        String name;
        String department;
        String designation;
        double salary;

        Employee(int id, String name, String department, String designation, double salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.designation = designation;
            this.salary = salary;
        }

        String toFileLine() {
            return id + "|" + clean(name) + "|" + clean(department) + "|" +
                   clean(designation) + "|" + salary;
        }

        private String clean(String value) {
            return value.replace("|", "/").replace("\n", " ").replace("\r", " ");
        }

        static Employee fromFileLine(String line) {
            String[] parts = line.split("\\|", -1);
            if (parts.length != 5) return null;
            try {
                return new Employee(Integer.parseInt(parts[0]), parts[1], parts[2],
                                    parts[3], Double.parseDouble(parts[4]));
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        ensureFileExists();
        System.out.println("==================================");
        System.out.println("   EMPLOYEE MANAGEMENT SYSTEM");
        System.out.println("==================================");

        while (true) {
            System.out.println("\n1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee by ID");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Exit");

            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: addEmployee(); break;
                case 2: viewEmployees(); break;
                case 3: searchEmployee(); break;
                case 4: updateEmployee(); break;
                case 5: deleteEmployee(); break;
                case 6:
                    System.out.println("Thank you for using the Employee Management System.");
                    SCANNER.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1 to 6.");
            }
        }
    }

    private static void ensureFileExists() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create data file: " + e.getMessage());
        }
    }

    private static List<Employee> readEmployees() {
        List<Employee> employees = new ArrayList<Employee>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Employee employee = Employee.fromFileLine(line);
                    if (employee != null) employees.add(employee);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }
        return employees;
    }

    private static void writeEmployees(List<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Employee employee : employees) writer.println(employee.toFileLine());
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }

    private static boolean idExists(int id) {
        for (Employee employee : readEmployees()) {
            if (employee.id == id) return true;
        }
        return false;
    }

    private static void addEmployee() {
        System.out.println("\n--- Add Employee ---");
        int id = readInt("Enter employee ID: ");
        if (id <= 0) {
            System.out.println("Employee ID must be a positive number.");
            return;
        }
        if (idExists(id)) {
            System.out.println("That employee ID already exists.");
            return;
        }

        String name = readText("Enter employee name: ");
        String department = readText("Enter department: ");
        String designation = readText("Enter designation: ");
        double salary = readDouble("Enter monthly salary: ");
        if (salary < 0) {
            System.out.println("Salary cannot be negative.");
            return;
        }

        List<Employee> employees = readEmployees();
        employees.add(new Employee(id, name, department, designation, salary));
        writeEmployees(employees);
        System.out.println("Employee added successfully.");
    }

    private static void viewEmployees() {
        System.out.println("\n--- Employee List ---");
        List<Employee> employees = readEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.printf("%-8s %-22s %-18s %-20s %12s%n",
                          "ID", "NAME", "DEPARTMENT", "DESIGNATION", "SALARY");
        System.out.println("--------------------------------------------------------------------------------");
        for (Employee e : employees) {
            System.out.printf("%-8d %-22s %-18s %-20s %12.2f%n",
                              e.id, shorten(e.name, 21), shorten(e.department, 17),
                              shorten(e.designation, 19), e.salary);
        }
    }

    private static void searchEmployee() {
        System.out.println("\n--- Search Employee ---");
        int id = readInt("Enter employee ID to search: ");
        Employee employee = findById(id);
        if (employee == null) {
            System.out.println("Employee not found.");
        } else {
            printEmployee(employee);
        }
    }

    private static void updateEmployee() {
        System.out.println("\n--- Update Employee ---");
        int id = readInt("Enter employee ID to update: ");
        List<Employee> employees = readEmployees();
        for (Employee e : employees) {
            if (e.id == id) {
                System.out.println("Press Enter to keep an existing value.");
                String name = readOptionalText("Name [" + e.name + "]: ", e.name);
                String department = readOptionalText("Department [" + e.department + "]: ", e.department);
                String designation = readOptionalText("Designation [" + e.designation + "]: ", e.designation);
                String salaryText = readLine("Monthly salary [" + e.salary + "]: ");
                double salary = e.salary;
                if (!salaryText.trim().isEmpty()) {
                    try {
                        salary = Double.parseDouble(salaryText.trim());
                        if (salary < 0) {
                            System.out.println("Salary cannot be negative. Update cancelled.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid salary. Update cancelled.");
                        return;
                    }
                }

                e.name = name;
                e.department = department;
                e.designation = designation;
                e.salary = salary;
                writeEmployees(employees);
                System.out.println("Employee updated successfully.");
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static void deleteEmployee() {
        System.out.println("\n--- Delete Employee ---");
        int id = readInt("Enter employee ID to delete: ");
        List<Employee> employees = readEmployees();
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().id == id) {
                String confirm = readLine("Are you sure you want to delete this record? (y/n): ");
                if (confirm.equalsIgnoreCase("y")) {
                    iterator.remove();
                    writeEmployees(employees);
                    System.out.println("Employee deleted successfully.");
                } else {
                    System.out.println("Delete cancelled.");
                }
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    private static Employee findById(int id) {
        for (Employee e : readEmployees()) {
            if (e.id == id) return e;
        }
        return null;
    }

    private static void printEmployee(Employee e) {
        System.out.println("Employee ID : " + e.id);
        System.out.println("Name        : " + e.name);
        System.out.println("Department  : " + e.department);
        System.out.println("Designation : " + e.designation);
        System.out.printf("Salary      : %.2f%n", e.salary);
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number, for example 25000.");
            }
        }
    }

    private static String readText(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }

    private static String readOptionalText(String prompt, String currentValue) {
        String value = readLine(prompt).trim();
        return value.isEmpty() ? currentValue : value;
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }

    private static String shorten(String value, int maxLength) {
        if (value.length() <= maxLength) return value;
        return value.substring(0, maxLength - 3) + "...";
    }
}
