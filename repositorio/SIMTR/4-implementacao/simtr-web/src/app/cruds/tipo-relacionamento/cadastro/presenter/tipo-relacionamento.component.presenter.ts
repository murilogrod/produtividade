import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Utils } from "src/app/cruds/util/utils";
import { LoaderService } from "src/app/services";
import { TIPO_RELACIONAMENTO } from "../../constant.tipo-relacionamento";
import { GridTipoRelacionamento } from "../../pesquisa/model/grid-tipo-relacionamento";
import { TipoRelacionamentoService } from "../../service/tipo-relacionamento.service";
import { IncludeTipoRelacionamento } from "../model/include-tipo-relacionamento.model";
import { SelectTipoPessoa } from "../../../model/select-tipo-pessoa.interface";
import { TipoRelacionamento } from "../model/tipo-relacionamento";
import { TipoRelacionamentoModel } from "../model/tipo-relacionamento.model";
import { UpdateTipoRelacionamento } from "../model/update-tipo-relacionamento.model";
import { TipoRelacionamentoComponent } from "../view/tipo-relacionamento.component";

@Injectable()
export class TipoRelacionamentoPresenter {

    tipoRelacionamentoModel: TipoRelacionamentoModel;
    tipoRelacionamento: TipoRelacionamento;

    constructor(private tipoRelacionamentoService: TipoRelacionamentoService,
        private router: Router,
        private loadService: LoaderService) { }

    /**
     * Inicializa a configuração inicial do tipo relacionamento: verifica tipo relacionamento
     * carregamento listagem tipos relacionamentos para validação de nome
     * @param params 
     * @param tipoRelacionamentoComponent 
     */
    initConfigTipoRelacionamento(params: any, tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        this.carregamentoTiposRelacionamentos(params, tipoRelacionamentoComponent);
    }

    /**
     * Navega para a url passada como parâmetro
     * @param url 
     */
    navigateUrl(parameters: string[]) {
        this.router.navigate(parameters);
    }

    /**
     * Atualiza o tipo relacionamento depedendo do estado do mesmo: 
     * novo tipo relacionamento ou edição de tipo relacionamento existente
     * validação ocorrencia nome tipo relacionamento
     * @param tipoRelacionamentoComponent 
     * @param params 
     */
    checkUpdateTipoRelacionamento(tipoRelacionamentoComponent: TipoRelacionamentoComponent, params: any) {
        const nomeTipoRelacionamentoValido: boolean = this.validarNomeTipoRelacionamento();
        if (!nomeTipoRelacionamentoValido) {
            tipoRelacionamentoComponent.addMessageError(TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_MESMO_NOME);
        } else {
            if (this.checkParamIntUrl(params)) {
                this.updateTipoRelacionamento(tipoRelacionamentoComponent, Number(params.id));
            } else {
                this.saveTipoRelacionamento(tipoRelacionamentoComponent);
            }
        }
    }

    /**
     * Anula receitaPF
     * receitaPF e PJ nao podem ser ambas verdadeiras
     */
    changeReceitaPF() {
        this.tipoRelacionamento.indicador_receita_pj = false;
    }

    /**
     * Anula receitaPJ
     * receitaPJ e PF nao podem ser ambas verdadeiras
     */
    changeReceitaPJ() {
        this.tipoRelacionamento.indicador_receita_pf = false;
    }

    /**
     * Carrega a listagem de tiposRelacionamentos no componente tipoRelacionamento
     * para validação de ocorrencia de nome
     * @param params 
     * @param tipoRelacionamentoComponent 
     */
    private carregamentoTiposRelacionamentos(params: any, tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        if (this.tipoRelacionamentoService.existTiposRelacionamentos()) {
            this.tipoRelacionamentoModel.gridTipoRelacionamentos = this.tipoRelacionamentoService.getTiposRelacionamentos();
            this.inicializarTipoPessoasChecagemEdicaoTipoRelacionamento(params, tipoRelacionamentoComponent);
        }
        else {
            this.tipoRelacionamentoService.get().subscribe(tiposRelacionamentos => {
                this.tipoRelacionamentoModel.gridTipoRelacionamentos = tiposRelacionamentos;
                this.inicializarTipoPessoasChecagemEdicaoTipoRelacionamento(params, tipoRelacionamentoComponent);
            });
        }
    }

    /**
     * Checagem edição tipo relacionamento e montagem dropdown tipo pessoa
     * @param params 
     * @param tipoRelacionamentoComponent 
     */
    private inicializarTipoPessoasChecagemEdicaoTipoRelacionamento(params: any, tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        this.verificarEdicaoTipoRelacionamento(params, tipoRelacionamentoComponent);
        this.getTiposPessoas();
    }

    /**
     * Valida a ocorrencia mesmo nome tipo de relacionamento
     */
    private validarNomeTipoRelacionamento(): boolean {
        if (this.tipoRelacionamentoModel.editar) {
            const filterTiposRelacionamentos: Array<GridTipoRelacionamento> = this.tipoRelacionamentoModel.gridTipoRelacionamentos.filter(tipoRelacionamento => tipoRelacionamento.id !== this.tipoRelacionamento.id);
            return !filterTiposRelacionamentos.some(ocorrenciaTipoRelacionamento => ocorrenciaTipoRelacionamento.nome.toUpperCase().trim() == this.tipoRelacionamento.nome.toUpperCase().trim());
        } else {
            return !this.tipoRelacionamentoModel.gridTipoRelacionamentos.some(ocorrenciaTipoRelacionamento => ocorrenciaTipoRelacionamento.nome.toUpperCase().trim() == this.tipoRelacionamento.nome.toUpperCase().trim());
        }
    }

    /**
     * Recupera o tipo relacionamento passando id
     * @param id 
     * @param tipoRelacionamentoComponent 
     */
    private carregarTipoRelacionamento(id: number, tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        this.loadService.show();
        this.tipoRelacionamentoService.getById(id).subscribe(tipoRelacionamento => {
            this.tipoRelacionamento = tipoRelacionamento
            this.custumizeHeaderGrid(tipoRelacionamento);
            this.inicializarTipoPessoa();
            this.loadService.hide();
        }, error => {
            this.onErrorTipoRelacionamento(id, error, tipoRelacionamentoComponent);
        });
    }

    /**
     * Monta o valor para o dropdown tipo pessoa
     */
    private inicializarTipoPessoa() {
        let code: number;
        let name: string;
        let value: string;
        ({ code, name, value } = Utils.carregarTipoPessoa(code, name, value, this.tipoRelacionamento.tipo_pessoa));
        this.tipoRelacionamentoModel.selectedTipoPessoa =
            <SelectTipoPessoa>{
                code: code,
                name: name,
                value: value
            };
    }

    /**
     * inicializa dropdown com todas aos tipos de pessoas
     */
    private getTiposPessoas() {
        this.tipoRelacionamentoModel.tipoPessoas = Utils.getTiposPessoas();
    }

    /**
     * Salva um novo tipo relacionamento; relizando uma requisição post
     * @param tipoRelacionamentoComponent 
     */
    private saveTipoRelacionamento(tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        this.loadService.show();
        this.tipoRelacionamentoService.save(this.inicializarIncludeTipoRelacionamento()).subscribe(response => {
            this.tipoRelacionamentoService.setNovoTipoRelacionamento(true);
            this.tipoRelacionamentoService.setId(Number(response.body.id));
            this.navigateUrl([TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO]);
        }, error => {
            this.onErrorTipoRelacionamento(null, error, tipoRelacionamentoComponent);
        });
    }

    /**
     * Inicializa o objeto de inclusão de tipo relacionamento
     */
    private inicializarIncludeTipoRelacionamento(): IncludeTipoRelacionamento {
        const includeTipoRelacionamento: IncludeTipoRelacionamento = new IncludeTipoRelacionamento();
        includeTipoRelacionamento.nome = this.tipoRelacionamento.nome;
        includeTipoRelacionamento.tipo_pessoa = this.tipoRelacionamentoModel.selectedTipoPessoa.value;
        includeTipoRelacionamento.indicador_principal = this.tipoRelacionamento.indicador_principal;
        includeTipoRelacionamento.indicador_relacionado = this.tipoRelacionamento.indicador_relacionado;
        includeTipoRelacionamento.indicador_sequencia = this.tipoRelacionamento.indicador_sequencia;
        includeTipoRelacionamento.indicador_receita_pf = this.tipoRelacionamento.indicador_receita_pf;
        includeTipoRelacionamento.indicador_receita_pj = this.tipoRelacionamento.indicador_receita_pj;
        return includeTipoRelacionamento;
    }

    /**
     * Atualiza o tipo relacionamento; realizando uma requisição PATCH
     * @param tipoRelacionamentoComponent 
     */
    private updateTipoRelacionamento(tipoRelacionamentoComponent: TipoRelacionamentoComponent, id: number) {
        this.loadService.show();
        this.tipoRelacionamentoService.update(id, this.inicializarUpdateTipoRelacionamento()).subscribe(() => {
            this.loadService.hide();
            this.tipoRelacionamentoService.setEdicaoTipoRelacionamento(true);
            this.tipoRelacionamentoService.setId(id);
            this.navigateUrl([TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO]);
        }, error => {
            this.onErrorTipoRelacionamento(id, error, tipoRelacionamentoComponent);
        });
    }

    /**
     * Inicializa o objeto de atulização de tipo relacionamento
     */
    private inicializarUpdateTipoRelacionamento(): UpdateTipoRelacionamento {
        const updateTipoRelacionamento: UpdateTipoRelacionamento = new UpdateTipoRelacionamento();
        updateTipoRelacionamento.nome = this.tipoRelacionamento.nome;
        updateTipoRelacionamento.tipo_pessoa = this.tipoRelacionamentoModel.selectedTipoPessoa.value;
        updateTipoRelacionamento.indicador_principal = this.tipoRelacionamento.indicador_principal;
        updateTipoRelacionamento.indicador_relacionado = this.tipoRelacionamento.indicador_relacionado;
        updateTipoRelacionamento.indicador_sequencia = this.tipoRelacionamento.indicador_sequencia;
        updateTipoRelacionamento.indicador_receita_pf = this.tipoRelacionamento.indicador_receita_pf;
        updateTipoRelacionamento.indicador_receita_pj = this.tipoRelacionamento.indicador_receita_pj;
        return updateTipoRelacionamento;
    }

    /**
     * Verifica se é uma edição de tipo relacionamento
     * @param params 
     * @param tipoRelacionamentoComponent 
     */
    private verificarEdicaoTipoRelacionamento(params: any, tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        let paramIntUrl: boolean = this.checkParamIntUrl(params);
        if (paramIntUrl) {
            this.validarLabelEdicao(paramIntUrl);
            this.tipoRelacionamentoModel.showHeader = true;
            const id: number = Number(params.id);
            this.carregarTipoRelacionamento(id, tipoRelacionamentoComponent);
        } else {
            this.tipoRelacionamentoModel.showHeader = false;
        }
    }

    /**
     * Lança o erro para o usuário
     * @param id 
     * @param error 
     * @param tipoRelacionamentoComponent 
     */
    private onErrorTipoRelacionamento(id: number, error: any, tipoRelacionamentoComponent: TipoRelacionamentoComponent) {
        this.loadService.hide();
        if (error.status == 403) {
            tipoRelacionamentoComponent.addMessageError(error.error.mensagem);
        } else if (error.status == 404) {
            this.navigateUrl([TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO, id.toString(), TIPO_RELACIONAMENTO.TIPO_RELACIONAMENTO_NOT_FOUND]);
        } else {
            tipoRelacionamentoComponent.addMessageError(JSON.stringify(error));
        }
        throw error;
    }

    /**
     * Adiciona os valores: ID e data ultima alteracao no header da grid tipo relacionamento
     * @param tipoRelacionamento 
     */
    private custumizeHeaderGrid(tipoRelacionamento: TipoRelacionamento) {
        Utils.custumizeHeaderGrid(this.tipoRelacionamentoModel.configGridHeader, tipoRelacionamento.id, tipoRelacionamento.ultima_alteracao);
    }

    /**
     * Altera a label do cadastro tipo relacionamento: Edição de tipo relacionamento e Novo Tipo Relacionamento
     * @param paramIntUrl 
     */
    private validarLabelEdicao(paramIntUrl: boolean) {
        this.tipoRelacionamentoModel.editar = paramIntUrl;
    }

    /**
     * Verifica existencia parametro inteiro na URL
     * @param params 
     */
    private checkParamIntUrl(params: any): boolean {
        return Utils.checkParamIntUrl(params.id);
    }

}