package com.ctdl.btl;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PhoneManager {
    private final SinglyLinkedList<Phone> phones = new SinglyLinkedList<>();

    public void addPhone(Phone phone) {
        phones.addLast(phone);
    }

    public boolean updatePhone(String id, Phone updated) {
        return phones.update(phone -> phone.getId().equalsIgnoreCase(id), phone -> updated);
    }

    public boolean deletePhone(String id) {
        return phones.removeIf(phone -> phone.getId().equalsIgnoreCase(id));
    }

    public Optional<Phone> findById(String id) {
        return phones.findFirst(phone -> phone.getId().equalsIgnoreCase(id));
    }

    public List<Phone> findByBrand(String brand) {
        String normalized = brand.toLowerCase(Locale.ROOT);
        return phones.toList().stream()
                .filter(phone -> phone.getBrand().toLowerCase(Locale.ROOT).contains(normalized))
                .collect(Collectors.toList());
    }

    public List<Phone> findByPriceRange(double min, double max) {
        return phones.toList().stream()
                .filter(phone -> phone.getPrice() >= min && phone.getPrice() <= max)
                .collect(Collectors.toList());
    }

    public List<Phone> sortByPriceAsc() {
        return sortCopy(Comparator.comparingDouble(Phone::getPrice));
    }

    public List<Phone> sortByPriceDesc() {
        return sortCopy(Comparator.comparingDouble(Phone::getPrice).reversed());
    }

    public List<Phone> sortByStockDesc() {
        return sortCopy(Comparator.comparingInt(Phone::getStock).reversed());
    }

    public List<Phone> sortByReleaseYearDesc() {
        return sortCopy(Comparator.comparingInt(Phone::getReleaseYear).reversed());
    }

    public Optional<Phone> findMostExpensive() {
        return phones.toList().stream().max(Comparator.comparingDouble(Phone::getPrice));
    }

    public Optional<Phone> findCheapest() {
        return phones.toList().stream().min(Comparator.comparingDouble(Phone::getPrice));
    }

    public Optional<Phone> findHighestStock() {
        return phones.toList().stream().max(Comparator.comparingInt(Phone::getStock));
    }

    public Optional<Phone> findLowestStock() {
        return phones.toList().stream().min(Comparator.comparingInt(Phone::getStock));
    }

    public double totalInventoryValue() {
        return phones.toList().stream().mapToDouble(Phone::getInventoryValue).sum();
    }

    public double averagePrice() {
        return phones.isEmpty() ? 0 : phones.toList().stream().mapToDouble(Phone::getPrice).average().orElse(0);
    }

    public long countPhonesByBrand(String brand) {
        return findByBrand(brand).size();
    }

    public long countPhonesInStock() {
        return phones.toList().stream().filter(phone -> phone.getStock() > 0).count();
    }

    public Map<String, Long> countPhonesPerBrand() {
        return phones.toList().stream()
                .collect(Collectors.groupingBy(Phone::getBrand, Collectors.counting()));
    }

    public Map<Integer, Long> countByStorage() {
        return phones.toList().stream()
                .collect(Collectors.groupingBy(Phone::getStorageGb, Collectors.counting()));
    }

    public List<Phone> getAll() {
        return phones.toList();
    }

    public void replaceAll(List<Phone> newPhones) {
        phones.clear();
        phones.bulkAdd(newPhones);
    }

    private List<Phone> sortCopy(Comparator<Phone> comparator) {
        List<Phone> copy = phones.toList();
        copy.sort(comparator);
        return copy;
    }
}
