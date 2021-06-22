jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdministradorService } from '../service/administrador.service';
import { IAdministrador, Administrador } from '../administrador.model';

import { AdministradorUpdateComponent } from './administrador-update.component';

describe('Component Tests', () => {
  describe('Administrador Management Update Component', () => {
    let comp: AdministradorUpdateComponent;
    let fixture: ComponentFixture<AdministradorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let administradorService: AdministradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdministradorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdministradorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdministradorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      administradorService = TestBed.inject(AdministradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const administrador: IAdministrador = { id: 456 };

        activatedRoute.data = of({ administrador });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(administrador));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const administrador = { id: 123 };
        spyOn(administradorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ administrador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: administrador }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(administradorService.update).toHaveBeenCalledWith(administrador);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const administrador = new Administrador();
        spyOn(administradorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ administrador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: administrador }));
        saveSubject.complete();

        // THEN
        expect(administradorService.create).toHaveBeenCalledWith(administrador);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const administrador = { id: 123 };
        spyOn(administradorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ administrador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(administradorService.update).toHaveBeenCalledWith(administrador);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
