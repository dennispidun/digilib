import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

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

    this.http.post(`/api/login?username=${credentials.username}&password=${credentials.password}`, {}).subscribe(response => {
        const token = (response as any).token;
        if (token) {
          localStorage.setItem("token", token);
        } else {
          localStorage.removeItem("token");
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
