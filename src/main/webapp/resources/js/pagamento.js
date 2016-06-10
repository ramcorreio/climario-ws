function retornoProcessamento(xhr, status, args) {
	console.log(args.erro);
	console.log(args.messagem);
	console.log(args.link);
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
			processarPagamentos([{name: 'paymentMethods', value: JSON.stringify(response.paymentMethods)}]);
			
		},
		error: function(response) {
			//tratamento do erro
			console.log("error....");
			console.log(response);
		},
		complete: function(response) {
			console.log("complete....");
			//console.log(response);
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
		
		var params = {
			cardNumber: $('#numeroCartao').val(),
		    cvv: $('#cvv').val(),
		    brand: $('#basic').val().toLowerCase(),
		    expirationMonth: $('#validade').val().substring(0, 2),
		    expirationYear: $('#validade').val().substring(3, 7),
		    success: function(response) {
		    	console.log(response);
		        var param = response['card']['token'];
		        console.log("token: " + param);
		    	processar([
		           {name: 'token', value: param},
		           {name: 'senderHash', value: PagSeguroDirectPayment.getSenderHash()},
		           {name: 'cpfCnpjHolder', value: $('#cpfCnpjHolder').val()},
		           {name: 'nomeHolder', value: $('#nome').val()}
		    	]);
		    },
		    error: function(response) {
		    	console.log(response);
		    }
		}
		
		PagSeguroDirectPayment.createCardToken(params);
	}
}