package de.unihildesheim.digilib.borrowing.model;

import org.springframework.stereotype.Component;

@Component
public class BorrowingModelMapper {

    public ListBorrowingDto mapToListBorrowing(Borrowing borrowing) {
        ListBorrowingDto borrowingDto = new ListBorrowingDto();
        borrowingDto.setBorrower(borrowing.getBorrower());
        borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
        borrowingDto.setReturnedOn(borrowing.getReturnedOn());
        return borrowingDto;
    }
}
