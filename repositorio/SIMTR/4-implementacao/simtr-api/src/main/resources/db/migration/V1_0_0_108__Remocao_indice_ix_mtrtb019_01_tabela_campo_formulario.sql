DO $$
BEGIN

IF EXISTS(
    SELECT * FROM information_schema.table_constraints
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND CONSTRAINT_NAME = 'ix_mtrtb019_01'
) THEN
	ALTER TABLE mtr.mtrtb019_campo_formulario DROP CONSTRAINT ix_mtrtb019_01;
END IF;

DROP INDEX IF EXISTS mtr.ix_mtrtb019_01;

END $$;