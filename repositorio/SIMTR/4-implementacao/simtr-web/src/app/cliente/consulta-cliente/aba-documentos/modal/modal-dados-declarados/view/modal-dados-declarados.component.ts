import { DialogService, DialogComponent } from 'angularx-bootstrap-modal';
import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { Utils } from '../../../../../../utils/Utils';
import { environment } from 'src/environments/environment';

declare var $: any;

import { LoaderService, EventService } from 'src/app/services';
import { ConsultaClienteService } from 'src/app/cliente/consulta-cliente/service/consulta-cliente-service';
import { DocumentoService } from 'src/app/documento/documento-service';
import { ModalDadosDeclaradosPresenter } from '../presenter/modal-dados-declarados.presenter';
import { KzCpfCnpjValidator, KzCnpjValidator } from '../../../../../../shared';
import { DadosDeclaradosModel } from '../model/dados-declarados.model';
import { CepOnlineService } from 'src/app/compartilhados/componentes/cep-online/service/cep-online-service';

export interface MessageModel {
	dadosDeclarados: any;
	idDossie: number;
	idTipoDocumento: number;
	habilitaAlteracao: boolean;
}

export interface MessageModelSaida {
	resultado: boolean;
	mensagem: any;

}

@Component({
	selector: 'modal-dados-declarados',
	templateUrl: './modal-dados-declarados.component.html',
	styleUrls: ['./modal-dados-declarados.component.css'],
	encapsulation: ViewEncapsulation.None
})
export class ModalDadosDeclaradosComponent extends DialogComponent<MessageModel, MessageModelSaida> implements MessageModel, OnInit {

	dadosDeclarados: any;
	idDossie: number;
	idTipoDocumento: number;
	ptBR: any;
	dataAtual;
	habilitaAlteracao: boolean;

	presenter: ModalDadosDeclaradosPresenter;
	cepOnlineValidoChange: boolean;


	constructor(dialogService: DialogService, private loadService: LoaderService,
		private clienteService: ConsultaClienteService, private documentoService: DocumentoService,
		private modalDadosDeclaradosPresenter: ModalDadosDeclaradosPresenter,
		private cepOnlineService: CepOnlineService) {
		super(dialogService);
		this.presenter = modalDadosDeclaradosPresenter;
	}

	inicializarModel() {
		this.presenter.model = new DadosDeclaradosModel();
		this.presenter.orderFieldsDadosDeclarados(this.dadosDeclarados);
		this.presenter.model.idDossie = this.idDossie;
		this.presenter.removeAtributosInativosGuardaHistorico();
		this.presenter.model.idTipoDocumento = this.idTipoDocumento;
		this.presenter.model.listaItens = [];
		this.presenter.model.opcoesSelecionadas = [];
		this.dataAtual = new Date();
		this.presenter.model.formularioValido = true;
		this.carregarSelect();

		this.ptBR = {
			firstDayOfWeek: 1,
			dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
			dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
			dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
			monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
			monthNamesShort: ['Jan', 'Feb', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dec'],
			today: 'Hoje',
			clear: 'Limpar'
		};
	}

	ngOnInit(): void {
		this.inicializarModel();
		this.loadService.show();
		this.presenter.carregarModel();
		this.presenter.separarCamposHidden();
	}

	salvarDadosDeclarados() {
		this.loadService.show();
		let documento = this.presenter.carregarFormulario();
		if (this.presenter.model.formularioValido) {

			documento.tipo_documento = this.presenter.model.idTipoDocumento;
			documento.origem_documento = "O";

			this.documentoService.insertDocument(this.presenter.model.idDossie, documento).subscribe(retorno => {

				this.close();
				this.result = { resultado: true, mensagem: null };
			}, error => {
				this.loadService.hide();
				this.close();
				this.result = { resultado: false, mensagem: error };
				throw error;
			});

		}else{
			this.loadService.hide();
		}

	}

	carregarSelect() {
		this.presenter.model.dadosDeclarados.forEach(elemento => {
			if (elemento.tipo_campo === "SELECT") {
				elemento.listaItens = [];
				elemento.opcoes.forEach(opcao => {
					elemento.listaItens.push({ label: opcao.valor, value: opcao.chave });

				});
				elemento.listaItens.sort(Utils.ordenarDocumentosNodeByLabel);
				elemento.listaItens.splice(0, 0, { label: "Selecione", value: null });
			}
		});
	}

	maskCPfCnpj(evento) {
		evento.target.value = Utils.masKcpfCnpj(evento.target.value);
	}

	maskCPf(evento) {
		evento.target.value = Utils.masKcpfCnpj(evento.target.value);
		if (evento.target.value.length === 14) {
			this.adicionarErrorCpfCnpj(evento.target.value);
		}
	}


	maskCnpj(evento) {
		evento.target.value = Utils.maskCnpj(evento.target.value);
		if (evento.target.value.length === 18) {
			this.adicionarErrorCnpj(evento.target.value);
		}

	}

	adicionarErrorCpfCnpj(cpfCnpj: string) {
		if (cpfCnpj) {
			this.presenter.model.dadosDeclarados.forEach(elemento => {
				if ((elemento.tipo_campo === "CPF" || elemento.tipo_campo === "CNPJ" || elemento.tipo_campo === "CPF_CNPJ") && elemento.valor && elemento.valor.replace(/\D/g, "") === cpfCnpj.replace(/\D/g, "")) {
					elemento.campoInvalido = cpfCnpj && !KzCpfCnpjValidator.cpfCnpjValido(cpfCnpj);
				}
			});
		}
	}

	adicionarErrorCnpj(cpfCnpj: string) {
		if (cpfCnpj) {
			this.presenter.model.dadosDeclarados.forEach(elemento => {
				if ((elemento.tipo_campo === "CNPJ" || elemento.tipo_campo === "CPF_CNPJ") && elemento.valor && elemento.valor.replace(/\D/g, "") === cpfCnpj.replace(/\D/g, "")) {
					elemento.campoInvalido = cpfCnpj && !KzCnpjValidator.cnpjValido(cpfCnpj);
				}
			});
		}
	}

	dismiss() {
		this.close();
	}

	maskCalendar(event): void {
		event.target.value = Utils.mascaraData(event);
	}

	onBlurCep(event, itemCampo: any){
		if (itemCampo.tipo_campo === "CEP" || itemCampo.tipo_campo === "CEP_ONLINE"){

			if(itemCampo.indicador_obrigatorio){
				
				if(event.target.value.length != 10) {
					itemCampo.campoInvalido = true;
				}else{
					itemCampo.campoInvalido = false;
				}

			}else{
				
				if(event.target.value.length != 10) {
					itemCampo.campoInvalido = false;
					itemCampo.valor = null;
					event.target.value = null;
				}else{
					itemCampo.campoInvalido = false;
				}
			}
			
			if(itemCampo.tipo_campo === "CEP_ONLINE"){

				if(event.target.value){
					let cepValor = event.target.value.replace(/\D/g, '');
		
					if (cepValor.length !== 8) {
						this.cepOnlineService.initDadosDeclarados(null, this.presenter.model.dadosDeclarados, itemCampo.nome_documento)
						this.loadService.hide();
					}else{
						this.cepOnlineService.cep(cepValor).subscribe(resposta => {
							let cepObjeto = this.cepOnlineService.montarObjetoCep(resposta);
							this.cepOnlineService.initDadosDeclarados(cepObjeto, this.presenter.model.dadosDeclarados, itemCampo.nome_documento)
							this.loadService.hide();
		
						}, error => {
							this.cepOnlineService.initDadosDeclarados(null, this.presenter.model.dadosDeclarados, itemCampo.nome_documento)
							this.loadService.hide();
							throw error;
						});
					}
				}else{
					this.cepOnlineService.initDadosDeclarados(null, this.presenter.model.dadosDeclarados, itemCampo.nome_documento)
					this.loadService.hide();
				}
			}
		}
	}

	formatDecimal(item) {
		let value = item.valor;
		if (value != "" && value != undefined) {
			let valueMascara = Utils.aplicarMascaraMonetario(value);	
			let valorDecimal = valueMascara.replace(/\./g, '').replace(',', '.');
			item.valor = valorDecimal;	
		}
	}

	completarContaCaixaComZeroEsquerda(item){
		if (item.valor && item.valor != '') {
			let resposta: string = Utils.completarContaCaixaComZeroEsquerda(item.valor);
			item.valor = resposta;
		}
	}
}
