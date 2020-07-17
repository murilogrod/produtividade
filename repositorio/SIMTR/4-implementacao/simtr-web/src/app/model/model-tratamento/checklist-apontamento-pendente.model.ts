import { Apontamento } from "./apontamento.model";

export class CheckListApontamentoPendente{
    identificador_checklist: number;
    nome_checklist: string;
    identificador_instancia: number;
    apontamentos_pendentes: Apontamento[];
}