public class StandardBST {
    Node root;
    public void insert(int key, TransactionRecord record) {
        if (root == null) {
            root = new Node(key, record);
            return;
        }
        Node current = root;
        while (true) {
            if (key < current.key) {
                if (current.left == null) {
                    current.left = new Node(key, record);
                    break;
                }
                current = current.left;
            } else if (key > current.key) {
                if (current.right == null) {
                    current.right = new Node(key, record);
                    break;
                }
                current = current.right;
            } else {
                break;
            }
        }
    }

    public Node search(int key) {
        Node current = root;
        while (current != null) {
            if (current.key == key) return current;
            if (key < current.key) current = current.left;
            else current = current.right;
        }
        return null;
    }

    public void delete(int key) {
        Node parent = null;
        Node current = root;

        while (current != null && current.key != key) {
            parent = current;
            if (key < current.key) current = current.left;
            else current = current.right;
        }

        if (current == null) return;

        if (current.left == null || current.right == null) {
            Node newChild = (current.left == null) ? current.right : current.left;

            if (parent == null) {
                root = newChild;
            } else if (parent.left == current) {
                parent.left = newChild;
            } else {
                parent.right = newChild;
            }
        } else {
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.key = successor.key;
            current.record = successor.record;

            if (successorParent.left == successor) {
                successorParent.left = successor.right;
            } else {
                successorParent.right = successor.right;
            }
        }
    }
    private int minValue(Node root) {
        int minv = root.key;
        while (root.left != null) {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }
}
