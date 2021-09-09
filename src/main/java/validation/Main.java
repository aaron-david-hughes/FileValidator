package validation;

import validation.exceptions.FileTypeException;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String... args) throws FileNotFoundException, FileTypeException {
        boolean validation = FileValidation.runValidation("{\"hello\": 1, \"world\": [1, 2, 3, 4]}", "json");

        System.out.println(validation ? "yeet" : "no");

        validation = FileValidation.runValidation(new File("src/main/validation/machines/json/jsons/three.json"));

        System.out.println(validation ? "yeet" : "no");

        validation = FileValidation.runValidation("     ", null);

        System.out.println(validation ? "yeet" : "no");
    }
}
