import { ParecerApontamento } from "./parecer-apontamento.model";

export class Verificacao{
    identificador_instancia_documento: number;
    identificador_checklist: number;
    analise_realizada: boolean;
    parecer_apontamentos: ParecerApontamento[];
}