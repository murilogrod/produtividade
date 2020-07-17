SEU PROJETO
====================

O que é isto?
-----------

Este é o seu projeto gerado pelo SIOGP. Com as seguintes caracteristicas
  Interface: HTML5 com Angular e Bootstrap
  Webservice: Java EE Restfull
  Angular: 1.4.7
  Bootstrap: 3.3.4
  Java: JDK 1.8
  Suporte: Edge, IE9, Chrome 40, Firefox 40, Mobile, Android 2.0.0
  Mobile: Android APK v2.0.0 integrado
  Aceite: Selenium 2.29
  Metricas: Caixa Analythics
  Orientação: EJB e WebService por Funcionalidade
  Comentários: SIM
  Segurança: SIM
  Documentação: DOCEB30
  
A solução ANGULAR oferece uma aplicação web responsiva com suporte a dispositivos móveis.
A interface utiliza o framework bootstrap/angular integrado a um webservice java 1.8 com arquitetura rest.

Este template é orientado por tela, isto significa que a estrutura do seu projeto irá conter um objeto EJB para cada funcionalidade. 

Requisitos do sistema
-------------------

Tudo o que você precisa para construir este projeto é JBoss 6.3 EAP, Eclipse, Java 8.0 (Java SDK 1.8) ou melhor, Maven 3,0 ou superior.

Estes arquivos podem ser encontrados em:

    http://gitlab.corerj.caixa/SUPORTE/ambiente_padrao
  
A aplicação que este projeto produz é projetada para ser executada em um JBoss AS 7.
 
NOTA:
Este projeto recupera artefatos do repositório JBoss Community Maven.

Aponte o eclipse (Window > Preferences > Maven > User Settings > Global Settings) para realizar a pesquisa do nexus da caixa (arquivo esta indo com seu projeto maven/settings.xml). 

Configure estas duas variáveis de ambiente no JBoss (Para sistemas em desenvolvimento):

    -Dsimma.local=https://des.analytics.mobilidade.caixa.gov.br/analytics
    -Dsicpu.local=https://des.central.mobilidade.caixa.gov.br/sicpu
    -Dsisit.local=https://des.central.mobilidade.caixa.gov.br/sisit

Com os pré-requisitos fora do caminho, você está pronto para construir e implantar.

Importando o projeto para o Eclipse
=================================

File > Import > Existing Maven Projects > Browser = Selecione o projeto raiz

