import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRelatorio } from '../relatorio.model';

@Component({
  selector: 'jhi-relatorio-detail',
  templateUrl: './relatorio-detail.component.html',
})
export class RelatorioDetailComponent implements OnInit {
  relatorio: IRelatorio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatorio }) => {
      this.relatorio = relatorio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
