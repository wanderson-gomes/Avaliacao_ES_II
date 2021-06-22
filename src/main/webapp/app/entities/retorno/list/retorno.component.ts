import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRetorno } from '../retorno.model';
import { RetornoService } from '../service/retorno.service';
import { RetornoDeleteDialogComponent } from '../delete/retorno-delete-dialog.component';

@Component({
  selector: 'jhi-retorno',
  templateUrl: './retorno.component.html',
})
export class RetornoComponent implements OnInit {
  retornos?: IRetorno[];
  isLoading = false;

  constructor(protected retornoService: RetornoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.retornoService.query().subscribe(
      (res: HttpResponse<IRetorno[]>) => {
        this.isLoading = false;
        this.retornos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRetorno): number {
    return item.id!;
  }

  delete(retorno: IRetorno): void {
    const modalRef = this.modalService.open(RetornoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.retorno = retorno;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
