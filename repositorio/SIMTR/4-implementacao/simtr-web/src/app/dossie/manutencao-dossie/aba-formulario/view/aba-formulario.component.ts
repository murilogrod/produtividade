import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation, OnChanges, AfterContentInit } from '@angular/core';
import { AlertMessageService } from '../../../../services';
import { KzCpfValidator } from '../../../../shared';
import { DossieProduto, CampoFormulario, RespostaDossie, ArvoreDinamica, VinculoCliente } from '../../../../model';
import { FormGroup, FormBuilder, Validators, NgForm } from '@angular/forms';
import { SIGLA_LARGURA_TELA } from '../../../../constants/constants';
import { MudancaSalvaService } from '../../../../services/mudanca-salva.service';
import { Utils } from '../../../../utils/Utils';
import { AbaFormularioDossiePresenter } from '../presenter/aba-formulario.component.presenter';
import { FormularioEnumTipoFormulario } from 'src/app/documento/formulario-generico/model/formulario-enum-tipo-formulario';
import { FormularioGenericoService } from 'src/app/documento/formulario-generico/service/formulario-generico.service';

@Component({
	selector: 'aba-formulario',
	templateUrl: './aba-formulario.component.html',
	styleUrls: ['./aba-formulario.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class AbaFormularioComponent extends AlertMessageService implements OnInit, OnChanges, AfterContentInit {

	@Input() habilitaAlteracao;
	@Input() clienteLista;
	@Input() listaGarantias;
	@Input() produtoLista;	
	@Input() hasClienteValido;
	@Input() campoFormulario;
	@Output() formValidacaoChanged: EventEmitter<NgForm> = new EventEmitter<NgForm>();

	tipoFormulario: string;

	constructor(private formularioGenericoService: FormularioGenericoService,
        private mudancaSalvaService: MudancaSalvaService) {
		super();
	}

	ngOnInit() {
		this.tipoFormulario = FormularioEnumTipoFormulario.FORMULARIO;
	}

	ngAfterContentInit() {		
		this.formularioGenericoService.chamarVerificarVinculos(this.tipoFormulario, this.campoFormulario, this.clienteLista, this.produtoLista, this.listaGarantias);
	}

	ngOnChanges() {
		this.formularioGenericoService.chamarVerificarVinculos(this.tipoFormulario, this.campoFormulario, this.clienteLista, this.produtoLista, this.listaGarantias);
	}

	handleChangeFormValidacao(input) {
		this.formValidacaoChanged.emit(input);
	}

	handleChangeCampoFormulario(input) {
		this.campoFormulario = Object.assign([], input);
	}

}
