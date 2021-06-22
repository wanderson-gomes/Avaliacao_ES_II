import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RelatorioDetailComponent } from './relatorio-detail.component';

describe('Component Tests', () => {
  describe('Relatorio Management Detail Component', () => {
    let comp: RelatorioDetailComponent;
    let fixture: ComponentFixture<RelatorioDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RelatorioDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ relatorio: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RelatorioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RelatorioDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load relatorio on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.relatorio).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
