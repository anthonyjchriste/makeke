$(function(){
	$(".selectable").click(function(){
		$("#name").val($("#name" + this.id).text())
		$("#edition").val($("#edition" + this.id).text())
		$("#isbn").val($("#isbn" + this.id).text())
	});
});

$(function() {
	switch ($(document).attr("title")) {
		case "Welcome to Makeke":
			$("#home").addClass("active");
			break;
		case "Book Explorer":
			$("#explorer").addClass("active")
			break;
		case "My Requests / Offers":
			$("#manage").addClass("active");
			break;
	}
});