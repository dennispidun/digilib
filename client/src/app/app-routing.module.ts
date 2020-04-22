import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {InventoryComponent} from "./inventory/inventory.component";
import {DetailsComponent} from "./details/details.component";
import {ContentComponent} from "./content/content.component";
import {LoginComponent} from "./login/login.component";
import {AppComponent} from "./app.component";

const routes: Routes = [
  {
    path: "", component: ContentComponent,
    children: [
      {path: "book/:invnr", component: DetailsComponent},
      {path: "dashboard", component: InventoryComponent},
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
