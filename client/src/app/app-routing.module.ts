import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {InventoryComponent} from "./inventory/inventory.component";

const routes: Routes = [
  { path: "**", component: InventoryComponent }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
