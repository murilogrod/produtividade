SIMTR - Sistema de Modernização no Tratamento e Reuso de Documentos por Imagem
==============================================================================

## Descrição


## Requisitos Mínimos

 - [Java] 1.8+
 - [JBoss] 1.7+
 - [Maven] 3.2.5+
 - [jBPM] 7.27.0+


## Instalação Manual



## Ambientes

 - SIMTR-API:
   - PRD: https://simtr-api.apl.caixa/simtr-api
   - HMP: http://simtr-api-hmp.nprd2.caixa/simtr-api
   - TQS: http://simtr-api-tqs.nprd2.caixa/simtr-api
   - DEV: http://simtr-api-dev.nprd2.caixa/simtr-api
   - HMP PEDES: http://10.123.15.53:8080/simtr-api
   - TST PEDES: http://10.123.15.52:8080/simtr-api
   - DES PEDES: http://10.123.15.51:8080/simtr-api
 
 - SIMTR-WEB:
   - PRD: https://dossiedigital.caixa ou https://simtr-web.apl.caixa
   - HMP: http://simtr-web-hmp.nprd2.caixa
   - TQS: http://simtr-web-tqs.nprd2.caixa
   - DEV: http://simtr-web-dev.nprd2.caixa
   - HMP PEDES: http://simtr.hmp.caixa
   - TST PEDES: http://simtr.tst.caixa
   - DES PEDES: http://simtr.des.caixa
   
 - JBPM:
   - PRD: https://simtr-jbpm.caixa/business-central
   - HMP: https://simtr-jbpm.hmp.caixa/business-central
   
 - SIMTR-PAE:
   - PRD: https://simtr-web-pae.apl.caixa
   - HMP: http://simtr-web-pae-hmp.nprd2.caixa
   - TQS: http://simtr-web-pae-tqs.nprd2.caixa
   - DEV: http://simtr-web-pae-dev.nprd2.caixa


## IPs do Ambiente CEPTI (utilizados em regras de firewall)
- SIMTR-API-DEV:
    - Entrada: 10.116.180.17 (:80 ou :443)
    - Saída: 10.116.182.109

- SIMTR-API-PRD:
	- Entrada: 10.121.110.2 (:80 ou :443)
	- Saída: 10.121.96.104
	
	
## Servidores JBPM DEV e TQS
- DEV:
	- CBRTQAPLLX014
	- 10.116.26.28
		
- TQS:
	- CBRDEAPLLX085
	- 10.116.95.121

- Comando de start do JBPM em DEV/TQS:
	nohup /opt/jbpm-server/bin/standalone.sh -b 0.0.0.0  > /dev/null &
	
	
## Bancos de Dados
- PEDES:
	- DES: go7875sx018.goiania.caixa:5432/posdes01?currentSchema=mtr
	- TST: go7875sx018.goiania.caixa:5432/postst01?currentSchema=mtr
	- HMP: go7875sx018.goiania.caixa:5432/poshmp01?currentSchema=mtr

- CEPTI:	
	- DEV/TQS: SCTDEDADNT0007.EXTRA.CAIXA.GOV.BR:5432/posprd01?currentSchema=mtr (Proxy somente consulta: 10.116.83.70:8080/posprd01?currentSchema=mtr)
	- HMP: 
	- PRD: 10.192.176.207:5415/posprd15?currentSchema=mtr

	
## Configuração



#### Manual


#### Propriedades do JBoss

- Lista de propriedades com os valores referentes ao ambiente DEV CEPTI:

```    
	<system-properties>
        <property name="ged_token_teste" value="false"/>
        <property name="mock_siiso" value="false"/>
        <property name="mock_siiso_pf" value="false"/>
        <property name="mock_sipes" value="false"/>
        <property name="obj_siiso" value="{}"/>
        <property name="obj_siiso_pf" value="{}"/>
        <property name="obj_sipes" value="{}"/>
        <property name="simtr_apikey" value="l7eee98acd64c94538af840e0d15f4504b"/>
        <property name="simtr_pae_acesso_matriz_funcao" value="2030;2037;2038;2048"/>
        <property name="simtr_pae_acesso_matriz_unidade" value="5402;5061;5532;5307"/>
        <property name="simtr_auth_server_url_sso_filter" value="https://login.des.caixa/auth"/>
        <property name="simtr_resource_sso_filter" value="cli-ser-mtr"/>
        <property name="simtr_realm_sso_filter" value="intranet"/> 
        <property name="simtr_ignore_sso_link_filter" value="false"/> 
        <property name="simtr_sso_client_secret" value="d2dc0325-c071-4f1f-897b-c3e0dec512a6"/>
        <property name="simtr_url_ignore_filter" value="/rest/servidor"/>
        <property name="simtr_url_manual_usuario" value="ftp://ftp.go.caixa/PEDES/SIICD/SIICD_Manual_Usuario.pdf"/>
        <property name="simtr_foto_perfil_url" value="http://tdv.caixa/img"/>
        <property name="sipes_user" value="SMTRPESD"/>
        <property name="sipes_password" value="123"/>
        <property name="url_localidade" value="http://api.des.caixa:8080/informacoes-corporativas/v1/localidades/ceps"/>
        <property name="url_siecm_ged" value="http://siecm.des.caixa/siecm-web"/>
        <property name="url_sipes" value="http://api.des.caixa:8080/pesquisa-cadastral/v1/pesquisar"/>
        <property name="url_sso_internet" value="https://logindes.caixa.gov.br"/>
        <property name="url_sso_intranet" value="https://login.des.caixa"/>
        <property name="url.servico.apimanager" value="https://api.des.caixa:8443"/>
        <property name="url.servico.avalia.documento.sifrc" value="http://s.siclon.caixa/DossieProcessaAntifraude/"/>
        <property name="url.servico.avalia.documento.sicod" value="http://ba0000sx205.ba.caixa:8080/sicod/api/avaliacao/consultar/cnh"/>
        <property name="url.servico.atualiza.cliente.sicli" value="http://api.des.caixa:8080/cadastro/v1/clientes"/>
        <property name="url.servico.consulta.cpf.sicli" value="http://api.des.caixa:8080/cadastro/v1/clientes"/>
        <property name="url.servico.consulta.cnpj.siiso" value="https://api.des.caixa:8443/cadastro-receita/v3/pessoas-juridicas"/>
        <property name="url.servico.consulta.cpf.siiso" value="https://api.des.caixa:8443/cadastro-receita/v3/pessoas-fisicas"/>
        <property name="url.servico.consulta.tomador.siric" value="https://api.des.caixa:8443/risco-credito/v1/tomadores-credito/{cpf-cnpj}/avaliacoes-tomador"/>
        <property name="url.servico.extracao.autenticidade" value="http://172.28.14.41:8104/fleximageService/CefService"/>
        <property name="token.servico.extracao.autenticidade" value="FLEXIMAGE 9D5B466CDBC5C03B0ACF024251CBFBF00BC31B7EE0A8C9C587F4ECF76CB62F49D39063A882CCC991"/>
        <property name="outsourcing_sso_client_secret" value="cli-ser-mtr-flexdoc;47d5aa0f-a150-42e3-83ff-5795a4edf79c"/>
        <property name="url.simtr.api" value="http://simtr-api-dev.nprd2.caixa"/>
        <property name="url.servico.bpm" value="https://simtr-jbpm.dev.caixa/kie-server/services/rest"/>
        <property name="usuario.nome.servico.bpm" value="kieserver"/>
        <property name="simtr.keystore.type" value="JCEKS"/>
        <property name="simtr.keystore.url" value="${jboss.server.config.dir}/simtr-keystore.jceks"/>
        <property name="simtr.keystore.secret" value="serverpwd"/>
        <property name="simtr.keystore.bpm.alias" value="bpm-usr-alias"/>
        <property name="simtr.keystore.bpm.secret" value="bpm-usr-secret"/>
    </system-properties>
```

	**OBS: Os valores das propriedades definidos acima tratam-se de exemplos e devem ser definidos conforme cada ambiente.**


## Dúvidas?

Caso tenha dúvidas ou precise de suporte, entrar em contato com a GITECGO (PEDeS) ou GEBAN04.

## Changelog

Para consultar o log de alterações da API acesse o arquivo [CHANGELOG-API.md](CHANGELOG-API.md).

Para consultar o log de alterações da aplicação SPA (WEB) acesse o arquivo [CHANGELOG-WEB.md](CHANGELOG-WEB.md).
