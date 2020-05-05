import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";

interface User {
  username: string;
  firstname: string;
  lastname: string;
  enabled: boolean;
}

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  readonly PAGESIZE: number = 1;

  users: User[];
  pageNo = 0;
  first: boolean;
  last: boolean;

  constructor(private http: HttpClient) {
    this.updateUsers();
  }

  ngOnInit(): void {
  }

  updateUsers() {
      this.http.get(`/api/users?pageNo=${this.pageNo}&pageSize=${this.PAGESIZE}`)
        .subscribe((data: any) => {
        this.first = data.first;
        this.last = data.last;
        this.users = data.content;
      });
    }


  back() {
    if (!this.first) {
      this.pageNo--;
      this.updateUsers();
    }
  }

  next() {
    if (!this.last) {
      this.pageNo++;
      this.updateUsers();
    }
  }
}
