/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

import java.util.Objects;

public record Token(TokenCategory category, String image, int line, int column) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Token token = (Token) obj;
        return line == token.line &&
                column == token.column &&
                category == token.category &&
                Objects.equals(image, token.image);
    }

    @Override
    public String toString() {
        return "Token{" +
                "category=" + category +
                ", image='" + image + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
