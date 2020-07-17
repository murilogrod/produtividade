package br.gov.caixa.simtr.modelo.mapeamento.constantes;

public final class ConstantesCadastroCampoFormulario {
	
	private ConstantesCadastroCampoFormulario() {}
	
	 // ************* API MODEL *************
    // Identificadores de objetos na visão das operações de cadastro interno a plataforma
    public static final String API_MODEL_CAMPO_FORMULARIO = "cadastro.campoformulario.CampoFormularioDTO";
    public static final String API_MODEL_PROCESSO_ORIGINADOR = "cadastro.campoformulario.ProcessoOriginadorDTO";
    public static final String API_MODEL_PROCESSO_FASE = "cadastro.campoformulario.ProcessoFaseDTO";
    public static final String API_MODEL_TIPO_RELACIONAMENTO = "cadastro.campoformulario.TipoRelacionamentoDTO";
    public static final String API_MODEL_PRODUTO = "cadastro.campoformulario.ProdutoDTO";
    public static final String API_MODEL_GARANTIA = "cadastro.campoformulario.GarantiaDTO";

    // ************* XML Root *************
    public static final String XML_ROOT_CAMPO_FORMULARIO = "campo_formulario";

    // ************* NOMES DE ATRIBUTOS *************
    public static final String PROCESSO_ORIGINADOR = "processo_originador";
    public static final String PROCESSO_FASE = "processo_fase";
    public static final String TIPO_RELACIONAMENTO = "tipo_relacionamento";
    public static final String PRODUTO = "produto";
    public static final String GARANTIA = "garantia";
    public static final String QUANTIDADE = "quantidade";
    public static final String ID = "id";
    public static final String NOME = "nome";
}
