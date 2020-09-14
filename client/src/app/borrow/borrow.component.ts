import {Component, Input, OnInit} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {debounceTime, distinctUntilChanged, map, switchMap} from "rxjs/operators";
import {Borrowing} from "../borrower-list/borrowing.model";

const PARAMS = new HttpParams({
  fromObject: {
    action: "opensearch",
    format: "json",
    origin: "*"
  }
});

@Component({
  selector: "app-borrow",
  templateUrl: "./borrow.component.html",
  styleUrls: ["./borrow.component.scss"]
})
export class BorrowComponent implements OnInit {

  @Input() invnr: string;

  weeks = 1;
  borrowerName: string;

  error = {
    name: "",
    weeks: "",
    general: ""
  };

  msg: string;

  searchFailed: boolean;

  borrower;

  constructor(public activeModal: NgbActiveModal, private http: HttpClient) {
  }
  ngOnInit(): void {
  }

  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.findBorrower(term))
    );

  findBorrower(term: string): Observable<string[]> {
    this.msg = "";
    this.borrower = undefined;
    if (term === "") {
      return of([]);
    }


    return this.http.get(`/api/borrower?search=${term}`)
      .pipe(
        map(value => (value as []).map(value1 => {
          this.borrower = value1 as any;
          return this.borrower.firstname + " " + this.borrower.lastname;
        }))
      )
  }

  getCurrentlyBorrowed() {
    if (this.borrower !== undefined) {
      this.http.get(`/api/borrower/${this.borrower.id}/unreturned`).toPromise().then((data) => {
        const borrowings = data as Borrowing[];
        if (borrowings.length === 1) {
          this.msg = "Achtung: dieser Schüler hat bereits ein Buch ausgeliehen. 2 Bücher pro Schüler sollten nur bei Ausnahmen erlaubt werden.";
        }
      })
    }
  }

  borrow() {
    this.validate().then(() => {
      this.http.post("/api/books/" + this.invnr + "/borrowings", {
        firstname: this.borrowerName.substring(0, this.borrowerName.lastIndexOf(" ")),
        lastname: this.borrowerName.substring(this.borrowerName.lastIndexOf(" ") + 1),
        weeks: this.weeks
      }).subscribe((data) => {
        this.activeModal.close(data);
      }, (err) => {
        if (err.error.apierror.message === "StudentCannotBorrowMultipleBooks") {
          this.error.general = "Ein Schüler darf nicht mehr als zwei Bücher gleichzeitig ausleihen.";
        } else if (err.error.apierror.message === "Validation error") {
          err.error.apierror.subErrors.forEach(apiError => {
            this.error[apiError.field] = apiError.message;
          });
        }

      });
    }).catch(() => {

    });
  }

  private validate(): Promise<any> {
    if (!this.borrowerName.includes(" ")) {
      this.searchFailed = true;
      this.error.name = "Bitte geben Sie einen Vornamen an.";
      return Promise.reject();
    } else {
      const firstname = this.borrowerName.substring(0, this.borrowerName.lastIndexOf(" "));
      const lastname = this.borrowerName.substring(this.borrowerName.lastIndexOf(" ") + 1);
      if (firstname.length === 0) {
        this.searchFailed = true;
        this.error.name = "Bitte geben Sie einen Vornamen an.";
        return Promise.reject();
      }
      if (lastname.length === 0) {
        this.searchFailed = true;
        this.error.name = "Bitte geben Sie einen Nachnamen an.";
        return Promise.reject();
      }
    }

    return Promise.resolve();
  }
}
