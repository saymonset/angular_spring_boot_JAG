import { Injectable } from '@angular/core';
//import { DatePipe, formatDate } from '@angular/common';
import { Cliente } from './cliente';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
//Toods estos conevierten a observable
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
        return clientes.map(cliente => {
          cliente.nombre = cliente.nombre.toUpperCase();
          //let datePipe = new DatePipe('es');
          //cliente.createAt = datePipe.transform(cliente.createAt, 'EEEE dd, MMMM yyyy');
          //cliente.createAt = formatDate(cliente.createAt, 'dd-MM-yyyy', 'es');
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
    /* Si vas a usar operadores, estos deben estar dentro del metodo pipe */
    //en post quitamos el tipo de dato para que no lo convierta (this.http.post()
    return this.http.post(this.urlEndPoint, cliente, { headers: this.httpHeaders }).pipe(
      //usamos el map para transformar la respuesta
      //transformamos la respuesta json y su atributo cliente en objeto cliente 
      //y es lo que devuelve
            map((response: any) => response.cliente as Cliente),
      /* Este operador catchError se encarga de interceptar el observable,
       el flujo en busca de fallas */
      catchError(e => {

        // si es un bad request que viene de la validacion
        if (e.status == 400) {
          return throwError(e);
        }

        //Aqui tomamos el error de e.error.mensaje queviene del backend
        console.error(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        //retornamos un observable
        return throwError(e);
      })
    );
  }

  getCliente(id): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`).pipe(
       /* Este operador catchError se encarga de interceptar el observable,
       el flujo en busca de fallas */
     
      catchError(e => {
        //Regresamos al listado cliente y lo redirigimos con Router que es inyectado en el constructor
        this.router.navigate(['/clientes']);
        console.error(e.error.mensaje);
        swal('Error al editar', e.error.mensaje, 'error');
        /* Con throwError(e) lo convertimos en un observable que es lo que espera en la salida */
        return throwError(e);
      })
    );
  }

  update(cliente: Cliente): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint}/${cliente.id}`, cliente, { headers: this.httpHeaders }).pipe(
      catchError(e => {

        if (e.status == 400) {
          return throwError(e);
        }

        console.error(e.error.mensaje);
        //atributos que vienen del backend (mensaje y error)
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
