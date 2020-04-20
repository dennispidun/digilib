import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Book} from "../inventory/book.model";
import {BorrowComponent} from "../borrow/borrow.component";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "app-details",
  templateUrl: "./details.component.html",
  styleUrls: ["./details.component.scss"]
})
export class DetailsComponent implements OnInit {

  private invNr: string;

  book: Book;

  constructor(private modalService: NgbModal, private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.invNr = this.route.snapshot.paramMap.get("invnr");
    this.http.get("/api/books/" + this.invNr).toPromise().then((data) => {
      this.book = data as Book;
    });
  }

  borrow() {
    const modalRef: NgbModalRef = this.modalService.open(BorrowComponent);
    modalRef.componentInstance.invnr = this.invNr;

    modalRef.result.then((result) => {
      this.book.borrowedOn = result.borrowedOn;
      this.book.borrowerName = result.student.surname + " " + result.student.lastname;
    })

  }
}
