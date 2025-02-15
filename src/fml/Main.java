package fml;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

//        String fmlData = "name:\"John Doe\";age:30;isStudent:true;grades:{9.5,8.0,7.5};";
//        String fmlData = """
//        name:"John Doe";
//        age:30;
//        isStudent:false;
//        grades:{9.5,8.0,7.5};
//        address:
//            street:"road";
//            number:666;
//        ;
//        """;
//        String fmlData = """
//            stringArr:{
//              sub_str:
//                v1:10;
//                v2:"sub";
//              ;,
//              sub_str:
//                v1:
//                    v2:"sup";
//                ;
//              ;,
//              sub_str:'g';
//              v1:20;
//              v2:"sut";
//            };
//        """;
        String fmlData = """
            # All data types of a FML file.

            # Literals
            string:"string \\"between\\" double quotes";
            char:'c';
            quote_in_char:'\\'';

            # Numerics
            int:10;
            float:0.5;

            # Booleans
            boolTrue:true;
            boolFalse:false;

            # Vector
            stringArr:{"yes", "no", "maybe"};
            chargArr:{'a', 'b', 'c'};
            intArr:{0, 1, 2, 10, -5};
            floatArr:{0.9, 1.7, -0.2, 1.06, -5.618};
            boolArr:{true, false, true};

            # Matrix
            multArr2:{
              {0, 1, 2},
              {0, 1, 2},
              {0, 1, 2}
            };

            # Multidimensional array
            multArr3:{
              {
                {0, 1, 2},
                {0, 1, 2},
                {0, 1, 2}
              },
              {
                {0, 1, 2},
                {0, 1, 2},
                {0, 1, 2}
              },
              {
                {0, 1, 2},
                {0, 1, 2},
                {0, 1, 2}
              }
            };

            # Sub-structures
            sub_str:
              v1: 10;
              v2: "sub";
            ;

            # Array of sub-structures
            stringArr:{
              sub_str:
                v1:10;
                v2:"sub";
              ;,
              sub_str:
                v1:11;
                v2:"sup";
              ;,
              sub_str:
                v1:20;
                v2:"sut";
              ;
            };
        """;

        Map<String, Object> parsedData = FML.parse(fmlData);
        System.out.println("Parsed Data: " + parsedData);

        System.out.println("Original Text: \"" + fmlData + "\"");

        String plainText = FML.stringify(parsedData);
        System.out.println("Stringifier Text: \"" + plainText + "\"");

        String beautifyText = FML.beautify(parsedData);
        System.out.println("Beautifier Text: ```\n" + beautifyText + "\n```");

    }

}
