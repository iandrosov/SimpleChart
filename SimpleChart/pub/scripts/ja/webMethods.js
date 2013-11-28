
var row = "even";
var other = "odd";
var swap = "";

var path1 = "";
var menu1 = "";
var path1_class = "";


var normal = 1;
var error  = 2;
var serverkey = 4;


function w(text)
{
	document.write(text);
}


function pathInit(css_class)
{
	path1 = "";
	path1_class = css_class;
	
}

function pathAdd(text, url)
{
	path1 += text;
	path1 += " &gt; ";
}

function pathShow()
{
	w("<table width=100% >");
	w("<tr>");
	w("<td class=" + path1_class + ">");
	w(path1);
	w("</td></tr></table>");
}


function menuInit()
{
	menu1 = "";
	
}


function normalize(text)
{
        var newstring = "";
        for(var i = 0; i < text.length; i++){
	        if(text.substring(i,i+1) == "\uff11") newstring += "1"; 
	        else if(text.substring(i,i+1) == "\uff12") newstring += "2"; 
	        else if(text.substring(i,i+1) == "\uff13") newstring += "3"; 
	        else if(text.substring(i,i+1) == "\uff14") newstring += "4"; 
	        else if(text.substring(i,i+1) == "\uff15") newstring += "5"; 
	        else if(text.substring(i,i+1) == "\uff16") newstring += "6"; 
	        else if(text.substring(i,i+1) == "\uff17") newstring += "7"; 
	        else if(text.substring(i,i+1) == "\uff18") newstring += "8"; 
	        else if(text.substring(i,i+1) == "\uff19") newstring += "9"; 
	        else if(text.substring(i,i+1) == "\uff10") newstring += "0"; 
                else if(text.substring(i,i+1) == "\uff0e") newstring += ".";
                else if(text.substring(i,i+1) == "\u3002") newstring += ".";
                else newstring += text.substring(i,i+1);
	}
	return newstring;
}

function menuAdd(text, url, bonus_text)
{
	menu1 += "<li>";
	menu1 += "<a href=\"" + url + "\">" + text + "</a>";
	if (bonus_text)
	{
		menu1 += "<br>";
		menu1 += bonus_text;
	}
	menu1 += "</li>";

}


function menuShow()
{
	w("<table width=100% >");
	w("<tr>");
	w("<td>");  //class=\"submenu\"
	w("<ul>");
	w(menu1);
	w("</ul>");
	w("</td></tr></table>");

}



function messageInit()
{
	message1 = "";
	
}


function messageAdd(text, message_type)
{
	menu1 += text;

}


function messageShow()
{
	w("<table width=100% >");
	w("<tr>");
	w("<td class=\"message\">");
	w(menu1);
	w("</td>");
	w("</tr>");
	w("</table>");
}



function writeEditPass(mode, name, value)
{
	if(mode == 'edit')
	{
		w("<input type=\"password\" name=\""+name+"\" value=\""+value+"\">");
	}
	else
	{
		if(value == "")
			value = "unspecified";
		w(value);
	}
}

function writeEdit(mode, name, value)
{
	if(mode == 'edit')
	{
		w("<input type=\"text\" name=\""+name+"\" value=\""+value+"\">");
	}
	else
	{
		if(value == "")
			value = "unspecified";
		w(value);
	}
}

function writeMessage(msg)
{
	w("<TR><TD class=\"message\" colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;"+msg+"</TD></TR>");
}

function reloadTopFrame()
{
	parent.topmenu.location.replace("top.dsp");
}

function writeTD (c)
{
	w("<TD CLASS=\"");
	w(row);
	w(c);
	w("\">");
	return true;
}

function writeTDspan(c,span)
{
	w("<TD CLASS=\"");
	w(row);
	w(c);
	w("\" COLSPAN=");
	w(span);
	w(">");
}

function writeTDrowspan(c,span)
{
	w("<TD CLASS=\"");
	w(row);
	w(c);
	w("\" ROWSPAN=");
	w(span);
	w(">");
}

function swapRows()
{
	swap = row;
	row = other;
	other = swap;
}

function resetRows()
{
	row = "even";
	other = "odd";
	swap = "";
}


function isNum(num) 
{
  	num = num.toString();

  	if (num.length == 0)
  	  	return false;

  	for (var i = 0; i < num.length; i++)
    	if (num.substring(i, i+1) < "0" || num.substring(i, i+1) > "9")
      		return false;

  	return true;
}


function verifyRequiredField(form, field)
{
	if (document.forms[form][field].value == "")
	{
		document.forms[form][field].focus();
		return false;
	}
	return true;
 }

function verifyRequiredNonNegNumber(form, fieldName)
{
	var field = document.forms[form][fieldName];

	if (field.value == "")
	{
		field.focus();
		return false;
	}

	if ( !isNum(field.value))
	{
		field.focus();
		return false;
	}

	if ( field.value < 0)
	{
		field.focus();
		return false;
	}

	return true;
}



function verifyRequiredFieldList(form, fieldList)
{
	for (index in fieldList)
	{
	   if (!verifyRequiredField(form, fieldList[index]))
	   {
		  alert ("エラー: 選択したフィールドには有効なデータが必要です。");
		  return false;
	   }
	}
	return true;
 }

function verifyFieldsEqual(form, field1, field2)
{
	if (document.forms[form][field1].value != document.forms[form][field2].value)
	{
		alert("エラー: フィールドに同じ値が入っていると思われます。再度入力してください。");
		document.forms[form][field1].focus();
		return false;
	}
	return true;
}

function setNavigation(doc_url, help_url, is_doc)
{

	if(parent == null || parent.frames == null || parent.frames.menu == null || parent.frames.menu.document == null)
		return false;

	if(parent.frames.menu.moveArrow != null)
	{
		if(is_doc != null) parent.frames.menu.moveArrow(doc_url);
		else
		parent.frames.menu.moveArrow(doc_url+location.search);
  	}

	if(parent.frames.menu.document.forms["urlsaver"] != null && parent.frames.menu.document.forms["urlsaver"].helpURL != null)
		parent.frames.menu.document.forms["urlsaver"].helpURL.value = help_url;
  
        return true;
        
}  


function initMenu(firstImage)
{
	var previousMenuImage = document.images[firstImage];
	previousMenuImage.src="images/selectedarrow.gif";
	return true;
}
