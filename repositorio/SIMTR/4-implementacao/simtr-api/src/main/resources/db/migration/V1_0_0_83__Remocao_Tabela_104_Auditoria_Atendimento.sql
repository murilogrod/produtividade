DO $$
BEGIN

	
/* Tabela 104 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb104_auditoria_atendimento'
) THEN 
    DROP TABLE mtr.mtrtb104_auditoria_atendimento;
END IF;

END $$;