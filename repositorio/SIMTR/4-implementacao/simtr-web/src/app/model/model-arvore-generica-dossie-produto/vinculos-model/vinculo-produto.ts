import { ElementoConteudo } from '../../elemento-conteudo';
import { Vinculo } from './vinculo.model';
import { CampoFormulario } from '../../campo-formulario';
import { RespostaFormulario } from 'src/app/dossie/manutencao-dossie/model-endPoint-dossie-produto/respostaFormulario';

export class VinculoProduto implements Vinculo {
    id: number;
    codigo_modalidade?: string;
    codigo_operacao?: string;
    nome?: string;
    valor_contrato?: number;
    prazo_carencia?: string;
    prazo_contrato?: string;   
    listaGarantia?: any[];

    produto?: string;
    idProduto?: number;
    valor?: number;
    taxa_juros?: number;
    periodo_juros?: string;
    prazo?: string;
    carencia?: string;
    liquidacao?: boolean;
    numero_contrato?: number;
    elementos_conteudos?: ElementoConteudo[];
    instancias_documento: any;
    vinculoNovo?:boolean;
    exclusao?:boolean;
    respostas_formulario?: RespostaFormulario[];
}