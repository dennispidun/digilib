import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UploadService} from "../upload.service";

@Component({
  selector: 'app-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.scss']
})
export class ImportComponent implements OnInit {
  @ViewChild("fileUpload", {static: false}) fileUpload: ElementRef;
  files = [];
  uploadService: UploadService;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  uploadFile(file) {
    const formData = new FormData();
    formData.append('filekey', file.data);
    file.inProgress = true;
    // this.uploadService.upload(formData); solange der endpoint noch nicht da ist, nur zu Testzwecken
    console.log(this.files[0].data);
  }

  private uploadFiles() {
    this.fileUpload.nativeElement.value = '';
    this.files.forEach(
      file => {
        this.uploadFile(file);
      }
    );
  }


  onClick() {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      for (let index = 0; index < fileUpload.files.length; index++) {
        const file = fileUpload.files[index];
        this.files.push({data: file, inProgress: false, progress: 0});
      }
      this.uploadFiles();
    };
    fileUpload.click();
  }
}
