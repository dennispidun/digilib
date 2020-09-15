import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AppService} from "../app.service";
import {User} from "../user/user.model";
import {Book} from "../inventory/book.model";

@Component({
  selector: "app-archived",
  templateUrl: "./archived.component.html",
  styleUrls: ["./archived.component.scss"]
})
export class ArchivedComponent implements OnInit {

  @ViewChild("search") searchElement: ElementRef;

  books: Book[] = [];
  beforeBooks: Book[] | undefined = undefined;
  nextBooks: Book[] | undefined = undefined;

  booksCount = 0;
  pagesCount = 0;

  first = true;
  last: boolean;

  loading = false;

  lastTimeKeyEntered = 0;

  private PAGE_SIZE = 10;

  pageNo = 0;

  user: User;


  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.updateBooks(this.pageNo);
    this.app.getUser().subscribe(data => {
      this.user = data;
    });
  }

  ngOnInit() {
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngAfterViewInit() {
  }

  updateBooks(pageNo) {
    this.loading = true;

    return this.http.get(`/api/books/archived?pageNo=${pageNo}&pageSize=${this.PAGE_SIZE}`)
      .toPromise().then((data: any) => {
        this.books = this.parseBooks(data.content);
        this.first = data.first;
        this.last = data.last;
        this.booksCount = data.totalElements;
        this.pagesCount = Math.ceil(this.booksCount / 10);

        if(!this.last) {
          this.getBooks(pageNo + 1).then((books: any) => {
            this.nextBooks = this.parseBooks(books.content);
            this.loading = false;
          });
        }

        if(!this.first) {
          this.getBooks(pageNo - 1).then((books: any) => {
            this.beforeBooks = this.parseBooks(books.content);
            this.loading = false;
          });
        }
      });
  }

  private getBooks(pageNo: number) {
    return this.http.get(`/api/books/archived?pageNo=${pageNo}&pageSize=${this.PAGE_SIZE}`)
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
}
