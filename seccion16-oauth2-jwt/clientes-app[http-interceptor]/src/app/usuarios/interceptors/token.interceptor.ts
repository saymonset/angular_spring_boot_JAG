import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';


/* aqui es la guia de la documentacion para interceptores
https://angular.io/guide/http#intercepting-requests-and-responses */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {
    let token = this.authService.token;

    if (token != null) {
      /* creamos un clone del request original y lo modificamos */
      const authReq = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + token)
      });

      /* next.handle: ir al proximo interceptor dentro del stack del conjunto de
      interceptores que tengamos */
      return next.handle(authReq);
    }

    return next.handle(req);
  }
}
