import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdministrador, Administrador } from '../administrador.model';
import { AdministradorService } from '../service/administrador.service';

@Component({
  selector: 'jhi-administrador-update',
  templateUrl: './administrador-update.component.html',
})
export class AdministradorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idAdministrador: [],
    nome: [],
    cpf: [],
    rg: [],
    dataNascimento: [],
    cidade: [],
    bairro: [],
    contato: [],
  });

  constructor(protected administradorService: AdministradorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ administrador }) => {
      this.updateForm(administrador);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const administrador = this.createFromForm();
    if (administrador.id !== undefined) {
      this.subscribeToSaveResponse(this.administradorService.update(administrador));
    } else {
      this.subscribeToSaveResponse(this.administradorService.create(administrador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdministrador>>): void {
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

  protected updateForm(administrador: IAdministrador): void {
    this.editForm.patchValue({
      id: administrador.id,
      idAdministrador: administrador.idAdministrador,
      nome: administrador.nome,
      cpf: administrador.cpf,
      rg: administrador.rg,
      dataNascimento: administrador.dataNascimento,
      cidade: administrador.cidade,
      bairro: administrador.bairro,
      contato: administrador.contato,
    });
  }

  protected createFromForm(): IAdministrador {
    return {
      ...new Administrador(),
      id: this.editForm.get(['id'])!.value,
      idAdministrador: this.editForm.get(['idAdministrador'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      rg: this.editForm.get(['rg'])!.value,
      dataNascimento: this.editForm.get(['dataNascimento'])!.value,
      cidade: this.editForm.get(['cidade'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      contato: this.editForm.get(['contato'])!.value,
    };
  }
}
