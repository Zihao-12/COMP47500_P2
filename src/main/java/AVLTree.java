public class AVLTree {
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
