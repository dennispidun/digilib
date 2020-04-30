import {Component, OnInit, ViewEncapsulation} from "@angular/core";
import {Router} from "@angular/router";
import {AppService} from "../app.service";
import {HttpClient} from "@angular/common/http";

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"]
})
export class LoginComponent implements OnInit {


  credentials = {username: "admin", password: "password"};

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
  }
  ngOnInit(): void {
  }

  login() {
    this.app.authenticate(this.credentials, () => {
      this.router.navigate(["dashboard"]);
    });
    return false;
  }
}
