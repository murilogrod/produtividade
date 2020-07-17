

## Apoio ao Negocio - Dossiê Cliente

### <span style="color:blue">GET /negocio/v1/dossie-cliente/{id}</span>
>>> + *Finalidade* : Captura o dossiê de cliente baseado no identificador único do SIMTR.

>>> + *Corpo da Mensagem* : Não se aplica

>>> + *Retorno* : 

>>>> + documentos: Lista de metainformações dos documentos vinculados ao cliente. Nesta consulta não será retornado o binário de nenhum documento neste momento. As informações que destacamos para este objeto são as seguintes:

>>>>> + codigo_ged: Identificador do documento junto ao GED (SIECM) que deverá ser utilizado para capturar o binário

>>>>> + data_hora_validade: Data e Hora definidas para viabilizar o reuso do documento. Caso não estea definida, ou seja, superior ao momento atual, o documento possui validade para utilização em novos negócios. Caso contrario, este documento não poderá mais ser reutilizado, e deverá ser capturado outro documento, caso não exista nenhum de mesma tipologia com validade vinculado ao dossiê do cliente.

>>>> + produtos_habilitados: Esta informação trata da lista de produtos que estão previamente habilitados no modelo do dossiê digital para o cliente consultado

>>>> + dossies_produto: Contemplam as operações (produtos ou serviços) relacionadas ao cliente consultado que foram transitadas pelo SIMTR. Importante observar que não se trata dos produtos constrados pelo cliente junto a CAIXA, essas informações, caso desejadas, deverão ser obtidas junto aos sistemas comerciais



### <span style="color:blue">GET /negocio/v1/dossie-cliente/cpf/{cpf}</span>
>>> + *Finalidade* : Captura o dossiê de cliente baseado no CPF do mesmo.

>>> + *Corpo da Mensagem* : Não se aplica.

>>> + *Retorno* : O retorno deste serviço é o mesmo apresentado na consulta por ID.



### <span style="color:blue">GET /negocio/v1/dossie-cliente/cnpj/{cnpj}</span>
>>> + *Finalidade* : Captura o dossiê de cliente baseado no CNPJ do mesmo.

>>> + *Corpo da Mensagem* : Não se aplica

>>> + *Retorno* : O retorno deste serviço é o mesmo apresentado na consulta por ID.



### <span style="color:green"> POST /negocio/v1/dossie-cliente/{id}/documento </span>
>>> + *Finalidade* : Inclui um documento encaminhados para armazenamento em um Dossiê de Cliente identificado pelo identificador único do mesmo junto ao SIMTR. Importante observar que se já existir um documento valido para novos negócios 

>>> + *Corpo da Mensagem* : 

>>>> + codigo_integracao: Informação fornecida pela GEBAN utilizada para identificar o canal de comunicação (informação depreciada pois o canal será identificado pelo seu clienteID do SSO

>>>> + tipo_documento: Identificador do tipo de documento, podendo ser obtido através do consumo do serviço: GET /cadastro/v1/tipo-documento

>>>> + origem_documento: De forma a atender necessidades definidas no AD238 é necessario identificar a origem da midia em que o documento foi obtido podendo assumir:

>>>>> + A = Cópia Autenticada Administrativamente

>>>>> + C = Cópia Autenticada em Cartório

>>>>> + O = Original (Documento NatuDigital)

>>>>> + S = Cópia Simples

>>>> + mimetype: Indicação do tipo de binario encaminhado utilizado para realizer o armazenamento de forma mais otimizada e viabilizar a manipulação do mesmo de forma adequada. Pode assumir um dos valores a seguir:

>>>>> + image/jpg, image/jpeg, application/pdf, image/png

>>>> + binario: Conteudo do documento propriamente dito Deve ser enviada uma String no formato Base64

>>> + *Retorno* : 

>>> + Caso a inclusão seja bem suceida, será retornado um código HTTP 204 (No Content) indicando que o documento foi armazenado e vinculado ao dossi~e do cliente com sucesso.


### <span style="color:blue">GET /negocio/v1/dossie-cliente/{id}/dados-declarados</span>
>>> + *Finalidade* : 

>>> + *Corpo da Mensagem* : Não se aplica

>>> + *Retorno* : 



### <span style="color:green"> POST /negocio/v1/dossie-cliente/{id}/cadastro-caixa </span>
>>> + *Finalidade* : 

>>> + *Corpo da Mensagem* : 

>>> + *Retorno* : 




### <span style="color:green"> POST /negocio/v1/dossie-cliente/{id}/nivel-documental </span>
>>> + *Finalidade* : 

>>> + *Corpo da Mensagem* : 

>>> + *Retorno* : 




