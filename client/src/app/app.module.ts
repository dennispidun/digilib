import {BrowserModule} from "@angular/platform-browser";
import {Injectable, NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";

import {AppComponent} from "./app.component";
import {InventoryComponent} from "./inventory/inventory.component";
import {ArchivedComponent} from "./archived/archived.component";
import {HTTP_INTERCEPTORS, HttpClientModule, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {AppRoutingModule} from "./app-routing.module";
import {DetailsComponent} from "./details/details.component";

import {NgbButtonLabel, NgbModule, NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {BorrowComponent} from "./borrow/borrow.component";
import {ContentComponent} from "./content/content.component";
import {LoginComponent} from "./login/login.component";
import {BorrowerListComponent} from "./borrower-list/borrower-list.component";
import {AuthInterceptor} from "./http.interceptor";
import {ImportComponent} from "./import/import.component";
import {AddBookComponent} from './add-book/add-book.component';
import {UserComponent} from './user/user.component';
import {UserDetailsComponent} from './user-details/user-details.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {EditBookComponent} from './edit-book/edit-book.component';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set("X-Requested-With", "XMLHttpRequest")
    });
    return next.handle(xhr);
  }
}

@NgModule({
  declarations: [
    AppComponent,
    InventoryComponent,
    ArchivedComponent,
    DetailsComponent,
    BorrowComponent,
    ContentComponent,
    LoginComponent,
    BorrowerListComponent,
    AddBookComponent,
    ImportComponent,
    UserComponent,
    UserDetailsComponent,
    EditBookComponent
  ],
  entryComponents: [
    BorrowComponent,
    UserDetailsComponent,
    AddBookComponent,
    EditBookComponent
  ],
    imports: [
        NgbModule,
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        NgbTypeaheadModule,
        DragDropModule
    ],
  providers: [NgbButtonLabel,
    {provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
