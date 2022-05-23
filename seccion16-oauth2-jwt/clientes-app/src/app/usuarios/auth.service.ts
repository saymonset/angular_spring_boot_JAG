import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Usuario } from './usuario';
import { JsonpModule } from '@angular/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _usuario: Usuario;
  private _token: string;

  constructor(private http: HttpClient) { }

  /* 
  Creamos get usuario.
  siempre son publicos, seguida la palabra reservada  get 
  retorna un tipo*/
  /* se combina con _usuario que es privado */
  public get usuario(): Usuario {
    if (this._usuario != null) {
      return this._usuario;
    } else if (this._usuario == null && sessionStorage.getItem('usuario') != null) {
      /* convertimos el sessionstorage del string plano usuario a  un objeto y 
      convertimos en un usuario con as usuario*/
      this._usuario = JSON.parse(sessionStorage.getItem('usuario')) as Usuario;
      return this._usuario;
    }
    return new Usuario();
  }

  public get token(): string {
    if (this._token != null) {
      return this._token;
    } else if (this._token == null && sessionStorage.getItem('token') != null) {
      this._token = sessionStorage.getItem('token');
      return this._token;
    }
    return null;
  }

  login(usuario: Usuario): Observable<any> {
    const urlEndpoint = 'http://localhost:8080/oauth/token';


    /* Credenciales del cliente o alicacion */
    /* convertir a base 64 con btoa */
    const credenciales = btoa('angularapp' + ':' + '12345');

    /* el content-type debe ser application/x-www-form-urlencoded */
    const httpHeaders = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + credenciales
    });

    /* aqui pasamos lo parametros con URLSearchParams */
    let params = new URLSearchParams();
    params.set('grant_type', 'password');
    params.set('username', usuario.username);
    params.set('password', usuario.password);
    console.log(params.toString());
    /* Este objeto se debe convertir a string con params.toString */
    return this.http.post<any>(urlEndpoint, params.toString(), { headers: httpHeaders });
  }

  guardarUsuario(accessToken: string): void {
    let payload = this.obtenerDatosToken(accessToken);
    this._usuario = new Usuario();
    this._usuario.nombre = payload.nombre;
    this._usuario.apellido = payload.apellido;
    this._usuario.email = payload.email;
    this._usuario.username = payload.user_name;
    this._usuario.roles = payload.authorities;
    /* Objeto global de java script sessionStorage que nos permite guardar datos en la session */
    /* sessionStorage es parte del api html5 */
    /* No se puede guardar un objeto, solo se guarda tipo string, entonces convertimo
    a un json plano tipo string con JSON.stringify */
    sessionStorage.setItem('usuario', JSON.stringify(this._usuario));
  }

  guardarToken(accessToken: string): void {
    this._token = accessToken;
    sessionStorage.setItem('token', accessToken);
  }x

  obtenerDatosToken(accessToken: string): any {
    if (accessToken != null) {
      /* Del arreglo que tiene tres partes, obtenemos el payload que es la posicion dos
       del 
      indice del arreglo
      Utilizando la funcion de javaScript atob  convertimos en nuestro Json 
      Lo decodificamos ese string de base 64 con atob
 */
/* convertimos este string a un objeto con JSON.parse */
      return JSON.parse(atob(accessToken.split(".")[1]));
    }
    return null;
  }

  isAuthenticated(): boolean {
    let payload = this.obtenerDatosToken(this.token);
    if (payload != null && payload.user_name && payload.user_name.length > 0) {
      return true;
    }
    return false;
  }

  hasRole(role: string): boolean {
    //si el get usuario en roles con includes permite validar si 
    //existe algun elemento dentro de ese arreglo
    if (this.usuario.roles.includes(role)) {
      return true;
    }
    return false;
  }

  logout(): void {
    this._token = null;
    this._usuario = null;
    /* dos formas de borrar session */
    /* borra todas las variables de sesion */
    sessionStorage.clear();
    /* Borra cada una por separado de session */
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('usuario');
  }
}
