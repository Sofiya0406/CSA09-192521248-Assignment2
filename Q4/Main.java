import java.util.*;

class Main {
    static String code(int i) { return String.format("MED%06d", i); }
    static long t() { return System.nanoTime(); }

    public static void main(String[] args) {
        Random rnd = new Random(1);
        String[] names = {"HashMap", "TreeMap", "LinkedHashMap"};
        long guard = 0;
        for (int n : new int[]{1000, 10000, 100000}) {
            List<String> codes = new ArrayList<>();
            for (int i = 0; i < n; i++) codes.add(code(i));
            Collections.shuffle(codes, rnd);              // insertion order != code order
            List<String> sortedCodes = new ArrayList<>(codes);
            Collections.sort(sortedCodes);
            String[] look = new String[100000];
            for (int i = 0; i < look.length; i++) look[i] = codes.get(rnd.nextInt(n));

            System.out.println("=== n = " + n + " medicines, 100000 lookups ===");
            Map<String, Integer> hashMapCopy = null;
            for (int m = 0; m < 3; m++) {
                Map<String, Integer> map;
                if (m == 0) map = new HashMap<>();
                else if (m == 1) map = new TreeMap<>();
                else map = new LinkedHashMap<>();
                long s = t();
                for (int i = 0; i < n; i++) map.put(codes.get(i), i);
                double put = (t() - s) / 1e6;
                double get = 0;
                for (int r = 0; r < 3; r++) {
                    s = t();
                    for (String c : look) guard += map.get(c);
                    get += (t() - s) / 1e6 / 3;
                }
                s = t();
                List<String> keys = new ArrayList<>(map.keySet());
                double iter = (t() - s) / 1e6;
                System.out.printf("%-14s put %.2f ms | 100k lookups %.2f ms | report %.2f ms | code order: %b | insertion order: %b%n",
                    names[m], put, get, iter, keys.equals(sortedCodes), keys.equals(codes));
                if (m == 0) hashMapCopy = map;
            }

            // extra work when the "wrong" map is used for the code-order report
            long s = t();
            List<String> k2 = new ArrayList<>(hashMapCopy.keySet());
            Collections.sort(k2);
            System.out.printf("HashMap + sort keys for code-order report: %.2f ms%n", (t() - s) / 1e6);

            // alternative: sorted array + binary search
            String[] arr = sortedCodes.toArray(new String[0]);
            s = t();
            for (String c : look) guard += Arrays.binarySearch(arr, c);
            System.out.printf("Sorted array + binarySearch 100k lookups: %.2f ms%n", (t() - s) / 1e6);
        }
        if (guard == 42) System.out.println();
    }
                  }
