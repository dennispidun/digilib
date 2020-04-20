package de.unihildesheim.digilib.book;

public class ISBNUtils {

    public static void validateIsbn13(String isbn) {
        if (isbn == null) {
            throw new ISBNNotValidException();
        }

        //remove any hyphens
        isbn = isbn.replaceAll("-", "");

        //must be a 13 digit ISBN
        if (isbn.length() != 13) {
            throw new ISBNNotValidException();
        }

        try {
            int tot = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt(isbn.substring(i, i + 1));
                tot += (i % 2 == 0) ? digit * 1 : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (tot % 10);
            if (checksum == 10) {
                checksum = 0;
            }

            if (checksum != Integer.parseInt(isbn.substring(12))) {
                throw new ISBNNotValidException();
            }
        } catch (NumberFormatException nfe) {
            throw new ISBNNotValidException();
        }
    }

    public static String regenerateISBN(String isbn) {
        String newISBN = isbn.replaceAll("-", "");
        return newISBN;
    }

}
