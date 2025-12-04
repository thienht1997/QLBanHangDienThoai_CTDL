import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lớp quản lý danh sách điện thoại: CRUD, tìm kiếm, sắp xếp và thống kê.
 */
public class PhoneManager {
    private final SinglyLinkedList<Phone> phones = new SinglyLinkedList<>();

    /**
     * Thêm điện thoại mới vào danh sách.
     *
     * @param phone đối tượng cần lưu.
     */
    public void addPhone(Phone phone) {
        phones.addLast(phone);
    }

    /**
     * Cập nhật thông tin theo mã.
     *
     * @param id      mã điện thoại cần sửa.
     * @param updated dữ liệu mới.
     * @return true nếu tìm được và đã sửa.
     */
    public boolean updatePhone(String id, Phone updated) {
        return phones.update(phone -> phone.getId().equalsIgnoreCase(id), phone -> updated);
    }

    /**
     * Xoá điện thoại theo mã.
     *
     * @param id mã cần xoá.
     * @return true nếu xoá thành công.
     */
    public boolean deletePhone(String id) {
        return phones.removeIf(phone -> phone.getId().equalsIgnoreCase(id));
    }

    /**
     * Tìm theo mã (không phân biệt hoa thường).
     *
     * @param id mã cần tìm.
     * @return Optional điện thoại tương ứng.
     */
    public Optional<Phone> findById(String id) {
        return phones.findFirst(phone -> phone.getId().equalsIgnoreCase(id));
    }

    /**
     * Tìm theo tên thương hiệu gần đúng.
     *
     * @param brand chuỗi thương hiệu.
     * @return danh sách kết quả.
     */
    public List<Phone> findByBrand(String brand) {
        String normalized = brand.toLowerCase(Locale.ROOT);
        return phones.toList().stream()
                .filter(phone -> phone.getBrand().toLowerCase(Locale.ROOT).contains(normalized))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo khoảng giá.
     *
     * @param min giá nhỏ nhất.
     * @param max giá lớn nhất.
     * @return danh sách phù hợp.
     */
    public List<Phone> findByPriceRange(double min, double max) {
        return phones.toList().stream()
                .filter(phone -> phone.getPrice() >= min && phone.getPrice() <= max)
                .collect(Collectors.toList());
    }

    /**
     * Sắp xếp theo giá tăng dần.
     *
     * @return danh sách mới được sắp xếp theo giá tăng.
     */
    public List<Phone> sortByPriceAsc() {
        return sortCopy(Comparator.comparingDouble(Phone::getPrice));
    }

    /**
     * Sắp xếp theo giá giảm dần.
     *
     * @return danh sách mới được sắp xếp theo giá giảm.
     */
    public List<Phone> sortByPriceDesc() {
        return sortCopy(Comparator.comparingDouble(Phone::getPrice).reversed());
    }

    /**
     * Sắp xếp theo số lượng tồn kho giảm dần.
     *
     * @return danh sách mới sắp xếp theo tồn kho giảm.
     */
    public List<Phone> sortByStockDesc() {
        return sortCopy(Comparator.comparingInt(Phone::getStock).reversed());
    }

    /**
     * Sắp xếp theo năm ra mắt giảm dần (máy mới trước).
     *
     * @return danh sách mới sắp xếp theo năm ra mắt.
     */
    public List<Phone> sortByReleaseYearDesc() {
        return sortCopy(Comparator.comparingInt(Phone::getReleaseYear).reversed());
    }

    /**
     * Tìm máy có giá cao nhất.
     *
     * @return điện thoại giá cao nhất (nếu có).
     */
    public Optional<Phone> findMostExpensive() {
        return phones.toList().stream().max(Comparator.comparingDouble(Phone::getPrice));
    }

    /**
     * @return điện thoại giá thấp nhất.
     */
    public Optional<Phone> findCheapest() {
        return phones.toList().stream().min(Comparator.comparingDouble(Phone::getPrice));
    }

    /**
     * @return điện thoại có tồn kho lớn nhất.
     */
    public Optional<Phone> findHighestStock() {
        return phones.toList().stream().max(Comparator.comparingInt(Phone::getStock));
    }

    /**
     * @return điện thoại có tồn kho thấp nhất.
     */
    public Optional<Phone> findLowestStock() {
        return phones.toList().stream().min(Comparator.comparingInt(Phone::getStock));
    }

    /**
     * Tính tổng giá trị tồn kho của toàn bộ danh mục.
     *
     * @return tổng giá trị tồn kho (VND).
     */
    public double totalInventoryValue() {
        return phones.toList().stream().mapToDouble(Phone::getInventoryValue).sum();
    }

    /**
     * Tính giá bán trung bình của các mẫu máy.
     *
     * @return giá trung bình (VND).
     */
    public double averagePrice() {
        return phones.isEmpty() ? 0 : phones.toList().stream().mapToDouble(Phone::getPrice).average().orElse(0);
    }

    /**
     * Đếm số mẫu có thương hiệu chỉ định.
     *
     * @param brand tên thương hiệu.
     * @return số lượng mẫu phù hợp.
     */
    public long countPhonesByBrand(String brand) {
        return findByBrand(brand).size();
    }

    /**
     * Đếm số mẫu còn hàng trong kho.
     *
     * @return lượng mẫu với stock > 0.
     */
    public long countPhonesInStock() {
        return phones.toList().stream().filter(phone -> phone.getStock() > 0).count();
    }

    /**
     * Đếm số mẫu theo từng thương hiệu.
     *
     * @return Map thương hiệu -> số mẫu.
     */
    public Map<String, Long> countPhonesPerBrand() {
        return phones.toList().stream()
                .collect(Collectors.groupingBy(Phone::getBrand, Collectors.counting()));
    }

    /**
     * Đếm số mẫu theo dung lượng bộ nhớ.
     *
     * @return Map dung lượng (GB) -> số mẫu.
     */
    public Map<Integer, Long> countByStorage() {
        return phones.toList().stream()
                .collect(Collectors.groupingBy(Phone::getStorageGb, Collectors.counting()));
    }

    /**
     * Lấy toàn bộ danh sách hiện tại (bản sao).
     *
     * @return danh sách mới.
     */
    public List<Phone> getAll() {
        return phones.toList();
    }

    /**
     * Thay thế dữ liệu bằng danh sách mới (dùng khi đọc file).
     *
     * @param newPhones dữ liệu mới.
     */
    public void replaceAll(List<Phone> newPhones) {
        phones.clear();
        phones.bulkAdd(newPhones);
    }

    /**
     * Tạo bản sao danh sách rồi sắp xếp theo comparator truyền vào.
     *
     * @param comparator tiêu chí sắp xếp.
     * @return danh sách mới đã sắp xếp.
     */
    private List<Phone> sortCopy(Comparator<Phone> comparator) {
        List<Phone> copy = phones.toList();
        copy.sort(comparator);
        return copy;
    }
}
