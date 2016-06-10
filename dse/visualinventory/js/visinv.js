$(document).ready(function(){
   
	   //Enable hover for touch-devices
	    $('.hover').bind('touchstart touchend', function(e) {
        	e.preventDefault();
        	$(this).toggleClass('hover_effect');
    	});
	  
	   //Create array of rect-object pairs
	   groupArray = assignGroups();		
	   loadObjects();
	   //Create image
	   var img = new Image();
	   img.src = img_source;
	   img.onload = function() {
	   		paintCanvas( this );
	   		defineRects();
			colorLinks();
	   };

	   
	   $("#" + canvasId).click(function(e){
		   defineRects();
	   	   whichClick(e.pageX, e.pageY);
	    });

	   $("." + rectClass).click(function(e){
	   	   whichClick(e.pageX, e.pageY);
	    });

	    $("." + rectClass).bind("contextmenu", function(e) {
				var imgUrn = $(this).children("." + labelClass).html().trim();
				var myUrl = imgURL + imgUrn;
				window.open(myUrl);
				return false;
		});

	   	$("." + objectClass).click(function(){
	   	   dataClick(objectIdBase + $(this).closest("div." + pairClass).attr("id"));
		   
	    });
	   
});

// takes URN, returns bit before subref
function plainURN(urn){
		return urn.split("@")[0];
}
// takes URN, reuturns a subreference
function urnROI(urn){
		var tempString = urn.split("@")[1];
		if (tempString.length > 0){ return tempString; } else { return ""; }
}

// uses global groupArray() and mapArray() defined in the xslt-generated html
// n.b.: the xslt will wrap the URN in a <span> element, which is accounted for before grabbing the roi
function loadObjects(){
	$("." + seqClass).each(function(){
			var seqId = $(this).attr("id");
			$(this).children("div." + pairClass).each(function(){
				var tempObject = new Object;
				tempObject.sequence = seqId;
				tempObject.groupClass = classForGroup(seqId);
				tempObject.rectId = $(this).children("div." + rectClass).attr("id");
				tempObject.objId = $(this).children("div." + objectClass).attr("id");
				tempObject.imgURN = $(this).children("div." + rectClass).children("span").html();
				tempObject.urn = plainURN(tempObject.imgURN);
				tempObject.roi = urnROI(tempObject.imgURN);
				var x = mapArray.push(tempObject);
			});
		
	});
}

function classForGroup(seq){
		var tempString = ""
	for (x = 0; x < groupArray.length; x++) {
		if (groupArray[x].id == seq){
				tempString = groupArray[x].className;
		}
	}
	return tempString;
}

function paintCanvas( img ){
	   var ctx = document.getElementById('canvas').getContext('2d');
	   var myCanvas = document.getElementById('canvas');
	   var img_w = img.width;
	   var img_h = img.height;
	   
	   document.getElementById(canvasId).width = img_w;
	   document.getElementById(canvasId).height = img_h;
   	   ctx.drawImage(img, 0, 0);
   	   
   	   //go ahead and resize data-table
   	   
   	   $("#rightDiv").width( window.innerWidth - (img.width * 1.2 ) );
	   
}

function defineRects(){
	var x;
	var thisROI, thisLeft, thisTop, thisWidth, thisHeight;
	var absCoords;
	
	
	for (x = 0; x < mapArray.length; x++){
		if (mapArray[x].roi.length > 0){
			thisRoi = mapArray[x].roi;
			if (thisRoi.split(",").length == 4){
			
				absCoords = doSomeMath(thisRoi, canvasId);
				
				thisLeft = absCoords.left;			
				thisTop = absCoords.top;			
				thisWidth = absCoords.width;			
				thisHeight = absCoords.height;
				$("#" + mapArray[x].rectId).css("position","absolute");
				$("#" + mapArray[x].rectId).css("top",thisTop);
				$("#" + mapArray[x].rectId).css("left",thisLeft);
				$("#" + mapArray[x].rectId).css("width",thisWidth);
				$("#" + mapArray[x].rectId).css("height",thisHeight);
				$("#" + mapArray[x].rectId).css("display","block");
				$("#" + mapArray[x].rectId).addClass("roiBox");
				$("#" + mapArray[x].rectId).addClass(mapArray[x].groupClass);

							
			} else {
				displayError("Item number " + x + " had a malformed CITE Image URN: " + thisRoi);
			}
		} else {
		displayError("Item number " + x + " had a malformed CITE Image URN: " + mapArray[x].roi);
		}
	}
}

function colorLinks(){
		$("div.toggler").each(function(){
			var seqId = $(this).children("a").attr("id").substring(7,$(this).children("a").attr("id").length);
			var className = classForGroup(seqId);
			$(this).children("a").addClass(className);
		});
}


//Given an ROI-string and a canvasObject, return an object containing
//left, top, width, height values in canvas-pixel coordinates
function doSomeMath(roiString, canvasObjectId){
	var returnObject = new Object();
	var canvasCoords = new Object();
	canvasCoords.left = $("#" + canvasObjectId).offset().left;
	canvasCoords.top = $("#" + canvasObjectId).offset().top;
	canvasCoords.width = $("#" + canvasObjectId).width();
	canvasCoords.height = $("#" + canvasObjectId).height();
	ml = parseFloat(roiString.split(",")[0]);
	mt = roiString.split(",")[1];
	mw = roiString.split(",")[2];
	mh = roiString.split(",")[3];

	returnObject.left = (canvasCoords.width * ml) + canvasCoords.left;
	returnObject.top = (canvasCoords.height * mt) + canvasCoords.top;
	returnObject.width = (canvasCoords.width * mw);
	returnObject.height = (canvasCoords.height * mh);
	return returnObject;
}

function displayError( whichError ){
		console.log(whichError);
}

//create a hash of groups to class-names
function assignGroups(){
	var tempArray = new Array();
	$("." + seqClass).each(function(index){
		var tempObject = new Object;	
		tempObject.id = $(this).attr("id");
		tempObject.className = "sequence_" + index;
		tempArray.push(tempObject);
	});
	return tempArray;
}

function whichClick(cx, cy){
	allRectVizOff();
	allDataVizOff();
	allSeqVizOff();
	$("." + rectClass).each( function(){
		if ( cx > $(this).offset().left ){
			if ( cx < ( $(this).offset().left + $(this).width() ) ){
				if ( cy > $(this).offset().top ){
					if ( cy < ( $(this).offset().top + $(this).height() ) ){
						rectVizOn($(this).attr("id") );
						var tempObj = objectIdBase + $(this).attr("id").substring(rectIdBase.length+1,$(this).attr("id").length);
						seqVizOn(sequenceForObject(tempObj));
						dataVizOn(objectIdBase + $(this).attr("id").substring(rectIdBase.length+1,$(this).attr("id").length));
					}
				}
			}
		}
	});

}

function dataClick(thisId){
	allRectVizOff();
	allDataVizOff();
	allSeqVizOff();
	rectVizOn(rectIdBase + "_" + thisId.substring(objectIdBase.length,thisId.length));
		seqVizOn(sequenceForObject(thisId));
		dataVizOn(thisId);
}

function rectVizOn(myRect){
		$("#" + myRect).addClass("cite_hilite");
}

function rectVizOff(myRect){
		$("#" + myRect).removeClass("cite_hilite");
}

function sequenceForObject(myObject){
		var sequenceId = "";
		sequenceId = $("#" + myObject).closest("div." + seqClass).attr("id");
	    return sequenceId;
}

function seqVizOn(mySeq){
		//$("#" + mySeq).addClass("cite_text_hilite");
}

function seqVizOff(mySeq){
		// Here we need to get the sequence that is the parent of the target defined by myObject
		//$("#" + mySeq).removeClass("cite_text_hilite");
}

function dataVizOn(myObj){
		$("#" + myObj).addClass("cite_text_hilite");
}

function dataVizOff(myObj){
		$("#" + myObj).removeClass("cite_text_hilite");
}

function allRectVizOff(){
	$("." + rectClass).each(function(){
		rectVizOff($(this).attr("id"));
	});
}

function allSeqVizOff(){
	$("." + seqClass).each(function(){
		dataVizOff($(this).attr("id"));
	});	
}

function allDataVizOff(){
	$("." + objectClass).each(function(){
		dataVizOff($(this).attr("id"));
	});	
}

function toggleThis(seqId){
		$("#" + seqId).toggle("slow");
}
