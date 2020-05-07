export interface Book {
  invnr: string;
  isbn: string;
  title: string;
  author: string;
  genre?: string;
  createdOn?: number;
  borrowedOn?: number;
  borrowerName?: string
  returnedOn?: number;
}
