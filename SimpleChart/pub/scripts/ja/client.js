//*******************************
//
// Generic Functions
//

//var noproc = "noproc";		// value (key)
//var noproclabel = "--- no processes ---";		// label

function insertOptionABC(newText,newValue, oTo, selected)
{
	//INSERT OPTION CORRECTLY ALPHABETICALLY
	var newoption;
		
	if (selected)
		newoption = new Option(newText, newValue, true, true);
	else
		newoption = new Option(newText, newValue);
	
	oTo.options[oTo.length] = newoption;

	if (oTo.length > 1)
	{
		var insertValue = newText.toLowerCase();
		var found = false;
		var index = 0;

		while (found == false && index < oTo.length-1)
		{
			//document.write("index " + index + ": " + insertValue + " < " + oTo.options[index].text.toLowerCase() + " ?<br>");
			if (insertValue < oTo.options[index].text.toLowerCase())
				found = true;
			else
				index++;
		}

		//do the insertion
		for (var i = oTo.options.length-1; i > index; i--) 
		{
			oTo.options[i] = new Option(oTo.options[i-1].text, oTo.options[i-1].value);
			if (oTo.options[i-1].selected) 
				oTo.options[i].selected = true;
		}

		if (selected)
			newoption = new Option(newText, newValue, true, true);
		else
			newoption = new Option(newText, newValue);
		
		oTo.options[index] = newoption;
	}
}


function insertOption(newText,newValue, oTo, selected)
{
	//INSERT OPTION LAST IN LIST
	if (selected)
		var newoption = new Option(newText, newValue, true, true);
	else
		var newoption = new Option(newText, newValue);

	oTo.options[oTo.length] = newoption;
}


function moveItems(oFrom,oTo)
{
	//NO REASON TO DO ANYTHING IF NOTHING IS SELECTED
	if (oFrom.selectedIndex == -1)
		return false;

	//UNSELECT TARGET ITEMS
	oTo.selectedIndex = -1;

	//REMOVE the ----NONE----
	if (oTo.options[0].value == noitemslabel)
		oTo.options[0] = null;

	//MOVE SELECTED OVER
	while (oFrom.selectedIndex > -1)
	{
		var index = oFrom.selectedIndex;
		var newText = oFrom.options[index].text;
		var newValue = oFrom.options[index].value;

		insertOptionABC(newText,newValue, oTo, true);
		oFrom.options[index] = null;
	}
	//ADD the ----NONE---- (if empty)
	addNone(oFrom);
}


function removeOption(value, object)
{
	for (var i = object.options.length; i > 0; i--) 
	{
		if (object.options[i-1].value == value)
		{
			object.options[i-1] = null;
			return true;
		}
	}
	return false;
}

function clearList(list)
{
	while (list.length > 0) 
		list.options[0] = null;
}


function unselect(currentlist, listone, listtwo, listthree)
{
	if ( currentlist.options[0].value == 0 )
	{
		//return false;
		currentlist.selectedIndex = -1;
	}
	listone.selectedIndex = -1;
	if (listtwo)
		listtwo.selectedIndex = -1;
	if (listthree)
		listthree.selectedIndex = -1;
}




function addNone(currentlist)
{
	if (currentlist.options.length == 0) {
	    var newoption = new Option(noitemslabel, none);
	    currentlist.options[currentlist.length] = newoption;
	 }
}



function cancelChanges()
{
	if (confirm("このページに加えた変更内容がすべて失われます。よろしいですか?"))
		window.location.reload();	
}


function selectOption(optionlist, value)
{
	for (var i = optionlist.options.length; i > 0; i--) 
	{
		if (optionlist.options[i-1].value == value)
			optionlist.selectedIndex = i-1;
	}
}



function conditionalmoveOptions(oFrom, oTo, examineList, moveList)
{
	//remove the none
	if (oFrom.options[0].value == "")
	{
		oFrom.options[0] = null;
		return true;
	}
		
	//if examineList = null then loop through entire list
	// else only loop through those in the inlude list
	
	//if moveList = null then move anybody
	// else only move those not in list

	if (examineList == null)
	{
		for (var i = oFrom.options.length; i > 0; i--) 
		{
			var value = oFrom.options[i-1].value;
			
			if (moveList[value] != value)
//			{
//				//keep it
//			}
//			else
			{
				if (removeOption(value, oFrom)) 
					insertOptionABC(value,value, oTo, false);
			}
		}
	}
	else
	{
		for (value in examineList) 
		{
			if (examineList[value] == value)
				if (removeOption(value, oFrom))
					insertOptionABC(value,value, oTo, false);
		}
	}
}


function checkDirty()
{
	if (Dirty != 0)
	{
		//Does not check to make sure it is really dirty.
		return (! confirm("変更内容が保存されていません。保存せずに継続してよろしいですか?"));
	}
}



function OLDinsertOptionABC(newText,newValue, oTo, selected)
{
	//INSERT OPTION CORRECTLY ALPHABETICALLY
	if (selected)
		var newoption = new Option(newText, newValue, true, true);
	else
		var newoption = new Option(newText, newValue);

	oTo.options[oTo.length] = newoption;
	var whereInsert = oTo.length-1;
	while (whereInsert > 0)
	{
		if (oTo.options[whereInsert].value.toUpperCase() < oTo.options[whereInsert-1].value.toUpperCase())
		{
			var lowerOptionText = oTo.options[whereInsert].text;
			var lowerOptionValue = oTo.options[whereInsert].value;
			var lowerOptionSelected = oTo.options[whereInsert].selected;

			var upperOptionText = oTo.options[whereInsert-1].text;
			var upperOptionValue = oTo.options[whereInsert-1].value;
			var upperOptionSelected = oTo.options[whereInsert-1].selected;
		
			oTo.options[whereInsert] = new Option(upperOptionText, upperOptionValue);
			oTo.options[whereInsert-1] = new Option(lowerOptionText, lowerOptionValue);

			if (upperOptionSelected)
				oTo.options[whereInsert].selected = true;
			if (lowerOptionSelected)
				oTo.options[whereInsert-1].selected = true;
					
		}
		whereInsert--;
	}
	//do the insertion
}
