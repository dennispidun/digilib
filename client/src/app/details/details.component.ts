import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Book} from "../inventory/book.model";
import {BorrowComponent} from "../borrow/borrow.component";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Borrow} from "../inventory/borrow.model";
import {EditBookComponent} from "../edit-book/edit-book.component";

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
      if (this.book.genre === null) {
        this.book.genre = {
          id: 0,
          genre: ""
        };
      }
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

  edit() {
    const oldBook: Book ={...this.book};
    const modalRef = this.modalService.open(EditBookComponent);
    modalRef.componentInstance.book = this.book;
    modalRef.result.then(
      (re) => {},
      (re) => {this.book = oldBook;})
    /*modalRef.componentInstance.pass.subscribe((result) => {
      if (result === "abort") {
        this.book = oldBook;
      }
    })*/
  }
}
