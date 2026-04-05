public class Node {
    int key;
    TransactionRecord record;
    int height;
    Node left, right;
    public Node(int key, TransactionRecord record) {
        this.key = key;
        this.record = record;
        this.height = 1;
    }
}
