import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {Book} from "./book.model";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
// tslint:disable-next-line:import-blacklist
import {fromEvent} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, tap} from "rxjs/operators";
import {AppService, User} from "../app.service";

@Component({
  selector: "app-inventory",
  templateUrl: "./inventory.component.html",
  styleUrls: ["./inventory.component.scss"]
})
export class InventoryComponent implements OnInit {

  @ViewChild("search") searchElement: ElementRef;

  books: Book[] = [];
  nextBooks: Book[] = [];
  behind = false;

  searchCache: String = "";

  private PAGE_SIZE = 10;

  pageNo = 0;

  user: User;

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.updateBooks();
    this.app.user.subscribe(data => {
      this.user = data;
    });
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

    fromEvent(document, "keypress")
      .pipe(
        filter(Boolean),
        tap((text) => this.addSearchInput(text))
      )
      .subscribe();
  }

  updateBooks() {
    let searchOption = "";
    let behindOption = "";

    if (this.searchElement && this.searchElement.nativeElement.value && this.searchElement.nativeElement.value.length >= 0) {
      searchOption = "&search=" + this.searchElement.nativeElement.value;
    }

    if (this.behind) {
      behindOption = "&behind=true";
    }

    this.http.get(`/api/books?pageNo=${this.pageNo}&pageSize=${this.PAGE_SIZE}${searchOption}${behindOption}`)
      .toPromise().then((data) => {
      this.books = this.parseBooks(data);
    });

    this.http.get(`/api/books?pageNo=${this.pageNo + 1}&pageSize=${this.PAGE_SIZE}${searchOption}${behindOption}`)
      .toPromise().then((data) => {
      this.nextBooks = this.parseBooks(data);
    });
  }

  next() {
    if (this.nextBooks.length > 0) {
      this.pageNo++;
      this.updateBooks();
    }
  }

  back() {
    if (this.pageNo > 0) {
      this.pageNo--;
    }
    this.updateBooks();
  }

  private parseBooks(data: Object): Book[] {
    return (data as Book[]).map(book => {
      book.createdOn = Date.parse(String(book.createdOn));
      book.borrowedOn = Date.parse(String(book.borrowedOn));
      if (book.title.length > 30) {
        book.title = book.title.substring(0, 27) + "...";
      }
      return book;
    });
  }

  view(book: Book) {
    return this.router.navigate(["book/" + book.invnr]);
  }

  searchBook() {
    this.pageNo = 0;
    this.updateBooks();
  }

  updateBehind() {
    this.pageNo = 0;
    this.searchElement.nativeElement.value = "";
    this.updateBooks();
  }

  private addSearchInput(text) {
    if (!this.behind) {
      if (text.key !== "Enter") {
        this.searchCache += text.key;
      } else {
        if (this.searchCache.length > 0) {
          this.searchElement.nativeElement.value = this.searchCache;
          this.searchCache = "";
          this.searchBook();
        }
      }
    }
  }
}
