import { NodeAbstract } from "./node-abstract.model";
import { UtilsArvore } from "src/app/documento/arvore-generica/UtilsArvore";
import { DocumentoArvore } from "../../documento-arvore";

export class NodeApresentacao extends NodeAbstract {
    id?: number;
    children?: NodeApresentacao[];
    parent?: NodeApresentacao;
    pages: NodeApresentacao[];
    matchFilter: boolean;
    acao_documento?: string;
    possui_instancia?: boolean;
    vinculo_anterior?: number;
    obrigatorio?: boolean;
    situacao?: string;
    motivo?: string;
    styleClass?: string;
    numero_processo?: number;
    identificador_elemento?: number;
    identificador_elemento_vinculador?: number;
    dossie_produto?: number;
    data_validade?: string;
    atributos?: string[]; 
    origem_documento?: string;
    removido_arvore?: any;
    quantidade_obrigatorio?: number;
    quantidade_maxima_obrigatoria: number;
    noReutilizavel?: boolean;
    existeDocumentoReuso: boolean;
    qtdImg?: number;
    labelVerdeEscuro?: boolean;
    labelLaranjaEscuro?: boolean;
    labelVermelhoEscuro?: boolean;
    labelBold?: boolean;
    dossieCliente?: boolean;
    documentoArvore: DocumentoArvore;

    constructor(label: string) {
        super();
        this.label = label;
    }

    setRemovidoArvore() {
        this.removido_arvore = "sublinhadoRemovido";
    }

    setFolderIcons() {
        this.expandedIcon = "fa fa-folder-open";
        this.collapsedIcon = "fa fa-folder";
    }

    setImageIcon(noApresentacaoConteudo: NodeApresentacao) {
        this.icon = UtilsArvore.getIconDocument(noApresentacaoConteudo);
        this.leaf = true;
    }

    setUri(uri: string[]) {
        this.uri = uri;
    }

}