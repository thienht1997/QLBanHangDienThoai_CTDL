package com.ctdl.btl;

import java.util.Objects;

public class Phone {
    private final String id;
    private final String model;
    private final String brand;
    private final int storageGb;
    private final double price;
    private final int stock;
    private final int releaseYear;

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

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public int getStorageGb() {
        return storageGb;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getInventoryValue() {
        return price * stock;
    }

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
