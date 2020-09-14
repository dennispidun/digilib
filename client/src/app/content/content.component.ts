import {Component, OnInit} from "@angular/core";
import {AppService} from "../app.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {User} from "../user/user.model";

@Component({
  selector: "app-content",
  templateUrl: "./content.component.html",
  styleUrls: ["./content.component.scss"]
})
export class ContentComponent implements OnInit {

  user: User;

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.app.getUser().subscribe(data => {
      this.user = data;
    });
  }

  ngOnInit(): void {
  }

  logout() {
    this.app.logout();
    this.router.navigate(["login"]);
  }

  edit() {
    this.app.editUser(this.user, false).then(() => {
    })
  }
}
