
export const FIELDS_NAME = {
    nome: 'nome',
    nome_mae: 'nome_mae',
    nome_pai: 'nome_pai',
    telefone: 'telefone',
    data_nascimento: 'data_nascimento',
    identidade: 'identidade',
    orgao_emissor: 'orgao_emissor',
    nis: 'nis',
    estado_civil: 'estado_civil',
    razao_social: 'razao_social',
    data_fundacao: 'data_fundacao',
    segmento: 'segmento'
}

export class UtilsCliente {

    // Valores validos para o Parametro( "S" = Data completa com hora, 
    // "N" = apenas data, "H" = apenas hora, 
    // "T" = hora formatada para tela, 
    // "ST" = data completa, sem o traço,
    // "I" = data com padrão yyyy/MM/dd hh:mm:ss
    static getDataCompleta(dataComHora: string) {
        let dataCompleta = "";
        var date = new Date();
        let mesN = date.getMonth() + 1;
        let dia = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        let mes = mesN < 10 ? "0" + mesN : mesN;

        let h = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        let m = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        let s = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        let hora = h + ":" + m + ":" + s;

        if (dataComHora === 'ST') {
            return dia + "/" + mes + "/" + date.getFullYear() + " " + hora;
        } else if (dataComHora === 'S') {
            return dia + "/" + mes + "/" + date.getFullYear() + " - " + hora;
        } else if (dataComHora === 'H') {
            return hora;
        } else if (dataComHora === 'T') {
            return dia + "/" + mes + "/" + date.getFullYear();
        } else if ("I") {
            return date.getFullYear() + "-" + mes + "-" + dia + " " + hora;
        } else {
            return date.getFullYear() + "-" + mes + "-" + dia;
        }
    }

    static converteDataHora(data) {
        const dataAux = data.substring(0, 10).split("/");
        return dataAux[2] + "-" + dataAux[1] + "-" + dataAux[0] + data.substring(10, data.length);
    }

    static converteData(data) {
        const dataAux = data.toString().split("/");
        return dataAux[2] + "-" + dataAux[1] + "-" + dataAux[0];
    }

    static validarDataMaior(dataInicio, dataFim) {
        const dataBanco = this.converteData(dataInicio);
        if (Date.parse(dataBanco) <= Date.parse(dataFim)) {
            return true;
        } else {
            return false;
        }
    }

    static validarDataMenor(dataInicio, dataFim) {
        const dataBanco = this.converteDataHora(dataInicio);
        if (Date.parse(dataBanco) > Date.parse(dataFim)) {
            return true;
        } else {
            return false;
        }
    }

    static isSicliOk(sicliInformation) {
        return sicliInformation != null && sicliInformation.dados.controleNegocial.codRetorno !== '03';
    }

    static retornaTipoPessoa(tipo) {
        if (tipo === 'CONJUGE') {
            return 'CÔNJUGE';
        } else if (tipo === 'CONJUGE_SOCIO') {
            return 'CÔNJUGE SÓCIO';
        } else if (tipo === 'EMPRESA_CONGLOMERADO') {
            return 'EMPRESA CONGLOMERADO';
        } else if (tipo === 'SOCIO') {
            return 'SÓCIO';
        } else if (tipo === 'SEGUNDO_TITULAR') {
            return 'SEGUNDO TITULAR';
        } else if (tipo === 'TOMADOR_PRIMEIRO_TITULAR') {
            return 'TOMADOR/PRIMEIRO TITULAR';
        } else {
            return tipo;
        }
    }

    static estadoCivil() {
        return [
            { id: 0, descricao: "Não Informado", valor: "NAO_INFORMADO" },
            { id: 1, descricao: "Solteiro (a)", valor: "SOLTEIRO" },
            { id: 2, descricao: "Casado (a)", valor: "CASADO" },
            { id: 3, descricao: "Divorciado (a)", valor: "DIVORCIADO" },
            { id: 4, descricao: "Separado (a) Judicialmente", valor: "SEPARADO_JUDICIALMENTE" },
            { id: 5, descricao: "Viúvo (a)", valor: "VIUVO" },
            { id: 6, descricao: "Com União Estável", valor: "UNIAO_ESTAVEL" },
            { id: 7, descricao: "Casado (a) com comunhão total de bens", valor: "CASADO_COMUNHAO_TOTAL_BENS" },
            { id: 8, descricao: "Casado (a) sem comunhão de bens", valor: "CASADO_SEM_COMUNHAO_TOTAL_BENS" },
            { id: 9, descricao: "Casado (a) com comunhão parcial de bens", valor: "CASADO_COMUNHAO_PARCIAL_BENS" }
        ]
    }

    static segmentoPJ() {
        return [
            { id: 1, rotulo: "MEI", descricao: "Micro Empreendedor Individual" },
            { id: 2, rotulo: "MPE", descricao: "Micro e Pequena Empresa" },
            { id: 3, rotulo: "MGE", descricao: "Média e Grande Empresa" },
            { id: 4, rotulo: "CORP", descricao: "Corporate" }
        ]
    }

    static tipoPortes() {
        return [
            { sigla: 'ME', descricao: 'MICROEMPRESA' },
            { sigla: 'MEI', descricao: 'MICRO EMPREENDEDOR INDIVIDUAL' },
            { sigla: 'EPP', descricao: 'EMPRESA DE PEQUENO PORTE' },
            { sigla: 'DEMAIS', descricao: 'DEMAIS' }
        ]
    }

    /**
     * Verifica se parâmetro é uma pessoa física
     * @param cpfCnpj 
     */
    static isCPF(cpfCnpj: string): boolean {
        return cpfCnpj.length === 14;
    }

    static RASCUNHO = "RASCUNHO";
    static AGUARDANDO_TRATAMENTO = "AGUARDANDO TRATAMENTO";

}