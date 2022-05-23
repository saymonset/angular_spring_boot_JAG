import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService,
    private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authService.isAuthenticated()) {
      if (this.isTokenExpirado()) {
        this.authService.logout();
        this.router.navigate(['/login']);
        return false;
      }
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }

  /* Aqui verficamos que el no este caducado */
  isTokenExpirado(): boolean {
    let token = this.authService.token;
    let payload = this.authService.obtenerDatosToken(token);
    /* obtenemos la fecha en milisegundos y la llevamos a segundos */
    /* Obtenemos fecha actual en segundos */
    let now = new Date().getTime() / 1000;
    /* expira si la fech exp es menor a la fecha Actual */
    if (payload.exp < now) {
      return true;
    }
    return false;
  }
}
