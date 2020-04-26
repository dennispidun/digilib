import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {Book} from "./book.model";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {fromEvent} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, tap} from "rxjs/operators";

@Component({
  selector: "app-inventory",
  templateUrl: "./inventory.component.html",
  styleUrls: ["./inventory.component.scss"]
})
export class InventoryComponent implements OnInit {

  @ViewChild("search") searchElement: ElementRef;

  books: Book[] = [];
  behind = false;

  constructor(private http: HttpClient, private router: Router) {
    this.updateBooks();
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    // server-side search
    fromEvent(this.searchElement.nativeElement, "keyup")
      .pipe(
        filter(Boolean),
        debounceTime(350),
        distinctUntilChanged(),
        tap((text) => this.searchBook())
      )
      .subscribe();
  }

  updateBooks() {
    this.http.get("/api/books").toPromise().then((data) => {
      this.parseBooks(data);
    });


  }

  private parseBooks(data: Object) {
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
    if (this.searchElement.nativeElement.value && this.searchElement.nativeElement.value.length >= 0) {
      this.http.get("/api/books?search=" + this.searchElement.nativeElement.value).toPromise().then((data) => {
        this.behind = false;
        this.parseBooks(data);
      });
    } else {
      this.updateBooks();
    }
  }
}
