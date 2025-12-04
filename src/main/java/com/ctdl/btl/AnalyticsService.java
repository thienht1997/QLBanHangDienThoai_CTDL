package com.ctdl.btl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final PhoneManager phoneManager;
    private final InvoiceManager invoiceManager;

    public AnalyticsService(PhoneManager phoneManager, InvoiceManager invoiceManager) {
        this.phoneManager = phoneManager;
        this.invoiceManager = invoiceManager;
    }

    public Map<String, Double> revenueByBrand() {
        Map<String, Phone> phoneIndex = buildPhoneIndex();
        return invoiceManager.getAll().stream()
                .collect(Collectors.groupingBy(
                        invoice -> phoneIndex.getOrDefault(invoice.getPhoneId(), dummyPhone()).getBrand(),
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    public Map<String, Integer> quantitySoldByBrand() {
        Map<String, Phone> phoneIndex = buildPhoneIndex();
        Map<String, Integer> result = new HashMap<>();
        for (Invoice invoice : invoiceManager.getAll()) {
            String brand = phoneIndex.getOrDefault(invoice.getPhoneId(), dummyPhone()).getBrand();
            result.merge(brand, invoice.getQuantity(), Integer::sum);
        }
        return result;
    }

    public Map<Integer, Double> revenueByMonth(int year) {
        return invoiceManager.getAll().stream()
                .filter(invoice -> invoice.getSaleDate().getYear() == year)
                .collect(Collectors.groupingBy(invoice -> invoice.getSaleDate().getMonthValue(),
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    public Map<String, Long> invoicesBySalespersonWithMinRevenue(double minRevenue) {
        return invoiceManager.getAll().stream()
                .filter(invoice -> invoice.getNetTotal() >= minRevenue)
                .collect(Collectors.groupingBy(Invoice::getSalesperson, Collectors.counting()));
    }

    public Map<String, Long> phonesByBrandWithStockGreaterThan(int minStock) {
        return phoneManager.getAll().stream()
                .filter(phone -> phone.getStock() >= minStock)
                .collect(Collectors.groupingBy(Phone::getBrand, Collectors.counting()));
    }

    public Map<String, Long> phonesByBrandWithPriceGreaterThan(double price) {
        return phoneManager.getAll().stream()
                .filter(phone -> phone.getPrice() >= price)
                .collect(Collectors.groupingBy(Phone::getBrand, Collectors.counting()));
    }

    public Map<String, Double> averageDiscountBySalesperson() {
        return invoiceManager.getAll().stream()
                .collect(Collectors.groupingBy(Invoice::getSalesperson,
                        Collectors.averagingDouble(Invoice::getDiscountRate)));
    }

    public long countInvoicesByCustomerKeyword(String keyword) {
        String normalized = keyword.toLowerCase(Locale.ROOT);
        return invoiceManager.getAll().stream()
                .filter(invoice -> invoice.getCustomerName().toLowerCase(Locale.ROOT).contains(normalized))
                .count();
    }

    private Map<String, Phone> buildPhoneIndex() {
        return phoneManager.getAll().stream()
                .collect(Collectors.toMap(Phone::getId, phone -> phone, (a, b) -> a));
    }

    private Phone dummyPhone() {
        return new Phone("UNKNOWN", "Unknown", "Khác", 0, 0, 0, LocalDate.now().getYear());
    }
}
