import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRetorno, Retorno } from '../retorno.model';

import { RetornoService } from './retorno.service';

describe('Service Tests', () => {
  describe('Retorno Service', () => {
    let service: RetornoService;
    let httpMock: HttpTestingController;
    let elemDefault: IRetorno;
    let expectedResult: IRetorno | IRetorno[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RetornoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        idMedico: 0,
        idPaciente: 0,
        dataRetorno: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataRetorno: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Retorno', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataRetorno: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataRetorno: currentDate,
          },
          returnedFromService
        );

        service.create(new Retorno()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Retorno', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idMedico: 1,
            idPaciente: 1,
            dataRetorno: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataRetorno: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Retorno', () => {
        const patchObject = Object.assign(
          {
            idMedico: 1,
            idPaciente: 1,
          },
          new Retorno()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataRetorno: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Retorno', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idMedico: 1,
            idPaciente: 1,
            dataRetorno: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataRetorno: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Retorno', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRetornoToCollectionIfMissing', () => {
        it('should add a Retorno to an empty array', () => {
          const retorno: IRetorno = { id: 123 };
          expectedResult = service.addRetornoToCollectionIfMissing([], retorno);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(retorno);
        });

        it('should not add a Retorno to an array that contains it', () => {
          const retorno: IRetorno = { id: 123 };
          const retornoCollection: IRetorno[] = [
            {
              ...retorno,
            },
            { id: 456 },
          ];
          expectedResult = service.addRetornoToCollectionIfMissing(retornoCollection, retorno);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Retorno to an array that doesn't contain it", () => {
          const retorno: IRetorno = { id: 123 };
          const retornoCollection: IRetorno[] = [{ id: 456 }];
          expectedResult = service.addRetornoToCollectionIfMissing(retornoCollection, retorno);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(retorno);
        });

        it('should add only unique Retorno to an array', () => {
          const retornoArray: IRetorno[] = [{ id: 123 }, { id: 456 }, { id: 93496 }];
          const retornoCollection: IRetorno[] = [{ id: 123 }];
          expectedResult = service.addRetornoToCollectionIfMissing(retornoCollection, ...retornoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const retorno: IRetorno = { id: 123 };
          const retorno2: IRetorno = { id: 456 };
          expectedResult = service.addRetornoToCollectionIfMissing([], retorno, retorno2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(retorno);
          expect(expectedResult).toContain(retorno2);
        });

        it('should accept null and undefined values', () => {
          const retorno: IRetorno = { id: 123 };
          expectedResult = service.addRetornoToCollectionIfMissing([], null, retorno, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(retorno);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
