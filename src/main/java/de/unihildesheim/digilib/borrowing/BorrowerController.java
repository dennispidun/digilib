package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrower;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrower")
public class BorrowerController {

    private BorrowerRepository borrowerRepository;

    public BorrowerController(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @GetMapping
    public List<Borrower> getBorrowers(@RequestParam(required = false) String search,
                                       @RequestParam(required = false) Integer pageNo,
                                       @RequestParam(required = false) Integer pageSize) {
        if (search != null && search.length() > 0) {
            return findBorrowersMatching(search);
        }

        return borrowerRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
    }

    private List<Borrower> findBorrowersMatching(@RequestParam(required = false) String search) {
        String firstname = search;
        String lastname = search;

        if (search.contains(" ")) {
            firstname = search.substring(0, search.lastIndexOf(" "));
            lastname = search.substring(search.lastIndexOf(" "));
        }

        List<Borrower> searchBorrowersFirst = borrowerRepository
                .findAllByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(firstname, lastname);
        List<Borrower> searchBorrowersSecond = borrowerRepository
                .findAllByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(lastname, firstname);
        searchBorrowersFirst.addAll(searchBorrowersSecond);
        return searchBorrowersFirst.stream().distinct().collect(Collectors.toList());
    }


    @PatchMapping(value = "/{id}/teacher", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Borrower setTeacher(@PathVariable("id") long id, @RequestBody String teacher) {
        Borrower borrower = borrowerRepository.findById(id).orElseThrow(() -> new BorrowerNotFoundException(id));
        borrower.setTeacher(teacher.equals("true"));
        return borrowerRepository.save(borrower);
    }

}
