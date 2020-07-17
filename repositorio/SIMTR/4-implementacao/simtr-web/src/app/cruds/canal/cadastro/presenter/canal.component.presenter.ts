import { Injectable } from "@angular/core";
import { Canal } from "../model/canal.model";
import { LoaderService } from "src/app/services";
import { CanalService } from "../../service/canal.service";
import { IncludeCanal } from "../model/include-canal.model";
import { Router } from "@angular/router";
import { CANAL } from "../../constant.canal";
import { CanalComponent } from "../view/canal.component";
import { UpdateCanal } from "../model/update-canal.model";
import { Utils } from "src/app/cruds/util/utils";

@Injectable()
export class CanalPresenter {

    canal: Canal;
    includeCanal: IncludeCanal;

    constructor(private canalService: CanalService,
        private router: Router,
        private loadService: LoaderService) { }

    /**
     * Navega para a url passada como parâmetro
     * @param url 
     */
    navigateUrl(parameters: string[]) {
        this.router.navigate(parameters);
    }


    /**
     * Atualiza o canal depedendo do estado do mesmo: novo canal ou edição de canal existente
     * @param canalComponent 
     * @param params 
     */
    checkUpdateCanal(canalComponent: CanalComponent, params: any) {
        if (this.checkParamIntUrl(params)) {
            this.updateCanal(canalComponent, Number(params.id));
        } else {
            this.saveCanal(canalComponent);
        }
    }

    /**
     * Inicializa a configuração inicial do canal: verifica edicao
     * @param params 
     * @param canalComponent 
     */
    initConfigCanal(params: any, canalComponent: CanalComponent) {
        this.verificarEdicaoCanal(params, canalComponent);
    }

    /**
     * Recupera o canal passando id
     * @param id 
     * @param canalComponent 
     */
    carregarCanal(id: number, canalComponent: CanalComponent) {
        this.loadService.show();
        this.canalService.getById(id).subscribe(canal => {
            this.includeCanal = canal
            this.custumizeHeaderGrid();
            this.loadService.hide();
        }, error => {
            this.onErrorCanal(id, error, canalComponent);
        });
    }

    /**
     * Adiciona os valores: ID e data ultima alteracao no header da grid canal
     */
    private custumizeHeaderGrid() {
        Utils.custumizeHeaderGrid(this.canal.configGridHeader, this.includeCanal.identificador_canal, this.includeCanal.data_hora_ultima_alteracao);
    }

    /**
     * Altera a label do cadastro canal: Edição de Canal e Novo Canal
     * @param paramIntUrl 
     */
    private validarLabelEdicao(paramIntUrl: boolean) {
        this.canal.editar = paramIntUrl;
    }

    /**
     * Verifica se é uma edição de canal
     * @param params 
     * @param canalComponent 
     */
    private verificarEdicaoCanal(params: any, canalComponent: CanalComponent) {
        let paramIntUrl: boolean = this.checkParamIntUrl(params);
        if (paramIntUrl) {
            this.validarLabelEdicao(paramIntUrl);
            this.canal.showHeader = true;
            const id: number = Number(params.id);
            this.carregarCanal(id, canalComponent);
        } else {
            this.canal.showHeader = false;
        }
    }

    /**
     * Atualiza o canal; realizando uma requisição PATCH
     * @param canalComponent 
     */
    private updateCanal(canalComponent: CanalComponent, id: number) {
        this.loadService.show();
        this.canalService.update(id, this.inicializarUpdateCanal()).subscribe(() => {
            this.loadService.hide();
            this.canalService.setEdicaoCanal(true);
            this.canalService.setId(id);
            this.navigateUrl([CANAL.CANAL]);
        }, error => {
            this.onErrorCanal(id, error, canalComponent);
        });
    }

    /**
     * Inicializa o objeto de atulização de canal
     */
    private inicializarUpdateCanal(): UpdateCanal {
        const updateCanal: UpdateCanal = new UpdateCanal();
        updateCanal.indicador_extracao_m0 = this.includeCanal.indicador_extracao_m0;
        updateCanal.indicador_extracao_m30 = this.includeCanal.indicador_extracao_m30;
        updateCanal.indicador_extracao_m60 = this.includeCanal.indicador_extracao_m60;
        updateCanal.indicador_avaliacao_autenticidade = this.includeCanal.indicador_avaliacao_autenticidade;
        updateCanal.indicador_atualizacao_documental = this.includeCanal.indicador_atualizacao_documental;
        updateCanal.indicador_outorga_cadastro_receita = this.includeCanal.indicador_outorga_cadastro_receita;
        updateCanal.indicador_outorga_cadastro_caixa = this.includeCanal.indicador_outorga_cadastro_caixa;
        updateCanal.indicador_outorga_apimanager = this.includeCanal.indicador_outorga_apimanager;
        updateCanal.indicador_outorga_siric = this.includeCanal.indicador_outorga_siric;
        updateCanal.canal_caixa = this.includeCanal.canal_caixa;
        updateCanal.descricao_canal = this.includeCanal.descricao_canal;
        updateCanal.nome_cliente_sso = this.includeCanal.nome_cliente_sso;
        updateCanal.sigla_canal = this.includeCanal.sigla_canal;
        updateCanal.url_callback_documento = this.includeCanal.url_callback_documento;
        updateCanal.url_callback_dossie = this.includeCanal.url_callback_dossie;
        updateCanal.unidade_callback_dossie = this.includeCanal.unidade_callback_dossie;
        return updateCanal;
    }

    /**
     * Lança o erro para o usuário
     * @param id 
     * @param error 
     * @param canalComponent 
     */
    private onErrorCanal(id: number, error: any, canalComponent: CanalComponent) {
        this.loadService.hide();
        if (error.status == 403) {
            canalComponent.addMessageError(error.error.mensagem);
        } else if (error.status == 404) {
            this.navigateUrl([CANAL.CANAL, id.toString(), CANAL.CANAL_NOT_FOUND]);
        } else {
            canalComponent.addMessageError(JSON.stringify(error));
        }
        throw error;
    }

    /**
     * Salva um novo canal; relizando uma requisição post e redireciona para 
     * a sua requisição get passando ID
     * @param canalComponent 
     */
    private saveCanal(canalComponent: CanalComponent) {
        this.loadService.show();
        this.canalService.save(this.includeCanal).subscribe(response => {
            this.canalService.setNovoCanal(true);
            this.canalService.setId(Number(response.body.id));
            this.navigateUrl([CANAL.CANAL]);
        }, error => {
            this.onErrorCanal(null, error, canalComponent);
        });
    }

    /**
     * Verifica existencia parametro inteiro na URL
     * @param params 
     */
    private checkParamIntUrl(params: any): boolean {
        return Utils.checkParamIntUrl(params.id);
    }

}