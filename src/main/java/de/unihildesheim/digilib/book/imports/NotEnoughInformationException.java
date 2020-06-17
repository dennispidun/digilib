package de.unihildesheim.digilib.book.imports;

public class NotEnoughInformationException extends RuntimeException {
    public NotEnoughInformationException(String input) {
        super("Das folgende Buch hat nicht genug Information: " + input);
    }
}
