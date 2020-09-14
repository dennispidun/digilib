import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Book} from "../inventory/book.model";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {debounceTime, distinctUntilChanged, map, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent implements OnInit {

  book: Book = {
    comment: "",
    deletedOn: undefined,
    price: "",
    type: "",
    isbn: "",
    invnr: "",
    title: "",
    author: "",
    daysOverdue: 0
  };
  error = {
    isbn: "",
    invnr: "",
    title: "",
    author: ""
  };
  genreSearchFailed: boolean;
  genreSearch = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.findGenre(term))
    );

  constructor(public activeModal: NgbActiveModal, private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  create() {
    this.validate();

    this.http.post("/api/books", this.book).subscribe((data) => {
      this.activeModal.close();
    }, error => {
      this.validate(error.error.apierror);
    });
  }

  private validate(apierror?: any) {
    this.error = {
      isbn: "",
      invnr: "",
      title: "",
      author: ""
    }

    if (apierror) {
      if (apierror.subErrors) {
        for (const subError of apierror.subErrors) {
          if (subError.field === "isbn") {
            this.error.isbn = subError.message;
          } else if (subError.field === "invnr") {
            this.error.invnr = subError.message;
          }
        }
      }
    }

    if (this.book.title.length === 0) {
      this.error.title = "Der Buchtitel darf nicht leer sein.";
    }
    if (this.book.isbn.length === 0) {
      this.error.isbn = "Die ISBN darf nicht leer sein.";
    }
    if (this.book.author.length === 0) {
      this.error.author = "Der Autor/ Die Autorin darf nicht leer sein.";
    }
    if (this.book.invnr.length === 0) {
      this.error.invnr = "Die Inventarnummer darf nicht leer sein.";
    }
  }

  private findGenre(term: string): Observable<string[]> {
    if (term === "") {
      return of([]);
    }

    return this.http.get(`/api/genres?search=${term}`)
      .pipe(
        map(value => (value as []))
      )
  }
}
