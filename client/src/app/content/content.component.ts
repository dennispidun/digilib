import { Component, OnInit } from "@angular/core";
import {AppService, User} from "../app.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: "app-content",
  templateUrl: "./content.component.html",
  styleUrls: ["./content.component.scss"]
})
export class ContentComponent implements OnInit {

  user: User;

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.app.authenticate(undefined, undefined);
    this.app.user.subscribe(data => {
      this.user = data;
    });
  }

  ngOnInit(): void {
  }

  logout() {
    this.app.logout();
    this.router.navigate(["login"]);
  }

}
