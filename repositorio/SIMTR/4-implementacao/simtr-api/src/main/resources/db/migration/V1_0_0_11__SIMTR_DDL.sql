---FUNCTION mtrsm001.GET_PROXIMA_SITUACAO(INT, INT)---
DROP  FUNCTION IF EXISTS mtrsm001.GET_PROXIMA_SITUACAO(INT, INT) ;

CREATE OR REPLACE FUNCTION mtrsm001.GET_PROXIMA_SITUACAO(situacao_atual INT, nu_dossie_produto INT) RETURNS INT AS $$
BEGIN
	DECLARE no_situacao_atual VARCHAR;
	DECLARE proxima_situacao INT;
	BEGIN
		no_situacao_atual := (SELECT no_tipo_situacao FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE nu_tipo_situacao_dossie=situacao_atual);

		IF(no_situacao_atual='Rascunho') THEN
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Aguardando Tratamento');
		END IF;
		
		IF(no_situacao_atual='Aguardando Tratamento') THEN
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Em Tratamento');
		END IF;

		RETURN 0; -- Retorna zero em casos não tratados.
	END;
END;
$$ LANGUAGE plpgsql;

--------------------------------------------------------------------------------------------------------------------------