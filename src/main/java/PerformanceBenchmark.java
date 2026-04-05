import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


        public Node search(Node root, int key) {
            if (root == null || root.key == key) return root;
            if (root.key < key) return search(root.right, key);
            return search(root.left, key);
        }

        public void delete(int key) {
            root = deleteRec(root, key);
        }

        private Node deleteRec(Node root, int key) {
            if (root == null) return root;

            if (key < root.key) root.left = deleteRec(root.left, key);
            else if (key > root.key) root.right = deleteRec(root.right, key);
            else {
                if ((root.left == null) || (root.right == null)) {
                    Node temp = (root.left != null) ? root.left : root.right;
                    if (temp == null) { root = null; }
                    else { root = temp; }
                } else {
                    Node temp = minValueNode(root.right);
                    root.key = temp.key;
                    root.record = temp.record;
                    root.right = deleteRec(root.right, temp.key);
                }
            }

            if (root == null) return root;

            root.height = Math.max(height(root.left), height(root.right)) + 1;
            int balance = getBalance(root);
            if (balance > 1 && getBalance(root.left) >= 0) return rightRotate(root);
            if (balance > 1 && getBalance(root.left) < 0) {
                root.left = leftRotate(root.left);
                return rightRotate(root);
            }
            if (balance < -1 && getBalance(root.right) <= 0) return leftRotate(root);
            if (balance < -1 && getBalance(root.right) > 0) {
                root.right = rightRotate(root.right);
                return leftRotate(root);
            }
            return root;
        }

        private Node minValueNode(Node node) {
            Node current = node;
            while (current.left != null) current = current.left;
            return current;
        }
    }

    public static void main(String[] args) {
        int[] sizes = {10000, 50000, 100000};

        for (int n : sizes) {
            System.out.println("--- Testing Size: " + n + " ---");

            List<Integer> sequentialData = new ArrayList<>();
            for (int i = 1; i <= n; i++) sequentialData.add(i);

            List<Integer> randomData = new ArrayList<>(sequentialData);
            Collections.shuffle(randomData);

            runTest("Sequential", sequentialData, n);
            runTest("Random", randomData, n);
        }
    }

    public static void runTest(String mode, List<Integer> data, int n) {
        AVLTree avl = new AVLTree();
        StandardBST bst = new StandardBST();

        long start = System.nanoTime();
        for (int x : data) avl.insert(x, new TransactionRecord(x));
        long avlInsert = (System.nanoTime() - start) / 1000000;

        start = System.nanoTime();
        for (int x : data) bst.insert(x, new TransactionRecord(x));
        long bstInsert = (System.nanoTime() - start) / 1000000;

        start = System.nanoTime();
        for (int x : data) avl.search(avl.root, x);
        long avlSearch = (System.nanoTime() - start) / 1000000;

        start = System.nanoTime();
        for (int x : data) bst.search(x);
        long bstSearch = (System.nanoTime() - start) / 1000000;

        start = System.nanoTime();
        for (int x : data) avl.delete(x);
        long avlDelete = (System.nanoTime() - start) / 1000000;

        start = System.nanoTime();
        for (int x : data) bst.delete(x);
        long bstDelete = (System.nanoTime() - start) / 1000000;

//        System.out.printf("%-12s | %-10s | %-10s | %-10s\n", mode, "Insert(ms)", "Search(ms)", "Delete(ms)");
//        System.out.printf("AVL Tree     | %-10d | %-10d | %-10d\n", avlInsert, avlSearch, avlDelete);
//        System.out.printf("Standard BST | %-10d | %-10d | %-10d\n\n", bstInsert, bstSearch, bstDelete);
        System.out.println(mode + "," + n + "," + avlInsert + "," + bstInsert + "," + avlSearch + "," + bstSearch + "," + avlDelete + "," + bstDelete);
    }
}