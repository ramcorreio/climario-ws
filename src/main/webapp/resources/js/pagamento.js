function opcoes() {
	/*PagSeguroDirectPayment.getPaymentMethods({
		2. amount: {valor da transação}
		3. success: {função de callback para chamadas bem sucedidas},
		4. error: {função de callback para chamadas que falharam},
		5. complete: {função de callback para todas chamadas}
		6. });*/
}


function retornoProcessamento(xhr, status, args) {
	console.log(args.erro);
	console.log(args.messagem);
	console.log(args.link);
	
	/*if(args.erro){
		alert("Falha no processamento do pedido.\n erro: " + args.codigo)
	}*/
}

function handleSession(xhr, status, args) {
	
	console.log(args.sessionId);
	PagSeguroDirectPayment.setSessionId(args.sessionId);
	console.log(PagSeguroDirectPayment.getSenderHash());
	PagSeguroDirectPayment.getPaymentMethods({
		amount: args.valorTotal,
		success: function(response) {
			//meios de pagamento disponíveis
			console.log("success....");
			//console.log(response.paymentMethods);
			//console.log(JSON.stringify(response.paymentMethods));
//			processarPagamentos([{name: 'paymentMethods', value: [{"name": "teste", "display":"Teste"}, {"name": "teste1", "display":"Teste 1"}]}]);
			processarPagamentos([{name: 'paymentMethods', value: JSON.stringify(response.paymentMethods)}]);
			
		},
		error: function(response) {
			//tratamento do erro
			console.log("error....");
			console.log(response);
		},
		complete: function(response) {
			console.log("complete....");
			console.log(response);
			//tratamento comum para todas chamadas
		}
	});
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
	
	if(args.tipo == "BOLETO") {
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