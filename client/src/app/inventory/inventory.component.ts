import {Component, OnInit} from "@angular/core";
import {Book} from "./book.model";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: "app-inventory",
  templateUrl: "./inventory.component.html",
  styleUrls: ["./inventory.component.scss"]
})
export class InventoryComponent implements OnInit {

  books: Book[] = [];
  behind = false;

  searchInput: string;

  constructor(private http: HttpClient, private router: Router) {
    this.updateBehind();
  }

  ngOnInit() {
  }

  updateBehind() {
    this.http.get("/api/books").toPromise().then((data) => {
      this.updateBooks(data);
    });


  }

  private updateBooks(data: Object) {
    const newBooks: Book[] = (data as Book[]).map(book => {
      book.createdOn = Date.parse(String(book.createdOn));
      book.borrowedOn = Date.parse(String(book.borrowedOn));
      if (book.title.length > 30) {
        book.title = book.title.substring(0, 27) + "...";
      }
      return book;
    });

    if (this.behind) {
      this.books = newBooks.filter(book => book.borrowedOn &&
        (Math.abs(book.borrowedOn - Date.now()) / (1000 * 3600 * 24)) >= 6);
    } else {
      this.books = newBooks;
    }
  }

  view(book: Book) {
    return this.router.navigate(["book/" + book.invnr]);
  }

  searchBook() {
    this.http.get("/api/books?search=" + this.searchInput).toPromise().then((data) => {
      this.behind = false;
      this.updateBooks(data);
    });
  }
}
