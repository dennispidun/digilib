export interface Borrower {
  id: number;
  firstname: string;
  lastname: string;
  teacher: boolean;
  unreturned: number;
  grade: string;
}

export interface Borrow {

  borrowedOn: number;
  returnedOn: number;

  borrower: Borrower;

  lenderFirstname: string;
  lenderLastname: string;

}
