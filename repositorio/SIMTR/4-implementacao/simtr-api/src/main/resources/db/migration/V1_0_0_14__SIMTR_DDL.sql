---FUNCTION mtrsm001.GET_PROXIMA_SITUACAO(INT, INT)---
DROP  FUNCTION IF EXISTS mtrsm001.GET_PROXIMA_SITUACAO(INT, INT) ;
DROP  FUNCTION IF EXISTS mtrsm001.GET_PROXIMA_SITUACAO(INT, INT, VARCHAR, INT, BOOLEAN, BOOLEAN) ;

CREATE OR REPLACE FUNCTION mtrsm001.GET_PROXIMA_SITUACAO(situacao_atual INT, nu_dossie_produto INT, usuario VARCHAR, unidade_usuario INT, tem_item_rejeitado BOOLEAN, tem_suspeita_fraude BOOLEAN) RETURNS INT AS $$
BEGIN
	DECLARE no_situacao_atual VARCHAR;
	DECLARE proxima_situacao INT;
	BEGIN
		no_situacao_atual := (SELECT no_tipo_situacao FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE nu_tipo_situacao_dossie=situacao_atual);

		IF(no_situacao_atual='Rascunho') THEN
			-- Insere a situação 'Criado'.
			INSERT INTO mtrsm001.mtrtb013_situacao_dossie(nu_versao, nu_dossie_produto, nu_tipo_situacao_dossie, ts_inclusao, co_matricula, nu_unidade, de_observacao)
			VALUES (1, nu_dossie_produto, (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Criado'), now(), usuario, unidade_usuario, '');
			-- Informa para o sistema realizar a inserção da situação 'Aguardando Tratamento'.
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Aguardando Tratamento');
		END IF;
		
		IF(no_situacao_atual='Aguardando Tratamento') THEN
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Em Tratamento');
		END IF;

		IF(no_situacao_atual='Em Tratamento') THEN
			IF(tem_suspeita_fraude) THEN
				RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Pendente Segurança');
			END IF;
			IF(tem_item_rejeitado) THEN
				RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Pendente Informação');
			END IF;
			-- Se não tem item (formulário/documento) rejeitado e não tem suspeita de fraude:
				-- Insere a situação 'Conforme'.
			INSERT INTO mtrsm001.mtrtb013_situacao_dossie(nu_versao, nu_dossie_produto, nu_tipo_situacao_dossie, ts_inclusao, co_matricula, nu_unidade, de_observacao)
			VALUES (1, nu_dossie_produto, (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Conforme'), now(), usuario, unidade_usuario, '');
				-- E retorna situação 'Finalizado Conforme'.
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Finalizado Conforme');
		END IF;

		IF(no_situacao_atual='Pendente Informação') THEN
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Em Alimentação');
		END IF;

		IF(no_situacao_atual='Em Alimentação') THEN
			-- Insere a situação 'Alimentação Finalizada'.
			INSERT INTO mtrsm001.mtrtb013_situacao_dossie(nu_versao, nu_dossie_produto, nu_tipo_situacao_dossie, ts_inclusao, co_matricula, nu_unidade, de_observacao)
			VALUES (1, nu_dossie_produto, (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Alimentação Finalizada'), now(), usuario, unidade_usuario, '');
			-- Informa para o sistema realizar a inserção da situação 'Aguardando Tratamento'.
			RETURN (SELECT nu_tipo_situacao_dossie FROM mtrsm001.mtrtb012_tipo_situacao_dossie WHERE no_tipo_situacao='Aguardando Tratamento');
		END IF;

		RETURN 0; -- Retorna zero em casos não tratados.
	END;
END;
$$ LANGUAGE plpgsql;