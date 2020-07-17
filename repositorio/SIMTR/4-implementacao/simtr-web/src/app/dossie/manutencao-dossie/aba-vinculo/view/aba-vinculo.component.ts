import { Component, DoCheck, EventEmitter, Input, IterableDiffers, OnChanges, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DialogService } from 'angularx-bootstrap-modal/dist/dialog.service';
import { MSG_MODAL_PESSOA, LOCAL_STORAGE_CONSTANTS, UNDESCOR } from 'src/app/constants/constants';
import { VinculoCliente, VinculoGarantia, VinculoProduto } from '../../../../model';
import { VinculoArvore } from '../../../../model/model-arvore-generica-dossie-produto/vinculos-model/vinculos-arvore-model/vinculo-arvore';
import { AlertMessageService, ApplicationService } from '../../../../services';
import { Utils } from '../../../../utils/Utils';
import { ModalGarantiaComponent } from '../../aba-formulario/modal/modal-garantia/modal-garantia.component';
import { ModalPessoaSimtrComponent } from '../../aba-formulario/modal/modal-pessoa-simtr/modal-pessoa-simtr.component';
import { ModalPessoaComponent } from '../../aba-formulario/modal/modal-pessoa/modal-pessoa.component';
import { ModalProdutoComponent } from '../../aba-formulario/modal/modal-produto/modal-produto.component';
import { AbaVinculo } from '../model/aba-vinculo.model';
import { AbaVinculoComponentPresenter } from '../presenter/aba-vinculo.component.presenter';
import { ModalDadosDeclaradosComponent } from '../../../../cliente/consulta-cliente/aba-documentos/modal/modal-dados-declarados/view/modal-dados-declarados.component';
import { TipoDocumentoService } from '../../../../cruds/tipo-documento/service/tipo-documento.service';
import { IdentificadorDossieFase } from '../../aba-formulario/modal/modal-pessoa/model/identificadorDossieEFase';
import { ModalProdutoVisualizacaoComponent } from '../../aba-formulario/modal/modal-produto-visualizacao/modal-produto-visualizacao.component';
import { LoadingAbaVinculo } from '../model/loading-aba-vinculo.model';
import { TipoVinculo } from 'src/app/documento/enum-tipo-vinculo/tipo-vinculo.enum';
import { ModalGarantiaVisualizacaoComponent } from '../../aba-formulario/modal/modal-garantia-visualizacao/modal-garantia-visualizacao.component';

declare var $: any;

@Component({
  selector: 'aba-vinculo',
  templateUrl: './aba-vinculo.component.html',
  styleUrls: ['./aba-vinculo.component.css']
})
export class AbaVinculoComponent extends AlertMessageService implements OnInit, OnChanges, DoCheck {

  @Input() cliente: VinculoCliente;
  @Input() identificadorDossieFase: IdentificadorDossieFase;
  @Input() processo;
  @Input() nuEtapa;
  @Input() processoEscolhido;
  @Input() idProcessoFase;
  @Input() clienteLista;
  @Input() icTipoPessoa;
  @Input() exibeVincularPessoa;
  @Input() exibeLoadArvore;
  @Input() produtosVinculados: any[];
  @Input() cnpj: string;
  @Input() idDossie;
  @Input() habilitaAlteracao;
  @Input() naoExisteVinculoPessoaPrincipal;
  @Input() listaTipoRelacionamento: any[];
  @Input() listaVinculoArvore: Array<VinculoArvore>;
  @Input() exibeVincularProduto;
  @Input() garantias;
  @Input() exibeVincularGarantia;
  @Input() produtoLista: VinculoProduto[];
  @Input() novosVinculos: VinculoCliente[];
  @Input() changeProduto: boolean;
  @Input() changeGarantia: boolean;
  @Output() produtoListaChanged: EventEmitter<VinculoProduto[]> = new EventEmitter<VinculoProduto[]>();
  @Output() garantiasChanged: EventEmitter<VinculoGarantia[]> = new EventEmitter<VinculoGarantia[]>();
  @Output() primeiroVinculoPessoa: EventEmitter<number> = new EventEmitter<number>();
  @Output() exibeLoadArvoreChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() clienteListaChanged: EventEmitter<VinculoCliente[]> = new EventEmitter<VinculoCliente[]>();
  @Output() listaVinculoArvoreChanged: EventEmitter<Array<VinculoArvore>> = new EventEmitter<Array<VinculoArvore>>();
  produtoEncontrado: boolean = false;
  differ: any;
  abaVinculoPresenter: AbaVinculoComponentPresenter;
  porte: string;

  constructor(
    private dialogService: DialogService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    abaVinculoPresenter: AbaVinculoComponentPresenter,
    private differs: IterableDiffers,
    private appService: ApplicationService,
    private tipoDocumentoServico: TipoDocumentoService,
  ) {
    super();
    this.differ = this.differs.find([]).create(null);
    this.abaVinculoPresenter = abaVinculoPresenter;
    this.abaVinculoPresenter.abaVinculo = new AbaVinculo();
    this.abaVinculoPresenter.loadingAbaVinculo = new LoadingAbaVinculo();
  }

  ngOnInit() {
    this.init();
  }

  ngOnChanges() {
    this.init();
  }

  ngDoCheck() {
    this.abaVinculoPresenter.ordenarListasOrdemAlfabetica(this.differ);
  }

  init() {
    this.initModelPresenter();
  }

  /**
 * Inicializa variaveis dinamicas no model do presenter
 */
  private initModelPresenter() {
    this.abaVinculoPresenter.aguardarCarregamentoVinculosCliente(this.clienteLista, this.activatedRoute.snapshot.params);
    this.exibeVincularGarantia = false;
    this.abaVinculoPresenter.abaVinculo.processoEscolhido = this.processoEscolhido;
    this.abaVinculoPresenter.abaVinculo.idProcessoFase = this.idProcessoFase;
    this.abaVinculoPresenter.abaVinculo.produtoLista = this.produtoLista ? this.produtoLista : [];
    this.abaVinculoPresenter.abaVinculo.garantias = this.garantias ? this.garantias : [];
    this.abaVinculoPresenter.abaVinculo.listaVinculoArvore = this.listaVinculoArvore;
    this.abaVinculoPresenter.abaVinculo.produtosVinculados = this.produtosVinculados;
    this.abaVinculoPresenter.abaVinculo.produtoListaChanged = this.produtoListaChanged;
    this.abaVinculoPresenter.abaVinculo.listaVinculoArvoreChanged = this.listaVinculoArvoreChanged;
    this.abaVinculoPresenter.abaVinculo.novosVinculos = this.novosVinculos;
    this.abaVinculoPresenter.abaVinculo.clienteLista = this.clienteLista;
    this.abaVinculoPresenter.abaVinculo.cliente = this.cliente;
    this.abaVinculoPresenter.abaVinculo.clienteListaChanged = this.clienteListaChanged;
    this.abaVinculoPresenter.abaVinculo.primeiroVinculoPessoa = this.primeiroVinculoPessoa;
    this.abaVinculoPresenter.abaVinculo.exibeLoadArvoreChanged = this.exibeLoadArvoreChanged;
    this.abaVinculoPresenter.abaVinculo.cnpj = this.cnpj;
    this.abaVinculoPresenter.abaVinculo.icTipoPessoa = this.icTipoPessoa;
    this.abaVinculoPresenter.abaVinculo.listaTipoRelacionamento = this.listaTipoRelacionamento;
    this.abaVinculoPresenter.abaVinculo.garantiasChanged = this.garantiasChanged;
    if (this.existeGarantiasVisualizacao()) {
      this.exibeVincularGarantia = true;
    }
    this.abaVinculoPresenter.carregarVinculosClientesProdutosGarantias(this.identificadorDossieFase, this.changeProduto, this.changeGarantia);
  }

  /**
   * Verifica se existe produtos vinculado ao porcesso atual para serem selecionados e incluidos 
   * na grid de produto
   */
  existeProdutosVisualizacao():boolean{
    return this.produtosVinculados && this.produtosVinculados.length > 0
  }

  /**
   * Verifica se existe garantias vinculadas aos produtos ou processo para serem selecionadas e incluidas
   * na grid de garantias
   */
  existeGarantiasVisualizacao():boolean{
    return (this.garantias && this.garantias.length > 0) || 
            (this.produtosVinculados &&
            this.produtosVinculados.filter(produto => produto.garantias_vinculadas && produto.garantias_vinculadas.length > 0).length > 0)
  }
  /**
   *
   * @param user
   */
  showModalSIMTR(user: VinculoCliente) {
    if (this.porte) {
      user.porte = this.porte;
    }
    this.dialogService.addDialog(ModalPessoaSimtrComponent, {
      user: user,
      porte: this.porte,
      identificadorDossieFase: this.identificadorDossieFase,
    }).subscribe(porte => {
      this.porte = porte;
    });
  }

  /**
   *
   * @param vinculoProduto 
   */
  showModalProdutoVisualizacao(vinculoProduto) {
    if(this.existeProdutosVisualizacao()){
      let produto = this.produtosVinculados.find(produto => produto.id === vinculoProduto.id);
      this.dialogService.addDialog(ModalProdutoVisualizacaoComponent, {
        vinculoProduto: vinculoProduto,
        produto: produto
      });
    }
  }

  /**
 *
 * @param vinculoGarantia 
 */
  showModalGarantiaVisualizacao(vinculoGarantia) {
    if((this.garantias && this.garantias.length > 0) || (this.produtosVinculados && this.produtosVinculados.filter(produto => produto.garantias_vinculadas && produto.garantias_vinculadas.length > 0).length > 0)){
      let buscarGarantiasProcesso = JSON.parse(this.appService.getItemFromLocalStorage(LOCAL_STORAGE_CONSTANTS.processoDossie + UNDESCOR + this.processoEscolhido.id));
      let garantia = buscarGarantiasProcesso.garantias_vinculadas.find(garantia => garantia.id === vinculoGarantia.garantia || garantia.id === vinculoGarantia.valor_garantia);
      this.dialogService.addDialog(ModalGarantiaVisualizacaoComponent, {
        vinculoGarantia: vinculoGarantia,
        garantia: garantia
      });
    }
  }

  showModalSICLI(user: VinculoCliente) {
    this.abaVinculoPresenter.showModalSICLI(user);
  }

  /**
   * Chamada para Dossie Cliente
   * @param tela_cpfCnpj 
   */
  linkDossieCliente(tela_cpfCnpj: any) {
    this.router.navigate(['principal', tela_cpfCnpj]);
  }

  /**
   * Método responsável por mostrar a Modal de inclusão de Dados de Pessoa na tabela do Formulário
   */
  showModalPessoa(inserir: boolean, vinculoCliente: any) {
    let existeVinculoClientePrincipal = this.abaVinculoPresenter.existeVinculoClientePrincipalNoDossieProduto();
    let listaModalTipoRelacionamento = existeVinculoClientePrincipal && inserir ? this.abaVinculoPresenter.filtrarLista() : this.listaTipoRelacionamento;
    this.dialogService.addDialog(ModalPessoaComponent, {
      listaTipoRelacionamento: listaModalTipoRelacionamento,
      listaCliente: this.abaVinculoPresenter.abaVinculo.clienteLista,
      vinculoCliente: vinculoCliente,
      tipoPessaGeraDossie: this.processo.tipo_pessoa,
      identificadorDossieFase: this.identificadorDossieFase,
    })
      .subscribe(retorno => {
        this.abaVinculoPresenter.montarVinculoCliente(retorno, inserir);
      });
  }

  mascaraCnpjCpf(v: any): void {
    return this.abaVinculoPresenter.mascaraCnpjCpf(v);
  }

  /**
   * Método responsável por mostrar a Modal de inclusão de Dados de Produto na tabela do Formulário
   */
  showModalProduto() {
    if(this.existeProdutosVisualizacao()){
      this.dialogService.addDialog(ModalProdutoComponent, {
        produtosVinculados: this.abaVinculoPresenter.abaVinculo.produtosVinculados,
        identificadorDossieFase: this.identificadorDossieFase
      }).subscribe(retorno => {
        this.abaVinculoPresenter.inicializaVinculoProduto(retorno);
      });
    }
  }

  /**
   * Método responsável por mostrar a Modal de inclusão de Dados de Garantia na tabela do Formulário
   */
  showModalGarantia() {
    if(this.existeGarantiasVisualizacao()){
      this.dialogService
        .addDialog(ModalGarantiaComponent, {
          produtos: this.abaVinculoPresenter.abaVinculo.produtoLista,
          produtosVinculados: this.abaVinculoPresenter.abaVinculo.produtosVinculados,
          clientes: this.abaVinculoPresenter.abaVinculo.clienteLista,
          processoEscolhido: this.abaVinculoPresenter.abaVinculo.processoEscolhido,
          processo: this.processo,
          identificadorDossieFase: this.identificadorDossieFase
        })
        .subscribe(retorno => {
          if (retorno) {
            this.abaVinculoPresenter.manterGarantiasServico(retorno);
            this.abaVinculoPresenter.inicializaVinculoGarantia(retorno, this.identificadorDossieFase);
          }
        });
    }
  }

  loadGarantia() {
    this.abaVinculoPresenter.loadGarantia();
  }

  preencheProdutoRetorno() {
    this.abaVinculoPresenter.preencheProdutoRetorno();
  }

  showModalDadosDeclarados(item) {

    let tipoPessoa = 'pessoa-juridica';
    if (item.tipo_pessoa === "F") {
      tipoPessoa = 'pessoa-fisica';
    }
    this.tipoDocumentoServico.consultarDadosDeclarados(tipoPessoa).subscribe(response => {
      this.dialogService.addDialog(ModalDadosDeclaradosComponent, {
        dadosDeclarados: response.atributos_extracao,
        idDossie: item.id,
        idTipoDocumento: response.identificador_tipo_documento,
        habilitaAlteracao: this.habilitaAlteracao
      }).subscribe(retorno => {
        if (retorno && retorno.resultado) {
          let messagesSuccess = new Array<string>();
          messagesSuccess.push('Registro salvo com sucesso.');
          this.alertMessagesSucessChanged.emit(messagesSuccess);
        } else {
          if (retorno) {
            this.addMessageError('Erro ao salvar Dados Declarados.');
            throw retorno.mensagem;
          }
        }
      }, error => {
        this.addMessageError('Erro ao salvar Dados Declarados.');
        throw error;
      });
    }, error => {
    });

  }
  /**
   * Metodo responsavel por excluir vinculo Cliente
   * @param index 
   * @param cnpj 
   * @param cpf 
   * @param tipoRel 
   */
  removePessoa(index: number, cnpj: string, cpf: string, tipoRel: String) {
    this.abaVinculoPresenter.removePessoa(index, cnpj, cpf, tipoRel, this);
  }

  removeProduto(index: number, produto: any) {
    this.abaVinculoPresenter.removeProduto(index, produto, this);
  }

  showAlertMessageWarning(msgs: string) {
    this.alertMessagesWarningChanged.emit(Object.assign([], msgs));
  }

  removeGarantia(index: number) {
    Utils.showMessageConfirm(this.dialogService, MSG_MODAL_PESSOA.CONFIRMA_EXCLUIR).subscribe(res => {
      this.abaVinculoPresenter.onSucessConfirmRemoveGarantia(res, index);
    });
  }

  habilitarBtns(pessoa: any) {
    return this.abaVinculoPresenter.habilitarBtns(pessoa);
  }

  decideTipoRelacionamento(input) {
    return this.abaVinculoPresenter.decideTipoRelacionamento(input);
  }

  decideFormaGaratia(input) {
    return this.abaVinculoPresenter.decideFormaGaratia(input);
  }

  decidePeriodoJuros(input) {
    return this.abaVinculoPresenter.decidePeriodoJuros(input);
  }

  formatValor(v: any) {
    return Utils.formatMoney(v, 2, "  ", ".", ",");
  }

  realizarLoad(tipoVinculo) {
    this.abaVinculoPresenter.countRowToProgressBar(tipoVinculo);
  }

  consultarSociosReceitaFederal(cliente: any) {
    this.abaVinculoPresenter.consultarSociosReceitaFederal(cliente, this);
  }

}
