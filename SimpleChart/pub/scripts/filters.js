//*******************************
//
// Code for managing filters for processes, services, documents
//

var allItems = new Array();	    // contains a list of ALL items
var filterItems = new Array();	// contains a list of permitted items
var Dirty = 0;
var pmadmin = "MonitorAdministrators";
var all = "INCLUDEALL";	    	// value (key)
var none = "INCLUDENONE";			// value (key)

var allitemslabel;		// label
var noitemslabel;		// label
var nosavemessage;      // message

function setTypeVars(single, plural)
{
	allitemslabel = "--- all " + plural + " ---";
	noitemslabel = "--- no " + plural + " ---";
	nosavemessage = "--- " + single + " Access changes have not been saved. ---";
}

/*
 * ADD FUNCTIONS
 * the add functions create the initial arrays used to run this page.  these
 * are called from the .dsp page to populate the initial arrays with data
 * returned from b2b service calls.  these arrays do not correspond specifically
 * with the widgets on the .dsp page, although they are used to populate them.
 */

// creates an array listing all the items.  stores label and key,
// so that we don't need to do any other database work to get the key when we
// have to save back to the database eventually (key is required then).
function addToAllItems(item, key)
{
	// list is alphabetized by item name
//	alert("adding " + item + ", " + key + "to all items list");
	allItems[item] = key;
//	document.write("all items:<br>");
//	printArray(allItems);
}
function addToFilterItems(item, key)
{
	//alert("add to filter items: " + item + ", " + key);
	// list is alphabetized by item name
	if (key == all)
	{
//		alert("adding all items to filterItems");
		filterItems = allItems;
	}
	else
	{
//		alert("adding " + item + ", " + key + "to filter list");
		filterItems[item] = key;
	}
//	document.write("filter items:<br>");
//	printArray(filterItems);
}

// keeps the filterItems array up to date with what is in the
// member items list.  should be called each time the member items
// list is changed.
// have to make a new array and populate it with the contents of the list.
function updateFilterItems(list)
{
	filterItems = new Array();
	for (var i = list.options.length; i > 0; i--)
	{
		filterItems[list.options[i-1].text] = list.options[i-1].value;
	}
}




// put all the items into the available items list.
function populateAvailableItemsList()
{
	clearList(frmAvailableItemsList);
	for (var item in allItems)
	{
		// items are alphabetized
		//alert("adding " + item + " to available items list");
		insertOptionABC(item, allItems[item], frmAvailableItemsList, false)
	}
}




// this loads new info into the member and available items lists
function loadItems()
{
	clearList(frmAvailableItemsList);
	clearList(frmMemberItemsList);
	populateAvailableItemsList();	// add all items to available list first

	// loop through each item and find out if this group has access to it.
	// if so, add the item to frmMemberItemsList and remove it from
	// frmAvailableItemsList
	for (var item in allItems)
	{
		// this means the item is in the filter
		if (filterItems[item] == allItems[item])
		{
			//alert("moving item " + item + " from available to member");
			insertOptionABC(item, allItems[item], frmMemberItemsList,false);
			removeOption(allItems[item], frmAvailableItemsList);
		}
	}

	// if there are no options in either list, add the none indicator
	addNone(frmAvailableItemsList);
	addNone(frmMemberItemsList);
}





// called when permissions are added to the member items list (arrow button
// pointing left is pressed)
function addAccess()
{
	var idx = frmAvailableItemsList.selectedIndex;
	// don't do anything if nothing is selected
	if (idx == -1 || frmAvailableItemsList.options[idx].value == none)
		return false;

	//alert("add idx: " + idx + ", text: " + frmAvailableItemsList.options[idx].text + ", value: " + frmAvailableItemsList.options[idx].value);
	if (frmAvailableItemsList.options[idx].value == all)
	{
		filterItems = allItems;
	}
	else
	{
		moveItems(frmAvailableItemsList, frmMemberItemsList);
		updateFilterItems(frmMemberItemsList);
	}
	loadItems();

	Dirty++;
}
// called when permissions are removed from the member items list (arrow button
// pointing right is pressed)
function removeAccess()
{
	var idx = frmMemberItemsList.selectedIndex;
	// don't do anything if nothing is selected
	if (idx == -1 || frmMemberItemsList.options[idx].value == none)
		return false;

	//alert("remove idx: " + idx + ", text: " + frmMemberItemsList.options[idx].text + ", value: " + frmMemberItemsList.options[idx].value);
	if (frmMemberItemsList.options[idx].value == all)
	{
		// if selected
		moveItems(frmMemberItemsList, frmAvailableItemsList);
		updateFilterItems(frmMemberItemsList);
	}
	else
	{
		moveItems(frmMemberItemsList, frmAvailableItemsList);
		if (removeOption(all, frmMemberItemsList))
			insertOptionABC(allitemslabel, all, frmAvailableItemsList, false);
		updateFilterItems(frmMemberItemsList);
	}
	loadItems();

	Dirty++;
}


function checkDirty (itemType)
{
	if (Dirty > 0)
	{	event.returnValue = nosavemessage;	}
}

function submitForm()
{
	Dirty = 0;
	return true;
}



// called when the save button is pressed.
// creates a string listing the process keys this entity should have access to.
// this info is submitted back to the detail page, which invokes a service
// based on the action set in this function.
// the service parses the access list and updates the entity attributes.
function saveChanges()
{
	var filterString = "";
	if (Dirty > 0)
	{
		for (var item in filterItems)
			filterString += filterItems[item] + ";";

		frmHiddenInsert.value = filterString;
		//alert("new filter: " + filterString);
    	frmHiddenAction.value = "updateFilter";
	}
}

// called when the remove filter button is pressed.
function removeFilter()
{
	frmHiddenAction.value = "removeFilter";
}

/*
// debugging functions
function printArray(array)
{
	document.write("<br>");
	for (var index in array)
	{
		document.write("&nbsp;&nbsp;[" + index + "]: " + array[index] + "<br>");
	}
	document.write("<br>");
}
*/
