/*==============================*/
/* CRIACAO DAS FUNCTIONS DE DML */ 
/*==============================*/

CREATE OR REPLACE FUNCTION mtr.ajusta_processo_tabela19() RETURNS void AS $$
BEGIN

    UPDATE mtr.mtrtb019_campo_formulario SET nu_processo = (
        SELECT nu_processo_pai FROM mtr.mtrtb026_relacao_processo WHERE nu_processo_filho = nu_processo_fase
    );
    
    UPDATE mtr.mtrtb019_campo_formulario SET nu_processo = (SELECT MIN(nu_processo) FROM mtr.mtrtb020_processo WHERE ic_dossie = true) WHERE nu_processo IS NULL;

END;
$$ LANGUAGE plpgsql;