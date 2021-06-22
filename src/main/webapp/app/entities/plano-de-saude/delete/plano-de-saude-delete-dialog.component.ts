import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlanoDeSaude } from '../plano-de-saude.model';
import { PlanoDeSaudeService } from '../service/plano-de-saude.service';

@Component({
  templateUrl: './plano-de-saude-delete-dialog.component.html',
})
export class PlanoDeSaudeDeleteDialogComponent {
  planoDeSaude?: IPlanoDeSaude;

  constructor(protected planoDeSaudeService: PlanoDeSaudeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planoDeSaudeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
