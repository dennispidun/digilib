import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEventType} from "@angular/common/http";
import {UploadService} from "../upload.service";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {catchError, map} from "rxjs/operators";
import {of} from "rxjs"
import {ImportResult} from "./importresult.model";
import {ImportError} from "./importerror";
import {Book} from "../inventory/book.model";

@Component({
  selector: 'app-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.scss']
})
export class ImportComponent implements OnInit {
  @ViewChild("fileUpload", {static: false}) fileUpload: ElementRef;
  files = [];
  delimiter: string;

  columns = [
    'Author',
    'Titel',
    'Inventur-Nummer',
    'Buchart',
    'Neupreis',
    'nicht anwesend'
  ];

  pos = [0, 1, 2, 3, 4, 5];
  path: string;
  progress: number;
  result: string;
  report: ImportResult;

  constructor(private http: HttpClient, private uploadService: UploadService) {
  }

  ngOnInit(): void {
    this.delimiter = "|";
    this.path = "./importfolder/";
    this.result = "";
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.columns, event.previousIndex, event.currentIndex);
    moveItemInArray(this.pos, event.previousIndex, event.currentIndex);
  }

  createData(file): FormData {
    const formData = new FormData();
    formData.append("delimiter", this.delimiter);
    formData.append("pos", this.pos.toString().replace(/,/g, ''));
    if (file === null) {
      formData.append("path", this.path);
    } else {
      file.inProgress = true;
      formData.append("file", file.data);
      file.inProgress = false;
    }
    return formData
  }

  printReport() {
    this.result += new Date().toLocaleString() + "\n";
    if (this.report.successfull > 0) {
      this.result += "Es wurde" + (this.report.successfull > 1 ? "n " + this.report.successfull + " Bücher" : " ein Buch")
        + " korrekt importiert.\n";
    }
    if (this.report.failed > 0) {
      this.result += "Es konnte" + (this.report.failed > 1 ? "n " + this.report.failed + " Bücher" : " ein Buch")
        + " nicht korrekt importiert werden.\n";
    }
    if (this.report.emptyLines > 0) {
      this.result += (this.report.emptyLines > 1 ? this.report.emptyLines + " Zeilen" : "Eine Zeile") + " wurde als leer interpretiert.\n";
    }
    for (let [key, value] of Object.entries(this.report.errs)) {
      const arr: object[] = value as object[];
      switch (key) {
        case "ALREADYEX":
          this.result += (arr.length > 1 ? arr.length + " Bücher existieren" : "Ein Buch existiert") + " bereits:\n";
          arr.forEach((element: Book) => {
            this.result += "#" + element.invnr + " von " + element.author + " mit dem Titel \"" + element.title + "\"\n";
          })
          break;
        case "DELIMITERERR":
          arr.forEach((element) => {
            this.result += element + "\n";
          })
      }
    }
  }

  uploadFile(file) {
    this.uploadService.upload(this.createData(file)).pipe(
      map(event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(event.loaded * 100 / event.total);
        } else if (event.type === HttpEventType.Response) {
          console.log(event.body);
          this.report = event.body as ImportResult;
          console.log(this.report);
          this.printReport()
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.log(error.error);
        this.report = error.error as ImportResult;
        console.log(this.report);
        this.printReport()
        return of(`${file.data.name} upload failed.`);
      })).subscribe();
  }

  startUpload() {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      this.progress = 0;
      for (const file of fileUpload.files) {
        this.uploadFile({data: file, inProgress: false, progress: 0});
      }
    };
    fileUpload.click();
  }

  startLocal() {
    this.uploadFile(null);
  }
}
