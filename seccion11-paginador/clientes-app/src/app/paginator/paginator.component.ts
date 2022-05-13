import { Component, OnInit, Input, OnChanges, SimpleChanges, enableProdMode } from '@angular/core';

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

  /* permiten calcular el numero de paginas a mostrar sin que se sature el paginador */
  desde: number;
  hasta: number;

  constructor() { }

  /* aqui no actualiza el rango de numero de pagina */
  /* en ngOnInit se crea y se inicializa una sola vez l */
  /* cada ves que hacemos click en el paginador, el enrutador link se da cuenta que es el mismo
  componente y no lo vuelve a recrear. Mantiene la misma instancia y solo cambia el parametro page
  que esta en un observable */
  ngOnInit() {
    this.initPaginator();
  }

  /* usamos este evento del ciclo de vida de angular, para que cambie el paginador y su numero en el arreglo*/
  ngOnChanges(changes: SimpleChanges) {
    let paginadorActualizado = changes['paginador'];

    if (paginadorActualizado.previousValue) {
      this.initPaginator();
    }

  }

  private initPaginator(): void {
    /* this.paginador.number es la pagina actual y viene del backend */
    /* this.paginador.totalPages es total de paginas y viene del backend */
    /* Supongamos que el total de paginas es 20 y nuestra pagina actual es 2 */
    /* this.desde = Math.min(Math.max(1, 2 - 4), 20 - 5);  */
    /* this.desde = Math.min(Math.max(1, -2), 15);  */
    /* this.desde = Math.min(1, 15);  */
     /* this.desde = 1  */
     /* hasta */
     /* this.hasta = Math.max(Math.min(20, 2 + 4), 6); */   
     /* this.hasta = Math.max(Math.min(20,6), 6); */    
     /* this.hasta = Math.max(6, 6); */     
     /* this.hasta = 6 */   

    this.desde = Math.min(Math.max(1, this.paginador.number - 4), this.paginador.totalPages - 5); 
    /* Aqui es alreves a this.desde */
    this.hasta = Math.max(Math.min(this.paginador.totalPages, this.paginador.number + 4), 6);

    /* si son muchas paginas entonces, lo limitamos a si es mayor a 5 */
    if (this.paginador.totalPages > 5) {
    
      /* cantidad de elementos que tiene nuestro arreglo con total de pag mayor a 5*/
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
