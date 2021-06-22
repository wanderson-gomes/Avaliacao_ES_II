import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRelatorio, getRelatorioIdentifier } from '../relatorio.model';

export type EntityResponseType = HttpResponse<IRelatorio>;
export type EntityArrayResponseType = HttpResponse<IRelatorio[]>;

@Injectable({ providedIn: 'root' })
export class RelatorioService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/relatorios');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(relatorio: IRelatorio): Observable<EntityResponseType> {
    return this.http.post<IRelatorio>(this.resourceUrl, relatorio, { observe: 'response' });
  }

  update(relatorio: IRelatorio): Observable<EntityResponseType> {
    return this.http.put<IRelatorio>(`${this.resourceUrl}/${getRelatorioIdentifier(relatorio) as number}`, relatorio, {
      observe: 'response',
    });
  }

  partialUpdate(relatorio: IRelatorio): Observable<EntityResponseType> {
    return this.http.patch<IRelatorio>(`${this.resourceUrl}/${getRelatorioIdentifier(relatorio) as number}`, relatorio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRelatorio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRelatorio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRelatorioToCollectionIfMissing(
    relatorioCollection: IRelatorio[],
    ...relatoriosToCheck: (IRelatorio | null | undefined)[]
  ): IRelatorio[] {
    const relatorios: IRelatorio[] = relatoriosToCheck.filter(isPresent);
    if (relatorios.length > 0) {
      const relatorioCollectionIdentifiers = relatorioCollection.map(relatorioItem => getRelatorioIdentifier(relatorioItem)!);
      const relatoriosToAdd = relatorios.filter(relatorioItem => {
        const relatorioIdentifier = getRelatorioIdentifier(relatorioItem);
        if (relatorioIdentifier == null || relatorioCollectionIdentifiers.includes(relatorioIdentifier)) {
          return false;
        }
        relatorioCollectionIdentifiers.push(relatorioIdentifier);
        return true;
      });
      return [...relatoriosToAdd, ...relatorioCollection];
    }
    return relatorioCollection;
  }
}
