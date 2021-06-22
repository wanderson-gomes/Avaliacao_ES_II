jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRetorno, Retorno } from '../retorno.model';
import { RetornoService } from '../service/retorno.service';

import { RetornoRoutingResolveService } from './retorno-routing-resolve.service';

describe('Service Tests', () => {
  describe('Retorno routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RetornoRoutingResolveService;
    let service: RetornoService;
    let resultRetorno: IRetorno | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RetornoRoutingResolveService);
      service = TestBed.inject(RetornoService);
      resultRetorno = undefined;
    });

    describe('resolve', () => {
      it('should return IRetorno returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRetorno = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRetorno).toEqual({ id: 123 });
      });

      it('should return new IRetorno if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRetorno = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRetorno).toEqual(new Retorno());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRetorno = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRetorno).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
