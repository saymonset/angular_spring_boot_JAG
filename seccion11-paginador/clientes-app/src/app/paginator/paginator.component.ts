import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'paginator-nav',
  templateUrl: './paginator.component.html'
})
export class PaginatorComponent implements OnInit, OnChanges {

 /*  <!-- Este es el input  [paginador] de paginator.component y le asignamos la variable paginador
 de ClientesComponent,  que es el response .
 Esto es una foma de injeccion de dependenca del objeto padre al objeto hijo
 --> */
  @Input() paginador: any;

  //creams nuestro arreglo de paginas
  paginas: number[];

  desde: number;
  hasta: number;

  constructor() { }

  ngOnInit() {
    this.initPaginator();
  }

  ngOnChanges(changes: SimpleChanges) {
    let paginadorActualizado = changes['paginador'];

    if (paginadorActualizado.previousValue) {
      this.initPaginator();
    }

  }

  private initPaginator(): void {
    this.desde = Math.min(Math.max(1, this.paginador.number - 4), this.paginador.totalPages - 5); 
    this.hasta = Math.max(Math.min(this.paginador.totalPages, this.paginador.number + 4), 6);

    if (this.paginador.totalPages > 5) {
      //Anteriormente en su forma basica es
      //this.paginas = new Array(this.paginador.totalPages).fill(0);
      //Usamos el metodo fill() para llenar el arreglo con datos
      //un map dentro de un rreglo es para modificar los datos, como argumento recibe dos parametros, el valor, indice
      //el valor y sabemos que es cero y el indice cambia su valor desde cero hasta la cantidad de elementos
      // ahora el indice le sumamos 1 para que empieze de uno y no de cero el num de pagina
      this.paginas = new Array(this.hasta - this.desde + 1).fill(0).map((_valor, indice) => indice + this.desde);
    } else {
          //Usamos el metodo fill() para llenar el arreglo con datos
      //un map dentro de un rreglo es para modificar los datos, como argumento recibe dos parametros, el valor, indice
      //el valor y sabemos que es cero y el indice cambia su valor desde cero hasta la cantidad de elementos
      // ahora el indice le sumamos 1 para que empieze de uno y no de cero el num de pagina
      this.paginas = new Array(this.paginador.totalPages).fill(0).map((_valor, indice) => indice + 1);
    }
  }

}
