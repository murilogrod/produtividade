import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { CamposComExpressaoModel } from "../model/formulario-campos-com-expressao.model";
import { CampoFormulario, VinculoCliente, VinculoProduto, VinculoGarantia } from "src/app/model";
import { TipoExpressao } from "src/app/dossie/manutencao-dossie/aba-formulario/modal/aba-formulario/aba-formulario-tipo-expressao.enum";
import { TipoCampoExpressao } from "../model/formulario-tipo-campo.model";
import * as moment from 'moment';
import { FormularioEnumTipoFormulario } from "../model/formulario-enum-tipo-formulario";

@Injectable()
export class FormularioGenericoService {
    listarCamposComExpressao: BehaviorSubject<Array<CamposComExpressaoModel>> = new BehaviorSubject<Array<CamposComExpressaoModel>>(null);

	get $listarCamposComExpressao() {
		return this.listarCamposComExpressao.asObservable();
	}

	setListarCamposComExpressao(listaCampoExpressao: Array<CamposComExpressaoModel>) {
		this.listarCamposComExpressao.next(listaCampoExpressao);
    }
	
	chamarVerificarVinculos(tipoListaExpressao: string, campoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
        if(tipoListaExpressao === FormularioEnumTipoFormulario.FORMULARIO) {
            this.notificarClientePessoa(tipoListaExpressao, campoFormulario, clienteLista, produtoLista, listaGarantias);
            this.notificarProduto(tipoListaExpressao, campoFormulario, clienteLista, produtoLista, listaGarantias);
            this.notificarGarantia(tipoListaExpressao, campoFormulario, clienteLista, produtoLista, listaGarantias);
        }		
	}

	notificarClientePessoa(tipoListaExpressao: string, campoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
		if (clienteLista && clienteLista.length > 0) {
			this.percorrerCampoEAtualizarLista(tipoListaExpressao, campoFormulario, clienteLista, produtoLista, listaGarantias);
		}
	}

	notificarProduto(tipoListaExpressao: string, campoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
		if (produtoLista && produtoLista.length > 0) {
			this.percorrerCampoEAtualizarLista(tipoListaExpressao, campoFormulario, clienteLista, produtoLista, listaGarantias);
		}
	}

	notificarGarantia(tipoListaExpressao: string, campoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
		if (listaGarantias && listaGarantias.length > 0) {
			this.percorrerCampoEAtualizarLista(tipoListaExpressao, campoFormulario, clienteLista, produtoLista, listaGarantias);
		}
	}
	
	percorrerCampoEAtualizarLista(tipoListaExpressao: string, campoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
		campoFormulario.forEach(formulario => {
			this.interprestaListaExpressao(tipoListaExpressao, formulario, campoFormulario, clienteLista, produtoLista, listaGarantias);
		});
	}

	montarListaCamposExisteExpressao(tipoListaExpressao: string, listaCampoFormulario: CampoFormulario[]) {
		let listaCampoExpressao = new Array<CamposComExpressaoModel>();
		listaCampoFormulario.forEach((campo, idx) => {
			if (campo.expressao_interface) {
				this.popularListaComCamposExisteExpressao(tipoListaExpressao, campo, idx, listaCampoExpressao);
			} else {
				campo.showCampo = true;
			}
		});
	}

	private popularListaComCamposExisteExpressao(tipoListaExpressao: string, campo: CampoFormulario, idx: number, listaCampoExpressao: CamposComExpressaoModel[]): any {
		let listaExpresao = JSON.parse(campo.expressao_interface);
        let campoComExpressao = new CamposComExpressaoModel();
        campoComExpressao.tipoFormulario = tipoListaExpressao;
		campoComExpressao.idxCampo = idx;
		campoComExpressao.conjuntoRegras = listaExpresao;
		listaCampoExpressao.push(campoComExpressao);
		this.setListarCamposComExpressao(listaCampoExpressao);
	}

	interprestaListaExpressao(tipoListaExpressao: string, campo: CampoFormulario, listaCampoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
		this.$listarCamposComExpressao.subscribe(conjutoRegra => {
			if (conjutoRegra) {
				for (let index = 0; index < conjutoRegra.length; index++) {
					if(tipoListaExpressao === conjutoRegra[index].tipoFormulario) {
						this.interpretandorConformeTipoListaExpressao(conjutoRegra, index, campo, listaCampoFormulario, clienteLista, produtoLista, listaGarantias);
					}				
				}
			}
		});
		return listaCampoFormulario;
	}

	private interpretandorConformeTipoListaExpressao(conjutoRegra: CamposComExpressaoModel[], index: number, campo: CampoFormulario, listaCampoFormulario: CampoFormulario[], clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {
		let posConjutoRegra = 0;
		let obj = conjutoRegra[index];
		let listaResposta;
		for (let conjunto of conjutoRegra[index].conjuntoRegras) {
			let qtdRegra = conjunto.length;
			if (!listaResposta || (posConjutoRegra > 0 && ((listaResposta && listaResposta.length == 0) || listaResposta.includes(false)))) {
				++posConjutoRegra;
				listaResposta = [];
				
				if(listaCampoFormulario[conjutoRegra[index].idxCampo] && listaCampoFormulario[conjutoRegra[index].idxCampo].expressao_interface){

					for (let regra of conjunto) {
						this.chamarValidacaoPorIdentificador(campo, regra, listaResposta, qtdRegra, listaCampoFormulario, conjutoRegra[index], clienteLista, produtoLista, listaGarantias);
						if (listaResposta && listaResposta.length > 0 && qtdRegra == listaResposta.length && !listaResposta.includes(false)) {
							listaCampoFormulario[conjutoRegra[index].idxCampo].showCampo = true;
						}else {
							listaCampoFormulario[conjutoRegra[index].idxCampo].showCampo = false;
						}
					}
				}
			}
			else {
				listaCampoFormulario[conjutoRegra[index].idxCampo].showCampo = true;
			}
		}
	}

	private chamarValidacaoPorIdentificador(campo: CampoFormulario, regra: any, listaResposta: any[], qtdRegra: any, listaCampoFormulario: CampoFormulario[], expressao: CamposComExpressaoModel, 
		clienteLista: VinculoCliente[], produtoLista: VinculoProduto[], listaGarantias: VinculoGarantia[]) {

		if (campo.id == regra.campo_resposta) {
			listaResposta.push(this.validarPorTipoCampo(campo, regra));
		}else if (regra.campo_resposta){
			let obj = listaCampoFormulario.filter(cp => regra.campo_resposta == cp.id);
			listaResposta.push(this.validarPorTipoCampo(obj[0], regra));
		}

		if (!regra.campo_resposta) {
			switch (regra.tipo_regra) {
				case TipoExpressao.GRID_CLIENTE:
						this.verificarCliente(clienteLista, regra, listaResposta);
					break;
				case TipoExpressao.GRID_PRODUTO:
						this.verificarProduto(produtoLista, regra, listaResposta);
					break;
				case TipoExpressao.GRID_GARANTIA:
						this.verificarGarantia(listaGarantias, regra, listaResposta);
					break;
			}
		}
	}

	private verificarGarantia(listaGarantias: VinculoGarantia[], regra: any, listaResposta: any[]) {
		if (listaGarantias && listaGarantias.length > 0) {
			for (let garantia of listaGarantias) {
				let resposta = this.validarOpcoesSelecionadasVinculos(regra, garantia);
				if (resposta != undefined) {
					listaResposta.push(resposta);
					break;
				}
			}
		}else {			
			listaResposta.push(false);
		}
	}

	private verificarProduto(produtoLista: VinculoProduto[], regra: any, listaResposta: any[]) {
		if (produtoLista && produtoLista.length > 0) {
			for (let produto of produtoLista) {
				let resposta = this.validarOpcoesSelecionadasVinculos(regra, produto);
				if (resposta != undefined) {
					listaResposta.push(resposta);
					break;
				}
			}
		}else {			
			listaResposta.push(false);
		}
	}

	private verificarCliente(clienteLista: VinculoCliente[], regra: any, listaResposta: any[]) {
		if (clienteLista && clienteLista.length > 0) {
			for (let cliente of clienteLista) {
				let resposta = this.validarOpcoesSelecionadasVinculos(regra, cliente);
				if (resposta != undefined) {
					listaResposta.push(resposta);
					break;
				}
			}
		}else {			
			listaResposta.push(false);
		}
	}

	private validarOpcoesSelecionadasVinculos( regra: any, vinculo: any) {
		let listaVinculoKeyValor = Object.entries(vinculo);
		let retorno;
		for(let atributoValor of listaVinculoKeyValor) {
			let objeto: any = atributoValor[1];
			if(regra.atributo === atributoValor[0] && (regra.valores.includes(objeto.nome) || regra.valores.includes(objeto))) {
				switch(regra.tipo_regra) {
					case TipoExpressao.GRID_CLIENTE:
						retorno = regra.valores.includes(objeto.nome);
						break;
					default:
						retorno = regra.valores.includes(objeto);
						break;
				}
			}
		}
		return retorno;
	}

	private validarPorTipoCampo(campo: CampoFormulario, regra: any) {
		let resposta = false;
		if(campo && campo.tipo_campo) {
			switch (campo.tipo_campo) {
				case TipoCampoExpressao.TEXT:
					resposta = this.validarCampoRespostaAberta(campo, resposta, regra);
					break;
				case TipoCampoExpressao.COLOR:
					resposta = this.validarCampoRespostaAberta(campo, resposta, regra);
					break;
				case TipoCampoExpressao.DATE:
					resposta = this.validarCampoRespostaAberta(campo, resposta, regra);
					break;
				case TipoCampoExpressao.SELECT:
					resposta = this.validarCampoRespostaAberta(campo, resposta, regra);
					break;
				case TipoCampoExpressao.RADIO:
					resposta = this.validarCampoRespostaAberta(campo, resposta, regra);
					break;
				case TipoCampoExpressao.CHECKBOX:
					resposta = this.validarOpcoesSelecionadas(regra, campo, resposta);
					break;
				case TipoCampoExpressao.MONETARIO:
					resposta = this.validarCampoRespostaAberta(regra, resposta, regra);
					break;
				case TipoCampoExpressao.DECIMAL:
					resposta = this.validarCampoRespostaAberta(regra, resposta, regra);
					break;
				case TipoCampoExpressao.CEP:
					resposta = this.validarCampoRespostaAberta(regra, resposta, regra);
					break;
				case TipoCampoExpressao.CEP_ONLINE:
					resposta = this.validarCampoRespostaAberta(regra, resposta, regra);
					break;
				case TipoCampoExpressao.CONTA_CAIXA:
					resposta = this.validarCampoRespostaAberta(regra, resposta, regra);
					break;	
			}
		}
		return resposta;
	}

	private validarOpcoesSelecionadas(regra: any, campo: CampoFormulario, resposta: boolean) {
		let listaResposta = [];
		let qtdResposta = regra.valores.length;
		if(campo.string_selecionadas) {
			for (let valor of campo.string_selecionadas) {
				let objSelecao: any = campo.opcoes_disponiveis.filter(opc => opc.valor_opcao == valor);
				if(objSelecao && objSelecao.length > 0 && regra.valores.includes(objSelecao[0].descricao_opcao)) {
					listaResposta.push(true);
				}
			}
			resposta = listaResposta && listaResposta.length > 0 && listaResposta.length == qtdResposta && !listaResposta.includes(false);
		}
		return resposta;
	}

	private validarCampoRespostaAberta(campo: CampoFormulario, resposta: boolean, regra: any) {
		switch(campo.tipo_campo) {
			case TipoCampoExpressao.TEXT:
				resposta = campo.resposta_aberta != null && campo.resposta_aberta != "" && (regra && regra.valores && regra.valores.length == 0 || regra.valores.includes(campo.resposta_aberta) || this.percorrerValores(regra, campo));
				break;
			case TipoCampoExpressao.COLOR:
				resposta = campo.resposta_aberta != null && campo.resposta_aberta != "" && (regra && regra.valores && regra.valores.length == 0 || this.percorrerValores(regra, campo));
				break;
			case TipoCampoExpressao.DATE:
				let dataConvert =  moment(campo.resposta_aberta).format("DD/MM/YYYY");
				resposta = campo.resposta_aberta != null && campo.resposta_aberta != "" && (regra && regra.valores && regra.valores.length == 0 || regra.valores.includes(dataConvert));
				break;
			case TipoCampoExpressao.SELECT:
				resposta = campo.resposta_aberta != null && campo.resposta_aberta != "" && (regra && regra.valores && regra.valores.includes(campo.resposta_aberta));
				break;
			case TipoCampoExpressao.RADIO:
				resposta = campo.resposta_aberta != null && campo.resposta_aberta != "" && (regra && regra.valores && regra.valores.includes(campo.resposta_aberta));
				break;
			default:
				resposta = campo.resposta_aberta != null && campo.resposta_aberta != "" && (regra && regra.valores && regra.valores.length == 0 || regra.valores.includes(campo.resposta_aberta) || this.percorrerValores(regra, campo));
			break;
		}
		return resposta;
	}

	private percorrerValores(regra: any, campo: CampoFormulario){
		let valorOk: boolean = false;
		if(regra && regra.valores && regra.valores.length > 0){
			for(let valor of regra.valores){
				let valorString: string = valor;
				if(valorString.toUpperCase() === campo.resposta_aberta.toUpperCase()){
					valorOk = true;
					break;
				}
			}
		}
		return valorOk;
	}
}