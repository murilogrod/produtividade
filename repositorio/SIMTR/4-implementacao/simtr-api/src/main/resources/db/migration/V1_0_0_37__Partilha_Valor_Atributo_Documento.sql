/* Tabela 045 */
---------------
ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_modo_partilha VARCHAR(1) NULL;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_modo_partilha IS 'Atributo utilizado para indicar a forma de partilha da informação de um atributo quando comparado com o campo do SICLI que representa o nome da mãe indicando qual parte da informação deve ser enviada para outro atributo, podendo assumir o seguinte dominio:

- S - Sobra
- L - Localizado';

ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_atributo_partilha INT4 NULL;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_modo_partilha IS 'Representa a relação com o atributo que deve ser utilizado na partilha da informação quando comparado ao nome da mãe do SICPF na forma indicada no atributo ic_modo_partilha';


/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE mtr.mtrtb045_atributo_extracao ADD CONSTRAINT fk_mtrtb045_mtrtb045_01
FOREIGN KEY (nu_atributo_partilha)
REFERENCES mtr.mtrtb045_atributo_extracao (nu_atributo_extracao)
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

/* CORRECAO DE NOMES DE CHAVES ESTRANGEIRAS*/
--------------------------------------------
ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME CONSTRAINT fk_mtrtb045_fk_mtrtb0_mtrtb009 TO fk_mtrtb045_mtrtb009_01