import {Book} from "../inventory/book.model";

export interface ImportResult {
  successfull: number;
  failed: number;
  alreadyExist: Book[];
  delimiterErrors: string;
  notEnoughInfo: string;
  emptyLines: number;
  encodingErr: string;
  ioerr: string;
  fileNotFoundErr: string;
  folderEmpty: string;
}
