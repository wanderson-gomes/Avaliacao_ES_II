import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsulta, getConsultaIdentifier } from '../consulta.model';

export type EntityResponseType = HttpResponse<IConsulta>;
export type EntityArrayResponseType = HttpResponse<IConsulta[]>;

@Injectable({ providedIn: 'root' })
export class ConsultaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/consultas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(consulta: IConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consulta);
    return this.http
      .post<IConsulta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(consulta: IConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consulta);
    return this.http
      .put<IConsulta>(`${this.resourceUrl}/${getConsultaIdentifier(consulta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(consulta: IConsulta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consulta);
    return this.http
      .patch<IConsulta>(`${this.resourceUrl}/${getConsultaIdentifier(consulta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConsulta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConsulta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConsultaToCollectionIfMissing(consultaCollection: IConsulta[], ...consultasToCheck: (IConsulta | null | undefined)[]): IConsulta[] {
    const consultas: IConsulta[] = consultasToCheck.filter(isPresent);
    if (consultas.length > 0) {
      const consultaCollectionIdentifiers = consultaCollection.map(consultaItem => getConsultaIdentifier(consultaItem)!);
      const consultasToAdd = consultas.filter(consultaItem => {
        const consultaIdentifier = getConsultaIdentifier(consultaItem);
        if (consultaIdentifier == null || consultaCollectionIdentifiers.includes(consultaIdentifier)) {
          return false;
        }
        consultaCollectionIdentifiers.push(consultaIdentifier);
        return true;
      });
      return [...consultasToAdd, ...consultaCollection];
    }
    return consultaCollection;
  }

  protected convertDateFromClient(consulta: IConsulta): IConsulta {
    return Object.assign({}, consulta, {
      dataConsulta: consulta.dataConsulta?.isValid() ? consulta.dataConsulta.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataConsulta = res.body.dataConsulta ? dayjs(res.body.dataConsulta) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((consulta: IConsulta) => {
        consulta.dataConsulta = consulta.dataConsulta ? dayjs(consulta.dataConsulta) : undefined;
      });
    }
    return res;
  }
}
