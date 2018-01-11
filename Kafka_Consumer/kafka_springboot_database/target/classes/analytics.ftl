<!DOCTYPE html>
<html lang="en">
<head>
<title>ANALYTICS</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body style="margin:20px">
	<div class="container-fluid">
	<div >
	<H1 class="text-center bg-primary text-white" > Integrated Customer Insight Platform</H1> <BR>
	</div>
		<div class="row">
		
			<div class="col-lg-3" style="text-align: center;">
				<h4></h4>
			</div>
			<div class="col-lg-9">
				<table class="table table-bordered" style="margin: 10px">
					<tbody>
						<tr>
							<td style="width: 50%;">
								<h4 class="text-center">EVENT DATA</h4>
							</td>
							<td style="width: 25%;">
								<h4 class="text-center">ANALYTICS</h4>
							</td>
							<td style="width: 25%;">
								<h4 class="text-center">DESTINATION</h4>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row" style="border-top: 0.5px solid grey;border-left: 0.5px solid grey">
			<div class="col-lg-3" >
				<h4 class="text-center">LOGS</h4>
			</div>
			<div class="col-lg-9" style="border-right: 0.5px solid grey">
				<table class="table table-bordered" style="margin: 10px">
					<tbody>
						<#list logEntities as item>
						<tr class="info">
							<td style="width: 50%;">CustomerId: ${item.customerId} - Message: ${item.message}</td>
							<td style="width: 25%;">${item.analytics}</td>
							<td style="width: 25%;">${item.destination}</td>
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row" style="border-top: 0.5px solid grey;border-left: 0.5px solid grey">
			<div class="col-lg-3" style="text-align: center;">
				<h4>SMS</h4>
			</div>
			<div class="col-lg-9" style="border-right: 0.5px solid grey">
				<table class="table table-bordered" style="margin: 10px">
					<tbody>
						<#list smsEntities as item>
						<tr class="danger">
							<td style="width: 50%;">CustomerId: ${item.customerId} - Message: ${item.message}</td>
							<td style="width: 25%;">${item.analytics}</td>
							<td style="width: 25%;">${item.destination}</td>
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>

		<div class="row" style="border-top: 0.5px solid grey; border-bottom: 0.5px solid grey;border-left: 0.5px solid grey">
			<div class="col-lg-3" style="text-align: center;">
				<h4>EMAIL</h4>
			</div>
			<div class="col-lg-9" style="border-right: 0.5px solid grey">
				<table class="table table-bordered" style="margin: 10px">
					<tbody>
						<#list emailEntities as item>
						<tr class="success">
							<td style="width: 50%;">CustomerId: ${item.customerId} - Message: ${item.message}</td>
							<td style="width: 25%;">${item.analytics}</td>
							<td style="width: 25%;">${item.destination}</td>
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
			
		</div>
		<br>
<button type="button" class="btn btn-primary center-block" onClick="location.href='http://localhost:9080/kafka/analytics/results'">Refresh</button> 
	</div>
	
	<script>
	
	</script>
</body>
</html>