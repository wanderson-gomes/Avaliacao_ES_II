import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRelatorio, Relatorio } from '../relatorio.model';
import { RelatorioService } from '../service/relatorio.service';

@Injectable({ providedIn: 'root' })
export class RelatorioRoutingResolveService implements Resolve<IRelatorio> {
  constructor(protected service: RelatorioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRelatorio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((relatorio: HttpResponse<Relatorio>) => {
          if (relatorio.body) {
            return of(relatorio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Relatorio());
  }
}
