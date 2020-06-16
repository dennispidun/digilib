package de.unihildesheim.digilib.book.imports;

public class DelimiterNotFoundException extends RuntimeException{
    public DelimiterNotFoundException(String delimiter, String input) {
        super("Das Trennzeichen " + delimiter + " konnte in dieser Zeile nicht gefunden werden: " + input);
    }
}
