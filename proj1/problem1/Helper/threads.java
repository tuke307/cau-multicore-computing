package Helper;
public class threads {
    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("You can assign up to " + cores + " threads on your computer.");
    }
}
