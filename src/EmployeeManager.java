import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Quản lý danh sách nhân viên.
 */
public class EmployeeManager {
    private final SinglyLinkedList<Employee> employees = new SinglyLinkedList<>();

    /**
     * Thêm nhân viên mới.
     *
     * @param employee nhân viên cần thêm.
     */
    public void addEmployee(Employee employee) {
        employees.appendRaw(employee);
    }

    /**
     * Cập nhật nhân viên theo mã.
     *
     * @param id      mã cần tìm.
     * @param updated dữ liệu mới.
     * @return true nếu cập nhật thành công.
     */
    public boolean updateEmployee(String id, Employee updated) {
        return employees.replaceFirst(e -> e.getId().equalsIgnoreCase(id), updated);
    }

    /**
     * Xoá nhân viên theo mã.
     *
     * @param id mã cần xoá.
     * @return true nếu xoá thành công.
     */
    public boolean deleteEmployee(String id) {
        return employees.removeFirst(e -> e.getId().equalsIgnoreCase(id));
    }

    /**
     * Tìm nhân viên theo mã (không phân biệt hoa thường).
     *
     * @param id mã cần tìm.
     * @return Optional<Employee>.
     */
    public Optional<Employee> findById(String id) {
        for (Employee e : employees) {
            if (e.getId().equalsIgnoreCase(id)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    /**
     * Tìm theo chức vụ.
     *
     * @param roleKeyword từ khóa chức vụ.
     * @return danh sách phù hợp.
     */
    public List<Employee> findByRole(String roleKeyword) {
        String normalized = roleKeyword.toLowerCase(Locale.ROOT);
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getRole().toLowerCase(Locale.ROOT).contains(normalized)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Tìm theo từ khóa tên.
     *
     * @param keyword chuỗi tìm kiếm.
     * @return danh sách phù hợp.
     */
    public List<Employee> findByNameKeyword(String keyword) {
        String normalized = keyword.toLowerCase(Locale.ROOT);
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getFullName().toLowerCase(Locale.ROOT).contains(normalized)) {
                result.add(e);
            }
        }
        return result;
    }

    /** @return danh sách mới sắp xếp lương giảm dần. */
    public List<Employee> sortBySalaryDesc() {
        return sortCopy(Comparator.comparingDouble(Employee::getSalary).reversed());
    }

    /** @return danh sách mới sắp xếp lương tăng dần. */
    public List<Employee> sortBySalaryAsc() {
        return sortCopy(Comparator.comparingDouble(Employee::getSalary));
    }

    /** @return danh sách mới sắp xếp kinh nghiệm giảm dần. */
    public List<Employee> sortByExperienceDesc() {
        return sortCopy(Comparator.comparingInt(Employee::getYearsExperience).reversed());
    }

    /** @return danh sách mới sắp xếp doanh số giảm dần. */
    public List<Employee> sortBySalesDesc() {
        return sortCopy(Comparator.comparingDouble(Employee::getMonthlySales).reversed());
    }

    /** @return nhân viên lương cao nhất (Optional). */
    public Optional<Employee> findHighestSalary() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getSalary() > candidate.getSalary()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return nhân viên lương thấp nhất (Optional). */
    public Optional<Employee> findLowestSalary() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getSalary() < candidate.getSalary()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return nhân viên kinh nghiệm nhiều nhất (Optional). */
    public Optional<Employee> findMostExperience() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getYearsExperience() > candidate.getYearsExperience()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return nhân viên doanh số cao nhất (Optional). */
    public Optional<Employee> findTopSales() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getMonthlySales() > candidate.getMonthlySales()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return tổng quỹ lương. */
    public double totalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

    /** @return lương trung bình. */
    public double averageSalary() {
        if (employees.isEmpty()) {
            return 0;
        }
        double sum = 0;
        int count = 0;
        for (Employee e : employees) {
            sum += e.getSalary();
            count++;
        }
        return sum / count;
    }

    /** @return kinh nghiệm trung bình. */
    public double averageExperience() {
        if (employees.isEmpty()) {
            return 0;
        }
        double sum = 0;
        int count = 0;
        for (Employee e : employees) {
            sum += e.getYearsExperience();
            count++;
        }
        return sum / count;
    }

    /** @return tổng doanh số tháng của toàn bộ nhân viên. */
    public double totalMonthlySales() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getMonthlySales();
        }
        return total;
    }

    /** @return số nhân viên. */
    public long countEmployees() {
        return employees.size();
    }

    /** @return Map chức vụ -> số nhân viên. */
    public Map<String, Long> countByRole() {
        Map<String, Long> summary = new HashMap<>();
        for (Employee e : employees) {
            summary.merge(e.getRole(), 1L, Long::sum);
        }
        return summary;
    }

    /** @return danh sách nhân viên (bản sao). */
    public List<Employee> getAll() {
        return employees.toList();
    }

    /**
     * Thay thế toàn bộ dữ liệu (dùng khi đọc file).
     *
     * @param newEmployees danh sách mới.
     */
    public void replaceAll(List<Employee> newEmployees) {
        employees.clear();
        employees.bulkAdd(newEmployees);
    }

    private List<Employee> sortCopy(Comparator<Employee> comparator) {
        List<Employee> copy = employees.toList();
        copy.sort(comparator);
        return copy;
    }
}
