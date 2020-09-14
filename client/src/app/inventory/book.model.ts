import {Genre} from "../details/genre.model";

export interface Book {
  invnr: string;
  isbn: string;
  title: string;
  author: string;
  genre?: Genre;
  createdOn?: number;
  borrowedOn?: number;
  borrowerName?: string
  returnedOn?: number;
  type: string;
  comment: string;
  price: string;
  daysOverdue: number;
  deletedOn: Date;
}
