package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrower;
import de.unihildesheim.digilib.borrowing.model.Borrowing;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrower")
public class BorrowerController {

    private BorrowerRepository borrowerRepository;
    private BorrowingRepository borrowingRepository;

    public BorrowerController(BorrowerRepository borrowerRepository, BorrowingRepository borrowingRepository) {
        this.borrowerRepository = borrowerRepository;
        this.borrowingRepository = borrowingRepository;
    }

    @GetMapping
    public List<Borrower> getBorrowers(@RequestParam(required = false) String search,
                                       @RequestParam int pageNo,
                                       @RequestParam int pageSize) {
        return borrowerRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
    }

    @PatchMapping(value = "/{id}/teacher", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Borrower setTeacher(@PathVariable("id") long id, @RequestBody String teacher) {
        Borrower borrower = borrowerRepository.findById(id).orElseThrow(() -> new BorrowerNotFoundException(id));
        borrower.setTeacher(teacher.equals("true"));
        return borrowerRepository.save(borrower);
    }

    @GetMapping(value = "/{id}/unreturned")
    public List<Borrowing> getUnreturned(@PathVariable("id") long id) {
        return borrowingRepository.getBorrowingByBorrower_IdAndReturnedOnIsNull(id);
    }

}
