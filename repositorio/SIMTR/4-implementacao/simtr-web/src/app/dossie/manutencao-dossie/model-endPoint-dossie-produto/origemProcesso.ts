import { ElementosConteudo } from "./elementos-conteudo";
import { ProdutoContratado } from "./produtoContratado";
import { GarantiasInformadas } from "./garantiasInformadas";


export class OrigemProcesso {
    processo?: number;
    elementos_conteudo?: ElementosConteudo[];
    produtos_contratados?: ProdutoContratado[];
    garantias_informadas?: GarantiasInformadas[];
}