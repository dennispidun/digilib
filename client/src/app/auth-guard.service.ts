import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {AppService} from "./app.service";

// tslint:disable-next-line:import-blacklist

@Injectable({
  providedIn: "root"
})
export class AuthGuardService implements CanActivate {

  constructor(public auth: AppService, public router: Router) {
  }

  canActivate(): boolean {
    if (!this.auth.authenticated()) {
      this.router.navigateByUrl("/login").then(() => {});
      return false;
    }
    return true;
  }

  // canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
  //
  //   return false;
  // }
}
