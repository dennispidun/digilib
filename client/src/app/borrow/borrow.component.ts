import {Component, Input, OnInit} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpClient} from "@angular/common/http";
@Component({
  selector: "app-borrow",
  templateUrl: "./borrow.component.html",
  styleUrls: ["./borrow.component.scss"]
})
export class BorrowComponent implements OnInit {
  borrowerName: string;

  @Input()
  invnr: string;

  constructor(public activeModal: NgbActiveModal, private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  borrow() {
    const splittedStudentName = this.borrowerName.split(" ");

    this.http.post("/api/books/" + this.invnr + "/borrowings", {
      firstname: splittedStudentName[0],
      lastname: splittedStudentName[1]
    }).subscribe((data) => {
      this.activeModal.close(data);
    });
  }
}
