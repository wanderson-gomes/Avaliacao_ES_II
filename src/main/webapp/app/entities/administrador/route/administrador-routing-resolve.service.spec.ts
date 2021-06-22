jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdministrador, Administrador } from '../administrador.model';
import { AdministradorService } from '../service/administrador.service';

import { AdministradorRoutingResolveService } from './administrador-routing-resolve.service';

describe('Service Tests', () => {
  describe('Administrador routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdministradorRoutingResolveService;
    let service: AdministradorService;
    let resultAdministrador: IAdministrador | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdministradorRoutingResolveService);
      service = TestBed.inject(AdministradorService);
      resultAdministrador = undefined;
    });

    describe('resolve', () => {
      it('should return IAdministrador returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrador = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdministrador).toEqual({ id: 123 });
      });

      it('should return new IAdministrador if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrador = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdministrador).toEqual(new Administrador());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrador = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdministrador).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
