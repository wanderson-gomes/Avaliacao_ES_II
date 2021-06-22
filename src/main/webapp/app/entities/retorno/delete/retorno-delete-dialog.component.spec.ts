jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { RetornoService } from '../service/retorno.service';

import { RetornoDeleteDialogComponent } from './retorno-delete-dialog.component';

describe('Component Tests', () => {
  describe('Retorno Management Delete Component', () => {
    let comp: RetornoDeleteDialogComponent;
    let fixture: ComponentFixture<RetornoDeleteDialogComponent>;
    let service: RetornoService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RetornoDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(RetornoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RetornoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RetornoService);
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
