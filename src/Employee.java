import java.util.Objects;

/**
 * Thông tin nhân viên bán hàng.
 */
public class Employee {
    private final String id;
    private final String fullName;
    private final String role;
    private final String phone;
    private final double salary;
    private final int yearsExperience;
    private final double monthlySales;

    public Employee(String id, String fullName, String role, String phone, double salary, int yearsExperience, double monthlySales) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.salary = salary;
        this.yearsExperience = yearsExperience;
        this.monthlySales = monthlySales;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public double getSalary() {
        return salary;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public double getMonthlySales() {
        return monthlySales;
    }

    public String toCsv() {
        return String.join(",",
                id,
                fullName,
                role,
                phone,
                String.valueOf(salary),
                String.valueOf(yearsExperience),
                String.valueOf(monthlySales));
    }

    public static Employee fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid employee line: " + line);
        }
        return new Employee(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                Double.parseDouble(parts[4].trim()),
                Integer.parseInt(parts[5].trim()),
                Double.parseDouble(parts[6].trim()));
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                ", salary=" + salary +
                ", yearsExperience=" + yearsExperience +
                ", monthlySales=" + monthlySales +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
