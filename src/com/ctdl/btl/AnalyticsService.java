package com.ctdl.btl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
        return invoiceManager.getAll().stream()
                .collect(Collectors.groupingBy(
                        invoice -> phoneIndex.getOrDefault(invoice.getPhoneId(), dummyPhone()).getBrand(),
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    /**
     * Số lượng bán ra theo thương hiệu.
     *
     * @return Map thương hiệu -> tổng số lượng.
     */
    public Map<String, Integer> quantitySoldByBrand() {
        Map<String, Phone> phoneIndex = buildPhoneIndex();
        Map<String, Integer> result = new HashMap<>();
        for (Invoice invoice : invoiceManager.getAll()) {
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
        return invoiceManager.getAll().stream()
                .filter(invoice -> invoice.getSaleDate().getYear() == year)
                .collect(Collectors.groupingBy(invoice -> invoice.getSaleDate().getMonthValue(),
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    /**
     * Đếm số hóa đơn đạt ngưỡng giá trị tối thiểu theo từng nhân viên.
     *
     * @param minRevenue ngưỡng doanh thu (VND).
     * @return Map nhân viên -> số hóa đơn đạt yêu cầu.
     */
    public Map<String, Long> invoicesBySalespersonWithMinRevenue(double minRevenue) {
        return invoiceManager.getAll().stream()
                .filter(invoice -> invoice.getNetTotal() >= minRevenue)
                .collect(Collectors.groupingBy(Invoice::getSalesperson, Collectors.counting()));
    }

    /**
     * Đếm số mẫu máy tồn kho >= minStock theo thương hiệu.
     *
     * @param minStock số lượng tối thiểu.
     * @return Map thương hiệu -> số mẫu đạt điều kiện.
     */
    public Map<String, Long> phonesByBrandWithStockGreaterThan(int minStock) {
        return phoneManager.getAll().stream()
                .filter(phone -> phone.getStock() >= minStock)
                .collect(Collectors.groupingBy(Phone::getBrand, Collectors.counting()));
    }

    /**
     * Đếm số mẫu máy có giá >= price theo thương hiệu.
     *
     * @param price ngưỡng giá.
     * @return Map thương hiệu -> số mẫu đạt điều kiện.
     */
    public Map<String, Long> phonesByBrandWithPriceGreaterThan(double price) {
        return phoneManager.getAll().stream()
                .filter(phone -> phone.getPrice() >= price)
                .collect(Collectors.groupingBy(Phone::getBrand, Collectors.counting()));
    }

    /**
     * Tính mức chiết khấu trung bình của mỗi nhân viên bán hàng.
     *
     * @return Map nhân viên -> tỷ lệ chiết khấu trung bình.
     */
    public Map<String, Double> averageDiscountBySalesperson() {
        return invoiceManager.getAll().stream()
                .collect(Collectors.groupingBy(Invoice::getSalesperson,
                        Collectors.averagingDouble(Invoice::getDiscountRate)));
    }

    /**
     * Đếm hóa đơn có tên khách hàng chứa từ khoá.
     *
     * @param keyword từ khoá không phân biệt hoa thường.
     * @return số hóa đơn phù hợp.
     */
    public long countInvoicesByCustomerKeyword(String keyword) {
        String normalized = keyword.toLowerCase(Locale.ROOT);
        return invoiceManager.getAll().stream()
                .filter(invoice -> invoice.getCustomerName().toLowerCase(Locale.ROOT).contains(normalized))
                .count();
    }

    /**
     * Tạo map tra cứu nhanh phoneId -> Phone.
     *
     * @return Map mã -> đối tượng phone.
     */
    private Map<String, Phone> buildPhoneIndex() {
        return phoneManager.getAll().stream()
                .collect(Collectors.toMap(Phone::getId, phone -> phone, (a, b) -> a));
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
