import { Component, OnInit, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { ApplicationService, LoaderService } from 'src/app/services';
import { VISUALIZADOR_EXTERNO, TIPO_ARVORE_DOCUMENTO } from 'src/app/constants/constants';
import { LocalStorageVisualizador } from 'src/app/model/local-storage-visualizador';

declare var $: any;

@Component({
	selector: 'app-visualizador-externo',
	templateUrl: './visualizador-externo.component.html',
	styleUrls: ['./visualizador-externo.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class VisualizadorExternoComponent implements OnInit, AfterViewInit {

	constructor(
		private loadService: LoaderService,
		private appService: ApplicationService
	) { }

	objImagemVisualizar: LocalStorageVisualizador;
	qtdVisualiza: number;
	isHabilitarDuasVisualizacaoImg: boolean;
	tipoLayout: any;
	listDocumentosImagens2: any[];
	

	ngOnInit() {
		this.qtdVisualiza = 1;
		this.objImagemVisualizar = new LocalStorageVisualizador();
		this.objImagemVisualizar = JSON.parse(this.appService.getItemFromLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO));

		if(this.objImagemVisualizar.visualizadorDois){
			this.listDocumentosImagens2 = JSON.parse(this.appService.getItemFromLocalStorage(VISUALIZADOR_EXTERNO.IMAGEM_EXTERNO_2));
		}

		if(this.objImagemVisualizar.layoutVerticalA || this.objImagemVisualizar.layoutVerticalB){
			this.tipoLayout = "VERTICAL";
		}else{
			this.tipoLayout = "HORIZONTAL";
		}

		this.isHabilitarDuasVisualizacaoImg = this.objImagemVisualizar.visualizadorDois;
		this.girarIconeHorizontalVertical(this.isHabilitarDuasVisualizacaoImg);
		$('.select2').select2();
	}

	ngAfterViewInit() {
		this.loadService.show();	

		$('#layout').select2().val("" + this.tipoLayout + "").trigger("change");

		$('#layout').on("change", event => {
			this.tipoLayout = event.target.value;
		});
	}
    
	habilitarDuasVisualizacaoImg() {
		if(this.isHabilitarDuasVisualizacaoImg){
			this.isHabilitarDuasVisualizacaoImg = false;
		}else{
			this.isHabilitarDuasVisualizacaoImg = true;
		}
		this.girarIconeHorizontalVertical(this.isHabilitarDuasVisualizacaoImg);
	}

	private girarIconeHorizontalVertical(girar: boolean) {
		$(".incoAddVisualizar .fa").removeClass("horizontal");
		if (girar) {
			$(".incoAddVisualizar .fa").addClass("horizontal");
		}
	}
}
