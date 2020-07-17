# Roteiro de Implantação SIMTR

## Informações

Nome do sistema: SIMTR

Situação: novo

## Banco de dados 

Postgresql 9.4

## Configuração servidor de aplicação

Versão: JBOSS EAP 7

### Instalando módulo do keycloak
- Extrair os arquivos do ZIP keycloak-wildfly-adapter-dist-3.2.1.Final.zip
- Copiar a pasta keycloak de dentro do zip e colar em <JBOSS_HOME>/modules/system/add-ons
- Procure pela tag ** <extensions>** e adicione a linha abaixo caso não existir
```
	 <extension module="org.keycloak.keycloak-adapter-subsystem"/>
```
- Procure pela tag ** <security-domains>** e adicione o código abaixo caso não exisitr
```
	<security-domain name="keycloak">
		<authentication>
			<login-module code="org.keycloak.adapters.jaas.BearerTokenLoginModule" flag="required"/>
		</authentication>
	</security-domain>
```

- Procure pela última tag ** <subsystem xxxxxx> ** e adicione o subsystem do keycloak caso não existir
```
	<subsystem xmlns="urn:jboss:domain:keycloak:1.1">
	</subsystem>
```


### properties


- Somente adicionar se a propriedade não existir (Substituir os valores de acordo com o ambiente da caixa)
```
<system-properties>
    <property name="simtr_url_manual_usuario" value="<URL_Manual_Usuario>"/>
	<property name="simtr_siusr_login_fake" value="true"/>
	<property name="simtr_siusr_cpf_endpoint" value="/var/sistemas"/>
    <property name="simtr_rotina_apaga_imagens_temporarias" value="0 0 01 * * *"/>	
	<property name="simtr_rotina_apaga_documentos_temporarios" value="0 0 02 * * *"/>
	<property name="simtr_imagens" value="/imagens"/>
	<property name="url_siecm_ged" value="http://<DNS>/siecm-web/"/>
	<property name="url_sicpf" value="http://<DNS>/sibar/consulta_cpf/v1/consulta"/>	
	<property name="url_sipes" value="http://<DNS>/clientes/pesquisa-cadastral/v1"/>
	<property name="mock_sipes" value="<true or false>"/>
	<property name="obj_sipes" value="<JSON_SIPES>"/>
	<property name="semsg.url.webservice" value="http://<DNS>/servico/template"/>
	<property name="sipes_user" value="<USUARIO>"/>
	<property name="sipes_password" value="<SENHA>"/>
	<property name="url.servico.consulta.cpf.sicli" value="http://<DNS>/sicli-web/ws/v1/clientes"/>
	<property name="url.servico.avalia.documento.sifrc" value="http://<DNS>/DossieProcessaAntifraude"/>
    <property name="url.servico.atualiza.cliente.sicli" value="http://<DNS>/sicli-web/ws/manutencao/atualizaDossieDigital"/>
    <property name="url.servico.consulta.cliente.sicli" value="http:/<DNS>/cadastro/ws/v1/clientes"/>
</system-properties>
```
- A propriedade "simtr_siusr_login_fake" indica ao sistema para usar o fake do siusr.
- A propriedade "simtr_siusr_cpf_endpoint" serve para identificar o caminho onde o siusr deverá ir. Se a propriedade "simtr_siusr_login_fake" for true, este
	valor deverá ser um caminho dentro do servidor. Se a propriedade for false, o valor deverá ser o URL do servidor do SIUSR (http://<DNS>/siusr-service/AutenticaService/AutenticaWS)
- A propriedade "simtr_rotina_apaga_imagens_temporarias" irá disparar uma rotina que apaga as imagens temporárias do sistema
- A propriedade "simtr_rotina_apaga_documentos_temporarios" irá disparar uma rotina que apaga do banco de dados  os arquivos temporários usados
	para o dossiê digital.
- A propriedade "simtr_imagens" especifica o caminho onde as imagens temporárias serão guardadas.
- Na propriedade "url_siecm_ged" substituir o <DNS> pelo valor correto
- Na propriedade "url_sicpf" substituir o <DNS> pelo valor correto
- Na propriedade "url_sipes" substituir o <DNS> pelo valor correto
- A propriedade "mock_sipes" indica ao sistema se ele deverá usar a propriedade obj_sipes ou url_sipes para fazer a chamada
- A propriedade "obj_sipes" serve para fornecer um json quando a propriedade "mock_sipes" for true
- Na propriedade "semsg.url.webservice" substituir o <DNS> pelo valor correto. Usado para fazer consulta dos end-points do SEMSG
- A propriedade "sipes_user" indica para chamada SIPES quando "mock_sipes" false o usuário para autorização basic
- A propriedade "sipes_password" inidica para chamada SIPES quando "mock_sipes" false a senha para autorização basic
- Na propriedade "url.servico.consulta.cpf.sicli" substituir o <DNS> pelo valor correto Usado para fazer consulta dos dados do cliente no SICLI
- Na propriedade "url.servico.avalia.documento.sifrc" <DNS> pelo valor correto Usado para fazer avaliação junto ao SIFRC/>
- Na propriedade "url.servico.atualiza.cliente.sicli" substituir o <DNS> pelo valor correto Usado para fazer a atualização dos dados do cliente no SICLI
- Na propriedade "url.servico.consulta.cliente.sicli" substituir o <DNS> pelo valor correto Usado para fazer a consulta dos dados do cliente no SICLI
### datasource

- Somente adicionar se o datasource não existir (Substituir os valores de acordo com o ambiente da caixa).

```
<datasource jta="true" jndi-name="java:jboss/datasources/simtr-ds" pool-name="simtr-ds" enabled="true" use-ccm="true">
    <connection-url>**URL_BASE_DADOS**</connection-url>
    <driver-class>org.postgresql.Driver</driver-class>
    <driver>postgresql</driver>
    <security>
        <user-name>**USUARIO_BASE_DADOS**</user-name>
        <password>**SENHA_BASE_DADOS**</password>
    </security>
</datasource>

<datasource jta="true" jndi-name="java:jboss/datasources/siico-mtr-ds" pool-name="siicoPU" enabled="true" use-ccm="true">
    <connection-url>**URL_BASE_DADOS**</connection-url>
    <driver-class>org.postgresql.Driver</driver-class>
    <driver>postgresql</driver>
    <security>
        <user-name>**USUARIO_BASE_DADOS**</user-name>
        <password>**SENHA_BASE_DADOS**</password>
    </security>
    <validation>
        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker"/>
        <background-validation>true</background-validation>
        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
    </validation>
</datasource>
```

### virtual server

- Procure pela tag **subsystem xmlns="urn:jboss:domain:undertow:X.X"** e dentro da tag **server name="default-server"** adicione caso não existir(Substituir os valores de acordo com o ambiente da caixa)

```
<host name="SIMTR" default-web-module="SIMTR" alias="**<DNS_APLICAÇÃO>**">
    <location name="/" handler="welcome-content"/>
</host>
```

### Adicionando proteção do back-end com o keycloak

- Procure pela tag ** <subsystem xmlns="urn:jboss:domain:keycloak:1.1"> ** e adicione o código abaixo caso não existir. Substituir os valores para o ambiente caixa

```
	<secure-deployment name="simtr-api.war">
		<realm><REALM></realm>
		<resource><RESOURCE></resource>
		<bearer-only>true</bearer-only>
		<auth-server-url><URL_KEYCLOAK_SERVER></auth-server-url>
		<ssl-required>EXTERNAL</ssl-required>
		<allow-any-hostname>true</allow-any-hostname>
		<enable-cors>true</enable-cors>
		<cors-allowed-headers>Origin, X-Requested-With, X-XSRF-TOKEN, Content-Type, Accept , Authorization, access-control-allow-origin,Access-Control-Allow-Credentials</cors-allowed-headers>
		<cors-allowed-methods>GET, POST, DELETE, PUT, OPTIONS, PATCH</cors-allowed-methods>
	</secure-deployment>
```
-<REAL> Nome do realm onde o cliente da aplicação foi criado
-<RESOURCE> Nome do cliente da aplicação
-<URL_KEYCLOAK_SERVER> URL do servidor , para desenvolvimento será https://login.des.caixa/auth

### deploy

Baixar o(s) pacote(s) e implantá-lo(s) conforme os passos abaixo:

1) Acesse o link  [SIMTR_REPOSITORIO](http://artefatos.pedes.caixa/nexus/content/repositories/releases/br/gov/caixa/SIMTR/);

2) Procure pelo war mais atual;

3) Realize o deploy.