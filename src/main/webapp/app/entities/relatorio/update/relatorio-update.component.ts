import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRelatorio, Relatorio } from '../relatorio.model';
import { RelatorioService } from '../service/relatorio.service';
import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';

@Component({
  selector: 'jhi-relatorio-update',
  templateUrl: './relatorio-update.component.html',
})
export class RelatorioUpdateComponent implements OnInit {
  isSaving = false;

  medicosSharedCollection: IMedico[] = [];

  editForm = this.fb.group({
    id: [],
    idMedico: [],
    descricao: [],
    diagnostico: [],
    medico: [],
  });

  constructor(
    protected relatorioService: RelatorioService,
    protected medicoService: MedicoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatorio }) => {
      this.updateForm(relatorio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const relatorio = this.createFromForm();
    if (relatorio.id !== undefined) {
      this.subscribeToSaveResponse(this.relatorioService.update(relatorio));
    } else {
      this.subscribeToSaveResponse(this.relatorioService.create(relatorio));
    }
  }

  trackMedicoById(index: number, item: IMedico): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelatorio>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(relatorio: IRelatorio): void {
    this.editForm.patchValue({
      id: relatorio.id,
      idMedico: relatorio.idMedico,
      descricao: relatorio.descricao,
      diagnostico: relatorio.diagnostico,
      medico: relatorio.medico,
    });

    this.medicosSharedCollection = this.medicoService.addMedicoToCollectionIfMissing(this.medicosSharedCollection, relatorio.medico);
  }

  protected loadRelationshipsOptions(): void {
    this.medicoService
      .query()
      .pipe(map((res: HttpResponse<IMedico[]>) => res.body ?? []))
      .pipe(map((medicos: IMedico[]) => this.medicoService.addMedicoToCollectionIfMissing(medicos, this.editForm.get('medico')!.value)))
      .subscribe((medicos: IMedico[]) => (this.medicosSharedCollection = medicos));
  }

  protected createFromForm(): IRelatorio {
    return {
      ...new Relatorio(),
      id: this.editForm.get(['id'])!.value,
      idMedico: this.editForm.get(['idMedico'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      diagnostico: this.editForm.get(['diagnostico'])!.value,
      medico: this.editForm.get(['medico'])!.value,
    };
  }
}
