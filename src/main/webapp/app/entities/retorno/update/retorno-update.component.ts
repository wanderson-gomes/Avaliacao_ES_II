import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRetorno, Retorno } from '../retorno.model';
import { RetornoService } from '../service/retorno.service';
import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';

@Component({
  selector: 'jhi-retorno-update',
  templateUrl: './retorno-update.component.html',
})
export class RetornoUpdateComponent implements OnInit {
  isSaving = false;

  medicosCollection: IMedico[] = [];

  editForm = this.fb.group({
    id: [],
    idMedico: [],
    idPaciente: [],
    dataRetorno: [],
    medico: [],
  });

  constructor(
    protected retornoService: RetornoService,
    protected medicoService: MedicoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retorno }) => {
      this.updateForm(retorno);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retorno = this.createFromForm();
    if (retorno.id !== undefined) {
      this.subscribeToSaveResponse(this.retornoService.update(retorno));
    } else {
      this.subscribeToSaveResponse(this.retornoService.create(retorno));
    }
  }

  trackMedicoById(index: number, item: IMedico): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetorno>>): void {
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

  protected updateForm(retorno: IRetorno): void {
    this.editForm.patchValue({
      id: retorno.id,
      idMedico: retorno.idMedico,
      idPaciente: retorno.idPaciente,
      dataRetorno: retorno.dataRetorno,
      medico: retorno.medico,
    });

    this.medicosCollection = this.medicoService.addMedicoToCollectionIfMissing(this.medicosCollection, retorno.medico);
  }

  protected loadRelationshipsOptions(): void {
    this.medicoService
      .query({ filter: 'retorno-is-null' })
      .pipe(map((res: HttpResponse<IMedico[]>) => res.body ?? []))
      .pipe(map((medicos: IMedico[]) => this.medicoService.addMedicoToCollectionIfMissing(medicos, this.editForm.get('medico')!.value)))
      .subscribe((medicos: IMedico[]) => (this.medicosCollection = medicos));
  }

  protected createFromForm(): IRetorno {
    return {
      ...new Retorno(),
      id: this.editForm.get(['id'])!.value,
      idMedico: this.editForm.get(['idMedico'])!.value,
      idPaciente: this.editForm.get(['idPaciente'])!.value,
      dataRetorno: this.editForm.get(['dataRetorno'])!.value,
      medico: this.editForm.get(['medico'])!.value,
    };
  }
}
