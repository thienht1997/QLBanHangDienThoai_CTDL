import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Lớp chịu trách nhiệm hiển thị menu console và điều hướng đến các chức năng.
 */
public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final PhoneManager phoneManager = new PhoneManager();
    private final InvoiceManager invoiceManager = new InvoiceManager();
    private final AnalyticsService analyticsService = new AnalyticsService(phoneManager, invoiceManager);
    private final FileService fileService = new FileService();
    private final Path phoneFile = Path.of("data", "phones.csv");
    private final Path invoiceFile = Path.of("data", "invoices.csv");

    /**
     * Hàm khởi động chính: đọc dữ liệu, seed nếu trống và mở menu.
     */
    public void run() {
        loadFromFiles();
        if (phoneManager.getAll().isEmpty()) {
            seedPhones();
        }
        if (invoiceManager.getAll().isEmpty()) {
            seedInvoices();
        }
        mainMenuLoop();
    }

    /**
     * Đọc dữ liệu từ file CSV (nếu có).
     */
    private void loadFromFiles() {
        try {
            phoneManager.replaceAll(fileService.readPhones(phoneFile));
            invoiceManager.replaceAll(fileService.readInvoices(invoiceFile));
        } catch (IOException e) {
            System.out.println("Khong the doc file du lieu: " + e.getMessage());
        }
    }

    /**
     * Ghi dữ liệu hiện tại xuống file CSV.
     */
    private void saveToFiles() {
        try {
            fileService.writePhones(phoneFile, phoneManager.getAll());
            fileService.writeInvoices(invoiceFile, invoiceManager.getAll());
            System.out.println("Da luu du lieu vao thu muc data.");
        } catch (IOException e) {
            System.out.println("Khong the ghi file: " + e.getMessage());
        }
    }

    /**
     * Vòng lặp menu chính.
     */
    private void mainMenuLoop() {
        while (true) {
            System.out.println("\n===== QUAN LY BAN DIEN THOAI =====");
            System.out.println("1. Quan ly Dien Thoai");
            System.out.println("2. Quan ly Hoa Don");
            System.out.println("3. Bao cao & Thong ke");
            System.out.println("4. Doc du lieu tu file");
            System.out.println("5. Ghi du lieu ra file");
            System.out.println("6. Chay demo tu dong cac chuc nang");
            System.out.println("0. Thoat");
            int choice = readInt("Chon: ");
            switch (choice) {
                case 1 -> phoneMenu();
                case 2 -> invoiceMenu();
                case 3 -> reportMenu();
                case 4 -> {
                    loadFromFiles();
                    System.out.println("Da doc lai du lieu tu file.");
                }
                case 5 -> saveToFiles();
                case 6 -> autoDemo();
                case 0 -> {
                    System.out.println("Tam biet!");
                    return;
                }
                default -> System.out.println("Lua chon khong hop le.");
            }
        }
    }

    /**
     * Menu con dành cho quản lý điện thoại.
     * Hiển thị các lựa chọn CRUD, tìm kiếm, sắp xếp và thống kê điện thoại.
     * Kết thúc khi người dùng chọn 0.
     */
    private void phoneMenu() {
        while (true) {
            System.out.println("\n--- Quan ly Dien Thoai ---");
            System.out.println("1. In danh sach");
            System.out.println("2. Them moi");
            System.out.println("3. Cap nhat");
            System.out.println("4. Xoa");
            System.out.println("5. Tim theo thuong hieu");
            System.out.println("6. Tim theo khoang gia");
            System.out.println("7. Sap xep theo gia tang");
            System.out.println("8. Sap xep theo gia giam");
            System.out.println("9. Sap xep theo so luong ton giam dan");
            System.out.println("10. Sap xep theo nam ra mat moi nhat");
            System.out.println("11. Tim lon/ nho nhat");
            System.out.println("12. Tong/Trung binh/ Dem");
            System.out.println("0. Quay lai");
            int choice = readInt("Chon: ");
            switch (choice) {
                case 1 -> printPhones(phoneManager.getAll());
                case 2 -> phoneManager.addPhone(inputPhone());
                case 3 -> updatePhone();
                case 4 -> deletePhone();
                case 5 -> {
                    String brand = readLine("Nhap thuong hieu: ");
                    printPhones(phoneManager.findByBrand(brand));
                }
                case 6 -> {
                    double min = readDouble("Gia min: ");
                    double max = readDouble("Gia max: ");
                    printPhones(phoneManager.findByPriceRange(min, max));
                }
                case 7 -> printPhones(phoneManager.sortByPriceAsc());
                case 8 -> printPhones(phoneManager.sortByPriceDesc());
                case 9 -> printPhones(phoneManager.sortByStockDesc());
                case 10 -> printPhones(phoneManager.sortByReleaseYearDesc());
                case 11 -> showPhoneExtremes();
                case 12 -> showPhoneAggregations();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Lua chon khong hop le.");
            }
        }
    }

    /**
     * Menu con dành cho quản lý hóa đơn.
     * Bao gồm toàn bộ thao tác CRUD, tìm kiếm nhiều tiêu chí và thống kê cơ bản.
     * Kết thúc khi người dùng chọn 0.
     */
    private void invoiceMenu() {
        while (true) {
            System.out.println("\n--- Quan ly Hoa Don ---");
            System.out.println("1. In danh sach");
            System.out.println("2. Them moi");
            System.out.println("3. Cap nhat");
            System.out.println("4. Xoa");
            System.out.println("5. Tim theo khach hang");
            System.out.println("6. Tim theo khoang ngay");
            System.out.println("7. Tim theo ma dien thoai");
            System.out.println("8. Sap xep ngay tang");
            System.out.println("9. Sap xep ngay giam");
            System.out.println("10. Sap xep theo gia tri giam dan");
            System.out.println("11. Sap xep theo so luong giam dan");
            System.out.println("12. Tim lon/ nho nhat");
            System.out.println("13. Tong/Trung binh/ Dem");
            System.out.println("0. Quay lai");
            int choice = readInt("Chon: ");
            switch (choice) {
                case 1 -> printInvoices(invoiceManager.getAll());
                case 2 -> invoiceManager.addInvoice(inputInvoice());
                case 3 -> updateInvoice();
                case 4 -> deleteInvoice();
                case 5 -> {
                    String name = readLine("Nhap ten khach hang: ");
                    printInvoices(invoiceManager.findByCustomerName(name));
                }
                case 6 -> {
                    LocalDate start = readDate("Ngay bat dau (yyyy-MM-dd): ");
                    LocalDate end = readDate("Ngay ket thuc (yyyy-MM-dd): ");
                    printInvoices(invoiceManager.findByDateRange(start, end));
                }
                case 7 -> {
                    String phoneId = readLine("Nhap ma dien thoai: ");
                    printInvoices(invoiceManager.findByPhoneId(phoneId));
                }
                case 8 -> printInvoices(invoiceManager.sortByDateAsc());
                case 9 -> printInvoices(invoiceManager.sortByDateDesc());
                case 10 -> printInvoices(invoiceManager.sortByNetTotalDesc());
                case 11 -> printInvoices(invoiceManager.sortByQuantityDesc());
                case 12 -> showInvoiceExtremes();
                case 13 -> showInvoiceAggregations();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Lua chon khong hop le.");
            }
        }
    }

    /**
     * Menu báo cáo thống kê nâng cao.
     * Cho phép lọc/nhóm các chỉ số doanh thu theo từng điều kiện.
     */
    private void reportMenu() {
        while (true) {
            System.out.println("\n--- Bao cao & Thong ke ---");
            System.out.println("1. Doanh thu theo thuong hieu");
            System.out.println("2. So luong ban theo thuong hieu");
            System.out.println("3. Doanh thu theo thang (nhap nam)");
            System.out.println("4. Hoa don theo nhan vien (loc theo doanh thu toi thieu)");
            System.out.println("5. Dien thoai ton kho >= N theo thuong hieu");
            System.out.println("6. Dien thoai cao cap (gia >= X) theo thuong hieu");
            System.out.println("7. Giam gia trung binh theo nhan vien");
            System.out.println("8. Dem hoa don theo tu khoa ten khach hang");
            System.out.println("0. Quay lai");
            int choice = readInt("Chon: ");
            switch (choice) {
                case 1 -> printDoubleMap("Doanh thu", analyticsService.revenueByBrand());
                case 2 -> printIntMap("So luong", analyticsService.quantitySoldByBrand());
                case 3 -> {
                    int year = readInt("Nhap nam: ");
                    printDoubleMap("Doanh thu theo thang " + year, analyticsService.revenueByMonth(year));
                }
                case 4 -> {
                    double min = readDouble("Gia tri don hang toi thieu: ");
                    printLongMap("So hoa don", analyticsService.invoicesBySalespersonWithMinRevenue(min));
                }
                case 5 -> {
                    int min = readInt("Ton kho toi thieu: ");
                    printLongMap("So dien thoai", analyticsService.phonesByBrandWithStockGreaterThan(min));
                }
                case 6 -> {
                    double minPrice = readDouble("Gia toi thieu: ");
                    printLongMap("So dien thoai cao cap", analyticsService.phonesByBrandWithPriceGreaterThan(minPrice));
                }
                case 7 -> printDoubleMap("Giam gia trung binh (%)", analyticsService.averageDiscountBySalesperson());
                case 8 -> {
                    String keyword = readLine("Nhap tu khoa: ");
                    long count = analyticsService.countInvoicesByCustomerKeyword(keyword);
                    System.out.println("So hoa don chua '" + keyword + "': " + count);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Lua chon khong hop le.");
            }
        }
    }

    /**
     * Tự động chạy qua tất cả chức năng để trình diễn bài toán.
     * Dùng để minh chứng yêu cầu đề tài mà không cần thao tác thủ công.
     */
    private void autoDemo() {
        System.out.println("\n--- Bat dau demo tu dong ---");
        System.out.println("1) In danh sach dien thoai va hoa don");
        printPhones(phoneManager.getAll());
        printInvoices(invoiceManager.getAll());

        System.out.println("2) Tim kiem theo thuong hieu va khoang gia");
        printPhones(phoneManager.findByBrand("Apple"));
        printPhones(phoneManager.findByPriceRange(10000000, 20000000));

        System.out.println("3) Sap xep dien thoai va hoa don");
        printPhones(phoneManager.sortByPriceAsc());
        printPhones(phoneManager.sortByPriceDesc());
        printPhones(phoneManager.sortByStockDesc());
        printPhones(phoneManager.sortByReleaseYearDesc());
        printInvoices(invoiceManager.sortByDateAsc());
        printInvoices(invoiceManager.sortByDateDesc());
        printInvoices(invoiceManager.sortByNetTotalDesc());
        printInvoices(invoiceManager.sortByQuantityDesc());

        System.out.println("4) Tim gia tri lon/ nho nhat");
        showPhoneExtremes();
        showInvoiceExtremes();

        System.out.println("5) Thong ke tong/ trung binh/ dem");
        showPhoneAggregations();
        showInvoiceAggregations();

        System.out.println("6) Bao cao dieu kien");
        printDoubleMap("Doanh thu theo thuong hieu", analyticsService.revenueByBrand());
        printIntMap("So luong ban theo thuong hieu", analyticsService.quantitySoldByBrand());
        printDoubleMap("Doanh thu theo thang nam 2024", analyticsService.revenueByMonth(2024));
        printLongMap("Hoa don >= 20 trieu theo nhan vien",
                analyticsService.invoicesBySalespersonWithMinRevenue(20_000_000));
        printLongMap("Dien thoai ton kho >= 10", analyticsService.phonesByBrandWithStockGreaterThan(10));
    }

    /**
     * In ra các điện thoại có giá/ tồn kho lớn nhất - nhỏ nhất.
     * Không trả về giá trị, chỉ hiển thị kết quả ra console.
     */
    private void showPhoneExtremes() {
        Optional<Phone> maxPrice = phoneManager.findMostExpensive();
        Optional<Phone> minPrice = phoneManager.findCheapest();
        Optional<Phone> maxStock = phoneManager.findHighestStock();
        Optional<Phone> minStock = phoneManager.findLowestStock();
        maxPrice.ifPresent(phone -> System.out.println("Gia cao nhat: " + phone));
        minPrice.ifPresent(phone -> System.out.println("Gia thap nhat: " + phone));
        maxStock.ifPresent(phone -> System.out.println("Ton kho nhieu nhat: " + phone));
        minStock.ifPresent(phone -> System.out.println("Ton kho it nhat: " + phone));
    }

    /**
     * In ra hóa đơn có giá trị hoặc số lượng lớn/nhỏ nhất.
     * Không trả về giá trị, dùng cho mục minh hoạ trong menu.
     */
    private void showInvoiceExtremes() {
        invoiceManager.findLargestOrder().ifPresent(invoice -> System.out.println("Don gia tri lon nhat: " + invoice));
        invoiceManager.findSmallestOrder().ifPresent(invoice -> System.out.println("Don gia tri nho nhat: " + invoice));
        invoiceManager.findHighestQuantity().ifPresent(invoice -> System.out.println("So luong lon nhat: " + invoice));
        invoiceManager.findLowestQuantity().ifPresent(invoice -> System.out.println("So luong nho nhat: " + invoice));
    }

    /**
     * Các thống kê tổng hợp đối với điện thoại.
     * Bao gồm tổng giá trị tồn, giá trung bình và phân bổ theo thương hiệu.
     */
    private void showPhoneAggregations() {
        System.out.printf("Tong gia tri ton kho: %.0f%n", phoneManager.totalInventoryValue());
        System.out.printf("Gia trung binh: %.0f%n", phoneManager.averagePrice());
        System.out.println("So dien thoai dang co hang: " + phoneManager.countPhonesInStock());
        System.out.println("So dien thoai Apple: " + phoneManager.countPhonesByBrand("Apple"));
        System.out.println("Thong ke theo thuong hieu: " + phoneManager.countPhonesPerBrand());
    }

    /**
     * Các thống kê tổng hợp đối với hóa đơn.
     * Bao gồm tổng doanh thu, trung bình, số lượng bán và tổng chiết khấu.
     */
    private void showInvoiceAggregations() {
        System.out.printf("Tong doanh thu: %.0f%n", invoiceManager.totalRevenue());
        System.out.printf("Gia tri trung binh: %.0f%n", invoiceManager.averageInvoiceValue());
        System.out.println("Tong so luong ban: " + invoiceManager.totalQuantitySold());
        System.out.println("Tong so hoa don: " + invoiceManager.countInvoices());
        System.out.printf("Tong tien giam gia: %.0f%n", invoiceManager.totalDiscountAmount());
    }

    /**
     * Thu thập thông tin điện thoại từ người dùng.
     *
     * @return đối tượng Phone mới được nhập.
     */
    private Phone inputPhone() {
        String id = readLine("Ma: ");
        String model = readLine("Model: ");
        String brand = readLine("Thuong hieu: ");
        int storage = readInt("Bo nho (GB): ");
        double price = readDouble("Gia: ");
        int stock = readInt("So luong ton: ");
        int year = readInt("Nam ra mat: ");
        return new Phone(id, model, brand, storage, price, stock, year);
    }

    /**
     * Cập nhật thông tin một điện thoại.
     * Đầu vào lấy trực tiếp từ người dùng (mã cần cập nhật và dữ liệu mới).
     * Nếu mã mới khác mã cũ sẽ bị từ chối để tránh thay đổi khóa chính.
     */
    private void updatePhone() {
        String id = readLine("Nhap ma can cap nhat: ");
        Optional<Phone> existing = phoneManager.findById(id);
        if (existing.isEmpty()) {
            System.out.println("Khong tim thay.");
            return;
        }
        System.out.println("Thong tin hien tai: " + existing.get());
        Phone updated = inputPhone();
        if (!updated.getId().equalsIgnoreCase(id)) {
            System.out.println("Ma moi phai trung ma cu.");
            return;
        }
        phoneManager.updatePhone(id, updated);
        System.out.println("Da cap nhat.");
    }

    /**
     * Xoá điện thoại theo mã.
     * Xác nhận trạng thái xoá cho người dùng.
     */
    private void deletePhone() {
        String id = readLine("Nhap ma can xoa: ");
        if (phoneManager.deletePhone(id)) {
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay.");
        }
    }

    /**
     * Nhập thông tin hóa đơn từ người dùng.
     *
     * @return hóa đơn mới.
     */
    private Invoice inputInvoice() {
        String id = readLine("Ma hoa don: ");
        String customer = readLine("Khach hang: ");
        String phoneNumber = readLine("SDT khach hang: ");
        String phoneId = readLine("Ma dien thoai: ");
        int quantity = readInt("So luong: ");
        double unitPrice = readDouble("Don gia: ");
        double discount = readDouble("Chiet khau (0-1): ");
        LocalDate date = readDate("Ngay ban (yyyy-MM-dd): ");
        String salesperson = readLine("Nhan vien ban hang: ");
        return new Invoice(id, customer, phoneNumber, phoneId, quantity, unitPrice, discount, date, salesperson);
    }

    /**
     * Cập nhật một hóa đơn.
     * Quy trình tương tự cập nhật điện thoại nhưng áp dụng cho Invoice.
     */
    private void updateInvoice() {
        String id = readLine("Nhap ma hoa don: ");
        Optional<Invoice> existing = invoiceManager.findById(id);
        if (existing.isEmpty()) {
            System.out.println("Khong tim thay.");
            return;
        }
        System.out.println("Thong tin: " + existing.get());
        Invoice updated = inputInvoice();
        if (!updated.getId().equalsIgnoreCase(id)) {
            System.out.println("Ma moi phai trung ma cu.");
            return;
        }
        invoiceManager.updateInvoice(id, updated);
        System.out.println("Da cap nhat.");
    }

    /**
     * Xoá hóa đơn theo mã.
     * Thông báo kết quả sau khi thao tác.
     */
    private void deleteInvoice() {
        String id = readLine("Nhap ma hoa don: ");
        if (invoiceManager.deleteInvoice(id)) {
            System.out.println("Da xoa.");
        } else {
            System.out.println("Khong tim thay.");
        }
    }

    /**
     * In bảng danh sách điện thoại ra màn hình.
     *
     * @param phones danh sách cần in.
     */
    private void printPhones(List<Phone> phones) {
        if (phones.isEmpty()) {
            System.out.println("(Khong co du lieu)");
            return;
        }
        System.out.println("Ma | Model | Thuong hieu | Gia | Ton | Nam");
        for (Phone phone : phones) {
            System.out.printf("%s | %s | %s | %.0f | %d | %d%n",
                    phone.getId(), phone.getModel(), phone.getBrand(),
                    phone.getPrice(), phone.getStock(), phone.getReleaseYear());
        }
    }

    /**
     * In bảng danh sách hóa đơn ra màn hình.
     *
     * @param invoices danh sách cần in.
     */
    private void printInvoices(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            System.out.println("(Khong co du lieu)");
            return;
        }
        System.out.println("Ma | Khach | Dien thoai | SL | Don gia | Giam | Ngay | NV");
        for (Invoice invoice : invoices) {
            System.out.printf("%s | %s | %s | %d | %.0f | %.0f | %s | %s%n",
                    invoice.getId(), invoice.getCustomerName(), invoice.getPhoneId(),
                    invoice.getQuantity(), invoice.getUnitPrice(), invoice.getDiscountRate(),
                    invoice.getSaleDate(), invoice.getSalesperson());
        }
    }

    /**
     * In các thống kê dạng Map với giá trị double.
     *
     * @param title tiêu đề hiển thị.
     * @param map   dữ liệu cần in.
     */
    private void printDoubleMap(String title, Map<?, Double> map) {
        System.out.println(title + ":");
        map.forEach((key, value) -> System.out.printf(" - %s: %.0f%n", key, value));
    }

    /**
     * In thống kê dạng Map với giá trị int.
     *
     * @param title tiêu đề hiển thị.
     * @param map   dữ liệu cần in.
     */
    private void printIntMap(String title, Map<?, Integer> map) {
        System.out.println(title + ":");
        map.forEach((key, value) -> System.out.printf(" - %s: %d%n", key, value));
    }

    /**
     * In thống kê dạng Map với giá trị long.
     *
     * @param title tiêu đề hiển thị.
     * @param map   dữ liệu cần in.
     */
    private void printLongMap(String title, Map<?, Long> map) {
        System.out.println(title + ":");
        map.forEach((key, value) -> System.out.printf(" - %s: %d%n", key, value));
    }

    /**
     * Đọc chuỗi và cắt khoảng trắng dư.
     *
     * @param prompt thông điệp hiển thị cho người dùng.
     * @return chuỗi đã nhập.
     */
    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Đọc số nguyên kèm kiểm tra lỗi.
     *
     * @param prompt thông điệp yêu cầu nhập.
     * @return số nguyên hợp lệ.
     */
    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Nhap so hop le.");
            }
        }
    }

    /**
     * Đọc số thực kèm kiểm tra lỗi.
     *
     * @param prompt thông điệp yêu cầu nhập.
     * @return số thực hợp lệ.
     */
    private double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Nhap so hop le.");
            }
        }
    }

    /**
     * Đọc ngày theo định dạng yyyy-MM-dd.
     *
     * @param prompt thông điệp yêu cầu nhập.
     * @return đối tượng LocalDate hợp lệ.
     */
    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readLine(prompt));
            } catch (Exception e) {
                System.out.println("Dinh dang ngay yyyy-MM-dd.");
            }
        }
    }

    /**
     * Sinh dữ liệu mẫu cho danh sách điện thoại.
     * Chỉ chạy khi chưa có dữ liệu nào trong hệ thống.
     */
    private void seedPhones() {
        phoneManager.addPhone(new Phone("P001", "iPhone 15 Pro", "Apple", 256, 33990000, 15, 2023));
        phoneManager.addPhone(new Phone("P002", "Galaxy S24 Ultra", "Samsung", 256, 31990000, 12, 2024));
        phoneManager.addPhone(new Phone("P003", "Xiaomi 14", "Xiaomi", 256, 18990000, 20, 2024));
        phoneManager.addPhone(new Phone("P004", "Oppo Reno 11", "Oppo", 128, 10990000, 25, 2023));
        phoneManager.addPhone(new Phone("P005", "Vivo V30", "Vivo", 256, 13990000, 18, 2024));
    }

    /**
     * Sinh dữ liệu mẫu cho danh sách hóa đơn.
     * Gắn kết với các điện thoại mẫu để tiện thử nghiệm.
     */
    private void seedInvoices() {
        invoiceManager.addInvoice(new Invoice("INV001", "Nguyen Van A", "0909000001", "P001", 1, 34990000, 0.05,
                LocalDate.of(2024, 3, 12), "Tran Thi B"));
        invoiceManager.addInvoice(new Invoice("INV002", "Tran Thi C", "0909000002", "P002", 2, 31990000, 0.02,
                LocalDate.of(2024, 3, 15), "Le Van D"));
        invoiceManager.addInvoice(new Invoice("INV003", "Le Van E", "0909000003", "P003", 3, 18990000, 0.03,
                LocalDate.of(2024, 4, 2), "Tran Thi B"));
        invoiceManager.addInvoice(new Invoice("INV004", "Pham Thi F", "0909000004", "P004", 2, 10990000, 0.01,
                LocalDate.of(2024, 4, 18), "Nguyen Van G"));
        invoiceManager.addInvoice(new Invoice("INV005", "Do Van H", "0909000005", "P005", 1, 13990000, 0.04,
                LocalDate.of(2024, 5, 5), "Le Van D"));
    }
}
