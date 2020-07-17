# SimtrWeb

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.4.9.

## Build e Publish do front-end

* Acesse a pasta 4-implementacao/simtr-web;

* Se ainda não houver baixado os módulos (node_modules), execute o comando "npm i" e aguarde a instalação destes;

* Para build do código, execute o comando "ng build --env=prod";
    - A flag --env indica qual arquivo environment deve ser utilizado, e o arquivo com sufixo prod contêm as configurações para produção;
        - Os arquivos environment ficam na pasta src/environments; 

* Após finalizado o build será gerada a pasta /dist com o conteúdo a ser publicado. Para publicar execute o script "./publish.sh";
    - Será solicitada a senha do servidor NginX onde está hospedado o front-end;

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
