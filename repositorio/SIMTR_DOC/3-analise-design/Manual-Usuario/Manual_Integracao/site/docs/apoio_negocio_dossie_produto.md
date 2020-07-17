
## Apoio ao Negocio - Dossiê Produto

### <span style="color:green">POST /negocio/v1/dossie-produto</span>
>>> + *Finalidade* : Realizar a inclusão de um dossiê de produto de acordo a definição do processo que rege a operação. Após a inclusão da operação o objeto descrito no item acima será retornado para o solicitante com a indicação do identificador gerado para a operação.

>>> + *Corpo da Mensagem* : 

>>>>> + **rascunho**: O sistema permite realizar a inclusão de uma operação ainda que todas as informações obrigatórias não estejam definidas. Isso permite que as informações sejam incluídas parcialmente e posteriormente complementadas para dar início as tratativas. 

>>>>> Para criar um dossiê em situação de rascunho enviar “true” para criar o dossiê já iniciando as tratativas da operação enviar “false”

>>>>> <u> IMPORTANTE </u>: Um dossiê de produto criado em modo rascunho não notifica nenhuma área e não inicia as contagens de SLA assim como não dispara a execução do processo junto a ferramento de BPM que realizará o acompanhamento do ciclo de vida da operação.

>>>>> + **processo_origem**: Identificador do processo responsável por definir toda a estrutura da operação. Esse identificador deverá ser obtido com a equipe de gestão do SIMTR, assim como toda a estrutura deste, como elementos de documentação obrigatórios, tipos de vínculos possíveis para pessoas, respostas necessárias a formulários, etc.

>>>>> + **elementos_conteudo**: Representa a lista de documentos definidos para a operação que são esperados de acordo com o processo definido. Necessario enviar cada documento com a respectiva identificação de qual elemento de conteúdo esta sendo atendido que pode ser referente ao processo global ou a etapa da operação. Além disso, o serviço realizará uma validação se a tipologia indicada no documento, é condizente com a definida para o elemento indicado.

>>>>> + **respostas_formulario**: Representa a lista de respostas esperadas para formulários definidos já na primeira fase do processo. Cada resposta deverá ser informada junto com o identificador do campo de formulário indicado.

>>>>>> + **resposta**: Deve ser preenchido apenas quando o campo de formulário estiver definido para uma resposta subjetiva. Esta resposta deve ser enviada apenas como um único texto.

>>>>>> + **opcoes_selecionadas**: Deve ser preenchido apenas quando o campo de formulário estiver definido para uma resposta objetiva. Esta resposta deve ser enviada como uma lista de opções, cada uma representada por um texto. A lista de opções pode ser obtida com a equipe de gestão do SIMTR.

>>>>>> <u> IMPORTANTE </u>: Apenas uma das opções de resposta deve ser utilizada, nunca as duas, ou seja, ou envia o atributo, ou envia o atributo opções_selecionadas.

>>>>> + **produtos_contratados**: Representa a lista de produtos que estão sendo vinculados a esta operação e para cada produto deverá ser informado os elementos abaixo:

>>>>>> + **id**: Deve ser informado o identificador do produto junto ao SIMTR. Caso esse campo seja informado ele terá prioridade frente ao par de atributos operacao/modalidade que não deverão ser informados.

>>>>>> + **operacao**: Deverá ser informado juntamente com o atributo modalidade e apenas se o atributo id não for informado. Representa a código principal do produto CAIXA.

>>>>>> + **modalidade**: Deverá ser informado juntamente com o atributo operacao e apenas se o atributo id não for informado. Representa a código o códigop de subproduto do produto CAIXA, a exemplo dos produtos BCD (opeacao 149) e modalidade de acordo com a linha de produtos (computadores, eletrodomésticos, etc) as operações que não possuem subprodutos, seão preenchidas com 0 mas devem ser confirmadas com a equipe de gestão do SIMTR.

>>>>>> + **elementos_conteudo**: Representa a lista de documentos definidos para o produto indicado que são esperados de acordo com o processo definido. Necessario enviar cada documento com a respectiva identificação de qual elemento de conteúdo esta sendo atendido. Além disso, o serviço realizará uma validação se a tipologia indicada no documento, é condizente com a definida para o elemento indicado.

>>>>>> + **respostas_formulario**: Representa a lista de respostas esperadas para formulários definidos para o produto indicado. Cada resposta deverá ser informada junto com o identificador do campo de formulário indicado e segue as mesmas regras que as definidas no bloco de respostas_formulario do processo.

>>>>> + **vinculos_pessoas**: Representa a lista de vínculos de pessoas associados com a operação. Todo dossiê deverá ter ao menos um vinculo de pessoa definido que será o dito vinculo principal da operação e seu tipo de relacionamento deverá respeitar essa indicação. Os atributos deverão ser preenchidos conforme abaixo:

>>>>>> + **dossie_cliente**: Identificador do dossiê de cliente a ser associado com a operação. O identificador do cliente pode ser obtido consumindo o serviço de consulta de dossiês de cliente já definido neste documento.

>>>>>> + **dossie_cliente_relacionado**: Alguns tipo de relacionamento indicam relacionamento entre as pessoas (ex: Socio, este cliente é sócio de quem?). Este atributo deve conter o identificador do dossiê cliente ao qual o identificador informado no atributo “dossie_cliente” esta relacionado.

>>>>>> Por exemplo, este identificador pode conter o código da pessoa jurídica ao qual o dossiê_cliente esta sendo vinculado como sócio.

>>>>>>  <u>IMPORTANTE </u>: Os identificadores informados neste campo deverão ter seu próprio registro vinculado a operação para que possam ser referenciados neste campo.

>>>>>> + **sequencia_titularidade**: Alguns tipos de relacionamento permitem associação de mais de uma pessoa com o mesmo tipo indicando sua sequencia (ex: Titular, este registro é o primeiro titular) Este atributo deve conter a indicação da sequencia ocupada pela pessoa informada no atributo “dossie_cliente”.

>>>>>> <u> IMPORTANTE </u>: Todos os registros que indicarem sequencia não poderam ter ausências na lista sequencial inicando de 1, por exemplo, três registros com indicação de sequencial devem ser os valores 1, 2, 3 respectivamente.

>>>>>> + **tipo_relacionamento**: Aqui deverá ser informado o tipo de relacionamento que o dossiê de cliente tem com esta operação. Os tipos de relacionamento são variáveis conforme cada processo e deve ser identificado junto aos gestores do SIMTR de acordo com o processo que será utilizado. O tipo de relacionamento indicado, também poderá definir respostas de formulário que serão necessárias assim como a lista de documentos esperados.

>>>>>> + **documentos_utilizados**: Quando o cliente indicado já possui documentos na base a serem reutilizados, é possível indica-los para reuso e essa indicação é realizada passando a lista de identificadores dos documentos junto ao SIMTR que estão associados ao cliente indicado.

>>>>>> + **documentos_novos**: Lista de documentos informados para operação que são previstos pelo tipo de relacionamento informado no processo mas não estão sendo reutilizados, seja porque ainda não existem associado ao dossiê do cliente, seja porque tratam-se de documentos efetivamente novos que estão sendo enviados. A lista de atributos segue as mesmas regras de submissão de documentos já definidos para outras estruturas.

>>>>>> + **respostas_formulario**: Representa a lista de respostas esperadas para formulários definidos para o tipo de vinculo indicado. Cada resposta deverá ser informada junto com o identificador do campo de formulário indicado e segue as mesmas regras que as definidas no bloco de respostas_formulario do processo.

>>>>> + **garantias_informadas**: Representa a lista de garantias que estão sendo vinculados a esta operação e para cada garantia deverá ser informado os elementos abaixo:

>>>>>> + **identificador_garantia**: Deve ser informado o identificador da garantia junto ao SIMTR que esta sendo ofertada na operação.

>>>>>> + **identificador_produto**: Deve ser informado o identificador do produto junto ao SIMTR que compõe essa operação e que a garantia esta cobrindo.

>>>>>> + **valor_garantia**: Deverá ser indicado o valor da garantia na operação em construção. O valor deverá ser defindo um numero decimal, não deve ser utilizado separador de milhares e deverá ser utilizado o ponto como separador de centavos.

>>>>>> + **clientes_avalistas**: Lista de identificadores de dossiês de clientes associados a garantia informada. Esse atributo é utilizado no caso de garantias fidejussórias que dependem do vinculo de uma pessoa associada. O identificador do cliente pode ser obtido consumindo o serviço de consulta de dossiês de cliente já definido neste documento.

>>>>>> + **documentos_utilizados**: Quando o atributo clientes_avalistas é preenchido e existem documentos a serem reutilizados que já fazem parte da base de dados do SIMTR, é possível indica-los para reuso e essa indicação é realizada passando a lista de identificadores dos documentos junto ao SIMTR que estão associados aos clientes informados.

>>>>>> + **documentos_novos**: Lista de documentos informados para associação a garantia que são previstos, mas não estão sendo reutilizados, seja porque ainda não existem associados aos dossiês de clientes, seja porque tratam-se de documentos efetivamente novos que estão sendo enviados. A lista de atributos segue as mesmas regras de submissão de documentos já definidos para outras estruturas.

>>>>>> + **respostas_formulario**: Representa a lista de respostas esperadas para formulários definidos para a garantia indicada. Cada resposta deverá ser informada junto com o identificador do campo de formulário indicado e segue as mesmas regras que as definidas no bloco de respostas_formulario do processo.

>>> + *Retorno* : Mesmo retorno apresentado no serviço de consulta da operação descrito no item acima.


### <span style="color:blue"> GET /negocio/v1/dossie-produto/{id}</span>
>>> + *Finalidade* : Realizar a consulta de um dossiê de produto com base no seu identificador. A obtenção do identificador acontece em outro serviço dedicado a inclusão da operação. Por este serviço o valor indicado para permissão de alteração de dados desse dossiê será retornado sempre como “false” indicando que não será permitida alteração de informação ou inclusão de documento no mesmo.

>>> + *Corpo da Mensagem* : Não se aplica

>>> + *Retorno* : 

>>>> + **id**: Identificador único do dossiê de produto utilizado na consulta e definido após sua criação.

>>>> + **alteracao**: Atributo utilizado para indicar se o dossiê de produto foi capturado em modo de alteração.

>>>> + **data_hora_finalizacao**: Representa a data e hora que o dossiê foi finalizado, caso a operação ainda esteja em andamento este valor será retornado como null.

>>>> + **situacao_atual**: Representa a atual situação em que o dossiê se encontra perante o ciclo de vida deste conforme definição acima.

>>>> + **data_hora_situacao_atual**: Indica a data e hora de atribuição da situação atual

>>>> + **processo_dossie**: Objeto que representa o processo indicado na geração do dossiê, e nele estão definidos os documentos referentes ao processo (através das instancias de documentos) e as todas as respostas de formulários que foram indicadas durante a manipulação do dossiê.

>>>>> + **instancias_documento**: Apresenta a lista de vínculos dos documentos esperados pela própria definição do processo com o dossiê de produto

>>>>> + **respostas_formulario**: Apresenta a lista de respostas de formuláio apresentados que estão vinculadas ao processo. 

>>>>> Para a estrutura de processo que define a estrutura da operação, não são associados campos de formulário, apenas para a estrutura de processo que representa a fase da operação. Logo essa lista será sempre vazia neste momento.

>>>> + **processo_fase**: Objeto que representa o processo definido na fase atual da operação, e nele estão definidos os documentos referentes a captura determinada pela etapa da operação (através das instancias de documentos) e as todas as respostas de formulários que foram indicadas durante a manipulação do dossiê.

>>>>> + **instancias_documento**: Apresenta a lista de vínculos dos documentos esperados pela própria definição do processo com o dossiê de produto.

>>>>> + **respostas_formulario*: Apresenta a lista de respostas de formuláio apresentados que estão vinculadas a esta etapa do processo.

>>>> + **unidades_tratamento**: Esse elemento representa a lista de unidades que estão definidas para realizar a manipulação/tratamento do dossi~e no momento atual. Se a lotação física ou administrativa do usuário que tentar executar alteração de informação no dossiê não estiver presente nesta lista, a ação será impedida causando um retorno sob código 403 que indica a proibição da ação.

>>>> + **historico_situacoes**: Este elemento representa a lista de situações atribuídas ao dossiê durante o seu ciclo de vida. Cada elemento representa uma situação atribuída com indicação da unidade responsável e data/hora da ação.

>>>> + **produtos_contratados**: Este bloco apresenta a lista de produtos associados ao dossiê (se houver) e as infomações vinculadas baseado no seu formulário específico (se houver) e os documentos associados (se houver)

>>>>> + **instancias_documento**: Apresenta a lista de vínculos dos documentos apresentados/reutilizados e esperados pela relação de um produto CAIXA com o dossiê de produto.

>>>>> + **respostas_formulario**: Apresenta a lista de respostas de formuláio esperadas que estão vinculadas a esta associação de produto CAIXA.

>>>> + **vinculos_pessoas**: Este bloco apresenta a lista de pessoas (físicas ou jurídicas) associados ao dossiê (necessário ao mínimo 1 pessoa,com exceção do dossiê salvo em situação de rascunho) e as infomações vinculadas baseado no seu formulário específico (se houver) e os documentos associados (se houver)

>>>>> + **instancias_documento**: Apresenta a lista de vínculos dos documentos apresentados/reutilizados e esperados pela associação de uma pessoa sob um determinado tipo de vinculo (forma de relacionamento com esta operação junto ao SIMTR).

>>>>> + **respostas_formulario**: Apresenta a lista de respostas de formuláio esperados pela associação de uma pessoa sob um determinado tipo de vinculo.

>>>> + **garantias_informadas**: Este bloco representa a lista de garantias associadas ao dossiê (se houver) e as informações vinculadas baseada no seu formulário específico (se houver) e os documentos associados (se houver)

>>>>> + **instancias_documento**: Apresenta a lista de vínculos dos documentos apresentados/reutilizados e esperados pela associação de uma garantia junto ao SIMTR.

>>>>> + **respostas_formulario**: Apresenta a lista de respostas de formuláio esperados pela associação de uma garantia com a operação junto ao SIMTR.

>>>> + **instancias_documentos**: O dossiê de produto permite a associação de documentos associadas as seguintes estruturas de uma operação:

>>>>> + Estrutura global do processo;

>>>>> + Fases da operação;

>>>>> + Vinculo de Pessoas;

>>>>> + Vinculo de Produto;

>>>>> + Vinculo de Garantias;

>>>>> Este bloco apresenta de forma global a lista de todos os documentos utilizados no dossiê até o momento. Apesar deste bloco, cada uma das estruturas acima apresenta um bloco instancias de documentos que contém apenas os documentos apresentados a seu próprio contexto.

>>>>> OBS: Este bloco existe, pois, essas estruturas também estão presentes nas fases do processo, e o dossiê retorna apenas a fase atual, não apresentando as estruturas passadas e futuras.

>>>> + **respostas_fomulario**: O dossiê de produto permite a associação de perguntas dinâmicas associadas as seguintes estruturas de uma operação:

>>>>> + Fases da operação;

>>>>> + Vinculo de Pessoas;

>>>>> + Vinculo de Produto;

>>>>> + Vinculo de Garantias;

>>>>>  Este bloco apresenta de forma global a lista de todas as respostas apresentadas no dossiê até o momento. Apesar deste bloco, cada uma das estruturas acima apresenta um bloco de respostas de formulário que contem apenas as respostas apresentadas a seu próprio contexto.

>>>>> OBS: Este bloco existe, pois, essas estruturas também estão presentes nas fases do processo, e o dossiê retorna apenas a fase atual, não apresentando as estruturas passadas e futuras.

>>>> + **verificacoes**: Este bloco a lista de verificações (avaliações de conformidade) realizadas no dossiê, indicando:

>>>>> + A fase em que a verficiação foi realizada;

>>>>> + A instância do documento que foi avaliada;

>>>>>> + Caso seja uma avaliação “não documental” este elemento não será apresentado;

>>>>> + A data/hora de verificação

>>>>> + A unidade responsável pela análise;

>>>>> + A indicação de análise realizada

>>>>>> + Alguns documentos podem não ser alvo de verificação;

>>>>> + Indicação de aprovação na verificação

>>>>> + Lista de pareceres sobre cada apontamento analisado na verificação. Sob cada elemento existe a indicação de aprovação ou não daquele apontamento a orientação padrão sob a ação que deve ser realizada para correção e comentário apresentado pelo analista sob a situação.



### <span style="color:green"> POST /negocio/v1/dossie-produto/{id}</span>
>>> + *Finalidade* : Realizar a consulta de um dossiê de produto com base no seu identificador. A obtenção do identificador acontece em outro serviço dedicado a inclusão da operação. Por este serviço é realizada uma com a tentativa de captura para manipulação de dados e deverá ser observado o valor indicado para permissão de alteração de dados desse dossiê que só poderá ocorrer se a operação for retornada como “true” indicando que não será permitida alteração de informação ou inclusão de documento pela mesma matricula responsável pela captura.

>>> + *Corpo da Mensagem* : Não se aplica

>>> + *Retorno* : Mesmo retorno apresentado no serviço de consulta da operação descrito no item acima com possibilidade de apresentar o atributo “alteracao” com o valor “true”.


### <span style="color:#50e3c2"> PATCH /negocio/v1/dossie-produto/{id}</span>
>>> + *Finalidade* : Atualização de informações de um Dossiê de Produto.

>>> + *Corpo da Mensagem* : 

>>> + *Retorno* : 



