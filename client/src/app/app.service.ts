import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "./user/user.model";

@Injectable({
  providedIn: "root"
})
export class AppService {

  user: Observable<User>;

  constructor(private http: HttpClient) {
    this.user = new Observable((observer) => {
      this.http.get("/api/user").subscribe(data => {
        const userData = data as any;
        observer.next({
          firstname: userData.firstname,
          lastname: userData.lastname,
          username: userData.username,
          enabled: userData.enabled,
          role: userData.role
        });
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
    return credentials && credentials.length > 0;
  }

  logout() {
    localStorage.removeItem("token");
  }
}
