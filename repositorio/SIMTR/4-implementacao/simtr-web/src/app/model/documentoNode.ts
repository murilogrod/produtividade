import { TreeNode } from 'primeng/primeng';
import {ConteudoDocumento} from './conteudo-documento';
import { NodeAbstract } from './model-arvore-generica-dossie-produto/model-front-end-no-arvore/node-abstract.model';

export class DocumentoNode extends NodeAbstract implements TreeNode {
    label?: string;
    data?: any;
    icon?: any;
    expandedIcon?: any;
    collapsedIcon?: any;
    children?: DocumentoNode[];
    leaf?: boolean;
    expanded?: boolean;
    type?: string;
    parent?: DocumentoNode;
    partialSelected?: boolean;
    styleClass?: string;
    draggable?: boolean;
    droppable?: boolean;
    selectable?: boolean;
    uri: any[];
    pages : DocumentoNode[];
    id?: number;
    matchFilter : boolean; //Será usado para filtrar os nodes
    acao_documento?: string;
    possui_instancia?: boolean;
    vinculo_anterior?: number;
    obrigatorio?: boolean;
    situacao?: string;
    motivo?: string;
    numero_processo?: number;
    dossie_produto?:number;
    data_validade?: string;
    atributos: string[];
    origem_documento?: string;
    removido_arvore?: any;


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

    setImageIcon (status) {
        if (status === "A") {
            this.icon = "fa-check-circle-gray";
        } else if (status === "I") {
            this.icon = "fa-check-circle-red";
        } else if (status === "N") {
            this.icon = "fa-check-circle-gray";
        } else if (status === "R") {
            this.icon = "fa-check-circle-green";
        } else if (status === "criado") {
            this.icon = "fa-circle-gray";
        } else if (status === "rejeitado") {
            this.icon = "fa-times-circle-red";
        } else if (status === "suspeita-fraude") {
            this.icon = "fa-exclamation-triangle-yellow";
        } else if (status === "rejeitado-fraude") {
            this.icon = "fa-times-circle-red";
        } else if (status === "conforme") {
            this.icon = "fa-check-circle-green";
        }
        this.leaf = true;
    }

    setUri(uri: string[]) {
        this.uri = uri;
    }
}