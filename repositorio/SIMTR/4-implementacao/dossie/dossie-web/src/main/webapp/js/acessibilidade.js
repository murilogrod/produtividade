﻿'use strict';

if (!proporcaoFonte) var proporcaoFonte = 0;

function adequacaoAcessibilidade(){
	for (var i = 1; i <= 6; i++) {
		var sizeh = $("h" + i).css('font-size');

		if (sizeh) {
			sizeh = sizeh.replace('px', '');
			sizeh = parseInt(sizeh) + 2 * proporcaoFonte;
			$("h" + i).css("cssText", "font-size : " + sizeh + "px");
		}
	}
	var size = $(".btn-group-sm>.btn, .btn-sm").css('font-size');
	if (size){
		size = size.replace('px', '');
		size = parseInt(size) + 2 * proporcaoFonte;
		$(".btn-group-sm>.btn, .btn-sm").css("cssText", "font-size : " + size + "px");
	}
	
	var sizeFC = $(".form-control").css('font-size');
	if (sizeFC){ 
		sizeFC = sizeFC.replace('px', '');
		sizeFC = parseInt(sizeFC) + 2 * proporcaoFonte;
		$(".form-control").css("cssText", "font-size : " + sizeFC + "px");
	}

	var sizeAce = $(".theme-header-acessibilidade").css("height");
	sizeAce = sizeAce.replace('px', '');
	sizeAce = parseInt(sizeAce) + 2 * proporcaoFonte;
	$(".theme-header-acessibilidade").css("cssText", "height: " + sizeAce + "px");
}

function alterarFonte(valor) {
	var size = $("body").css('font-size');
	var sizeFC = $(".form-control").css('font-size');
	var sizeAce = $(".theme-header-acessibilidade").css("height");
	sizeFC = sizeFC ? sizeFC.replace('px', '') : 10;
	sizeAce = sizeAce.replace('px', '');
	
	if (valor && proporcaoFonte <= 3) {
		proporcaoFonte++;
		size = parseInt(size) + 2;
		sizeFC = parseInt(sizeFC) + 2;
		
		for (var i = 1; i <= 6; i++) {
			var sizeh = $("h" + i).css('font-size');
			if (sizeh) {
				sizeh = sizeh.replace('px', '');
				sizeh = parseInt(sizeh) + 2;
				$("h" + i).css("cssText", "font-size : " + sizeh + "px");
			}
		}
		$(".btn-group-sm>.btn, .btn-sm").css("cssText", "font-size : " + size + "px");
		$(".form-control").css("cssText", "font-size : " + sizeFC + "px");
		$("body").css("cssText", "font-size: " + size + "px !important");
		$(".theme-header-acessibilidade").css("cssText", "height: " + (parseInt(sizeAce) + 2) + "px");
	} else if (proporcaoFonte > 0 && !valor) {
		proporcaoFonte--;
		size = parseInt(size) - 2;
		sizeFC = parseInt(sizeFC) - 2;

		for (var i = 1; i <= 6; i++) {
			var sizeh = $("h" + i).css('font-size');
			if (sizeh) {
				sizeh = sizeh.replace('px', '');
				sizeh = parseInt(sizeh) - 2;
				$("h" + i).css("cssText", "font-size : " + sizeh + "px");
			}
		}
		$(".btn-group-sm>.btn, .btn-sm").css("cssText", "font-size : " + size + "px");
		$(".form-control").css("cssText", "font-size : " + sizeFC + "px");
		$("body").css("cssText", "font-size: " + size + "px !important");
		$(".theme-header-acessibilidade").css("cssText", "height: " + (parseInt(sizeAce) - 2) + "px");
	}
}

function controlaMenu(){	
	var teste = $(".tabbar.show-tabbar").children("li");
	var num = teste.length / 2;
	
	for (var i=0; i < teste.length; i++){
		if (num > i){
			if (teste[i].children[1]) teste[i].children[1].classList.add("submenu-right");
		} else {
			if (teste[i].children[1]) teste[i].children[1].classList.add("submenu-left");
		}
	}
}