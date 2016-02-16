<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/spring.tld" prefix="spring"  %> 
<%@ taglib uri="/WEB-INF/tld/spring-form.tld" prefix="form"  %> 

<%

 

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><tiles:insertAttribute name="title" ignore="true" />
		</title>
		 
		<base href="<%= basePath %>" />
		<script type="text/javascript"  src="resources/js/jquery-1.10.2.min.js"></script>
 		<script type="text/javascript"  src="resources/js/json.min.js"></script>
                <script type="text/javascript"  src="resources/js/stomp.js"></script>
                <script type="text/javascript"  src="resources/js/knockout-3.0.0.js"></script>
                <script type="text/javascript"  src="resources/js/sockjs-0.3.4.min.js"></script>
		 
		

	</head>
	<body>
	 

	 
		 
		 
			<div id="innerBody">
			<tiles:insertAttribute name="body" />  
			</div>
	 
	 
	</body>
	
</html>
