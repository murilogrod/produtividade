import { StatusDocumento } from "src/app/model/model-tratamento/status-documento.enum";
import { Checklist } from "src/app/model/checklist";
import { RespostaCampoFormulario } from "src/app/model/resposta-campo-formulario";

export class UtilsTratamnto{
    public static verificaStatusDocumento(checkListDoc: Checklist): StatusDocumento{
        if(checkListDoc && !checkListDoc.situacaoConforme && 
            checkListDoc.habilitaVerificacao && 
                !checkListDoc.existeIncomformidade &&
                    (!checkListDoc.listaResposta || checkListDoc.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada))){
            return StatusDocumento.NOVO;
        } else if(checkListDoc && !checkListDoc.situacaoConforme && 
            !checkListDoc.habilitaVerificacao ||
                (checkListDoc && checkListDoc.existeIncomformidade != undefined && checkListDoc.existeIncomformidade && 
                    (!checkListDoc.listaResposta || checkListDoc.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada)))){
            return StatusDocumento.NAO_TRATADO;
        } else if(checkListDoc && !checkListDoc.situacaoConforme && 
                (checkListDoc.habilitaVerificacao && 
                    checkListDoc.existeIncomformidade &&
                        checkListDoc.listaResposta && 
                            !checkListDoc.listaResposta.find(resposta => undefined == resposta.verificacaoAprovada) && 
                                checkListDoc.listaResposta.find(resposta => false == resposta.verificacaoAprovada))){
            return StatusDocumento.REJEITADO;
        } else if(checkListDoc && (checkListDoc.situacaoConforme || (checkListDoc.habilitaVerificacao && undefined != checkListDoc.existeIncomformidade && !checkListDoc.existeIncomformidade))){
            return StatusDocumento.CONFORME;
        }
    }
}