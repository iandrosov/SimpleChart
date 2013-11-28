/* 
 * adopted from WmRoot/pub/menu.dsp, changed 'body' to 'main' for different
 * main frame names 
 * kyang changed 'main' back to 'body' - main keeps creating new windows
 * instead of putting the pages into the body frame. 05/29/02
 */


var previousMenuImage;
var menuInit = false;

function adapterMenuClick(url, help){
  moveArrow(url);
  document.forms["urlsaver"].helpURL.value = help;
  return false;
}

function tdClick(thisTD, id)
{
  alert(thisTD.all);
  thisTD.all[id].click();
}

function menuClick(url, target) {


  switch (target)
  {

    case "body":
  	  parent[target].window.location.href= url;
	  break;
    default:   
          window.open(url, target, "directories=no,location=yes,menubar=yes,scrollbars=yes,status=yes,toolbar=yes,resizable=yes");
	  break;
  }

  menuMove(url, target);

  return false;
}


function menuMove(url, target) {

  if (target=="body")
  	moveArrow(url);

}

function IE() {
	if (navigator.appName == "Microsoft Internet Explorer")
		return true;
	return false;

}
function menuMouseOver(object, id) {
	object.style.background='#FFFFFF';
	window.status= id;
}
function menuMouseOut(object) {
	object.style.background='#F0ECDA';
	window.status='';

}

function moveArrow(item)
{
	if(menuInit)
	{
  		if(previousMenuImage != null) 
			previousMenuImage.src="images/blank.gif";

  		var temp = previousMenuImage;
  		previousMenuImage=document.images[item];

  		if(previousMenuImage == null) 
			previousMenuImage = temp;

  		previousMenuImage.src="images/selectedarrow.gif";
	}
}


function initMenu(firstImage)
{
	previousMenuImage = document.images[firstImage];
	previousMenuImage.src="images/selectedarrow.gif";
	menuInit = true;
	return true;
}
