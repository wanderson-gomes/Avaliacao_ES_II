import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRetorno, getRetornoIdentifier } from '../retorno.model';

export type EntityResponseType = HttpResponse<IRetorno>;
export type EntityArrayResponseType = HttpResponse<IRetorno[]>;

@Injectable({ providedIn: 'root' })
export class RetornoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/retornos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(retorno: IRetorno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retorno);
    return this.http
      .post<IRetorno>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(retorno: IRetorno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retorno);
    return this.http
      .put<IRetorno>(`${this.resourceUrl}/${getRetornoIdentifier(retorno) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(retorno: IRetorno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retorno);
    return this.http
      .patch<IRetorno>(`${this.resourceUrl}/${getRetornoIdentifier(retorno) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRetorno>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRetorno[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRetornoToCollectionIfMissing(retornoCollection: IRetorno[], ...retornosToCheck: (IRetorno | null | undefined)[]): IRetorno[] {
    const retornos: IRetorno[] = retornosToCheck.filter(isPresent);
    if (retornos.length > 0) {
      const retornoCollectionIdentifiers = retornoCollection.map(retornoItem => getRetornoIdentifier(retornoItem)!);
      const retornosToAdd = retornos.filter(retornoItem => {
        const retornoIdentifier = getRetornoIdentifier(retornoItem);
        if (retornoIdentifier == null || retornoCollectionIdentifiers.includes(retornoIdentifier)) {
          return false;
        }
        retornoCollectionIdentifiers.push(retornoIdentifier);
        return true;
      });
      return [...retornosToAdd, ...retornoCollection];
    }
    return retornoCollection;
  }

  protected convertDateFromClient(retorno: IRetorno): IRetorno {
    return Object.assign({}, retorno, {
      dataRetorno: retorno.dataRetorno?.isValid() ? retorno.dataRetorno.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataRetorno = res.body.dataRetorno ? dayjs(res.body.dataRetorno) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((retorno: IRetorno) => {
        retorno.dataRetorno = retorno.dataRetorno ? dayjs(retorno.dataRetorno) : undefined;
      });
    }
    return res;
  }
}
