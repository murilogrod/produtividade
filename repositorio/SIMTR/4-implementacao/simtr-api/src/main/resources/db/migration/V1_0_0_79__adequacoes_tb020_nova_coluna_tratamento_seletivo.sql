DO $$ 
BEGIN 
/* Tabela 020 */ 
--------------- 
IF NOT EXISTS( 
SELECT * FROM information_schema.columns 
WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'ic_tratamento_seletivo' 
) THEN 
ALTER TABLE mtr.mtrtb020_processo ADD COLUMN ic_tratamento_seletivo BOOLEAN NOT NULL DEFAULT FALSE; 
COMMENT ON COLUMN mtr.mtrtb020_processo.ic_tratamento_seletivo IS 'Atributo que indica se o dossiê de produto pode ser capturado para tratamento de forma específica, desde que o mesmo esteja na situação "AGUARDANDO TRATAMENTO" e o solicitante possua o perfil específico para realização de tal ação.'; 
ALTER TABLE mtr.mtrtb020_processo ALTER COLUMN ic_tratamento_seletivo DROP DEFAULT; 
END IF; 
END $$; 