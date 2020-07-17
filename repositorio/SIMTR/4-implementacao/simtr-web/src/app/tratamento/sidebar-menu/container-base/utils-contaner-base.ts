export class UtilsContainerBase {

   static addMaisQuery: boolean;

    /**
     * Query para consultar vinculo de tratamento
     * @param vinculoDossieCliente 
     * @param vincularProdutosContratados 
     * @param vincularDocumentos 
     * @param vincularGarantias 
     * @param vincularForumalario 
     * @param vincularTratamento 
     */
    static parametrosQuery(vinculoDossieCliente: boolean, vincularProdutosContratados: boolean, vincularDocumentos: boolean, vincularGarantias: boolean, vincularForumalario: boolean, vincularTratamento: boolean) {
        let query = "";
        this.addMaisQuery = false;
        query = this.vinculacoesDossieCliente(vinculoDossieCliente, query);
        query = this.vinculacoesProdutosContratados(vincularProdutosContratados, query);
        query = this.vinculacoesDocumentos(vincularDocumentos, query);
        query = this.vinculacoesGarantias(vincularGarantias, query);
        query = this.vinculacoesFormulario(vincularForumalario, query);
        query = this.vinculacoesTratamento(vincularTratamento, query);
        return query;
    }

    private static addEcomercial(addMais: boolean) {
        return (addMais ? "&" : "");
    }

    static vinculacoesTratamento(vincularTratamento: boolean, query: string) {
        if (vincularTratamento) {
            query = query + UtilsContainerBase.addEcomercial(this.addMaisQuery ) + "vinculacoesTratamento=" + vincularTratamento;
            this.addMaisQuery  = true;
        }
        return query;
    }

    static vinculacoesFormulario(vincularForumalario: boolean, query: string) {
        if (vincularForumalario) {
            query = query + UtilsContainerBase.addEcomercial(this.addMaisQuery ) + "vinculacoesFormulario=" + vincularForumalario;
            this.addMaisQuery  = true;
        }
        return query;
    }

    static vinculacoesGarantias(vincularGarantias: boolean, query: string) {
        if (vincularGarantias) {
            query = query + UtilsContainerBase.addEcomercial(this.addMaisQuery ) + "vinculacoesGarantias=" + vincularGarantias;
            this.addMaisQuery  = true;
        }
        return query;
    }

    static vinculacoesDocumentos(vincularDocumentos: boolean, query: string) {
        if (vincularDocumentos) {
            query = query + UtilsContainerBase.addEcomercial(this.addMaisQuery ) + "vinculacoesDocumentos=" + vincularDocumentos;
            this.addMaisQuery  = true;
        }
        return query;
    }

    static vinculacoesProdutosContratados(vincularProdutosContratados: boolean, query: string) {
        if (vincularProdutosContratados) {
            query = query + UtilsContainerBase.addEcomercial(this.addMaisQuery ) + "vinculacoesProdutosContratados=" + vincularProdutosContratados;
            this.addMaisQuery  = true;
        }
        return query;
    }

    static vinculacoesDossieCliente(vinculoDossieCliente: boolean, query: string) {
        if (vinculoDossieCliente) {
            query = query + UtilsContainerBase.addEcomercial(this.addMaisQuery ) + "vinculacoesDossieCliente=" + vinculoDossieCliente;
            this.addMaisQuery  = true;
        }
        return query;
    }
}