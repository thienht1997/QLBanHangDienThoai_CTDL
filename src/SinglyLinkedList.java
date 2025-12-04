import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Danh sách liên kết đơn tổng quát dùng làm cấu trúc lưu trữ chính cho toàn bộ chương trình.
 * Chỉ cài đặt các thao tác cần thiết: thêm, xoá, cập nhật, tìm kiếm, sắp xếp và duyệt.
 */
public class SinglyLinkedList<T> implements Iterable<T> {

    /**
     * Nút lưu trữ dữ liệu và liên kết tới phần tử kế tiếp.
     */
    private static final class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Thêm phần tử vào cuối danh sách để giữ nguyên thứ tự nhập.
     *
     * @param data phần tử cần thêm.
     */
    public void addLast(T data) {
        Node<T> node = new Node<>(data);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /**
     * Thêm nhanh một tập hợp phần tử (dùng khi đọc file).
     *
     * @param items danh sách phần tử cần bổ sung.
     */
    public void bulkAdd(Collection<T> items) {
        items.forEach(this::addLast);
    }

    /**
     * Xoá mọi phần tử thoả điều kiện cho trước.
     *
     * @param predicate điều kiện xác định phần tử cần xoá.
     * @return true nếu có ít nhất một phần tử bị xoá.
     */
    public boolean removeIf(Predicate<T> predicate) {
        boolean removed = false;
        Node<T> prev = null;
        Node<T> current = head;
        while (current != null) {
            if (predicate.test(current.data)) {
                removed = true;
                size--;
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                if (current == tail) {
                    tail = prev;
                }
            } else {
                prev = current;
            }
            current = current.next;
        }
        return removed;
    }

    /**
     * Tìm phần tử đầu tiên thoả mãn điều kiện và trả về Optional.
     *
     * @param predicate điều kiện tìm kiếm.
     * @return Optional chứa phần tử phù hợp hoặc rỗng nếu không tìm thấy.
     */
    public Optional<T> findFirst(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    /**
     * Cập nhật mọi phần tử thoả điều kiện bằng hàm cập nhật truyền vào.
     *
     * @param predicate điều kiện chọn phần tử.
     * @param updater   hàm nhận phần tử cũ và trả về giá trị mới.
     * @return true nếu có ít nhất một phần tử được cập nhật.
     */
    public boolean update(Predicate<T> predicate, Function<T, T> updater) {
        Node<T> current = head;
        boolean updated = false;
        while (current != null) {
            if (predicate.test(current.data)) {
                current.data = updater.apply(current.data);
                updated = true;
            }
            current = current.next;
        }
        return updated;
    }

    /**
     * Sắp xếp danh sách bằng cách chuyển tạm sang ArrayList.
     *
     * @param comparator tiêu chí sắp xếp.
     */
    public void sort(Comparator<T> comparator) {
        List<T> temp = toList();
        temp.sort(comparator);
        clear();
        bulkAdd(temp);
    }

    /**
     * Tạo bản sao dạng ArrayList để phục vụ các thao tác thống kê/stream.
     *
     * @return danh sách mới chứa cùng phần tử.
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>(size);
        for (T item : this) {
            list.add(item);
        }
        return list;
    }

    /**
     * Xoá toàn bộ phần tử.
     */
    public void clear() {
        head = tail = null;
        size = 0;
    }

    /**
     * Lấy số lượng phần tử hiện có.
     *
     * @return số phần tử trong danh sách.
     */
    public int size() {
        return size;
    }

    /**
     * Kiểm tra danh sách rỗng.
     *
     * @return true nếu không có phần tử nào.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Cho phép dùng for-each để duyệt danh sách.
     *
     * @return iterator tuần tự qua từng phần tử.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cursor = head;

            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public T next() {
                T data = cursor.data;
                cursor = cursor.next;
                return data;
            }
        };
    }
}
