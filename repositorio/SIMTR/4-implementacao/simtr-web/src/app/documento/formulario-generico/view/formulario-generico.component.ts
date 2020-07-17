import { AlertMessageService, EventService, LoaderService } from "src/app/services";
import { ViewEncapsulation, Component, OnInit, Input, Output, EventEmitter, OnChanges } from "@angular/core";
import { NgForm, FormGroup, FormBuilder, Form, FormsModule } from "@angular/forms";
import { MudancaSalvaService } from "src/app/services/mudanca-salva.service";
import { FormularioGenericoComponentPresenter } from "../presenter/formulario-generico.component.presenter";
import { CampoFormulario } from "src/app/model";
import { formularioGenericoModel } from "../model/formulario-generico.model";
import { FormularioGenericoService } from "../service/formulario-generico.service";
import { CamposFormulario } from "src/app/model/campos-formulario";
import { FormularioEnumTipoFormulario } from "../model/formulario-enum-tipo-formulario";
import { Utils } from "src/app/utils/Utils";
import { CepOnlineService } from "src/app/compartilhados/componentes/cep-online/service/cep-online-service";
import { TIPO_DOCUMENTO } from '../../../constants/constants';

const _BASE: string = "_BASE";
const _COMPLEMENTO: string = "_COMPLEMENTO";

@Component({
    selector: 'formulario-generico',
    templateUrl: './formulario-generico.component.html',
    styleUrls: ['./formulario-generico.css'],
    encapsulation: ViewEncapsulation.None
})
export class FormularioGenericoComponent extends AlertMessageService implements OnInit, OnChanges {

	@Input() tipoFormulario: string;
	@Input() legenda: string;
	@Input() habilitaAlteracao: boolean;
	@Input() clienteLista;
	@Input() listaGarantias;
	@Input() produtoLista;	
	@Input() hasClienteValido;
	@Input() campoFormulario;
	@Input() toogleFieldSet: boolean;
	@Input() habilitarMenssagem: boolean;
	@Input() messageFormularioVazio: string;
	@Output() formValidacaoChanged: EventEmitter<NgForm> = new EventEmitter<NgForm>();
	@Output() camposFormularioChanged: EventEmitter<CamposFormulario> = new EventEmitter<CamposFormulario>();
    
	formulario: FormGroup;
	formValidacao: NgForm;
	listaCamposFormulario: CampoFormulario[] = [];
	contadorCampos: number = 1;

    formularioGenericoComponentPresenter: FormularioGenericoComponentPresenter;

	constructor(
		private formBuilder: FormBuilder,
        private mudancaSalvaService: MudancaSalvaService,
        private formularioGenericoService: FormularioGenericoService,
		formularioGenericoComponentPresenter: FormularioGenericoComponentPresenter,
		private loadService: LoaderService,
		private cepOnlineService: CepOnlineService
	) {
		super();
        this.formularioGenericoComponentPresenter = formularioGenericoComponentPresenter;
        this.formularioGenericoComponentPresenter.formularioGenericoModel = new formularioGenericoModel();
	}

    ngOnInit() {
		this.incializarFormulario();
	}
	
	ngOnChanges() {
		this.incializarFormulario();
		this.campoFormulario = Object.assign([], this.campoFormulario);
	}

	incializarFormulario(){
		this.formularioGenericoComponentPresenter.inicializarValidacao(this.formulario, this.formBuilder);
		
		// montar mascara do tipo conta caixa
		this.campoFormulario.forEach(campo => {
			if(campo.tipo_campo == TIPO_DOCUMENTO.CONTA_CAIXA){
				this.completarContaCaixaComZeroEsquerda(campo);
			}
		});
		
		this.formularioGenericoService.montarListaCamposExisteExpressao(this.tipoFormulario ,this.campoFormulario);

		if(this.tipoFormulario == FormularioEnumTipoFormulario.FORMULARIO) {
			this.campoFormulario.forEach(campo => {
				this.formularioGenericoService.interprestaListaExpressao(this.tipoFormulario, campo, this.campoFormulario, this.clienteLista, this.produtoLista, this.listaGarantias);
			});
		}
	}

	activityTreeTypeClicked(radio: any, formulario: any, formDinamico: NgForm): void {
		for (let form of this.campoFormulario) {
			if (form.id == formulario.id) {
				form.resposta_aberta = radio.valor_opcao;
				this.changeAcao(form, formDinamico);
				break;
			}
		};
		this.campoFormulario = Object.assign([], this.campoFormulario);
	}

	checkValidAndTouched(form: NgForm, formulario: any) {
		this.formValidacao = form;
		let statusCampo = null;
		
		if(this.formValidacao && this.formValidacao.controls){
			statusCampo = this.formValidacao.controls[formulario.id] != null && !this.formValidacao.controls[formulario.id].valid && this.formValidacao.controls[formulario.id].touched;
			this.formValidacaoChanged.emit(this.formValidacao);
		}

		if(statusCampo && formulario.tipo_campo === 'CEP_ONLINE_DEP'){
			statusCampo = this.formValidacao.controls[formulario.id] != null 
				&& this.formValidacao.controls[formulario.id].value 
				&& this.formValidacao.controls[formulario.id].touched 
				&& !this.formValidacao.controls[formulario.id].valid ? false : true;
		}
			
		return statusCampo;
	}

	defineLarguraCampo(formulario: CampoFormulario): string {

		let larguraCampo: string = "col-sm-12";

		if (formulario && formulario.lista_apresentacoes && formulario.lista_apresentacoes.length > 0) {
			larguraCampo = this.formularioGenericoComponentPresenter.aplicaLarguraPCCampo(formulario, larguraCampo);
			larguraCampo = this.formularioGenericoComponentPresenter.aplicaLarguraTabletCampo(formulario, larguraCampo);
			larguraCampo = this.formularioGenericoComponentPresenter.aplicaLarguraMobileCampo(formulario, larguraCampo);
		}
		return larguraCampo;
	}

	changeAcao(formulario: CampoFormulario, formDinamico: NgForm) {
		this.mudancaSalvaService.setIsMudancaSalva(false);
		this.campoFormulario = this.formularioGenericoService.interprestaListaExpressao(this.tipoFormulario, formulario, this.campoFormulario, this.clienteLista, this.produtoLista, this.listaGarantias);
		this.camposFormularioChanged.emit(this.campoFormulario);
		this.checkValidAndTouched(formDinamico, formulario);
	}

	verificarFormularioModal(): boolean {
		return this.tipoFormulario != FormularioEnumTipoFormulario.FORMULARIO;
	}

	preencherCampoHidden(formulario: any){
		if(formulario.resposta_aberta && formulario.resposta_aberta.length == 8){
			
			let cepBase = formulario.resposta_aberta.substring(0, 5);
			let cepComplemento = formulario.resposta_aberta.substring(5);
			
			let campoHiddenBase = this.retornaCampoHidden(formulario.nome_campo + _BASE);
			if(campoHiddenBase){
				campoHiddenBase.resposta_aberta = cepBase;
			}

			let campoHiddenComplemento = this.retornaCampoHidden(formulario.nome_campo + _COMPLEMENTO);
			if(campoHiddenComplemento){
				campoHiddenComplemento.resposta_aberta = cepComplemento;
			}
		}
	}

	retornaCampoHidden(nome:string):any{
		return this.campoFormulario.find((element) => {
		  return element.tipo_campo === 'HIDDEN' && element.nome_campo && element.nome_campo.toUpperCase() === nome.toUpperCase();
		});
	}

	contadoCampos(index: number) {
		this.contadorCampos++;
	}

	maskCalendar(event: any): void {
		event.target.value = Utils.mascaraData(event);
	}

	onBlurCepOnline(event, formulario: any){
        if(formulario.tipo_campo === "CEP_ONLINE"){
			this.loadService.show();
			if(formulario.resposta_aberta){
				let cepValor = formulario.resposta_aberta.replace(/\D/g, '');
		
				if (cepValor.length !== 8) {
					this.cepOnlineService.initFormularioDinamico(null, this.campoFormulario, formulario.nome_campo);
					this.atualizarCampos();
					this.loadService.hide();
				}else{
					this.cepOnlineService.cep(cepValor).subscribe(resposta => {
						let cepObjeto = this.cepOnlineService.montarObjetoCep(resposta);

						this.cepOnlineService.initFormularioDinamico(cepObjeto, this.campoFormulario, formulario.nome_campo);
						this.atualizarCampos();
						this.loadService.hide();

					}, error =>{
						this.cepOnlineService.initFormularioDinamico(null, this.campoFormulario, formulario.nome_campo);
						this.atualizarCampos()
						this.loadService.hide();
						throw error;
					});
				}
			}else{
				this.cepOnlineService.initFormularioDinamico(null, this.campoFormulario, formulario.nome_campo);
				this.atualizarCampos();
				this.loadService.hide();
			}	
		}
	}

	private atualizarCampos(){
		this.campoFormulario.forEach(campo => {
			this.formularioGenericoService.interprestaListaExpressao(this.tipoFormulario, campo, this.campoFormulario, this.clienteLista, this.produtoLista, this.listaGarantias);
		});
	}

	formatDecimal(formulario) {
		let value = formulario.resposta_aberta;
		if (value != "" && value != undefined) {
			let valueMascara = Utils.aplicarMascaraMonetario(value);	
			let valorDecimal = valueMascara.replace(/\./g, '').replace(',', '.');
			formulario.resposta_aberta = valorDecimal;	
		}
	}

	completarContaCaixaComZeroEsquerda(formulario){
		let value = formulario.resposta_aberta;
		if (value && value != '') {
			let resposta: string = Utils.completarContaCaixaComZeroEsquerda(value);
			formulario.resposta_aberta = resposta;
		}
	}
}