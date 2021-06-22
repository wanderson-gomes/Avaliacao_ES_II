import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPaciente, Paciente } from '../paciente.model';

import { PacienteService } from './paciente.service';

describe('Service Tests', () => {
  describe('Paciente Service', () => {
    let service: PacienteService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaciente;
    let expectedResult: IPaciente | IPaciente[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PacienteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        idUsuario: 0,
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

      it('should create a Paciente', () => {
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

        service.create(new Paciente()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Paciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idUsuario: 1,
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

      it('should partial update a Paciente', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
            cpf: 1,
            bairro: 'BBBBBB',
            contato: 1,
          },
          new Paciente()
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

      it('should return a list of Paciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idUsuario: 1,
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

      it('should delete a Paciente', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPacienteToCollectionIfMissing', () => {
        it('should add a Paciente to an empty array', () => {
          const paciente: IPaciente = { id: 123 };
          expectedResult = service.addPacienteToCollectionIfMissing([], paciente);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paciente);
        });

        it('should not add a Paciente to an array that contains it', () => {
          const paciente: IPaciente = { id: 123 };
          const pacienteCollection: IPaciente[] = [
            {
              ...paciente,
            },
            { id: 456 },
          ];
          expectedResult = service.addPacienteToCollectionIfMissing(pacienteCollection, paciente);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Paciente to an array that doesn't contain it", () => {
          const paciente: IPaciente = { id: 123 };
          const pacienteCollection: IPaciente[] = [{ id: 456 }];
          expectedResult = service.addPacienteToCollectionIfMissing(pacienteCollection, paciente);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paciente);
        });

        it('should add only unique Paciente to an array', () => {
          const pacienteArray: IPaciente[] = [{ id: 123 }, { id: 456 }, { id: 1052 }];
          const pacienteCollection: IPaciente[] = [{ id: 123 }];
          expectedResult = service.addPacienteToCollectionIfMissing(pacienteCollection, ...pacienteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const paciente: IPaciente = { id: 123 };
          const paciente2: IPaciente = { id: 456 };
          expectedResult = service.addPacienteToCollectionIfMissing([], paciente, paciente2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paciente);
          expect(expectedResult).toContain(paciente2);
        });

        it('should accept null and undefined values', () => {
          const paciente: IPaciente = { id: 123 };
          expectedResult = service.addPacienteToCollectionIfMissing([], null, paciente, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paciente);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
