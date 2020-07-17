import { DadoDivergente } from "../model/dadoDivergente";

import * as moment from 'moment';
import { VinculoCliente } from '../../../../model';
import { FIELDS_NAME } from "../../utils/utils-client";


const TELEFONE_COMUM = 11;

const TELEFONE_CELULAR = 14;

export class UtilsAbaIdentificacao {
    static isDifferent(value : string, otherValue : string) {
        return value != null && otherValue != null && value.trim() !== otherValue.trim();
    }

    static isDifferentNumber(value : number, otherValue : number) {
        return value != null && otherValue != null && value !== otherValue;
    }

    static getFirstMeioComunicacao(list) {
        for (const item of list) {
            if (item.meioComunicacao === TELEFONE_COMUM || item.meioComunicacao === TELEFONE_CELULAR) {
                return item;
            }
        }
    }

    static transformDateToPrimeNG(date: string) {
        if(date) {
            if(date.length == 10) {
                return date.substring(3,5) + '/' + date.substring(0,2) + '/' + date.substring(6,11);
            }else {
                let data = new Date(date);
                var mes=data.getMonth();                
                return data.getDate() + '/' + (mes++) + '/' + data.getFullYear();;
            }
        }
    }

    static isFieldDifferent(value, list: DadoDivergente[]) {
        if (list != null && list.length > 0) {
           for (const item of list) {
              if (item.field === value) {
                 return item;
              }
           }
        }
     }

    static createClienteBasedOnSicli(cpfCnpj : string , cpf : boolean, list : DadoDivergente[]) {
        const newCliente = new VinculoCliente();
        if (cpf) {
           newCliente.cpf = cpfCnpj;
           newCliente.tipo_pessoa = 'F';
           let data = this.isFieldDifferent(FIELDS_NAME.nome, list);
           if (data != null) {
              newCliente.nome = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.nome_mae, list);
           if (data != null) {
              newCliente.nome_mae = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.nome_pai, list);
           if (data != null) {
              newCliente.nome_pai = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.data_nascimento, list);
           if (data != null) {
              newCliente.data_nascimento = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.telefone, list);
           if (data != null) {
              newCliente.telefone = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.identidade, list);
           if (data != null) {
              newCliente.numero_identidade = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.orgao_emissor, list);
           if (data != null) {
              newCliente.orgao_emissor = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.nis, list);
           if (data != null) {
              newCliente.numero_nis = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.estado_civil, list);
           if (data != null) {
              newCliente.estado_civil = data.newValue;
           }
        } else {
           newCliente.cnpj = cpfCnpj;
           newCliente.tipo_pessoa = 'J';
           let data = this.isFieldDifferent(FIELDS_NAME.razao_social, list);
           if (data != null) {
              newCliente.razao_social = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.data_fundacao, list);
           if (data != null) {
              newCliente.data_fundacao = data.newValue;
           }
           data = this.isFieldDifferent(FIELDS_NAME.segmento, list);
           if (data != null) {
              newCliente.segmento = data.newValue;
           }
        }

        return newCliente;
    }

    static updateClienteSicli(cliente: VinculoCliente , sicliInformation) {
        const sicliUser = sicliInformation.dados.cliente.campos;
        if (cliente.tipo_pessoa === 'F') {
            cliente.nome = sicliUser.NOME_CLIENTE.NOME;
            cliente.nome_mae = sicliUser.NOME_MAE.nome;
            if (sicliUser.NOME_PAI != null) {
                cliente.nome_pai = sicliUser.NOME_PAI.nome;
            }
            const telefone = UtilsAbaIdentificacao.getFirstMeioComunicacao(sicliUser.MEIO_COMUNICACAO);
            if (telefone != null) {
                cliente.telefone = telefone.prefixoDiscagem + telefone.codComunicacao;
            }
            const data_nascimento = moment(sicliUser.DT_NASCIMENTO.data);
            if (data_nascimento != null) {
                cliente.data_nascimento = data_nascimento.format('DD/MM/YYYY');;
            }

            const identidade = sicliUser.IDENTIDADE != null && sicliUser.IDENTIDADE.length > 0 ? sicliUser.IDENTIDADE[0] : null;
            if (identidade != null) {
                cliente.numero_identidade = identidade.doc.numero;
                cliente.orgao_emissor = identidade.siglaorgemissor;
            }

            const nis = sicliUser.NIS != null && sicliUser.NIS.length > 0 ? sicliUser.NIS[0] : null;
            if (nis != null) {
                cliente.numero_nis = nis.doc.nis + nis.doc.dv;
            }

            cliente.estado_civil = sicliUser.ESTADO_CIVIL.estadocivil;
        } else {
            const data_fundacao = moment(sicliUser.DT_CONST_EMPRESA.data);
            cliente.razao_social = sicliUser.NOME_CLIENTE.NOME;
            cliente.data_fundacao = data_fundacao.format('DD/MM/YYYY');
        }
    }

    static updateCliente(cliente : VinculoCliente , newCliente : VinculoCliente) {
        if (newCliente.tipo_pessoa === 'F') {
            if (newCliente.nome != null) {
                cliente.nome = newCliente.nome;
            }
            if (newCliente.telefone != null) {
                cliente.telefone = newCliente.telefone;
            }
            if (newCliente.data_nascimento != null) {
                cliente.data_nascimento = newCliente.data_nascimento;
            }
            if (newCliente.nome_mae != null) {
                cliente.nome_mae = newCliente.nome_mae;
            }
            if (newCliente.nome_pai != null) {
                cliente.nome_pai = newCliente.nome_pai;
            }
            if (newCliente.numero_identidade != null) {
                cliente.numero_identidade = newCliente.numero_identidade;
            }
            if (newCliente.orgao_emissor != null) {
                cliente.orgao_emissor = newCliente.orgao_emissor;
            }
            if (newCliente.numero_nis != null) {
                cliente.numero_nis = newCliente.numero_nis;
            }
            if (newCliente.estado_civil != null) {
                cliente.estado_civil = newCliente.estado_civil;
            }
        } else {
            if (newCliente.razao_social != null) {
                cliente.razao_social = newCliente.razao_social;
            }
            if (newCliente.data_fundacao != null) {
                cliente.data_fundacao = newCliente.data_fundacao;
            }
            if (newCliente.segmento != null) {
                cliente.segmento = newCliente.segmento;
            }
        }
    }
}