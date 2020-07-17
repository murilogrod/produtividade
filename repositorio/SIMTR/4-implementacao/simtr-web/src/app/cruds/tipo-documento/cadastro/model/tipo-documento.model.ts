import { GridTipoDocumento } from "../../model/grid-tipo-documento.model";
import { SelectItem } from "primeng/primeng";
import { TipoDocumento } from "./tipo-documento";
import { Observable } from "rxjs";
import { IncludeAtributoExtracao } from "./include-atributo-extracao";
import { UpdateAtributoExtracao } from "./update-atributo-extracao";
import { ModalOpcaoAtributoModel } from "src/app/cruds/atributo/modal-atributo/opcao-grid/model/modal-opcao-atributo-model";
import { SelectTipoPessoa } from "src/app/cruds/model/select-tipo-pessoa.interface";
import { InterfaceFuncaoDocumental } from "src/app/cruds/model/funcao-documental.interface";
import { GridFuncaoDocumental } from "./grid-funcao-documental";

export class TipoDocumentoModel {
    showHeader: boolean;
    icones: SelectItem[];
    iconesFa: Array<SelectItem> = [
        { label: 'fa-clock-o', value: 'fa fa-clock-o' },
        { label: 'fa-thumbs-o-down', value: 'fa fa-thumbs-o-down' },
        { label: 'fa-reply', value: 'fa fa-reply' },
        { label: 'fa-thumbs-o-up', value: 'fa fa-thumbs-o-up' },
        { label: 'fa-unlock-alt', value: 'fa fa-unlock-alt' },
        { label: 'fa-file-excel-o', value: 'fa fa-file-excel-o' },
        { label: 'fa-clone', value: 'fa fa-clone' },
        { label: 'fa-wrench', value: 'fa fa-wrench' },
        { label: 'fa-lock', value: 'fa fa-lock' },
        { label: 'fa-calendar-times-o', value: 'fa fa-calendar-times-o' },
        { label: 'fa-check-square-o', value: 'fa fa-check-square-o' },
        { label: 'fa-shield', value: 'fa fa-shield' },
        { label: 'fa-database', value: 'fa fa-database' },
        { label: 'fa-pencil', value: 'fa fa-pencil' },
        { label: 'fa-star-half-empty', value: 'fa fa-star-half-empty' },
        { label: 'fa-calendar-check-o', value: 'fa fa-calendar-check-o' },
        { label: 'fa-500px', value: 'fa fa-500px' },
        { label: 'fa-adjust', value: 'fa fa-adjust' },
        { label: 'fa-adn', value: 'fa fa-adn' },
        { label: 'fa-coffee', value: 'fa fa-coffee' },
        { label: 'fa-align-justify', value: 'fa fa-align-justify' },
        { label: 'fa-bell', value: 'fa fa-bell' },

        { label: 'fa-building', value: 'fa fa-building' },
        { label: 'fa-amazon', value: 'fa fa-amazon' },
        { label: 'fa-ambulance', value: 'fa fa-ambulance' },
        { label: 'fa-anchor', value: 'fa fa-anchor' },
        { label: 'fa-android', value: 'fa fa-android' },
        { label: 'fa-angellist', value: 'fa fa-angellist' },
        { label: 'fa-cubes', value: 'fa fa-cubes' },
        { label: 'fa-envelope-o', value: 'fa fa-envelope-o' },
        { label: 'fa-exchange', value: 'fa fa-exchange' },
        { label: 'fa-flag', value: 'fa fa-flag' },
        { label: 'fa-diamond', value: 'fa fa-diamond' },
        { label: 'fa-folder-open-o', value: 'fa fa-folder-open-o' }
    ];
    configGridHeader: any[] = [
        { width: '30%', classIcon: 'fa fa-desktop fa-4x', labelTitle: 'ID', index: 0 },
        { width: '100%', classIcon: 'fa fa-clock-o fa-4x', labelTitle: 'DATA DA ÚLTIMA ATUALIZAÇÃO', index: 1 }
    ];
    controlError: boolean;
    editar: boolean = false;
    /* TOOLTIPS */
    no_tipo_documento: string = 'Nome do Tipo de Documento. Exemplos: RG, CNH, Certidão Negativa de Débito, Passaporte.';
    //ic_ativo: string = 'Indica se o Tipo de Documento está ativo, ou seja, se poderá ser utilizado em vinculações a checklists, processos, etc.';
    ic_tipo_pessoa: string = 'Tipo de pessoa que pode ter este Tipo de Documento atribuído: Física, Jurídica ou Ambos (Física e Jurídica). Quando o combo não for selecionado indicará ao sistema que se trata de um documento de produto/serviço, não relacionado a um tipo de pessoa.';
    ic_validade_auto_contida: string = 'Determina se a validade do documento está definida no próprio documento, como no caso de certidões que possuem a validade expressa em seu corpo. Caso este item esteja desmarcado, o prazo de validade deve ser calculado conforme definido no campo Prazo de Validade.';
    pz_validade_dias: string = 'Quantidade de dias de validade do documento a partir da sua emissão. Caso o valor deste campo não seja definido indicará que o documento possui um prazo de validade indeterminado.';
    no_classe_siecm: string = 'Nome da classe no GED que será utilizada para armazenar o documento junto ao SIECM.';
    co_tipologia: string = 'Código da tipologia documental corporativa, conforme definição da área de Gestão Arquivística da Caixa.';
    no_arquivo_minuta: string = 'Nome do arquivo utilizado pelo gerador de relatório do SIMTR para emissão da minuta do documento.';
    de_tags: string = 'TAGS que serão utilizadas para pesquisa de documentos associados ao Tipo de Documento. Para adicionar uma nova TAG, basta preencher neste campo e clicar na seta ao lado. Para excluir uma TAG, utilizar o "X" no canto inferior de cada TAG.';
    no_avatar: string = 'Nome do ícone que será utilizado na interface gráfica para exibição e organização das filas de extração de dados dos documentos associados a esta tipologia.';
    co_rgb_box: string = 'Cor do ícone.';
    ic_validacao_cadastral: string = 'Indica se o documento pode ser enviado para avaliação de validade cadastral, realizada atualmente pelo SIFRC.';
    ic_validacao_documental: string = 'Indica se o documento pode ser enviado para avaliação de validade documental externa.';
    ic_extracao_externa: string = 'Indica se o documento deve ser encaminhado para extração de dados junto a empresa de prestação de serviço externa.';
    ic_processo_administrativo: string = 'Indica se o Tipo de Documento é utilizado no Processo Administrativo Eletronico (PAE).';
    ic_dossie_digital: string = 'Indica se o Tipo de Documento é utilizado no Dossiê Digital.';
    ic_apoio_negocio: string = 'Indica se o Tipo de Documento é utilizado nos processos de Sustentação Digital ao Negocio.';
    ic_reuso: string = 'Indica se deve ser aplicado reuso ou não na carga do documento.';
    ic_extracao_m0: string = 'Indica se o documento pode ser encaminhado para extração em janela on-line (M0).';
    ic_validacao_sicod: string = 'Indica se o documento pode ser enviado para avaliação de validade documental com o SICOD.';
    ic_multiplos: string = 'Indica se pode haver mais de um documento deste Tipo em um mesmo dossiê de cliente. Caso este item esteja desmarcado, ao receber novo upload de um documento o sistema invalidará para novos negócios o documento capturado anteriormente.';
    ic_ativo: string = 'Indica se o tipo documento esta ativo ou não para utilização pelo sistema.';
    ic_binario_outsourcing: string = 'Indica se o binário do documento será armazenado no SIECM antes do encaminhamento para o serviço de Outsourcing.';
    gridTipoDocumentos: Array<GridTipoDocumento> = new Array<GridTipoDocumento>();
    tipoLogiaCadastrada: boolean = false;
    nomeTipoDocumentoJaCadastrado: string;
    tipoLogiaInconssistente: boolean = false;
    nomeApoioNegocioCadastrado: boolean = false;
    nomeDossieDigitalCadastrado: boolean = false;
    nomeProcessoAdministrativoCadastrado: boolean = false;
    classeSiecmDossieDigital: boolean = false;
    classeSiecmProcessoAdministrativo: boolean = false;
    ultimoIdTipoDocumento: number;
    tipoDocumento: TipoDocumento;
    formularioTipoDocumentoAlterado: boolean = false;
    requesicoesIncludeAtributos: Array<Observable<IncludeAtributoExtracao>> = new Array<Observable<IncludeAtributoExtracao>>(0);
    requesicoesUpdateAtributos: Array<Observable<UpdateAtributoExtracao>> = new Array<Observable<UpdateAtributoExtracao>>(0);
    requesicoesIncludeOpcoesAtributos: Array<Observable<ModalOpcaoAtributoModel>> = new Array<Observable<ModalOpcaoAtributoModel>>(0);
    requesicoesAtributosRemovidos: Array<Observable<any>> = new Array<Observable<any>>(0);
    requesicoesOpcoesAtributosRemovidos: Array<Observable<any>> = new Array<Observable<any>>(0);
    tipoPessoas: Array<SelectTipoPessoa>;
    selectedTipoPessoa: SelectTipoPessoa;
    funcoesDocumentaisInterface: Array<InterfaceFuncaoDocumental>;
    selectedFuncaoDocumental: InterfaceFuncaoDocumental;
    funcoesDocumentais: Array<GridFuncaoDocumental> = new Array<GridFuncaoDocumental>(0);
    rowsPerPageOptions: number[] = [];
    configTableColsFuncoesDocumentais: any[] = [
        { field: 'identificador_funcao_documental', header: 'ID', class: 'W-5' },
        { field: 'nome_funcao_documental', header: 'FUNÇÃO DOCUMENTAL' },
        { field: 'label_indicador_processo_administrativo', header: 'PAE', tooltip: true, msg: 'USO PROCESSO ADMINISTRATIVO' },
        { field: 'label_indicador_dossie_digital', header: 'DOSSIÊ DIGITAL' },
        { field: 'label_indicador_apoio_negocio', header: 'NEGÓCIO' },
        { field: 'acoes', header: 'AÇÔES' },
    ];
    countFuncaoDocumental: number = 0;

    emptyFuncoesdocumentaisInterface(): boolean {
        return this.selectedFuncaoDocumental == undefined || (this.funcoesDocumentaisInterface && this.funcoesDocumentaisInterface.length == 0);
    }

    hasIndicador(): boolean {
        return this.nomeApoioNegocioCadastrado || this.nomeDossieDigitalCadastrado || this.nomeProcessoAdministrativoCadastrado;
    }

    hasClasseSiecm(): boolean {
        return this.classeSiecmDossieDigital || this.classeSiecmProcessoAdministrativo;
    }
}