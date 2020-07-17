import { ProdutoContratado } from "./produtoContratado";
import { VinculosPessoas } from "./vinculos-pessoas";
import { GarantiasInformadas } from "./garantiasInformadas";
import { RespostaFormulario } from "./respostaFormulario";
import { ElementosConteudo } from "./elementos-conteudo";

export class DossieProdutoModel { 
    rascunho?: boolean;
    processo_origem?: number;
    produtos_contratados?: ProdutoContratado[];
    vinculos_pessoas?: VinculosPessoas[];
    garantias_informadas?: GarantiasInformadas[];
    respostas_formulario?: RespostaFormulario[];
    elementos_conteudo?: ElementosConteudo[];
    
}
