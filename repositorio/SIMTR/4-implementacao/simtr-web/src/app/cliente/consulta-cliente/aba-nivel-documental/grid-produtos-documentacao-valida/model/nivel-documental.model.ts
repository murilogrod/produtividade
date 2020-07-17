import { ProdutoHabilitado } from "./produto-habilitado.model";

export class NivelDocumental{
    dataHoraApuracaoNivel: string;
    produtosHabilitados: ProdutoHabilitado[];
    contadorDocumentacaoValida: number = 0 ;

    constructor(){
        this.dataHoraApuracaoNivel = undefined;
        this.produtosHabilitados = new Array<ProdutoHabilitado>();
    }
} 