showlink = function(el, cellval, opts){
	var url = cellval.colModel.formatoptions.baseLinkUrl;
	url = url.replace("__id__", "" + cellval.rowId);
	var text = cellval.colModel.formatoptions.linkText;
   return "<a href='" + url + "'>"+ text + "</a>";
}