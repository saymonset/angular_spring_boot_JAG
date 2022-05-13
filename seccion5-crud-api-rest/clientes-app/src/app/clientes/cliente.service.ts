import { Injectable } from '@angular/core';
//import { DatePipe, formatDate } from '@angular/common';
import { Cliente } from './cliente';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import swal from 'sweetalert2';

import { Router } from '@angular/router';

@Injectable()
export class ClienteService {
  private urlEndPoint: string = 'http://localhost:8080/api/clientes';

  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient, private router: Router) { }

  getClientes(): Observable<Cliente[]> {
    return this.http.get(this.urlEndPoint).pipe(
      tap(response => {
        let clientes = response as Cliente[];
        console.log('ClienteService: tap 1');
        clientes.forEach(cliente => {
          console.log(cliente.nombre);
        });
      }),
      map(response => {
        let clientes = response as Cliente[];
        //Aqui usamos el map de javascript para alterar el nombre e cada uno de los objetos
        return clientes.map(cliente => {
          cliente.nombre = cliente.nombre.toUpperCase();
          //let datePipe = new DatePipe('es');
          //Formateamos la fecha con el objeto datePipe de angular
          //EEEE dia de la semana, dd es numero de dia, MMMM nombre del mes y despues el yyyy es el anio en numero
          //cliente.createAt = datePipe.transform(cliente.createAt, 'EEEE dd, MMMM yyyy');
          //Formateamos la fecha con el objeto formaDate de angular
          //cliente.createAt = formatDate(cliente.createAt, 'dd-MM-yyyy', 'es');
          //retornamo el cliente del arrelo map de java script para crear internamente 
          //la lsita de clientes que esta retornando
          return cliente;
        });
      }
      ),
      tap(response => {
        console.log('ClienteService: tap 2');
        response.forEach(cliente => {
          console.log(cliente.nombre);
        });
      })
    );
  }

  create(cliente: Cliente): Observable<Cliente> {
    return this.http.post(this.urlEndPoint, cliente, { headers: this.httpHeaders }).pipe(
      map((response: any) => response.cliente as Cliente),
      catchError(e => {

        //si el estatus del error es 400, entonces disparamos un return con  throwError(e)
        //y en el subscribe, manejamos el aributo errors que vienen del backend
        //este error lo manejamos de forma distinta que los otros tipos de errores, por eso retornamos el
        //error que viene del backedn
        if (e.status == 400) {
          return throwError(e);
        }
       //Si no hay mensaje de error de backen, manejamo el error de otro tipo mas sencillo
        console.error(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  getCliente(id): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/clientes']);
        console.error(e.error.mensaje);
        swal('Error al editar', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  update(cliente: Cliente): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint}/${cliente.id}`, cliente, { headers: this.httpHeaders }).pipe(
      catchError(e => {

        //si el estatus del error es 400, entonces disparamos un return con  throwError(e)
        //y en el subscribe, manejamos el aributo errors que vienen del backend
        //este error lo manejamos de forma distinta que los otros tipos de errores, por eso retornamos el
        //error que viene del backedn
        if (e.status == 400) {
          return throwError(e);
        }

        console.error(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  delete(id: number): Observable<Cliente> {
    return this.http.delete<Cliente>(`${this.urlEndPoint}/${id}`, { headers: this.httpHeaders }).pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

}
