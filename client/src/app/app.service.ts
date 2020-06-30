import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Subject} from "rxjs";
import {User} from "./user/user.model";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {UserDetailsComponent} from "./user-details/user-details.component";

@Injectable({
  providedIn: "root"
})
export class AppService {

  loggedinUser: User;

  private user: BehaviorSubject<User>;

  constructor(private http: HttpClient, private modalService: NgbModal) {
    this.user = new BehaviorSubject(null);

    if (this.authenticated()) {
      this.updateUser();
    }
  }

  public getUser(): Subject<User> {
    return this.user;
  }

  private updateUser() {
    this.http.get("/api/user").toPromise().then(user => {
      this.loggedinUser = user as User;
      this.user.next({
        firstname: this.loggedinUser.firstname,
        lastname: this.loggedinUser.lastname,
        username: this.loggedinUser.username,
        password: this.loggedinUser.password,
        enabled: this.loggedinUser.enabled,
        role: this.loggedinUser.role,
      });
    });
  }

  editUser(user: User, roleEditable?: boolean): Promise<any> {
    const modalRef: NgbModalRef = this.modalService.open(UserDetailsComponent);
    const editUserModal: UserDetailsComponent = modalRef.componentInstance;
    editUserModal.editUser = {...user};
    editUserModal.roleEditable = roleEditable;
    this.getUser().subscribe(next => {
      editUserModal.loggedInUser = next;
    })

    return modalRef.result.then(() => {
      this.updateUser();
    }).catch((err) => {
    })
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

        this.updateUser();
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
