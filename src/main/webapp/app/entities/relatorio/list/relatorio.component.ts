import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelatorio } from '../relatorio.model';
import { RelatorioService } from '../service/relatorio.service';
import { RelatorioDeleteDialogComponent } from '../delete/relatorio-delete-dialog.component';

@Component({
  selector: 'jhi-relatorio',
  templateUrl: './relatorio.component.html',
})
export class RelatorioComponent implements OnInit {
  relatorios?: IRelatorio[];
  isLoading = false;

  constructor(protected relatorioService: RelatorioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.relatorioService.query().subscribe(
      (res: HttpResponse<IRelatorio[]>) => {
        this.isLoading = false;
        this.relatorios = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRelatorio): number {
    return item.id!;
  }

  delete(relatorio: IRelatorio): void {
    const modalRef = this.modalService.open(RelatorioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.relatorio = relatorio;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
