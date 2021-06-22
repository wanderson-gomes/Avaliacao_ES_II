import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConsulta, Consulta } from '../consulta.model';
import { ConsultaService } from '../service/consulta.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-consulta-update',
  templateUrl: './consulta-update.component.html',
})
export class ConsultaUpdateComponent implements OnInit {
  isSaving = false;

  pacientesCollection: IPaciente[] = [];

  editForm = this.fb.group({
    id: [],
    idPaciente: [],
    dataConsulta: [],
    horario: [],
    paciente: [],
  });

  constructor(
    protected consultaService: ConsultaService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consulta }) => {
      this.updateForm(consulta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consulta = this.createFromForm();
    if (consulta.id !== undefined) {
      this.subscribeToSaveResponse(this.consultaService.update(consulta));
    } else {
      this.subscribeToSaveResponse(this.consultaService.create(consulta));
    }
  }

  trackPacienteById(index: number, item: IPaciente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsulta>>): void {
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

  protected updateForm(consulta: IConsulta): void {
    this.editForm.patchValue({
      id: consulta.id,
      idPaciente: consulta.idPaciente,
      dataConsulta: consulta.dataConsulta,
      horario: consulta.horario,
      paciente: consulta.paciente,
    });

    this.pacientesCollection = this.pacienteService.addPacienteToCollectionIfMissing(this.pacientesCollection, consulta.paciente);
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query({ filter: 'consulta-is-null' })
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing(pacientes, this.editForm.get('paciente')!.value)
        )
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesCollection = pacientes));
  }

  protected createFromForm(): IConsulta {
    return {
      ...new Consulta(),
      id: this.editForm.get(['id'])!.value,
      idPaciente: this.editForm.get(['idPaciente'])!.value,
      dataConsulta: this.editForm.get(['dataConsulta'])!.value,
      horario: this.editForm.get(['horario'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
    };
  }
}
