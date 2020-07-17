select 
	tb002.nu_dossie_produto, 
	tb013.ts_inclusao, 
	tb002.co_matricula_priorizado, 
	tb002.nu_unidade_priorizado, 
	tb002.nu_peso_prioridade, 
	tb013.nu_situacao_dossie, 
	tb012.nu_tipo_situacao_dossie, 
	tb018.nu_dossie_produto, 
	tb018.nu_unidade, 
	tb020_dossie.nu_processo as nu_processo_dossie, 
	tb020_fase.nu_processo as nu_processo_fase, 
	tb002.ic_canal_caixa, 
	tb002.ts_finalizado, 
	tb002.nu_unidade_criacao
from mtrsm001.mtrtb002_dossie_produto tb002 
left outer join mtrsm001.mtrtb013_situacao_dossie tb013 on tb002.nu_dossie_produto = tb013.nu_dossie_produto 
left outer join mtrsm001.mtrtb012_tipo_situacao_dossie tb012 on tb013.nu_tipo_situacao_dossie = tb012.nu_tipo_situacao_dossie 
left outer join mtrsm001.mtrtb018_unidade_tratamento tb018 on tb002.nu_dossie_produto = tb018.nu_dossie_produto 
left outer join mtrsm001.mtrtb020_processo tb020_dossie on tb002.nu_processo_dossie = tb020_dossie.nu_processo 
left outer join mtrsm001.mtrtb020_processo tb020_fase on tb002.nu_processo_fase = tb020_fase.nu_processo 
where (tb018.nu_unidade in (5402 , 5402)) 
	and (tb002.ts_finalizado is null) 
	and (tb020_dossie.nu_processo in (1,3,10,11,12,14,15,9,16,18,21,22) or tb020_fase.nu_processo in (1,3,10,11,12,14,15,9,16,18,21,22)) 
	and tb013.nu_tipo_situacao_dossie = 3 
	and tb013.nu_situacao_dossie = (
		select 
			tb013_2.nu_situacao_dossie 
		from mtrsm001.mtrtb013_situacao_dossie tb013_2 
		where tb013_2.nu_dossie_produto = tb002.nu_dossie_produto 
			and tb013_2.ts_inclusao = (
				select max(tb013_3.ts_inclusao) 
				from mtrsm001.mtrtb013_situacao_dossie tb013_3 
				where tb013_3.nu_dossie_produto=tb002.nu_dossie_produto
			)
	) 
	order by tb002.nu_unidade_priorizado DESC nulls last, tb013.ts_inclusao ASC