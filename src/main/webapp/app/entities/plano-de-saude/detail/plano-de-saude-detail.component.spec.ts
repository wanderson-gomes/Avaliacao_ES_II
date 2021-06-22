import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlanoDeSaudeDetailComponent } from './plano-de-saude-detail.component';

describe('Component Tests', () => {
  describe('PlanoDeSaude Management Detail Component', () => {
    let comp: PlanoDeSaudeDetailComponent;
    let fixture: ComponentFixture<PlanoDeSaudeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PlanoDeSaudeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ planoDeSaude: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PlanoDeSaudeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlanoDeSaudeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load planoDeSaude on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.planoDeSaude).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
