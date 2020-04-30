import {Book} from "../inventory/book.model";
import {Borrower} from "../inventory/borrow.model";

export interface Borrowing {
  id: number;
  borrowedOn: Date;
  returnedOn: Date;
  book: Book;
  borrower: Borrower;
}
