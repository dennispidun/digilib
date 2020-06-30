import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {HttpClient} from "@angular/common/http";
import {User} from "../user/user.model";

@Component({
  selector: 'app-add-user',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit {

  user: User = {
    username: "",
    firstname: "",
    lastname: "",
    password: "",
    role: "USER"
  };
  error = {
    username: "",
    firstname: "",
    lastname: "",
    password: ""
  };

  loggedInUser: User;

  editUser: User;

  action = "erstellen";
  roleEditable = true;

  constructor(public activeModal: NgbActiveModal, private http: HttpClient) {
  }

  ngOnInit(): void {
    if (this.editUser) {
      this.action = "bearbeiten";
      this.user = {...this.editUser};
    }
  }

  createOrEdit() {
    this.validate();

    if (!this.editUser) {
      this.http.post("/api/users", this.user).subscribe((success) => {
        this.activeModal.close();
      }, error => {
        this.validate(error.error.apierror);
      });
    } else {
      this.http.patch("/api/users", this.user).subscribe((success) => {
        this.activeModal.close();
      }, error => {
        this.validate(error.error.apierror);
      });
    }
  }

  private validate(apierror?: any) {
    this.error = {
      username: "",
      firstname: "",
      lastname: "",
      password: "",
    }

    if (apierror) {
      if (apierror.subErrors) {
        for (const subError of apierror.subErrors) {
          if (subError.field === "username") {
            this.error.username = subError.message;
          }
        }
      }
    }

    if (!this.user.username || this.user.username.length === 0) {
      this.error.username = "Der Username darf nicht leer sein.";
    }
    if (!this.user.firstname || this.user.firstname.length === 0) {
      this.error.firstname = "Der Vorname darf nicht leer sein.";
    }
    if (!this.user.lastname || this.user.lastname.length === 0) {
      this.error.lastname = "Der Nachname darf nicht leer sein.";
    }
    if (!this.editUser && (!this.user.password || this.user.password.length === 0)) {
      this.error.password = "Das Passwort darf nicht leer sein.";
    }
  }

}
