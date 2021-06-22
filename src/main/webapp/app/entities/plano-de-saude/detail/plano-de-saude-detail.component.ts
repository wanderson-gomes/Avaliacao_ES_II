import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanoDeSaude } from '../plano-de-saude.model';

@Component({
  selector: 'jhi-plano-de-saude-detail',
  templateUrl: './plano-de-saude-detail.component.html',
})
export class PlanoDeSaudeDetailComponent implements OnInit {
  planoDeSaude: IPlanoDeSaude | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planoDeSaude }) => {
      this.planoDeSaude = planoDeSaude;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
