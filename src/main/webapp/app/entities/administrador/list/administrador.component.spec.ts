import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AdministradorService } from '../service/administrador.service';

import { AdministradorComponent } from './administrador.component';

describe('Component Tests', () => {
  describe('Administrador Management Component', () => {
    let comp: AdministradorComponent;
    let fixture: ComponentFixture<AdministradorComponent>;
    let service: AdministradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdministradorComponent],
      })
        .overrideTemplate(AdministradorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdministradorComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AdministradorService);

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
      expect(comp.administradors?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
