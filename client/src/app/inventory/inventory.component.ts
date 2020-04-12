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
  behind = true;

  constructor(private http: HttpClient) {
    this.books.push({
      author: 'Christian Ullenboom',
      invnr: '2018-04-1234',
      createdOn: Date.now(),
      isbn: '978-3-16-148410-0',
      title: 'Java ist auch eine Insel',
    });
    this.books.push({
      author: 'Christian Ullenboom',
      invnr: '2017-03-1344',
      createdOn: Date.now() - 123123332300,
      borrowedOn: Date.now() - 3012000000,
      isbn: '978-3-16-148410-0',
      title: 'Java ist auch eine Insel',
    });
  }

  ngOnInit() {
  }

  getBooksFiltered(): Book[] {
    if (this.behind) {
      return this.books.filter(book => book.borrowedOn !== undefined && Math.abs(Date.now() - book.borrowedOn) / (1000 * 3600 * 24) > 6);
    }
    return this.books;
  }
}
