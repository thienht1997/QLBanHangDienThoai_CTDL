package com.ctdl.btl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Invoice {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String id;
    private final String customerName;
    private final String customerPhone;
    private final String phoneId;
    private final int quantity;
    private final double unitPrice;
    private final double discountRate;
    private final LocalDate saleDate;
    private final String salesperson;

    public Invoice(
            String id,
            String customerName,
            String customerPhone,
            String phoneId,
            int quantity,
            double unitPrice,
            double discountRate,
            LocalDate saleDate,
            String salesperson) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.phoneId = phoneId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountRate = discountRate;
        this.saleDate = saleDate;
        this.salesperson = salesperson;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public String getSalesperson() {
        return salesperson;
    }

    public double getGrossTotal() {
        return unitPrice * quantity;
    }

    public double getNetTotal() {
        return getGrossTotal() * (1 - discountRate);
    }

    public String toCsv() {
        return String.join(",",
                id,
                customerName,
                customerPhone,
                phoneId,
                String.valueOf(quantity),
                String.valueOf(unitPrice),
                String.valueOf(discountRate),
                saleDate.format(DATE_FORMAT),
                salesperson);
    }

    public static Invoice fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 9) {
            throw new IllegalArgumentException("Invalid invoice line: " + line);
        }
        return new Invoice(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                Integer.parseInt(parts[4].trim()),
                Double.parseDouble(parts[5].trim()),
                Double.parseDouble(parts[6].trim()),
                LocalDate.parse(parts[7].trim(), DATE_FORMAT),
                parts[8].trim());
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", phoneId='" + phoneId + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", discountRate=" + discountRate +
                ", saleDate=" + saleDate +
                ", salesperson='" + salesperson + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice invoice)) return false;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
