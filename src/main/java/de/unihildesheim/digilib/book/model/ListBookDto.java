package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.borrowing.model.Borrowing;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ListBookDto extends BookDto {

    private Date borrowedOn;
    private String borrowerName;
    private Date returnedOn;

    public ListBookDto(Book book, Date borrowedOn) {
        this.setAuthor(book.getAuthor());
        this.setInvnr(book.getInvnr());
        this.setIsbn(book.getIsbn());
        this.setTitle(book.getTitle());
        this.setCreatedOn(book.getCreatedOn());

        if (book.getBorrowings() != null && book.getBorrowings().size() >= 1 && book.getBorrowings().get(0) != null) {
            this.setBorrowedOn(book.getBorrowings().get(0).getBorrowedOn());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ListBookDto that = (ListBookDto) o;
        return Objects.equals(this.getInvnr(), that.getInvnr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getInvnr());
    }
}

