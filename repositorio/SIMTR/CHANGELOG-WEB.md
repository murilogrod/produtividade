** Release Notes: ** 

V 1.7.0 (WIP)
=============

### Inclusões
- Apresentação do bloco de unidade analisada no Dashboard para os usuários com perfil MTRSDNMTZ
- Inclusão de novo menu apresentando a lista de dossiês em situação "Aguardando Complementação"
	- Esta tela viabiliza capturar o dossiê de produto no modelo de fila para realizar alimentação de informação (tela de manutenção do dossiê de produto)
- Inclusão de orientação ao operador referente ao que deve ser observado na avaliação do checklist na tela de tratamento
- Possibilidade de atualizar dados de um documento a partir da consulta ao mesmo
- Possibilidade de cadastrar a senha junto ao SSO para o cliente pessoa fisica a partir da aba "Identificação" na tela de consulta ao dossiê de cliente
- Possibilidade de disparar a atualização cadastral do cliente junto ao Cadastro.CAIXA por meio dos documentos atualizados a partir da aba "Documentos" na tela de consulta ao dossiê de cliente 
- Possibilidade de gerar o cartão de assinaturas para o cliente pessoa fisica a partir da aba "Documentos" na tela de consulta ao dossiê de cliente
- Tela de Tratamento passa a apresentar a instancia de documento já avaliada como conforme na cor verde
- Cadastro do cliente passa a contar com novo campo para email que será utilizado no cadastro do SSO
- Formulario passam a contar com um novo tipo de campo CEP (Limita o tipo de caracter e realiza a formatação do campo no padrão)
- Inclusão da possibilidade de definir um formulário dinamico para o vinculo de pessoas conforme o tipo de relacionamento definido no processo
- Inclusão da possibilidade de definir um formulário dinamico para o vinculo de produtos conforme o produto selecionado no processo
- Inclusão da possibilidade de definir um formulário dinamico para o vinculo de garantias conforme a garantia selecionada no processo
- Ativada a utilização de expressões de interface para apresentar campos condicionais nos formulários dinâmicos 

### Alterações
- Adequação do consumo aos end points de manipulação de documentos para considerar um único binario, ao inves de uma possível lista de conteudos
- Alterado a tela de extração manual de dados de documentos para componentizar os elementos:
	- Segregado componente de visualização de imagens
	- Segregado componente de montagem do formulario de extração de dados
	- Definido componente controlador de eventos (submissão, cancelçamento, rejeição) fazendo uso dos componentes de formulario e visualizador
- Alterado o termo "Novo Documento" para "Upload"
- Alterado serviço de cadastramento de documento de Dados Declarados para considerar a possibilidade de atributos multivalorados
- Adequação do formulário de Dados Declarados para considerar a configuração de ordenação dos campos
- Tela de tratamento passa a apresentar os chaveadores indicativos de registros analisados ou não desabilitados quando o registro não requer análise
- Grid de processos na tela de Dashboard passa a apresentar apenas uma unidade e em caso de lista apresenta em hist sobre o repouso do mouse
- Melhoria nas cores do bradcrumb do dossiê de produto para facilitar a identificação da situação atual do mesmo 
    
### Correções
- Corrigido apresentação da lista de situações expandida no dashboard
- Corrigido apresentação do nome do checklist na tela de tratamento
- Corrigido erro de estouro de persistence storage ao solicitar a visualização da imagem em outra janela na tela de tratamento
- Ajustado vinculo de garantias que não estava permitindo a associação de registro isento de associação cliente relacionado (Ex: Aval)
- Mensagens de validação na tela de tatamento não exigem mais que o registro seja selecionado
- Corrigido apresentação de alerta indevido na tela de tratamento por identificação incorreta de múltiplos checklists
- Ajustado apresentação de cores aos nomes nas arvores de documento quando um documento é reutilizado
- Corrigido possibilidade de alterar dados do cliente oriundos da Receita Federal na aba "Identificação" 
- Tela de tratamento não estava permitindo finalizar o tratamento para registros que não possuem checklists associados
- Tela de tratamento não estava permitindo finalizar o tratamento para registros de checklists de fase com análise desabilitada
- Corrigido cor de documento aprovado na aba "Verificações" do dossiê de produto
 
    
### Erros Conhecidos


V 1.6.0 (11/06/2019)
====================

### Inclusões
- S32 - (Interface Cadastro Digital) Possibilidade de selecionar o comunicar com scanner por protocolo http na porta 5128
- S32 - (Interface Cadastro Digital) Inclusão de possibilidade de indicar cliente "Sem Renda" sendo enviada informação sob o documento de dados declarados
- S32 - (Interface Cadastro Digital) Dispensada a necessidade de indicar a "Confirmação de identificação do cliente"

### Alterações
- S32 - (Interface Cadastro Digital) Retira necessidade de captura de documentos de forma sequencial
- Apresentação e envio de informações do tipo de relacionamento baseado na indicação de identificador fornececido pela estrutura de processo
- Apresentação de orientação nos campos de dados declarados exibida apenas de alguma orientação estiver parametrizada
- Evolução da apresentação de cores das arvores de documentos.
	- Desconsiderada presença de instancias "inativas" (rejeitadas ou reprovadas) apresentando corretamente a identificação de pendêncianoa documentos obrigatórios
- Padronização dos end-points do grupos de operações com o Dossie de Cliente (dossie-cliente)
- Padronização dos end-points do grupos de operações com o Dossie de Produto (dossie-produto)
- Padronização do local de definição do valor para o cabeçalho "integracao"
	- Atributo passa a ser definido no arquivo de ambientes (environments.ts)

### Correções
S32 - O vinculo de produto estava enviando incorretamente o identificador e Codigos de operação/modalidade
S32 - Remover um vinculo de produto não estava removendo a respectiva arvore de documentos
S32 - Apresentação de icone de orientação de preenchimento dos campos apenas quando existe uma orientação definida
S32 - Ajustado apresentação de árvore de documentos com mais de dois níveis  

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