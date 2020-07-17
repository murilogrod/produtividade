import { Component, OnInit } from '@angular/core';
import { MacroProcesso } from '../model/macro-processo';
import { MacroProcessoService } from './macro-processo.service';
import { ConfirmationService } from 'primeng/primeng';
import { LoaderService } from '../services';
declare var $: any;
@Component({
	selector: 'crud-macro-processo',
	templateUrl: './macro-processo.component.html',
	styleUrls: ['./macro-processo.component.css']
})
export class MacroProcessoComponent implements OnInit {
	macroProcesso: MacroProcesso;
	itens: any = [];
	tituloModal: String = "Inclusão de Macro Processo"

	constructor(private macroProcessoService: MacroProcessoService, private confirmationService: ConfirmationService, private loadService: LoaderService) {

	}

	ngOnInit() {
		this.macroProcesso = new MacroProcesso();
		this.macroProcesso.ic_ativo = "Sim"
		this.macroProcessoService.getMacroProcessos().subscribe(dados => {
			this.itens = dados.json();
		},
			() => {
				this.loadService.hide();
			});
	}

	limpaTela(form) {
		form.control.markAsPristine();
		form.control.markAsUntouched();
		this.macroProcesso = new MacroProcesso();
		this.macroProcesso.ic_ativo = "Sim"
		this.tituloModal = "Inclusão de Macro Processo";

	}
	salvar(form) {
		if (this.macroProcesso.nu_macroprocesso == null) {
			this.macroProcessoService.saveMacroProcessos(this.macroProcesso).subscribe(() => {
				this.limpaTela(form)
				$('#incluirMacroProcesso').modal('toggle')
				this.macroProcessoService.getMacroProcessos().subscribe(dados => {
					this.itens = dados.json();
				},
					() => {
						this.loadService.hide();
					});
			},
				() => {
					this.loadService.hide();
				});

		}
		else {
			this.macroProcessoService.updateMacroProcessos(this.macroProcesso).subscribe(() => {
				this.macroProcessoService.getMacroProcessos().subscribe(dados => {

					this.itens = dados.json();
					this.limpaTela(form)
				},
				() => {
				  this.loadService.hide();
				}); 
				$('#incluirMacroProcesso').modal('toggle')
			},
			() => {
			  this.loadService.hide();
			}); 
		}
	}

	alterar(dados) {
		this.tituloModal = "Alteração de Macro Processo";
		this.macroProcesso = Object.assign({}, dados);
	}

	excluir(dados, form) {

		this.confirmationService.confirm({
			message: 'Você confirma a exclusão deste Macro Processo?',
			accept: () => {
				this.macroProcessoService.deleteMacroProcesso(dados.nu_macroprocesso).subscribe(() => {
					this.macroProcessoService.getMacroProcessos().subscribe(dados => {
						this.itens = dados.json();
					},
					() => {
					  this.loadService.hide();
					}); 
				},
				() => {
				  this.loadService.hide();
				}); 
			}
		});

	}
	abrirModal(form) {
		this.limpaTela(form);

	}
}
