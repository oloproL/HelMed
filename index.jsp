<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.*"%>
<%@page import="com.unir.helmed.JSONParser"%>
<%@page import="com.unir.helmed.Category"%>
<%@page import="org.json.JSONArray"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>HelMed</title>
	<link rel="stylesheet" href="styles.css">
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script language="javascript" src="index.js" type="text/javascript"></script>
	
</head>
<body>
	<div class="container">
		<div class="title"><img src="logo.png"></div>
		<div class="dashboard">
		<hr>
		<div id="instructions" class="instructions">
			<table class="instuctionsTable">
			<tr><td>Select the symptom category for the disease you are looking for.</td></tr>
			<tr><td>Select one symptom and then select a value for this symptom.</td></tr>
			<tr><td>Then you will see the possible diagnoses for your selection</td></tr>
			<tr><td>If you choose one disease, you will see information about that and some drugs for its treatment</td></tr>
			</table>
		</div>
		<form name="myForm" id="myForm" enctype="text/plain"> 
		<input type="hidden" name="categoryIndex" id="categoryIndex">
		<input type="hidden" name="symptomIndex" id="symptomIndex">
		<input type="hidden" name="valueIndex" id="valueIndex">
		<table>
			<tr>
				<td colspan="1" width="100px" align="right"><label for="category">Category : </label></td>
				<td colspan="1"><select id="category" name="category" style="width:100%;" onChange="getSymptoms(this)">
					<option value="" >- selecciona -</option>
					<option value="arr">Arrhythmia</option>
					<option value="bc">Breast Cancer</option>
					<option value="ckd">Chronic Kidney Disease</option>
					<option value="derma">Dermatology</option>
					<option value="thy">Thyroid</option>
					</select>
				</td>
				<td colspan="1"><input class="button" type="button" value="Search" onclick="search()"></td>
			</tr>
			<tr>
				<td colspan="1" width="100px" align="right"><label for="symptoms">Symptom : </label></td>
				<td colspan="1"><select id="symptoms" name="symptoms" style="width:100%;" onChange="getValues(this)">
					<option value="" >- selecciona -</option>
					<%
					try{
						Category category = (Category)session.getAttribute("category");
					
						%>
						<script>document.getElementById("category").selectedIndex = <%= session.getAttribute("categoryIndex") %></script>

						<%
						for(String symptom: category.getSymptoms()){
					%>
						<option value="<%= symptom.toString() %>"><%= symptom.toString().replace('_', ' ').toUpperCase() %></option>
					<%	
						}
					}catch(NullPointerException npe){/*Expected exception at loading the page at first time*/
					}catch(Exception e){ e.printStackTrace(); }
					%>
				</select>
				</td>
			</tr>
			<tr style="height:45px;">
				<td colspan="1" width="100px" align="right"><label for="values">Value : </label></td>
				<td colspan="1"><select id="values" name="values" style="width:100%;">	
					<option value="" >- selecciona -</option>
					<%
					try{
						ArrayList<String> values = (ArrayList<String>)session.getAttribute("values");
						Collections.sort(values);
						%>
						<script>document.getElementById("symptoms").selectedIndex = <%= session.getAttribute("symptomIndex") %></script>
						<%
						for(String value: values){
					%>
						<option value="<%= value.toString() %>"><%= value.toString() %></option>
					<%	
						}
					}catch(NullPointerException npe){/*Expected exception at loading the page at first time*/
					}catch(Exception e){ e.printStackTrace(); }
					%>
					</select>
				</td>
			</tr>
			</table>
		</form> 	
		<hr>

		<div id="diseases" class="diseases">
			<% 
			try{ 
				ArrayList<String[]> diseases = (ArrayList<String[]>)session.getAttribute("diseases");
				if (diseases != null){
				%>
				<script>
				document.getElementById("values").selectedIndex = <%= session.getAttribute("valueIndex") %>
				document.getElementById("diseases").style.height="200px"
				</script>
				<form name="diseasesForm" id="diseasesForm" method="POST" action="">
				<input type="hidden" name="diseaseIndex" id="diseaseIndex">
				<table class="diseasesTable">
				<tr>
				<tr>
				<th>Disease</th><th>Confidence Factor</th>
				</tr>
				<%
				for(String[] obj: diseases){
			%>
				<tr>
				<td><input type="radio" name="diseases" value="<%= obj[0] %>" onclick="moreInfo()"><%= obj[0] %></td>
				<td align="center"><%= obj[1] %> %</td>
				</tr>
			<%	
				}
			%>
				</table>
				</form>
			<%
				}
			}catch(NullPointerException npe){/*Expected exception at loading the page at first time*/
			}catch(Exception e){ e.printStackTrace(); }
			
			%>	
		</div>
		<div id="chart" class="chart">
			<%
			try{
				ArrayList<String[]> diseases = (ArrayList<String[]>)session.getAttribute("diseases");
				JSONParser js = new JSONParser();
				JSONArray data = js.parseToJSON(diseases);
			%>
				<script>drawChart(<%= data %>);</script>
			<%
			}catch(NullPointerException npe){/*Expected exception at loading the page at first time*/
			}catch(Exception e){ e.printStackTrace(); }
			%>
		</div>
		<div id="info" class="info">
			<%	
			try{ 
				String info = (String)session.getAttribute("info");
				String disease = (String)session.getAttribute("disease");
				if(info != null && disease != null){
				%>
				<hr class="hrInfo">
				<table><th><%=disease %></th></table>
				<p><%= info%></p> 
				</table>
				<script>
				setRadioButton('<%= session.getAttribute("diseaseIndex")%>');
				</script>
				
			<%	}
			}catch(NullPointerException npe){/*Expected exception at loading the page at first time*/
			}catch(Exception e){ e.printStackTrace(); }
			
			%>
		</div>
		<div id="drugs" class="drugs">
			<%
			try{
				ArrayList<String[]> drugs = (ArrayList<String[]>)session.getAttribute("drugs");
				String disease = (String)session.getAttribute("disease");

				if(drugs != null && disease != null){					
				%>
				<hr class="hrDrugs">
				<table><th>Drugs for <%=disease %></th></table>
				<%
					if(drugs.size()>0){
						%>
						<table>
						<%
						for(String[] drug: drugs){
						%>
						<tr>
						<td class="drug"><%= drug[0] %></td>
						<td class="drug"><%= drug[1] %></td>
						</tr>
						<%	
						}	
						%></table><%
					}else
						%><p>No drugs found for this disease</p><%
				}
			}catch(NullPointerException npe){/*Expected exception at loading the page at first time*/
			}catch(Exception e){ e.printStackTrace(); }
				%>
			</div>
		</div>
	</div>
</body>
</html>