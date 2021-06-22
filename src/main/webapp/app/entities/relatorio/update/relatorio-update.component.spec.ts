jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RelatorioService } from '../service/relatorio.service';
import { IRelatorio, Relatorio } from '../relatorio.model';
import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';

import { RelatorioUpdateComponent } from './relatorio-update.component';

describe('Component Tests', () => {
  describe('Relatorio Management Update Component', () => {
    let comp: RelatorioUpdateComponent;
    let fixture: ComponentFixture<RelatorioUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let relatorioService: RelatorioService;
    let medicoService: MedicoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RelatorioUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RelatorioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RelatorioUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      relatorioService = TestBed.inject(RelatorioService);
      medicoService = TestBed.inject(MedicoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Medico query and add missing value', () => {
        const relatorio: IRelatorio = { id: 456 };
        const medico: IMedico = { id: 72715 };
        relatorio.medico = medico;

        const medicoCollection: IMedico[] = [{ id: 41608 }];
        spyOn(medicoService, 'query').and.returnValue(of(new HttpResponse({ body: medicoCollection })));
        const additionalMedicos = [medico];
        const expectedCollection: IMedico[] = [...additionalMedicos, ...medicoCollection];
        spyOn(medicoService, 'addMedicoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ relatorio });
        comp.ngOnInit();

        expect(medicoService.query).toHaveBeenCalled();
        expect(medicoService.addMedicoToCollectionIfMissing).toHaveBeenCalledWith(medicoCollection, ...additionalMedicos);
        expect(comp.medicosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const relatorio: IRelatorio = { id: 456 };
        const medico: IMedico = { id: 37491 };
        relatorio.medico = medico;

        activatedRoute.data = of({ relatorio });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(relatorio));
        expect(comp.medicosSharedCollection).toContain(medico);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const relatorio = { id: 123 };
        spyOn(relatorioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ relatorio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: relatorio }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(relatorioService.update).toHaveBeenCalledWith(relatorio);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const relatorio = new Relatorio();
        spyOn(relatorioService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ relatorio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: relatorio }));
        saveSubject.complete();

        // THEN
        expect(relatorioService.create).toHaveBeenCalledWith(relatorio);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const relatorio = { id: 123 };
        spyOn(relatorioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ relatorio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(relatorioService.update).toHaveBeenCalledWith(relatorio);
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
