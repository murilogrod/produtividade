Pré-requisitos:
- Node.JS (download em https://nodejs.org/)
- Visual Studio Code (download em https://code.visualstudio.com/download)
- Ionic (instalar pelo comando "npm install -g ionic cordova")



Passos:
1) Abrir o projeto SIMTR-node-server e executá-lo com a linha de comando abaixo:
	node server.js

2) Abrir o projeto app-simtr, editar o arquivo environment.ts na pasta app-simtr/src/environments, colocando o IP atual da máquina.
3) Executar o projeto app-simtr, através da linha de comando abaixo:
	ionic serve --devapp

	Obs.: para visualizar o projeto no smartphone, tanto a máquina que está executando o projeto quanto o smartphone deverão estar numa mesma rede Wi-Fi, e o smartphone iniciará a execução dentro do app "Ionic DevApp" que está disponível nas stores da Apple e Google.