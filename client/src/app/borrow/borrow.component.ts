import {Component, Input, OnInit} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {catchError, debounceTime, distinctUntilChanged, map, switchMap} from "rxjs/operators";

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
  borrowerName: string;

  @Input() invnr: string;

  error: string;

  searchFailed: boolean;

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

  findBorrower(term: string): Observable<String[]> {
    if (term === "") {
      return of([]);
    }


    return this.http.get(`/api/borrower?search=${term}`)
      .pipe(
        map(value => (value as []).map(value1 => {
          const borrower = value1 as any;
          return borrower.firstname + " " + borrower.lastname;
        }))
      )
  }

  borrow() {

    this.http.post("/api/books/" + this.invnr + "/borrowings", {
      firstname: this.borrowerName.substring(0, this.borrowerName.lastIndexOf(" ")),
      lastname: this.borrowerName.substring(this.borrowerName.lastIndexOf(" ") + 1)
    }).subscribe((data) => {
      this.activeModal.close(data);
    }, (err) => {
      if (err.error.apierror.message === "StudentCannotBorrowMultipleBooks") {
        this.error = "Ein Schüler darf nicht zwei Bücher gleichzeitig ausleihen.";
      }
    });
  }
}
