import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../app.service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {UserDetailsComponent} from "../user-details/user-details.component";
import {User} from "./user.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  readonly PAGESIZE: number = 10;

  users: User[];
  pageNo = 0;
  first: boolean;
  last: boolean;
  loggedInUser: User;
  addingUser = false;
  editUser: User;

  constructor(private app: AppService, private http: HttpClient,private router: Router, private modalService: NgbModal) {
    this.ngOnInit();
  }

  ngOnInit(): void {
    this.updateUsers();
    // this.loggedInUser = this.app.getLoggedInUser();
    this.app.getUser().subscribe(user => {
      this.loggedInUser = user;
    });
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

  setEnabled(user: User) {
    this.http.patch(`/api/users/${user.username}/enabled`, user.enabled).subscribe();
  }

  edit() {
    this.app.editUser(this.editUser, false).then(() => {
      this.updateUsers();
    }).catch((err) => {
    })
  }

  addUser() {
    const modalRef: NgbModalRef = this.modalService.open(UserDetailsComponent);

    this.addingUser = true;

    modalRef.result.then(() => {
      this.updateUsers();
      this.addingUser = false;
    });
  }
}
