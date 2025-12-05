import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Tổng hợp các báo cáo/ thống kê nâng cao dựa trên dữ liệu điện thoại và hóa đơn.
 */
public class AnalyticsService {
    private final PhoneManager phoneManager;
    private final InvoiceManager invoiceManager;

    public AnalyticsService(PhoneManager phoneManager, InvoiceManager invoiceManager) {
        this.phoneManager = phoneManager;
        this.invoiceManager = invoiceManager;
    }

    /**
     * Doanh thu theo thương hiệu điện thoại.
     *
     * @return Map thương hiệu -> doanh thu.
     */
    public Map<String, Double> revenueByBrand() {
        Map<String, Phone> phoneIndex = buildPhoneIndex();
        Map<String, Double> result = new HashMap<>();
        for (Invoice invoice : invoiceManager.getAll()) {
            Phone phone = phoneIndex.getOrDefault(invoice.getPhoneId(), dummyPhone());
            String brand = phone.getBrand();
            result.merge(brand, invoice.getNetTotal(), Double::sum);
        }
        return result;
    }

    /**
     * Số lượng bán ra theo thương hiệu.
     *
     * @return Map thương hiệu -> tổng số lượng.
     */
    public Map<String, Integer> quantitySoldByBrand() {
        Map<String, Phone> phoneIndex = buildPhoneIndex();
        Map<String, Integer> result = new HashMap<>();
        List<Invoice> invoices = invoiceManager.getAll();
        for (Invoice invoice : invoices) {
            String brand = phoneIndex.getOrDefault(invoice.getPhoneId(), dummyPhone()).getBrand();
            result.merge(brand, invoice.getQuantity(), Integer::sum);
        }
        return result;
    }

    /**
     * Doanh thu từng tháng của một năm.
     *
     * @param year năm cần thống kê.
     * @return Map tháng -> doanh thu trong năm đó.
     */
    public Map<Integer, Double> revenueByMonth(int year) {
        Map<Integer, Double> result = new HashMap<>();
        for (Invoice invoice : invoiceManager.getAll()) {
            if (invoice.getSaleDate().getYear() != year) {
                continue;
            }
            int month = invoice.getSaleDate().getMonthValue();
            result.merge(month, invoice.getNetTotal(), Double::sum);
        }
        return result;
    }

    /**
     * Đếm số hóa đơn đạt ngưỡng giá trị tối thiểu theo từng nhân viên.
     *
     * @param minRevenue ngưỡng doanh thu (VND).
     * @return Map nhân viên -> số hóa đơn đạt yêu cầu.
     */
    public Map<String, Long> invoicesBySalespersonWithMinRevenue(double minRevenue) {
        Map<String, Long> summary = new HashMap<>();
        for (Invoice invoice : invoiceManager.getAll()) {
            if (invoice.getNetTotal() >= minRevenue) {
                summary.merge(invoice.getSalesperson(), 1L, Long::sum);
            }
        }
        return summary;
    }

    /**
     * Đếm số mẫu máy tồn kho >= minStock theo thương hiệu.
     *
     * @param minStock số lượng tối thiểu.
     * @return Map thương hiệu -> số mẫu đạt điều kiện.
     */
    public Map<String, Long> phonesByBrandWithStockGreaterThan(int minStock) {
        Map<String, Long> summary = new HashMap<>();
        for (Phone phone : phoneManager.getAll()) {
            if (phone.getStock() >= minStock) {
                summary.merge(phone.getBrand(), 1L, Long::sum);
            }
        }
        return summary;
    }

    /**
     * Đếm số mẫu máy có giá >= price theo thương hiệu.
     *
     * @param price ngưỡng giá.
     * @return Map thương hiệu -> số mẫu đạt điều kiện.
     */
    public Map<String, Long> phonesByBrandWithPriceGreaterThan(double price) {
        Map<String, Long> summary = new HashMap<>();
        for (Phone phone : phoneManager.getAll()) {
            if (phone.getPrice() >= price) {
                summary.merge(phone.getBrand(), 1L, Long::sum);
            }
        }
        return summary;
    }

    /**
     * Tính mức chiết khấu trung bình của mỗi nhân viên bán hàng.
     *
     * @return Map nhân viên -> tỷ lệ chiết khấu trung bình.
     */
    public Map<String, Double> averageDiscountBySalesperson() {
        Map<String, double[]> aggregates = new HashMap<>();
        for (Invoice invoice : invoiceManager.getAll()) {
            double[] stats = aggregates.computeIfAbsent(invoice.getSalesperson(), key -> new double[2]);
            stats[0] += invoice.getDiscountRate();
            stats[1] += 1;
        }
        Map<String, Double> averages = new HashMap<>();
        for (Map.Entry<String, double[]> entry : aggregates.entrySet()) {
            double[] stats = entry.getValue();
            double average = stats[1] == 0 ? 0 : stats[0] / stats[1];
            averages.put(entry.getKey(), average);
        }
        return averages;
    }

    /**
     * Đếm hóa đơn có tên khách hàng chứa từ khoá.
     *
     * @param keyword từ khoá không phân biệt hoa thường.
     * @return số hóa đơn phù hợp.
     */
    public long countInvoicesByCustomerKeyword(String keyword) {
        String normalized = keyword.toLowerCase(Locale.ROOT);
        long count = 0;
        for (Invoice invoice : invoiceManager.getAll()) {
            if (invoice.getCustomerName().toLowerCase(Locale.ROOT).contains(normalized)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tạo map tra cứu nhanh phoneId -> Phone.
     *
     * @return Map mã -> đối tượng phone.
     */
    private Map<String, Phone> buildPhoneIndex() {
        Map<String, Phone> index = new HashMap<>();
        for (Phone phone : phoneManager.getAll()) {
            index.putIfAbsent(phone.getId(), phone);
        }
        return index;
    }

    /**
     * Trả về đối tượng "Unknown" phòng trường hợp dữ liệu sai.
     *
     * @return Phone giả.
     */
    private Phone dummyPhone() {
        return new Phone("UNKNOWN", "Unknown", "Khác", 0, 0, 0, LocalDate.now().getYear());
    }
}
