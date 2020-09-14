package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.borrowing.model.Borrowing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Setter
@NoArgsConstructor
public class ListBookDto extends BookDto {

    private LocalDateTime borrowedOn;
    private String borrowerName;
    private LocalDateTime returnedOn;
    private LocalDate shouldReturnOn;
    private int daysOverdue;

    public ListBookDto(Book book) {
        this.setAuthor(book.getAuthor());
        this.setInvnr(book.getInvnr());
        this.setIsbn(book.getIsbn());
        this.setTitle(book.getTitle());
        this.setCreatedOn(book.getCreatedOn());

        if (book.getBorrowings() != null && book.getBorrowings().size() > 0) {
            Borrowing borrowing = book.getBorrowings().get(0);
            if (borrowing.getReturnedOn() == null) {

                this.setBorrowedOn(borrowing.getBorrowedOn());
                this.setReturnedOn(borrowing.getReturnedOn());
                this.setShouldReturnOn(borrowing.getShouldReturnOn());
                this.setBorrowerName(borrowing.getBorrower().getFirstname() + " " + borrowing.getBorrower().getLastname());

                if (this.shouldReturnOn != null) {
                    this.setDaysOverdue((int) DAYS.between(this.shouldReturnOn, LocalDate.now()));

                }
            }
        }
    }

    public ListBookDto(Borrowing borrowing) {
        Book book = borrowing.getBook();

        this.setAuthor(book.getAuthor());
        this.setInvnr(book.getInvnr());
        this.setIsbn(book.getIsbn());
        this.setTitle(book.getTitle());
        this.setCreatedOn(book.getCreatedOn());

        if (borrowing.getReturnedOn() == null) {
            this.setBorrowedOn(borrowing.getBorrowedOn());
            this.setReturnedOn(borrowing.getReturnedOn());
            this.setShouldReturnOn(borrowing.getShouldReturnOn());
            this.setBorrowerName(borrowing.getBorrower().getFirstname() + " " + borrowing.getBorrower().getLastname());
            this.setDaysOverdue((int) DAYS.between(this.shouldReturnOn, LocalDate.now()));
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

