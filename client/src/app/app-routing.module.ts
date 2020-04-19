import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {InventoryComponent} from "./inventory/inventory.component";
import {DetailsComponent} from "./details/details.component";

const routes: Routes = [
  {
    path: "book/:invnr",
    component: DetailsComponent
  },
  {
    path: "", component: InventoryComponent
  }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
