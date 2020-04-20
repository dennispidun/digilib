interface Student {
  firstname: string;
  lastname: string;
}

export interface Borrow {

  borrowedOn: number;
  returnedOn: number;

  student: Student;

}
