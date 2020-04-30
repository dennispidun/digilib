import { Component, OnInit } from "@angular/core";
import {AppService} from "../app.service";
import {Router} from "@angular/router";

@Component({
  selector: "app-content",
  templateUrl: "./content.component.html",
  styleUrls: ["./content.component.scss"]
})
export class ContentComponent implements OnInit {

  constructor(private app: AppService, private router: Router) { }

  ngOnInit(): void {
  }

  logout() {
    this.app.logout();
    this.router.navigate(["login"]);
  }

}
