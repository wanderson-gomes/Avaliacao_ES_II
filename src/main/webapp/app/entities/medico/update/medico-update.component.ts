import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedico, Medico } from '../medico.model';
import { MedicoService } from '../service/medico.service';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

@Component({
  selector: 'jhi-medico-update',
  templateUrl: './medico-update.component.html',
})
export class MedicoUpdateComponent implements OnInit {
  isSaving = false;

  administradorsSharedCollection: IAdministrador[] = [];

  editForm = this.fb.group({
    id: [],
    idMedico: [],
    nome: [],
    crm: [],
    cpf: [],
    rg: [],
    especialidade: [],
    contato: [],
    administrador: [],
  });

  constructor(
    protected medicoService: MedicoService,
    protected administradorService: AdministradorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medico }) => {
      this.updateForm(medico);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medico = this.createFromForm();
    if (medico.id !== undefined) {
      this.subscribeToSaveResponse(this.medicoService.update(medico));
    } else {
      this.subscribeToSaveResponse(this.medicoService.create(medico));
    }
  }

  trackAdministradorById(index: number, item: IAdministrador): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedico>>): void {
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

  protected updateForm(medico: IMedico): void {
    this.editForm.patchValue({
      id: medico.id,
      idMedico: medico.idMedico,
      nome: medico.nome,
      crm: medico.crm,
      cpf: medico.cpf,
      rg: medico.rg,
      especialidade: medico.especialidade,
      contato: medico.contato,
      administrador: medico.administrador,
    });

    this.administradorsSharedCollection = this.administradorService.addAdministradorToCollectionIfMissing(
      this.administradorsSharedCollection,
      medico.administrador
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

  protected createFromForm(): IMedico {
    return {
      ...new Medico(),
      id: this.editForm.get(['id'])!.value,
      idMedico: this.editForm.get(['idMedico'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      crm: this.editForm.get(['crm'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      rg: this.editForm.get(['rg'])!.value,
      especialidade: this.editForm.get(['especialidade'])!.value,
      contato: this.editForm.get(['contato'])!.value,
      administrador: this.editForm.get(['administrador'])!.value,
    };
  }
}
