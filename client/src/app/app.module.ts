import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";

import {AppComponent} from "./app.component";
import {InventoryComponent} from "./inventory/inventory.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from "./app-routing.module";
import {DetailsComponent} from "./details/details.component";

import {NgbButtonLabel, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {BorrowComponent} from "./borrow/borrow.component";
import {ContentComponent} from "./content/content.component";
import {LoginComponent} from "./login/login.component";
import {AuthInterceptor} from "./http.interceptor";

@NgModule({
  declarations: [
    AppComponent,
    InventoryComponent,
    DetailsComponent,
    BorrowComponent,
    ContentComponent,
    LoginComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [NgbButtonLabel,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
