import { Apontamento } from "src/app/cruds/model/apontamento.model";

export class ModalApontamento {
    apontamento: Apontamento;
    ultimoNome: string;
    ultimaOrdem: number;
    pendenciaInformacaoSeguranca: boolean = false;
    ocorrenciaApontamento: boolean = false;
    editar: boolean = false;
}