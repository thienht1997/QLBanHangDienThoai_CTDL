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

    /**
     * Thêm khách hàng mới vào danh sách.
     *
     * @param customer khách hàng cần thêm.
     */
    public void addCustomer(Customer customer) {
        customers.appendRaw(customer);
    }

    /**
     * Cập nhật thông tin khách hàng theo mã.
     *
     * @param id      mã cần tìm.
     * @param updated dữ liệu mới.
     * @return true nếu có cập nhật.
     */
    public boolean updateCustomer(String id, Customer updated) {
        return customers.replaceFirst(c -> c.getId().equalsIgnoreCase(id), updated);
    }

    /**
     * Xoá khách hàng theo mã.
     *
     * @param id mã khách cần xoá.
     * @return true nếu xoá thành công.
     */
    public boolean deleteCustomer(String id) {
        return customers.removeFirst(c -> c.getId().equalsIgnoreCase(id));
    }

    /**
     * Tìm khách hàng theo mã.
     *
     * @param id mã cần tìm.
     * @return Optional<Customer>.
     */
    public Optional<Customer> findById(String id) {
        for (Customer c : customers) {
            if (c.getId().equalsIgnoreCase(id)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    /**
     * Tìm theo từ khóa tên.
     *
     * @param keyword chuỗi tìm kiếm.
     * @return danh sách phù hợp.
     */
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

    /**
     * Tìm theo hạng thành viên.
     *
     * @param tier tên hạng (contains).
     * @return danh sách phù hợp.
     */
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

    /** @return danh sách mới sắp xếp tên A-Z. */
    public List<Customer> sortByNameAsc() {
        return sortCopy(Comparator.comparing(Customer::getFullName, String.CASE_INSENSITIVE_ORDER));
    }

    /** @return danh sách mới sắp xếp tên Z-A. */
    public List<Customer> sortByNameDesc() {
        return sortCopy(Comparator.comparing(Customer::getFullName, String.CASE_INSENSITIVE_ORDER).reversed());
    }

    /** @return danh sách mới sắp xếp tổng chi tiêu giảm dần. */
    public List<Customer> sortByTotalSpentDesc() {
        return sortCopy(Comparator.comparingDouble(Customer::getTotalSpent).reversed());
    }

    /** @return danh sách mới sắp xếp theo năm tham gia tăng dần. */
    public List<Customer> sortByJoinYearAsc() {
        return sortCopy(Comparator.comparingInt(Customer::getJoinYear));
    }

    /** @return khách chi tiêu cao nhất (Optional). */
    public Optional<Customer> findHighestSpent() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getTotalSpent() > candidate.getTotalSpent()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return khách chi tiêu thấp nhất (Optional). */
    public Optional<Customer> findLowestSpent() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getTotalSpent() < candidate.getTotalSpent()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return khách tham gia sớm nhất (Optional). */
    public Optional<Customer> findEarliestJoin() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getJoinYear() < candidate.getJoinYear()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return khách tham gia gần nhất (Optional). */
    public Optional<Customer> findLatestJoin() {
        Customer candidate = null;
        for (Customer c : customers) {
            if (candidate == null || c.getJoinYear() > candidate.getJoinYear()) {
                candidate = c;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** @return tổng chi tiêu của toàn bộ khách hàng. */
    public double totalSpent() {
        double total = 0;
        for (Customer c : customers) {
            total += c.getTotalSpent();
        }
        return total;
    }

    /** @return chi tiêu trung bình. */
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

    /** @return số khách hiện có. */
    public long countCustomers() {
        return customers.size();
    }

    /** @return Map hạng -> số khách. */
    public Map<String, Long> countByTier() {
        Map<String, Long> summary = new HashMap<>();
        for (Customer c : customers) {
            summary.merge(c.getTier(), 1L, Long::sum);
        }
        return summary;
    }

    /** @return Map năm tham gia -> số khách. */
    public Map<Integer, Long> countByJoinYear() {
        Map<Integer, Long> summary = new HashMap<>();
        for (Customer c : customers) {
            summary.merge(c.getJoinYear(), 1L, Long::sum);
        }
        return summary;
    }

    /** @return Map domain email -> số khách. */
    public Map<String, Long> countByEmailDomain() {
        Map<String, Long> summary = new HashMap<>();
        for (Customer c : customers) {
            String[] parts = c.getEmail().split("@");
            String domain = parts.length > 1 ? parts[1] : "unknown";
            summary.merge(domain, 1L, Long::sum);
        }
        return summary;
    }

    /** @return danh sách khách hàng (bản sao). */
    public List<Customer> getAll() {
        return customers.toList();
    }

    /**
     * Thay thế toàn bộ dữ liệu (dùng khi đọc file).
     *
     * @param newCustomers danh sách mới.
     */
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
