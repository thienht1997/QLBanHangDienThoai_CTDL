package com.ctdl.btl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvoiceManager {
    private final SinglyLinkedList<Invoice> invoices = new SinglyLinkedList<>();

    public void addInvoice(Invoice invoice) {
        invoices.addLast(invoice);
    }

    public boolean updateInvoice(String id, Invoice updated) {
        return invoices.update(invoice -> invoice.getId().equalsIgnoreCase(id), invoice -> updated);
    }

    public boolean deleteInvoice(String id) {
        return invoices.removeIf(invoice -> invoice.getId().equalsIgnoreCase(id));
    }

    public Optional<Invoice> findById(String id) {
        return invoices.findFirst(invoice -> invoice.getId().equalsIgnoreCase(id));
    }

    public List<Invoice> findByCustomerName(String customerName) {
        String normalized = customerName.toLowerCase(Locale.ROOT);
        return invoices.toList().stream()
                .filter(invoice -> invoice.getCustomerName().toLowerCase(Locale.ROOT).contains(normalized))
                .collect(Collectors.toList());
    }

    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        return invoices.toList().stream()
                .filter(invoice -> !invoice.getSaleDate().isBefore(start) && !invoice.getSaleDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Invoice> findByPhoneId(String phoneId) {
        return invoices.toList().stream()
                .filter(invoice -> invoice.getPhoneId().equalsIgnoreCase(phoneId))
                .collect(Collectors.toList());
    }

    public List<Invoice> sortByDateAsc() {
        return sortCopy(Comparator.comparing(Invoice::getSaleDate));
    }

    public List<Invoice> sortByDateDesc() {
        return sortCopy(Comparator.comparing(Invoice::getSaleDate).reversed());
    }

    public List<Invoice> sortByNetTotalDesc() {
        return sortCopy(Comparator.comparingDouble(Invoice::getNetTotal).reversed());
    }

    public List<Invoice> sortByQuantityDesc() {
        return sortCopy(Comparator.comparingInt(Invoice::getQuantity).reversed());
    }

    public Optional<Invoice> findLargestOrder() {
        return invoices.toList().stream().max(Comparator.comparingDouble(Invoice::getNetTotal));
    }

    public Optional<Invoice> findSmallestOrder() {
        return invoices.toList().stream().min(Comparator.comparingDouble(Invoice::getNetTotal));
    }

    public Optional<Invoice> findHighestQuantity() {
        return invoices.toList().stream().max(Comparator.comparingInt(Invoice::getQuantity));
    }

    public Optional<Invoice> findLowestQuantity() {
        return invoices.toList().stream().min(Comparator.comparingInt(Invoice::getQuantity));
    }

    public double totalRevenue() {
        return invoices.toList().stream().mapToDouble(Invoice::getNetTotal).sum();
    }

    public double averageInvoiceValue() {
        return invoices.isEmpty() ? 0 : invoices.toList().stream().mapToDouble(Invoice::getNetTotal).average().orElse(0);
    }

    public int totalQuantitySold() {
        return invoices.toList().stream().mapToInt(Invoice::getQuantity).sum();
    }

    public long countInvoices() {
        return invoices.size();
    }

    public double totalDiscountAmount() {
        return invoices.toList().stream()
                .mapToDouble(invoice -> invoice.getGrossTotal() - invoice.getNetTotal())
                .sum();
    }

    public Map<String, Long> countBySalesperson() {
        return invoices.toList().stream()
                .collect(Collectors.groupingBy(Invoice::getSalesperson, Collectors.counting()));
    }

    public Map<String, Double> revenueBySalesperson() {
        return invoices.toList().stream()
                .collect(Collectors.groupingBy(Invoice::getSalesperson,
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    public Map<Integer, Double> revenueByMonth() {
        return invoices.toList().stream()
                .collect(Collectors.groupingBy(invoice -> invoice.getSaleDate().getMonthValue(),
                        Collectors.summingDouble(Invoice::getNetTotal)));
    }

    public List<Invoice> getAll() {
        return invoices.toList();
    }

    public void replaceAll(List<Invoice> newInvoices) {
        invoices.clear();
        invoices.bulkAdd(newInvoices);
    }

    private List<Invoice> sortCopy(Comparator<Invoice> comparator) {
        List<Invoice> copy = invoices.toList();
        copy.sort(comparator);
        return copy;
    }
}
