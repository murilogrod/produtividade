import { CampoEntrada } from "./campo-entrada";
import { Formulario } from "./formulario";
import { OpcoesDisponiveis } from "./opcoes-disponiveis";
import { ListaApresentacoes } from "./lista-apresentacoes";

export class CampoFormulario {
    id : number;
    formulario : Formulario;
    campo_entrada : CampoEntrada; 
    ordem_apresentacao : number;
    campo_apresentacao_vinculados : any;
    obrigatorio?: boolean;
    resposta: any;
    nome: string;
    codigo_campo: number;
    nome_campo: string;
    label_campo: string;
    label: string;
    expressao_interface?: string;
    tipo_campo: string;
    mascara_campo?: string;
    placeholder_campo?: string;
    tamanho_minimo?: number;
    tamanho_maximo?: number;
    opcoes_disponiveis?: OpcoesDisponiveis[];
    lista_apresentacoes?: ListaApresentacoes[];
    respostas_formulario?: any;
    resposta_aberta?: any;
    opcoes_selecionadas?: any[];
    string_selecionadas?: string[];
    showCampo?: boolean;
}