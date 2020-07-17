import { CheckListPendente } from "./checklist-pendente.model";
import { CheckListInesperado } from "./checklist-inesperado.model";
import { CheckListFaseReplicado } from "./checklist-fase-replicado.model";
import { InstanciaDocumentoReplicada } from "./instancia-documento-replicada.model";
import { InstanciaDocumentoPendente } from "./instancia-documento-pendente.model";
import { CheckListApontamentoPendente } from "./checklist-apontamento-pendente.model";

export class VerificacaoInvalida{
    identificadores_instancia_invalida: number[];
    checklists_nao_documentais_ausentes:CheckListPendente[];
    checklists_inesperados: CheckListInesperado[];
    checklists_nao_documentais_replicados: CheckListFaseReplicado[];
    instancias_verificacao_replicada: InstanciaDocumentoReplicada[];
    instancias_pendentes: InstanciaDocumentoPendente[];
    checklists_incompletos: CheckListApontamentoPendente[];
    mensagem: string;
    detalhe: string;
}