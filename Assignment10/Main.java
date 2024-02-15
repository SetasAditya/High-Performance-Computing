public class Main {
    public static void main(String[] args) {
        LazySkipList<Integer> skipList = new LazySkipList<>();

        // Insert operation
        skipList.add(5);
        skipList.add(10);
        skipList.add(7);

        // Contains operation
        System.out.println("Contains 5: " + skipList.contains(5)); // true
        System.out.println("Contains 10: " + skipList.contains(10)); // true
        System.out.println("Contains 7: " + skipList.contains(7)); // true
        System.out.println("Contains 15: " + skipList.contains(15)); // false

        // Delete operation
        boolean deleted = skipList.remove(10);
        System.out.println("Deleted 10: " + deleted); // true

        System.out.println("Contains 10: " + skipList.contains(10)); // false
    }
}
