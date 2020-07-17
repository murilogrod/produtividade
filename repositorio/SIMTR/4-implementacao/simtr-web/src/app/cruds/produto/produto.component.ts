import { Component, OnInit } from '@angular/core';
import { ConfirmationService, SelectItem } from 'primeng/primeng';
import { CrudProdutoService } from './produto.service';
import { CrudProduto } from './../model/crud-produto';
import { LoaderService } from 'src/app/services';



@Component({
	selector: 'crud-produto',
	templateUrl: './produto.component.html',
	styleUrls: ['./produto.component.css']
})
export class CrudProdutoComponent implements OnInit {
	entidade: CrudProduto;
	public itens: any = [];
	public telaCadastro = false;
	public tiposDocumento: SelectItem[] = []
	public tipoDocumentoSelecionado;
	public tiposDocumentoView = []
	public naoPodeIncluirTipoDocumento = false;
	public usoDossieDigital = false
	public contratacaoConjunta = false
	public pesquisaCadin = false
	public pesquisaCcf = false
	public pesquisaReceita = false
	public pesquisaScpc = false
	public pesquisaSerasa = false
	public pesquisaSicow = false
	public pesquisaSinad = false
	public tipoPessoa = 'F'
	public codigo_produto_portal_empreendedor;
	public operacao = ''
	public modalidade = ''
	public mensagem = '';
	public exibirMensagem = false;
	public tipoMensagem = 'info'
	public tipoIcone = 'info'


	constructor(private service: CrudProdutoService, private confirmationService: ConfirmationService, private loadService: LoaderService) {

	}

	ngOnInit() {
		this.entidade = new CrudProduto();

		this.service.get().subscribe(dados => {
			this.itens = dados
			
			this.classificaItens();
		},
		() => {
			this.loadService.hide();
		}); 

	}

	limpaTela(form) {
		form.control.markAsPristine();
		form.control.markAsUntouched();
		this.entidade = new CrudProduto();
		this.tipoPessoa = 'F'
		this.codigo_produto_portal_empreendedor = undefined;
		this.contratacaoConjunta = false
		this.pesquisaCadin = false
		this.pesquisaCcf = false
		this.pesquisaReceita = false;
		this.pesquisaScpc = false;
		this.pesquisaSerasa = false
		this.pesquisaSicow = false
		this.pesquisaSinad = false
		this.exibirMensagem = false
		this.operacao = ''
		this.modalidade = ''
		this.mensagem = ''
		this.tipoIcone = 'info'
		this.tipoMensagem = 'info'

	}
	salvar(form) {
		this.entidade.indicador_contratacao_conjunta = this.contratacaoConjunta
		this.entidade.indicador_dossie_digital = this.usoDossieDigital
		this.entidade.indicador_pesquisa_cadin = this.pesquisaCadin
		this.entidade.indicador_pesquisa_ccf = this.pesquisaCcf
		this.entidade.indicador_pesquisa_receita = this.pesquisaReceita
		this.entidade.indicador_pesquisa_scpc = this.pesquisaScpc
		this.entidade.indicador_pesquisa_serasa = this.pesquisaSerasa
		this.entidade.indicador_pesquisa_sicow = this.pesquisaSicow
		this.entidade.indicador_pesquisa_sinad = this.pesquisaSinad
		this.entidade.indicador_tipo_pessoa = this.tipoPessoa
		this.entidade.codigo_produto_portal_empreendedor = this.codigo_produto_portal_empreendedor;
		this.entidade.operacao_produto = Number.parseInt(this.entidade.operacao_produto)
		this.entidade.modalidade_produto = Number.parseInt(this.entidade.modalidade_produto)
		this.entidade.nome_produto = this.entidade.nome_produto.toUpperCase();

		if (this.entidade.identificador_produto == null) {

			this.service.save(this.entidade).subscribe(() => {

				this.limpaTela(form)
				this.mensagem = 'Registro cadastrado com sucesso.'
				this.tipoIcone = 'info'
				this.tipoMensagem = 'info'
				this.exibirMensagem = true

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
			let x = {
				indicador_contratacao_conjunta: this.entidade.indicador_contratacao_conjunta,
				indicador_dossie_digital: this.entidade.indicador_dossie_digital,
				indicador_pesquisa_cadin: this.entidade.indicador_pesquisa_cadin,
				indicador_pesquisa_ccf: this.entidade.indicador_pesquisa_ccf,
				indicador_pesquisa_receita: this.entidade.indicador_pesquisa_receita,
				indicador_pesquisa_scpc: this.entidade.indicador_pesquisa_scpc,
				indicador_pesquisa_serasa: this.entidade.indicador_pesquisa_serasa,
				indicador_pesquisa_sicow: this.entidade.indicador_pesquisa_sicow,
				indicador_pesquisa_sinad: this.entidade.indicador_pesquisa_sinad,
				indicador_tipo_pessoa: this.entidade.indicador_tipo_pessoa,
				codigo_produto_portal_empreendedor: this.entidade.codigo_produto_portal_empreendedor,
				modalidade_produto: Number.parseInt(this.entidade.modalidade_produto),
				operacao_produto: Number.parseInt(this.entidade.operacao_produto),
				nome_produto: this.entidade.nome_produto.toUpperCase()
			}

			this.service.update(this.entidade.identificador_produto, x).subscribe(() => {
				this.mensagem = 'Registro alterado com sucesso.'
				this.tipoIcone = 'info'
				this.tipoMensagem = 'info'
				this.exibirMensagem = true
				this.service.get().subscribe(dados => {
					
					this.itens = dados;
					this.classificaItens();
					this.cancelar(form);
				},
				() => {
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
	}

	async alterar(dados) {

		this.entidade = Object.assign({}, dados);


		this.setUsosForm()
		this.tipoPessoa = this.entidade.indicador_tipo_pessoa;
		this.telaCadastro = true

	}

	excluir(dados, form) {

		this.confirmationService.confirm({
			message: 'Você confirma a exclusão deste Registro?',
			accept: () => {
				this.service.delete(dados.identificador_produto).subscribe(() => {
					this.service.get().subscribe(dados => {
						this.itens = dados;
						this.classificaItens()
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
		});

	}

	abrirModal() {
		this.telaCadastro = true;
		this.entidade = {};
		this.entidade.indicador_tipo_pessoa = 'F'
		this.contratacaoConjunta = false
		this.pesquisaCadin = false
		this.pesquisaCcf = false
		this.pesquisaReceita = false;
		this.pesquisaScpc = false;
		this.pesquisaSerasa = false
		this.pesquisaSicow = false
		this.pesquisaSinad = false
		this.exibirMensagem = false
		this.usoDossieDigital = false
	}

	cancelar(f) {
		this.limpaTela(f)
		this.telaCadastro = false;


	}

	mudouTipoDocumento() {

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


	classificaItens() {

		if (this.itens !== undefined && this.itens !== []) {
			this.itens.sort((n1, n2): number => {
				if (n1.nome_produto < n2.nome_produto) return -1;
				if (n1.nome_produto > n2.nome_produto) return 1;
				return 0;
			});
		}

	}

	setUsosForm() {
		this.pesquisaCadin = this.entidade.indicador_pesquisa_cadin
		this.pesquisaCcf = this.entidade.indicador_pesquisa_ccf
		this.pesquisaReceita = this.entidade.indicador_pesquisa_receita
		this.pesquisaScpc = this.entidade.indicador_pesquisa_scpc
		this.pesquisaSerasa = this.entidade.indicador_pesquisa_serasa
		this.pesquisaSicow = this.entidade.indicador_pesquisa_sicow
		this.pesquisaSinad = this.entidade.indicador_pesquisa_sinad
		this.contratacaoConjunta = this.entidade.indicador_contratacao_conjunta
		this.codigo_produto_portal_empreendedor = this.entidade.codigo_produto_portal_empreendedor;
	}



}
