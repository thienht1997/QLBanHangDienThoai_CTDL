/**
 * Hàng đợi (FIFO) đơn giản dùng node liên kết.
 */
public class SimpleQueue<T> {
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

    /** Thêm vào cuối hàng đợi. */
    public void enqueue(T data) {
        Node<T> node = new Node<>(data);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /** Lấy và bỏ phần tử đầu hàng, null nếu rỗng. */
    public T dequeue() {
        if (head == null) {
            return null;
        }
        T val = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return val;
    }

    /** Xem phần tử đầu hàng, không bỏ. */
    public T peek() {
        return head == null ? null : head.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
