/**
 * Ngăn xếp (LIFO) đơn giản dùng node liên kết.
 */
public class SimpleStack<T> {
    private static final class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top;
    private int size;

    /** Đưa phần tử lên đỉnh ngăn xếp. */
    public void push(T data) {
        Node<T> node = new Node<>(data);
        node.next = top;
        top = node;
        size++;
    }

    /** Lấy và bỏ phần tử đỉnh, trả về null nếu rỗng. */
    public T pop() {
        if (top == null) {
            return null;
        }
        T val = top.data;
        top = top.next;
        size--;
        return val;
    }

    /** Xem phần tử đỉnh, không bỏ. */
    public T peek() {
        return top == null ? null : top.data;
    }

    /** @return true nếu ngăn xếp rỗng. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** @return số phần tử. */
    public int size() {
        return size;
    }
}
