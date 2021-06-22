jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlanoDeSaudeService } from '../service/plano-de-saude.service';
import { IPlanoDeSaude, PlanoDeSaude } from '../plano-de-saude.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

import { PlanoDeSaudeUpdateComponent } from './plano-de-saude-update.component';

describe('Component Tests', () => {
  describe('PlanoDeSaude Management Update Component', () => {
    let comp: PlanoDeSaudeUpdateComponent;
    let fixture: ComponentFixture<PlanoDeSaudeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let planoDeSaudeService: PlanoDeSaudeService;
    let pacienteService: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlanoDeSaudeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlanoDeSaudeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanoDeSaudeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      planoDeSaudeService = TestBed.inject(PlanoDeSaudeService);
      pacienteService = TestBed.inject(PacienteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Paciente query and add missing value', () => {
        const planoDeSaude: IPlanoDeSaude = { id: 456 };
        const paciente: IPaciente = { id: 98586 };
        planoDeSaude.paciente = paciente;

        const pacienteCollection: IPaciente[] = [{ id: 11606 }];
        spyOn(pacienteService, 'query').and.returnValue(of(new HttpResponse({ body: pacienteCollection })));
        const additionalPacientes = [paciente];
        const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
        spyOn(pacienteService, 'addPacienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ planoDeSaude });
        comp.ngOnInit();

        expect(pacienteService.query).toHaveBeenCalled();
        expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(pacienteCollection, ...additionalPacientes);
        expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const planoDeSaude: IPlanoDeSaude = { id: 456 };
        const paciente: IPaciente = { id: 30458 };
        planoDeSaude.paciente = paciente;

        activatedRoute.data = of({ planoDeSaude });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(planoDeSaude));
        expect(comp.pacientesSharedCollection).toContain(paciente);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const planoDeSaude = { id: 123 };
        spyOn(planoDeSaudeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ planoDeSaude });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: planoDeSaude }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(planoDeSaudeService.update).toHaveBeenCalledWith(planoDeSaude);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const planoDeSaude = new PlanoDeSaude();
        spyOn(planoDeSaudeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ planoDeSaude });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: planoDeSaude }));
        saveSubject.complete();

        // THEN
        expect(planoDeSaudeService.create).toHaveBeenCalledWith(planoDeSaude);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const planoDeSaude = { id: 123 };
        spyOn(planoDeSaudeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ planoDeSaude });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(planoDeSaudeService.update).toHaveBeenCalledWith(planoDeSaude);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPacienteById', () => {
        it('Should return tracked Paciente primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPacienteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
