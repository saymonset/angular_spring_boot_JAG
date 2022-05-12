import { Injectable } from '@angular/core';
import { CLIENTES } from './clientes.json';
import { Cliente } from './cliente';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of'
//debe estar registrada en el modulo (HttpClientModule)
import { HttpClient } from '@angular/common/http';
//reactive extension java script
import { map } from 'rxjs/operators';


@Injectable()
export class ClienteService {
  private urlEndPoint: string = 'http://localhost:8080/api/clientes';
  constructor(private http: HttpClient) { }

  getClientes(): Observable<Cliente[]> {
    //return of(CLIENTES);
    //Aqui con map , casteams el tipo a arreglo de clientes 
    return this.http.get(this.urlEndPoint).pipe(
      map(response => response as Cliente[])
    );
  }

}
