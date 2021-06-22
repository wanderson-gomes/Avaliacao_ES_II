import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlanoDeSaude, PlanoDeSaude } from '../plano-de-saude.model';
import { PlanoDeSaudeService } from '../service/plano-de-saude.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-plano-de-saude-update',
  templateUrl: './plano-de-saude-update.component.html',
})
export class PlanoDeSaudeUpdateComponent implements OnInit {
  isSaving = false;

  pacientesSharedCollection: IPaciente[] = [];

  editForm = this.fb.group({
    id: [],
    idPlano: [],
    nomePlano: [],
    ativo: [],
    cnpj: [],
    formaDePag: [],
    paciente: [],
  });

  constructor(
    protected planoDeSaudeService: PlanoDeSaudeService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planoDeSaude }) => {
      this.updateForm(planoDeSaude);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planoDeSaude = this.createFromForm();
    if (planoDeSaude.id !== undefined) {
      this.subscribeToSaveResponse(this.planoDeSaudeService.update(planoDeSaude));
    } else {
      this.subscribeToSaveResponse(this.planoDeSaudeService.create(planoDeSaude));
    }
  }

  trackPacienteById(index: number, item: IPaciente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanoDeSaude>>): void {
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

  protected updateForm(planoDeSaude: IPlanoDeSaude): void {
    this.editForm.patchValue({
      id: planoDeSaude.id,
      idPlano: planoDeSaude.idPlano,
      nomePlano: planoDeSaude.nomePlano,
      ativo: planoDeSaude.ativo,
      cnpj: planoDeSaude.cnpj,
      formaDePag: planoDeSaude.formaDePag,
      paciente: planoDeSaude.paciente,
    });

    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing(
      this.pacientesSharedCollection,
      planoDeSaude.paciente
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing(pacientes, this.editForm.get('paciente')!.value)
        )
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));
  }

  protected createFromForm(): IPlanoDeSaude {
    return {
      ...new PlanoDeSaude(),
      id: this.editForm.get(['id'])!.value,
      idPlano: this.editForm.get(['idPlano'])!.value,
      nomePlano: this.editForm.get(['nomePlano'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      cnpj: this.editForm.get(['cnpj'])!.value,
      formaDePag: this.editForm.get(['formaDePag'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
    };
  }
}
