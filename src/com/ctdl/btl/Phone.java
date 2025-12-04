package com.ctdl.btl;

import java.util.Objects;

/**
 * Thông tin một mẫu điện thoại trong cửa hàng.
 */
public class Phone {
    // Sử dụng final để đảm bảo tính toàn vẹn dữ liệu sau khi khởi tạo
    private final String id;
    private final String model;
    private final String brand;
    private final int storageGb;
    private final double price;
    private final int stock;
    private final int releaseYear;

    /**
     * @param id          mã duy nhất của mẫu điện thoại.
     * @param model       tên model.
     * @param brand       thương hiệu sản xuất.
     * @param storageGb   dung lượng bộ nhớ (GB).
     * @param price       giá bán (VND).
     * @param stock       số lượng tồn kho.
     * @param releaseYear năm ra mắt.
     */
    public Phone(
            String id,
            String model,
            String brand,
            int storageGb,
            double price,
            int stock,
            int releaseYear) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.storageGb = storageGb;
        this.price = price;
        this.stock = stock;
        this.releaseYear = releaseYear;
    }

    /**
     * @return mã điện thoại.
     */
    public String getId() {
        return id;
    }

    /**
     * @return tên model.
     */
    public String getModel() {
        return model;
    }

    /**
     * @return thương hiệu.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return dung lượng bộ nhớ GB.
     */
    public int getStorageGb() {
        return storageGb;
    }

    /**
     * @return giá bán VND.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return số lượng tồn kho.
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return năm ra mắt.
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Tính tổng giá trị tồn kho của mẫu máy này.
     *
     * @return giá trị tồn kho = giá * số lượng.
     */
    public double getInventoryValue() {
        return price * stock;
    }

    /**
     * Xuất dữ liệu thành một dòng CSV để lưu file.
     *
     * @return chuỗi CSV gồm 7 cột.
     */
    public String toCsv() {
        return String.join(",",
                id,
                model,
                brand,
                String.valueOf(storageGb),
                String.valueOf(price),
                String.valueOf(stock),
                String.valueOf(releaseYear));
    }

    /**
     * Parse một dòng CSV thành đối tượng Phone.
     *
     * @param line dòng CSV cần đọc.
     * @return đối tượng Phone tương ứng.
     */
    public static Phone fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid phone line: " + line);
        }
        return new Phone(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                Integer.parseInt(parts[3].trim()),
                Double.parseDouble(parts[4].trim()),
                Integer.parseInt(parts[5].trim()),
                Integer.parseInt(parts[6].trim()));
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", storageGb=" + storageGb +
                ", price=" + price +
                ", stock=" + stock +
                ", releaseYear=" + releaseYear +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone phone)) return false;
        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
