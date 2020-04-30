42227526
42227526
42238089
9783468181511
9783468181511
8001770000000001
9941212369237
9941235859968
9941277343869
358605651425
31134122007009
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
4316268503464
import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Book} from "../inventory/book.model";
import {BorrowComponent} from "../borrow/borrow.component";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Borrow} from "../inventory/borrow.model";

@Component({
  selector: "app-details",
  templateUrl: "./details.component.html",
  styleUrls: ["./details.component.scss"]
})
export class DetailsComponent implements OnInit {

  private invnr: string;

  book: Book;
  history: Borrow[] = [];

  constructor(private modalService: NgbModal, private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.invnr = this.route.snapshot.paramMap.get("invnr");
    this.http.get("/api/books/" + this.invnr).toPromise().then((data) => {
      this.book = data as Book;
    });

    this.updateHistory();
  }

  private updateHistory() {
    this.http.get("/api/books/" + this.invnr + "/borrowings").toPromise().then((data) => {
      this.history = data as Borrow[];
    });
  }

  borrow() {
    const modalRef: NgbModalRef = this.modalService.open(BorrowComponent);
    modalRef.componentInstance.invnr = this.invnr;

    modalRef.result.then((result) => {
      this.book.borrowedOn = result.borrowedOn;
      this.book.returnedOn = result.returnedOn;
      this.book.borrowerName = result.borrower.firstname + " " + result.borrower.lastname;
      this.updateHistory();
    });

  }

  cancel() {
    this.http.delete("/api/books/" + this.invnr + "/latest-borrowing", {
    }).subscribe((data) => {
      // @ts-ignore
      this.book.returnedOn = data.returnedOn;
      this.updateHistory();
    });
  }
}
