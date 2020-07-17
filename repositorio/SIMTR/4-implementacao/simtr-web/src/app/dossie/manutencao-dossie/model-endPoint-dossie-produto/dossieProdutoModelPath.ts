import { ElementosConteudo } from "./elementos-conteudo";
import { ProdutoContratado } from "./produtoContratado";
import { VinculosPessoas } from "./vinculos-pessoas";
import { GarantiasInformadas } from "./garantiasInformadas";
import { RespostaCampoFormulario } from "src/app/model/resposta-campo-formulario";

export class DossieProdutoModelPath {
    cancelamento?: boolean;
    finalizacao?: boolean;
    retorno?: boolean;
    justificativa?: string;
    elementos_conteudo?: ElementosConteudo[];
    produtos_contratados?: ProdutoContratado[];
    vinculos_pessoas?: VinculosPessoas[];
    garantias_informadas?: GarantiasInformadas[];
    respostas_formulario?: RespostaCampoFormulario[];
}