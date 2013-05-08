/* 
 * This file is part of Makeke.
 *
 * Makeke is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Makeke is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Makeke.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * Copyright (C) Anthony Christe 2013 
 */

/*
 * Allows for the copying of text when creating a new offer or request.
 */
$(function(){
	// Create click handler for all elements marked as selectable (will be "a" tags).
	$(".selectable").click(function(){
		// Update the text fields using the data parsed from table 
		$("#name").val($("#name" + this.id).text())
		$("#edition").val($("#edition" + this.id).text())
		$("#isbn").val($("#isbn" + this.id).text())
	});
});

/*
 * Sets nav menu bar item to active based on title of page.
 */
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