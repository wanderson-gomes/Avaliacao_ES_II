jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PacienteService } from '../service/paciente.service';

import { PacienteDeleteDialogComponent } from './paciente-delete-dialog.component';

describe('Component Tests', () => {
  describe('Paciente Management Delete Component', () => {
    let comp: PacienteDeleteDialogComponent;
    let fixture: ComponentFixture<PacienteDeleteDialogComponent>;
    let service: PacienteService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PacienteDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PacienteDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PacienteDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PacienteService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
