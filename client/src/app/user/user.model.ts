export interface User {
  username: string;
  firstname: string;
  lastname: string;
  password?: string;
  enabled?: boolean;
  role: string;
}
