import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlanoDeSaude } from '../plano-de-saude.model';
import { PlanoDeSaudeService } from '../service/plano-de-saude.service';
import { PlanoDeSaudeDeleteDialogComponent } from '../delete/plano-de-saude-delete-dialog.component';

@Component({
  selector: 'jhi-plano-de-saude',
  templateUrl: './plano-de-saude.component.html',
})
export class PlanoDeSaudeComponent implements OnInit {
  planoDeSaudes?: IPlanoDeSaude[];
  isLoading = false;

  constructor(protected planoDeSaudeService: PlanoDeSaudeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.planoDeSaudeService.query().subscribe(
      (res: HttpResponse<IPlanoDeSaude[]>) => {
        this.isLoading = false;
        this.planoDeSaudes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPlanoDeSaude): number {
    return item.id!;
  }

  delete(planoDeSaude: IPlanoDeSaude): void {
    const modalRef = this.modalService.open(PlanoDeSaudeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.planoDeSaude = planoDeSaude;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
