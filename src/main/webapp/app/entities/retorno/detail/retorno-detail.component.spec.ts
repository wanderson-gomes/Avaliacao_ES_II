import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RetornoDetailComponent } from './retorno-detail.component';

describe('Component Tests', () => {
  describe('Retorno Management Detail Component', () => {
    let comp: RetornoDetailComponent;
    let fixture: ComponentFixture<RetornoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RetornoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ retorno: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RetornoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RetornoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load retorno on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.retorno).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
