import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IConsulta, Consulta } from '../consulta.model';

import { ConsultaService } from './consulta.service';

describe('Service Tests', () => {
  describe('Consulta Service', () => {
    let service: ConsultaService;
    let httpMock: HttpTestingController;
    let elemDefault: IConsulta;
    let expectedResult: IConsulta | IConsulta[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConsultaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        idPaciente: 0,
        dataConsulta: currentDate,
        horario: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataConsulta: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Consulta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataConsulta: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataConsulta: currentDate,
          },
          returnedFromService
        );

        service.create(new Consulta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Consulta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idPaciente: 1,
            dataConsulta: currentDate.format(DATE_FORMAT),
            horario: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataConsulta: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Consulta', () => {
        const patchObject = Object.assign(
          {
            idPaciente: 1,
            horario: 'BBBBBB',
          },
          new Consulta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataConsulta: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Consulta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idPaciente: 1,
            dataConsulta: currentDate.format(DATE_FORMAT),
            horario: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataConsulta: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Consulta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConsultaToCollectionIfMissing', () => {
        it('should add a Consulta to an empty array', () => {
          const consulta: IConsulta = { id: 123 };
          expectedResult = service.addConsultaToCollectionIfMissing([], consulta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consulta);
        });

        it('should not add a Consulta to an array that contains it', () => {
          const consulta: IConsulta = { id: 123 };
          const consultaCollection: IConsulta[] = [
            {
              ...consulta,
            },
            { id: 456 },
          ];
          expectedResult = service.addConsultaToCollectionIfMissing(consultaCollection, consulta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Consulta to an array that doesn't contain it", () => {
          const consulta: IConsulta = { id: 123 };
          const consultaCollection: IConsulta[] = [{ id: 456 }];
          expectedResult = service.addConsultaToCollectionIfMissing(consultaCollection, consulta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consulta);
        });

        it('should add only unique Consulta to an array', () => {
          const consultaArray: IConsulta[] = [{ id: 123 }, { id: 456 }, { id: 22091 }];
          const consultaCollection: IConsulta[] = [{ id: 123 }];
          expectedResult = service.addConsultaToCollectionIfMissing(consultaCollection, ...consultaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const consulta: IConsulta = { id: 123 };
          const consulta2: IConsulta = { id: 456 };
          expectedResult = service.addConsultaToCollectionIfMissing([], consulta, consulta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consulta);
          expect(expectedResult).toContain(consulta2);
        });

        it('should accept null and undefined values', () => {
          const consulta: IConsulta = { id: 123 };
          expectedResult = service.addConsultaToCollectionIfMissing([], null, consulta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consulta);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
