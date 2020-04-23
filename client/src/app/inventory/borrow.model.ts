interface Borrower {
  firstname: string;
  lastname: string;
}

export interface Borrow {

  borrowedOn: number;
  returnedOn: number;

  borrower: Borrower;

}
