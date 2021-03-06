import { Injectable } from '@angular/core';
//import { DatePipe, formatDate } from '@angular/common';
import { Cliente } from './cliente';
import { Region } from './region';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import swal from 'sweetalert2';

import { Router } from '@angular/router';
import { AuthService } from '../usuarios/auth.service';

@Injectable()
export class ClienteService {
  private urlEndPoint: string = 'http://localhost:8080/api/clientes';

  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient, private router: Router,
    private authService: AuthService) { }

  private agregarAuthorizationHeader() {
    let token = this.authService.token;
    if (token != null) {
      /* aqui mandamos el token en las cabeceras */
      /* El valor tiene que partir con el tipo Bearer. Muy importante */
      return this.httpHeaders.append('Authorization', 'Bearer ' + token);
    }
    return this.httpHeaders;
  }
  

  /* 401 es no autorizado. Indica que la peticion request no ha sido ejecutada porque carece
  de credenciales validas de autenticacion */
  /* El 403  es forbidden que es un recurso prohibido. Indica que el servidor se niega 
  a permitir la accion solicitada por falta de permisos */
    
  private isNoAutorizado(e): boolean {
    /* 401 de no autenticado */
    if (e.status == 401) {

      /* Si el token expiro y aunque estemos autenticados, nos dara este error
      401 de no autenticado */
      /* Si estamos autenticados pero dio error 401, el token expiro
      y cerramos la session */
      if (this.authService.isAuthenticated()) {
        this.authService.logout();
      }
      this.router.navigate(['/login']);
      return true;
    }

    if (e.status == 403) {
      swal('Acceso denegado', `Hola ${this.authService.usuario.username} no tienes acceso a este recurso!`, 'warning');
      this.router.navigate(['/clientes']);
      return true;
    }
    return false;
  }

  getRegiones(): Observable<Region[]> {
    //agregamos cabeceras con cada metodo protegido
    return this.http.get<Region[]>(this.urlEndPoint + '/regiones', { headers: this.agregarAuthorizationHeader() }).pipe(
      catchError(e => {
        this.isNoAutorizado(e);
        return throwError(e);
      })
    );
  }

  getClientes(page: number): Observable<any> {
    return this.http.get(this.urlEndPoint + '/page/' + page).pipe(
      tap((response: any) => {
        console.log('ClienteService: tap 1');
        (response.content as Cliente[]).forEach(cliente => console.log(cliente.nombre));
      }),
      map((response: any) => {
        (response.content as Cliente[]).map(cliente => {
          cliente.nombre = cliente.nombre.toUpperCase();
          //let datePipe = new DatePipe('es');
          //cliente.createAt = datePipe.transform(cliente.createAt, 'EEEE dd, MMMM yyyy');
          //cliente.createAt = formatDate(cliente.createAt, 'dd-MM-yyyy', 'es');
          return cliente;
        });
        return response;
      }),
      tap(response => {
        console.log('ClienteService: tap 2');
        (response.content as Cliente[]).forEach(cliente => console.log(cliente.nombre));
      })
    );
  }

  create(cliente: Cliente): Observable<Cliente> {
     //agregamos cabeceras con cada metodo protegido 
    return this.http.post(this.urlEndPoint, cliente, { headers: this.agregarAuthorizationHeader() })
      .pipe(
        map((response: any) => response.cliente as Cliente),
        catchError(e => {
          if (this.isNoAutorizado(e)) {
            return throwError(e);
          }

          if (e.status == 400) {
            return throwError(e);
          }

          console.error(e.error.mensaje);
          swal(e.error.mensaje, e.error.error, 'error');
          return throwError(e);
        })
      );
  }

  getCliente(id): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`, { headers: this.agregarAuthorizationHeader() }).pipe(
      catchError(e => {

        if (this.isNoAutorizado(e)) {
          return throwError(e);
        }

        this.router.navigate(['/clientes']);
        console.error(e.error.mensaje);
        swal('Error al editar', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  update(cliente: Cliente): Observable<any> {
      //agregamos cabeceras con cada metodo protegido
    return this.http.put<any>(`${this.urlEndPoint}/${cliente.id}`, cliente, { headers: this.agregarAuthorizationHeader() }).pipe(
      catchError(e => {

        if (this.isNoAutorizado(e)) {
          return throwError(e);
        }

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
      //agregamos cabeceras con cada metodo protegido
    return this.http.delete<Cliente>(`${this.urlEndPoint}/${id}`, { headers: this.agregarAuthorizationHeader() }).pipe(
      catchError(e => {

        if (this.isNoAutorizado(e)) {
          return throwError(e);
        }

        console.error(e.error.mensaje);
        swal(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  subirFoto(archivo: File, id): Observable<HttpEvent<{}>> {

    /*
    
  Agregamos cabeceras con cada metodo protegido
    aqui en la seguridad es dstinto porque estamos mandando un FormData y no un appication JSON */

    let formData = new FormData();
    formData.append("archivo", archivo);
    formData.append("id", id);

    /* httpHeaders: es ua cabecera privada del metodo 
      Agregamos cabeceras para metodo protegido*/
    
    let httpHeaders = new HttpHeaders();
    let token = this.authService.token;
    if (token != null) {
        //agregamos cabeceras con cada metodo protegido
      httpHeaders = httpHeaders.append('Authorization', 'Bearer ' + token);
    }

    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData, {
      reportProgress: true,
      headers: httpHeaders
    });

    return this.http.request(req).pipe(
      catchError(e => {
        this.isNoAutorizado(e);
        return throwError(e);
      })
    );

  }

}
