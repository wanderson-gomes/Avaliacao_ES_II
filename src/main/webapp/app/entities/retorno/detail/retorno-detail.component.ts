import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRetorno } from '../retorno.model';

@Component({
  selector: 'jhi-retorno-detail',
  templateUrl: './retorno-detail.component.html',
})
export class RetornoDetailComponent implements OnInit {
  retorno: IRetorno | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retorno }) => {
      this.retorno = retorno;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
