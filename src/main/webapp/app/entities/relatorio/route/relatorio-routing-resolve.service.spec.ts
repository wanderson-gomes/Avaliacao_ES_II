jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRelatorio, Relatorio } from '../relatorio.model';
import { RelatorioService } from '../service/relatorio.service';

import { RelatorioRoutingResolveService } from './relatorio-routing-resolve.service';

describe('Service Tests', () => {
  describe('Relatorio routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RelatorioRoutingResolveService;
    let service: RelatorioService;
    let resultRelatorio: IRelatorio | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RelatorioRoutingResolveService);
      service = TestBed.inject(RelatorioService);
      resultRelatorio = undefined;
    });

    describe('resolve', () => {
      it('should return IRelatorio returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelatorio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRelatorio).toEqual({ id: 123 });
      });

      it('should return new IRelatorio if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelatorio = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRelatorio).toEqual(new Relatorio());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRelatorio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRelatorio).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
