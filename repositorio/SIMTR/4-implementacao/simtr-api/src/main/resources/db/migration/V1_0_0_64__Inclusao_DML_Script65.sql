/*==============================*/
/* CRIACAO DAS FUNCTIONS DE DML */ 
/*==============================*/

CREATE OR REPLACE FUNCTION mtr.atualiza_tipo_relacionamento() RETURNS void AS $$
BEGIN
    INSERT INTO mtr.mtrtb041_tipo_relacionamento (nu_versao, no_tipo_relacionamento, ic_principal, ic_relacionado, ic_sequencia, ic_tipo_pessoa) VALUES
    (1, 'Avalista', false, true, false, 'A'),
    (1, 'Conglomerado - Participante', false, true, false, 'J'),
    (1, 'Conjuge', false, true, false, 'F'),
    (1, 'Curador', false, true, false, 'F'),
    (1, 'Fiador', false, true, false, 'A'),
    (1, 'Procurador', false, true, false, 'F'),
    (1, 'Sócio Pessoa Física', false, true, false, 'F'),
    (1, 'Sócio Pessoa Jurídica', false, true, false, 'J'),
    (1, 'Representante Legal - Sócio', false, true, false, 'F'),
    (1, 'Representante Legal - Não Sócio', false, true, false, 'F'),
    (1, 'Titular', true, false, true, 'A'),
    (1, 'Tomador do Contrato', true, false, false, 'A'),
    (1, 'Tutor', false, true, false, 'F');

    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Avalista') WHERE TRIM(ic_tipo_relacionamento) = 'AVALISTA';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Conglomerado - Participante') WHERE TRIM(ic_tipo_relacionamento) = 'CONGLOMERADO';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Conjuge') WHERE TRIM(ic_tipo_relacionamento) = 'CONJUGE';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Curador') WHERE TRIM(ic_tipo_relacionamento) = 'CURADOR';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Fiador') WHERE TRIM(ic_tipo_relacionamento) = 'FIADOR';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Procurador') WHERE TRIM(ic_tipo_relacionamento) = 'PROCURADOR';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Sócio Pessoa Física') WHERE TRIM(ic_tipo_relacionamento) = 'SOCIO_PF';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Sócio Pessoa Jurídica') WHERE TRIM(ic_tipo_relacionamento) = 'SOCIO_PJ';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Representante Legal - Sócio') WHERE TRIM(ic_tipo_relacionamento) = 'SOCIO_REPRESENTANTE_LEGAL';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Representante Legal - Não Sócio') WHERE TRIM(ic_tipo_relacionamento) = 'REPRESENTANTE_LEGAL_NAO_SOCIO';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Titular') WHERE TRIM(ic_tipo_relacionamento) = 'TITULAR';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Tomador do Contrato') WHERE TRIM(ic_tipo_relacionamento) = 'TOMADOR';
    UPDATE mtr.mtrtb004_dossie_cliente_produto SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Tutor') WHERE TRIM(ic_tipo_relacionamento) = 'TUTOR';

    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Avalista') WHERE TRIM(ic_tipo_relacionamento) = 'AVALISTA';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Conglomerado - Participante') WHERE TRIM(ic_tipo_relacionamento) = 'CONGLOMERADO';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Conjuge') WHERE TRIM(ic_tipo_relacionamento) = 'CONJUGE';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Curador') WHERE TRIM(ic_tipo_relacionamento) = 'CURADOR';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Fiador') WHERE TRIM(ic_tipo_relacionamento) = 'FIADOR';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Procurador') WHERE TRIM(ic_tipo_relacionamento) = 'PROCURADOR';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Sócio Pessoa Física') WHERE TRIM(ic_tipo_relacionamento) = 'SOCIO_PF';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Sócio Pessoa Jurídica') WHERE TRIM(ic_tipo_relacionamento) = 'SOCIO_PJ';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Representante Legal - Sócio') WHERE TRIM(ic_tipo_relacionamento) = 'SOCIO_REPRESENTANTE_LEGAL';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Representante Legal - Não Sócio') WHERE TRIM(ic_tipo_relacionamento) = 'REPRESENTANTE_LEGAL_NAO_SOCIO';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Titular') WHERE TRIM(ic_tipo_relacionamento) = 'TITULAR';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Tomador do Contrato') WHERE TRIM(ic_tipo_relacionamento) = 'TOMADOR';
    UPDATE mtr.mtrtb025_processo_documento SET nu_tipo_relacionamento = (SELECT nu_tipo_relacionamento FROM mtr.mtrtb041_tipo_relacionamento WHERE no_tipo_relacionamento = 'Tutor') WHERE TRIM(ic_tipo_relacionamento) = 'TUTOR';

END;
$$ LANGUAGE plpgsql;