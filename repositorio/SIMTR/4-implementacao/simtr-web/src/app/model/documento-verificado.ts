import { ApontamentoChecklist } from "./apontamento-cheklist";

export class DocumentoVerificado {
    id?: number;
    fase?: string;
    documento?: string;
    dataHoraVerificaco?: string;
    realizada?: boolean;
    descricaoRealizada?: string;
    aprovado ?: boolean;
    descricaoAprovado?: string;
    unidVerificacao?: string;
    mostrar?: boolean;
    listaApontamento?: ApontamentoChecklist[];
    habilitarDetalhes?: boolean;
}