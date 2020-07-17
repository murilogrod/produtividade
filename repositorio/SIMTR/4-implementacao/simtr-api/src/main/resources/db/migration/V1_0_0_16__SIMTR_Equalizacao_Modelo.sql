/* ALTERAÇÕES DE NOMES DE CHAVE PRIMARIA e INDICES */
----------------------------------------------------
ALTER INDEX "mtrsm001"."pk_mtrtb0032" RENAME TO "pk_mtrtb032";
ALTER INDEX "mtrsm001"."pk_mtrtb042" RENAME TO "pk_mtrtb019";
ALTER INDEX "mtrsm001"."pk_mtrtb043" RENAME TO "pk_mtrtb042";
ALTER INDEX "mtrsm001"."pk_mtrtb044_comportamento_pesq" RENAME TO "pk_mtrtb044";
ALTER INDEX "mtrsm001"."pk_mtrtb045_atributo_extracao" RENAME TO "pk_mtrtb045";
ALTER INDEX "mtrsm001"."pk_mtrtb102_sicli_erro" RENAME TO "pk_mtrtb200";
ALTER INDEX "mtrsm001"."ix_mtrtb005_01" RENAME TO "ix_mtrtb009_01";
ALTER INDEX "mtrsm001"."ix_mtrtb005_02" RENAME TO "ix_mtrtb009_02";

/* Adequação de nome de tabela acima de 31 caracteres */
-------------------------------------------------------
ALTER TABLE "mtrsm001"."mtrtb040_cadeia_tipo_stco_dossie" RENAME TO "mtrtb040_cadeia_tipo_sto_dossie";

/* Definição de valores para colunas transformadas em NOT NULL ou criadas indice de unicidade*/
----------------------------------------------------------------------------------------------
UPDATE mtrsm001.mtrtb003_documento SET ic_dossie_digital = false;
UPDATE mtrsm001.mtrtb019_campo_formulario SET ic_obrigatorio = false;
UPDATE mtrsm001.mtrtb019_campo_formulario SET nu_ordem = nu_campo_entrada; -- APENAS PARA IMPEDIR MESMA ORDEM NO MESMO PROCESSO
INSERT INTO mtrsm001.mtrtb006_canal(nu_versao, sg_canal, de_canal, co_integracao) VALUES (1, 'SIMTR', 'Cliente Web do SIMTR na Intranet', 9999999999);
UPDATE mtrsm001.mtrtb003_documento SET nu_canal_captura = (SELECT nu_canal FROM mtrsm001.mtrtb006_canal WHERE co_integracao = 9999999999) WHERE nu_canal_captura is null;
UPDATE mtrsm001.mtrtb026_relacao_processo SET nu_ordem = null WHERE nu_processo_pai NOT IN (SELECT nu_processo FROM mtrsm001.mtrtb020_processo WHERE ic_dossie = true);

/* Exclusão de registros excluidos para adequação da tabela */
---------------------------------------------------------------
DELETE FROM mtrsm001.mtrtb043_documento_garantia;

/* Adequação de Sequences - Parte 01 de 02 */
--------------------------------------------
ALTER SEQUENCE mtrsm001.mtrsq006_canal_captura RENAME TO mtrsq006_canal;
create sequence mtrsm001.MTRSQ004_DOSSIE_CLIENTE_PRODUTO;
create sequence mtrsm001.MTRSQ017_SITUACAO_INSTNCA_DCMNTO;
create sequence mtrsm001.MTRSQ019_CAMPO_FORMULARIO;
create sequence mtrsm001.MTRSQ032_ELEMENTO_CONTEUDO;
create sequence mtrsm001.MTRSQ043_DOCUMENTO_GARANTIA;
create sequence mtrsm001.MTRSQ046_PROCESSO_ADM;
create sequence mtrsm001.MTRSQ047_CONTRATO_ADM;
create sequence mtrsm001.MTRSQ048_APENSO_ADM;
create sequence mtrsm001.MTRSQ049_DOCUMENTO_ADM;
create sequence mtrsm001.MTRSQ103_AUTORIZACAO_ORIENTACAO;


/* Derubada de chaves para correção de nomeclatura da chave primaria da tabela 019 */
------------------------------------------------------------------------------------
ALTER TABLE mtrsm001.mtrtb029_campo_apresentacao DROP CONSTRAINT fk_mtrtb0w9_mtrtb042;
ALTER TABLE mtrsm001.mtrtb030_resposta_dossie DROP CONSTRAINT fk_mtrtb030_mtrtb042;

--------------------------------------------------------------------------------------------------------------

/* Tabela 001 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb001_dossie_cliente" IS 'Tabela responsável pelo armazenamento do dossiê do cliente com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do cliente.';
COMMENT ON TABLE "mtrsm001"."mtrtb001_pessoa_fisica" IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa física.';
COMMENT ON TABLE "mtrsm001"."mtrtb001_pessoa_juridica" IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa jurídica.';

/* Tabela 002 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb002_dossie_produto" ALTER COLUMN "nu_processo_dossie" TYPE INT4, ALTER COLUMN "nu_processo_dossie" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb002_dossie_produto" ALTER COLUMN "nu_processo_fase" TYPE INT4, ALTER COLUMN "nu_processo_dossie" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb002_dossie_produto" IS 'Tabela responsável pelo armazenamento do dossiê do produto com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do produto.
Para cada produto contratado ou submetido a análise deve ser gerado um novo registro representando o vínculo com o cliente.';

/* Tabela 003 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ALTER COLUMN "ic_dossie_digital" TYPE BOOLEAN, ALTER COLUMN "ic_dossie_digital" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ALTER COLUMN "ic_temporario" TYPE INT4, ALTER COLUMN "ic_temporario" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ALTER COLUMN "ix_antifraude" TYPE NUMERIC(5,2), ALTER COLUMN "ix_antifraude" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ALTER COLUMN "ts_validade" TYPE TIMESTAMP, ALTER COLUMN "ts_validade" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ALTER COLUMN "nu_canal_captura" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ADD COLUMN "co_ged" VARCHAR(255) NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb003_documento" ALTER COLUMN "nu_tipo_documento" TYPE INT4, ALTER COLUMN "nu_tipo_documento" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb003_documento" IS 'Tabela responsável pelo armazenamento da referência dos documentos de um determinado cliente.
Esses documentos podem estar associados a um ou mais dossiês de produtos possibilitando o reaproveitamento dos mesmos em diversos produtos.
Nesta tabela serão efetivamente armazenados os dados dos documentos que pode representar o agrupamento de uma ou mais imagens na sua formação.
Também deverão ser armazenadas as propriedades do mesmo e as marcas conforme seu ciclo de vida.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ic_dossie_digital" IS 'Atributo utilizado para determinar que o documento foi obtido do repositorio do dossiê digital e encontra-se disponivel no OBJECT STORE dessa solução.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ic_temporario" IS 'Atributo utilizado para indicar se o documento ainda esta fase de analise sob a otica do dossiê digital.

Quando um documento for submetido pelo fluxo do dossiê digital, antes de utiliza-lo uma serie de verificações deve ser realizada dentro de um determinado espaço de tempo, senão esse documento será expurgado da base.

Esse atributo pode assumir o dominio abaixo:

0 - Definitivo
1 - Temporario - Extração de dados (OCR)
2 - Temporario - Antifraude';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."co_extracao" IS 'Atributo utilizado para armazenar o codigo de identificação junto ao serviço de extração de dados';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ts_envio_extracao" IS 'Atributo utilizado para armazenar a data e hora de envio do documento para o serviço de extração de dados.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ts_retorno_extracao" IS 'Atributo utilizado para armazenar a data e hora de retorno dos atributos extraidos do documento junto ao serviço de extração de dados.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."co_autenticidade" IS 'Atributo utilizado para armazenar o codigo de identificação do documento junto ao serviço de avaliação de autenticidade documental';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ts_envio_autenticidade" IS 'Atributo utilizado para armazenar a data e hora de envio do documento para o serviço de avaliação de autenticidade documental.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ts_retorno_autenticidade" IS 'Atributo utilizado para armazenar a data e hora de retorno do score junto ao serviço de avaliação de autenticidade documental.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ix_antifraude" IS 'Atributo utilizado para armazenar o indice retornado pelo serviço de antifraude para o documento.
O indice devolve o percentual para o indicio de fraude no documento.
Para documentos validos este valor deve tender a zero.';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."ts_validade" IS 'Atributo que armazena a data de validade do documento conforme definições corporativas calculado pelo prazo definido no tipo documento.

Sempre que definida uma data futura para o documento a hora deve ser definida com o valor de 23:59:59';
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."co_ged" IS 'Atributo utilizado para identificar a localização de um documento armazenado em um sistema de Gestão Eletronica de Documentos (GED).
A utilização deste atrinbuto dependerá da estrategia de armazenamento, pois se a identificação for feita através do registro do documento, será aqui que deverá estar armazenado o identificador único deste conteudo perante o GED, porém se a identificação for individual por conteudo (pagina, imagem, etc) este atributo ficará inutilizado.';

/* Tabela 004 */
---------------
ALTER TABLE "mtrsm001"."mtrtb004_dossie_cliente_produto" ADD COLUMN "nu_dossie_cliente_relacionado" BIGINT;
ALTER TABLE "mtrsm001"."mtrtb004_dossie_cliente_produto" DROP COLUMN "ts_vinculo_pessoa";
COMMENT ON TABLE "mtrsm001"."mtrtb004_dossie_cliente_produto" IS 'Tabela de relacionamento para permitir vincular um dossiê de produto a mais de um dossiê de cliente devido a necessidades de produtos com mais de um titular.';

/* Tabela 006 */
---------------
ALTER TABLE "mtrsm001"."mtrtb006_canal" ADD COLUMN "ic_canal_caixa" VARCHAR(3) NOT NULL DEFAULT 'AGE';
ALTER TABLE "mtrsm001"."mtrtb006_canal" ALTER COLUMN "ic_canal_caixa" DROP DEFAULT;
ALTER TABLE "mtrsm001"."mtrtb006_canal" ALTER COLUMN "sg_canal" TYPE VARCHAR(10);
COMMENT ON TABLE "mtrsm001"."mtrtb006_canal" IS 'Tabela responsável pelo armazenamento dos possíveis canais de captura de um documento para identificação de sua origem.';

/* Tabela 007 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb007_atributo_documento" ALTER COLUMN "ix_assertividade" TYPE NUMERIC(5,2), ALTER COLUMN "ix_assertividade" DROP NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb007_atributo_documento" IS 'Tabela responsável por armazenar os atributos capturados do documento utilizando a estrutura de chave x valor onde o nome do atributo determina o campo do documento que a informação foi extraída e o conteúdo trata-se do dado propriamente extraído.';
COMMENT ON COLUMN "mtrsm001"."mtrtb007_atributo_documento"."ix_assertividade" IS 'Atributo utilizado para armazenar o indice de assertividade retornado pelo serviço de extração de dados. ';

/* Tabela 008 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb008_conteudo" IS 'Tabela responsável pelo armazenamento das referências de conteúdo que compõem o documento.
Nesta tabela serão efetivamente armazenados os dados que caracterizam a imagem (ou o binário) e dados para localização do arquivo propriamente dito no repositório.';
COMMENT ON COLUMN "mtrsm001"."mtrtb008_conteudo"."de_uri" IS 'Atributo utilizado para identificar a localização de um conteudo armazenado em compartilhamento de arquivos ou sob algum serviço web.';
COMMENT ON COLUMN "mtrsm001"."mtrtb008_conteudo"."co_ged" IS 'Atributo utilizado para identificar a localização de um conteudo armazenado em um sistema de Gestão Eletronica de Documentos (GED).
A utilização deste atrinbuto dependerá da estrategia de armazenamento, pois se a identificação for feita através do registro do documento, este atributo ficará inutilizado, porém se a identificação for individual por conteudo (pagina, imagem, etc) será aqui que deverá estar armazenado o identificador único deste conteudo perante o GED.';

/* Tabela 009 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb009_tipo_documento" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb009_tipo_documento" ALTER COLUMN "ic_tipo_pessoa" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb009_tipo_documento" ADD COLUMN "ic_reuso" BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE ONLY "mtrsm001"."mtrtb009_tipo_documento" ALTER COLUMN "ic_reuso" DROP DEFAULT;
ALTER TABLE ONLY "mtrsm001"."mtrtb009_tipo_documento" ADD COLUMN "ic_processo_administrativo" BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE ONLY "mtrsm001"."mtrtb009_tipo_documento" ALTER COLUMN "ic_processo_administrativo" DROP DEFAULT;
COMMENT ON TABLE "mtrsm001"."mtrtb009_tipo_documento" IS 'Tabela responsável pelo armazenamento dos possíveis tipos de documento que podem ser submetidos ao vínculo com os dossiês.';
COMMENT ON COLUMN "mtrsm001"."mtrtb009_tipo_documento"."nu_tipo_documento" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm001"."mtrtb009_tipo_documento"."ic_reuso" IS 'Atributo utilizado para identificar se deve ser aplicado reuso ou não na carga do documento.

Existem situações, em que é necessario carregar um novo documento do mesmo tipo de forma atualizada, pois espera-se que o documento contenha novas informações devido ao andamento da contratação.

Nestes casos, não deverá ser realizada a inclusão de uma instancia do documento de forma automatica para esta determinada tipologia';
COMMENT ON COLUMN "mtrsm001"."mtrtb009_tipo_documento"."ic_processo_administrativo" IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Processo Administrativo Eletronico (PAE) considerando que o mesmo possui restrições de apresentação quanto as opções de seleção para o usuário.';



/* Tabela 010 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb010_funcao_documental" ALTER COLUMN "nu_funcao_documental" TYPE INTEGER, ALTER COLUMN "nu_funcao_documental" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb010_funcao_documental" IS 'Tabela responsável por armazenar as possíveis funções documentais.
Essa informação permite agrupar documentos que possuem a mesma finalidade e um documento pode possui mais de uma função.
Exemplos dessa atribuição funcional, são:
- Identificação;
- Renda;
- Comprovação de Residência
- etc';
COMMENT ON COLUMN "mtrsm001"."mtrtb010_funcao_documental"."nu_funcao_documental" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 011 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb011_funcao_documento" ALTER COLUMN "nu_funcao_documental" TYPE INTEGER, ALTER COLUMN "nu_funcao_documental" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb011_funcao_documento" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb011_funcao_documento" IS 'Tabela associativa que vincula um tipo de documento a sua função.
Ex: 
- RG x Identificação
- DIRPF x Renda
- DIRPF x Identificação';

/* Tabela 012 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb012_tipo_situacao_dossie" ALTER COLUMN "nu_tipo_situacao_dossie" TYPE INTEGER, ALTER COLUMN "nu_tipo_situacao_dossie" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb012_tipo_situacao_dossie" IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um dossiê de produto.

Como exemplo podemos ter as possíveis situações:
- Criado
- Atualizado
- Disponível
- Em Análise
- etc';
COMMENT ON COLUMN "mtrsm001"."mtrtb012_tipo_situacao_dossie"."nu_tipo_situacao_dossie" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 013 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb013_situacao_dossie" ALTER COLUMN "nu_tipo_situacao_dossie" TYPE INTEGER, ALTER COLUMN "nu_tipo_situacao_dossie" SET NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb013_situacao_dossie" ADD COLUMN "ts_saida" TIMESTAMP;
COMMENT ON TABLE "mtrsm001"."mtrtb013_situacao_dossie" IS 'Tabela responsável por armazenar o histórico de situações relativas ao dossiê do produto. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';
COMMENT ON COLUMN "mtrsm001"."mtrtb013_situacao_dossie"."nu_tipo_situacao_dossie" IS 'Atributo utilizado para armazenar o tipo situação do dossiê que será atribuido manualmente pelo operador ou pela automacao do workflow quando estruturado.';

/* Tabela 014 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb014_instancia_documento" IS 'Tabela responsável pelo armazenamento de instâncias de documentos que estarão vinculados aos dossiês dos produtos.';

/* Tabela 015 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb015_situacao_documento" ALTER COLUMN "nu_situacao_documento" TYPE INTEGER, ALTER COLUMN "nu_situacao_documento" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb015_situacao_documento" IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um documento.
Essas situações também deverão agrupar motivos para atribuição desta situação.
Como exemplo podemos ter as possíveis situações e entre parênteses os motivos de agrupamento:
- Aprovado
- Rejeitado (Ilegível / Rasurado / Segurança)
- Pendente (Recaptura)';
COMMENT ON COLUMN "mtrsm001"."mtrtb015_situacao_documento"."nu_situacao_documento" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 016 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb016_motivo_stco_documento" ALTER COLUMN "nu_motivo_stco_documento" TYPE INTEGER, ALTER COLUMN "nu_motivo_stco_documento" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb016_motivo_stco_documento" ALTER COLUMN "nu_situacao_documento" TYPE INTEGER, ALTER COLUMN "nu_situacao_documento" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb016_motivo_stco_documento" IS 'Tabela de motivos específicos para indicar a causa de uma determinada situação vinculada a um dado documento.
Exemplo:  
- Ilegível -> Rejeitado
- Rasurado -> rejeitado
- Segurança -> Rejeitado
- Recaptura -> pendente';
COMMENT ON COLUMN "mtrsm001"."mtrtb016_motivo_stco_documento"."nu_motivo_stco_documento" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 017 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb017_stco_instnca_documento" ALTER COLUMN "nu_motivo_stco_documento" TYPE INTEGER, ALTER COLUMN "nu_motivo_stco_documento" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb017_stco_instnca_documento" ALTER COLUMN "nu_situacao_documento" TYPE INTEGER, ALTER COLUMN "nu_situacao_documento" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb017_stco_instnca_documento" IS 'Tabela responsável por armazenar o histórico de situações relativas a instância do documento em avaliação. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';
COMMENT ON COLUMN "mtrsm001"."mtrtb017_stco_instnca_documento"."nu_motivo_stco_documento" IS 'Atributo utilizado pata armazenar a referencia o motivo especifico para a situação escolhida vinculada a instancia do documento em avaliação';
COMMENT ON COLUMN "mtrsm001"."mtrtb017_stco_instnca_documento"."nu_situacao_documento" IS 'Atributo utilizado pata armazenar a referencia a situação do documento escolhida vinculada a instancia do documento em avaliação';

/* Tabela 018 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb018_unidade_tratamento" IS 'Tabela utilizada para identificar as unidades de tratamento atribuídas para o dossiê naquele dado momento.
Sempre que a situação do dossiê for modificada, os registros referentes ao dossiê especificamente serão excluídos e reinseridos novos com base na nova situação.';

/* Tabela 019 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb019_campo_formulario" ALTER COLUMN "ic_ativo" TYPE BOOLEAN, ALTER COLUMN "ic_ativo" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb019_campo_formulario" ALTER COLUMN "ic_obrigatorio" TYPE BOOLEAN, ALTER COLUMN "ic_obrigatorio" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb019_campo_formulario" ALTER COLUMN "nu_processo" TYPE INT4, ALTER COLUMN "nu_processo" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb019_campo_formulario" IS 'Atributo utilizado para vincular o campo entrrada ao formulário.';

/* Tabela 020 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb020_processo" ALTER COLUMN "nu_processo" TYPE INTEGER, ALTER COLUMN "nu_processo" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb020_processo" IS 'Tabela responsável pelo armazenamento dos processos que podem ser atrelados aos dossiês de forma a identificar qual o processo bancário relacionado.
Processos que possuam vinculação com dossiês de produto não devem ser excluídos fisicamente, e sim atribuídos como inativo.
Exemplos de processos na linguagem negocial são:
- Concessão de Crédito Habitacional
- Conta Corrente
- Financiamento de Veículos
- Pagamento de Loterias
- Etc';
COMMENT ON COLUMN "mtrsm001"."mtrtb020_processo"."nu_processo" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 021 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb021_unidade_autorizada" ALTER COLUMN "ic_tipo_tratamento" TYPE VARCHAR(10), ALTER COLUMN "ic_tipo_tratamento" SET NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb021_unidade_autorizada" ADD COLUMN "nu_apenso_adm" BIGINT;
ALTER TABLE "mtrsm001"."mtrtb021_unidade_autorizada" ADD COLUMN "nu_contrato_adm" BIGINT;
ALTER TABLE ONLY "mtrsm001"."mtrtb021_unidade_autorizada" ALTER COLUMN "nu_processo" TYPE INTEGER, ALTER COLUMN "nu_processo" SET NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb021_unidade_autorizada" ADD COLUMN "nu_processo_adm" BIGINT;
ALTER TABLE ONLY "mtrsm001"."mtrtb021_unidade_autorizada" ALTER COLUMN "nu_processo" DROP NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb021_unidade_autorizada" IS 'Tabela responsável pelo armazenamento das unidades autorizadas a utilização do processo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb021_unidade_autorizada"."ic_tipo_tratamento" IS 'Atributo que indica as ações possiveis a serem realizadas no sistema para a determinada unidade de acordo com o registro de vinculação da autorização. A representação dos valores das ações são definidas em numeração binaria e determinam quais as permissões da unidade sobre os itens aos quais estão vinculados. Os valores possiveis são:

1 - CONSULTA_DOSSIE
2 - TRATAR_DOSSIE
4 - CRIAR_DOSSIE
8 - PRIORIZAR_DOSSIE
16 - ALIMENTAR_DOSSIE

Considerando o fato, como exemplo uma unidade que possa consultar, tratar e criar dossiês, mas não pode priorizar, nem alimentar o dossiê e deve estar representado o valor 7 no seguinte formato:

0000000111';
COMMENT ON COLUMN "mtrsm001"."mtrtb021_unidade_autorizada"."nu_processo" IS 'Atributo que representa o processo na definição de autorização a realização de algum tipo de tratamento por uma determinada unidade.';

/* Tabela 022 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb022_produto" ADD COLUMN "ic_pesquisa_sinad" BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE ONLY "mtrsm001"."mtrtb022_produto" ALTER COLUMN "ic_pesquisa_sinad" DROP DEFAULT;
COMMENT ON TABLE "mtrsm001"."mtrtb022_produto" IS 'Tabela responsável pelo armazenamento dos produtos da CAIXA que serão vinculados aos processos definidos.';
COMMENT ON COLUMN "mtrsm001"."mtrtb022_produto"."ic_pesquisa_sinad" IS 'Atributo utilizado para identificar se a pesquisa junto a Receita deve ser realizada para o produto especificado.';

/* Tabela 023 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb023_produto_processo" ALTER COLUMN "nu_processo" TYPE INTEGER, ALTER COLUMN "nu_processo" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb023_produto_processo" IS 'Tabela de relacionamento para vinculação do produto com o processo. 
Existe a possibilidade que um produto seja vinculado a diversos processos pois pode diferenciar a forma de realizar as ações conforme o canal de contratação, campanha, ou outro fator, como por exemplo uma conta que seja contratada pela agência física, agência virtual, CCA ou Aplicativo de abertura de contas.';

/* Tabela 024 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb024_produto_dossie" IS 'Tabela de relacionamento para vinculação dos produtos selecionados para tratamento no dossiê. 
Existe a possibilidade que mais de um produto seja vinculado a um dossiê para tratamento único como é o caso do contrato de relacionamento que envolve Cartão de Credito / CROT / CDC / Conta Corrente.';

/* Tabela 025 */
---------------
ALTER TABLE "mtrsm001"."mtrtb025_processo_documento" ADD COLUMN "ic_obrigatorio" BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE "mtrsm001"."mtrtb025_processo_documento" ALTER COLUMN "ic_obrigatorio" DROP DEFAULT;
ALTER TABLE "mtrsm001"."mtrtb025_processo_documento" ADD COLUMN "ic_validar" BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE "mtrsm001"."mtrtb025_processo_documento" ALTER COLUMN "ic_validar" DROP DEFAULT;
ALTER TABLE ONLY "mtrsm001"."mtrtb025_processo_documento" ALTER COLUMN "nu_funcao_documental" TYPE INTEGER, ALTER COLUMN "nu_funcao_documental" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb025_processo_documento" ALTER COLUMN "nu_processo" TYPE INTEGER, ALTER COLUMN "nu_processo" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb025_processo_documento" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" DROP NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb025_processo_documento" IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para os titulares do dossiê de um processo específico. Fazendo um paralelo, seria como o os elementos do conteúdo, porém voltados aos documentos do cliente, pois um dossiê pode ter a quantidade de clientes definida dinamicamente e por isso não cabe na estrutura do elemento do conteúdo. Esta estrutura ficará a cargo dos elementos específicos do produto.
Quando um dossiê é criado, todos os CNPFs/CNPJs envolvidos na operação deverão apresentar os tipos de documentos ou algum documento da função documental definidas nesta relação com o processo específico definido para o dossiê de produto.';
COMMENT ON COLUMN "mtrsm001"."mtrtb025_processo_documento"."nu_funcao_documental" IS 'Atrinuto utilizado para referenciar uma função documental necessaria ao processo. Quando definido, qualquer documento valido que o cliente tenha que seja desta função documental, deve ser considerado que o documento existente já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.';
COMMENT ON COLUMN "mtrsm001"."mtrtb025_processo_documento"."nu_processo" IS 'Atributo utilizado para identificar o processo de vinculação para agrupar os documentos necessarios.';
COMMENT ON COLUMN "mtrsm001"."mtrtb025_processo_documento"."nu_tipo_documento" IS 'Atrinuto utilizado para referenciar um tipo de documento especifico, necessari ao processo. Quando definido, apenas a presença do documento especifica em estado valido, presente e associado ao dossiê do cliente deve ser considerado existente e já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';

/* Tabela 026 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb026_relacao_processo" ALTER COLUMN "nu_processo_filho" TYPE INTEGER, ALTER COLUMN "nu_processo_filho" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb026_relacao_processo" ALTER COLUMN "nu_processo_pai" TYPE INTEGER, ALTER COLUMN "nu_processo_pai" SET NOT NULL;
COMMENT ON COLUMN "mtrsm001"."mtrtb026_relacao_processo"."nu_processo_pai" IS 'Atributo que representa o processo pai da relação entre os processos. Os processos "pai" são os processos que estão em nivel superior em uma visão de arvore de processos.
Os processos que não possuem registro com outro processo pai são conhecidos como processos patriarcas e estes são os processos inicialmente exibidos nas telas de tratamento e/ou visão de arvores.';

/* Tabela 027 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb027_campo_entrada" IS 'Tabela responsável por armazenar a estrutura de entradas de dados que serão alimentados na inclusão de um novo dossiê para o processo vinculado.
Esta estrutura permitirá realizar a construção dinâmica do formulário.
Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';

/* Tabela 028 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb028_opcao_campo" IS 'Tabela responsável pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- Lista;
- Radio;
- Check;

Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';

/* Tabela 029 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb029_campo_apresentacao" IS 'Tabela utilizada para armazenar informações acerca da apresentação do campo na interface gráfica conforme o dispositivo.';

/* Tabela 030 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb030_resposta_dossie" IS 'Tabela responsável pelo armazenamento das respostas aos itens montados dos formulários de inclusão de processos para um dossiê específico.';

/* Tabela 031 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb031_resposta_opcao" IS 'Tabela de relacionamento com finalidade de armazenar todas as respostas objetivas informadas pelo cliente a mesma pergunta no formulário de identificação do dossiê.';

/* Tabela 032 */
---------------
ALTER TABLE "mtrsm001"."mtrtb032_elemento_conteudo" DROP COLUMN "nu_fase_utilizacao";
ALTER TABLE ONLY "mtrsm001"."mtrtb032_elemento_conteudo" ALTER COLUMN "nu_processo" TYPE INTEGER, ALTER COLUMN "nu_processo" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb032_elemento_conteudo" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" DROP NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb032_elemento_conteudo" IS 'Tabela responsável pelo armazenamento dos elementos que compõem o mapa de documentos para vinculação ao processo.
Esses elementos estão associados aos tipos de documentos para identificação dos mesmo no ato da captura.';
COMMENT ON COLUMN "mtrsm001"."mtrtb032_elemento_conteudo"."nu_processo" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 033 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb033_garantia" ALTER COLUMN "ic_fidejussoria" TYPE BOOLEAN, ALTER COLUMN "ic_fidejussoria" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb033_garantia" ALTER COLUMN "nu_garantia" TYPE INTEGER, ALTER COLUMN "nu_garantia" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb033_garantia" IS 'Tabela responsável pelo armazenamento das garantias da CAIXA que serão vinculados aos dossiês criados.';
COMMENT ON COLUMN "mtrsm001"."mtrtb033_garantia"."ic_fidejussoria" IS 'Atributo utilizado para identificar se trata-se de uma garantia fidejussoria a exemplo do avalista';
COMMENT ON COLUMN "mtrsm001"."mtrtb033_garantia"."nu_garantia" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 034 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb034_garantia_produto" ALTER COLUMN "nu_garantia" TYPE INTEGER, ALTER COLUMN "nu_garantia" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb034_garantia_produto" IS 'Tabela de relacionamento responsável por vincular as garantias possíveis de exibição quando selecionado um dado produto.';

/* Tabela 035 */
---------------
ALTER TABLE "mtrsm001"."mtrtb035_garantia_informada" DROP COLUMN "nu_dossie_cliente";
ALTER TABLE ONLY "mtrsm001"."mtrtb035_garantia_informada" ALTER COLUMN "nu_garantia" TYPE INTEGER, ALTER COLUMN "nu_garantia" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb035_garantia_informada" IS 'Tabela responsável por manter a lista de garantias informadas durante o ciclo de vida do dossiê do produto.
Os documentos submetidos são arquivados normalmente na tabela de documentos e vinculados ao dossiê do produto através de instâncias.';
COMMENT ON COLUMN "mtrsm001"."mtrtb035_garantia_informada"."nu_garantia" IS 'Atributo que representa a chave primaria da entidade.';

/* Tabela 036 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb036_composicao_documental" IS 'Tabela responsável por agrupar tipos de documentos visando criar estruturas que representam conjuntos de tipos de documentos a serem analisados conjuntamente.
Essa conjunção será utilizada na análise do nível documento e por ser formada como os exemplos a seguir:
- RG
- Contracheque
-----------------------------------------------------------------------------------
- CNH
- Conta Concessionária
- DIRPF';

/* Tabela 037 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb037_regra_documental" ALTER COLUMN "nu_funcao_documental" TYPE INTEGER, ALTER COLUMN "nu_funcao_documental" DROP NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb037_regra_documental" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" DROP NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb037_regra_documental" RENAME "in_antifraude"  TO "ix_antifraude";
COMMENT ON TABLE "mtrsm001"."mtrtb037_regra_documental" IS 'Tabela utilizada para armazenar as regras de atendimento da composição. Para que uma composição documental esteja satisfeita, todas as regras a ela associadas devem ser atendidas, ou seja, a regra para cada documento definido deve ser verdadeira. Além da presença do documento vinculado valida no dossiê situações como índice mínimo do antifraude e canal devem ser respeitadas. Caso não seja atendida ao menos uma das regras, a composição não terá suas condições satisfatórias atendidas e consequentemente o nível documental não poderá ser atribuído ao dossiê do cliente.';

/* Tabela 038 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb038_nivel_documental" IS 'Tabela responsável por armazenar as referências de níveis documentais possíveis para associação a clientes e produtos.
O nível documental é uma informação pertencente ao cliente, porém o mesmo deve estar associado a um conjunto de tipos de documentos e informações que torna a informação dinâmica para o cliente, ou seja, se um cliente submete um determinado documento que aumenta sua gama de informações válidas, ele pode ganhar um determinado nível documental, porém se um documento passa a ter sua validade ultrapassada o cliente perde aquele determinado nível.';

/* Tabela 039 */
---------------
COMMENT ON TABLE "mtrsm001"."mtrtb039_produto_composicao" IS 'Tabela de relacionamento que vincula uma composição de documentos a um ou mais produtos.
Essa associação visa identificar as necessidade documentais para um determinado produto no ato de sua contratação, permitindo ao sistema autorizar ou não a operação do ponto de vista documental. ';

/* Tabela 040 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb040_cadeia_tipo_sto_dossie" ALTER COLUMN "nu_tipo_situacao_atual" TYPE INTEGER, ALTER COLUMN "nu_tipo_situacao_atual" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb040_cadeia_tipo_sto_dossie" ALTER COLUMN "nu_tipo_situacao_seguinte" TYPE INTEGER, ALTER COLUMN "nu_tipo_situacao_seguinte" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb040_cadeia_tipo_sto_dossie" IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em um dossiê de produto a partir um determinado tipo de situação.';
COMMENT ON COLUMN "mtrsm001"."mtrtb040_cadeia_tipo_sto_dossie"."nu_tipo_situacao_atual" IS 'Atributo que representa o tipo de situação atual na relação';
COMMENT ON COLUMN "mtrsm001"."mtrtb040_cadeia_tipo_sto_dossie"."nu_tipo_situacao_seguinte" IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de um dossiê de produto';

/* Tabela 041 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb041_cadeia_stco_documento" ALTER COLUMN "nu_situacao_documento_atual" TYPE INTEGER, ALTER COLUMN "nu_situacao_documento_atual" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb041_cadeia_stco_documento" ALTER COLUMN "nu_situacao_documento_seguinte" TYPE INTEGER, ALTER COLUMN "nu_situacao_documento_seguinte" SET NOT NULL;
COMMENT ON TABLE "mtrsm001"."mtrtb041_cadeia_stco_documento" IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em uma instância de documento a partir um determinado tipo de situação.';
COMMENT ON COLUMN "mtrsm001"."mtrtb041_cadeia_stco_documento"."nu_situacao_documento_atual" IS 'Atributo que representa o tipo de situação atual na relação';
COMMENT ON COLUMN "mtrsm001"."mtrtb041_cadeia_stco_documento"."nu_situacao_documento_seguinte" IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de uma instancia de documento.';

/* Tabela 043 */
---------------
ALTER TABLE "mtrsm001"."mtrtb043_documento_garantia" DROP CONSTRAINT "pk_mtrtb043_documento_garantia";
ALTER TABLE "mtrsm001"."mtrtb043_documento_garantia" ADD COLUMN "nu_documento_garantia" BIGINT NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb043_documento_garantia" ADD COLUMN "nu_funcao_documental" INTEGER;
ALTER TABLE ONLY "mtrsm001"."mtrtb043_documento_garantia" ALTER COLUMN "nu_garantia" TYPE INTEGER, ALTER COLUMN "nu_garantia" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb043_documento_garantia" ALTER COLUMN "nu_processo" TYPE INTEGER, ALTER COLUMN "nu_processo" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb043_documento_garantia" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" DROP NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb043_documento_garantia" ADD COLUMN "nu_versao" INTEGER NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb043_documento_garantia" ADD CONSTRAINT "pk_mtrtb043" PRIMARY KEY ("nu_documento_garantia");
COMMENT ON COLUMN "mtrsm001"."mtrtb043_documento_garantia"."nu_tipo_documento" IS 'Atributo que representa o tipo de documento especifico definido na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';

/* Tabela 044 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb044_comportamento_pesquisa" ALTER COLUMN "nu_comportamento_pesquisa" TYPE INTEGER, ALTER COLUMN "nu_comportamento_pesquisa" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb044_comportamento_pesquisa" ALTER COLUMN "de_orientacao" SET NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb044_comportamento_pesquisa" RENAME "vr_codigo_retorno" TO "ic_codigo_retorno";


/* Tabela 045 */
---------------
ALTER TABLE ONLY "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "de_atributo_ged" TYPE VARCHAR(255), ALTER COLUMN "de_atributo_ged" DROP NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_calculo_data" BOOLEAN;
ALTER TABLE ONLY "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "nu_atributo_extracao" TYPE INTEGER, ALTER COLUMN "nu_atributo_extracao" SET NOT NULL;
ALTER TABLE ONLY "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "nu_tipo_documento" TYPE INTEGER, ALTER COLUMN "nu_tipo_documento" SET NOT NULL;


/* Tabela 046 */
---------------
CREATE TABLE "mtrsm001"."mtrtb046_processo_adm" (
                "nu_processo_adm" BIGINT NOT NULL,
                "nu_versao" INTEGER NOT NULL,
                "nu_processo" INTEGER NOT NULL,
                "nu_ano_processo" INTEGER NOT NULL,
                "nu_pregao" INTEGER,
                "nu_unidade_pregao" INTEGER,
                "nu_ano_pregao" INTEGER,
                "de_objeto_contratacao" TEXT NOT NULL,
                "ts_inclusao" TIMESTAMP NOT NULL,
                "co_matricula" VARCHAR(7) NOT NULL,
                "ts_finalizacao" TIMESTAMP,
                "co_matricula_finalizacao" VARCHAR(7),
                CONSTRAINT "pk_mtrtb046" PRIMARY KEY ("nu_processo_adm")
);
COMMENT ON TABLE "mtrsm001"."mtrtb046_processo_adm" IS 'Tabela responsavel pelo armazenamento dos processos administrativo eletronicos formados quando a inicio de um fluxo de contratação.

Com base nesses processos podem ser criados registros de contratos ou apensos de penalidade baseados no andamento do processo.

O registro do processo é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_processo_adm" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_versao" IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_processo" IS 'Atributo utilizado para armazenar o numero de identificação negocial do processo

Juntamente com o "nu_ano_processo", forma-se o numero de identificação negocial do processo.

Ex: 894-2017';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_ano_processo" IS 'Atributo utilizado para armazenar o ano de identificação negocial do processo.

Juntamente com o "nu_processo", forma-se o numero de identificação negocial do processo.

Ex: 894-2017';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_pregao" IS 'Atributo utilizado para armazenar o numero de identificação negocial do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_ano_pregao" e "nu_unidade_pregao", forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_unidade_pregao" IS 'Atributo utilizado para armazenar a unidade CAIXA responsável pela execução do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_pregao" e "nu_ano_pregao, forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."nu_ano_pregao" IS 'Atributo utilizado para armazenar o ano de identificação negocial do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_pregao" e "nu_unidade_pregao", forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."de_objeto_contratacao" IS 'Atributo utilizado para armazenar uma descrição livre sobre o objeto de contratação relativo ao processo administrativo.

Em caso de integração com o SICLG, essa informação deve ser carregada deste sistema que armazena a identificação deste objeto.';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."ts_inclusao" IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do processo administrativo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."co_matricula" IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do processo administrativo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."ts_finalizacao" IS 'Atributo utilizado para armazenar a data/hora de finalização do registro do processo administrativo.

Após o registro ser finalizado, não deverá mais ser possivel realizar carga de documentos relacionados ao processo administrativo, incluido os contratos e apensos vinculados ao mesmo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb046_processo_adm"."co_matricula_finalizacao" IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela finalização do registro do processo administrativo.

Após o registro ser finalizado, não deverá mais ser possivel realizar carga de documentos relacionados ao processo administrativo, incluido os contratos e apensos vinculados ao mesmo.';

/* Tabela 047 */
---------------
CREATE TABLE "mtrsm001"."mtrtb047_contrato_adm" (
                "nu_contrato_adm" BIGINT NOT NULL,
                "nu_versao" INTEGER NOT NULL,
                "nu_processo_adm" BIGINT NOT NULL,
                "nu_contrato" INTEGER NOT NULL,
                "nu_ano_contrato" INTEGER NOT NULL,
                "de_contrato" TEXT NOT NULL,
                "ts_inclusao" TIMESTAMP NOT NULL,
                "co_matricula" VARCHAR(7) NOT NULL,
                "nu_cpf_cnpj_fornecedor" VARCHAR(14) NOT NULL,
                CONSTRAINT "pk_mtrtb047" PRIMARY KEY ("nu_contrato_adm")
);
COMMENT ON TABLE "mtrsm001"."mtrtb047_contrato_adm" IS 'Tabela responsavel pelo armazenamento dos contratos eletronicos formados durante um fluxo de contratação.

Os contratos são relacionados com um processo e podem ser criados registros de apensos de penalidade ou ressarcimento baseados no andamento do contrato.

O registro do contrato é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."nu_contrato_adm" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."nu_versao" IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."nu_processo_adm" IS 'Atributo que representa o processo administrativo de vinculação do contrato.';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."nu_contrato" IS 'Atributo utilizado para armazenar o numero de identificação negocial do contrato

Juntamente com o "nu_ano_contrato", forma-se o numero de identificação negocial do contrato.

Ex: 105/2018';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."nu_ano_contrato" IS 'Atributo utilizado para armazenar o ano de identificação negocial do contrato

Juntamente com o "nu_contrato", forma-se o numero de identificação negocial do contrato.

Ex: 105/2018';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."de_contrato" IS 'Atributo utilizado para armazenar uma descrição livre sobre o contrato';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."ts_inclusao" IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do contrato administrativo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."co_matricula" IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do contrato administrativo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb047_contrato_adm"."nu_cpf_cnpj_fornecedor" IS 'Atributo utilizado para identificar o CPF/CNPJ do fornecedor vinculado ao contrato.

O atributo esta armazenado como texto para que seja armazenado o valor do atributo para os casos de CPF com 11 posições e nos casos de CNPJ com 14 posições';


/* Tabela 048 */
---------------
CREATE TABLE "mtrsm001"."mtrtb048_apenso_adm" (
                "nu_apenso_adm" BIGINT NOT NULL,
                "nu_versao" INTEGER NOT NULL,
                "nu_processo_adm" BIGINT,
                "nu_contrato_adm" BIGINT,
                "nu_cpf_cnpj_fornecedor" VARCHAR(14),
                "ic_tipo_apenso" VARCHAR(2) NOT NULL,
                "de_apenso" TEXT,
                "ts_inclusao" TIMESTAMP NOT NULL,
                "co_matricula" VARCHAR(7) NOT NULL,
                "co_protocolo_siclg" VARCHAR(255) NOT NULL,
                CONSTRAINT "pk_mtrtb048" PRIMARY KEY ("nu_apenso_adm")
);
COMMENT ON TABLE "mtrsm001"."mtrtb048_apenso_adm" IS 'Tabela responsavel pelo armazenamento dos apensos eletronicos formados durante um fluxo de contratação.

Os apensos são relacionados com um processo, neste caso representam as penalidade de processo, ou podem ser vinculados aos contratos, neste caso representam apensos de penalidade ou ressarcimento.

O registro do apenso é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."nu_apenso_adm" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."nu_versao" IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."nu_processo_adm" IS 'Atributo que representa o processo administrativo vinculado ao apenso.

Quando este atributo esta preenchido o atributo que representa a vinculação com o contrato deve estar nulo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."nu_contrato_adm" IS 'Atributo que representa o contrato administrativo vinculado ao apenso.

Quando este atributo esta preenchido o atributo que representa a vinculação com o processo deve estar nulo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."nu_cpf_cnpj_fornecedor" IS 'Atributo utilizado para identificar o CPF/CNPJ do fornecedor vinculado ao apenso.

Este atributo deve ser preenchido apenas quando o apenso esta vinculado ao processo, pois quando vinculado a algum contrato, o CPF/CNPJ relativo ao contrato já esta identificado no registro do mesmo.

Para os casos de processo, não existe uma definição da pessoa relacionada pois processo pode ainda estar na fase de identificação e validação dos participanetes, mas em alguns casos torna-se necessario aplicar uma penalidade a algum desses participantes.

O atributo esta armazenado como texto para que seja armazenado o valor do atributo para os casos de CPF com 11 posições e nos casos de CNPJ com 14 posições';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."ic_tipo_apenso" IS 'Atributo utilizado para identificar o tipo de apenso sob o seguinte dominio:

RC - Ressarcimento de Contrato
PC - Penalidade de Contrato
PP - Penalidade de Processo

Este atributo só deve ser identificado como PP quando a chave estrangeiro da relação com o processo administrativo esta preenchida. Para os casos em que o apenso esteja relacionado com o contrato, este atributo deverá possuir os demais valores.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."de_apenso" IS 'Atributo utilizado para armazenar uma descrição livre do apenso.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."ts_inclusao" IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do apenso administrativo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."co_matricula" IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do apenso administrativo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb048_apenso_adm"."co_protocolo_siclg" IS 'Atributo utilizado para armanezar o numero de protocolo atribuido pelo SICLG no momento da criação do fluxo do apenso.

Esta informação servirá como identificação negocial do registro do apenso.';

/* Tabela 049 */
---------------
CREATE TABLE "mtrsm001"."mtrtb049_documento_adm" (
                "nu_documento_adm" BIGINT NOT NULL,
                "nu_versao" INTEGER NOT NULL,
                "nu_documento" BIGINT NOT NULL,
                "nu_documento_substituto" BIGINT,
                "nu_processo_adm" BIGINT,
                "nu_contrato_adm" BIGINT,
                "nu_apenso_adm" BIGINT,
                "ic_valido" BOOLEAN NOT NULL,
                "ic_confidencial" BOOLEAN NOT NULL,
                CONSTRAINT "pk_mtrtb049" PRIMARY KEY ("nu_documento_adm")
);
COMMENT ON TABLE "mtrsm001"."mtrtb049_documento_adm" IS 'Tabela responsavel pelo armazenamento dos metadados dos documentos relacionados ao processo administrativo eletronico.

Esses documentos podem estar associados a um processo, contrato ou apenso.

Nesta tabela serão armazenadas as indicações de documento valido, confidencial e a indicação de qual documento de substituição do original invalidando o mesmo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_documento_adm" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_versao" IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_documento" IS 'Atributo utilizado para relacionar o documento que foi carregado na estrutura do ECM com a estrutura relacionada a um processo, contrato ou apenso';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_documento_substituto" IS 'Atributo utilizado para relacionar o documento que invalidou o documento original representado no atributo "nu_documento" de forma a substitui-lo';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_processo_adm" IS 'Atributo que representa o processo administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o contrato e o apenso deverão estar com os valores nulos.';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_contrato_adm" IS 'Atributo que representa o contrato administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o processo e o apenso deverão estar com os valores nulos.';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."nu_apenso_adm" IS 'Atributo que representa o apenso administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o processo e o contrato deverão estar com os valores nulos.';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."ic_valido" IS 'Atributo utilizdo para indicar que o documento esta valido e não foi substituido por nenhum outro documento.';
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."ic_confidencial" IS 'Atributo utilizado para identificar que o documento tem cunho confidencial.

Nestes casos, só deverá ser possivel realizar o download ou visualização do documento usuários que possuam perfil especifico para este fim.';

/* Tabela 100 */
---------------
ALTER TABLE "mtrsm001"."mtrtb100_autorizacao" ADD COLUMN "sg_canal_solicitacao" VARCHAR(10) NOT NULL DEFAULT 'SIPAN';
ALTER TABLE "mtrsm001"."mtrtb100_autorizacao" ALTER COLUMN "sg_canal_solicitacao" DROP DEFAULT;
COMMENT ON TABLE "mtrsm001"."mtrtb100_autorizacao" IS 'Tabela utilizada para armazenar as autorizações relacionadas ao nível documental geradas e entregues para os clientes.';
COMMENT ON COLUMN "mtrsm001"."mtrtb100_autorizacao"."sg_canal_solicitacao" IS 'Sigla de identificação do canal/sistema solicitante da autorização.
Informação é obtida pela comparação do codigo de integração enviado com o registro da tabela 006';


/* Tabela 102 */
---------------
ALTER TABLE "mtrsm001"."mtrtb102_autorizacao_negada" DROP CONSTRAINT pk_mtrtb102_autorizacao_negada;
ALTER TABLE "mtrsm001"."mtrtb102_autorizacao_negada" RENAME "nu_autorizacao"  TO "nu_autorizacao_negada";
ALTER TABLE "mtrsm001"."mtrtb102_autorizacao_negada" ADD CONSTRAINT pk_mtrtb102 PRIMARY KEY (nu_autorizacao_negada);
ALTER TABLE "mtrsm001"."mtrtb102_autorizacao_negada" ALTER COLUMN "co_sistema_solicitante" TYPE VARCHAR(10), ALTER COLUMN "co_sistema_solicitante" SET NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb102_autorizacao_negada" RENAME "co_sistema_solicitante"  TO "sg_canal_solicitacao";
COMMENT ON COLUMN "mtrsm001"."mtrtb102_autorizacao_negada"."sg_canal_solicitacao" IS 'Sigla de identificação do canal/sistema solicitante da autorização.
Informação é obtida pela comparação do codigo de integração enviado com o registro da tabela 006';

/* Tabela 103 */
---------------
CREATE TABLE "mtrsm001"."mtrtb103_autorizacao_orientacao" (
                "nu_autorizacao_orientacao" BIGINT NOT NULL,
                "nu_versao" INTEGER NOT NULL,
                "nu_autorizacao" BIGINT NOT NULL,
                "ic_sistema" VARCHAR(10) NOT NULL,
                "de_orientacao" TEXT NOT NULL,
                CONSTRAINT "pk_mtrtb103" PRIMARY KEY ("nu_autorizacao_orientacao")
);
COMMENT ON TABLE "mtrsm001"."mtrtb103_autorizacao_orientacao" IS 'Tabela utilizada para armazenar as orientações encaminhadas conforme comportamentos de pesquisa definido ao momento da autorização de forma a permitir recuperar todo a informação referente a autorização fornecida.';
COMMENT ON COLUMN "mtrsm001"."mtrtb103_autorizacao_orientacao"."nu_autorizacao_orientacao" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm001"."mtrtb103_autorizacao_orientacao"."nu_versao" IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN "mtrsm001"."mtrtb103_autorizacao_orientacao"."nu_autorizacao" IS 'Atributo utilizado para vincular a orientação a autorização concedida.
Quando este atributo estiver definido, o atributo nu_autorizacao_negada deverá ser nulo.';
COMMENT ON COLUMN "mtrsm001"."mtrtb103_autorizacao_orientacao"."ic_sistema" IS 'Atributo utilizado para identificar o sistema de realização da pesquisa cadastral que originou a orientação a epoca do fornecimento da autorização.';
COMMENT ON COLUMN "mtrsm001"."mtrtb103_autorizacao_orientacao"."de_orientacao" IS 'Atributo utilizado para armazenar a orientação concedida a época do fornecimento da autorização.';

/* Tabela 200 */
---------------
ALTER TABLE "mtrsm001"."mtrtb200_sicli_erro" ADD COLUMN "nu_versao" INTEGER NOT NULL DEFAULT 1;
ALTER TABLE "mtrsm001"."mtrtb200_sicli_erro" ALTER COLUMN "nu_versao" DROP DEFAULT;
COMMENT ON TABLE "mtrsm001"."mtrtb200_sicli_erro" IS 'Tabela utilizada para armazenar os erros de comunicação e resposta do SECLI';

/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE "mtrsm001"."mtrtb004_dossie_cliente_produto" ADD CONSTRAINT "fk_mtrtb004_mtrtb001_02"
FOREIGN KEY ("nu_dossie_cliente_relacionado")
REFERENCES "mtrsm001"."mtrtb001_dossie_cliente" ("nu_dossie_cliente")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb021_unidade_autorizada" ADD CONSTRAINT "fk_mtrtb021_mtrtb046"
FOREIGN KEY ("nu_processo_adm")
REFERENCES "mtrsm001"."mtrtb046_processo_adm" ("nu_processo_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb021_unidade_autorizada" ADD CONSTRAINT "fk_mtrtb021_mtrtb047"
FOREIGN KEY ("nu_contrato_adm")
REFERENCES "mtrsm001"."mtrtb047_contrato_adm" ("nu_contrato_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb021_unidade_autorizada" ADD CONSTRAINT "fk_mtrtb021_mtrtb048"
FOREIGN KEY ("nu_apenso_adm")
REFERENCES "mtrsm001"."mtrtb048_apenso_adm" ("nu_apenso_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb029_campo_apresentacao" ADD CONSTRAINT "fk_mtrtb029_mtrtb019"
FOREIGN KEY ("nu_campo_formulario")
REFERENCES "mtrsm001"."mtrtb019_campo_formulario" ("nu_campo_formulario")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb030_resposta_dossie" ADD CONSTRAINT "fk_mtrtb030_mtrtb019"
FOREIGN KEY ("nu_campo_formulario")
REFERENCES "mtrsm001"."mtrtb019_campo_formulario" ("nu_campo_formulario")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb043_documento_garantia" ADD CONSTRAINT "fk_mtrtb043_mtrtb010"
FOREIGN KEY ("nu_funcao_documental")
REFERENCES "mtrsm001"."mtrtb010_funcao_documental" ("nu_funcao_documental")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb047_contrato_adm" ADD CONSTRAINT "fk_mtrtb047_mtrtb046"
FOREIGN KEY ("nu_processo_adm")
REFERENCES "mtrsm001"."mtrtb046_processo_adm" ("nu_processo_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb048_apenso_adm" ADD CONSTRAINT "fk_mtrtb048_mtrtb046"
FOREIGN KEY ("nu_processo_adm")
REFERENCES "mtrsm001"."mtrtb046_processo_adm" ("nu_processo_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb048_apenso_adm" ADD CONSTRAINT "fk_mtrtb048_mtrtb047"
FOREIGN KEY ("nu_contrato_adm")
REFERENCES "mtrsm001"."mtrtb047_contrato_adm" ("nu_contrato_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb049_documento_adm" ADD CONSTRAINT "fk_mtrtb049_mtrtb003"
FOREIGN KEY ("nu_documento")
REFERENCES "mtrsm001"."mtrtb003_documento" ("nu_documento")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb049_documento_adm" ADD CONSTRAINT "fk_mtrtb049_mtrtb046"
FOREIGN KEY ("nu_processo_adm")
REFERENCES "mtrsm001"."mtrtb046_processo_adm" ("nu_processo_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb049_documento_adm" ADD CONSTRAINT "fk_mtrtb049_mtrtb047"
FOREIGN KEY ("nu_contrato_adm")
REFERENCES "mtrsm001"."mtrtb047_contrato_adm" ("nu_contrato_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb049_documento_adm" ADD CONSTRAINT "fk_mtrtb049_mtrtb048"
FOREIGN KEY ("nu_apenso_adm")
REFERENCES "mtrsm001"."mtrtb048_apenso_adm" ("nu_apenso_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb049_documento_adm" ADD CONSTRAINT "fk_mtrtb049_mtrtb049"
FOREIGN KEY ("nu_documento_substituto")
REFERENCES "mtrsm001"."mtrtb049_documento_adm" ("nu_documento_adm")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

ALTER TABLE "mtrsm001"."mtrtb103_autorizacao_orientacao" ADD CONSTRAINT "fk_mtrtb103_mtrtb100"
FOREIGN KEY ("nu_autorizacao")
REFERENCES "mtrsm001"."mtrtb100_autorizacao" ("nu_autorizacao")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;
--------------------------------------------------------------------------------------------------------------

/* INDICES DE UNICIDADE*/
------------------------
CREATE UNIQUE INDEX ix_mtrtb006_01 ON "mtrsm001"."mtrtb006_canal" USING BTREE ( "co_integracao" );
CREATE UNIQUE INDEX ix_mtrtb019_01 ON "mtrsm001"."mtrtb019_campo_formulario" USING BTREE ( "nu_processo", "nu_ordem" );
CREATE UNIQUE INDEX ix_mtrtb026_01 ON "mtrsm001"."mtrtb026_relacao_processo" USING BTREE ( "nu_processo_pai", "nu_ordem" );
CREATE UNIQUE INDEX ix_mtrtb043_01 ON "mtrsm001"."mtrtb043_documento_garantia" USING BTREE ( "nu_garantia", "nu_processo" );
CREATE UNIQUE INDEX ix_mtrtb044_01 ON "mtrsm001"."mtrtb044_comportamento_pesquisa" USING BTREE ( "nu_produto", "ic_sistema_retorno", "ic_codigo_retorno" );
CREATE UNIQUE INDEX ix_mtrtb046_01 on "mtrsm001"."mtrtb046_processo_adm" USING BTREE ("nu_processo", "nu_ano_processo");
CREATE UNIQUE INDEX ix_mtrtb046_02 on "mtrsm001"."mtrtb046_processo_adm" USING BTREE ("nu_pregao", "nu_unidade_pregao", "nu_ano_pregao");
CREATE UNIQUE INDEX ix_mtrtb047_01 on "mtrsm001"."mtrtb047_contrato_adm" USING BTREE ("nu_contrato", "nu_ano_contrato");
CREATE UNIQUE INDEX ix_mtrtb048_01 on "mtrsm001"."mtrtb048_apenso_adm" USING BTREE ("co_protocolo_siclg");


/* Adequação de Sequences - Parte 02 de 02 */
--------------------------------------------
ALTER TABLE mtrsm001.mtrtb001_dossie_cliente 		ALTER COLUMN nu_dossie_cliente 		 	SET DEFAULT nextval('mtrsm001.mtrsq001_dossie_cliente'::regclass);
ALTER TABLE mtrsm001.mtrtb002_dossie_produto 		ALTER COLUMN nu_dossie_produto 		 	SET DEFAULT nextval('mtrsm001.mtrsq002_dossie_produto'::regclass);
ALTER TABLE mtrsm001.mtrtb003_documento 		ALTER COLUMN nu_documento 			SET DEFAULT nextval('mtrsm001.mtrsq003_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb004_dossie_cliente_produto	ALTER COLUMN nu_dossie_cliente_produto 		SET DEFAULT nextval('mtrsm001.mtrsq004_dossie_cliente_produto'::regclass);
ALTER TABLE mtrsm001.mtrtb006_canal 			ALTER COLUMN nu_canal 				SET DEFAULT nextval('mtrsm001.mtrsq006_canal'::regclass);
ALTER TABLE mtrsm001.mtrtb007_atributo_documento 	ALTER COLUMN nu_atributo_documento 	 	SET DEFAULT nextval('mtrsm001.mtrsq007_atributo_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb008_conteudo 			ALTER COLUMN nu_conteudo 			SET DEFAULT nextval('mtrsm001.mtrsq008_conteudo'::regclass);
ALTER TABLE mtrsm001.mtrtb009_tipo_documento 		ALTER COLUMN nu_tipo_documento 		 	SET DEFAULT nextval('mtrsm001.mtrsq009_tipo_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb010_funcao_documental 	ALTER COLUMN nu_funcao_documental 	 	SET DEFAULT nextval('mtrsm001.mtrsq010_funcao_documental'::regclass);
ALTER TABLE mtrsm001.mtrtb012_tipo_situacao_dossie 	ALTER COLUMN nu_tipo_situacao_dossie 		SET DEFAULT nextval('mtrsm001.mtrsq012_tipo_situacao_dossie'::regclass);
ALTER TABLE mtrsm001.mtrtb013_situacao_dossie 		ALTER COLUMN nu_situacao_dossie 		SET DEFAULT nextval('mtrsm001.mtrsq013_situacao_dossie'::regclass);
ALTER TABLE mtrsm001.mtrtb014_instancia_documento 	ALTER COLUMN nu_instancia_documento 		SET DEFAULT nextval('mtrsm001.mtrsq014_instancia_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb015_situacao_documento 	ALTER COLUMN nu_situacao_documento 	 	SET DEFAULT nextval('mtrsm001.mtrsq015_situacao_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb016_motivo_stco_documento 	ALTER COLUMN nu_motivo_stco_documento  		SET DEFAULT nextval('mtrsm001.mtrsq016_motivo_stco_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb017_stco_instnca_documento 	ALTER COLUMN nu_stco_instnca_documento 		SET DEFAULT nextval('mtrsm001.mtrsq017_situacao_instnca_dcmnto'::regclass);
ALTER TABLE mtrsm001.mtrtb019_campo_formulario 		ALTER COLUMN nu_campo_formulario 		SET DEFAULT nextval('mtrsm001.mtrsq019_campo_formulario'::regclass);
ALTER TABLE mtrsm001.mtrtb020_processo 			ALTER COLUMN nu_processo 			SET DEFAULT nextval('mtrsm001.mtrsq020_processo'::regclass);
ALTER TABLE mtrsm001.mtrtb021_unidade_autorizada 	ALTER COLUMN nu_unidade_autorizada 	 	SET DEFAULT nextval('mtrsm001.mtrsq021_unidade_autorizada'::regclass);
ALTER TABLE mtrsm001.mtrtb022_produto 			ALTER COLUMN nu_produto 			SET DEFAULT nextval('mtrsm001.mtrsq022_produto'::regclass);
ALTER TABLE mtrsm001.mtrtb024_produto_dossie 		ALTER COLUMN nu_produto_dossie 			SET DEFAULT nextval('mtrsm001.mtrsq024_produto_dossie'::regclass);
ALTER TABLE mtrsm001.mtrtb025_processo_documento 	ALTER COLUMN nu_processo_documento 	 	SET DEFAULT nextval('mtrsm001.mtrsq025_processo_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb027_campo_entrada 		ALTER COLUMN nu_campo_entrada 		 	SET DEFAULT nextval('mtrsm001.mtrsq027_campo_entrada'::regclass);
ALTER TABLE mtrsm001.mtrtb028_opcao_campo 		ALTER COLUMN nu_opcao_campo 			SET DEFAULT nextval('mtrsm001.mtrsq028_opcao_campo'::regclass);
ALTER TABLE mtrsm001.mtrtb029_campo_apresentacao 	ALTER COLUMN nu_campo_apresentacao 		SET DEFAULT nextval('mtrsm001.mtrsq029_campo_apresentacao'::regclass);
ALTER TABLE mtrsm001.mtrtb030_resposta_dossie 		ALTER COLUMN nu_resposta_dossie 		SET DEFAULT nextval('mtrsm001.mtrsq030_resposta_dossie'::regclass);
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo 	ALTER COLUMN nu_elemento_conteudo 		SET DEFAULT nextval('mtrsm001.mtrsq032_elemento_conteudo'::regclass);
ALTER TABLE mtrsm001.mtrtb033_garantia 			ALTER COLUMN nu_garantia 			SET DEFAULT nextval('mtrsm001.mtrsq033_garantia'::regclass);
ALTER TABLE mtrsm001.mtrtb035_garantia_informada 	ALTER COLUMN nu_garantia_informada 		SET DEFAULT nextval('mtrsm001.mtrsq035_garantia_informada'::regclass);
ALTER TABLE mtrsm001.mtrtb036_composicao_documental 	ALTER COLUMN nu_composicao_documental  		SET DEFAULT nextval('mtrsm001.mtrsq036_composicao_documental'::regclass);
ALTER TABLE mtrsm001.mtrtb037_regra_documental 		ALTER COLUMN nu_regra_documental 		SET DEFAULT nextval('mtrsm001.mtrsq037_regra_documental'::regclass);
ALTER TABLE mtrsm001.mtrtb043_documento_garantia 	ALTER COLUMN nu_documento_garantia 		SET DEFAULT nextval('mtrsm001.mtrsq043_documento_garantia'::regclass);
ALTER TABLE mtrsm001.mtrtb044_comportamento_pesquisa 	ALTER COLUMN nu_comportamento_pesquisa 		SET DEFAULT nextval('mtrsm001.mtrsq044_comportamento_pesquisa'::regclass);
ALTER TABLE mtrsm001.mtrtb045_atributo_extracao 	ALTER COLUMN nu_atributo_extracao 	 	SET DEFAULT nextval('mtrsm001.mtrsq045_atributo_extracao'::regclass);
ALTER TABLE mtrsm001.mtrtb046_processo_adm 		ALTER COLUMN nu_processo_adm 			SET DEFAULT nextval('mtrsm001.mtrsq046_processo_adm'::regclass);
ALTER TABLE mtrsm001.mtrtb047_contrato_adm 		ALTER COLUMN nu_contrato_adm 			SET DEFAULT nextval('mtrsm001.mtrsq047_contrato_adm'::regclass);
ALTER TABLE mtrsm001.mtrtb048_apenso_adm 		ALTER COLUMN nu_apenso_adm 			SET DEFAULT nextval('mtrsm001.mtrsq048_apenso_adm'::regclass);
ALTER TABLE mtrsm001.mtrtb049_documento_adm 		ALTER COLUMN nu_documento_adm 			SET DEFAULT nextval('mtrsm001.mtrsq049_documento_adm'::regclass);
ALTER TABLE mtrsm001.mtrtb100_autorizacao 		ALTER COLUMN nu_autorizacao 			SET DEFAULT nextval('mtrsm001.mtrsq100_autorizacao'::regclass);
ALTER TABLE mtrsm001.mtrtb101_documento 		ALTER COLUMN nu_documento 			SET DEFAULT nextval('mtrsm001.mtrsq101_documento'::regclass);
ALTER TABLE mtrsm001.mtrtb102_autorizacao_negada 	ALTER COLUMN nu_autorizacao_negada 		SET DEFAULT nextval('mtrsm001.mtrsq102_autorizacao_negada'::regclass);
ALTER TABLE mtrsm001.mtrtb103_autorizacao_orientacao 	ALTER COLUMN nu_autorizacao_orientacao 		SET DEFAULT nextval('mtrsm001.mtrsq103_autorizacao_orientacao'::regclass);
ALTER TABLE mtrsm001.mtrtb200_sicli_erro 		ALTER COLUMN nu_sicli_erro 			SET DEFAULT nextval('mtrsm001.mtrsq200_sicli_erro'::regclass);

drop sequence mtrsm001.MTRSQ005_DOSSIE_CLIENTE_PRODUTO;
drop sequence mtrsm001.MTRSQ017_STCO_INSTNCA_DOCUMENTO;
drop sequence mtrsm001.MTRSQ032_ELEMENTO_DOSSIE;


/* Ajuste/Reinclusão de registros excluidos para adequação da tabela */
---------------------------------------------------------------
INSERT INTO mtrsm001.mtrtb043_documento_garantia(nu_versao, nu_garantia, nu_processo, nu_tipo_documento, nu_funcao_documental) VALUES (1, 63, 3, 8, null);
INSERT INTO mtrsm001.mtrtb043_documento_garantia(nu_versao, nu_garantia, nu_processo, nu_tipo_documento, nu_funcao_documental) VALUES (1, 66, 3, 8, null);
UPDATE mtrsm001.mtrtb006_canal SET sg_canal = 'SIMTRWEB' WHERE sg_canal = 'SIMTR';