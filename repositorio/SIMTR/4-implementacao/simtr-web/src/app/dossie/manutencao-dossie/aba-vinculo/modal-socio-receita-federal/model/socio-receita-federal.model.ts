import { Socio } from "../../model/socio.model";

export class SocioReceitaFederal {
    cnpj: string;
    relacionamento: string;
    sociosReceitaFederal: Array<Socio>;
    selectedSociosReceitaFederal: Array<Socio>;
    listaTipoRelacionamento: any[] = new Array<any>();
    configTableColsSociosReceitaFederal: any[] = [
        { field: 'check', select: true },
        { field: 'nome_socio', class: 'W-15', header: 'Nome Sócio', select: false },
        { field: 'descricao_qualificacao', class: 'W-13', header: 'Descrição', tooltip: true, msg: 'Descrição Qualificação', select: false },
        { field: 'cpf_cnpj', class: 'W-10', header: 'CPF/CNPJ', select: false },
        { field: 'pc_capital_social', header: 'Capital', tooltip: true, msg: 'Capital Social', select: false },
        { field: 'data_inicio', header: 'Data', tooltip: true, msg: 'Data Início', select: false },
        { field: 'cpf_representante', class: 'W-10', header: 'CPF', tooltip: true, msg: 'CPF Representante', select: false },
        { field: 'nome_representante', class: 'W-15', header: 'Nome Represent.', tooltip: true, msg: 'Nome Representante', select: false }
    ];
    clienteLista: any[];
    habilitaAlteracao: boolean;
    adicaoSocio: boolean;
    isSociosSelected(): boolean {
        return this.selectedSociosReceitaFederal && this.selectedSociosReceitaFederal.length > 0;
    }
}