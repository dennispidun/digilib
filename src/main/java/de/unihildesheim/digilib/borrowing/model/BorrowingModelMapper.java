package de.unihildesheim.digilib.borrowing.model;

import org.springframework.stereotype.Component;

@Component
public class BorrowingModelMapper {

    public BorrowingDto mapToListBorrowing(Borrowing borrowing) {
        BorrowingDto borrowingDto = new BorrowingDto();
        borrowingDto.setBorrower(borrowing.getBorrower());
        borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
        borrowingDto.setReturnedOn(borrowing.getReturnedOn());
        if (borrowing.getLender() != null) {
            borrowingDto.setLenderFirstname(borrowing.getLender().getFirstname());
            borrowingDto.setLenderLastname(borrowing.getLender().getLastname());
        }
        return borrowingDto;
    }
}
