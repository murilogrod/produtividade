/* ACERTO DE DICIONARIO */
-------------------------
COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.vr_renda_mensal IS
'Atributo utilizado para armazenar o valor da renda mensal do cliente.
Esta informação pode influenciar no fluxo de tratamento de alguns processos.';
COMMENT ON COLUMN mtr.mtrtb001_pessoa_juridica.vr_faturamento_anual IS
'Atributo utilizado para armazenar o valor do fatutramento anual da empresa.
Esta informação pode influenciar no fluxo de tratamento de alguns processos.';

COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ic_canal_caixa IS 'Atributo utilizado para indicar o canal CAIXA utilizado para submeter o dossiê conforme o seguinte dominio:

AGE - Agência Fisica
AGD - Agência Digital
CCA - Correspondente Caixa Aqui';
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.nu_processo_fase IS 'Atributo utilizado para referenciar a etapa de andamento do dossiê.
A este registro deverá estar vinculado apenas processos do tipo "etapa"';
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ts_finalizado IS 'Atributo que armazena a data e hora que foi realizada a finalização do dossiê.';

COMMENT ON COLUMN mtr.mtrtb004_dossie_cliente_produto.nu_dossie_cliente_relacionado IS 'Atributo que armazena a referencia para o dossiê do cliente em que o registro de cliente atual possui relação.

Por exemplo, nos casos de identificar uma relação entre um CPF e um CNPJ junto ao dossiê do produto, o atributo "nu_dossie_cliente" manteria o registro do CPF e o atributo "nu_dossie_cliente_relacionado" manteria o registro do CNPJ';


COMMENT ON COLUMN mtr.mtrtb006_canal.ic_canal_caixa IS 'Atributo utilizado para indicar o canal CAIXA utilizado para submeter o dossiê conforme o seguinte dominio:

AGE - Agência Fisica
AGD - Agência Digital
CCA - Correspondente Caixa Aqui';

COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.no_classe_ged IS 'Atributo utilizado para armazenar o nome da classe no GED que deve ser utilizada para armazenar o documento junto ao SIECM.';

COMMENT ON COLUMN mtr.mtrtb012_tipo_situacao_dossie.ic_tipo_final IS 'Atributo utilizado para identificar o tipo de situação que é final. Após ser aplicado uma situação deste tipo, o sistema não deverá permitir que sejam criados novos registros de situação para o dossiê do produto';

COMMENT ON COLUMN mtr.mtrtb013_situacao_dossie.ts_saida IS 'Atributo utilizado para armazenar a data/hora de saída da situação ao dossiê';

COMMENT ON COLUMN mtr.mtrtb014_instancia_documento.nu_dossie_cliente_produto IS 'Atributo que representa e relação existente entre o dossiê de cliente e o dossiê de produto ao qual foi a instancia foi vinculada. Utilizado apenas para os casos de documentos associados pela vinculção definida pelo processo originador do dossiê de produto. Para os casos de documento definidos pelo elemento de conteudo do processo/produto ou da garantia informada no dossiê do produto este atributo não estara definido.';

COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_processo IS 'Atributo que representa o processo em que deverá ser apresentado o campo do formulário para preenchimento.

Só devem ser associado a este atributo, registros de processos que representam um processo que define uma etapa (fase).';

COMMENT ON COLUMN mtr.mtrtb020_processo.ic_dossie IS 'Atributo utilizado para identificar os processos que podem ter vinculação de dossiês de produto. Ao navegar na árvore de processo, ao chegar a um processo que possa ter vinculação de dossiê, o sistema apresenta as opções de inclusão de dossiê para o processo, considerando a parametrização do processo.

true - Pode ter vinculação de dossiê de produto
false - Não pode ter vinculação direta de dossiê de produto.';
COMMENT ON COLUMN mtr.mtrtb020_processo.ic_controla_validade_documento IS 'Atributo utilizado para identificar se os dossiês vinculados a este tipo de processo deverão ter os registros de documentos com a validade controlada. 
Caso positivo, quando um documento submetida (B) possui a mesma tipologia documental de um pré existente (A) faz com que o registro do documento mais antigo no repositorio (A) tenha o valor de ts_validade alterado para o momento da carga do novo documento(B).
Caso negativo, o atributo ts_validade deve ser sempre nulo e a situação da instancia do documento representada na tabela 017 deve ser registrada apenas como "DOCUMENTO CRIADO"

Apenas para processos que possuam este atributo como "true" a legenda de cores de documentos do dossiê de produto deve ser exibida.';
COMMENT ON COLUMN mtr.mtrtb020_processo.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o processo atribuído.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
A - Física ou Jurídica';

COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_apenso_adm IS 'Atributo utilizado para identificar o apenso administrativo, a qual a autorização esta vinculada.';
COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_contrato_adm IS 'Atributo utilizado para identificar o contrato administrativo, a qual a autorização esta vinculada.';
COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_processo_adm IS 'Atributo utilizado para identificar o processo administrativo, a qual a autorização esta vinculada.';

COMMENT ON COLUMN mtr.mtrtb025_processo_documento.ic_obrigatorio IS
'Atributo utilizado para identificar se um determinado documento é obrigatorio para o determinado tipo de relacionamento especificado no processo.

Caso este atributo seja true, significa que o documento que atende a indicação do tipo especifico ou função documental indicado deve estar presente na tabela de documentos e vinculado ao dossiê do cliente associado a operação conforme tipo de relacionamento indicado.

Caso este atributo seja false, trata-se de um documento opcional que poderá auxiloiar na analise da operação, mas que não é essencial e pode não existir na relação de documentos vinculados ao dossiê do cliente especificado.';
COMMENT ON COLUMN mtr.mtrtb025_processo_documento.ic_validar IS
'Atributo que indica se o documento deve ser validado quando vinculado no dossiê.
Caso verdadeiro, a instancia do documento deve ser criada com a situação "Criado" -> "Aguardando Avaliação.
Caso false, a instancia do documento deve ser criada com a situação de aprovada conforme regra de negocio realizada pelo sistema, desde que já exista outra instancia do mesmo documento com situação aprovada previamente.';

COMMENT ON COLUMN mtr.mtrtb029_campo_apresentacao.nu_campo_formulario IS 'Atributo que representa o campo do formulario ao qual a forma de exibição referencia.';

COMMENT ON COLUMN mtr.mtrtb030_resposta_dossie.nu_campo_formulario IS 'Atributo utilizado para identificar o campo do formulario dinamico ao qual a resposta esta vinculada.';

COMMENT ON COLUMN mtr.mtrtb032_elemento_conteudo.nu_processo IS 'Atributo que representa o processo ao qual o elemento de conteudo esta vinculado.

Caso o processo associado seja um processo dossiê, o elemento de conteudo deverá ser visivel durante todas as fases da operação.

Caso o processo associado seja um processo fase, o elemento de conteudo deverá ser visivel apenas quando o dossiê de produto estiver sendo representado na mesma fase de vinculação.';

COMMENT ON COLUMN mtr.mtrtb035_garantia_informada.de_garantia IS 'Atributo utilizado para armazenar uma descrição livre da garantia pelo usuario submissor.
Essa informação será utilizada pelo usuáro avaliador da operação/documentos.';

COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_documento_garantia IS
'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_garantia IS
'Atributo que representa o identificador da garantia associada ao registro de vinculação perante o dossiê de produto.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_processo IS
'Atributo que representa o processo definido na relação para identificar o tipo de documento necessario a apresentação visando a comprovação da garantia a ser ofertada.

Só devem ser associado a este atributo, registros de processos que representam um processo que é utilizado para gerar dossiê.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_tipo_documento IS
'Atributo que representa o tipo de documento especifico definido na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_funcao_documental IS
'Atributo que representa a função documental definida na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa o tipo documental deverá estar prenchido.';



/* Exclusão de valores Default */
--------------------------------
ALTER TABLE mtr.mtrtb035_garantia_informada ALTER COLUMN pc_garantia_informada DROP DEFAULT;
ALTER TABLE mtr.mtrtb035_garantia_informada ALTER COLUMN ic_forma_garantia DROP DEFAULT;