import { ListaApresentacoes } from './lista-apresentacoes';
import { OpcoesDisponiveis } from './opcoes-disponiveis';

export class CamposFormulario{

    codigo_campo: number;
    nome_campo: string;
    label_campo: string;
    obrigatorio: boolean;
    expressao_interface?: string;
    ordem_apresentacao: number;
    tipo_campo: string;
    mascara_campo?: string;
    placeholder_campo?: string;
    tamanho_minimo?: number;
    tamanho_maximo?: number;
    opcoes_disponiveis?: OpcoesDisponiveis[];
    lista_apresentacoes?: ListaApresentacoes[];
    respostas_formulario?: any;
    resposta_aberta?: any;

}