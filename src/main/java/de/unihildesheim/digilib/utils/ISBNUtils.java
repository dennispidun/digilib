package de.unihildesheim.digilib.utils;

public class ISBNUtils {

    public static void validateIsbn13(String isbn) {
        if (isbn == null) {
            throw new ISBNNotValidException(isbn);
        }

        //remove any hyphens
        isbn = isbn.replaceAll("-", "");

        //must be a 13 digit ISBN
        if (isbn.length() != 13) {
            throw new ISBNNotValidException(isbn);
        }

        try {
            int tot = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt(isbn.substring(i, i + 1));
                tot += (i % 2 == 0) ? digit : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (tot % 10);
            if (checksum == 10) {
                checksum = 0;
            }

            if (checksum != Integer.parseInt(isbn.substring(12))) {
                throw new ISBNNotValidException(isbn);
            }
        } catch (NumberFormatException nfe) {
            throw new ISBNNotValidException(isbn);
        }
    }

    public static void validateIsbn10(String isbn) {
        if (isbn == null) {
            throw new ISBNNotValidException(isbn);
        }

        //remove any hyphens
        isbn = isbn.replaceAll("-", "");

        //must be a 10 digit ISBN
        if (isbn.length() != 10) {
            throw new ISBNNotValidException(isbn);
        }

        try {
            int tot = 0;
            for (int i = 0; i < 9; i++) {
                int digit = Integer.parseInt(isbn.substring(i, i + 1));
                tot += ((10 - i) * digit);
            }

            String checksum = Integer.toString((11 - (tot % 11)) % 11);
            if ("10".equals(checksum)) {
                checksum = "X";
            }

            if (!checksum.equals(isbn.substring(9))) {
                throw new ISBNNotValidException(isbn);
            }
        } catch (NumberFormatException nfe) {
            throw new ISBNNotValidException(isbn);
        }
    }


    public static String regenerateISBN(String isbn) {
        if (isbn == null) {
            return "";
        }
        return isbn.replaceAll("-", "").strip();
    }

}
