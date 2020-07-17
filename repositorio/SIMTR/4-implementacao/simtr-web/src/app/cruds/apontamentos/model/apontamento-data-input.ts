import { Apontamento } from "src/app/cruds/model/apontamento.model";

export class ApontamentoDataInput {
    index: number;
    apontamento: Apontamento;
    apontamentos: Array<Apontamento>;
}