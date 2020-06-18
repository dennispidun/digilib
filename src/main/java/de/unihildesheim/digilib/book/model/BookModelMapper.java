package de.unihildesheim.digilib.book.model;

import org.springframework.stereotype.Component;

@Component
public class BookModelMapper {

    public ListBookDto mapToListBook(Book book) {
        ListBookDto bookDto = new ListBookDto();
        bookDto.setIsbn(book.getIsbn());
        bookDto.setInvnr(book.getInvnr());
        bookDto.setTitle(book.getTitle());
        bookDto.setCreatedOn(book.getCreatedOn());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setGenre(book.getGenre().getGenre());
        return bookDto;
    }
}
