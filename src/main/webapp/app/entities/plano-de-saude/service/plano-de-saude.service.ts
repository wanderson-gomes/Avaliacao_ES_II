import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlanoDeSaude, getPlanoDeSaudeIdentifier } from '../plano-de-saude.model';

export type EntityResponseType = HttpResponse<IPlanoDeSaude>;
export type EntityArrayResponseType = HttpResponse<IPlanoDeSaude[]>;

@Injectable({ providedIn: 'root' })
export class PlanoDeSaudeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/plano-de-saudes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(planoDeSaude: IPlanoDeSaude): Observable<EntityResponseType> {
    return this.http.post<IPlanoDeSaude>(this.resourceUrl, planoDeSaude, { observe: 'response' });
  }

  update(planoDeSaude: IPlanoDeSaude): Observable<EntityResponseType> {
    return this.http.put<IPlanoDeSaude>(`${this.resourceUrl}/${getPlanoDeSaudeIdentifier(planoDeSaude) as number}`, planoDeSaude, {
      observe: 'response',
    });
  }

  partialUpdate(planoDeSaude: IPlanoDeSaude): Observable<EntityResponseType> {
    return this.http.patch<IPlanoDeSaude>(`${this.resourceUrl}/${getPlanoDeSaudeIdentifier(planoDeSaude) as number}`, planoDeSaude, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlanoDeSaude>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlanoDeSaude[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlanoDeSaudeToCollectionIfMissing(
    planoDeSaudeCollection: IPlanoDeSaude[],
    ...planoDeSaudesToCheck: (IPlanoDeSaude | null | undefined)[]
  ): IPlanoDeSaude[] {
    const planoDeSaudes: IPlanoDeSaude[] = planoDeSaudesToCheck.filter(isPresent);
    if (planoDeSaudes.length > 0) {
      const planoDeSaudeCollectionIdentifiers = planoDeSaudeCollection.map(
        planoDeSaudeItem => getPlanoDeSaudeIdentifier(planoDeSaudeItem)!
      );
      const planoDeSaudesToAdd = planoDeSaudes.filter(planoDeSaudeItem => {
        const planoDeSaudeIdentifier = getPlanoDeSaudeIdentifier(planoDeSaudeItem);
        if (planoDeSaudeIdentifier == null || planoDeSaudeCollectionIdentifiers.includes(planoDeSaudeIdentifier)) {
          return false;
        }
        planoDeSaudeCollectionIdentifiers.push(planoDeSaudeIdentifier);
        return true;
      });
      return [...planoDeSaudesToAdd, ...planoDeSaudeCollection];
    }
    return planoDeSaudeCollection;
  }
}
