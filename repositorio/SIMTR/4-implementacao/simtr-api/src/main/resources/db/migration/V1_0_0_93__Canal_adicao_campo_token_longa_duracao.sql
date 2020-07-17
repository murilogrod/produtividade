DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'de_token'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN de_token TEXT;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_siric IS 'Atributo utilizado para armazenar um token de longa duração criado para validar o canal com base em alguns serviços especificos que necessitarão ser públicos do ponto de vista do Keycloak (SSO) sendo utilizado este token a ser enviado para captura das informações e controle de permissão';
END IF;

END $$;