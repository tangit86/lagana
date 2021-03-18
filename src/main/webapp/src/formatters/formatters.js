import React from 'react';
import moment from 'moment'

export const  timeFormatter = (timestamp)=>{

    var a = new Date(timestamp*1);
    return moment(a).format("ddd,DD-MMM HH:mm:ss.SSS");
}


var xmlFormat = require('xml-formatter');

export const xmlFormatter = (xml,conf)=>{
  // const xmlString = xmlFormat(xml,conf).replace(">","><br>");
  // return (
  //   <div>{htmlDecode(xmlString)}</div>
  // );
  return (<pre style={{boxSizing:"border-box"}}>{formatXml(xml)}</pre>);
}

function formatXml(xml) {
  var formatted = '';
  var reg = /(>)(<)(\/*)/g;
  xml = xml.replace(reg, '$1\r\n$2$3');
  var pad = 0;
  xml.split('\r\n').forEach((node)=> {
      var indent = 0;
      if (node.match( /.+<\/\w[^>]*>$/ )) {
          indent = 0;
      } else if (node.match( /^<\/\w/ )) {
          if (pad != 0) {
              pad -= 1;
          }
      } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
          indent = 1;
      } else {
          indent = 0;
      }

      var padding = '';
      for (var i = 0; i < pad; i++) {
          padding += '  ';
      }

      formatted += padding + node + '\r\n';
      pad += indent;
  });

  return formatted;
}