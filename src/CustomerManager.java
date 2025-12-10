import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Quản lý danh sách khách hàng trên danh sách liên kết đơn.
 */
public class CustomerManager {
    private final SinglyLinkedList<Customer> customers = new SinglyLinkedList<>();

    public void addCustomer(Customer customer) {
        customers.addLast(customer);
    }

    public boolean updateCustomer(String id, Customer updated) {
        return customers.update(c -> c.getId().equalsIgnoreCase(id), c -> updated);
    }

    public boolean deleteCustomer(String id) {
        return customers.removeIf(c -> c.getId().equalsIgnoreCase(id));
    }

    public Optional<Customer> findById(String id) {
        return customers.findFirst(c -> c.getId().equalsIgnoreCase(id));
    }

    public List<Customer> findByNameKeyword(String keyword) {
        String normalized = keyword.toLowerCase(Locale.ROOT);
        List<Customer> result = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getFullName().toLowerCase(Locale.ROOT).contains(normalized)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Customer> findByTier(String tier) {
        String normalized = tier.toLowerCase(Locale.ROOT);
        List<Customer> result = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getTier().toLowerCase(Locale.ROOT).contains(normalized)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Customer> sortByNameAsc() {
        return sortCopy(Comparator.comparing(Customer::getFullName, String.CASE_INSENSITIVE_ORDER));
    }

    public List<Customer> sortByNameDesc() {
        return sortCopy(Comparator.comparing(Customer::getFullName, String.CASE_INSENSITIVE_ORDER).reversed());
    }

    public List<Customer> sortByTotalSpentDesc() {
        return sortCopy(Comparator.comparingDouble(Customer::getTotalSpent).reversed());
    }

    public List<Customer> sortByJoinYearAsc() {
        return sortCopy(Comparator.comparingInt(Customer::getJoinYear));
    }

    public Optional<Customer> findHighestSpent() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getTotalSpent() > candidate.getTotalSpent()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public Optional<Customer> findLowestSpent() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getTotalSpent() < candidate.getTotalSpent()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public Optional<Customer> findEarliestJoin() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getJoinYear() < candidate.getJoinYear()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public Optional<Customer> findLatestJoin() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getJoinYear() > candidate.getJoinYear()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    public double totalSpent() {
        double total = 0;
        for (Customer c : customers) {
            total += c.getTotalSpent();
        }
        return total;
    }

    public double averageSpent() {
        if (customers.isEmpty()) {
            return 0;
        }
        double sum = 0;
        int count = 0;
        for (Customer c : customers) {
            sum += c.getTotalSpent();
            count++;
        }
        return sum / count;
    }

    public long countCustomers() {
        return customers.size();
    }

    public Map<String, Long> countByTier() {
        Map<String, Long> summary = new HashMap<>();
        for (Customer c : customers) {
            summary.merge(c.getTier(), 1L, Long::sum);
        }
        return summary;
    }

    public Map<Integer, Long> countByJoinYear() {
        Map<Integer, Long> summary = new HashMap<>();
        for (Customer c : customers) {
            summary.merge(c.getJoinYear(), 1L, Long::sum);
        }
        return summary;
    }

    public Map<String, Long> countByEmailDomain() {
        Map<String, Long> summary = new HashMap<>();
        for (Customer c : customers) {
            String[] parts = c.getEmail().split("@");
            String domain = parts.length > 1 ? parts[1] : "unknown";
            summary.merge(domain, 1L, Long::sum);
        }
        return summary;
    }

    public List<Customer> getAll() {
        return customers.toList();
    }

    public void replaceAll(List<Customer> newCustomers) {
        customers.clear();
        customers.bulkAdd(newCustomers);
    }

    private List<Customer> sortCopy(Comparator<Customer> comparator) {
        List<Customer> copy = customers.toList();
        copy.sort(comparator);
        return copy;
    }
}
