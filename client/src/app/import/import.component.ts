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
    this.path = "/importfolder/";
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
      formData.append("file", file.data);
    }
    return formData
  }

  uploadFile(file) {
    file.inProgress = true;
    this.uploadService.upload(this.createData(file)).pipe(
      map(event => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            this.progress = Math.round(event.loaded * 100 / event.total);
            return { status: 'progress', message: this.progress };
          case HttpEventType.Response:
            return event;
          case HttpEventType.Sent:
            this.result += "Die Datei wurde abgeschickt.\n";
            break;
          default:
            this.result += "Folgender unbehandelter Fehler ist aufgetreten: " + event.type + "\n";
            return `Unhandled event: ${event.type}`;
        }
      }),
      catchError((error: HttpErrorResponse) => {
        this.result += "Beim Upload ist etwas schiefgegangen." + error.status + " " + error.error + " " + error.message;
        return of(`${file.data.name} upload failed.`);
      })).subscribe((event: any) => {
      if (typeof (event) === 'object') {
        this.result += "Event: " + event.body + "\n";
      }
    });
    file.inProgress = false;
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
    this.uploadService.upload(this.createData(null)).subscribe();
  }
}
