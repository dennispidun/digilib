import {Component, OnInit} from '@angular/core';
import {Book} from './book.model';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss']
})
export class InventoryComponent implements OnInit {

  books: Book[] = [];
  behind = false;

  constructor(private http: HttpClient) {
    this.updateBehind();
  }

  ngOnInit() {
  }

  updateBehind() {
    if (this.behind) {
      this.books = this.books.filter(book => book.borrowedOn &&
        (Math.abs(Date.parse(String(book.borrowedOn)) - Date.now()) / (1000 * 3600 * 24)) >= 6);
    } else {
      this.http.get('/api/books').toPromise().then((data) => {
        this.books = data as [];
        console.log(data);
      });
    }

  }
}
