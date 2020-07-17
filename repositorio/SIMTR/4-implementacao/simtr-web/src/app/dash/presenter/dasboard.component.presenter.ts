import { Injectable } from "@angular/core";
import { UnidadeAnalisada } from "../model/unidade-analisada.model";
import { Dashboard } from "../model/dashboard.model";
import { DashboardViewChild } from "../model/dashboard.viewchild.model";
import * as Chart from 'chart.js'
import { DashboardService } from "../dashboard.service";
import { LoaderService, AuthenticationService, ApplicationService } from "src/app/services";
import { DashComponent } from "../view/dashboard.component";
import { environment } from 'src/environments/environment';
import { PERFIL_ACESSO, DASHBOARD, EVENTO_DASHBOARD } from "src/app/constants/constants";
import { LoadingModel } from "../tabela-situacao/model/loading.model";

declare var $: any;

@Injectable()
export class DashboardCompoenentPresenter {

	unidadeAnalisada: UnidadeAnalisada;
	dashboard: Dashboard;
	dashboardViewChild: DashboardViewChild;
	loadingModel: LoadingModel;

	constructor(private service: DashboardService,
		private authenticationService: AuthenticationService,
		private appService: ApplicationService) {
	}

	getDados(dashboardComponent: DashComponent) {
		this.service.getDados()
			.subscribe(dados => {
				this.onSucessDadosGraphic(dados, dashboardComponent);
			}, error => {
				dashboardComponent.addMessageWarning(error.error.mensagem);
				throw error;
			});

	}

	getCustomDados(dashboardComponent: DashComponent) {
		this.dashboard = new Dashboard();
		this.service.getCustomDados(this.unidadeAnalisada)
			.subscribe(customDados => {
				this.onSucessDadosGraphic(customDados, dashboardComponent);
			}, error => {
				this.dashboard.emptyResumoProcesso = true;
				dashboardComponent.addMessageWarning(error.error.mensagem);
				dashboardComponent.addMessageWarning(error.error.detalhe);
				this.dashboardViewChild.resumoProcesso.nativeElement.className = "hidden";
				throw error
			});
	}

	/**
	 * Sucesso ao retornar dados do dashboard
	 * @param customDados 
	 */
	private onSucessDadosGraphic(dados: any, dashboardComponent: DashComponent) {
		this.dashboard.collapse = false;
		this.dashboard.collapseAll = true;
		if (dados.dossies_produto.length > 0) {
			this.dashboardViewChild.resumoProcesso.nativeElement.classList.remove("hidden");
			this.dashboard.estat = dados;
			this.dashboard.listaSituacao = Object.assign(new Array<any>(), this.dashboard.estat.dossies_produto);
			this.dashboard.listaProcesso = Object.assign(new Array<any>(), this.dashboard.estat.dossies_produto);
			this.configuraDadosDataTable();
			this.dashboard.resumo_situacao = dados.resumo_situacao;
			this.criaEstatistica();
			this.dashboard.qtdLista = dados.dossies_produto.length;
			this.dashboard.contadorLista = 0;
			this.dashboard.situacoes = Object.keys(this.dashboard.totais);
			this.dashboard.atributos = Object.keys(this.dashboard.resumo_situacao);
			this.dashboard.processos = Object.keys(this.dashboard.totaisProcesso);
			this.dashboard.atributosOriginal = this.dashboard.atributos;
			this.montaGrafico();
			this.mudarExibicaoEvento(false);
			this.expandeBoxDossie();
			this.expandeBoxSituacao();
			this.expandeBoxProcesso();
		} else {
			this.dashboard.listaSituacao = Object.assign(new Array<any>(), []);
			this.dashboard.listaProcesso = Object.assign(new Array<any>(), []);
			this.dashboardViewChild.resumoProcesso.nativeElement.className = "hidden";
			dashboardComponent.addMessageWarning(DASHBOARD.MSG_DADOS_VAZIO);
		}
		this.loadingModel.situacaoProcesso = false;
		this.loadingModel.resunoProcesso = false;
		this.loadingModel.resumoSituacao = false;
	}

	getTipoPessoa(dossie) {
		if (dossie.cnpj) {
			return 1
		} else {
			if (dossie.cpf) {
				return 2
			}
		}
	}

	configuraDadosDataTable() {
		let dossies = this.dashboard.estat.dossies_produto;
		for (let i in dossies) {
			(dossies[i] as any).fieldProdutosContratados = this.pegaProdutos(dossies[i].produtos_contratados);
			(dossies[i] as any).fieldUnidadesContratadas = this.pegaUnidadeAtendimento(dossies[i].unidades_tratamento);
		}
	}


	pegaProdutos(produtos) {
		if (produtos) {
			let prods = ''
			for (let i = 0; i < produtos.length; i++) {
				prods += produtos[i].codigo_operacao + ' '
			}
			return prods
		}
	}

	pegaUnidadeAtendimento(unidades) {
		const uni = unidades.toString()
		if (uni) {
			return uni.replace(',', '-')
		}
	}

	pegaCorProcesso(processo: any, configProcessos: any) {
		for (let i = 0; i < configProcessos.length; i++) {
			if (configProcessos[i].status === processo) {
				return configProcessos[i].cor;
			}
		}
	}

	getDossiesProcesso(tipo, estat: any) {
		const retorno = [];
		for (let i = 0; i < estat.dossies_produto.length; i++) {

			if (estat.dossies_produto[i].processo_dossie === tipo) {
				retorno.push(estat.dossies_produto[i]);
			}
		}
		return retorno;
	}

	mudaExibicaoSituacao(atributos: any[], totais: any[], atributosOriginal: any, all: boolean) {
		if (!all) {
			const temp = []
			for (let i = 0; i < atributos.length; i++) {
				if (totais[atributos[i]] > 0) {
					temp.push(atributos[i])
				}
			}
			return temp;

		} else {
			return atributosOriginal;

		}
	}

	criaEstatistica() {
		for (let i = 0; i < this.dashboard.estat.dossies_produto.length; i++) {
			if (this.dashboard.totais[this.dashboard.estat.dossies_produto[i].situacao_atual]) {
				this.dashboard.totais[this.dashboard.estat.dossies_produto[i].situacao_atual]++
			} else {
				this.dashboard.totais[this.dashboard.estat.dossies_produto[i].situacao_atual] = 1
			}
			if (this.dashboard.totaisProcesso[this.dashboard.estat.dossies_produto[i].processo_dossie]) {
				this.dashboard.totaisProcesso[this.dashboard.estat.dossies_produto[i].processo_dossie]++
			} else {
				this.dashboard.totaisProcesso[this.dashboard.estat.dossies_produto[i].processo_dossie] = 1
			}
		}
	}

	montaGrafico() {
		$(() => {
			/* ChartJS
			* -------
			* Here we will create a few charts using ChartJS
			*/


			//-------------
			//- PIE CHART -
			//-------------
			// Get context with jQuery - using jQuery's .get() method.
			const pieChartCanvas = $('#pieChart').get(0).getContext('2d');

			const pieData = [];


			for (let i = 0; i < this.dashboard.processos.length; i++) {

				this.dashboard.dossiesProcesso = this.getDossiesProcesso(this.dashboard.processos[i], this.dashboard.estat);
				const item = {
					value: this.dashboard.dossiesProcesso.length,
					label: this.dashboard.processos[i],
					color: this.pegaCorProcesso(this.dashboard.processos[i], this.dashboard.configProcessos),
					highlight: this.pegaCorProcesso(this.dashboard.processos[i], this.dashboard.configProcessos)

				}
				pieData.push(item)
			}

			const pieOptions = {
				//Boolean - Whether we should show a stroke on each segment
				segmentShowStroke: true,
				//String - The colour of each segment stroke
				segmentStrokeColor: '#fff',
				//Number - The width of each segment stroke
				segmentStrokeWidth: 2,
				//Number - The percentage of the chart that we cut out of the middle
				percentageInnerCutout: 50, // This is 0 for Pie charts
				//Number - Amount of animation steps
				animationSteps: 2,
				//String - Animation easing effect
				animationEasing: 'easeOutBounce',
				//Boolean - Whether we animate the rotation of the Doughnut
				animateRotate: true,
				//Boolean - Whether we animate scaling the Doughnut from the centre
				animateScale: false,
				//Boolean - whether to make the chart responsive to window resizing
				responsive: true,
				// Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
				maintainAspectRatio: true,


				//String - A legend template
				legendTemplate: '<ul ><% for (var i=0; i<segments.length; i++){%><li><span style="background-color:<%=segments[i].fillColor%>"></span><%if(segments[i].label){%><%=segments[i].label%>( <b><%=segments[i].value%></b> )<%}%></li><%}%></ul>'
			}
			//Create pie or douhnut chart
			// You can switch between pie and douhnut using the method below.

			const grafico = new Chart(pieChartCanvas).Doughnut(pieData, pieOptions);


			var originalShowTooltip = grafico.showTooltip;
			grafico.showTooltip = function (activeElements) {
				$("#pieChart").css("cursor", activeElements.length ? "pointer" : "default");
				originalShowTooltip.apply(this, arguments);
			}

			this.dashboard.chart = grafico;

			const html = grafico.generateLegend();
			this.dashboardViewChild.legenda.nativeElement.innerHTML = html;
		});
	}

	mudarExibicaoEvento(all: boolean) {
		let attr;
		this.dashboard.collapseAll = !all;
		this.dashboard.all = all;
		attr = this.mudaExibicaoSituacao(this.dashboard.atributos, this.dashboard.totais, this.dashboard.atributosOriginal, all);
		this.dashboard.atributos = attr;
		if (this.dashboard.collapse && !this.dashboard.collapseAll && all) {
			this.dashboard.collapse = false;
		}
	}

	expandeResumoPorSituacao() {
		this.dashboard.collapse = true;
		this.dashboard.collapseAll = true;
	}

	recolheResumoPorSituacao() {
		this.dashboard.collapse = false;
		if (this.dashboard.all) {
			this.mudarExibicaoEvento(false);
		}
	}

	pegaCor(situacao) {
		for (let i = 0; i < this.dashboard.config.length; i++) {
			if (this.dashboard.config[i].status === situacao) {
				return this.dashboard.config[i].cor
			}
		}
	}

	pegaIcone(situacao) {
		for (let i = 0; i < this.dashboard.config.length; i++) {
			if (this.dashboard.config[i].status === situacao) {
				return this.dashboard.config[i].icone
			}
		}
	}

	pegaQtde(situacao) {
		if (this.dashboard.totais[situacao]) {
			return this.dashboard.totais[situacao]
		} else
			return 0
	}

	changeTabResumoSituacao(item) {
		this.dashboard.eventoSituacao = item;
		this.expandeBoxDossie();
		this.expandeBoxSituacao();
		this.offsetSituacao();
	}

	private offsetSituacao() {
		$('html, body').animate({
			scrollTop: $("#collapseSituacao").offset().top
		}, 2000);
	}

	changeTabResumoProcesso(evt) {
		if (this.dashboard.chart) {
			let eventoProc: string;
			let elemento = this.dashboard.chart.getSegmentsAtEvent(evt);
			for (let i = 0; this.dashboard.processos.length > i; i++) {
				if (elemento[0] && this.dashboard.processos[i] == elemento[0].label) {
					this.dashboard.dossiesProcesso = this.getDossiesProcesso(this.dashboard.processos[i], this.dashboard.estat);
					this.dashboard.indexProcesso = i + 1;
					eventoProc = this.dashboard.dossiesProcesso[this.dashboard.indexProcesso].processo_dossie;
					break;
				}
			}
			this.dashboard.eventoProcesso = eventoProc;
			this.expandeBoxDossie();
			this.expandeBoxProcesso();
			this.offsetProcesso();
		}
	}

	private offsetProcesso() {
		$('html, body').animate({
			scrollTop: $("#collapseProcesso").offset().top
		}, 2000);
	}

	formataSituacao(situacao): string {
		const dados = situacao.split(' ')
		if (dados.length === 1) {
			return situacao += '\n\n';
		} else {
			return situacao;
		}
	}

	/**
	 * Verifica se o usuário logado possui a permissao: MTRADM ou MTRSDNMTZ
	 */
	hasCredentialUnidadeAnalisada(): boolean {
		const credentials: string = `${PERFIL_ACESSO.MTRADM},${PERFIL_ACESSO.MTRSDNMTZ}`;
		if (!environment.production) {
			return this.appService.hasCredential(credentials, this.authenticationService.getRolesInLocalStorage());
		} else {
			return this.appService.hasCredential(credentials, this.authenticationService.getRolesSSO());
		}
	}

	carregarListaDossieSituacao(sitauacao: string) {
		if (this.dashboard.estat.dossies_produto && this.dashboard.estat.dossies_produto.length > 0) {
			this.dashboard.estat.dossies_produto.forEach(dossie => {
				if (dossie.situacao_atual == sitauacao || sitauacao == EVENTO_DASHBOARD.TODOS) {
					this.dashboard.listaSituacao.push(dossie);
				}
			});
		}
	}

	expandeBoxSituacao() {
		$(function () {
			if (!$('#collapseSituacao').hasClass('in')) {
				$('#btnExpandedSituacao').click();
			}
		})
	}

	expandeBoxProcesso() {
		$(function () {
			if (!$('#collapseProcesso').hasClass('in')) {
				$('#btnExpandedProcesso').click();
			}
		})
	}

	expandeBoxDossie() {
		$(function () {
			if (!$('#collapseDossie').hasClass('in')) {
				$('#btnExpandedDossie').click();
			}
		})
	}
}