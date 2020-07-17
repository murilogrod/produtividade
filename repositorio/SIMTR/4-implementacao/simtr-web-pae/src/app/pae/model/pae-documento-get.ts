export class PaeDocumentoGet  {
    tipo_documento?: string = null;
    descricao_documento?: string = null
    mime_type?: string = null;
    origem_documento?: string = null;
    confidencial = true;
    valido = true;
    documento_substituicao?: string = null;
    justificativa_substituicao?: string = null;
    atributos_documento?: any []
    conteudos?: any = []
}