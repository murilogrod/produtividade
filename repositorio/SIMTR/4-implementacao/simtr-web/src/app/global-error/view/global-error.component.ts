import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { EventService, LoaderService } from "src/app/services";
import { GLOBAL_ERROR } from "../utils/error-contants";
import { GlobalError } from "../model/global-error";

declare var $;

@Component({
  selector: 'app-global-error',
  templateUrl: './global-error.component.html',
  styleUrls: ['./global-error.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class GlobalErrorComponent implements OnInit {

  constructor(private eventService: EventService, private loadService: LoaderService) {}

  mostrarAlertGlobal: boolean;
  erroGerado: GlobalError;
  openMaisDetalhes: boolean;
  titulo: string;

  ngOnInit() {
    this.eventService.on(GLOBAL_ERROR.CAMINHO, msg => {
      this.erroGerado = new GlobalError();
      this.erroGerado = msg;
      this.titulo = this.erroGerado.titulo;
      this.mostrarAlertGlobal = true;
      this.loadService.hide();
    });
  }

  fecharModal() {
    this.mostrarAlertGlobal = false;
    this.openMaisDetalhes = false;
    this.erroGerado = new GlobalError();
  }

  mostrarMaisDetalhes() {
    this.openMaisDetalhes = !this.openMaisDetalhes;
  }
}
