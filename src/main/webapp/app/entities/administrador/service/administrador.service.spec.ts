import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAdministrador, Administrador } from '../administrador.model';

import { AdministradorService } from './administrador.service';

describe('Service Tests', () => {
  describe('Administrador Service', () => {
    let service: AdministradorService;
    let httpMock: HttpTestingController;
    let elemDefault: IAdministrador;
    let expectedResult: IAdministrador | IAdministrador[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AdministradorService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        idAdministrador: 0,
        nome: 'AAAAAAA',
        cpf: 0,
        rg: 0,
        dataNascimento: currentDate,
        cidade: 'AAAAAAA',
        bairro: 'AAAAAAA',
        contato: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataNascimento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Administrador', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataNascimento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.create(new Administrador()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Administrador', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idAdministrador: 1,
            nome: 'BBBBBB',
            cpf: 1,
            rg: 1,
            dataNascimento: currentDate.format(DATE_FORMAT),
            cidade: 'BBBBBB',
            bairro: 'BBBBBB',
            contato: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Administrador', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
            cpf: 1,
            rg: 1,
          },
          new Administrador()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Administrador', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idAdministrador: 1,
            nome: 'BBBBBB',
            cpf: 1,
            rg: 1,
            dataNascimento: currentDate.format(DATE_FORMAT),
            cidade: 'BBBBBB',
            bairro: 'BBBBBB',
            contato: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataNascimento: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Administrador', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAdministradorToCollectionIfMissing', () => {
        it('should add a Administrador to an empty array', () => {
          const administrador: IAdministrador = { id: 123 };
          expectedResult = service.addAdministradorToCollectionIfMissing([], administrador);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(administrador);
        });

        it('should not add a Administrador to an array that contains it', () => {
          const administrador: IAdministrador = { id: 123 };
          const administradorCollection: IAdministrador[] = [
            {
              ...administrador,
            },
            { id: 456 },
          ];
          expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, administrador);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Administrador to an array that doesn't contain it", () => {
          const administrador: IAdministrador = { id: 123 };
          const administradorCollection: IAdministrador[] = [{ id: 456 }];
          expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, administrador);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(administrador);
        });

        it('should add only unique Administrador to an array', () => {
          const administradorArray: IAdministrador[] = [{ id: 123 }, { id: 456 }, { id: 91578 }];
          const administradorCollection: IAdministrador[] = [{ id: 123 }];
          expectedResult = service.addAdministradorToCollectionIfMissing(administradorCollection, ...administradorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const administrador: IAdministrador = { id: 123 };
          const administrador2: IAdministrador = { id: 456 };
          expectedResult = service.addAdministradorToCollectionIfMissing([], administrador, administrador2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(administrador);
          expect(expectedResult).toContain(administrador2);
        });

        it('should accept null and undefined values', () => {
          const administrador: IAdministrador = { id: 123 };
          expectedResult = service.addAdministradorToCollectionIfMissing([], null, administrador, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(administrador);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
