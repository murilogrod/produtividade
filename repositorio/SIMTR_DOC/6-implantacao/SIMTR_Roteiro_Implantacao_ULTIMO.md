######## Tarefas de JBoss ########

######## Adicionar a seguinte propriedade ########
	
	<property name="url.servico.avalia.documento.sifrc" value="http://<DNS>/DossieProcessaAntifraude"/>
    <property name="url.servico.apimanager" value="https://api.des.caixa:8443"/>
	<property name="url.servico.consulta.tomador.siric" value="https://api.des.caixa:8443/risco-credito/v1/tomadores-credito/{cpf-cnpj}/avaliacoes-tomador"/>
    <property name="url.servico.executa.tomador.siric" value="https://api.des.caixa:8443/risco-credito/v2/tomadores-credito/{cpf-cnpj}/propostas-tomador"/>
    <property name="url.servico.analytics" value="https://des.analytics.mobilidade.caixa.gov.br/analytics/ws/evento/registrar"/>
    <property name="simtr.analitycs" value="true"/>
    <property name="analitycs.simtr.key" value="9256be37c021722e9e6c78544ac6cbfa6e31f6e7"/>
    *Para que os serviços do analytics sejam acessados é necessário adicionador os certificados para os endereços*
    

######## Adicionar o seguinte datasource ########
	
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