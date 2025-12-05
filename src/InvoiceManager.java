import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Quản lý danh sách hóa đơn: CRUD, tìm kiếm, sắp xếp, thống kê doanh thu.
 */
public class InvoiceManager {
    private final SinglyLinkedList<Invoice> invoices = new SinglyLinkedList<>();

    /**
     * Thêm hóa đơn mới.
     *
     * @param invoice dữ liệu cần thêm.
     */
    public void addInvoice(Invoice invoice) {
        invoices.addLast(invoice);
    }

    /**
     * Cập nhật hóa đơn theo mã.
     *
     * @param id      mã cần tìm.
     * @param updated dữ liệu mới.
     * @return true nếu cập nhật thành công.
     */
    public boolean updateInvoice(String id, Invoice updated) {
        return invoices.update(invoice -> invoice.getId().equalsIgnoreCase(id), invoice -> updated);
    }

    /**
     * Xoá hóa đơn theo mã.
     *
     * @param id mã cần xoá.
     * @return true nếu xoá thành công.
     */
    public boolean deleteInvoice(String id) {
        return invoices.removeIf(invoice -> invoice.getId().equalsIgnoreCase(id));
    }

    /**
     * Tìm theo mã.
     *
     * @param id mã cần tìm.
     * @return Optional chứa hóa đơn phù hợp.
     */
    public Optional<Invoice> findById(String id) {
        return invoices.findFirst(invoice -> invoice.getId().equalsIgnoreCase(id));
    }

    /**
     * Tìm theo tên khách (gần đúng).
     *
     * @param customerName chuỗi cần tìm.
     * @return danh sách kết quả.
     */
    public List<Invoice> findByCustomerName(String customerName) {
        String normalized = customerName.toLowerCase(Locale.ROOT);
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getCustomerName().toLowerCase(Locale.ROOT).contains(normalized)) {
                result.add(invoice);
            }
        }
        return result;
    }

    /**
     * Tìm theo khoảng ngày mua.
     *
     * @param start ngày bắt đầu.
     * @param end   ngày kết thúc.
     * @return danh sách kết quả.
     */
    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            LocalDate saleDate = invoice.getSaleDate();
            if (!saleDate.isBefore(start) && !saleDate.isAfter(end)) {
                result.add(invoice);
            }
        }
        return result;
    }

    /**
     * Tìm theo mã điện thoại.
     *
     * @param phoneId mã điện thoại.
     * @return danh sách hóa đơn bán mẫu máy đó.
     */
    public List<Invoice> findByPhoneId(String phoneId) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getPhoneId().equalsIgnoreCase(phoneId)) {
                result.add(invoice);
            }
        }
        return result;
    }

    /**
     * Sắp xếp hóa đơn theo ngày tăng dần.
     *
     * @return danh sách mới sau sắp xếp.
     */
    public List<Invoice> sortByDateAsc() {
        return sortCopy(Comparator.comparing(Invoice::getSaleDate));
    }

    /**
     * Sắp xếp hóa đơn theo ngày giảm dần.
     *
     * @return danh sách mới sau sắp xếp.
     */
    public List<Invoice> sortByDateDesc() {
        return sortCopy(Comparator.comparing(Invoice::getSaleDate).reversed());
    }

    /**
     * Sắp xếp theo giá trị đơn hàng giảm dần.
     *
     * @return danh sách mới sau sắp xếp.
     */
    public List<Invoice> sortByNetTotalDesc() {
        return sortCopy(Comparator.comparingDouble(Invoice::getNetTotal).reversed());
    }

    /**
     * Sắp xếp theo số lượng bán giảm dần.
     *
     * @return danh sách mới sau sắp xếp.
     */
    public List<Invoice> sortByQuantityDesc() {
        return sortCopy(Comparator.comparingInt(Invoice::getQuantity).reversed());
    }

    /**
     * Tìm hóa đơn có giá trị lớn nhất.
     *
     * @return Optional chứa hóa đơn phù hợp.
     */
    public Optional<Invoice> findLargestOrder() {
        Invoice candidate = null;
        for (Invoice invoice : invoices) {
            if (candidate == null || invoice.getNetTotal() > candidate.getNetTotal()) {
                candidate = invoice;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /**
     * Tìm hóa đơn có giá trị nhỏ nhất.
     *
     * @return Optional hóa đơn giá trị thấp nhất.
     */
    public Optional<Invoice> findSmallestOrder() {
        Invoice candidate = null;
        for (Invoice invoice : invoices) {
            if (candidate == null || invoice.getNetTotal() < candidate.getNetTotal()) {
                candidate = invoice;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /**
     * Tìm hóa đơn có số lượng bán lớn nhất.
     *
     * @return Optional kết quả tương ứng.
     */
    public Optional<Invoice> findHighestQuantity() {
        Invoice candidate = null;
        for (Invoice invoice : invoices) {
            if (candidate == null || invoice.getQuantity() > candidate.getQuantity()) {
                candidate = invoice;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /**
     * Tìm hóa đơn có số lượng bán thấp nhất.
     *
     * @return Optional kết quả tương ứng.
     */
    public Optional<Invoice> findLowestQuantity() {
        Invoice candidate = null;
        for (Invoice invoice : invoices) {
            if (candidate == null || invoice.getQuantity() < candidate.getQuantity()) {
                candidate = invoice;
            }
        }
        return Optional.ofNullable(candidate);
    }

    /** Thống kê tổng hợp */
    /**
     * Tổng doanh thu sau chiết khấu.
     *
     * @return tổng tiền thực thu.
     */
    public double totalRevenue() {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getNetTotal();
        }
        return total;
    }

    /**
     * Giá trị trung bình mỗi hóa đơn.
     *
     * @return doanh thu trung bình.
     */
    public double averageInvoiceValue() {
        if (invoices.isEmpty()) {
            return 0;
        }
        double sum = 0;
        int count = 0;
        for (Invoice invoice : invoices) {
            sum += invoice.getNetTotal();
            count++;
        }
        return sum / count;
    }

    /**
     * Tổng số lượng máy đã bán.
     *
     * @return tổng quantity.
     */
    public int totalQuantitySold() {
        int total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getQuantity();
        }
        return total;
    }

    /**
     * Đếm số hóa đơn.
     *
     * @return tổng hóa đơn.
     */
    public long countInvoices() {
        return invoices.size();
    }

    /**
     * Tổng số tiền chiết khấu đã áp dụng.
     *
     * @return số tiền giảm.
     */
    public double totalDiscountAmount() {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getGrossTotal() - invoice.getNetTotal();
        }
        return total;
    }

    /**
     * Đếm số hóa đơn mỗi nhân viên phụ trách.
     *
     * @return Map nhân viên -> số hóa đơn.
     */
    public Map<String, Long> countBySalesperson() {
        Map<String, Long> summary = new HashMap<>();
        for (Invoice invoice : invoices) {
            summary.merge(invoice.getSalesperson(), 1L, Long::sum);
        }
        return summary;
    }

    /**
     * Doanh thu theo nhân viên.
     *
     * @return Map nhân viên -> doanh thu.
     */
    public Map<String, Double> revenueBySalesperson() {
        Map<String, Double> summary = new HashMap<>();
        for (Invoice invoice : invoices) {
            summary.merge(invoice.getSalesperson(), invoice.getNetTotal(), Double::sum);
        }
        return summary;
    }

    /**
     * Doanh thu theo tháng (bất kể năm).
     *
     * @return Map tháng -> doanh thu.
     */
    public Map<Integer, Double> revenueByMonth() {
        Map<Integer, Double> summary = new HashMap<>();
        for (Invoice invoice : invoices) {
            int month = invoice.getSaleDate().getMonthValue();
            summary.merge(month, invoice.getNetTotal(), Double::sum);
        }
        return summary;
    }

    /**
     * Trả về danh sách hiện tại.
     *
     * @return bản sao danh sách hóa đơn.
     */
    public List<Invoice> getAll() {
        return invoices.toList();
    }

    /**
     * Thay thế toàn bộ dữ liệu.
     *
     * @param newInvoices dữ liệu mới.
     */
    public void replaceAll(List<Invoice> newInvoices) {
        invoices.clear();
        invoices.bulkAdd(newInvoices);
    }

    /**
     * Tạo bản sao và sắp xếp theo tiêu chí truyền vào.
     *
     * @param comparator tiêu chí sắp xếp.
     * @return danh sách mới đã sắp xếp.
     */
    private List<Invoice> sortCopy(Comparator<Invoice> comparator) {
        List<Invoice> copy = invoices.toList();
        copy.sort(comparator);
        return copy;
    }
}
