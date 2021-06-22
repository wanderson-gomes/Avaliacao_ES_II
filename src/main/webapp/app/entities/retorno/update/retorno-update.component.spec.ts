jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RetornoService } from '../service/retorno.service';
import { IRetorno, Retorno } from '../retorno.model';
import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';

import { RetornoUpdateComponent } from './retorno-update.component';

describe('Component Tests', () => {
  describe('Retorno Management Update Component', () => {
    let comp: RetornoUpdateComponent;
    let fixture: ComponentFixture<RetornoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let retornoService: RetornoService;
    let medicoService: MedicoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RetornoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RetornoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RetornoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      retornoService = TestBed.inject(RetornoService);
      medicoService = TestBed.inject(MedicoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call medico query and add missing value', () => {
        const retorno: IRetorno = { id: 456 };
        const medico: IMedico = { id: 75776 };
        retorno.medico = medico;

        const medicoCollection: IMedico[] = [{ id: 70084 }];
        spyOn(medicoService, 'query').and.returnValue(of(new HttpResponse({ body: medicoCollection })));
        const expectedCollection: IMedico[] = [medico, ...medicoCollection];
        spyOn(medicoService, 'addMedicoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ retorno });
        comp.ngOnInit();

        expect(medicoService.query).toHaveBeenCalled();
        expect(medicoService.addMedicoToCollectionIfMissing).toHaveBeenCalledWith(medicoCollection, medico);
        expect(comp.medicosCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const retorno: IRetorno = { id: 456 };
        const medico: IMedico = { id: 4836 };
        retorno.medico = medico;

        activatedRoute.data = of({ retorno });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(retorno));
        expect(comp.medicosCollection).toContain(medico);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const retorno = { id: 123 };
        spyOn(retornoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ retorno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: retorno }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(retornoService.update).toHaveBeenCalledWith(retorno);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const retorno = new Retorno();
        spyOn(retornoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ retorno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: retorno }));
        saveSubject.complete();

        // THEN
        expect(retornoService.create).toHaveBeenCalledWith(retorno);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const retorno = { id: 123 };
        spyOn(retornoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ retorno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(retornoService.update).toHaveBeenCalledWith(retorno);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMedicoById', () => {
        it('Should return tracked Medico primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMedicoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
