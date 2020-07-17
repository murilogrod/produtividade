import { ElementosConteudo } from "./elementos-conteudo";
import { RespostaFormulario } from "./respostaFormulario";


export class ProdutoContratado {
    
    id?: number;
    codigo_operacao?: number;
    codigo_modalidade?: number;
    valor?: number;
    taxa_juros?: number;
    periodo_juros?: string;
    prazo?: number;
    carencia?: number;
    liquidacao?: boolean;
    numero_contrato?: string;
    elementos_conteudo?: ElementosConteudo[];
    exclusao?: boolean;
    respostas_formulario?: RespostaFormulario[];
}