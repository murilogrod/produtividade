## Apoio ao Negocio - Processo

### <span style="color:blue"> GET /negocio/v1/processo/{id}</span>
>>> + *Finalidade* : Captura o processo especificado com toda a hierarquia do mesmo.

>>> + *Corpo da Mensagem* : Não se aplica

>>> + *Retorno* : 

>>>>> + processos_filho: Objeto utilizado para representar o Processo Fase do dossiê de produto no retorno as consultas realizadas sob a ótica Apoio ao Negocio.

>>>>>> + orientacao_usuario:	Informação textual utilizada para apresentar ao usuário o que deve ser feito nesta fase da operação.

>>>>>> + campos_formulario: Objeto utilizado para representar o campo de formulário vinculado a um processo fase

>>>>>>> + ordem_apresentacao:	Indica a seqüência de apresentação deste campo perante o formulário.

>>>>>>> +placeholder_campo:	Caso possua valor, determina um exemplo de valor a ser preenchido pelo usuário de forma exibir no campo quando este esta vazio como um atributo placeholder previsto no HTML.

>>>>>>> + tipo_campo: Indica o tipo de componente a ser utilizado na montagem do formulário para apresentação do campo.Ex: INPUT, RADIO, CHECK, etc Enum: CHECKBOX, COLOR, DATE, EMAIL, NUMBER, PASSWORD, RADIO, SELECT, SPAN, TEXT, TEXTAREA, TIME, URL, CNPJ, CPF, CPF_CNPJ, SELECT_SN, SELECT_VF, TELEFONE_DDD, CEP, HIDDEN, CEP_ONLINE, CEP_ONLINE_DEP 

>>>>>>> + tamanho_minimo:	Caso possua valor, determina uma validação de quantidade mínima de caracteres a ser preenchida no campo do formulário.

>>>>>>> + lista_apresentacoes:Objeto utilizado para representar a forma de apresentação do campo de formulário no retorno as consultas realizadas no Processo sob a ótica Apoio ao Negocio.

>>>>>>>> + largura:Identifica o numero de colunas ocupadas na montagem da interface baseado no conceito do bootstrap. Este valor pode variar de 1 a 12.

>>>>>>>> + tipo_dispositivo: Indica o tipo resolução do dispositivo para renderização do campo com o numero de colunas indicadas. Enum:, L, M, H, X 

>>>>>>> + nome_campo:	Nome do campo para fins de identificação programática perante o formulário.

>>>>>>> + tamanho_maximo:Caso possua valor, determina uma validação de quantidade máxima de caracteres a ser preenchida no campo do formulário.

>>>>>>> + label: Label da campo a ser apresentado na interface para o usuário.

>>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulário.

>>>>>>> + obrigatorio: Indica se o campo deve ser apresentado na interface com restrição de obrigatoriedade no preenchimento. Essa obrigatoriedade deverá ser validada.

>>>>>>> + mascara_campo: Caso possua valor, determina a mascara a ser aplicada na entrada de dados pelo usuário.

>>>>>>> + id:Identificador único do campo que representa a entrada de um formulário do processo

>>>>>>> + opcoes_disponiveis:	Objeto utilizado para representar uma opção de campo de formulário para os casos de elementos objetivos

>>>>>>>> + valor_opcao: Elemento que representa o valor da resposta. Conteúdo a ser utilizado no atributo value do HTML.

>>>>>>>> + descricao_opcao: Elemento que representa a descrição da resposta. Descrição visual apresentada no HTML.

>>>>>> + elementos_conteudo:	Objeto utilizado para representar o elemento de conteúdo vinculado a um produto ou a uma etapa do processo (fase)

>>>>>>> + identificador_elemento:	Identificador único do elemento de conteúdo esperado na definição de uma etapa do processo ou vinculação de um produto

>>>>>>> + identificador_elemento_vinculador: Identificador único do elemento de conteúdo vinculador do elemento de conteúdo, viabilizando montar uma hierarquia (arvore) de elementos dependentes

>>>>>>> + obrigatorio: Indica para se este elemento tem obrigatoriedade de carga na submissão do criação/atualização do dossiê de produto

>>>>>>> + quantidade_obrigatorios: Nos casos em que o elemento de conteúdo trata-se um agrupador (pasta) indica quantos elementos mínimos são necessários daquele item

>>>>>>> + label:	Determina o nome do label a ser exibido nos casos de elementos agrupadores (pasta). Quando o elemento for folha, deverá utilizado o nome definido pelo objeto tipo de documento e este campo será nulo.

>>>>>>> + nome_elemento: Nome do campo utilizado para fins de identificação programática perante a interface na montagem da arvore de elementos conteúdos.

>>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir uma ação sobre este elemento de conteúdo, como por exemplo, ser apresentado ou ocultado na interface. Trata-se de uma AngularExpression.

>>>>>>> + tipo_documento: Objeto utilizado para representar o tipo de documento relacionado ao vinculo de pessoas na definição do processo.

>>>>>>>> + id:	Identificador único do tipo de documento.

>>>>>>>> + nome: Nome descritivo do tipo de documento.

>>>>>>>> + permite_reuso: Valor que indica se o tipo de documento possibilita o reuso.

>>>>>> + checklists: Objeto utilizado para representar o checklist vinculado ao processo fase e relacionado ou não a um tipo de documento /função documental

>>>>>>> + id: Identificador único do checklist vinculado ao processo fase

>>>>>>> + data_revogacao:example: dd/MM/yyyy - Data limite para apresentação do checklist para um dossiê que ainda não tenha sido vinculado a nenhum checklist definido para a fase. Caso o checklist já esteja associado ao dossiê, o mesmo deverá ser apresentado independente da sua data de revogação

>>>>>>> + nome: Nome utilizado para fins de identificação do checklist a apresentação do mesmo como label

>>>>>>> + verificacao_previa:	Indica que o checklist trata-se de uma verificação previa e deve ser analisado antes dos demais previstos para a etapa

>>>>>>> + orientacao_operador: Apresenta uma orientação para o operador indicando o que deve ser observado na avaliação do checklist

>>>>>>> + tipo_documento: mesmos campos apresentados acima

>>>>>>> + funcao_documental: Objeto utilizado para representar a função documental utilizado na definição do processo não contendo a lista de tipos de documentos associados

>>>>>>>> + id:	Identificador único da função documental.

>>>>>>>> + nome: Nome descritivo da função documental.

>>>>>>> + apontamentos: Objeto utilizado para representar apontamento relacionado a um checklist

>>>>>>>> + id: Identificador único do apontamento

>>>>>>>> + titulo: Valor que representa o titulo do apontamento a ser apresentado ao operador

>>>>>>>> + descricao: Valor que indica um detalhamento sobre o apontamento. Pode ser usado como uma instrução mais detalhada sobre o que realizar naquela verificação

>>>>>> + avatar:Identificação do nome da ícone que representa o processo.

>>>>>> + nome: Nome de identificação do processo.

>>>>>> + gera_dossie: Indica se o processo pode gerar dossiê.

>>>>>> + id: Código de identificação do processo.

>>>>>> + sequencia: Indica a seqüência que o processo ocupa no fluxo de execução nos casos de processo fase.

>>>>> + tempo_tratamento: Indica o tempo em minutos que operador tem no ato do tratamento do dossiê baseado neste processo

>>>>> + garantias_vinculadas: Objeto utilizado para representar a garantia relacionada com o processo/produto.

>>>>>> + id:	Identificador único da garantia a ser utilizada

>>>>>> + codigo_bacen: Código de identificação da garantia junto ao BACEN, também utilizado como código corporativo da garantia na CAIXA

>>>>>> + nome_garantia: Nome da garantia CAIXA

>>>>>> + indicador_fidejussoria: Indicador de garantia fidejussória. Em caso positivo será necessário indicar o dossiê de cliente que realiza o papel de fiduciante para essa garantia

>>>>>> + campos_formulario: Objeto utilizado para representar o campo de formulário vinculado a um processo fase

>>>>>>> + ordem_apresentacao:	Indica a seqüência de apresentação deste campo perante o formulário.

>>>>>>> + placeholder_campo:	Caso possua valor, determina um exemplo de valor a ser preenchido pelo usuário de forma exibir no campo quando este esta vazio como um atributo placeholder previsto no HTML.

>>>>>>> + tipo_campo: Indica o tipo de componente a ser utilizado na montagem do formulário para apresentação do campo.Ex: INPUT, RADIO, CHECK, etc Enum: CHECKBOX, COLOR, DATE, EMAIL, NUMBER, PASSWORD, RADIO, SELECT, SPAN, TEXT, TEXTAREA, TIME, URL, CNPJ, CPF, CPF_CNPJ, SELECT_SN, SELECT_VF, TELEFONE_DDD, CEP, HIDDEN, CEP_ONLINE, CEP_ONLINE_DEP

>>>>>>> + tamanho_minimo:	Caso possua valor, determina uma validação de quantidade mínima de caracteres a ser preenchida no campo do formulário.

>>>>>>> + lista_apresentacoes: Objeto utilizado para representar a forma de apresentação do campo de formulário no retorno as consultas realizadas no Processo sob a ótica Apoio ao Negócio.

>>>>>>>> + largura: Identifica o numero de colunas ocupadas na montagem da interface baseado no conceito do bootstrap. Este valor pode variar de 1 a 12.

>>>>>>>> + tipo_dispositivo: Indica o tipo resolução do dispositivo para renderização do campo com o número de colunas indicadas. Enum: W, L, M, H, X 

>>>>>>> + nome_campo: Nome do campo para fins de identificação programática perante o formulário.

>>>>>>> + tamanho_maximo:	Caso possua valor, determina uma validação de quantidade máxima de caracteres a ser preenchida no campo do formulário.

>>>>>>> + label: Label da campo a ser apresentado na interface para o usuário.

>>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulário.

>>>>>>> + obrigatorio: Indica se o campo deve ser apresentado na interface com restrição de obrigatoriedade no preenchimento. Essa obrigatoriedade deverá ser validada.

>>>>>>> + mascara_campo: Caso possua valor, determina a mascara a ser aplicada na entrada de dados pelo usuário.

>>>>>>> + id:	Identificador único do campo que representa a entrada de um formulário do processo.

>>>>>>> + opcoes_disponiveis: Objeto utilizado para representar uma opção de campo de formulário para os casos de elementos objetivos

>>>>>>>> + valor_opcao:Elemento que representa o valor da resposta. Conteúdo a ser utilizado no atributo value do HTML.

>>>>>>>> + descricao_opcao: Elemento que representa a descrição da resposta. Descrição visual apresentada no HTML.

>>>>>>> + nome_campo: Nome do campo para fins de identificação programática perante o formulário.

>>>>>>> + tamanho_maximo:	Caso possua valor, determina uma validação de quantidade máxima de caracteres a ser preenchida no campo do formulário.

>>>>>>> + label: Label da campo a ser apresentado na interface para o usuário.

>>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulário.

>>>>>>> + obrigatorio: Indica se o campo deve ser apresentado na interface com restrição de obrigatoriedade no preenchimento. Essa obrigatoriedade deverá ser validada.

>>>>>>> + mascara_campo:Caso possua valor, determina a mascara a ser aplicada na entrada de dados pelo usuário.

>>>>>>> + id:	Identificador único do campo que representa a entrada de um formulário do processo

>>>>>>> + opcoes_disponiveis: Objeto utilizado para representar uma opção de campo de formulário para os casos de elementos objetivos

>>>>>>>> + valor_opcao: Elemento que representa o valor da resposta. Conteúdo a ser utilizado no atributo value do HTML.

>>>>>>>> + descricao_opcao: Elemento que representa a descrição da resposta. Descrição visual apresentada no HTML.

>>>>>> + funcoes_documentais: Objeto utilizado para representar a função documental utilizado na definição do processo não contendo a lista de tipos de documentos associados

>>>>>>> + id: Identificador único da função documental.

>>>>>>> + nome: Nome descritivo da função documental.

>>>>>> + tipos_documento: Objeto utilizado para representar o tipo de documento relacionado ao vinculo de pessoas na definição do processo.

>>>>>>> + id:	Identificador único do tipo de documento.

>>>>>>> + nome: Nome descritivo do tipo de documento.

>>>>>>> + permite_reuso:	Valor que indica se o tipo de documento possibilita o reuso.

>>>>> + produtos_vinculados: Objeto utilizado para representar as definições do produto associados ao processo

>>>>>> + id:	Identificador único do produto

>>>>>> + codigo_operacao: Código de operação corporativo do produto CAIXA

>>>>>> + codigo_modalidade: Código da modalidade do do produto CAIXA. Este valor indica uma subdivisão do produto.

>>>>>> + nome: Nome do produto CAIXA

>>>>>> + campos_formulario: Objeto utilizado para representar o campo de formulário vinculado a um processo fase

>>>>>> + ordem_apresentacao: Indica a seqüência de apresentação deste campo perante o formulário.

>>>>>> + placeholder_campo: Caso possua valor, determina um exemplo de valor a ser preenchido pelo usuário de forma exibir no campo quando este esta vazio como um atributo placeholder previsto no HTML.

>>>>>> + tipo_campo: Indica o tipo de componente a ser utilizado na montagem do formulário para apresentação do campo.Ex: INPUT, RADIO, CHECK, etc Enum:  CHECKBOX, COLOR, DATE, EMAIL, NUMBER, PASSWORD, RADIO, SELECT, SPAN, TEXT, TEXTAREA, TIME, URL, CNPJ, CPF, CPF_CNPJ, SELECT_SN, SELECT_VF, TELEFONE_DDD, CEP, HIDDEN, CEP_ONLINE, CEP_ONLINE_DEP 

>>>>>> + tamanho_minimo: Caso possua valor, determina uma validação de quantidade mínima de caracteres a ser preenchida no campo do formulário.

>>>>>> + lista_apresentacoes: Objeto utilizado para representar a forma de apresentação do campo de formulário no retorno as consultas realizadas no Processo sob a ótica Apoio ao Negocio.

>>>>>>> + largura: Identifica o numero de colunas ocupadas na montagem da interface baseado no conceito do bootstrap. Este valor pode variar de 1 a 12.

>>>>>>> + tipo_dispositivo: Indica o tipo resolução do dispositivo para renderização do campo com o numero de colunas indicadas. Enum: L, M, H, X 

>>>>>> + nome_campo: Nome do campo para fins de identificação programática perante o formulário.

>>>>>> + tamanho_maximo: Caso possua valor, determina uma validação de quantidade máxima de caracteres a ser preenchida no campo do formulário.

>>>>>> + label: Label da campo a ser apresentado na interface para o usuário.

>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulário.

>>>>>> + obrigatorio: Indica se o campo deve ser apresentado na interface com restrição de obrigatoriedade no preenchimento. Essa obrigatoriedade deverá ser validada.

>>>>>> + mascara_campo: Caso possua valor, determina a mascara a ser aplicada na entrada de dados pelo usuário.

>>>>>> + id:	Identificador único do campo que representa a entrada de um formulário do processo

>>>>>> + opcoes_disponiveis: Objeto utilizado para representar uma opção de campo de formulário para os casos de elementos objetivos

>>>>>>> + valor_opcao: Elemento que representa o valor da resposta. Conteúdo a ser utilizado no atributo value do HTML.

>>>>>>> + descricao_opcao: Elemento que representa a descrição da resposta. Descrição visual apresentada.

>>>>>> +  elementos_conteudo: Objeto utilizado para representar o elemento de conteúdo vinculado a um produto ou a uma etapa do processo (fase).

>>>>>>> +  identificador_elemento: Identificador único do elemento de conteúdo esperado na definição de uma etapa do processo ou vinculação de um produto.

>>>>>>> +  identificador_elemento_vinculador: Identificador único do elemento de conteúdo vinculador do elemento de conteudo, viabilizando montar uma hierarquia (arvore) de elementos dependentes.

>>>>>>> + obrigatorio: Indica para se este elemento tem obrigatoriedade de carga na submissão do criação/atualização do dossiê de produto.

>>>>>>> + quantidade_obrigatorios: Nos casos em que o elemento de conteúdo trata-se um agrupador (pasta) indica quantos elementos mínimos são necessários daquele item.

>>>>>>> + label: Determina o nome do label a ser exibido nos casos de elementos agrupadores (pasta). Quando o elemento for folha, deverá utilizado o nome definido pelo objeto tipo de documento e este campo será nulo.

>>>>>>> + nome_elemento: Nome do campo utilizado para fins de identificação programática perante a interface na montagem da arvore de elementos conteúdos.

>>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir uma ação sobre este elemento de conteúdo, como por exemplo, ser apresentado ou ocultado na interface. Trata-se de uma AngularExpression.

>>>>>>> + tipo_documento: Objeto utilizado para representar o tipo de documento relacionado ao vinculo de pessoas na definição do processo.

>>>>>>>> + id:	Identificador único do tipo de documento.

>>>>>>>> + nome: Nome descritivo do tipo de documento.

>>>>>>>> + permite_reuso: Valor que indica se o tipo de documento possibilita o reuso.

>>>>>> +  garantias_vinculadas: Objeto utilizado para representar a garantia relacionada com o processo/produto

>>>>>>> +  id:	Identificador único da garantia a ser utilizada

>>>>>>> + codigo_bacen: Código de identificação da garantia junto ao BACEN, também utilizado como código corporativo da garantia na CAIXA

>>>>>>> + nome_garantia: Nome da garantia CAIXA

>>>>>>> + indicador_fidejussoria:	Indicador de garantia fidejussória. Em caso positivo será necessário indicar o dossiê de cliente que realiza o papel de fiduciante para essa garantia

>>>>>>> + campos_formulario:	Vide campos detalhados acima

>>>>>>> + funcoes_documentais:	Vide campos detalhados acima

>>>>>>> + tipos_documento:	Vide campos detalhados acima

>>>>> +  elementos_conteudo:	Objeto utilizado para representar o elemento de conteudo vinculado a um produto ou a uma etapa do processo (fase)

>>>>>> + identificador_elemento:	Identificador único do elemento de conteúdo esperado na definição de uma etapa do processo ou vinculação de um produto

>>>>>> + identificador_elemento_vinculador: Identificador único do elemento de conteúdo vinculador do elemento de conteúdo, viabilizando montar uma hierarquia (arvore) de elementos dependentes

>>>>>> + obrigatorio: Indica para se este elemento tem obrigatoriedade de carga na submissão do criação/atualização do dossiê de produto

>>>>>> + quantidade_obrigatorios: Nos casos em que o elemento de conteúdo trata-se um agrupador (pasta) indica quantos elementos mínimos são necessários daquele item

>>>>>> + label: Determina o nome do label a ser exibido nos casos de elementos agrupadores (pasta). Quando o elemento for folha, deverá utilizado o nome definido pelo objeto tipo de documento e este campo será nulo.

>>>>>> + nome_elemento: Nome do campo utilizado para fins de identificação programática perante a interface na montagem da arvore de elementos conteúdos.

>>>>>> + expressao_interface: Determina uma expressão a ser utilizada pela interface para definir uma ação sobre este elemento de conteúdo, como por exemplo, ser apresentado ou ocultado na interface. Trata-se de uma AngularExpression.

>>>>>> + tipo_documento: Objeto utilizado para representar o tipo de documento relacionado ao vinculo de pessoas na definição do processo.

>>>>>>> + id: Identificador único do tipo de documento.

>>>>>>> + nome: Nome descritivo do tipo de documento.

>>>>>>> + permite_reuso: Valor que indica se o tipo de documento possibilita o reuso.

>>>>> + unidades_autorizadas: Objeto utilizado para representar quais ações de execução estão definidas para unidade indicada no registro.

>>>>>> + unidade: CGC de identificação da unidade, examplo: 9999

>>>>> + tratamento_autorizados: Lista ações possíveis para manipulação do processo pela unidade identificada. Enum: CONSULTA_DOSSIE, TRATAR_DOSSIE, CRIAR_DOSSIE, PRIORIZAR_DOSSIE 

>>>>> + documentos_vinculo: Objeto utilizado para representar a estrutura de documentação definida para o vinculo determinado no processo.

>>>>>> + tipo_relacionamento: Identifica o tipo de relacionamento definido para o vinculo.

>>>>>> + obrigatorio: Indica para a interface se este elemento tem obrigatoriedade de carga.

>>>>>> + funcao_documental:	Vide campos detalhados acima

>>>>>> + tipo_documento: Vide campos detalhados acima

>>>>> + priorizado:	Indica que o processo esta priorizado e por isso a visão de captura de dossiês para tratamento será diferenciada.

>>>>> + tipo_pessoa: Indica o tipo de pessoa definido para o processo. F = Física | J = Juridica | A = Ambos: F, J, A

>>>>> + tratamento_seletivo: Indica que dossiê do processo pode ser capturado para tratamento de forma específica

>>>>> + avatar:	Identificação do nome ícone que representa o processo.

>>>>> + nome: Nome de identificação do processo.

>>>>> + gera_dossie: Indica se o processo pode gerar dossiê.

>>>>> + id:	Codigo de identificação do processo.

>>>>> + tipos_relacionamento: Objeto utilizado para o tipo de relacionamento possível de ser definido na vinculação de um dossiê de cliente ao dossiê de produto e os parâmetros que definem o comportamento esperado nessa inclusão.

>>>>>> + indica_sequencia: Indica se o tipo de relacionamento exige a identificação de sequenciamento do mesmo tipo de vinculo.

>>>>>> + indica_relacionado:	Indica se o tipo de relacionamento exige a identificação de um dossiê de cliente relacionado.

>>>>>> + indica_receita_pf: Indica se o tipo de relacionamento será criado para os socios pessoas fisicas indicados após a consulta de quadro societário junto a Receita.

>>>>>> + campos_formulario: Vide campos detalhados acima

>>>>>> + principal: Indica se o tipo de relacionamento tem caráter de atribuição imediata ao cliente contextualizado.

>>>>>> + indica_receita_pj: Indica se o tipo de relacionamento será criado para os sócios pessoas físicas indicados após a consulta de quadro societário junto a Receita.

>>>>>> + tipo_pessoa: Indica o tipo de pessoa passível de associação com o vinculo em referência. Enum: F, J, A 

>>>>>> + nome: Identifica o tipo de relacionamento definido para o vinculo.

>>>>>> + id: Identificador único do tipo de relacionamento definido para o vinculo.





