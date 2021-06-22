import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FormaDePag } from 'app/entities/enumerations/forma-de-pag.model';
import { IPlanoDeSaude, PlanoDeSaude } from '../plano-de-saude.model';

import { PlanoDeSaudeService } from './plano-de-saude.service';

describe('Service Tests', () => {
  describe('PlanoDeSaude Service', () => {
    let service: PlanoDeSaudeService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlanoDeSaude;
    let expectedResult: IPlanoDeSaude | IPlanoDeSaude[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlanoDeSaudeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        idPlano: 0,
        nomePlano: 'AAAAAAA',
        ativo: false,
        cnpj: 0,
        formaDePag: FormaDePag.Dinheiro,
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

      it('should create a PlanoDeSaude', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PlanoDeSaude()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PlanoDeSaude', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idPlano: 1,
            nomePlano: 'BBBBBB',
            ativo: true,
            cnpj: 1,
            formaDePag: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PlanoDeSaude', () => {
        const patchObject = Object.assign(
          {
            ativo: true,
            cnpj: 1,
          },
          new PlanoDeSaude()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PlanoDeSaude', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idPlano: 1,
            nomePlano: 'BBBBBB',
            ativo: true,
            cnpj: 1,
            formaDePag: 'BBBBBB',
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

      it('should delete a PlanoDeSaude', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlanoDeSaudeToCollectionIfMissing', () => {
        it('should add a PlanoDeSaude to an empty array', () => {
          const planoDeSaude: IPlanoDeSaude = { id: 123 };
          expectedResult = service.addPlanoDeSaudeToCollectionIfMissing([], planoDeSaude);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(planoDeSaude);
        });

        it('should not add a PlanoDeSaude to an array that contains it', () => {
          const planoDeSaude: IPlanoDeSaude = { id: 123 };
          const planoDeSaudeCollection: IPlanoDeSaude[] = [
            {
              ...planoDeSaude,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlanoDeSaudeToCollectionIfMissing(planoDeSaudeCollection, planoDeSaude);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PlanoDeSaude to an array that doesn't contain it", () => {
          const planoDeSaude: IPlanoDeSaude = { id: 123 };
          const planoDeSaudeCollection: IPlanoDeSaude[] = [{ id: 456 }];
          expectedResult = service.addPlanoDeSaudeToCollectionIfMissing(planoDeSaudeCollection, planoDeSaude);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(planoDeSaude);
        });

        it('should add only unique PlanoDeSaude to an array', () => {
          const planoDeSaudeArray: IPlanoDeSaude[] = [{ id: 123 }, { id: 456 }, { id: 94332 }];
          const planoDeSaudeCollection: IPlanoDeSaude[] = [{ id: 123 }];
          expectedResult = service.addPlanoDeSaudeToCollectionIfMissing(planoDeSaudeCollection, ...planoDeSaudeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const planoDeSaude: IPlanoDeSaude = { id: 123 };
          const planoDeSaude2: IPlanoDeSaude = { id: 456 };
          expectedResult = service.addPlanoDeSaudeToCollectionIfMissing([], planoDeSaude, planoDeSaude2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(planoDeSaude);
          expect(expectedResult).toContain(planoDeSaude2);
        });

        it('should accept null and undefined values', () => {
          const planoDeSaude: IPlanoDeSaude = { id: 123 };
          expectedResult = service.addPlanoDeSaudeToCollectionIfMissing([], null, planoDeSaude, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(planoDeSaude);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
