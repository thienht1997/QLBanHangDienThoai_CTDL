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

    public void addEmployee(Employee employee) {
        employees.addLast(employee);
    }

    public boolean updateEmployee(String id, Employee updated) {
        return employees.update(e -> e.getId().equalsIgnoreCase(id), e -> updated);
    }

    public boolean deleteEmployee(String id) {
        return employees.removeIf(e -> e.getId().equalsIgnoreCase(id));
    }

    public Optional<Employee> findById(String id) {
        return employees.findFirst(e -> e.getId().equalsIgnoreCase(id));
    }

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

    public List<Employee> sortBySalaryDesc() {
        return sortCopy(Comparator.comparingDouble(Employee::getSalary).reversed());
    }

    public List<Employee> sortBySalaryAsc() {
        return sortCopy(Comparator.comparingDouble(Employee::getSalary));
    }

    public List<Employee> sortByExperienceDesc() {
        return sortCopy(Comparator.comparingInt(Employee::getYearsExperience).reversed());
    }

    public List<Employee> sortBySalesDesc() {
        return sortCopy(Comparator.comparingDouble(Employee::getMonthlySales).reversed());
    }

    public Optional<Employee> findHighestSalary() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getSalary() > candidate.getSalary()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public Optional<Employee> findLowestSalary() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getSalary() < candidate.getSalary()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public Optional<Employee> findMostExperience() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getYearsExperience() > candidate.getYearsExperience()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public Optional<Employee> findTopSales() {
        Employee candidate = null;
        for (Employee e : employees) {
            if (candidate == null || e.getMonthlySales() > candidate.getMonthlySales()) {
                candidate = e;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public double totalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

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

    public double totalMonthlySales() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getMonthlySales();
        }
        return total;
    }

    public long countEmployees() {
        return employees.size();
    }

    public Map<String, Long> countByRole() {
        Map<String, Long> summary = new HashMap<>();
        for (Employee e : employees) {
            summary.merge(e.getRole(), 1L, Long::sum);
        }
        return summary;
    }

    public List<Employee> getAll() {
        return employees.toList();
    }

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
