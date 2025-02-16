package fml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

//        String fmlData = "name:\"John Doe\";age:30;isStudent:true;grades:{9.5,8.0,7.5};";

        String fmlData = "";
        try {
            fmlData = Files.readString(Paths.get("..\\samples\\all_data_structures.fml"));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        Map<String, Object> parsedData = FML.parse(fmlData);
        System.out.println("Parsed Data: " + parsedData);

        System.out.println("Original Text: \"" + fmlData + "\"");

        String plainText = FML.stringify(parsedData);
        System.out.println("Stringifier Text: \"" + plainText + "\"");

        String beautifyText = FML.beautify(parsedData);
        System.out.println("Beautifier Text: ```\n" + beautifyText + "\n```");

    }

}
