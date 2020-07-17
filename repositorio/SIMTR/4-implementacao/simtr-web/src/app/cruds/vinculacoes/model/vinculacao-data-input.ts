import { GridVinculacao } from "./grid-vinculacao.model";
import { Vinculacao } from "../../model/vinculacao.model";

export class VinculacaoDataInput {
    processo: string;
    fase: string;
    tipoDocumento: string;
    funcaoDocumental: string;
    dataRevogacao: string;
    vinculacao: Vinculacao;
    vinculacoes: Array<GridVinculacao>;
    conflitoVinculacao: boolean = false;
    gridVinculacaoConflitante: GridVinculacao;
    indexVinculacaoConflitante: number;
}