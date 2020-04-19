import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { AppComponent } from "./app.component";
import { InventoryComponent } from "./inventory/inventory.component";
import { HttpClientModule } from "@angular/common/http";
import { AppRoutingModule } from "./app-routing.module";
import { DetailsComponent } from "./details/details.component";

import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { BorrowComponent } from "./borrow/borrow.component";

@NgModule({
  declarations: [
    AppComponent,
    InventoryComponent,
    DetailsComponent,
    BorrowComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
