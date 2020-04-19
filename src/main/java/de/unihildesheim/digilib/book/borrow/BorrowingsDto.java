package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.student.Student;
import lombok.Data;

import java.util.Date;

@Data
public class BorrowingsDto {

    private Student student;
    private Date borrowedOn;
    private Date returnedOn;
}
