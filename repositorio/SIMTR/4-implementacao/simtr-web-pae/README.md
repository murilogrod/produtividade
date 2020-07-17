# SimtrWeb

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.4.9.

## Build e Publish do front-end

* Acesse a pasta 4-implementacao/simtr-web;

* Se ainda não houver baixado os módulos (node_modules), execute o comando "npm i" e aguarde a instalação destes;

* Para build do código, execute o comando "ng build --prod";

* Após finalizado o build, será gerada a pasta /dist com o conteúdo a ser publicado.

* Para publicar, execute o comando abaixo:
	scp -p -r dist/* <usuario>@<url_servidor>:<diretorio_www>
	- <usuario> é o usuário do servidor com acesso ao diretório onde serão salvos os arquivos;
	- <url_servidor> pode ser a URL do servidor ou o seu IP;
	- <diretorio_www> é o diretório do servidor onde serão salvos os arquivos (normalmente /var/www/simtr-pae/);
    - Ao executar o comando será solicitada a senha do usuário do servidor pra onde se está enviando os arquivos;
	
	
	
	
## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
