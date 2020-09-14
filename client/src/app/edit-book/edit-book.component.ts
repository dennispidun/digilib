import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Book} from "../inventory/book.model";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Observable, of} from "rxjs";
import {debounceTime, distinctUntilChanged, map, switchMap} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-edit-book',
  templateUrl: './edit-book.component.html',
  styleUrls: ['./edit-book.component.scss']
})
export class EditBookComponent implements OnInit, OnDestroy {
  @Input() book: Book;

  error = {
    isbn: "",
    title: "",
    author: ""
  };

  constructor(public activeModal: NgbActiveModal, private http: HttpClient) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {

  }

  genreSearch = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.findGenre(term))
    );

  private findGenre(term: string): Observable<string[]> {
    if (term === "") {
      return of([]);
    }

    return this.http.get(`/api/genres?search=${term}`)
      .pipe(
        map(value => (value as []))
      )
  }

  private validate(apierror?: any) {
    this.error = {
      isbn: "",
      title: "",
      author: ""
    }

    if (apierror) {
      if (apierror.subErrors) {
        for (const subError of apierror.subErrors) {
          if (subError.field === "isbn") {
            this.error.isbn = subError.message;
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
    }
  }

  modify(archive) {
    this.validate();

    if (archive) {
      if (this.book.deletedOn === null) {
        this.book.deletedOn = new Date();
      } else {
        this.book.deletedOn = null;
      }
    }

    this.http.patch("/api/books/" + this.book.invnr, this.book).subscribe((data) => {
      this.activeModal.close(data);
    }, error => {
      this.validate(error.error.apierror);
    });
  }
}
