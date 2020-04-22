import {Component, OnInit, ViewEncapsulation} from "@angular/core";

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"]
})
export class LoginComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
