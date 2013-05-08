$(function(){
	$(".selectable").click(function(){
		$("#name").val($("#name" + this.id).text())
		$("#edition").val($("#edition" + this.id).text())
		$("#isbn").val($("#isbn" + this.id).text())
	});
});