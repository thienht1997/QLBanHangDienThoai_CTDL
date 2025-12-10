import java.util.Objects;

/**
 * Thông tin khách hàng dùng cho CRUD và thống kê.
 */
public class Customer {
    private final String id;
    private final String fullName;
    private final String phone;
    private final String email;
    private final String tier;
    private final int joinYear;
    private final double totalSpent;

    /**
     * Khởi tạo đầy đủ thông tin khách hàng.
     *
     * @param id        mã khách hàng (duy nhất).
     * @param fullName  họ tên.
     * @param phone     số điện thoại.
     * @param email     email liên hệ.
     * @param tier      hạng thành viên.
     * @param joinYear  năm tham gia.
     * @param totalSpent tổng chi tiêu tích lũy.
     */
    public Customer(String id, String fullName, String phone, String email, String tier, int joinYear, double totalSpent) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.tier = tier;
        this.joinYear = joinYear;
        this.totalSpent = totalSpent;
    }

    /** Mã khách hàng (khóa chính). */
    public String getId() {
        return id;
    }

    /** Họ tên đầy đủ. */
    public String getFullName() {
        return fullName;
    }

    /** Số điện thoại liên hệ. */
    public String getPhone() {
        return phone;
    }

    /** Địa chỉ email. */
    public String getEmail() {
        return email;
    }

    /** Hạng thành viên (Bronze/Silver/Gold/Platinum). */
    public String getTier() {
        return tier;
    }

    /** Năm tham gia lần đầu. */
    public int getJoinYear() {
        return joinYear;
    }

    /** Tổng chi tiêu tích lũy. */
    public double getTotalSpent() {
        return totalSpent;
    }

    /**
     * Xuất khách hàng thành 1 dòng CSV.
     *
     * @return chuỗi CSV 7 cột.
     */
    public String toCsv() {
        return String.join(",",
                id,
                fullName,
                phone,
                email,
                tier,
                String.valueOf(joinYear),
                String.valueOf(totalSpent));
    }

    /**
     * Parse 1 dòng CSV thành đối tượng Customer.
     *
     * @param line dòng CSV.
     * @return Customer tương ứng.
     */
    public static Customer fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid customer line: " + line);
        }
        return new Customer(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim(),
                Integer.parseInt(parts[5].trim()),
                Double.parseDouble(parts[6].trim()));
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", tier='" + tier + '\'' +
                ", joinYear=" + joinYear +
                ", totalSpent=" + totalSpent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
