import { DocumentoNovo } from "./documentoNovo";
import { RespostaFormulario } from "./respostaFormulario";

export class GarantiasInformadas {
    id?: number;
    exclusao?: boolean;
    identificador_garantia?: number;
    identificador_produto?: number;
    valor_garantia?: number;
    percentual_garantia?: number;
    forma_garantia?: string;
    clientes_avalistas?: any[];
    documentos_utilizados?: any[];
    documentos_novos?: DocumentoNovo[];
    respostas_formulario?: RespostaFormulario[];
    
}