import { ApontamentoChecklist } from "./apontamento-cheklist";

export class Checklist {
    idVinculo?: string;
    idInstancia?: string;
    nomeVinculo?: any;
    idDocumento?: string;
    idTipoDocumento?: string;
    descricaoTipoDocumento?: string;
    idcheck?: number;
    checklistPrevio: boolean;
    existeIncomformidade?: boolean;
    obrigatorio?: boolean;
    orientacao?: string;
    tipo?:string;
    habilitaVerificacao?: boolean;
    listaResposta?: ApontamentoChecklist[];
    isfuncaoDocumental?: boolean;
    idElementoConteudo?: string;
    idGarantiaInformada?: string;
    idFuncaoDocumental?: any;
    data_revogacao?: Date;
    semApontamentos?: boolean;
    documentoConforme?: boolean;
    situacaoConforme?: boolean;
}