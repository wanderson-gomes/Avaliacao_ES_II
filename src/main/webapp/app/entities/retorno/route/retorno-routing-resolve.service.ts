import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRetorno, Retorno } from '../retorno.model';
import { RetornoService } from '../service/retorno.service';

@Injectable({ providedIn: 'root' })
export class RetornoRoutingResolveService implements Resolve<IRetorno> {
  constructor(protected service: RetornoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRetorno> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((retorno: HttpResponse<Retorno>) => {
          if (retorno.body) {
            return of(retorno.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Retorno());
  }
}
