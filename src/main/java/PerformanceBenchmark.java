import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerformanceBenchmark {


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