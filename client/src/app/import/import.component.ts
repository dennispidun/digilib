import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEventType} from "@angular/common/http";
import {UploadService} from "../upload.service";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {catchError, map} from "rxjs/operators";
import {of} from "rxjs"

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

  uploadFile(file) {
    this.uploadService.upload(this.createData(file)).pipe(
      map(event => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            this.progress = Math.round(event.loaded * 100 / event.total);
            this.result += "Fortschritt: " + this.progress + "\n";
            return { status: 'progress', message: this.progress };
          case HttpEventType.Response:
            this.result += "Server-Antwort: " + event.body + "\n";
            console.log(event);
            return event;
          case HttpEventType.Sent:
            this.result += "Die Datei wurde abgeschickt.\n";
            break;
          default:
            console.log(event);
            this.result += "Folgender unbehandelter Fehler ist aufgetreten: " + event.type + "\n";
            return `Unhandled event: ${event.type}`;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.log(error);
        this.result += "Beim Upload ist etwas schiefgegangen. " + error.status + " " + error.message;
        return of(`${file.data.name} upload failed.`);
      })).subscribe((event: any) => {
      if (typeof (event) === 'object') {
        console.log(event);
        this.result += "Event: " + event.body + "\n";
      }
    }, (error => {
      console.log(error);
    }));
  }

  startUpload() {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      this.progress = 0;
      for (const file of fileUpload.files) {
        console.log("upload: " + this.uploadFile({data: file, inProgress: false, progress: 0}));
      }
    };
    fileUpload.click();
  }

  startLocal() {
    console.log("local import: " + this.uploadFile(null));
  }
}
