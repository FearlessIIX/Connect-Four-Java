import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {

    }
    public static boolean hasDuplicates(int[] arr) {
        for (int i : arr)
            if (IntStream.of(arr).filter(num -> num == i).count() > 1) return true;
        return false;
    }
}
