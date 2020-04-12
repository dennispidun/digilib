export interface Book {
  invnr: string;
  isbn: string;
  title: string;
  author: string;
  borrowedOn?: number;
  createdOn: number;
}
