<html>
<head>
<!-- demo_area_chart.dsp -->
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script language=javascript>
	var rowCount=0;
	var oddRow=false;
	function writeTdClassTag(){
		if (oddRow){
			document.write ('<td class=oddrow-l>');
		}
		else {
			document.write('<td class=evenrow-l>')
		}
	}
	function newRow(){
		rowCount++;
		if ((rowCount % 2) >0){
			oddRow=true;
		}
		else{
			oddRow=false;
		}
	}
	function resetRowCount(){
		rowCount=0;
	}

</script>
<LINK REL="stylesheet" TYPE="text/css" HREF="styles/webMethods.css">
<SCRIPT TYPE="text/javascript;charset=UTF-8" SRC="scripts/webMethods.js"></SCRIPT>
<title>Monitor</title>
</head>
<body>
<form name="alerts" action="demo_area_chart.dsp" method="POST">

<table width="100%">
	<td class="menusection-Adapters">SimpleChart &gt; Demo Area Chart</td>
</table>

%invoke SimpleChart.demo:createAreaChart%
	<table width="100%">

	%ifvar message%
		<tr><td class="message">
			%value message%
		</td>
		</tr>
	%endif%
	<br>
	  <td class=heading colspan=9>Demo Area Chart</td>
	<tr>
	<td valign=top>
	</table>

		<table width="100%">
		<IMG src=%value chart_file% alt="Demo Area Chart">
		</table>

	<table width="100%">
	<br>
	<tr>
	<tr>
	<td class=heading colspan=12>Demo Area Chart</td>
	<tr>
	<td valign=top>
	</table>
	<script>
		resetRowCount();
	</script>

	%ifvar demo_results -isnull%
	    <table width="100%">
	        <td class=subheading>There are no stats available.</td>
	    </table>
	%else%
		<table width=100%>
		<td class=subheading width="20%">Date</td>
		<td class=subheading width="10%">Total</td>
		<td class=subheading width="20%">Average Time (sec)</td>
		<td class=subheading width="25%">Min Time (sec)</td>
		<td class=subheading width="25%">Max Time (sec)</td>
		
		<tr>
		%loop demo_results%
			<script>
				newRow();
				writeTdClassTag();
			</script>
				%ifvar CATEGORYDATE%
					%value CATEGORYDATE%
				%else%
					<center>-</center>
				%endif%
			<script>
				writeTdClassTag();
			</script>
				%ifvar TOTAL%
					%value TOTAL%
				%else%
					<center>N/A</center>
				%endif%
			<script>
				writeTdClassTag();
			</script>
				%ifvar AVERAGETIME%
					%value AVERAGETIME%
				%else%
					<center>N/A</center>
				%endif%
			<script>
				writeTdClassTag();
			</script>
				%ifvar MINTIME%
					%value MINTIME%
				%else%
					<center>N/A</center>
				%endif%
			<script>
				writeTdClassTag();
			</script>
				%ifvar MAXTIME%
					%value MAXTIME%
				%else%
					<center>N/A</center>
				%endif%
		%loopsep <tr>%
		%endloop%
		</table>
	%endif%
	%onerror%
	<table width="100%">
          <TR><TD colspan="7">&nbsp;</TD></TR>
          <TR><TD class="message" colspan=7>%value errorMessage%</TD></TR>
          <TR><TD colspan="7">&nbsp;</TD></TR></table>
%endinvoke%
</form>
</body>
</html>
