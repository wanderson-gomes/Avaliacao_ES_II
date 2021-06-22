import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlanoDeSaude, PlanoDeSaude } from '../plano-de-saude.model';
import { PlanoDeSaudeService } from '../service/plano-de-saude.service';

@Injectable({ providedIn: 'root' })
export class PlanoDeSaudeRoutingResolveService implements Resolve<IPlanoDeSaude> {
  constructor(protected service: PlanoDeSaudeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlanoDeSaude> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((planoDeSaude: HttpResponse<PlanoDeSaude>) => {
          if (planoDeSaude.body) {
            return of(planoDeSaude.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlanoDeSaude());
  }
}
