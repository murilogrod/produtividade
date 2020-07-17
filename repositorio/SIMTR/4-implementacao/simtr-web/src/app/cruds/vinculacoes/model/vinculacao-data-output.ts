import { Vinculacao } from "../../model/vinculacao.model";

export class VinculacaoDataOutput {
    vinculacao: Vinculacao;
    vinculacaoConflitante: Vinculacao;
    conflitoVinculacao: boolean = false;
    edicaoConflitoVinculacao: boolean;
    indexVinculacaoConflitante: number;
    idChecklistAssociadoVinculacao: number;
}