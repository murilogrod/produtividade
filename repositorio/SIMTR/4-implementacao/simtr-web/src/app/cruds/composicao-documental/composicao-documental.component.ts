import { Component, OnInit } from '@angular/core';

import { ConfirmationService, SelectItem } from 'primeng/primeng';

import { ComposicaoDocumentalService } from './composicao-documental.service';
import { CrudComposicaoDocumental } from '../model/crud-composicao-documental';
import { LoaderService } from 'src/app/services';

declare var $: any;

@Component({
	selector: 'crud-composicao-documental',
	templateUrl: './composicao-documental.component.html',
	styleUrls: ['./composicao-documental.component.css']
})
export class ComposicaoDocumentalComponent implements OnInit {
	entidade: CrudComposicaoDocumental;
	public itens: any = [];
	public tituloModal: String = "Inclusão de Função Documental"
	public telaCadastro = false;
	public tiposDocumento: SelectItem[] = []
	private tiposDocumentoOriginal: SelectItem[] = []
	public tipoDocumentoSelecionado;
	private tiposDocumentoId = [];
	public tiposDocumentoView = []
	public naoPodeIncluirTipoDocumento = false;
	public icConclusao = "false"
	public mensagem = '';
	public exibirMensagem = false;
	public tipoMensagem = 'info'
	public tipoIcone = 'info'

	constructor(private service: ComposicaoDocumentalService, private confirmationService: ConfirmationService, private loadService: LoaderService) {

	}

	ngOnInit() {
		this.entidade = new CrudComposicaoDocumental();
		this.service.get().subscribe(dados => {
			this.itens = dados
			
			this.classificaItens();
		},
			() => {
				this.loadService.hide();
			});


	}

	limpaTela(form) {
		form.control.markAsPristine();
		form.control.markAsUntouched();
		this.entidade = new CrudComposicaoDocumental();
		this.tituloModal = "Inclusão de Função Documental";
		this.naoPodeIncluirTipoDocumento = false
		this.tiposDocumento = this.tiposDocumentoOriginal
		this.tiposDocumentoView = []
		this.icConclusao = "false"
		this.exibirMensagem = false
		this.mensagem = ''
		this.tipoIcone = 'info'
		this.tipoMensagem = 'info'

	}
	salvar(form) {
		this.entidade.indicador_conclusao = this.icConclusao == "true" ? true : false
		if (this.entidade.id_composicao_documental == null) {
			this.service.save(this.entidade).subscribe(() => {
				this.limpaTela(form)
				this.mensagem = 'Registro cadastrado com sucesso.'
				this.tipoIcone = 'info'
				this.tipoMensagem = 'info'
				this.exibirMensagem = true;
				this.service.get().subscribe(dados => {
					this.itens = dados;
					this.classificaItens();
				},
				() => {
					this.loadService.hide();
				});
			}, error => {

				if (error.error.mensagem) {
					this.mensagem = error.error.mensagem
				} else {
					this.mensagem = error.message
				}

				this.tipoIcone = 'ban'
				this.tipoMensagem = 'error'
				this.exibirMensagem = true
				this.loadService.hide();
				throw error;
			});

		}
		else {
			this.service.update(this.entidade).subscribe(() => {
				this.mensagem = 'Registro alterado com sucesso.'
				this.tipoIcone = 'info'
				this.tipoMensagem = 'info'
				this.exibirMensagem = true
				this.service.get().subscribe(dados => {

					this.itens = dados;
					this.classificaItens();
					this.cancelar(form);
				},
					() => {
						this.loadService.hide();
					});

			}, error => {
				if (error.error.mensagem) {
					this.mensagem = error.error.mensagem
				} else {
					this.mensagem = error.message
				}
				this.tipoIcone = 'ban'
				this.tipoMensagem = 'error'
				this.exibirMensagem = true;
				this.loadService.hide();
				throw error;
			});
		}
	}

	async alterar(dados) {

		this.tituloModal = "Alteração de Função Documental";
		this.entidade = Object.assign({}, dados);

		this.setUsosForm()
		// if (this.entidade.tipos_documento) {
		//   this.preparaCombo()
		//   this.tiposDocumentoView = this.entidade.tipos_documento;
		//   await this.classificaTipoDocView();
		// } else {
		//   this.tiposDocumentoView = [];
		// }

		if (this.tiposDocumento.length == 0) {
			this.naoPodeIncluirTipoDocumento = true
		} else {
			this.naoPodeIncluirTipoDocumento = false
		}

		this.telaCadastro = true

	}

	excluir(dados, form) {

		this.confirmationService.confirm({
			message: 'Você confirma a exclusão deste Registro?',
			accept: () => {
				this.service.delete(dados.id_composicao_documental).subscribe(() => {
					this.service.get().subscribe(dados => {
						this.itens = dados;
						this.classificaItens()
					});
				}, error => {
					if (error.error.mensagem) {
						this.mensagem = error.error.mensagem
					} else {
						this.mensagem = error.message
					}

					this.tipoIcone = 'ban'
					this.tipoMensagem = 'error'
					this.exibirMensagem = true
					this.loadService.hide();
					throw error;
				});
			}
		});

	}

	abrirModal() {
		this.telaCadastro = true;
		this.entidade = {};
		this.naoPodeIncluirTipoDocumento = false
	}

	cancelar(f) {
		this.limpaTela(f)
		this.telaCadastro = false;
	}

	mudouTipoDocumento() {

	}

	excluirTipoDoc(tipo) {

		this.tiposDocumentoView = this.excluiDaLista(tipo, this.tiposDocumentoView)
		let x = {
			value: tipo.id_tipo_documento,
			label: tipo.nome_tipo_documento
		}
		this.tiposDocumento.push(x)
		this.tipoDocumentoSelecionado = this.tiposDocumento[0].value
		this.naoPodeIncluirTipoDocumento = false
		this.classificaTipoDocs();
	}

	excluiDaLista(x, array) {

		let vet = array.filter(function (k, index, arr) {

			if (k.id_tipo_documento) {
				return k.id_tipo_documento != x.id_tipo_documento;
			} else if (k.value) {
				return k.value != x.value;
			}
		});

		return vet
	}

	async incluirDocumento() {
		let x = {
			id_tipo_documento: this.tipoDocumentoSelecionado,
			nome_tipo_documento: this.tiposDocumentoId[this.tipoDocumentoSelecionado].nome_tipo_documento
		}


		this.tiposDocumentoView = [...this.tiposDocumentoView, x]
		await this.classificaTipoDocView()
		let y = {
			value: this.tipoDocumentoSelecionado,
			label: this.tiposDocumentoId[this.tipoDocumentoSelecionado].nome_tipo_documento

		}
		this.tiposDocumento = this.excluiDaLista(y, this.tiposDocumento)
		if (this.tiposDocumento.length > 0) {
			this.tipoDocumentoSelecionado = this.tiposDocumento[0].value
		} else {
			this.naoPodeIncluirTipoDocumento = true
		}
	}

	classificaItens() {

		if (this.itens !== undefined && this.itens !== []) {
			this.itens.sort((n1, n2): number => {
				if (n1.nome_composicao_documental < n2.nome_composicao_documental) return -1;
				if (n1.nome_composicao_documental > n2.nome_composicao_documental) return 1;
				return 0;
			});
		}

	}

	classificaTipoDocs() {

		if (this.tiposDocumento !== undefined && this.tiposDocumento !== []) {
			this.tiposDocumento.sort((n1, n2): number => {
				if (n1.label < n2.label) return -1;
				if (n1.label > n2.label) return 1;
				return 0;
			});
		}

	}

	async classificaTipoDocView() {

		if (this.tiposDocumentoView !== undefined && this.tiposDocumentoView !== []) {
			this.tiposDocumentoView.sort((n1, n2): number => {
				if (n1.nome_funcao_documental < n2.nome_funcao_documental) return -1;
				if (n1.nome_funcao_documental > n2.nome_funcao_documental) return 1;
				return 0;
			});
		}

	}

	setUsosForm() {
		if (this.entidade.indicador_conclusao) {
			this.icConclusao = "true"
		} else {
			this.icConclusao = "false"
		}
	}


}
