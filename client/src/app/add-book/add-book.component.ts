import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Book} from "../inventory/book.model";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-add-book',
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent implements OnInit {

  book: Book = {
    isbn: "",
    invnr: "",
    title: "",
    author: ""
  };
  error = {
    isbn: "",
    invnr: "",
    title: "",
    author: ""
  };

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
  }
}
