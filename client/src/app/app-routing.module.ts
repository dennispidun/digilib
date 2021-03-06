import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {InventoryComponent} from "./inventory/inventory.component";
import {ArchivedComponent} from "./archived/archived.component";
import {DetailsComponent} from "./details/details.component";
import {ContentComponent} from "./content/content.component";
import {LoginComponent} from "./login/login.component";
import {AppComponent} from "./app.component";
import {BorrowerListComponent} from "./borrower-list/borrower-list.component";
import {AuthGuardService} from "./auth-guard.service";
import {ImportComponent} from "./import/import.component";
import {UserComponent} from "./user/user.component";

const routes: Routes = [
  {
    path: "", component: ContentComponent, canActivate: [AuthGuardService],
    children: [
      {path: "book/:invnr", component: DetailsComponent},
      {path: "dashboard", component: InventoryComponent},
      {path: "archive", component: ArchivedComponent},
      {path: "personen", component: BorrowerListComponent},
      {path: "benutzer", component: UserComponent},
      {path: "import", component: ImportComponent},
      {path: "", redirectTo: "/dashboard", pathMatch: "full"}
    ]
  },
  {
    path: "", component: AppComponent,
    children: [
      {path: "login", component: LoginComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
