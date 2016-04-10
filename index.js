/**
 * Calling SymptomServlet to obtain the symptoms of the category selected 
 * @param category Category selected
 */
function getSymptoms(category){
	var form = document.getElementById("myForm");
	form.action = "Symptoms.servlet";
	var index = document.getElementById("categoryIndex");
	index.value = category.selectedIndex;
	form.submit();
}


/**
 * Calling ValuesServlet to obtain the values list for the symptom selected
 * @param symptom Symptom selected
 */
function getValues(symptom){
	var form = document.getElementById("myForm");
	form.action = "Values.servlet";
	var index = document.getElementById("symptomIndex");
	index.value = symptoms.selectedIndex;
	form.submit();
}

/**
 * Function to search all diseases which can be diagnosticated by the symptom and value selected
 */
function search(){
	var form = document.getElementById("myForm");
	form.action = "Diseases.servlet";
	var index = document.getElementById("valueIndex");
	index.value = values.selectedIndex;
	form.submit();
}

/**
 * Function to obtain more information about a disease
 */
function moreInfo(){
	var form = document.getElementById("diseasesForm");
	form.action = "Info.servlet";
	
	var elements = form.elements;
	for(i=0;i<elements.length;i++){
		if(elements[i].checked)
			document.getElementById("diseaseIndex").value = elements[i].value;
	}
	form.submit();
}

/**
 * Method to select a radio button
 * @param value
 */
function setRadioButton(value){
	var buttons = document.getElementById("diseasesForm").elements;
	for(i=0;i<buttons.length;i++){
		if(buttons[i].value == value)
			buttons[i].checked = true;
	}
}

/**
 * Method to draw a pie chart with the confidence factor of the diseases
 * @param data Data to draw the chart
 */
function drawChart(data){
	var width = 450,
    height = 180,
    radius = Math.min(width, height) / 2;

	var color = d3.scale.ordinal()
	    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);

	var arc = d3.svg.arc()
	    .outerRadius(radius - 10)
	    .innerRadius(0);

	var labelArc = d3.svg.arc()
	    .outerRadius(radius - 40)
	    .innerRadius(radius - 40);

	var pie = d3.layout.pie()
	    .sort(null)
	    .value(function(d) { return d.ConfidenceFactor; });

	var svg = d3.select(".chart").append("svg")
	    .attr("width", width)
	    .attr("height", height)
	  .append("g")
	    .attr("transform", "translate(" + width / 4 + "," + height / 2 + ")");

	 var g = svg.selectAll(".arc")
	     .data(pie(data))
	   .enter().append("g")
	     .attr("class", "arc");
	
	 g.append("path")
	     .attr("d", arc)
	     .style("fill", function(d) { return color(d.data.Disease); });
	
	
	
	 // Creating the legend
	 var legendRectSize = 18;
	 var legendSpacing = 4;
	 
	 var legend = svg.selectAll('.legend')
	  .data(color.domain())
	  .enter()
	  .append('g')
	  .attr('class', 'legend')
	  .attr('transform', function(d, i) {
	    var height = legendRectSize + legendSpacing;
	    var offset =  height * color.domain().length / 4;
	    var horz = legendRectSize+80;
	    var vert = i * height - 4*offset+20;
	    return 'translate(' + horz + ',' + vert + ')';
	  });
	 
	 legend.append('rect')
	  .attr('width', legendRectSize)
	  .attr('height', legendRectSize)
	  .style('fill', color)
	  .style('stroke', color);
	 
	 legend.append('text')
	  .attr('x', legendRectSize + legendSpacing)
	  .attr('y', legendRectSize - legendSpacing)
	  .attr('width','100')
	  .text(function(d) { return d; });
}
