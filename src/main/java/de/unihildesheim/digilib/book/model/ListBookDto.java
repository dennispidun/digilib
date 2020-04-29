package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.borrowing.model.Borrowing;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ListBookDto extends BookDto {

    private Date borrowedOn;
    private String borrowerName;
    private Date returnedOn;

    public ListBookDto(Book book) {
        this.setAuthor(book.getAuthor());
        this.setInvnr(book.getInvnr());
        this.setIsbn(book.getIsbn());
        this.setTitle(book.getTitle());
        this.setCreatedOn(book.getCreatedOn());

        if (book.getBorrowings() != null && book.getBorrowings().size() >= 1 && book.getBorrowings().get(0) != null) {
            this.setBorrowedOn(book.getBorrowings().get(0).getBorrowedOn());
        }

    }

}

