import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaciente, Paciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;

  administradorsSharedCollection: IAdministrador[] = [];

  editForm = this.fb.group({
    id: [],
    idUsuario: [],
    nome: [],
    cpf: [],
    rg: [],
    dataNascimento: [],
    cidade: [],
    bairro: [],
    contato: [],
    administrador: [],
  });

  constructor(
    protected pacienteService: PacienteService,
    protected administradorService: AdministradorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.updateForm(paciente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.createFromForm();
    if (paciente.id !== undefined) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  trackAdministradorById(index: number, item: IAdministrador): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
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

  protected updateForm(paciente: IPaciente): void {
    this.editForm.patchValue({
      id: paciente.id,
      idUsuario: paciente.idUsuario,
      nome: paciente.nome,
      cpf: paciente.cpf,
      rg: paciente.rg,
      dataNascimento: paciente.dataNascimento,
      cidade: paciente.cidade,
      bairro: paciente.bairro,
      contato: paciente.contato,
      administrador: paciente.administrador,
    });

    this.administradorsSharedCollection = this.administradorService.addAdministradorToCollectionIfMissing(
      this.administradorsSharedCollection,
      paciente.administrador
    );
  }

  protected loadRelationshipsOptions(): void {
    this.administradorService
      .query()
      .pipe(map((res: HttpResponse<IAdministrador[]>) => res.body ?? []))
      .pipe(
        map((administradors: IAdministrador[]) =>
          this.administradorService.addAdministradorToCollectionIfMissing(administradors, this.editForm.get('administrador')!.value)
        )
      )
      .subscribe((administradors: IAdministrador[]) => (this.administradorsSharedCollection = administradors));
  }

  protected createFromForm(): IPaciente {
    return {
      ...new Paciente(),
      id: this.editForm.get(['id'])!.value,
      idUsuario: this.editForm.get(['idUsuario'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      rg: this.editForm.get(['rg'])!.value,
      dataNascimento: this.editForm.get(['dataNascimento'])!.value,
      cidade: this.editForm.get(['cidade'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      contato: this.editForm.get(['contato'])!.value,
      administrador: this.editForm.get(['administrador'])!.value,
    };
  }
}
