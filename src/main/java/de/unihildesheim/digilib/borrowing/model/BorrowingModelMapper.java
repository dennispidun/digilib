package de.unihildesheim.digilib.borrowing.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class BorrowingModelMapper {

    public BorrowingDto mapToListBorrowing(Borrowing borrowing) {
        BorrowingDto borrowingDto = new BorrowingDto();
        borrowingDto.setBorrower(borrowing.getBorrower());
        borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
        borrowingDto.setReturnedOn(borrowing.getReturnedOn());
        borrowingDto.setShouldReturnOn(borrowing.getShouldReturnOn());
        borrowingDto.setDaysOverdue(borrowing.getShouldReturnOn() != null ?
                (int) DAYS.between(borrowing.getShouldReturnOn(), LocalDate.now()) : 0);
        if (borrowing.getLender() != null) {
            borrowingDto.setLenderFirstname(borrowing.getLender().getFirstname());
            borrowingDto.setLenderLastname(borrowing.getLender().getLastname());
        }
        if (borrowing.getReceiver() != null) {
            borrowingDto.setReceiverFirstname(borrowing.getReceiver().getFirstname());
            borrowingDto.setReceiverLastname(borrowing.getReceiver().getLastname());
        }
        return borrowingDto;
    }
}
