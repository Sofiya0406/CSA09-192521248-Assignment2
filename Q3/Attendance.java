import java.util.*;

class BadStudent {
    String regNo, name;
    BadStudent(String r, String n) { regNo = r; name = n; }
    public boolean equals(Object o) {
        return o instanceof BadStudent && regNo.equals(((BadStudent) o).regNo);
    }
}

class Student {
    private final String regNo, name;
    Student(String r, String n) { regNo = r; name = n; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Student && regNo.equals(((Student) o).regNo);
    }
    @Override public int hashCode() { return regNo.hashCode(); }
}

class MutStudent {
    String regNo;
    MutStudent(String r) { regNo = r; }
    @Override public boolean equals(Object o) {
        return o instanceof MutStudent && regNo.equals(((MutStudent) o).regNo);
    }
    @Override public int hashCode() { return regNo.hashCode(); }
}

class Main {
    public static void main(String[] args) {
        Set<BadStudent> t1 = new HashSet<>();
        t1.add(new BadStudent("192311001", "Arun"));
        t1.add(new BadStudent("192311002", "Divya"));
        System.out.println("Test1 (no duplicate scans, no hashCode): " + t1.size());

        Set<BadStudent> t2 = new HashSet<>();
        t2.add(new BadStudent("192311001", "Arun"));
        t2.add(new BadStudent("192311002", "Divya"));
        t2.add(new BadStudent("192311001", "Arun"));
        System.out.println("Test2 (duplicate scan, no hashCode): " + t2.size());

        Set<Student> t3 = new HashSet<>();
        t3.add(new Student("192311001", "Arun"));
        t3.add(new Student("192311002", "Divya"));
        t3.add(new Student("192311001", "Arun"));
        System.out.println("Test3 (duplicate scan, with hashCode): " + t3.size());

        Set<MutStudent> t4 = new HashSet<>();
        MutStudent m = new MutStudent("192311001");
        t4.add(m);
        m.regNo = "192311999";
        System.out.println("Test4 (regNo changed after add): contains=" + t4.contains(m) + " size=" + t4.size());
    }
  }
