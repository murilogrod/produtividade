export class DocumentoGED {
	cliente?: number;
	ids: string[];
	tipoDocumento?: number;
	funcaoDocumental?: number;
	validade?: string;
	ordem?: number;
	matricula?: string;
	acao_documento?: string;
	identificador?: number;
	vinculo_anterior?: number;
	situacao?: string;
	motivo?: string;
	// NOVO
	tipo_documento?: number;
	origem_documento?: string;
	mime_type?: string;
	atributos: string[];
	imagens: string[];
	binario?: string;
	indiceDocListPdfOriginal?: number;
	quantidade_conteudos: number;
}