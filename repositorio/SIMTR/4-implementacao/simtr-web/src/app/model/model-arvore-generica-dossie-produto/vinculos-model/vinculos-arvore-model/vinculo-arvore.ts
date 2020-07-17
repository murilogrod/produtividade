import { NodeApresentacao } from "../../model-front-end-no-arvore/node-apresentacao.model";
import { VinculoArvoreCliente } from "./vinculo-arvore-cliente";


export class  VinculoArvore{
    id: number;
    idProcessoFase: number;
    nome: string;
    noApresentacao: NodeApresentacao[];
    classeValidacao: string;
    ocultarVinculoArvore: boolean;
    arcoordion:boolean;
    alterandoVinculo?:boolean;
    emptyTreeProcesso?:boolean;
    emptyTreeCliente?:boolean;
    emptyTreeProduto?:boolean;
    emptyTreeGarantia?:boolean;
}