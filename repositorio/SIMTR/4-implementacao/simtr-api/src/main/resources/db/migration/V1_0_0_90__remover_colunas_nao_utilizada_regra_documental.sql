/* Tabela mtrtb037_regra_documental */
---------------
DO $$
BEGIN
    ALTER TABLE mtr.mtrtb037_regra_documental DROP COLUMN IF EXISTS nu_canal_captura;
    ALTER TABLE mtr.mtrtb037_regra_documental DROP COLUMN IF EXISTS ix_antifraude;
END $$; 
