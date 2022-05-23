import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService,
    private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

      /* validamos primero si no esta autenticado para salir del login y no
      seguir validando */
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }

    /* hacemos un cast de string a la data que se le envia a Guard*/
    /* En RoleGuard, le mandamos esta data como constantes  RoleGuard], data: { role: 'ROLE_ADMIN' }  */
    /* Si no tienes estos role seteados en la data de Guard, entonces no tiene permiso
    para ver esos enlaces y nos salimos  a clientes sin antes dar un mensaje */
    let role = next.data['role'] as string;
    console.log(role);
    if (this.authService.hasRole(role)) {
      return true;
    }
    swal('Acceso denegado', `Hola ${this.authService.usuario.username} no tienes acceso a este recurso!`, 'warning');
    this.router.navigate(['/clientes']);
    return false;
  }
}
