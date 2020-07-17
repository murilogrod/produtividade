/* Tabela 002 */
---------------
ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN IF EXISTS no_container_bpm;
ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN no_container_bpm VARCHAR(255);
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.no_container_bpm IS 'Atributo que armazena o valor de identificação do container utilizado no agrupamento dos processos junto a solução de BPM que possui o processo originador vinculado.
Ex: bpm-simtr_1.0.0';

ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN IF EXISTS no_processo_bpm;
ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN no_processo_bpm VARCHAR(255);
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.no_processo_bpm IS 'Atributo que armazena o valor de identificação do processo orignador junto a solução de BPM.
Ex: bpm-simtr.ProcessoConformidade';

/* Tabela 016 */
---------------
DROP TABLE IF EXISTS mtr.mtrtb016_subprocesso_bpm;

CREATE TABLE mtr.mtrtb016_subprocesso_bpm (
   nu_dossie_produto            int8 NOT NULL,
   nu_instancia_subprocesso_bpm int8 NOT NULL,
   CONSTRAINT pk_mtrtb016_subprocessos_bpm PRIMARY KEY (nu_dossie_produto, nu_instancia_subprocesso_bpm)
);

COMMENT ON TABLE mtr.mtrtb016_subprocesso_bpm IS
'Tabela responsavel por armazenar o identificador dos subprocessos do BPM que possuem relação com o dossiê de produto, mas foram originados a partir da instancia de processo principal ou de algum de seus subprocessos.';

COMMENT ON COLUMN mtr.mtrtb016_subprocesso_bpm.nu_dossie_produto IS
'Atributo utilizado para vincular o dossiê do produto com o identificados do subprocesso existente no BPM utilizado para encaminhar os sinal a todos os vinculados.';

COMMENT ON COLUMN mtr.mtrtb016_subprocesso_bpm.nu_instancia_subprocesso_bpm IS
'Atributo que indica o código da instancia de subprocesso no BPM vinculada a instancia principal relacionada ao dossiê de produto.';

ALTER TABLE mtr.mtrtb016_subprocesso_bpm
ADD CONSTRAINT fk_mtrtb016_mtrtb002 FOREIGN KEY (nu_dossie_produto)
REFERENCES mtr.mtrtb002_dossie_produto (nu_dossie_produto)
ON DELETE RESTRICT ON UPDATE RESTRICT;
