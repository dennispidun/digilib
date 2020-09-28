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

  credentials = {username: "", password: ""};

  error = "";


  constructor(private app: AppService, private http: HttpClient, private router: Router) {
  }
  ngOnInit(): void {
  }

  async login() {
    this.app.authenticate(this.credentials, (result) => {

      if (result && result.status === "authenticated") {
        this.router.navigate(["dashboard"]);
      } else if (result && result.status === "error") {
        const statusCode = result.error.status;
        if (statusCode !== 200 && statusCode !== 401 && statusCode !== 403) {
          this.error = "System ist nicht erreichbar, bitte kontaktieren Sie einen Administrator.";
        } else if (statusCode === 401) {
          this.error = "Bitte überprüfen Sie Ihre Anmeldedaten.";
        }
      }
    });
    return false;
  }

}
