jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConsultaService } from '../service/consulta.service';
import { IConsulta, Consulta } from '../consulta.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

import { ConsultaUpdateComponent } from './consulta-update.component';

describe('Component Tests', () => {
  describe('Consulta Management Update Component', () => {
    let comp: ConsultaUpdateComponent;
    let fixture: ComponentFixture<ConsultaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let consultaService: ConsultaService;
    let pacienteService: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConsultaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConsultaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConsultaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      consultaService = TestBed.inject(ConsultaService);
      pacienteService = TestBed.inject(PacienteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call paciente query and add missing value', () => {
        const consulta: IConsulta = { id: 456 };
        const paciente: IPaciente = { id: 75022 };
        consulta.paciente = paciente;

        const pacienteCollection: IPaciente[] = [{ id: 55730 }];
        spyOn(pacienteService, 'query').and.returnValue(of(new HttpResponse({ body: pacienteCollection })));
        const expectedCollection: IPaciente[] = [paciente, ...pacienteCollection];
        spyOn(pacienteService, 'addPacienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ consulta });
        comp.ngOnInit();

        expect(pacienteService.query).toHaveBeenCalled();
        expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(pacienteCollection, paciente);
        expect(comp.pacientesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const consulta: IConsulta = { id: 456 };
        const paciente: IPaciente = { id: 44138 };
        consulta.paciente = paciente;

        activatedRoute.data = of({ consulta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(consulta));
        expect(comp.pacientesCollection).toContain(paciente);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consulta = { id: 123 };
        spyOn(consultaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consulta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consulta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(consultaService.update).toHaveBeenCalledWith(consulta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consulta = new Consulta();
        spyOn(consultaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consulta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consulta }));
        saveSubject.complete();

        // THEN
        expect(consultaService.create).toHaveBeenCalledWith(consulta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consulta = { id: 123 };
        spyOn(consultaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consulta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(consultaService.update).toHaveBeenCalledWith(consulta);
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
