/* Tabela 007 */
---------------
ALTER TABLE mtr.mtrtb007_atributo_documento ADD COLUMN de_opcoes_selecionadas TEXT;
COMMENT ON COLUMN mtr.mtrtb007_atributo_documento.de_opcoes_selecionadas IS 'Atributo utilizado para armazenar as opções selecionadas no caso de atributos multi seleção. Deve ser armazenado s lista de valores selecionados separados por ";" (ponto e vigula).';

/* Tabela 052 */
---------------
ALTER TABLE mtr.mtrtb052_apontamento ADD COLUMN nu_ordem INT4 NOT NULL DEFAULT 1;
COMMENT ON COLUMN mtr.mtrtb052_apontamento.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos apontamentos no checklist.';
ALTER TABLE mtr.mtrtb052_apontamento ALTER COLUMN nu_ordem DROP DEFAULT;

ALTER TABLE mtr.mtrtb052_apontamento ADD COLUMN co_tecla_atalho VARCHAR(30);
COMMENT ON COLUMN mtr.mtrtb052_apontamento.co_tecla_atalho IS 'Atributo utilizado para indicar a tecla de atalho do teclado que aciona o apontamento. 
Caso a definição de teclas definida seja pressionada na tela de tratamento, os apontamentos correspondentes durante o checklist em vigor deverão ser acionados para o "aprovação" ou "rejeição"';


/* Tabela 057 */
---------------
DROP TABLE IF EXISTS mtr.mtrtb057_atributo_opcao;

CREATE TABLE mtr.mtrtb057_opcao_atributo (
   nu_opcao_atributo    SERIAL               NOT NULL,
   nu_versao            INT4                 NOT NULL,
   nu_atributo_extracao INT4                 NOT NULL,
   no_value             varchar(50)          NOT NULL,
   no_opcao             varchar(255)         NOT NULL,
   ic_ativo             BOOL                 NOT NULL,
   CONSTRAINT pk_mtrtb057_opcao_atributo PRIMARY KEY (nu_opcao_atributo)
);

COMMENT ON TABLE mtr.mtrtb057_opcao_atributo IS
'Tabela responsavel pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- Lista;
- Radio;
- Check;

Registros desta tabela só devem ser excluidos fisicamente caso não exista nenhuma resposta de documento atrelada a este registro.
Caso essa situação ocorra o registro deve ser exluido logicamente definindo seu atributo ativo como false.';

COMMENT ON COLUMN mtr.mtrtb057_opcao_atributo.nu_opcao_atributo IS
'Atributo que representa a chave primaria da entidade.';

COMMENT ON COLUMN mtr.mtrtb057_opcao_atributo.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb057_opcao_atributo.nu_atributo_extracao IS
'Atributo utilizado para indicar qual atributo de extração esta relacionado com as opções';

COMMENT ON COLUMN mtr.mtrtb057_opcao_atributo.no_value IS
'Atributo utilizado para armazenar o valor que será definido como value da opção na interface grafica.';

COMMENT ON COLUMN mtr.mtrtb057_opcao_atributo.no_opcao IS
'Atributo que armazena o valor da opção que será exibida para o usuario no campo do formulario utilizado na interface grafica.';

COMMENT ON COLUMN mtr.mtrtb057_opcao_atributo.ic_ativo IS
'Atributo que indica se a opção do campo de entrada esta apta ou não para ser inserido no campo de entrada do formulario.';

ALTER TABLE mtr.mtrtb057_opcao_atributo
ADD CONSTRAINT fk_mtrtb057_mtrtb045 FOREIGN KEY (nu_atributo_extracao)
REFERENCES mtr.mtrtb045_atributo_extracao (nu_atributo_extracao)
ON DELETE RESTRICT ON UPDATE RESTRICT;
