import java.util.*;

class Main {
    static String[] f = new String[10000];

    static int useString(int n) {
        String s = "";
        for (int i = 0; i < n; i++) s = s + f[i];
        return s.length();
    }
    static int useBuilder(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(f[i]);
        return sb.toString().length();
    }
    static int useBuffer(int n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) sb.append(f[i]);
        return sb.toString().length();
    }
    static int useJoin(int n) {
        return String.join("", Arrays.copyOf(f, n)).length();
    }

    static long run(int kind, int tickets, int n) {
        long sum = 0;
        for (int t = 0; t < tickets; t++) {
            switch (kind) {
                case 0: sum += useString(n); break;
                case 1: sum += useBuilder(n); break;
                case 2: sum += useBuffer(n); break;
                default: sum += useJoin(n);
            }
        }
        return sum;
    }

    static long usedMem() {
        System.gc();
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public static void main(String[] args) {
        for (int i = 0; i < f.length; i++) f[i] = "Field" + i + ";";
        String[] names = {"String (+)", "StringBuilder", "StringBuffer", "String.join"};
        int[][] work = { {1, 10}, {10000, 50}, {1, 10000} };
        String[] wname = {"A: 1 ticket x 10 fields", "B: 10000 tickets x 50 fields",
                          "C: 1 ticket x 10000 fields"};
        for (int w = 0; w < work.length; w++) {
            int tickets = work[w][0], n = work[w][1];
            System.out.println("=== " + wname[w] + " ===");
            for (int k = 0; k < 4; k++) {
                run(k, tickets, n); // warm-up
                long before = usedMem();
                long total = 0;
                for (int r = 0; r < 5; r++) {
                    long s = System.nanoTime();
                    run(k, tickets, n);
                    total += System.nanoTime() - s;
                }
                long after = usedMem();
                long objs = (k == 0) ? (long) tickets * n : (long) tickets * 2;
                System.out.printf("%-14s avg %.3f ms | est. intermediate objects ~%d | mem delta %d KB%n",
                    names[k], total / 5 / 1e6, objs, (after - before) / 1024);
            }
        }
    }
                           }
