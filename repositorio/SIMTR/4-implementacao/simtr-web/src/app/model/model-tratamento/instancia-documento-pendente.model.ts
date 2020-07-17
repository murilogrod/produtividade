import { CheckListPendente } from "./checklist-pendente.model";

export class InstanciaDocumentoPendente{
    identificador_instancia: number;
    tipo_documento: string;
    checklist_esperado: CheckListPendente;
}