DO $$
BEGIN

/* Tabela 006 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'ts_ultima_alteracao'
) THEN 
    ALTER TABLE mtr.mtrtb051_checklist DROP COLUMN ts_inativacao TIMESTAMP;
    ALTER TABLE mtr.mtrtb051_checklist DROP COLUMN ts_ultima_alteracao;
END IF;
END $$;