import { Documento } from '../../documento';
import { Vinculo } from './vinculo.model';
import { RespostaFormulario } from 'src/app/dossie/manutencao-dossie/model-endPoint-dossie-produto/respostaFormulario';
import { SelectItem } from 'primeng/primeng';

export class VinculoGarantia implements Vinculo{
    id?: number;
    codigoGarantia?: number;
    nome?: string;
    nome_garantia?: string;
    produtoVinculado?: any;
    garantiaVinculada?: any;
    formaGarantia?: string;
    indicador_fidejussoria?: boolean; 
    relacionado?: string;
    clientes_avalistas?: any[];
    codigo_bacen?: any;

    dossies_cliente?: any[];
    garantia?: string;
    produto?: any;
    produto_operacao?: string;
    produto_modalidade?: string;
    produto_nome?: string;
    operacao: string;
    modalidade: string;
    valorGarantia?: string;
    percentual_garantia?: string;
    forma_garantia?: string;
    descricao?: string;
    documento?: Documento[];
    valor_garantia?: number;
    vinculoNovo?:boolean;
    exclusao?:boolean;
    instancias_documento: any;
    garantias_informadas: any[];
    respostas_formulario?: RespostaFormulario [];
}