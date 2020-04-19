import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Book} from "../inventory/book.model";

@Component({
  selector: "app-details",
  templateUrl: "./details.component.html",
  styleUrls: ["./details.component.scss"]
})
export class DetailsComponent implements OnInit {

  private invNr: string;

  book: Book;

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.invNr = this.route.snapshot.paramMap.get("invnr");
    this.http.get("/api/books/" + this.invNr).toPromise().then((data) => {
      this.book = data as Book;
    });
  }

}
