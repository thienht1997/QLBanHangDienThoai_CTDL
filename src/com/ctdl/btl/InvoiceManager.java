package com.ctdl.btl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return invoices.toList().stream()
                .filter(invoice -> invoice.getCustomerName().toLowerCase(Locale.ROOT).contains(normalized))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo khoảng ngày mua.
     *
     * @param start ngày bắt đầu.
     * @param end   ngày kết thúc.
     * @return danh sách kết quả.
     */
    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        return invoices.toList().stream()
                .filter(invoice -> !invoice.getSaleDate().isBefore(start) && !invoice.getSaleDate().isAfter(end))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo mã điện thoại.
     *
     * @param phoneId mã điện thoại.
     * @return danh sách hóa đơn bán mẫu máy đó.
     */
    public List<Invoice> findByPhoneId(String phoneId) {
        return invoices.toList().stream()
                .filter(invoice -> invoice.getPhoneId().equalsIgnoreCase(phoneId))
                .collect(Collectors.toList());
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
        return invoices.toList().stream().max(Comparator.comparingDouble(Invoice::getNetTotal));
    }

    /**
     * Tìm hóa đơn có giá trị nhỏ nhất.
     *
     * @return Optional hóa đơn giá trị thấp nhất.
     */
    public Optional<Invoice> findSmallestOrder() {
        return invoices.toList().stream().min(Comparator.comparingDouble(Invoice::getNetTotal));
    }

    /**
     * Tìm hóa đơn có số lượng bán lớn nhất.
     *
     * @return Optional kết quả tương ứng.
     */
    public Optional<Invoice> findHighestQuantity() {
        return invoices.toList().stream().max(Comparator.comparingInt(Invoice::getQuantity));
    }

    /**
     * Tìm hóa đơn có số lượng bán thấp nhất.
     *
     * @return Optional kết quả tương ứng.
     */
    public Optional<Invoice> findLowestQuantity() {
        return invoices.toList().stream().min(Comparator.comparingInt(Invoice::getQuantity));
    }

    /** Thống kê tổng hợp */
    /**
     * Tổng doanh thu sau chiết khấu.
     *
     * @return tổng tiền thực thu.
     */
    public double totalRevenue() {
        return invoices.toList().stream().mapToDouble(Invoice::getNetTotal).sum();
    }

    /**
     * Giá trị trung bình mỗi hóa đơn.
     *
     * @return doanh thu trung bình.
     */
    public double averageInvoiceValue() {
        return invoices.isEmpty() ? 0 : invoices.toList().stream().mapToDouble(Invoice::getNetTotal).average().orElse(0);
    }

    /**
     * Tổng số lượng máy đã bán.
     *
     * @return tổng quantity.
     */
    public int totalQuantitySold() {
        return invoices.toList().stream().mapToInt(Invoice::getQuantity).sum();
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
        return invoices.toList().stream()
                .mapToDouble(invoice -> invoice.getGrossTotal() - invoice.getNetTotal())
                .sum();
    }

    /**
     * Đếm số hóa đơn mỗi nhân viên phụ trách.
     *
     * @return Map nhân viên -> số hóa đơn.
     */
    public Map<String, Long> countBySalesperson() {
        return invoices.toList().stream()
                .collect(Collectors.groupingBy(Invoice::getSalesperson, Collectors.counting()));
    }

    /**
     * Doanh thu theo nhân viên.
     *
     * @return Map nhân viên -> doanh thu.
     */
    public Map<String, Double> revenueBySalesperson() {
        return invoices.toList().stream()
                .collect(Collectors.groupingBy(Invoice::getSalesperson,
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    /**
     * Doanh thu theo tháng (bất kể năm).
     *
     * @return Map tháng -> doanh thu.
     */
    public Map<Integer, Double> revenueByMonth() {
        return invoices.toList().stream()
                .collect(Collectors.groupingBy(invoice -> invoice.getSaleDate().getMonthValue(),
                        Collectors.summingDouble(Invoice::getNetTotal)));
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
