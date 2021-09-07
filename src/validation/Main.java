package validation;

public class Main {

    /**
     * for command line jar use
     * @param files list of file names to validate jsonValidator.machines.json syntax of
     */
    public static void main(String... files) {
        boolean validation = FileValidation.runValidation("{\"hello\": 1, \"world\": [1, 2, 3, 4]}", "json");

        System.out.println(validation ? "yeet" : "no");
    }
}
