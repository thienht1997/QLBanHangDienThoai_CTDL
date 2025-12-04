import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Đọc/ghi dữ liệu CSV cho điện thoại và hóa đơn.
 */
public class FileService {

    /**
     * Đọc danh sách điện thoại từ file.
     *
     * @param path đường dẫn file CSV.
     * @return danh sách điện thoại, rỗng nếu file chưa tồn tại.
     */
    public List<Phone> readPhones(Path path) throws IOException {
        if (Files.notExists(path)) {
            return Collections.emptyList();
        }
        return Files.readAllLines(path).stream()
                .filter(line -> !line.isBlank())
                .map(Phone::fromCsv)
                .collect(Collectors.toList());
    }

    /**
     * Ghi danh sách điện thoại xuống file CSV.
     *
     * @param path   file đích.
     * @param phones dữ liệu cần lưu.
     */
    public void writePhones(Path path, List<Phone> phones) throws IOException {
        ensureParent(path);
        Files.write(path, phones.stream()
                .map(Phone::toCsv)
                .collect(Collectors.toList()));
    }

    /**
     * Đọc danh sách hóa đơn.
     *
     * @param path file nguồn.
     * @return danh sách hóa đơn (có thể rỗng).
     */
    public List<Invoice> readInvoices(Path path) throws IOException {
        if (Files.notExists(path)) {
            return Collections.emptyList();
        }
        return Files.readAllLines(path).stream()
                .filter(line -> !line.isBlank())
                .map(Invoice::fromCsv)
                .collect(Collectors.toList());
    }

    /**
     * Ghi danh sách hóa đơn.
     *
     * @param path     file đích.
     * @param invoices dữ liệu cần lưu.
     */
    public void writeInvoices(Path path, List<Invoice> invoices) throws IOException {
        ensureParent(path);
        Files.write(path, invoices.stream()
                .map(Invoice::toCsv)
                .collect(Collectors.toList()));
    }

    /**
     * Đảm bảo thư mục đích tồn tại trước khi ghi.
     *
     * @param path file chuẩn bị ghi.
     */
    private void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
