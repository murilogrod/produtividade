import { Apontamento } from "src/app/cruds/model/apontamento.model";

export class ApontamentoDataOutput {
    index: number;
    ultimaOrdem: number;
    ultimoNome: string;
    apontamento: Apontamento;
}