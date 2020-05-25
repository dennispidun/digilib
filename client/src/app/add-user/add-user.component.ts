import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpClient} from "@angular/common/http";
import {User} from "../user/user.model";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {

  user: User = {
    username: "",
    firstname: "",
    lastname: "",
    role: ""
  };
  error = {
    username: "",
    firstname: "",
    lastname: ""
  };

  constructor(public activeModal: NgbActiveModal, private http: HttpClient) { }

  ngOnInit(): void {
  }

  create() {
    this.validate();

    this.http.post("/api/users", this.user).subscribe((success) => {
      this.activeModal.close();
    }, error => {
      this.validate(error.error.apierror);
    });
  }

  private validate(apierror?: any) {
    this.error = {
      username: "",
      firstname: "",
      lastname: ""
    }
    if (this.user.username.length === 0) {
      this.error.username = "Der Username darf nicht leer sein.";
    }
    if (this.user.firstname.length === 0) {
      this.error.firstname = "Der Vorname darf nicht leer sein.";
    }
    if (this.user.lastname.length === 0) {
      this.error.lastname = "Der Nachname darf nicht leer sein.";
    }
  }

}
