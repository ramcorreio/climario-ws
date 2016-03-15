<%@ page isErrorPage="true" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        
        <title>Erro ao carregar a página</title>
        
        <%-- <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.5/css/bootstrap.min.css"> --%>
        <%-- <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/error.css" /> --%>
		
    </head>
    <body style="background: #23262b;"> 
    
        <div id="cl-wrapper" class="error-container">
			<div class="page-error">
				<h1 class="number text-center">${pageContext.errorData.statusCode}</h1>
				<h2 class="description text-center">${description}</h2>
				<h3 class="text-center">${subDescription}</h3>
			</div>
			<div class="text-center copy">&copy; 2016 <a href="http://www.climario.com.br/" target="_blank">Clima Rio</a></div>	
		</div>
    </body>
</html>