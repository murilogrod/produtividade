import { Formulario } from "./formulario";
import { OpcaoCampo } from "./opcao-campo";

export class CampoEntrada {
  id?: number;
  formulario?: Formulario;
  nome?: string;
  tipo?: string;
  chave?: boolean;
  label?: string;
  largura?: number;
  mascara?: string;
  placeholder?: string;
  tamanho_maximo?: number;
  tamanho_minimo?: number;
  expressao?: string;
  ativo?: boolean;
  ordemApresentacao?: number;
  ident_processo_fase?: string;
  opcoes_campos_vinculados?: OpcaoCampo[];
}
