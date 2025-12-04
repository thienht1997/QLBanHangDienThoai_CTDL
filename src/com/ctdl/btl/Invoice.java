package com.ctdl.btl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Hóa đơn bán hàng ghi lại giao dịch giữa cửa hàng và khách.
 */
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

    /**
     * @param id            mã hóa đơn.
     * @param customerName  tên khách hàng.
     * @param customerPhone số điện thoại khách.
     * @param phoneId       mã điện thoại bán ra.
     * @param quantity      số lượng mua.
     * @param unitPrice     đơn giá tại thời điểm bán.
     * @param discountRate  tỷ lệ chiết khấu (0-1).
     * @param saleDate      ngày bán.
     * @param salesperson   nhân viên phụ trách.
     */
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

    /** @return mã hóa đơn. */
    public String getId() {
        return id;
    }

    /** @return tên khách hàng. */
    public String getCustomerName() {
        return customerName;
    }

    /** @return số điện thoại khách hàng. */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /** @return mã điện thoại mua. */
    public String getPhoneId() {
        return phoneId;
    }

    /** @return số lượng mua. */
    public int getQuantity() {
        return quantity;
    }

    /** @return đơn giá VND. */
    public double getUnitPrice() {
        return unitPrice;
    }

    /** @return tỷ lệ chiết khấu (0-1). */
    public double getDiscountRate() {
        return discountRate;
    }

    /** @return ngày bán. */
    public LocalDate getSaleDate() {
        return saleDate;
    }

    /** @return tên nhân viên phụ trách. */
    public String getSalesperson() {
        return salesperson;
    }

    /**
     * Thành tiền trước khi trừ chiết khấu.
     *
     * @return số tiền = đơn giá * số lượng.
     */
    public double getGrossTotal() {
        return unitPrice * quantity;
    }

    /**
     * Thành tiền sau khi áp dụng chiết khấu.
     *
     * @return số tiền khách phải trả.
     */
    public double getNetTotal() {
        return getGrossTotal() * (1 - discountRate);
    }

    /**
     * Xuất hóa đơn thành dòng CSV.
     *
     * @return chuỗi CSV gồm 9 trường.
     */
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

    /**
     * Đọc một dòng CSV và chuyển về đối tượng Invoice.
     *
     * @param line dòng CSV cần parse.
     * @return hóa đơn tương ứng.
     */
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
