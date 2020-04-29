package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrower;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import java.util.List;

@RestController
@RequestMapping("/api/borrower")
public class BorrowerController {

    private BorrowerRepository borrowerRepository;

    public BorrowerController(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
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

}
