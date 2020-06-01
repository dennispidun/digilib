import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "./user/user.model";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {UserDetailsComponent} from "./user-details/user-details.component";

@Injectable({
  providedIn: "root"
})
export class AppService {

  user: Observable<User>;

  constructor(private http: HttpClient, private modalService: NgbModal) {
    this.user = new Observable((observer) => {
      this.http.get("/api/user").subscribe(data => {
        const userData = data as any;
        observer.next({
          firstname: userData.firstname,
          lastname: userData.lastname,
          username: userData.username,
          password: userData.password,
          enabled: userData.enabled,
          role: userData.role,
        });
      });
    });
  }

  editUser(user: User, roleEditable?: boolean): Promise<any> {
    const modalRef: NgbModalRef = this.modalService.open(UserDetailsComponent, {});
    const editUserModal: UserDetailsComponent = modalRef.componentInstance;
    editUserModal.editUser = {...user};
    editUserModal.roleEditable = roleEditable;

    return modalRef.result;
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
