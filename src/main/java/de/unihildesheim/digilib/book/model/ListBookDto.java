package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.borrowing.model.Borrowing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ListBookDto extends BookDto {

    private LocalDateTime borrowedOn;
    private String borrowerName;
    private LocalDateTime returnedOn;

    public ListBookDto(Book book, LocalDateTime borrowedOn) {
        this.setAuthor(book.getAuthor());
        this.setInvnr(book.getInvnr());
        this.setIsbn(book.getIsbn());
        this.setTitle(book.getTitle());
        this.setCreatedOn(book.getCreatedOn());
        this.setBorrowedOn(borrowedOn);

        if (borrowedOn == null
                || (book.getBorrowings() != null
                && book.getBorrowings().size() >= 1
                && book.getBorrowings().get(0) != null)) {
            this.setBorrowedOn(book.getBorrowings().get(0).getBorrowedOn());
            Borrowing borrowing = book.getBorrowings().get(0);
            this.setReturnedOn(borrowing.getReturnedOn());
            this.setBorrowerName(borrowing.getBorrower().getFirstname() + " " + borrowing.getBorrower().getLastname());
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

