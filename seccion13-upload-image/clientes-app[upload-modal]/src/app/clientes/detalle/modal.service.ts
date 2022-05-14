import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  modal: boolean = false;

  //Observable para mnitorear cuando hags un upload
  private _notificarUpload = new EventEmitter<any>();

  constructor() { }

  //aplicamos el metodo get
  get notificarUpload(): EventEmitter<any> {
    return this._notificarUpload;
  }

  abrirModal() {
    this.modal = true;
  }

  cerrarModal() {
    this.modal = false;
  }
}
