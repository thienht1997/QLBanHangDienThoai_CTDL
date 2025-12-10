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

    /**
     * Khởi tạo đầy đủ thông tin nhân viên.
     *
     * @param id              mã nhân viên.
     * @param fullName        họ tên.
     * @param role            chức vụ.
     * @param phone           số điện thoại.
     * @param salary          lương cơ bản.
     * @param yearsExperience số năm kinh nghiệm.
     * @param monthlySales    doanh số trung bình tháng.
     */
    public Employee(String id, String fullName, String role, String phone, double salary, int yearsExperience, double monthlySales) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.salary = salary;
        this.yearsExperience = yearsExperience;
        this.monthlySales = monthlySales;
    }

    /** Mã nhân viên (khóa chính). */
    public String getId() {
        return id;
    }

    /** Họ tên nhân viên. */
    public String getFullName() {
        return fullName;
    }

    /** Chức vụ (Sales/Manager/...). */
    public String getRole() {
        return role;
    }

    /** Số điện thoại liên hệ. */
    public String getPhone() {
        return phone;
    }

    /** Lương cơ bản hàng tháng. */
    public double getSalary() {
        return salary;
    }

    /** Số năm kinh nghiệm. */
    public int getYearsExperience() {
        return yearsExperience;
    }

    /** Doanh số trung bình 1 tháng. */
    public double getMonthlySales() {
        return monthlySales;
    }

    /**
     * Xuất nhân viên thành 1 dòng CSV.
     *
     * @return chuỗi CSV 7 cột.
     */
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

    /**
     * Parse 1 dòng CSV thành đối tượng Employee.
     *
     * @param line dòng CSV.
     * @return Employee tương ứng.
     */
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
