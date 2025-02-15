/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml;

import java.util.Locale;
import java.util.Map;

import fml.core.Parser;
import fml.core.Stringifier;

public final class FML {

    static {
        Locale.setDefault(Locale.US);
    }

    /**
     * Parse a string FML data and return a Map.
     *
     * @param data FML data in string format.
     * @return Map representation of the FML data.
     */
    public static Map<String, Object> parse(String data) {
        return new Parser(data).parse();
    }

    /**
     * Convert FML structured data to plain text.
     *
     * @param data FML structured data to convert.
     * @return A String representation of the data.
     */
    public static String stringify(Map<String, Object> data) {
        return new Stringifier(data).stringify();
    }

    /**
     * Convert FML structured data to indented plain text.
     *
     * @param data FML structured data to convert.
     * @return A String representation of the data.
     */
    public static String beautify(Map<String, Object> data) {
        return new Stringifier(data, true, 0).stringify();
    }

    // avoid instantiation
    private FML() {
        throw new UnsupportedOperationException("Utility class");
    }
}