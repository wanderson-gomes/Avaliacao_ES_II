import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelatorio } from '../relatorio.model';
import { RelatorioService } from '../service/relatorio.service';

@Component({
  templateUrl: './relatorio-delete-dialog.component.html',
})
export class RelatorioDeleteDialogComponent {
  relatorio?: IRelatorio;

  constructor(protected relatorioService: RelatorioService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.relatorioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
