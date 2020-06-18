import { Component, OnInit } from '@angular/core';
import {Borrower} from "../inventory/borrow.model";
import {HttpClient} from "@angular/common/http";
import {Borrowing} from "./borrowing.model";

@Component({
  selector: 'app-borrower-list',
  templateUrl: './borrower-list.component.html',
  styleUrls: ['./borrower-list.component.scss']
})
export class BorrowerListComponent implements OnInit {
  pageNo = 0;
  nextBorrowers: Borrower[] = [];
  borrowers: Borrower[] = [];
  borrowings: Borrowing[];
  private PAGE_SIZE = 8;

  constructor(private http: HttpClient) {
    this.updateBorrowers();
  }

  ngOnInit(): void {
  }

  back() {
    if (this.pageNo > 0) {
      this.pageNo--;
    }
    this.updateBorrowers();
  }

  next() {
    if (this.nextBorrowers.length > 0) {
      this.pageNo++;
      this.updateBorrowers();
    }
  }

  private updateBorrowers() {
    this.http.get(`/api/borrower?pageNo=${this.pageNo}&pageSize=${this.PAGE_SIZE}`)
    .toPromise().then((data) => {
      this.borrowers = data as Borrower[];

      this.borrowers.forEach(b => {
        this.http.get(`/api/borrower/${b.id}/unreturned`)
          .toPromise().then((mData) => {
          this.borrowings = mData as Borrowing[];
          b.unreturned = this.borrowings.length;
        });
      });

    });

    this.http.get(`/api/borrower?pageNo=${this.pageNo + 1}&pageSize=${this.PAGE_SIZE}`)
      .toPromise().then((data) => {
      this.nextBorrowers = data as Borrower[];
    });

  }

  setTeacher(borrower: Borrower) {
    this.http.patch(`/api/borrower/${borrower.id}/teacher`, borrower.teacher)
      .subscribe();
  }


}
