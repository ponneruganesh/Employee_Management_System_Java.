# Employee Management System (Java)

## Project overview
A beginner-friendly Java console application for maintaining employee records. It supports adding, viewing, searching, updating, and deleting employee information. Records are saved in a local text file, so they remain available after the program is closed.

## Requirements
- JDK 8 or newer
- Command Prompt/Terminal, or an IDE such as Dev-C++ is NOT suitable for Java. Use Command Prompt, VS Code with Java extensions, IntelliJ IDEA, Eclipse, or a Java-enabled IDE.

## Run using Command Prompt / Terminal
1. Extract the ZIP file.
2. Open a terminal inside the extracted `Employee_Management_System` folder.
3. Compile:
   `javac src/EmployeeManagementSystem.java`
4. Run:
   `java -cp src EmployeeManagementSystem`

The program creates `employees.txt` in the folder from which you run the `java` command. To keep the data file with the project, run the command while your terminal is in the extracted project folder.

## Run in an IDE
Create/open a Java project, copy `src/EmployeeManagementSystem.java` into the source folder, then run the `EmployeeManagementSystem` class.

## Menu
1. Add Employee
2. View All Employees
3. Search Employee by ID
4. Update Employee
5. Delete Employee
6. Exit

## Employee fields
- Employee ID
- Name
- Department
- Designation
- Monthly salary

## Data storage
The program stores one employee per line in `employees.txt`, using `|` between fields. Do not manually put `|` inside a field.

## Notes
- This is a local, single-user educational project, not a production HR system.
- It does not include login, network access, or a database server.
- Take screenshots of the program running and include them in your internship submission if required.
