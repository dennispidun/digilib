import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";

export interface User {
  username: string;
  firstname: string;
  lastname: string;
}

@Injectable({
  providedIn: "root"
})
export class AppService {

  user: Observable<User>;


  constructor(private http: HttpClient) {
    this.user = new Observable((observer) => {
      this.http.get("/api/user").subscribe(data => {
        observer.next({firstname: data.firstname, lastname: data.lastname, username: data.username});
      });
    });
  }

  authenticate(credentials, callback) {
    if (!credentials) {
      return;
    }

    this.http.post(`/api/login?username=${credentials.username}&password=${credentials.password}`, {}).subscribe(response => {
        const token = (response as any).token;
        if (token) {
          localStorage.setItem("token", token);
        } else {
          localStorage.removeItem("token");
          return callback && callback({status: "unauthenticated"});
        }


        return callback && callback({status: "authenticated"});
      },
      error => {
        localStorage.removeItem("token");
        return callback && callback({status: "error", error});
      });
  }

  authenticated(): boolean {
    const credentials = localStorage.getItem("token");
    return credentials && credentials != null && credentials.length > 0;
  }

  logout() {
    localStorage.removeItem("credentials");
  }
}
