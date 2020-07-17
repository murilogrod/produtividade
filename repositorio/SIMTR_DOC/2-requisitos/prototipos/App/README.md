Pr�-requisitos:
- Node.JS (download em https://nodejs.org/)
- Visual Studio Code (download em https://code.visualstudio.com/download)
- Ionic (instalar pelo comando "npm install -g ionic cordova")



Passos:
1) Abrir o projeto SIMTR-node-server e execut�-lo com a linha de comando abaixo:
	node server.js

2) Abrir o projeto app-simtr, editar o arquivo environment.ts na pasta app-simtr/src/environments, colocando o IP atual da m�quina.
3) Executar o projeto app-simtr, atrav�s da linha de comando abaixo:
	ionic serve --devapp

	Obs.: para visualizar o projeto no smartphone, tanto a m�quina que est� executando o projeto quanto o smartphone dever�o estar numa mesma rede Wi-Fi, e o smartphone iniciar� a execu��o dentro do app "Ionic DevApp" que est� dispon�vel nas stores da Apple e Google.