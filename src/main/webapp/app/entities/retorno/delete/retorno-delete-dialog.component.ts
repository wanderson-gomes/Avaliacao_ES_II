import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetorno } from '../retorno.model';
import { RetornoService } from '../service/retorno.service';

@Component({
  templateUrl: './retorno-delete-dialog.component.html',
})
export class RetornoDeleteDialogComponent {
  retorno?: IRetorno;

  constructor(protected retornoService: RetornoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.retornoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
