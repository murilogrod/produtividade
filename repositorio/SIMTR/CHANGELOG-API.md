** Release Notes: ** 


V 1.10.0 (WIP) 
====================

### Inclusões
- S000 - Validação dos elementos obrigatórios na inclusão de inclusão de um dossiê de produto
- S035 - Cadastro do cliente passa a contar com o armazenamento do email utilizado no cadastro do SSO 

### Alterações
- S033 - Alterado end-points utilizados no fluxo de extração de dados pela equipe interna do contexto /avaliacao-extracao para o contexto /extracao-dados
- S033 - Alterado end-points utilizados na solicitação dos serviços de extração de dados, classificação e avaliação documental contexto /avaliacao-extracao para o contexto /outsourcing


V 1.9.0 (06/11/2019) 
====================
    
### Alterações
- S032 - Padronizado os end-points dos contextos /dossie-cliente e /dossie-produto para separar os termos compostos
- S032 - Padronizado os end-points dos contextos do apoio ao négocio para transitar um único binário para o documento na inclusão e na leitura

### Correções    
S032 - Corrigido estrutura de permissões nos end-points de consumo direto ao serviço do Cadastro.CAIXA
    
### Erros Conhecidos
- Não executa validação de obrigatóriedade na alteração de um dossiê de produto
- A quantidade de conteúdos esta sendo armazenada como a quantidade de binários e não como a quantidade de páginas

V 1.8.1 (04/06/2019)
=============================

### Alterações
- S032 - Alterado uso da API do SICLI para versão 1.3.2
   
### Erros Conhecidos
- Não executa validação de obrigatóriedade na inclusão de um dossiê de produto
- Não executa validação de obrigatóriedade na alteração de um dossiê de produto
- A quantidade de conteúdos esta sendo armazenada como a quantiadade de binários e não como a quantidade de páginas


V 1.8.0
=======

### Inclusões
- Inclusão de rotina para inabilitar documentos anteriores com base na parametrização da tabela 003
- Inclusão da consulta ao SICOD para documentos de CNH no fluxo do dossiê digital
- Incluida informação de orientação ao usuário para o processo fase
- Inclusão do armazenamento do porte da PJ na tabela 001
- Inclusão das propriedades JBoss:
    - simtr.keystore.secret
    - simtr.keystore.type
    - simtr.keystore.url
    - simtr.keystore.bpm.alias
    - simtr.keystore.bpm.secret


### Alterações
- Atualização da API do SICLI V1.3.0
	- Inclusão de opção "S" (Sem Renda) para o tipo de renda

- Modificação das referências do termo GED para SIECM nas definições de atributos e tipologia documental

- Remoção da referência de indicação de envio ao GED por atributo especifico. A informação passa a ser enviada para o GED baseado na parametrização do campo no_atributo_siecm
- Comunicação com o jBPM passa a ser permitida de 3 formas:
	- Usuario e senha de acesso do BPM definidos diretamente em propriedades - Comunicação Basic Auth;
	- Usuario de acesso do BPM definido em propriedade e senha definida em Keystore - Comunicação Basic Auth;
	- Usuário e senha de acesso do BPM não definidos em propriedades - Comunicação OAuth2 utilizando token de serviço;  
    
    
    
.    
----
    
** ESTRUTURA PADRÃO **     
    
V 0.0.0 (Major.minor.BugFix)
=============================

### Inclusões
    
### Alterações
    
### Correções    
    
### Erros Conhecidos

.    
----