import { Component, OnInit } from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "app-borrow",
  templateUrl: "./borrow.component.html",
  styleUrls: ["./borrow.component.scss"]
})
export class BorrowComponent implements OnInit {

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

}
