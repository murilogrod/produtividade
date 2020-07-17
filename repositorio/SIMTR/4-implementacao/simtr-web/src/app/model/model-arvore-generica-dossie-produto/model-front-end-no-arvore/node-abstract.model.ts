import { TreeNode } from "primeng/primeng";

export abstract class NodeAbstract implements TreeNode{
    id?: number;
    label?: string;
    data?: any;
    icon?: any;
    expandedIcon?: any;
    collapsedIcon?: any;
    children?: NodeAbstract[];
    leaf?: boolean;
    expanded?: boolean;
    type?: string;
    parent?: NodeAbstract;
    partialSelected?: boolean;
    possui_instancia?: boolean;
    matchFilter : boolean;
    styleClass?: string;
    draggable?: boolean;
    droppable?: boolean;
    selectable?: boolean;
    removidoArvore: any;
    uri: any[];
    qtdImg?: number;

    abstract setRemovidoArvore();
    abstract setFolderIcons();
    abstract setImageIcon (status);
}