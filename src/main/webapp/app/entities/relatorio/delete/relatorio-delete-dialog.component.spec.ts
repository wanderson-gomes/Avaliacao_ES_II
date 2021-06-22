jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { RelatorioService } from '../service/relatorio.service';

import { RelatorioDeleteDialogComponent } from './relatorio-delete-dialog.component';

describe('Component Tests', () => {
  describe('Relatorio Management Delete Component', () => {
    let comp: RelatorioDeleteDialogComponent;
    let fixture: ComponentFixture<RelatorioDeleteDialogComponent>;
    let service: RelatorioService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RelatorioDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(RelatorioDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RelatorioDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RelatorioService);
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
