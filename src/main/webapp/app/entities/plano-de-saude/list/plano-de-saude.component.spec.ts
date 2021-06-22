import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PlanoDeSaudeService } from '../service/plano-de-saude.service';

import { PlanoDeSaudeComponent } from './plano-de-saude.component';

describe('Component Tests', () => {
  describe('PlanoDeSaude Management Component', () => {
    let comp: PlanoDeSaudeComponent;
    let fixture: ComponentFixture<PlanoDeSaudeComponent>;
    let service: PlanoDeSaudeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlanoDeSaudeComponent],
      })
        .overrideTemplate(PlanoDeSaudeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanoDeSaudeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PlanoDeSaudeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.planoDeSaudes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
