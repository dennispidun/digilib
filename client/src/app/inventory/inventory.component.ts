import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {Book} from "./book.model";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
// tslint:disable-next-line:import-blacklist
import {fromEvent} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, tap} from "rxjs/operators";
import {AppService} from "../app.service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {AddBookComponent} from "../add-book/add-book.component";
import {User} from "../user/user.model";

@Component({
  selector: "app-inventory",
  templateUrl: "./inventory.component.html",
  styleUrls: ["./inventory.component.scss"]
})
export class InventoryComponent implements OnInit {

  @ViewChild("search") searchElement: ElementRef;

  books: Book[] = [];
  beforeBooks: Book[] | undefined = undefined;
  nextBooks: Book[] | undefined = undefined;

  booksCount = 0;

  first = true;
  last: boolean;
  behind = false;

  loading = false;

  searchCache = "";
  lastTimeKeyEntered = 0;

  private PAGE_SIZE = 10;

  pageNo = 0;

  user: User;

  addingBook = false;


  constructor(private app: AppService, private http: HttpClient, private router: Router,
              private modalService: NgbModal) {
    this.updateBooks(this.pageNo);
    this.app.getUser().subscribe(data => {
      this.user = data;
    });
  }

  ngOnInit() {
  }

  // tslint:disable-next-line:use-lifecycle-interface
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

  updateBooks(pageNo) {
    let searchOption = "";
    let behindOption = "";
    this.loading = true;

    if (this.searchElement && this.searchElement.nativeElement.value && this.searchElement.nativeElement.value.length >= 0) {
      searchOption = "&search=" + this.searchElement.nativeElement.value;
    }

    if (this.behind) {
      behindOption = "&behind=true";
    }


    return this.http.get(`/api/books?pageNo=${pageNo}&pageSize=${this.PAGE_SIZE}${searchOption}${behindOption}`)
      .toPromise().then((data: any) => {
        this.books = this.parseBooks(data.content);
        this.first = data.first;
        this.last = data.last;
        this.booksCount = data.totalElements;

        if(!this.last) {
          this.getBooks(pageNo + 1, searchOption, behindOption).then((books: any) => {
            this.nextBooks = this.parseBooks(books.content);
            this.loading = false;
          });
        }

        if(!this.first) {
          this.getBooks(pageNo - 1, searchOption, behindOption).then((books: any) => {
            this.beforeBooks = this.parseBooks(books.content);
            this.loading = false;
          });
        }
      });
  }

  private getBooks(pageNo: number, searchOption: string, behindOption: string) {
    return this.http.get(`/api/books?pageNo=${pageNo}&pageSize=${this.PAGE_SIZE}${searchOption}${behindOption}`)
      .toPromise();
  }

  next() {
    if (!this.last) {
      if (this.nextBooks) {
        this.books = this.nextBooks;
      }
      this.pageNo++;

      this.updateBooks(this.pageNo).then(() => {});
    }
  }

  back() {
    if (!this.first) {
      if (this.beforeBooks) {
        this.books = this.beforeBooks;
      }
      this.pageNo--;

      this.updateBooks(this.pageNo).then(() => {});
    }
  }

  private parseBooks(data: any): Book[] {
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
    return this.router.navigate(["book/" + encodeURIComponent(book.invnr)]);
  }

  searchBook() {
    this.nextBooks = undefined;
    this.beforeBooks = undefined;
    this.updateBooks(0).then(() => this.pageNo = 0);
  }

  updateBehind() {
    this.nextBooks = undefined;
    this.beforeBooks = undefined;
    this.searchElement.nativeElement.value = "";
    this.updateBooks(0).then(() => this.pageNo = 0);
  }

  private addSearchInput(text) {
    if (Date.now() - this.lastTimeKeyEntered > 50) {
      this.searchCache = "";
    }

    if (!this.behind && !this.addingBook) {
      if (text.key !== "Enter") {
        this.searchCache += text.key;
        this.lastTimeKeyEntered = Date.now();
      } else {
        if (this.searchCache.length > 0) {
          this.searchElement.nativeElement.value = this.searchCache;
          this.searchCache = "";
          this.searchBook();
        }
      }
    }
  }

  addBook() {
    const modalRef: NgbModalRef = this.modalService.open(AddBookComponent);
    this.addingBook = true;

    modalRef.result.then(() => {
      this.updateBooks(this.pageNo).then(() => {});
      this.addingBook = false;
    });
  }
}
