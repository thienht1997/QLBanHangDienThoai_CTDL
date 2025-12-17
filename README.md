# CTDL-BTL-03-QuanLyBanDienThoai

Chương trình Java dạng console quản lý bán điện thoại di động, đáp ứng đề tài **Chủ đề 03**. Toàn bộ dữ liệu được lưu bằng **danh sách liên kết đơn** tự triển khai (`SinglyLinkedList`).

## 1. Cấu trúc dự án

```
src/
 ├─ Main.java                // Điểm vào chương trình
 ├─ ConsoleApp.java          // Xử lý menu và luồng nghiệp vụ
 ├─ Phone.java               // Đối tượng Điện thoại
 ├─ Invoice.java             // Đối tượng Hóa đơn
 ├─ PhoneManager.java        // CRUD + tìm kiếm + sắp xếp + thống kê cho Điện thoại
 ├─ InvoiceManager.java      // CRUD + tìm kiếm + sắp xếp + thống kê cho Hóa đơn
 ├─ AnalyticsService.java    // Các thống kê điều kiện nâng cao
 ├─ SinglyLinkedList.java    // Cấu trúc dữ liệu chính
 └─ FileService.java         // Đọc/ghi CSV
data/
 ├─ phones.csv
 └─ invoices.csv
```

## 2. Yêu cầu đã triển khai

- **Quản lý ít nhất 2 đối tượng**: `Phone` và `Invoice`.
- **Danh sách liên kết đơn** tự cài đặt dùng để lưu trữ toàn bộ dữ liệu.
- **Nhập/In danh sách**: menu 1 & 2.
- **Đọc/Ghi file**: menu chính mục 4 & 5 (CSV trong thư mục `data`).
- **Thêm/Sửa/Xoá** cho cả Điện thoại và Hóa đơn.
- **Tìm kiếm (≥2)**: theo thương hiệu, theo khoảng giá, theo khách hàng, theo khoảng ngày, theo mã điện thoại.
- **Sắp xếp (≥4)**: giá tăng/giảm, tồn kho giảm, năm ra mắt, ngày tăng/giảm, giá trị đơn, số lượng.
- **Tìm lớn nhất/nhỏ nhất (≥4)**: giá cao/thấp nhất, tồn kho cao/thấp nhất, đơn giá trị lớn/nhỏ nhất, số lượng lớn/nhỏ nhất.
- **Tổng/trung bình/đếm (≥5)**: giá trị tồn kho, giá trung bình, số điện thoại còn hàng, doanh thu, giá trị đơn TB, tổng số lượng, tổng hóa đơn, tổng giảm giá...
- **Thống kê theo điều kiện (≥5)**: doanh thu theo thương hiệu, số lượng theo thương hiệu, doanh thu theo tháng, lọc hóa đơn theo doanh thu tối thiểu, điện thoại tồn kho ≥ N, điện thoại cao cấp theo thương hiệu, giảm giá TB theo nhân viên, đếm hóa đơn theo từ khóa khách.
- **Chương trình chính gọi lần lượt các chức năng**: mục 6 của menu chính chạy demo tự động.
- Menu tiếng Việt thân thiện, dễ sử dụng.

## 3. Hướng dẫn chạy (Windows & macOS)

- Yêu cầu: JDK 17+ đã được thêm vào `PATH`.
- Bảo đảm thư mục `out/` tồn tại (hoặc để `javac` tự tạo).

```powershell
cd C:\path\to\QLBanHangDienThoai_CTDL
javac -d out src\*.java
java -cp out Main
```

## 4. Ghi chú

- Khi chạy lần đầu nếu không có file CSV, chương trình tự sinh dữ liệu mẫu.
- Có thể chỉnh sửa dữ liệu trong `data/*.csv` để khởi tạo nhanh.
- Tất cả số tiền đang dùng đơn vị VND, nhập theo số nguyên (vd. 19990000). Chiết khấu nhập dạng 0–1.
