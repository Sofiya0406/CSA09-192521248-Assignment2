import java.util.*;

class Patient {
    final String name; final int severity; final long arrival;
    Patient(String name, int severity, long arrival) {
        this.name = name; this.severity = severity; this.arrival = arrival;
    }
    @Override public String toString() { return name + "(S" + severity + ")"; }
}

class Triage {
    private long counter = 0;
    private final PriorityQueue<Patient> queue = new PriorityQueue<>(
        Comparator.comparingInt((Patient p) -> p.severity)
                  .thenComparingLong(p -> p.arrival));

    void admit(String name, int severity) {
        queue.add(new Patient(name, severity, counter++));
    }
    Patient next() { return queue.poll(); }
    PriorityQueue<Patient> raw() { return queue; }

    public static void main(String[] args) {
        Triage er = new Triage();
        er.admit("Ravi", 3); er.admit("Meena", 1); er.admit("John", 3);
        er.admit("Asha", 5); er.admit("Kumar", 1); er.admit("Lata", 2);
        StringBuilder order = new StringBuilder();
        Patient p;
        while ((p = er.next()) != null) order.append(p).append(' ');
        System.out.println(order.toString().trim());
    }
}

class Main {
    public static void main(String[] args) {
        System.out.println("--- Given program ---");
        Triage.main(args);

        System.out.println("--- Hand-check with 8 patients ---");
        Triage er = new Triage();
        er.admit("Ravi", 3); er.admit("Meena", 1); er.admit("John", 3);
        er.admit("Asha", 5); er.admit("Kumar", 1); er.admit("Lata", 2);
        er.admit("Tara", 2); er.admit("Vikram", 4);
        System.out.println("Raw queue print : " + er.raw());
        StringBuilder sb = new StringBuilder();
        Patient p;
        while ((p = er.next()) != null) sb.append(p).append(' ');
        System.out.println("Treatment order : " + sb.toString().trim());

        System.out.println("--- Growth (ms, average of 5 runs) ---");
        Random rnd = new Random(1);
        for (int n : new int[]{1000, 10000, 100000}) {
            long addT = 0, pollT = 0;
            for (int run = 0; run < 5; run++) {
                Triage t = new Triage();
                long s = System.nanoTime();
                for (int i = 0; i < n; i++) t.admit("P" + i, 1 + rnd.nextInt(5));
                addT += System.nanoTime() - s;
                s = System.nanoTime();
                while (t.next() != null) { }
                pollT += System.nanoTime() - s;
            }
            System.out.printf("n=%d  add all: %.2f ms  poll all: %.2f ms%n",
                n, addT / 5 / 1e6, pollT / 5 / 1e6);
        }
    }
                 }
