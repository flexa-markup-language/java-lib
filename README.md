# FML Lib

FML is a data serialization format. This lib provides a handler for Java.


## Guides and Documentation

The documentation of FML and this handler can be found [here](https://bps-lib.github.io/). It contains all guides and detailed documentation.


## BPS Handler Operations

### BPS Class

#### Parsing operations

The FML class has three functions to transform data. The method `FML.parse()` will parse a string containing data in FML notation. The methods `FML.stringify()` and `FML.beautify()` will parse a `Map<String, Object>` in a string containing the data in FML notation.

```java
package main;

import java.util.Map;
import fml.FML;

public class Main {

    public static void main(String[] args) {
        String fmlNotationData = "name:\"John Doe\";age:30;isStudent:true;grades:{9.5,8.0,7.5};address:street:\"7th street\";number:1200;;";

        // Parsing a String in a Map<String, Object> and printing in the console
        Map<String, Object> parsedData = FML.parse(fmlNotationData);
        System.out.println("Parsed Data: " + parsedData);

        // Parsing a Map<string, Object> in a String and printing in the console
        String stringifiedText = FML.stringify(parsedData);
        System.out.println("Stringifier Data: " + stringifiedText);
    }
    
}
```
