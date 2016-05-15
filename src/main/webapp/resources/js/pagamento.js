
function retornoProcessamento(xhr, status, args) {
	console.log(args.erro);
	console.log(args.messagem);
	
	/*if(args.erro){
		alert("Falha no processamento do pedido.\n erro: " + args.codigo)
	}*/
}

function handleSession(xhr, status, args) {
	
	console.log(args.sessionId);
	PagSeguroDirectPayment.setSessionId(args.sessionId);
	console.log(PagSeguroDirectPayment.getSenderHash());
}

function autorizar(xhr, status, args) {
	
	console.log(args.autorizar);
	if(args.autorizar != true) {
		console.log("validando...");
		return false
	}	
	
	console.log("autorizando...");
	console.log(args.autorizar);
	console.log($('#numeroCartao').val());
	console.log($('#cvv').val());
	console.log($('#nome').val());
	console.log($('#validade').val());
	console.log($('#basic').val());
	
	if(args.tipo == "boleto") {
		processar([{name: 'senderHash', value: PagSeguroDirectPayment.getSenderHash()}]);
	}
	else {
		
		var param = {
			cardNumber: $('#numeroCartao').val(),
		    cvv: $('#cvv').val(),
		    brand: $('#basic').val().toLowerCase(),
		    expirationMonth: $('#validade').val().substring(0, 2),
		    expirationYear: $('#validade').val().substring(3, 7),
		    success: function(response) {
		    	console.log(response);
		        var param = response['card']['token'];
		        console.log(param);
		    	//processar({name1:$('#token').val(), name2:'value2'});
		    	processar([
		           {name: 'token', value: param},
		           {name: 'senderHash', value: PagSeguroDirectPayment.getSenderHash()}
		    	]);
		    	//$("#finalizar").click();
		        //$("#token").attr('type','text');
		    },
		    error: function(response) {
		    	console.log(response);
		    }
		}
		
		PagSeguroDirectPayment.createCardToken(param);
	}
}