import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRelatorio, Relatorio } from '../relatorio.model';

import { RelatorioService } from './relatorio.service';

describe('Service Tests', () => {
  describe('Relatorio Service', () => {
    let service: RelatorioService;
    let httpMock: HttpTestingController;
    let elemDefault: IRelatorio;
    let expectedResult: IRelatorio | IRelatorio[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RelatorioService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        idMedico: 0,
        descricao: 'AAAAAAA',
        diagnostico: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Relatorio', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Relatorio()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Relatorio', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idMedico: 1,
            descricao: 'BBBBBB',
            diagnostico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Relatorio', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
          },
          new Relatorio()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Relatorio', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idMedico: 1,
            descricao: 'BBBBBB',
            diagnostico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Relatorio', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRelatorioToCollectionIfMissing', () => {
        it('should add a Relatorio to an empty array', () => {
          const relatorio: IRelatorio = { id: 123 };
          expectedResult = service.addRelatorioToCollectionIfMissing([], relatorio);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relatorio);
        });

        it('should not add a Relatorio to an array that contains it', () => {
          const relatorio: IRelatorio = { id: 123 };
          const relatorioCollection: IRelatorio[] = [
            {
              ...relatorio,
            },
            { id: 456 },
          ];
          expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, relatorio);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Relatorio to an array that doesn't contain it", () => {
          const relatorio: IRelatorio = { id: 123 };
          const relatorioCollection: IRelatorio[] = [{ id: 456 }];
          expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, relatorio);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relatorio);
        });

        it('should add only unique Relatorio to an array', () => {
          const relatorioArray: IRelatorio[] = [{ id: 123 }, { id: 456 }, { id: 17022 }];
          const relatorioCollection: IRelatorio[] = [{ id: 123 }];
          expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, ...relatorioArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const relatorio: IRelatorio = { id: 123 };
          const relatorio2: IRelatorio = { id: 456 };
          expectedResult = service.addRelatorioToCollectionIfMissing([], relatorio, relatorio2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relatorio);
          expect(expectedResult).toContain(relatorio2);
        });

        it('should accept null and undefined values', () => {
          const relatorio: IRelatorio = { id: 123 };
          expectedResult = service.addRelatorioToCollectionIfMissing([], null, relatorio, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relatorio);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
