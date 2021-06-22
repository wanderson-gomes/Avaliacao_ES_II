import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RetornoService } from '../service/retorno.service';

import { RetornoComponent } from './retorno.component';

describe('Component Tests', () => {
  describe('Retorno Management Component', () => {
    let comp: RetornoComponent;
    let fixture: ComponentFixture<RetornoComponent>;
    let service: RetornoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RetornoComponent],
      })
        .overrideTemplate(RetornoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RetornoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RetornoService);

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
      expect(comp.retornos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
