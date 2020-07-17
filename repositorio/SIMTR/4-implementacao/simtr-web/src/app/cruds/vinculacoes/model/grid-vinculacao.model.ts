import { Vinculacao } from "../../model/vinculacao.model";

export class GridVinculacao {
    id: number;
    processo: string;
    fase: string;
    tipoDocumento: string;
    funcaoDocumental: string;
    dataRevogacao: string;
    vinculacao: Vinculacao;
    conflitoVinculacao: boolean = false;
    vinculacaoConflitante: Vinculacao;
    msgVinculacaoConflitante: string;
}