import java.util.*;

class Main {
    static long t() { return System.nanoTime(); }

    public static void main(String[] args) {
        int OPS = 1000;
        String[] ops = {"add end (n)", "insert front", "insert middle",
                        "remove front", "get k-th", "iterate all"};
        for (int n : new int[]{1000, 10000, 100000}) {
            System.out.println("=== n = " + n + " (ms, avg of 5 runs, " + OPS + " ops each) ===");
            for (int k = 0; k < 2; k++) {
                double[] r = new double[6];
                long sum = 0;
                for (int run = 0; run < 5; run++) {
                    List<Integer> l;
                    if (k == 0) l = new ArrayList<>(); else l = new LinkedList<>();
                    long s = t();
                    for (int i = 0; i < n; i++) l.add(i);
                    r[0] += t() - s;
                    s = t(); for (int i = 0; i < OPS; i++) l.add(0, i); r[1] += t() - s;
                    s = t(); for (int i = 0; i < OPS; i++) l.add(l.size() / 2, i); r[2] += t() - s;
                    s = t(); for (int i = 0; i < OPS; i++) l.remove(0); r[3] += t() - s;
                    s = t(); for (int i = 0; i < OPS; i++) sum += l.get(l.size() / 2); r[4] += t() - s;
                    s = t(); for (int x : l) sum += x; r[5] += t() - s;
                }
                System.out.println(k == 0 ? "ArrayList" : "LinkedList");
                for (int i = 0; i < 6; i++)
                    System.out.printf("   %-14s %.3f ms%n", ops[i], r[i] / 5 / 1e6);
                if (sum == 42) System.out.println();
            }
        }

        System.out.println("=== 'Up Next' queue: n addFirst + n removeFirst (ms, avg of 3) ===");
        for (int n : new int[]{1000, 10000, 50000}) {
            double[] r = new double[3];
            for (int run = 0; run < 3; run++) {
                List<Integer> al = new ArrayList<>();
                long s = t();
                for (int i = 0; i < n; i++) al.add(0, i);
                while (!al.isEmpty()) al.remove(0);
                r[0] += t() - s;

                LinkedList<Integer> ll = new LinkedList<>();
                s = t();
                for (int i = 0; i < n; i++) ll.addFirst(i);
                while (!ll.isEmpty()) ll.removeFirst();
                r[1] += t() - s;

                ArrayDeque<Integer> ad = new ArrayDeque<>();
                s = t();
                for (int i = 0; i < n; i++) ad.addFirst(i);
                while (!ad.isEmpty()) ad.pollFirst();
                r[2] += t() - s;
            }
            System.out.printf("n=%d  ArrayList %.2f | LinkedList %.2f | ArrayDeque %.2f%n",
                n, r[0] / 3 / 1e6, r[1] / 3 / 1e6, r[2] / 3 / 1e6);
        }
    }
}
