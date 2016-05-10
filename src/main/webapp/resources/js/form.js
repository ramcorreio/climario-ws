$(document).ready(function(){
	$(".maskphone").mask("(99)9999-9999?9");
	
	//ATIVACAO COMBOBOX
	if($("form select.hidden").length == 0){
		$("form").styleForm();		
	}
	
});