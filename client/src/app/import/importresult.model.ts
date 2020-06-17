import {ImportError} from './importerror';

export interface ImportResult {

  successfull: number;
  failed: number;
  emptyLines: number;
  errs: Map<ImportError, object[]>

}
