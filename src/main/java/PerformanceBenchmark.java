public class PerformanceBenchmark {
    static class TransactionRecord {
        int id;
        public TransactionRecord(int id) { this.id = id; }
    }

    static class Node {
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

    static class StandardBST {
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
    }

    static class AVLTree {
        Node root;
        private int height(Node N) { return N == null ? 0 : N.height; }
        private int max(int a, int b) { return Math.max(a, b); }
        private int getBalance(Node N) { return N == null ? 0 : height(N.left) - height(N.right); }
        private Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            x.right = y;
            y.left = T2;
            y.height = max(height(y.left), height(y.right)) + 1;
            x.height = max(height(x.left), height(x.right)) + 1;
            return x;
        }

        private Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = max(height(x.left), height(x.right)) + 1;
            y.height = max(height(y.left), height(y.right)) + 1;
            return y;
        }

        public void insert(int key, TransactionRecord record) {
            root = insertRec(root, key, record);
        }

        private Node insertRec(Node node, int key, TransactionRecord record) {
            if (node == null) return new Node(key, record);
            if (key < node.key) node.left = insertRec(node.left, key, record);
            else if (key > node.key) node.right = insertRec(node.right, key, record);
            else return node;

            node.height = 1 + max(height(node.left), height(node.right));
            int balance = getBalance(node);

            if (balance > 1 && key < node.left.key) return rightRotate(node);
            if (balance < -1 && key > node.right.key) return leftRotate(node);
            if (balance > 1 && key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            return node;
        }
    }

    public static void main(String[] args) {
        int[] testSizes = {10000, 50000, 100000};

        for (int size : testSizes) {
            System.out.println("Testing Data Size: " + size + " records...");

            AVLTree avl = new AVLTree();
            long startAVL = System.currentTimeMillis();
            for (int i = 1; i <= size; i++) {
                avl.insert(i, new TransactionRecord(i));
            }
            long endAVL = System.currentTimeMillis();

            System.out.println("AVL Tree Insertion Time: " + (endAVL - startAVL) + " ms");


            StandardBST bst = new StandardBST();
            System.out.print("Standard BST Inserting ");
            long startBST = System.currentTimeMillis();
            for (int i = 1; i <= size; i++) {
                bst.insert(i, new TransactionRecord(i));
            }
            long endBST = System.currentTimeMillis();
            System.out.println("Time: " + (endBST - startBST) + " ms\n");
        }
        System.out.println("Benchmark Complete.");
    }
}