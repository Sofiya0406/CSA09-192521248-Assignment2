import java.util.*;

class Team {
    final String name; final int points; final int goalDiff;
    Team(String name, int points, int goalDiff) {
        this.name = name; this.points = points; this.goalDiff = goalDiff;
    }
    @Override public String toString() { return name + "(" + points + "," + goalDiff + ")"; }
}

class Leaderboard<T> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;
    Leaderboard(Comparator<? super T> order) { this.order = order; }
    void add(T item) { items.add(item); }
    void addAll(List<? extends T> more) { items.addAll(more); }  // wildcard use
    List<T> ranking() {
        List<T> copy = new ArrayList<>(items);
        copy.sort(order);
        return copy;
    }
}

class Main {
    static final Comparator<Team> byRank =
        Comparator.comparingInt((Team t) -> t.points).reversed()
            .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
            .thenComparing(t -> t.name);

    public static void main(String[] args) {
        System.out.println("--- Given data ---");
        Leaderboard<Team> board = new Leaderboard<>(byRank);
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        System.out.println(board.ranking());

        System.out.println("--- Data with ties at every level ---");
        Leaderboard<Team> b2 = new Leaderboard<>(byRank);
        b2.addAll(Arrays.asList(
            new Team("Gamma", 9, 2), new Team("Beta", 9, 5), new Team("Echo", 6, 2),
            new Team("Alpha", 9, 5), new Team("Delta", 6, 2), new Team("Foxes", 3, 0)));
        System.out.println(b2.ranking());

        System.out.println("--- Faulty comparator 1: ignores goal difference and name ---");
        Leaderboard<Team> f1 = new Leaderboard<Team>(
            Comparator.comparingInt((Team t) -> t.points).reversed());
        f1.add(new Team("Panthers", 9, 4));
        f1.add(new Team("Eagles", 9, 6));
        f1.add(new Team("Cobras", 6, 1));
        f1.add(new Team("Bisons", 9, 4));
        System.out.println(f1.ranking());

        System.out.println("--- Faulty comparator 2: points by subtraction (overflow) ---");
        Team x = new Team("X", Integer.MAX_VALUE, 0);
        Team y = new Team("Y", -5, 0);
        Comparator<Team> bad = (a, b) -> b.points - a.points;
        Comparator<Team> good = Comparator.comparingInt((Team t) -> t.points).reversed();
        System.out.println("bad.compare(X,Y)  sign = " + Integer.signum(bad.compare(x, y))
            + "  (should be -1: X must rank above Y)");
        System.out.println("good.compare(X,Y) sign = " + Integer.signum(good.compare(x, y)));
    }
  }
