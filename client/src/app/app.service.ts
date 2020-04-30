import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class AppService {

  constructor(private http: HttpClient) {
  }

  authenticate(credentials, callback) {
    if (!credentials) {
      return;
    }

    const headers = new HttpHeaders(credentials ? {
      authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)
    } : {});

    this.http.get("/api/user", {headers: headers}).subscribe(response => {
      if (response["name"]) {
        localStorage.setItem("credentials", btoa(credentials.username + ":" + credentials.password));
      } else {
        localStorage.removeItem("credentials");
      }
      return callback && callback();
    });

  }

  authenticated(): boolean {
    const credentials = localStorage.getItem("credentials");
    return credentials && credentials != null && credentials.length > 0;
  }

  logout() {
    localStorage.removeItem("credentials");
  }
}
