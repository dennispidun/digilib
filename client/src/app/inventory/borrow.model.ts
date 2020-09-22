export interface Borrower {
  id: number;
  firstname: string;
  lastname: string;
  teacher: boolean;
  unreturned: number;
}

export interface Borrow {

  borrowedOn: number;
  returnedOn: number;

  borrower: Borrower;

  lenderFirstname: string;
  lenderLastname: string;
  receiverFirstname: string;
  receiverLastname: string;

}
