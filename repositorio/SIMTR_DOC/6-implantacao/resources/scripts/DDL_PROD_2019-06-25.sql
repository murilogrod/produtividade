-- Execução do flyway #54.


-- INICIO FLYWAY #54
/* Tabela 001 */
---------------
ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN ts_apuracao_nivel TIMESTAMP;
COMMENT ON COLUMN mtr.mtrtb001_dossie_cliente.ts_apuracao_nivel IS 'Atributo que representa a data/hora de apuração do nivel documental do cliente';

/* Tabela 003 */
---------------
ALTER TABLE mtr.mtrtb003_documento ADD COLUMN ts_cadastro_cliente TIMESTAMP;
COMMENT ON COLUMN mtr.mtrtb003_documento.ts_cadastro_cliente IS 'Atributo utilizado para indicar a data/hora de atualização do cliente junto ao sistema corporativo de cadastro da CAIXA (SICLI).';

ALTER TABLE mtr.mtrtb003_documento ADD COLUMN qt_bytes INT8 NOT NULL DEFAULT 0;
ALTER TABLE mtr.mtrtb003_documento ALTER COLUMN qt_bytes DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb003_documento.qt_bytes IS 'Atributo utilizado para armazenar o tamanho do documento em bytes.';

ALTER TABLE mtr.mtrtb003_documento ADD COLUMN qt_conteudos INT4 NOT NULL DEFAULT 0;
ALTER TABLE mtr.mtrtb003_documento ALTER COLUMN qt_conteudos DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb003_documento.qt_conteudos IS 'Atributo utilizado para armazenar o a quantidade de conteudos que compoem o documento. Necessario pois as situações que o conteudo fica armazenado em sistema externo como o GED não se possui a quantidade de conteudos de forma direta.';

/* Tabela 009 */
---------------
ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_multiplos BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_multiplos DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_multiplos IS 'Atributo utilizado para indicar se o documento vinculado a esta tipologia pode ser armazenado em mais de um registro. Caso negativo, a API deverá "invalidar" para noos negócios,  os registros de documentos capturados anteriormente, ainda validos vinculados a mesma tipologia.';


/* Tabela 045 */
---------------
ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_estrategia_partilha VARCHAR(30);
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_estrategia_partilha IS 'Atributo utilizado para indicar como a partilha do campo deverá ser feita, quando for o caso.
Ex: RECEITA_MAE signfica que a informalção do campo deve ser analisada baseado no nome da mãe obtido junto ao cadastro da receita federal.';

ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_presente_documento BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_presente_documento DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_presente_documento IS 'Atributo utilizado para indicar se a informação pode ser encontrada no documento ou trata-se de um metadado necessario para disponibilização da informação em algum sistema de integração.
Esse dados muitas vezes pode ser obtido a partir da interação com o cliente quando possivel.
Ex: O tipo de documento conta de luz, pode ter uma informação definida como indicador_correspondencia para ser utilizada no envio de dados do endereço para o sistema de cadastro de clientes, mas não esta presente no documento.';

ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN vr_padrao VARCHAR(30);
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.vr_padrao IS 'Atributo utilizado para manter opcionalmente um valor padrão a ser utilizado no envio das informação para as integrações quando não informado ou não capturado no documento.';

--Ajustes de comentario
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_modo_partilha IS 'Atributo utilizado para indicar a forma de partilha da informação de um atributo indicando qual parte da informação deve ser enviada para outro atributo, podendo assumir o seguinte dominio:

- S - Sobra
- L - Localizado';

ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN de_orientacao VARCHAR(255);
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.de_orientacao IS 'Atributo utilizado para indicar uma orientação a ser utilizada para orientar o usuário sobre a forma adequada de preencher o campo no caso de montagem dinâmica do formulário para captura/extração dos dados.';
-- FIM FLYWAY #54