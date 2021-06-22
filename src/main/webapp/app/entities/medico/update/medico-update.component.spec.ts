jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MedicoService } from '../service/medico.service';
import { IMedico, Medico } from '../medico.model';
import { IAdministrador } from 'app/entities/administrador/administrador.model';
import { AdministradorService } from 'app/entities/administrador/service/administrador.service';

import { MedicoUpdateComponent } from './medico-update.component';

describe('Component Tests', () => {
  describe('Medico Management Update Component', () => {
    let comp: MedicoUpdateComponent;
    let fixture: ComponentFixture<MedicoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let medicoService: MedicoService;
    let administradorService: AdministradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MedicoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      medicoService = TestBed.inject(MedicoService);
      administradorService = TestBed.inject(AdministradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Administrador query and add missing value', () => {
        const medico: IMedico = { id: 456 };
        const administrador: IAdministrador = { id: 56433 };
        medico.administrador = administrador;

        const administradorCollection: IAdministrador[] = [{ id: 38176 }];
        spyOn(administradorService, 'query').and.returnValue(of(new HttpResponse({ body: administradorCollection })));
        const additionalAdministradors = [administrador];
        const expectedCollection: IAdministrador[] = [...additionalAdministradors, ...administradorCollection];
        spyOn(administradorService, 'addAdministradorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medico });
        comp.ngOnInit();

        expect(administradorService.query).toHaveBeenCalled();
        expect(administradorService.addAdministradorToCollectionIfMissing).toHaveBeenCalledWith(
          administradorCollection,
          ...additionalAdministradors
        );
        expect(comp.administradorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const medico: IMedico = { id: 456 };
        const administrador: IAdministrador = { id: 48273 };
        medico.administrador = administrador;

        activatedRoute.data = of({ medico });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(medico));
        expect(comp.administradorsSharedCollection).toContain(administrador);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medico = { id: 123 };
        spyOn(medicoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medico });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medico }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(medicoService.update).toHaveBeenCalledWith(medico);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medico = new Medico();
        spyOn(medicoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medico });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medico }));
        saveSubject.complete();

        // THEN
        expect(medicoService.create).toHaveBeenCalledWith(medico);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medico = { id: 123 };
        spyOn(medicoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medico });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(medicoService.update).toHaveBeenCalledWith(medico);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAdministradorById', () => {
        it('Should return tracked Administrador primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAdministradorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
