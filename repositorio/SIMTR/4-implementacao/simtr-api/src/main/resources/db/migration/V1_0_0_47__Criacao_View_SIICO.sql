/* 
    Executa a criação da VIEW no esquema "ico", caso exista, para atender a necessidade da CEPTI
    Executa a criação da VIEW no esquema "icosm001", caso exista, para atender a necessidade do PEDES
*/
DO $$
BEGIN
	IF EXISTS(
		SELECT schema_name
		FROM information_schema.schemata
		WHERE schema_name = 'ico'
	)
	THEN
		EXECUTE '
			CREATE OR REPLACE VIEW mtr.icovw001_unidade_vinculada AS 
			SELECT unidade_vncda_suat.nu_unidade_dire,
			    unidade_vncda_suat.nu_natural_dire,
			    unidade_vncda_suat.tipo_dire as sg_tipo_dire,
			    unidade_vncda_suat.no_dire,
			    unidade_vncda_suat.co_email_dire,
			    unidade_vncda_suat.tipo_sr as sg_tipo_sr,
			    unidade_vncda_suat.nu_unidade_sr,
			    unidade_vncda_suat.nu_natural_sr,
			    unidade_vncda_suat.no_su_regional,
			    unidade_vncda_suat.co_email_sr,
			    unidade_vncda_suat.tipo_unidade as sg_tipo_unidade,
			    unidade_vncda_suat.nu_unidade,
			    unidade_vncda_suat.nu_natural,
			    unidade_vncda_suat.no_unidade,
			    unidade_vncda_suat.uf as sg_uf,
			    unidade_vncda_suat.co_email_ag
			   FROM ( WITH emails AS (
					 SELECT DISTINCT u24.nu_unidade,
					    u24.nu_natural,
					    m02.co_comunicacao AS intranet
					   FROM ico.icotbu24_unidade u24
					     LEFT JOIN ico.icotbm03_meiocomun m03 ON u24.nu_unidade = m03.nu_unidade_v03 AND u24.nu_natural = m03.nu_natural_v03
					     LEFT JOIN ico.icotbm02_meio_comu m02 ON m03.nu_sqncl_cmnco_m02 = m02.nu_sqnl_cmnco AND m02.nu_tipo_cmnco_m04 = 5
					  WHERE m02.nu_sqnl_cmnco = (( SELECT max(a.nu_sqncl_cmnco_m02) AS max
						   FROM ico.icotbm03_meiocomun a,
						    ico.icotbm02_meio_comu b
						  WHERE a.nu_unidade_v03 = m03.nu_unidade_v03 AND a.nu_natural_v03 = m03.nu_natural_v03 AND a.nu_sqncl_cmnco_m02 = b.nu_sqnl_cmnco AND b.nu_tipo_cmnco_m04 = 5))
					  ORDER BY u24.nu_unidade
					)
				( SELECT tp_vinc_sr.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_sr.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    suat.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    sr.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_unidade.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM ico.icotbu24_unidade pab
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_unidade ON tipo_unidade.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = suat.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 8 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[9, 56])) AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (tp_vinc_sr.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_sr.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842])) AND email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24 AND email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24 AND email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  ORDER BY pab.nu_unidade)
				UNION
				( SELECT tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    sr.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_pab.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_pab.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    agencia.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_pab.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM ico.icotbu24_unidade pab
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu21_tpunidade tipo_pab ON tipo_pab.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = agencia.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 42 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[8, 9, 56])) AND (tp_vinc_ag.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_ag.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842]))
				  ORDER BY pab.nu_unidade)) unidade_vncda_suat;
		';
	ELSE
		EXECUTE '
			CREATE OR REPLACE VIEW icosm001.icovw001_unidade_vinculada AS 
			SELECT unidade_vncda_suat.nu_unidade_dire,
			    unidade_vncda_suat.nu_natural_dire,
			    unidade_vncda_suat.tipo_dire as sg_tipo_dire,
			    unidade_vncda_suat.no_dire,
			    unidade_vncda_suat.co_email_dire,
			    unidade_vncda_suat.tipo_sr as sg_tipo_sr,
			    unidade_vncda_suat.nu_unidade_sr,
			    unidade_vncda_suat.nu_natural_sr,
			    unidade_vncda_suat.no_su_regional,
			    unidade_vncda_suat.co_email_sr,
			    unidade_vncda_suat.tipo_unidade as sg_tipo_unidade,
			    unidade_vncda_suat.nu_unidade,
			    unidade_vncda_suat.nu_natural,
			    unidade_vncda_suat.no_unidade,
			    unidade_vncda_suat.uf as sg_uf,
			    unidade_vncda_suat.co_email_ag
			   FROM ( WITH emails AS (
					 SELECT DISTINCT u24.nu_unidade,
					    u24.nu_natural,
					    m02.co_comunicacao AS intranet
					   FROM icosm001.icotbu24_unidade u24
					     LEFT JOIN icosm001.icotbm03_meiocomun m03 ON u24.nu_unidade = m03.nu_unidade_v03 AND u24.nu_natural = m03.nu_natural_v03
					     LEFT JOIN icosm001.icotbm02_meio_comu m02 ON m03.nu_sqncl_cmnco_m02 = m02.nu_sqnl_cmnco AND m02.nu_tipo_cmnco_m04 = 5
					  WHERE m02.nu_sqnl_cmnco = (( SELECT max(a.nu_sqncl_cmnco_m02) AS max
						   FROM icosm001.icotbm03_meiocomun a,
						    icosm001.icotbm02_meio_comu b
						  WHERE a.nu_unidade_v03 = m03.nu_unidade_v03 AND a.nu_natural_v03 = m03.nu_natural_v03 AND a.nu_sqncl_cmnco_m02 = b.nu_sqnl_cmnco AND b.nu_tipo_cmnco_m04 = 5))
					  ORDER BY u24.nu_unidade
					)
				( SELECT tp_vinc_sr.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_sr.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    suat.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    sr.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_unidade.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM icosm001.icotbu24_unidade pab
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_unidade ON tipo_unidade.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = suat.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 8 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[9, 56])) AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (tp_vinc_sr.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_sr.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842])) AND email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24 AND email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24 AND email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  ORDER BY pab.nu_unidade)
				UNION
				( SELECT tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    sr.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_pab.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_pab.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    agencia.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_pab.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM icosm001.icotbu24_unidade pab
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_pab ON tipo_pab.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = agencia.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 42 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[8, 9, 56])) AND (tp_vinc_ag.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_ag.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842]))
				  ORDER BY pab.nu_unidade)) unidade_vncda_suat;
		';
	END IF;

END
$$;