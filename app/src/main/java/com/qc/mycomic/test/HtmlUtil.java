package com.qc.mycomic.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class HtmlUtil {

    public static String getSearchHtml() {
        return "\n" +
                "\n" +
                "<!--headTrap<body></body><head></head><html></html>-->\n" +
                "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <!-- fragBegin: withOutDynamicConfig -->\n" +
                "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\" />\n" +
                "<!-- fragBegin: withOutViewPort -->\n" +
                "\n" +
                "<!-- 引入啄木鸟监控js -->\n" +
                "<script charset=\"utf-8\">\n" +
                "    !function(e,t){\"object\"==typeof exports&&\"undefined\"!=typeof module?t(exports):\"function\"==typeof define&&define.amd?define([\"exports\"],t):t(e.emonitor={})}(this,function(e){\"use strict\";var t=Object.prototype.toString,r=Object.prototype.hasOwnProperty,n=function(e){return\"[object Array]\"===t.call(e)},o=function(e){return\"[object Object]\"===t.call(e)},i=function(e){return\"string\"==typeof e},a=function(e){return\"function\"==typeof e},s=function(e,t){return r.call(e,t)},c=function(e){if(!e)return{};var t=document.createElement(\"a\");return t.href=e,{host:t.host,path:t.pathname,hostname:t.hostname,protocol:t.protocol.slice(0,-1)}},d={duration:\"delay\",name:\"resurl\",type:\"type\"},u=[\"css\",\"script\",\"img\",\"video\",\"audio\"],p=[\"cdn\",\"cgi\"],l=[\"s_path\",\"s_uuid\",\"s_traceid\",\"s_guid\",\"s_appid\",\"s_vuserid\",\"hc_pgv_pvid\",\"s_omgid\",\"err_desc\"],m=(function(){function e(e){this.value=e}function t(t){function r(o,i){try{var a=t[o](i),s=a.value;s instanceof e?Promise.resolve(s.value).then(function(e){r(\"next\",e)},function(e){r(\"throw\",e)}):n(a.done?\"return\":\"normal\",a.value)}catch(e){n(\"throw\",e)}}function n(e,t){switch(e){case\"return\":o.resolve({value:t,done:!0});break;case\"throw\":o.reject(t);break;default:o.resolve({value:t,done:!1})}(o=o.next)?r(o.key,o.arg):i=null}var o,i;this._invoke=function(e,t){return new Promise(function(n,a){var s={key:e,arg:t,resolve:n,reject:a,next:null};i?i=i.next=s:(o=i=s,r(e,t))})},\"function\"!=typeof t.return&&(this.return=void 0)}\"function\"==typeof Symbol&&Symbol.asyncIterator&&(t.prototype[Symbol.asyncIterator]=function(){return this}),t.prototype.next=function(e){return this._invoke(\"next\",e)},t.prototype.throw=function(e){return this._invoke(\"throw\",e)},t.prototype.return=function(e){return this._invoke(\"return\",e)}}(),function(e,t){if(!(e instanceof t))throw new TypeError(\"Cannot call a class as a function\")}),h=function(){function e(e,t){for(var r=0;r<t.length;r++){var n=t[r];n.enumerable=n.enumerable||!1,n.configurable=!0,\"value\"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,r,n){return r&&e(t.prototype,r),n&&e(t,n),t}}(),f=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var r=arguments[t];for(var n in r)Object.prototype.hasOwnProperty.call(r,n)&&(e[n]=r[n])}return e},v=function(){},g=function(e){var t=e.name,r=e.url,n=void 0===r?window.location.href:r;t=t.replace(/[\\[\\]]/g,\"\\\\$&\");var o=new RegExp(\"[?&]\"+t+\"(=([^&#]*)|&|#|$)\").exec(n);return o&&o[2]?decodeURIComponent(o[2].replace(/\\+/g,\" \")):\"\"},y=function(e){for(var t=encodeURIComponent(e)+\"=\",r=document.cookie.split(\";\"),n=0;n<r.length;n++){for(var o=r[n];\" \"===o.charAt(0);)o=o.substring(1,o.length);if(0===o.indexOf(t))return decodeURIComponent(o.substring(t.length,o.length))}return null},w=function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;return Math.random()<=e},_=function(e,t,r){var n=null;return function(){for(var o=arguments.length,i=Array(o),a=0;a<o;a++)i[a]=arguments[a];var s=this;clearTimeout(n),n=setTimeout(function(){e.apply(s,i),\"function\"==typeof r&&r()},t)}},b=function(e){var t=[];for(var r in e)t.push(encodeURIComponent(r)+\"=\"+encodeURIComponent(e[r]));return t.join(\"&\")},x=function(e){var t=e.baseUrl,r=e.data,n=e.method,o=void 0===n?\"GET\":n;if(\"GET\"===o){var i=new Image;i.onerror=function(){i=null},i.onload=function(){i=null},i.src=t+\"&\"+b(r)}else if(\"POST\"===o)try{var a=null;(a=window.XMLHttpRequest?new XMLHttpRequest:new ActiveXObject(\"Microsoft.XMLHTTP\")).open(\"POST\",t,!0),a.setRequestHeader(\"Content-type\",\"application/x-www-form-urlencoded\"),a.send(b(r))}catch(e){console.warn(\"xmlhttp error\",e)}},S=function(e){var t=[];return n(e)&&e.forEach(function(e){var r={};for(var n in e)if(d[n]&&(r[d[n]]=e[n],\"resurl\"===d[n])){var o=c(e[n]),i=o.hostname,a=o.path;r.reshost=i,r.respath=a,r.httpcode=200}t.push(r)}),t},E=function(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{},r={},n=[];if(o(t))for(var i in t)-1!==l.indexOf(String(i))?(r[i]=t[i],n.push(i)):console.warn(i,\"could not be modify.\");return f({},e,r)},k=function(){var e=\"\";try{if(e=window.localStorage.getItem(\"emonitor.hc_pgv_pvid\"))return e;var t=(new Date).getTime();return e=\"ek\"+[t,Math.floor(t*Math.random()*Math.random()).toString().slice(-5)].join(\"\"),window.localStorage.setItem(\"emonitor.hc_pgv_pvid\",e),e}catch(t){return console.warn(\"emonitor.hc_pgv_pvid get error\",t),e}},T=function(e,t){var r=arguments.length>2&&void 0!==arguments[2]?arguments[2]:\"\",n=void 0;return String(t).split(\".\").forEach(function(t){try{n=void 0!==n?n[t]:e[t]}catch(e){n=void 0}}),void 0===n?r:n},j=function(e){var t=e.data,r=void 0===t?{}:t,a=e.path,s=void 0===a?\"\":a,c=\"\";if(o(r)){if(i(s))return T(r,s,\"\");if(n(s))return s.forEach(function(e){if(\"\"!==T(r,e,\"\"))return c=T(r,e),!1}),c}return\"\"},M=function(e){var t=(arguments.length>1&&void 0!==arguments[1]?arguments[1]:{})||{},r=t.code,n=t.msg,i={};if(o(e))i=e;else try{i=JSON.parse(e)}catch(e){i={}}return{bizcode:j({data:i,path:r}),bizmsg:j({data:i,path:n})}},O=function(e){var t=\"\";if(!i(e))return console.warn(\"name is not string\"),t;try{return 0===(t=g({name:e})).length&&(t=y(e)||\"\"),t}catch(e){return console.error(\"Automatically get the value of the corresponding name from the url or cookie \"+e),t}},C=navigator.userAgent,L=function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:C,t=\"unknown\",r=\"unknown\",n=\"unknown\",o=\"unknown\",i=\"unknown\",a=e.toLowerCase();if(a.indexOf(\"windows\")>-1?i=\"windows\":a.indexOf(\"linux\")>-1?i=\"linux\":a.indexOf(\"mac\")>-1&&(i=\"mac\"),window.opera)o=window.opera.version(),r=window.opera.version(),n=\"opera\";else if(/AppleWebKit\\/(\\S+)/.test(e))if(o=RegExp.$1,n=\"webkit\",/Chrome\\/(\\S+)/.test(e))r=RegExp.$1,t=\"chrome\";else if(/Version\\/(\\S+)/.test(e))r=RegExp.$1,t=\"safari\";else{var s=parseFloat(o);r=s<100?1:s<312?1.2:s<412?1.3:2,t=\"safari\"}else/KHTML\\/(\\S+)/.test(e)||/Konqueror\\/([^;]+)/.test(e)?(o=RegExp.$1,r=RegExp.$1,n=\"khtml\",t=\"konq\"):/rv:([^\\)]+)\\) Gecko\\/\\d{8}/.test(e)?(o=RegExp.$1,n=\"gecko\",/Firefox\\/(\\S+)/.test(e)&&(r=RegExp.$1,t=\"firefox\")):/MSIE ([^;]+)/.test(e)&&(o=RegExp.$1,r=RegExp.$1,n=\"ie\",t=\"ie\");return{machine:\"PC\",name:t,version:r,engineVer:o,engine:n,machineSys:i}},R=function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:C,t=L(e),r=t.machineSys,n={mac:\"mac\"===r,version:t.engineVer,name:r},o={},i={iphone:e.match(/(iphone)\\s(os\\s)?([\\d_]+)/i),ipad:e.match(/(ipad).*\\s([\\d_]+)/i),ipod:e.match(/(ipod).*\\s([\\d_]+)/i),android:e.match(/(android)\\s([\\d\\.]+)/i),windows:e.match(/Windows(\\s+\\w+)?\\s+?(\\d+\\.\\d+)/)};i.ipod&&(n.ios=!0,n.ipod=!0,n.version=i.ipod[2].replace(/_/g,\".\"),n.name=\"ipod\"),i.ipad&&(n.ios=!0,n.ipad=!0,n.version=i.ipad[2].replace(/_/g,\".\"),n.name=\"ipad\"),i.iphone&&(n.iphone=!0,n.ios=!0,n.version=i.iphone[3].replace(/_/g,\".\"),n.name=\"iphone\"),i.android&&(n.android=!0,n.version=i.android[2],n.name=\"android\"),i.windows&&(n.windows=!0,n.version=i.windows[2],n.name=\"windows\");var a={WEISHI:e.match(/weishi_(\\d+?\\.\\d+?\\.\\d+?)/i),YYB:e.match(/\\/qqdownloader\\/(\\d+)(?:\\/(appdetail|external|sdk))?/i),mojii:e.match(/mojii/i),IE:e.match(/; msie\\b|; trident\\b|\\bedge\\//i),WeChat:e.match(/MicroMessenger\\/((\\d+)\\.(\\d+))\\.(\\d+)/)||e.match(/MicroMessenger\\/((\\d+)\\.(\\d+))/),MQQClient:!e.match(/QQReader/)&&e.match(/QQ\\/(\\d+\\.\\d+)/i)||e.match(/V1_AND_SQ_([\\d\\.]+)/),MQQReader:e.match(/QQReader/i),QQvideo:!e.match(/TADChid/)&&e.match(/QQLiveBrowser\\/([\\d.]+)/),QQHDvideo:e.match(/QQLiveHDBrowser\\/([\\d.]+)/),TBSSDK:e.match(/MQQBrowser\\/(\\d+\\.\\d+)/i)&&e.match(/MSDK\\/(\\d+\\.\\d+)/),MQQBrowser:e.match(/MQQBrowser\\/(\\d+\\.\\d+)/i),UCBrowser:e.match(/ucbrowser\\/(\\d+\\.\\d+)/i),Qzone:e.match(/Qzone\\/[\\w\\d\\_]*(\\d\\.\\d)[\\.\\w\\d\\_]*/i),Weibo:e.match(/Weibo/i),qqnews:e.match(/qqnews\\/(\\d+\\.\\d+\\.\\d+)/i),QQLiveBroadcast:e.match(/QQLiveBroadcast/i),kuserAgentibao:e.match(/qnreading\\/(\\d+\\.\\d+\\.\\d+)/i),liebao:e.match(/LieBaoFast\\/(\\d+\\.\\d+\\.\\d+)/i),baiduboxapp:e.match(/baiduboxapp\\/(\\d+\\.\\d+\\.\\d+)/i),IEMobile:e.match(/IEMobile(\\/|\\s+)(\\d+\\.\\d+)/)||e.match(/WPDesktop/),douban:e.match(/com\\.douban\\.frodo\\/(\\d+\\.\\d+\\.\\d+)/i),MiuiBrowser:e.match(/MiuiBrowser\\/(\\d+\\.\\d+)/i),BingPreview:e.match(/BingPreview\\/(\\d+\\.)/i),TADChid:e.match(/TADChid\\/(\\d+)/i),Chrome:n.ios?e.match(/CriOS\\/(\\d+\\.\\d+)/):e.match(/Chrome\\/(\\d+\\.\\d+)/),Safari:e.match(/Safari\\/(\\d+\\.\\d+)/),sukan:e.match(/synopsis/i)};if(a.MQQReader)o.MQQReader=!0,o.version=1,o.name=\"MQQReader\";else if(a.IEMobile)o.IEMobile=!0,o.version=1,o.name=\"IEMobile\";else for(var s in a)if(a[s]){o[s]=!0,o.version=a[s][1]||0,o.name=s;break}return{browser:o,os:n}},Q=!1,q=function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},t=e.name,r=e.type;try{if(!window.performance||!window.performance.getEntries)return console.warn(\"prerformance is not supported\"),[];var n=performance.getEntries(),o=[];if(!n&&!n.length)return o;try{n.forEach(function(e){var t={name:e.name,time_redirect:e.redirectEnd-e.redirectStart,time_dns:e.domainLookupEnd-e.domainLookupStart,time_requestTime:e.responseEnd-e.requestStart,time_tcp:e.connectEnd-e.connectStart,type:e.initiatorType,starttime:Math.floor(e.startTime),entryType:e.entryType,duration:Math.floor(e.duration)||0,decodedBodySize:e.decodedBodySize||0,nextHopProtocol:e.nextHopProtocol,json_entries:JSON.stringify(e)};o.push(t)})}catch(e){console.error(\"get resourceTiming err::::\",e)}return t?o.filter(function(e){return e.name===t})[0]:r?o.filter(function(e){return e.type===r}):o}catch(e){return console.warn(\"get performance happen error\"),[]}},A={SCRIPT:\"script\",LINK:\"link\",IMG:\"img\",AUDIO:\"audio\",VIDEO:\"video\",FETCH:\"fetch\",AJAX:\"ajax\",PROMISE:\"promise\"},I=function(){var e=(new Date).getTime();return window.performance&&window.performance.now&&(e=window.performance.now()),e},P=function(e){var t={type:\"\",url:\"\",code:\"\",isErr:!1,source:e};if(!o(e))return t;if(s(e,\"err_type\")){t.type=e.err_type;try{if(e.err_desc){var r=JSON.parse(e.err_desc);t.url=r.url||r.fileName||e.s_url}else t.url=e.s_url}catch(r){t.url=e.s_url,console.warn(r)}t.isErr=!0}else s(e,\"cgiurl\")?(t.type=\"cgi\",t.url=e.cgiurl,t.code=e.httpcode):s(e,\"resurl\")?(t.type=\"cdn\",t.url=e.resurl,t.code=e.httpcode):s(e,\"time_domready\")?(t.type=\"pagepf\",t.url=e.s_url,t.code=200):s(e,\"json_entries\")?(t.type=\"slowlog\",t.url=e.s_url,t.code=200):s(e,\"log\")&&(t.type=\"flowlog\",t.url=e.s_url,t.code=200);return t},N=function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:v,t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:window;return a(e)?function(){for(var r=arguments.length,n=Array(r),o=0;o<r;o++)n[o]=arguments[o];try{e.apply(t,n)}catch(e){console.error(\"wrapTryCatch \"+e)}}:e},U=function(){function e(t){var r=t.options,n=void 0===r?{}:r,o=t.baseUrl,i=void 0===o?\"\":o,a=t.sampling,s=void 0===a?1:a,c=t.delay,d=void 0===c?2e3:c,u=t.name,p=t.params,l=void 0===p?[]:p,h=t.cgi,f=void 0===h?{}:h,v=t.ptag,g=t.onBeforeSend,y=t.debug,w=t.logs,_=void 0===w?{}:w,b=t.onProxy;m(this,e),this.options=n,this.errorList=[],this.baseUrl=i,this.sampling=s,this.params=l,this.delay=d,this.name=u,this.cgiOptions=f,this.ptag=v,this.onBeforeSend=g,this.debug=y,this.logs=_,this.onProxy=N(b,this)}return h(e,[{key:\"init\",value:function(){try{this.proxyAjax(),this.proxyError(),this.proxyConsole(),this.proxyFetch(),this.proxyJsonp(),this.injectOptions()}catch(e){this.send({err_msg:\"emonitor init happen error\",err_stack:\"\"+e,err_type:\"jserror\",err_function:\"emonitor init func\"})}}},{key:\"injectOptions\",value:function(){var e=this;e.params.forEach(function(t){e.options[t]=g({name:t})}),\"string\"==typeof e.options.qq&&e.options.qq.length>0&&(e.options.openid=\"\")}},{key:\"proxyConsole\",value:function(){var e=this;if(\"undefined\"!=typeof console&&\"function\"==typeof console.error){var t=console.error;console.error=function(){for(var r=arguments.length,n=Array(r),o=0;o<r;o++)n[o]=arguments[o];t.apply(window.console,n),e.send({err_msg:n.join(\",\"),level:\"error\",err_type:\"console\"})}}}},{key:\"log\",value:function(){var e=this,t=e.logs||{},r=t.baseUrl,n=t.sampling,i=void 0===n?1:n;if(r){for(var a={level:\"\",log:\"\"},s=arguments.length,c=Array(s),d=0;d<s;d++)c[d]=arguments[d];c.length>1?a={level:c[0],log:c[1]}:o(c[0])?a=c[0]:console.warn(\"log params is empty\"),Math.random()<=i&&e.send(f({},a),!1,r)}}},{key:\"reportCgi\",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},t=this,r=t.cgiOptions||{},n=r.baseUrl,o=r.sampling,i=void 0===o?1:o;if(n){var a=e.cgiurl,s=c(a),d=s.host,u=s.path;Math.random()<=i&&t.send(f({cgipath:u,cgihost:d,bizcode_server:\"\"},e),!1,n)}}},{key:\"proxyAjax\",value:function(){var e=Object.create(null),t=this,r=t.onProxy,n=(new Date).getTime(),o=null,i=null,s=function(e,r){i=I();try{var a={},s=e&&(e.currentTarget||e.target),c=s.emonitorAjaxURL,d=s.emonitorAjaxSendTime||n,u=s?{currentStatus:s.status,cgiurl:c||s.responseURL}:{},p=u.currentStatus,l=void 0===p?\"\":p,m=u.cgiurl,h=void 0===m?\"\":m,v=-1!==[\"\",\"text\"].indexOf(s.responseType)?s.responseText||null:null;if(\"timeout\"===String(r))return a={err_msg:\"ajax请求错误\",err_stack:\"错误码:\"+l,level:\"error\",err_type:A.AJAX,err_code:l,err_desc:JSON.stringify({fileName:h,category:\"ajax\",text:\"ajax request timeout\",status:l})},t.errorList.push(a),t.send(f({},a)),void(a={});var g=M(v,t.cgiOptions),y=g.bizcode,w=g.bizmsg;try{t.reportCgi({cgiurl:h,delay:Math.round(Math.max(i-d,0)),httpcode:l,bizcode:y,bizmsg:w,starttime:o})}catch(e){console.warn(e)}\"number\"==typeof l&&(l<200||l>=300)&&(a={err_msg:\"ajax请求错误\",err_stack:\"错误码:\"+l,level:\"error\",err_type:A.AJAX,err_code:l,err_desc:JSON.stringify({fileName:h,category:\"ajax\",text:s.statusText,status:l})},t.errorList.push(a),t.send(f({},a)),a={})}catch(e){console.error(\"Ajax handleEvent \"+e)}};e.send=XMLHttpRequest.prototype.send,e.open=XMLHttpRequest.prototype.open,XMLHttpRequest.prototype.open=function(t,r){var n=!(arguments.length>2&&void 0!==arguments[2])||arguments[2];e.open.apply(this,[t,r,n]),this.emonitorAjaxURL=r},XMLHttpRequest.prototype.send=function(t){o=(new Date).getTime(),n=I(),this.emonitorAjaxSendTime=n;var i=this.onloadend,c=this.ontimeout;this.onloadend=function(e){s(e),\"function\"==typeof i&&i.apply(this,arguments)},this.ontimeout=function(e){s(e,\"timeout\"),\"function\"==typeof c&&c.apply(this,arguments)},a(r)&&r({type:\"ajax\",data:t,extra:{url:this.emonitorAjaxURL}}),e.send.apply(this,[t])}}},{key:\"proxyFetch\",value:function(){var e=this,t=this.onProxy;if(window.fetch){var r=window.fetch;window.fetch=function(){for(var n=arguments.length,o=Array(n),s=0;s<n;s++)o[s]=arguments[s];var c=(new Date).getTime(),d=I(),u=d;return r.apply(null,o).then(function(r){u=I(),r.ok||\"\"===r.url||e.send({err_msg:\"fetch not ok\",err_type:A.FETCH,err_code:r.status,err_desc:JSON.stringify({type:\"error\",fileName:o[0],options:o[1],category:\"fetch\"})});var n=\"\",s=\"\";try{var p=r.headers?r.headers.get(\"content-type\"):\"\";i(p)&&-1!==p.indexOf(\"application/json\")&&r.clone().json().then(function(i){try{a(t)&&t({type:\"fetch\",data:i,extra:{url:o[0]}});var p=M(i,e.cgiOptions);n=p.bizcode,s=p.bizmsg}catch(e){n=\"\",s=\"\"}try{e.reportCgi({cgiurl:o[0],delay:Math.round(Math.max(u-d,0)),httpcode:r.status,bizcode:n,bizmsg:s,starttime:c})}catch(e){console.warn(e)}}).catch(function(e){console.error(\"getCgiInfo\",e)})}catch(e){console.error(\"getCgiInfo\",e)}return r}).catch(function(t){e.send({err_msg:\"fetch not ok\",err_stack:\"\"+t,err_type:A.FETCH,err_desc:JSON.stringify({type:\"error\",fileName:o[0],options:o[1],category:\"fetch\"})})})}}}},{key:\"proxyError\",value:function(){var e=this,t=arguments,r=this;window.addEventListener(\"error\",function(e){var t=e.target?e.target:e.srcElement;if(t!==window&&t.nodeName&&A[t.nodeName.toUpperCase()]){var n={err_msg:t.localName+\" is load error\",err_stack:\"resource is not found\",err_type:\"\"+t.localName,err_desc:JSON.stringify({tagName:t.localName,type:e.type,fileName:t.currentSrc||t.src||t.href,category:\"resource\"}),timestamps:(new Date).getTime()};r.errorList.push(n),r.send(f({},n))}},!0);var n=window.onerror;window.onerror=function(o,i,a,s,c){if(\"Script error.\"===o&&!i)return!1;var d={};return setTimeout(function(){var e=s||window.event&&window.event.errorCharacter||0;(d=c&&c.stack?{err_msg:o,err_stack:c.stack,err_type:\"jserror\"}:{err_msg:o,err_stack:\"\",err_type:\"jserror\"}).err_desc=JSON.stringify({url:r.URL,fileName:i,category:\"javascript\",line:a,col:e}),r.errorList.push(f({},d)),r.send(f({},d))},0),\"function\"==typeof n?n.apply(e,t):!r.debug},window.addEventListener(\"unhandledrejection\",function(e){var t={err_msg:e.reason,err_type:A.PROMISE,err_desc:JSON.stringify({url:location.href,category:\"promise\"}),err_stack:\"promise is error\"};r.errorList.push(t),r.send(t),e.preventDefault()},!0)}},{key:\"proxyJsonp\",value:function(){var e=this,t=window.document.createElement,r=null,n=null,o=function(t,o){var i=e.cgiOptions.jsonp,a=void 0!==i&&i;if(\"function\"==typeof a&&a(t)||\"boolean\"==typeof a&&a){var s=\"load\"===o.type?200:500,d=c(t.src),u=d.host,p=d.path;e.reportCgi({cgiurl:t.src,delay:Math.round(Math.max(I()-n,0)),httpcode:s,cgipath:p,cgihost:u,starttime:r})}n=null,r=null};window.document.createElement=function(){var e=t.apply(this,arguments);return\"script\"===arguments[0]&&(r=(new Date).getTime(),n=I(),e.addEventListener(\"load\",function(t){o(e,t)},!0),e.addEventListener(\"error\",function(t){o(e,t)},!0)),e}}},{key:\"config\",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};for(var t in e)-1!==[\"baseUrl\",\"params\",\"options\",\"sampling\",\"delay\",\"name\",\"cgi\"].indexOf(String(t))&&(this[t]=e[t]);return this}},{key:\"send\",value:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},t=arguments.length>1&&void 0!==arguments[1]&&arguments[1],r=arguments[2],n=this,i=(n.cgiOptions||{}).btrace,s=void 0!==i&&i,d=e.err_type,u=void 0===d?\"console\":d;if(w(n.sampling)){var l=\"jserror\"===u?300:n.delay,m=_(x,l,function(){n.errorList=[]}),h=c(location.href),v=h.host,g=h.path,y=h.protocol,b=R(navigator.userAgent),S=b.browser,T=b.os,j=f({},this.options,e,{timestamps:(new Date).getTime(),_dc:Math.random(),dtime:(new Date).getTime(),hh_ua:navigator.userAgent,hh_uav:S.version,hh_ref:document.referrer,hc_pgv_pvid:k(),s_url:location.href,s_host:v,s_path:g,s_protocol:y,s_browser:S.name,s_os:T.name,s_qq:O(\"qq\"),s_openid:O(\"openid\"),s_app:n.name,s_ptag:n.ptag}),M=n.onBeforeSend;if(a(M))try{var C=P(j),L=M(C);if(\"boolean\"==typeof L&&!L)return;if(\"img\"===C.type&&C.url===C.source.s_url)return;if(-1!==p.indexOf(C.type)&&\"btrace.qq.com\"===c(C.url).hostname&&!s)return;o(L)&&(j=E(j,L))}catch(e){console.warn(e)}m({baseUrl:r||n.baseUrl,data:j,method:t?\"POST\":\"GET\"})}}}]),e}(),B=c;e.create=function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},t=e.options,r=void 0===t?{}:t,n=e.cgi,o=void 0===n?{sampling:1,baseUrl:\"\"}:n,i=e.logs,a=void 0===i?{sampling:1,baseUrl:\"\"}:i,s=e.baseUrl,c=e.delay,d=void 0===c?2e3:c,u=e.sampling,p=void 0===u?1:u,l=e.name,m=void 0===l?\"unknown\":l,h=e.params,f=void 0===h?[]:h,g=e.ptag,y=void 0===g?\"\":g,w=e.debug,_=void 0!==w&&w,b=e.onBeforeSend,x=void 0===b?v:b,S=e.onProxy,E=new U({options:r,baseUrl:s,params:f,delay:d,name:m,sampling:p,cgi:o,ptag:y,debug:_,onBeforeSend:x,logs:a,onProxy:void 0===S?v:S});return E.init(),E},e.getRcTiming=q,e.getPfTiming=function(){try{var e=window.performance||window.webkitPerformance||window.msPerformance||window.mozPerformance;if(void 0===e)return!1;var t=e.timing,r={};return r.time_response=t.responseStart-t.requestStart,r.time_firstpaint=t.responseEnd-t.responseStart,r.time_domready=t.domContentLoadedEventStart-t.responseEnd,r.time_readyStart=t.fetchStart-t.navigationStart,r.time_redirectTime=t.redirectEnd-t.redirectStart,r.time_appcacheTime=t.domainLookupStart-t.fetchStart,r.time_dns=t.domainLookupEnd-t.domainLookupStart,r.time_tcp=t.connectEnd-t.connectStart,r.time_requestTime=t.responseEnd-t.requestStart,r.time_initDomTreeTime=t.domInteractive-t.responseEnd,r.time_loadEventTime=t.loadEventEnd-t.loadEventStart,r.time6=t.domLoading-t.fetchStart,r.time_whiteScreen=t.domLoading-t.fetchStart,r.time7=t.loadEventEnd-t.fetchStart,r.time_firstScreenTime=t.loadEventEnd-t.fetchStart,r.time_parseDomTree=t.domComplete-t.domInteractive,r.time8=\"\",r}catch(e){return console.warn(\"err\",e),{}}},e.getCdnTiming=function(){var e={};return u.forEach(function(t){e[t]=S(q({type:t}))}),e},e.getSysInfo=R,e.getUrlParam=g,e.getCookie=y,e.injectVconsole=function(){try{var e=document.createElement(\"script\"),t=function(){Q||(Q=new window.VConsole)};e.src=\"https://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/vconsole/3.0.0/vconsole.min.js\",e.async=!0,e.charset=\"utf-8\",e.onload=t,e.onreadystatechange=function(){\"complete\"===this.readyState&&t()},document.getElementsByTagName(\"head\")[0].appendChild(e)}catch(e){console.error(\"vConsole script load err\")}},e.getLinkInfo=B,e.doReport=x,Object.defineProperty(e,\"__esModule\",{value:!0})});\n" +
                "</script>\n" +
                "<!-- 执行啄木鸟监控初始化逻辑 -->\n" +
                "<script charset=\"utf-8\">\n" +
                "    var isTimingReported = false;\n" +
                "    var _MAXTIMEOUT = 10000;\n" +
                "    var nativeToString = Object.prototype.toString;\n" +
                "    var bossInfo = {\n" +
                "        page: '//btrace.qq.com/kvcollect?BossId=6529&Pwd=1714580587', //页面质量上报\n" +
                "        error: '//btrace.qq.com/kvcollect?BossId=6527&Pwd=1102151080', // 页面错误上报\n" +
                "        slowlog: '//btrace.qq.com/kvcollect?BossId=6523&Pwd=1202531240', //慢日志上报\n" +
                "        cgi: '//btrace.qq.com/kvcollect?BossId=6528&Pwd=96045012', // cgi上报\n" +
                "        resource: '//btrace.qq.com/kvcollect?BossId=6958&Pwd=1123576360', // 素材质量上报\n" +
                "        flowlog: '//btrace.qq.com/kvcollect?BossId=6526&Pwd=878966364', // 流水日志上报\n" +
                "    };\n" +
                "    var emonitorIns = emonitor.create({\n" +
                "        baseUrl: bossInfo.error,\n" +
                "        name: 'TencenAC-H5',\n" +
                "        onBeforeSend: function(data) {\n" +
                "        // 在数据上报前调用 可以用作数据过滤，\n" +
                "        // (1)仅有return false 不上报数据；(2)当返回object对象，支持修改's_path', 's_traceid', 's_guid', 'hc_pgv_pvid', 's_omgid','err_desc';\n" +
                "        // data.type 上报类型 console，link，script，ajax，fetch，promise，img，audio，video，cgi\n" +
                "        // data.url 请求链接\n" +
                "        // data.code 请求状态，默认为空\n" +
                "        // data.isErr 是否是异常上报\n" +
                "        // data.source 上报的原始数据\n" +
                "        // 应用场景: 日志管理, 白名单等\n" +
                "        // console.log('report data::::', data);\n" +
                "        // 简单的全日志存储方案\n" +
                "        // 通过返回对象，修改特定上报字段。生产环境，按需使用!。\n" +
                "        // return { s_path: '/test', s_traceid: '1111'}\n" +
                "        // return { err_desc: 'test'};\n" +
                "        },\n" +
                "        // debug: true, 开发环境开启debug=true，window.onerror捕获的错误会展示在控制台\n" +
                "        cgi: {\n" +
                "            baseUrl: bossInfo.cgi,\n" +
                "            sampling: 1, // 默认采样率 可根据实际情况调整\n" +
                "            code: 'status',\n" +
                "            // msg: ['info'],\n" +
                "        },\n" +
                "        logs: {\n" +
                "        baseUrl: bossInfo.flowlog,\n" +
                "        },\n" +
                "    });\n" +
                "    function doCdnReport() {\n" +
                "        var emRcInfo = emonitor.getCdnTiming();\n" +
                "        for (var emRc in emRcInfo) {\n" +
                "            if (emRcInfo[emRc].length > 0) {\n" +
                "                emRcInfo[emRc].forEach(function(re) {\n" +
                "                    emonitorIns.config({\n" +
                "                        baseUrl: bossInfo.resource\n" +
                "                    }).send(re);\n" +
                "                });\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    setTimeout(function() {\n" +
                "        // 慢日志上报\n" +
                "        if (!isTimingReported) {\n" +
                "            var _resources = emonitor.getRcTiming();\n" +
                "            try {\n" +
                "                if (nativeToString.call(_resources) === '[object Array]') {\n" +
                "                    var _resourcesLen = _resources.length;\n" +
                "                    var _jsonEntries = [];\n" +
                "                    for (var _i = 0; _i < _resourcesLen; _i++) {\n" +
                "                        _jsonEntries.push(\n" +
                "                        _resources[_i].starttime +\n" +
                "                            '|' +\n" +
                "                            _resources[_i].duration +\n" +
                "                            '|' +\n" +
                "                            _resources[_i].name\n" +
                "                        );\n" +
                "                    }\n" +
                "                    emonitorIns.config({\n" +
                "                            baseUrl: bossInfo.slowlog\n" +
                "                        }).send({\n" +
                "                            json_entries: JSON.stringify(_jsonEntries)\n" +
                "                        },true);\n" +
                "                    emonitorIns.config({\n" +
                "                        baseUrl: bossInfo.error\n" +
                "                    });\n" +
                "                    // 通过类型获取一系列资源加载情况\n" +
                "                    emonitorIns.getRcTiming({\n" +
                "                        type: 'img' \n" +
                "                    });\n" +
                "                }\n" +
                "            } catch (err) {\n" +
                "                console.warn('emonitorIns send', err);\n" +
                "            }\n" +
                "        }\n" +
                "    }, _MAXTIMEOUT);\n" +
                "    window.addEventListener('load',function() {\n" +
                "        setTimeout(function() {\n" +
                "            if (!isTimingReported) {\n" +
                "                if (typeof onLoad === 'function') {\n" +
                "                    onLoad();\n" +
                "                } else {\n" +
                "                    emonitorIns\n" +
                "                    .config({\n" +
                "                        baseUrl: bossInfo.page\n" +
                "                    })\n" +
                "                    .send(emonitor.getPfTiming());\n" +
                "                    doCdnReport();\n" +
                "                    emonitorIns.config({\n" +
                "                    baseUrl: bossInfo.error\n" +
                "                    });\n" +
                "                }\n" +
                "                isTimingReported = true;\n" +
                "                }\n" +
                "            }, 0);\n" +
                "        },false);\n" +
                "    emonitorIns.log({\n" +
                "        level: 'info',\n" +
                "        log: 'emonitor supprt flow log',\n" +
                "    });\n" +
                "    emonitorIns.log('warn', 'warn emonitor supprt flow log');\n" +
                "</script>\n" +
                "\n" +
                "<meta charset=\"UTF-8\" />\n" +
                "\n" +
                "<!-- 百度站长工具校验代码 -->\n" +
                "<meta name=\"baidu-site-verification\" content=\"iCOfamZT3B\" />\n" +
                "\n" +
                "<!-- 关闭iOS电话号码识别、配置状态栏效果 -->\n" +
                "<meta name=\"format-detection\" content=\"telephone=no\" />\n" +
                "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />\n" +
                "<meta name=\"mobile-web-app-capable\" content=\"yes\" />\n" +
                "<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"white\" />\n" +
                "\n" +
                "<!-- iOS主屏&书签图标 -->\n" +
                "<link rel=\"apple-touch-icon-precomposed\" sizes=\"57x57\" href=\"//gtimg.ac.qq.com/h5/public/images/touch-icon-iphone-114.png?v=35ef6fea80e50951\" />\n" +
                "<link rel=\"apple-touch-icon-precomposed\" sizes=\"114x114\" href=\"//gtimg.ac.qq.com/h5/public/images/touch-icon-iphone-114.png?v=35ef6fea80e50951\" />\n" +
                "<link rel=\"apple-touch-icon-precomposed\" sizes=\"72x72\" href=\"//gtimg.ac.qq.com/h5/public/images/touch-icon-ipad-144.png?v=784fdda39985ba20\" />\n" +
                "<link rel=\"apple-touch-icon-precomposed\" sizes=\"144x144\" href=\"//gtimg.ac.qq.com/h5/public/images/touch-icon-ipad-144.png?v=784fdda39985ba20\" />\n" +
                "<link rel=\"shortcut icon\" href=\"//gtimg.ac.qq.com/h5/public/images/touch-icon-iphone-114.png?v=35ef6fea80e50951\">\n" +
                "<link rel=\"shortcut icon\" sizes=\"196x196\" href=\"//gtimg.ac.qq.com/h5/public/images/touch-icon-iphone-196.png?v=271259d431f48a94\">\n" +
                "\n" +
                "<!-- QQ浏览器锁定竖屏、全屏、应用模式 -->\n" +
                "<!--<meta name=\"x5-orientation\" content=\"portrait\"/>-->\n" +
                "<!--<meta name=\"x5-fullscreen\" content=\"true\"/>-->\n" +
                "<meta name=\"x5-pagetype\" content=\"webapp\" />\n" +
                "\n" +
                "<!-- UC浏览器锁定竖屏、全屏、应用模式 -->\n" +
                "<!--<meta name=\"screen-orientation\" content=\"portrait\"/>-->\n" +
                "<!--<meta name=\"full-screen\" content=\"yes\"/>-->\n" +
                "<!--<meta name=\"browsermode\" content=\"application\"/>-->\n" +
                "\n" +
                "<!-- 分享信息 -->\n" +
                "<meta itemprop=\"image\" content=\"https://gtimg.ac.qq.com/h5/public/images/share-icon.png?v=7ee367fb9d2965dc\" />\n" +
                "<meta itemprop=\"name\" content=\"腾讯动漫\" />\n" +
                "<meta name=\"description\" itemprop=\"description\" content=\"中国最大最权威的正版动漫网站，连载众多原创国漫，原创动画，正版日漫等海内外最热正版动漫内容\" /> \n" +
                "<!--运营商限制-->\n" +
                "<meta http-equiv=\"Content-Security-Policy\"\n" +
                "      content=\"default-src *.qq.com *.gtimg.com *.gtimg.cn *.qpic.cn *.qlogo.cn *.idqqimg.com 'self' data: callclient: txcomic: txcomicin: txcomicout: mtt: jsbridge: TcsJSBridge: 'unsafe-inline' 'unsafe-eval' https:; img-src *.qq.com *.gtimg.com *.gtimg.cn *.qpic.cn *.qlogo.cn *.idqqimg.com 'self' data: callclient: txcomic: txcomicin: txcomicout: mtt: jsbridge: TcsJSBridge: 'unsafe-inline' 'unsafe-eval' https:; media-src http: https:\"/>\n" +
                "\n" +
                "\n" +
                "<meta name=\"page-name\" content=\"search\" />\n" +
                "\n" +
                "<!-- 通用样式与脚本 -->\n" +
                "<link rel=\"stylesheet\" href=\"//gtimg.ac.qq.com/h5/public/css/core.css?v=de7aab02d55770c9\" />\n" +
                "<script>\n" +
                "  window.CLIENT_URL_ANDROID = '';\n" +
                "  window.CLIENT_URL_IOS = '';\n" +
                "\n" +
                "  window.pageAction = window.pageAction || {};\n" +
                "window.presetPageAction = window.presetPageAction || {};\n" +
                ";\n" +
                "  /**\n" +
                " * Created by krimeshu on 2016/6/6.\n" +
                " */\n" +
                "\n" +
                "(function () {\n" +
                "    function Blocker(opts) {\n" +
                "        opts = opts || {};\n" +
                "        var timeKey = opts.timeKey || 'default';\n" +
                "\n" +
                "        this.KEY = 'H5_BLOCKER-' + timeKey;\n" +
                "        this.EXPIRE_DAYS = 2;\n" +
                "        // this.BLOCK_TIME = ['00:00', '23:59'];\n" +
                "    }\n" +
                "\n" +
                "    Blocker.prototype = {\n" +
                "        check: function () {\n" +
                "            var isPad = this.isPad(),\n" +
                "                hasBlocked = this.getCookie(this.KEY),\n" +
                "                // now = new Date(),\n" +
                "                // today = [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'),\n" +
                "                // blockTimeStart = new Date(today + ' ' + this.BLOCK_TIME[0]),\n" +
                "                // blockTimeEnd = new Date(today + ' ' + this.BLOCK_TIME[1]),\n" +
                "                // isBlockTime = now >= blockTimeStart && now < blockTimeEnd;\n" +
                "                isBlockTime = true;\n" +
                "            return (!isPad && !hasBlocked && isBlockTime);\n" +
                "        },\n" +
                "        checkAndGo: function () {\n" +
                "            if (this.check()) {\n" +
                "                this.rememberBlocked();\n" +
                "                this.goBlockPage();\n" +
                "            }\n" +
                "        },\n" +
                "        checkAndDialog: function () {\n" +
                "            if (this.check()) {\n" +
                "                this.rememberBlocked();\n" +
                "                this.showBlockDialog();\n" +
                "            }\n" +
                "        },\n" +
                "        rememberBlocked: function () {\n" +
                "            var expire = this.EXPIRE_DAYS * 24 * 60;\n" +
                "            this.setCookie(this.KEY, new Date().getTime(), expire, '.m.ac.qq.com', '/');\n" +
                "        },\n" +
                "        goBlockPage: function () {\n" +
                "            var channelIDs = { 'index': 1 },\n" +
                "                channelID = channelIDs['search'];\n" +
                "            window.location.href = '/block.html?retUrl=' + encodeURIComponent(window.location.href) +\n" +
                "                (channelID ? '&channelID=' + channelID : '');\n" +
                "        },\n" +
                "        showBlockDialog: function () {\n" +
                "            var self = this,\n" +
                "                dialog = window.blockDialog;\n" +
                "            if (dialog) {\n" +
                "                dialog.open();\n" +
                "            } else {\n" +
                "                window.setTimeout(function () {\n" +
                "                    self.showBlockDialog();\n" +
                "                }, 50);\n" +
                "            }\n" +
                "        },\n" +
                "        getCookie: function (key) {\n" +
                "            var allcookies = document.cookie;\n" +
                "            var cookiesReg = new RegExp('(^|;)\\\\s*' + key + '\\\\s*(=\\\\s*([^;$]*?))?(;|$)');\n" +
                "            var groups = cookiesReg.exec(allcookies);\n" +
                "            return decodeURIComponent(groups ? (groups[3] || '') : '');\n" +
                "        },\n" +
                "        setCookie: function (key, value, expires, domain, path) {\n" +
                "            var values = [key + '=' + encodeURIComponent(value)];\n" +
                "            if (expires) {\n" +
                "                if (typeof expires === 'number') {\n" +
                "                    var expiresTime = new Date();\n" +
                "                    expiresTime.setMinutes(expiresTime.getMinutes() + expires);\n" +
                "                    values.push('expires=' + expiresTime.toGMTString());\n" +
                "                } else {\n" +
                "                    values.push('expires=' + expires);\n" +
                "                }\n" +
                "            }\n" +
                "            values.push('domain=' + (domain || '.ac.qq.com'));\n" +
                "            values.push('path=' + (path || '/'));\n" +
                "            document.cookie = values.join(';');\n" +
                "        },\n" +
                "        isPad: function () {\n" +
                "            var ua = window.navigator.userAgent;\n" +
                "            return /(^| |\\()iPad(;| |\\/|$)/.test(ua);\n" +
                "        }\n" +
                "    };\n" +
                "\n" +
                "    window.Blocker = Blocker;\n" +
                "})();;\n" +
                "</script>\n" +
                "<!-- fragEnd: withOutDynamicConfig -->\n" +
                "\n" +
                "\n" +
                "  <script>\n" +
                "    window.CLIENT_URL_ANDROID = 'http://m.ac.qq.com/apkSet/qqcomic_android_dm5004.apk';\n" +
                "    window.CLIENT_URL_IOS = 'https://itunes.apple.com/app/apple-store/id569387346?pt=604140&ct=h5zhuzhan1&mt=8';\n" +
                "\n" +
                "    (function autoBlock() {\n" +
                "      // 拦截类型：页面、忽略、对话框（默认）\n" +
                "      var specBlockType = {\n" +
                "        'index': 'ignore',\n" +
                "        'chapter': 'ignore',\n" +
                "        'comic_info': 'ignore'\n" +
                "      };\n" +
                "      // 根据页面名称确定拦截类型，并做处理\n" +
                "      var blockType = specBlockType['search'];\n" +
                "      // 微博里不适合跳转拦截页，强制改为弹窗\n" +
                "      if (/(^| )(Weibo)( |\\/|$|\\()/i.test(navigator.userAgent)) {\n" +
                "        blockType = null;\n" +
                "      }\n" +
                "      switch (blockType) {\n" +
                "        case 'page':\n" +
                "          new Blocker().checkAndGo();\n" +
                "          break;\n" +
                "        case 'ignore':\n" +
                "          break;\n" +
                "        default:\n" +
                "          new Blocker().checkAndDialog();\n" +
                "          break;\n" +
                "      }\n" +
                "    })();\n" +
                "  </script>\n" +
                "  <!-- fragEnd: withOutViewPort -->\n" +
                "    <title>搜索 - 腾讯动漫</title>\n" +
                "    <link rel=\"stylesheet\" href=\"//gtimg.ac.qq.com/h5/search/css/result.css?v=447c6037f8e804d3\"/>\n" +
                "</head>\n" +
                "<body class=\"\">\n" +
                "\n" +
                "<!-- 顶栏 -->\n" +
                "<header class=\"top-bar #bar_style#\">\n" +
                "    <a class=\"btn-top back\">[返回]</a>\n" +
                "\n" +
                "    <h1 class=\"top-title\">搜索：我的</h1>\n" +
                "\n" +
                "    <div class=\"top-logo\">[腾讯动漫LOGO]</div>\n" +
                "\n" +
                "    <div class=\"padding\"></div>\n" +
                "    <a class=\"btn-top search\" href=\"/search/index\">[搜索]</a>\n" +
                "    <a class=\"btn-top menu\">[菜单]</a>\n" +
                "</header>\n" +
                "<!-- 导航菜单 -->\n" +
                "<div class=\"nav-menu-box\">\n" +
                "    <div class=\"nav-menu-bg\"></div>\n" +
                "    <div class=\"nav-menu\">\n" +
                "        <ul class=\"nav-list\">\n" +
                "            <li class=\"item\" data-name=\"index\" data-button-id=\"10031\">\n" +
                "                <a class=\"link\" href=\"/\">\n" +
                "                    <i class=\"icon index\">[首页]</i>\n" +
                "                    <span class=\"name\">首页</span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "            <li class=\"item\" data-name=\"category\" data-button-id=\"10006\">\n" +
                "                <a class=\"link\" href=\"/category/index\">\n" +
                "                    <i class=\"icon category\">[分]</i>\n" +
                "                    <span class=\"name\">分类</span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "            <li class=\"item\" data-name=\"rank\" data-button-id=\"10007\">\n" +
                "                <a class=\"link\" href=\"/rank/index?type=rise\">\n" +
                "                    <i class=\"icon rank\">[奖]</i>\n" +
                "                    <span class=\"name\">排行</span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "            <li class=\"item\" data-name=\"bookshelf\" data-button-id=\"10003\">\n" +
                "                <a class=\"link top-bar-collect\" href=\"javascript:void(0)\">\n" +
                "                    <i class=\"icon collect\">[袋]</i>\n" +
                "                    <span class=\"name\">收藏</span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "            <li class=\"item\" data-name=\"history\" data-button-id=\"10004\">\n" +
                "                <a class=\"link\" href=\"/bookshelf/index?type=his\">\n" +
                "                    <i class=\"icon history\">[钟]</i>\n" +
                "                    <span class=\"name\">历史</span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "            <li class=\"item\" data-name=\"support\" data-button-id=\"10008\">\n" +
                "                <a class=\"link support\" href=\"javascript:void(0);\">\n" +
                "                    <i class=\"icon feedback\">[反馈]</i>\n" +
                "                    <span class=\"name\">反馈</span>\n" +
                "                </a>\n" +
                "            </li>\n" +
                "        </ul>\n" +
                "        <a class=\"btn-home\" href=\"javascript:void(0)\" data-button-id=\"10009\">[个人中心]</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<!-- 搜索结果 -->\n" +
                "<section class=\"result-box under-top-bar\">\n" +
                "                <!-- 结果列表 ST -->\n" +
                "        <ul class=\"result-list\" id=\"lst_searchResult\">\n" +
                "            <li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/636396\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/21_16_10_d7a2e4bb914a82321eb45dc1b2b8946e_1548058201246.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">鸡汤皇后</strong>\n" +
                "            <small class=\"comic-update\">2020-08-07 更新</small>\n" +
                "            <small class=\"comic-tag\">古风 大女主</small>\n" +
                "            <small class=\"comic-desc\">【每周四、周日更新】卫瑜槿知道自己在做梦，在梦里，她看了一场好戏。\n" +
                "这个戏是个古装剧，剧中的女主角很不巧，也叫卫瑜槿。\n" +
                "而她的出场角色，是皇后。</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/645332\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/27_22_59_ad896c6078941b072bdc539aaea06166_1595861975547.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我为邪帝</strong>\n" +
                "            <small class=\"comic-update\">2020-08-07 更新</small>\n" +
                "            <small class=\"comic-tag\">系统 后宫</small>\n" +
                "            <small class=\"comic-desc\">纵横万界，史上最肉、最帅邪帝！\n" +
                "    绝世美男子谢焱一朝穿越掉进妖女窝，为了不被采补至死，穿梭诸天万界，斩位面之子，拒联邦洋夷……终成一代邪帝的故事。\n" +
                "   周二。周五更新  不定时有加更福利~快去评论区催更吧~\n" +
                "   QQ群：【我为邪帝】975576675</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/646239\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/19_18_53_8ada44ddf26054179bca3104f083d1ab_1584615199968.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我捡起了一地属性</strong>\n" +
                "            <small class=\"comic-update\">2020-08-07 更新</small>\n" +
                "            <small class=\"comic-tag\">穿越 战斗</small>\n" +
                "            <small class=\"comic-desc\">游戏肝帝风夏穿越到末法时代的修真世界，凭借属性异能和在游戏中培养出来的战略手段一路打爆敌人，获得敌人的属性及技能，扭转乾坤！还有各路兽娘美少女……通通要往碗里来！\n" +
                "敌人得意：“我的盖世神功天下无敌！”\n" +
                "风夏一拳将其打爆！在敌人面前捏碎属性光团：“多谢，现在它是我的盖世神功了！”</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/530969\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/16_13_10_b7fb5155b13475e52cab389d348ff08d_1592284221839.png/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我的天劫女友</strong>\n" +
                "            <small class=\"comic-update\">2020-08-05 更新</small>\n" +
                "            <small class=\"comic-tag\">战斗 神仙</small>\n" +
                "            <small class=\"comic-desc\">现代修仙界，尔虞我诈，弱肉强食，人人自危。保安马英雄救下修仙少女之后，误打误撞的开始了修行之路........读者13号群：456529881~</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/621058\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/09_21_59_9cc0de380d9afe468d2f6f7a547a9408.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我是大神仙</strong>\n" +
                "            <small class=\"comic-update\">2020-08-06 更新</small>\n" +
                "            <small class=\"comic-tag\">古风 穿越</small>\n" +
                "            <small class=\"comic-desc\">年仅七岁却超速生长的短命神童时江，为恢复正常生活，带着寄宿体内的某位大神仙闯入仙界，从此走上成为仙界大亨的传奇之路……</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/635142\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/24_13_32_e0d85ee7fe660aa5e6a264f349cabfa1_1582522366246.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我的女友是恶龙</strong>\n" +
                "            <small class=\"comic-update\">2020-08-01 更新</small>\n" +
                "            <small class=\"comic-tag\">后宫 萌系</small>\n" +
                "            <small class=\"comic-desc\">【车速越来越快】【男性向·恋爱·爆笑·战斗】原本要救的公主竟然是恶龙，方士同学招架的住吗？恶龙小公主、女扮男装的长腿美女、合法萝莉师姐、随身携带“凶器”的师妹……来自东大陆的方士拾夜在西方大陆扮猪吃老虎的福利之旅，由此展开！</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/641382\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/19_19_07_9a79b5b82752ffae17f2d300555b07b0_1563534459647.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我的守护女友</strong>\n" +
                "            <small class=\"comic-update\">2020-08-04 更新</small>\n" +
                "            <small class=\"comic-tag\">丧尸 逆袭</small>\n" +
                "            <small class=\"comic-desc\">末世来临，凌默的异能觉醒，他发现自己居然可以控制丧尸。\n" +
                "凌默利用自己的异能穿过尸潮，终于找到了心爱的女友—叶恋。却发现她已经成为了变异丧尸。\n" +
                "为了帮助叶恋恢复神智，凌默踏上了末世求生之旅……</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/634724\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/09_13_08_1517ee87c2f2e7c199f40961f13e9142_1533791334326.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我老婆是魔王大人</strong>\n" +
                "            <small class=\"comic-update\">2020-08-02 更新</small>\n" +
                "            <small class=\"comic-tag\">穿越 战斗</small>\n" +
                "            <small class=\"comic-desc\">高能预警！我和女魔王的同居生活！脑洞无限大！</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/647607\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/19_19_20_44bd89b810a272ac1c29dcfc38485a32_1595157640831.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我偏要浪</strong>\n" +
                "            <small class=\"comic-update\">2020-08-05 更新</small>\n" +
                "            <small class=\"comic-tag\">穿越 后宫</small>\n" +
                "            <small class=\"comic-desc\">超神舰长穿越咸鱼异界，后宫无双唯他独浪！敌人：明明这个新手村安分了数千年，咋就天降个满级大佬咧？战神妻子，狐妖老婆，恶魔女王，血族公主，剑圣御姐，挂比妹妹，萌宠蛊娘，兽耳女仆都觉得很棒！</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "<li class=\"comic-item\">\n" +
                "    <a class=\"comic-link\" href=\"/comic/index/id/526730\">\n" +
                "        <div class=\"comic-cover\">\n" +
                "            <img class=\"cover-image\" src=\"https://manhua.qpic.cn/vertical/0/23_16_54_4ebfa5300be69bc921be47e9b3886ea1_1524473640744.jpg/210\">\n" +
                "        </div>\n" +
                "        <div class=\"comic-content\">\n" +
                "            <strong class=\"comic-title\">我为苍生</strong>\n" +
                "            <small class=\"comic-update\">2020-08-06 更新</small>\n" +
                "            <small class=\"comic-tag\">灵异 惊悚</small>\n" +
                "            <small class=\"comic-desc\">那些年我相信有一种律法叫天道，那些年我知道有一种命运叫轮回。那些年我掌握了一种占卜叫经验，那些年我记得有一种长生不死叫僵尸，那些年，那些我遇到的离奇诡异的事件！每周一，四双更新！二群：483231168 三群：486489010 四群：175478936 五群：555312333</small>\n" +
                "        </div>\n" +
                "    </a>\n" +
                "</li>\n" +
                "        </ul>\n" +
                "        <!-- 结果列表 ED -->\n" +
                "    </section>\n" +
                "\n" +
                "<script src=\"//gtimg.ac.qq.com/h5/public/js/core.js?v=866ae8e07bf139cc\" crossorigin></script>\n" +
                "\n" +
                "<script>\n" +
                "    //暂时注释Sentry 监控上报\n" +
                "    // Raven.config('https://42af86c49b6844ffa1e6cb1b400776ad@report.url.cn/sentry/1121').install();\n" +
                "\n" +
                "    window.HOT_TAG_ROOT = 'h5_v3.';\n" +
                "\n" +
                "    ek.share.setShare({\n" +
                "        link: ek.url.appendQueryString(window.location.href, {'shared': 1}),\n" +
                "        img_url: $('meta[itemprop=\"image\"]').attr('content'),\n" +
                "        title: $('meta[itemprop=\"name\"]').attr('content'),\n" +
                "        desc: $('meta[itemprop=\"description\"]').attr('content'),\n" +
                "        callback: function (res) {\n" +
                "            console.log('分享结果：', res);\n" +
                "        }\n" +
                "    });\n" +
                "\n" +
                "    ek.ping.init({\n" +
                "        androidAppUrl: CLIENT_URL_ANDROID || 'http://m.ac.qq.com/apkSet/qqcomic_android_dm2309.apk',\n" +
                "        iOSAppUrl: CLIENT_URL_IOS || 'https://itunes.apple.com/us/app/teng-xun-wei-man/id569387346?ls=1&mt=8',\n" +
                "        virtualUrl: function () {\n" +
                "            var pathName = ek.url.getPathName();\n" +
                "            var pathArgs = pathName.split('/');\n" +
                "            if (/^\\/Chapter\\/index/i.test(pathName)) {\n" +
                "                // 浏览页特殊处理\n" +
                "                pathArgs[1] = 'Comic';\n" +
                "                pathArgs[2] = 'view';\n" +
                "            } else if (/^\\/Bookshelf\\/index/i.test(pathName)) {\n" +
                "                // 书架特殊处理\n" +
                "                var type = ek.url.getQueryString('type') || 'fav';  // 只做统计，不输出、不执行\n" +
                "                pathArgs[1] += '_' + type;\n" +
                "            } else if (!pathArgs[1] && !pathArgs[2]) {\n" +
                "                // 首页补全处理\n" +
                "                pathArgs = '/Index/index'.split('/');\n" +
                "            } else if (!pathArgs[2]) {\n" +
                "                // 其它补全处理\n" +
                "                pathArgs.push('index');\n" +
                "            }\n" +
                "            // 掌火特殊处理\n" +
                "            // 合作渠道页面全部下线   20190731  janwardchen\n" +
                "            // var isCfApp = /CfApp( |\\/|$)/i.test(window.navigator.userAgent);\n" +
                "            // var client = isCfApp ? 'cfApp' : ek.clientType;\n" +
                "            var client = ek.clientType;\n" +
                "            pathArgs.splice(3, 0, 'client');\n" +
                "            pathArgs.splice(4, 0, client);\n" +
                "            return pathArgs.join('/');\n" +
                "        },\n" +
                "        virtualDomain: 'm.ac.qq.com',\n" +
                "        downAppTag: function () {\n" +
                "            // 根据页面名称生成下载的按键统计标记\n" +
                "            var pageName = $('meta[name=\"page-name\"]').attr('content');\n" +
                "            return ['h5_v3_app_down', pageName, ek.clientType, ek.os].join('.');\n" +
                "        },\n" +
                "        // 初始化完毕后上报页面访问URL\n" +
                "        reportVisit: true\n" +
                "    });\n" +
                "\n" +
                "</script>\n" +
                "<!--强制刷新-->\n" +
                "<script>\n" +
                "    var pg_SearchResult = new BIU.Page({\n" +
                "        name: 'Page_SearchResult',\n" +
                "        word: '我的',\n" +
                "        pageIndex: '1' | 0,\n" +
                "        pageSize: '10' | 0\n" +
                "    });\n" +
                "</script>\n" +
                "<script src=\"//gtimg.ac.qq.com/h5/search/js/result.js?v=dfd142fdf74aa360\"></script>\n" +
                "</body>\n" +
                "</html>\n" +
                "\n" +
                "<!--tailTrap<body></body><head></head><html></html>-->\n";
    }

    public static String getDetailHtml() {
        return "";
    }

    public static String getImageHtml() {
        return "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "    <meta name=\"MobileOptimized\" content=\"320\">\n" +
                "    <meta name=\"applicable-device\" content=\"mobile\">\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-transform\"/>\n" +
                "    <link rel=\"preconnect\" href=\"//m.manhuafen.com\">\n" +
                "        <title>一拳超人176话 - 村田雄介,ONE - 连载中 - 漫画粉</title>\n" +
                "    <meta name=\"keywords\" content=\"一拳超人\">\n" +
                "<meta name=\"description\" content=\"一拳超人漫画。主人公埼玉原本是一名整日奔波于求职的普通人。3年前的一天偶然遇到了\">\n" +
                "<meta name=\"csrf-param\" content=\"_csrf\">\n" +
                "    <meta name=\"csrf-token\" content=\"k1giEnxojSXvRIqHvp5Nu1hYncvTGVX7fDGxgiJWxYunOm8_ThjAYKIUz8_H0AvyC2jc_ONVHr4PQe7gam6u7A==\">\n" +
                "\n" +
                "<link href=\"https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/css/main.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/assets/f04ed0d8/css/font-awesome.min.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/plugins/toastr/toastr.min.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/assets/ade9283c/css/common.css\" rel=\"stylesheet\">        <script>function isMobileHanddle(){var e=navigator.userAgent;return(screen.width/screen.height<1||/AppleWebKit.*Mobile/i.test(e)||/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(e)||/ipad/i.test(e))}var host=location.host;isMobileHanddle()||\"m\"!==host.substr(0,host.indexOf(\".\"))||(location.href=location.href.replace(\"//m.\",\"//www.\"))</script>\n" +
                "</head>\n" +
                "<body class=\"clearfix\" style=\"background:#444;\">  \n" +
                "\n" +
                "<div class=\"UnderPage chapter-view\" id=\"chapter-view\">\n" +
                "    <!--分享弹层-->\n" +
                "    <!--分享弹层 end-->\n" +
                "    <div class=\"subHeader\" style=\"display: none;\" id=\"panel-title\">\n" +
                "\t  <a href=\"/\" class=\"iconHome\"></a>\n" +
                "        <a href=\"https://m.manhuafen.com/comic/1802/\" class=\"iconRet\"></a>\n" +
                "        <a href=\"javascript:void(0);\" class=\"BarTit\">\n" +
                "            176话            <span id=\"page-info\"></span>\n" +
                "        </a>\n" +
                "        <a href=\"javascript:void(0);\" class=\"iconShar\" id=\"shareIcon\"></a>\n" +
                "    </div>\n" +
                "  \n" +
                "<script>;var chapterImages = \"y7yMRIylYTcOH0EvG+KUC3xQ8ehlRw1eHjnr0slw6jdDmJ+HNvc6dQ3rMG0WJaWnpnKAXpMkodAtPlJuFF+S0A92TkbyecWX7s4YBUs6UomZOa9IwM1c/nPdhjuGQDWc2EmDXZiFb5KOq8FIdggz+uk09okhP1HRomf3bcCF9r5wNBLWVmrpoUkUWjafawG1J4bmtkbA0BZWEoGiG0FQQebD2h7fBEV9BOeHuzwg238LbaQdlgkpvoLr1i6X48wgGbukvHVH1BXYQW9qSydgLcDRaIu2UjfCg88mWAZtptRWWSqy49oycMXthUM96SMRXHywX4gu2S3mJOh41EYC9becWzadqyQKrF6V+Y/KuWmSNgv9Z50ZbqXus7xMeBQOX7JJFkFu6SeQ2T/H3jTn0vgbRees/lf/lUIcpmaqlZxvhuhJkq4lNVLHlyOcp2gdsxfksNOvkwJGNVYijYXEmiD7ma3huXeQWx/9VDca/lPTiYJ6R9gCpgFFL2vjDR7JvCXzWQVz9eILkXGSM/h0K98kTeQjhDK3lm08j5cIN+hg5gzS+OPfdRWx7o881i2MIxR3cLMOzJS/MyDgCMtnqGQx0F4k6mgkNNGBIBq6sJ9uZuJSRIsGKUMM4yfO6l54xwhYYUVLdXVCbazAQhlMOoI/1b5YowQAUvr94gH8EFIr0O6avF/XgtE9Z1UbS/HUEIEF6DhopPUvYYuSbJQQBTyyq0KFQtYWMy2rMxgwrEEaIqEXNnvS8F7jPih6T8K35HGb/yfWsEWtieErFRnLxuE+o00BIw3UnJNnX1+SAmMXkc+Ks7uFxEieo++TdHTPgXGaxUhgXZT7qk3cporw72prIu5vPL8QV4x4/Rci586X+ZlHyZFfa69LNI+XkWMdhJaCBIb4Wn6EJfKWIIUJLJJ8Y3GMfVN0yjzkh2IfQiRpktz+8OV3CbiLfPG045GOaxZyy9vzo7giLhj/0zMFgbv5J95BTP/uVTOLSTB5TWuJZaJLGsieMkYotUYwnH0sAHF4BEwHpgg/3AMo8i1p96jUeKHnjRVQXAranTwI6yJSPACD+USMdaHKsb2jl9XhuX5Kudvbpe2/UDFpQxXQP8HXgdFHMka+WyHu1VX62lyiIiXzjVsI72BdkEU7HXi7mY413SQO3J2QkhbeYxfTOf7D5eRLSywEyQPJCUys6cOMn1ZGrApYjS6K6kkDAPM9ZKcWk7eCsMg1Myaof2iD1nI3OvGTfHk5AhFqPBp44kC6zu4RtuR0+eRbsMtneHJojjJODMkgpXTI9cvMeNgpCHMw8tjg3A5Fd+is6IAVVXFS4Cbxw/Yi3ENZBTE93HEOId/UAek0K2vXvYwypdbXbmHOF7NFI9RJPAxbK9x6Xtxq+TljowSzfj/wUiZX8ciajbRoZmzzaFp/BZH6QR5GGfQemV/+7S9CnncNfMrdWuNQR3iUUsR9otKdVQOZFcm1WXfdFZBIrfp/ut+pN5u1MJGDWJDje8b0rqp17SfrdkPs471MJQm7F/n2nv232Er44SkZQtdAYtqG5z4NBqY4xYsX6S+su5kl+2x2M0gcfQ3MPv37C709eRq06i6+LoIkOaeV1Q8FhU7WbdnvjKOvAGfqnaDNxQRT41fOWo9/CTWySkdgbFehkDGDuvBPTqQ4lodzHHIXmiJZSSAY0HEAMA5goASawUcILT4EhPyMwZxRKkdEHsdSfzVBDQA83Y1w3vkdzK2cy1fKOMPd4BVoIVaj1/3KwCCRYeGB/iKs4uIGGrpUwZc3rh38XstmLRcFxpLubyHPjtH0QYvadSh3BZJoqoFMuYaeA+PbTUYbeEuZPgenGG3XnqtcX8cYHev2ClBWCuyxSricSoyoeAExRHG3z5GdUJGNMA4pfqzZYpQVEQBWdUIErZpj0b7bD/BDbRirFf15qLIF8VGkSDzPChkG8SLsoenEgo4WJbNoj0c090ydOnOxjcW7AZHsVGym\";var chapterPath = \"images/comic/276/551794/\";var chapterPrice = 0;var chapterCanRead = 1;;var siteName = \"\";var siteUrl = \"https://m.manhuafen.com\";var pageTitle = \"一拳超人176话 - 村田雄介,ONE - 连载中\";var comicUrl = \"https://m.manhuafen.com/comic/1802/\";var pageUrl = \"https://m.manhuafen.com/comic/1802/\";var pageImage = \"https://c.mlxsc.com/images/cover/201905/1558384189-Nm4NaFsOVxmHZf1.jpg\";var pageDomain = \"https://m.manhuafen.com\";var pageId = \"comic.1802\";var prevChapterData = {\"id\":526419,\"comic_id\":1802,\"comic_name\":\"一拳超人\",\"status\":1,\"vip\":0,\"is_end\":0,\"name\":\"175话\",\"type\":0,\"rtl\":0,\"image_mode\":0,\"category\":1,\"link\":\"\",\"link_name\":\"\",\"image_type\":0,\"count\":25,\"sort\":999,\"price\":0,\"created_at\":1594141767,\"updated_at\":1594141770,\"uri\":\"\\/comic\\/1802\\/526419.html\",\"url\":\"https:\\/\\/m.manhuafen.com\\/comic\\/1802\\/526419.html\"};var nextChapterData = {\"id\":null,\"comic_id\":null,\"comic_name\":null,\"status\":null,\"vip\":null,\"is_end\":null,\"name\":null,\"type\":null,\"rtl\":null,\"image_mode\":null,\"category\":null,\"link\":null,\"link_name\":null,\"image_type\":null,\"count\":null,\"sort\":null,\"price\":null,\"created_at\":null,\"updated_at\":null,\"uri\":\"\",\"url\":\"https:\\/\\/m.manhuafen.com\\/\"};</script> \n" +
                "  \n" +
                "   <div style=\"background:#FFF;border-bottom: 1px solid #ccc;\" class=\"autoHeight\">\n" +
                "      <script>\n" +
                "      document.writeln(\"<p class=\\'txtDesc autoHeight\\' style=\\'background:#FFF;color:#CCC;text-align:center;font-size:20px;\\'>首次打不开图片，请试着刷新一下！</p>\");\n" +
                "      </script>\n" +
                " </div>  \n" +
                "  \n" +
                "  \n" +
                "    <div id=\"images\"></div>\n" +
                "\n" +
                "\t<div class=\"loading\" id=\"loading\"><span>图片读取中，请稍等...</span></div>\n" +
                "    <div></div>\n" +
                "\t<script src=\"/assets/biyou.js?v0108\"></script>\n" +
                "\t<script>m_mhf_tu_guding2();</script>   \n" +
                "\t\n" +
                "    <div class=\"control_bottom\" style=\"display: none\" id=\"m_r_bottom\">\n" +
                "        <div id=\"action\" class=\"action-list\">\n" +
                "            <ul>\n" +
                "                <li><a href=\"javascript:SinTheme.prevChapter();\">上一章</a></li>\n" +
                "                <li><a href=\"javascript:SinTheme.prevPage();\">上一页</a></li>\n" +
                "                <li><a href=\"javascript:SinTheme.nextPage();\">下一页</a></li>\n" +
                "                <li><a href=\"javascript:SinTheme.nextChapter();\">下一章</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "   \n" +
                "  <script>m_mhf_tu_guding3();</script>    \n" +
                "\n" +
                "\t<div class=\"imgBox\">\n" +
                "        <div class=\"Sub_H2\">\n" +
                "            <span class=\"icon icon_h2_9\"></span>\n" +
                "            <span class=\"Title\">更新推荐</span>\n" +
                "            <a class=\"icon_More\" href=\"/rank/\" target=\"_blank\"></a>        </div>\n" +
                "        <ul id=\"w0\" class=\"col_3_1\"><li class=\"list-comic\" data-key=\"15343\"><a class=\"ImgA\" href=\"https://m.manhuafen.com/comic/15343/\"><img src=\"https://c.mlxsc.com/images/cover/202006/159101675863015343fb5259ca.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a><a class=\"txtA\" href=\"https://m.manhuafen.com/comic/15343/\">报告！帝君你有毒！</a><span class=\"info\">更新至:<a href=\"/comic/15343/558215.html\">20话 到了进退两男的地步</a></span>\n" +
                "</li>\n" +
                "<li class=\"list-comic\" data-key=\"15055\"><a class=\"ImgA\" href=\"https://m.manhuafen.com/comic/15055/\"><img src=\"https://c.mlxsc.com/images/cover/202005/158974145224915055d2c26746.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a><a class=\"txtA\" href=\"https://m.manhuafen.com/comic/15055/\">落花流水</a><span class=\"info\">更新至:<a href=\"/comic/15055/558210.html\">CH43</a></span>\n" +
                "</li>\n" +
                "<li class=\"list-comic\" data-key=\"14713\"><a class=\"ImgA\" href=\"https://m.manhuafen.com/comic/14713/\"><img src=\"https://c.mlxsc.com/images/cover/202005/158831756613314713bcf6da04.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a><a class=\"txtA\" href=\"https://m.manhuafen.com/comic/14713/\">绝顶弃少</a><span class=\"info\">更新至:<a href=\"/comic/14713/558208.html\">48话 有些人，只能揍</a></span>\n" +
                "</li>\n" +
                "<li class=\"list-comic\" data-key=\"15610\"><a class=\"ImgA\" href=\"https://m.manhuafen.com/comic/15610/\"><img src=\"https://c.mlxsc.com/images/cover/202006/159224463319815610b9ebc68b.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a><a class=\"txtA\" href=\"https://m.manhuafen.com/comic/15610/\">废墟部少女</a><span class=\"info\">更新至:<a href=\"/comic/15610/558204.html\">12话</a></span>\n" +
                "</li>\n" +
                "<li class=\"list-comic\" data-key=\"14477\"><a class=\"ImgA\" href=\"https://m.manhuafen.com/comic/14477/\"><img src=\"https://c.mlxsc.com/images/cover/202004/1587379493149144779eec05c7.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a><a class=\"txtA\" href=\"https://m.manhuafen.com/comic/14477/\">area51</a><span class=\"info\">更新至:<a href=\"/comic/14477/558203.html\">番外09</a></span>\n" +
                "</li>\n" +
                "<li class=\"list-comic\" data-key=\"15737\"><a class=\"ImgA\" href=\"https://m.manhuafen.com/comic/15737/\"><img src=\"https://c.mlxsc.com/images/cover/202006/159276771622615737feef99c8.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a><a class=\"txtA\" href=\"https://m.manhuafen.com/comic/15737/\">神仙代理人</a><span class=\"info\">更新至:<a href=\"/comic/15737/558202.html\">公告</a></span>\n" +
                "</li></ul>    </div>\n" +
                "   \n" +
                "    <!--控制面板end -->\n" +
                "\n" +
                "<!--baiduzidongshoulu-->\n" +
                "<script>\n" +
                "(function(){\n" +
                "    var bp = document.createElement('script');\n" +
                "    var curProtocol = window.location.protocol.split(':')[0];\n" +
                "    if (curProtocol === 'https') {\n" +
                "        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';\n" +
                "    }\n" +
                "    else {\n" +
                "        bp.src = 'http://push.zhanzhang.baidu.com/push.js';\n" +
                "    }\n" +
                "    var s = document.getElementsByTagName(\"script\")[0];\n" +
                "    s.parentNode.insertBefore(bp, s);\n" +
                "})();\n" +
                "</script>  \n" +
                "\t  \n" +
                "\n" +
                "<script src=\"https://cdn.bootcdn.net/ajax/libs/jquery/1.12.1/jquery.js\"></script>\n" +
                "<script src=\"/assets/14462cae/yii.js\"></script>\n" +
                "<script src=\"https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.js\"></script>\n" +
                "<script src=\"/js/config.js?v0108\"></script>\n" +
                "<script src=\"/js/common.js?v0108\"></script>\n" +
                "<script src=\"https://cdn.staticfile.org/crypto-js/3.1.9-1/crypto-js.js\"></script>\n" +
                "<script src=\"/js/decrypt0729.js\"></script>\n" +
                "<script src=\"https://cdn.staticfile.org/toastr.js/2.1.2/toastr.min.js\"></script>\n" +
                "<script src=\"/plugins/baiduTemplate.js\"></script>\n" +
                "<script src=\"https://cdn.staticfile.org/jquery-cookie/1.4.1/jquery.cookie.js\"></script>\n" +
                "<script src=\"/plugins/jquery/jquery.image.js\"></script>\n" +
                "<script src=\"https://cdn.staticfile.org/jquery.lazyload/1.9.1/jquery.lazyload.min.js\"></script>\n" +
                "<script src=\"/plugins/jquery/jquery.hotkeys.js\"></script>\n" +
                "<script src=\"/plugins/bootstrap/bootstrap.hover.dropdown.js\"></script>\n" +
                "<script src=\"/plugins/bootstrap/bootstrap.hover.tab.js\"></script>\n" +
                "<script src=\"/assets/ade9283c/js/theme.js\"></script>\n" +
                "<script>jQuery(function ($) {\n" +
                ";decrypt20180904(551794,\"176话\",1802,\"一拳超人\");\n" +
                "//$('#page-info').text('('+SinTheme.getPage()+'/'+SinMH.getChapterImageCount()+')');\n" +
                "});</script><!--xuanfu-->\n" +
                "<script>m_mhf_tu_xuanfu();</script>\n" +
                "\n" +
                "<div style=\"display:none;\"> \n" +
                "<script>\n" +
                "var cnzz_s_tag = document.createElement('script');\n" +
                "cnzz_s_tag.type = 'text/javascript';\n" +
                "cnzz_s_tag.async = true;\n" +
                "cnzz_s_tag.charset = 'utf-8';\n" +
                "cnzz_s_tag.src = 'https://w.cnzz.com/c.php?id=1277232052&async=1';\n" +
                "var root_s = document.getElementsByTagName('script')[0];\n" +
                "root_s.parentNode.insertBefore(cnzz_s_tag, root_s);\n" +
                "</script>\n" +
                "\n" +
                "<script>\n" +
                "var _hmt = _hmt || [];\n" +
                "(function() {\n" +
                "  var hm = document.createElement(\"script\");\n" +
                "  hm.src = \"https://hm.baidu.com/hm.js?7eaf18beba489da560f4e3c42c1e5466\";\n" +
                "  var s = document.getElementsByTagName(\"script\")[0]; \n" +
                "  s.parentNode.insertBefore(hm, s);\n" +
                "})();\n" +
                "</script>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
    }

    public static String getTestHtml() {
        return "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "<meta name=\"renderer\" content=\"webkit\">\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "<meta name=\"MobileOptimized\" content=\"320\">\n" +
                "<meta name=\"applicable-device\" content=\"mobile\">\n" +
                "<title>点击排行榜        - 爱米推漫画</title>\n" +
                "<meta name=\"keywords\" content=\"重生之锦绣大唐漫画,女帝的后宫漫画,万渣朝凰漫画,铁姬钢兵漫画\">\n" +
                "<meta name=\"description\" content=\"爱米推漫画专注搞笑漫画，冒险漫画，少年热血等类型的漫画。爱米推漫画第一时间更新重生之锦绣大唐漫画,女帝的后宫漫画,万渣朝凰漫画,铁姬钢兵漫画等好看的漫画，看搞笑、少年热血等类型漫画最好的网站！\">\n" +
                "<link href=\"/assets/8649435d/css/font-awesome.min.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/assets/e8e83fb4/css/bootstrap.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/plugins/toastr/toastr.min.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/css/main.css\" rel=\"stylesheet\">\n" +
                "<link href=\"/assets/d49067f5/css/common.css\" rel=\"stylesheet\"> </head>\n" +
                "<body class=\"clearfix\">\n" +
                "<div class=\"header\">\n" +
                "<div id=\"search-box\" style=\"display: none;\">\n" +
                "<div class=\"serCh\" id=\"serCh\">\n" +
                "<form id=\"searchForm\" action=\"\">\n" +
                "<a href=\"javascript:void(0);\" id=\"search-close\" class=\"searClose\"></a>\n" +
                "<a href=\"javascript:void(0);\" class=\"searBtn\" id=\"btn-search\"></a>\n" +
                "<div class=\"serChinputBox\"><input type=\"search\" class=\"searInput\" placeholder=\"漫画名或作者\" id=\"keywords\"></div>\n" +
                "</form>\n" +
                "</div>\n" +
                "<div class=\"messagSjr\" id=\"message-sjr\">\n" +
                "<ul class=\"keyTit\" id=\"search-results\"></ul>\n" +
                "<p id=\"hotTit\">大家都在搜:</p>\n" +
                "<ul id=\"w0\" class=\"tag\"><li data-key=\"10999\"><a href=\"https://m.imitui.com/manhua/yuanlong/\">元龙</a></li>\n" +
                "<li data-key=\"6948\"><a href=\"https://m.imitui.com/manhua/mozunyaobaobao/\">魔尊要抱抱</a></li>\n" +
                "<li data-key=\"7259\"><a href=\"https://m.imitui.com/manhua/chaonenglifang/\">超能立方</a></li>\n" +
                "<li data-key=\"6917\"><a href=\"https://m.imitui.com/manhua/mohuangdaguanjia/\">魔皇大管家</a></li>\n" +
                "<li data-key=\"841\"><a href=\"https://m.imitui.com/manhua/yinshihuazu/\">隐世华族</a></li>\n" +
                "<li data-key=\"9633\"><a href=\"https://m.imitui.com/manhua/gelaipunier/\">格莱普尼尔</a></li>\n" +
                "<li data-key=\"7099\"><a href=\"https://m.imitui.com/manhua/moshichaojixitong/\">末世超级系统</a></li>\n" +
                "<li data-key=\"3253\"><a href=\"https://m.imitui.com/manhua/guimiezhiren/\">鬼灭之刃</a></li>\n" +
                "<li data-key=\"6999\"><a href=\"https://m.imitui.com/manhua/zaiduyuni/\">再度与你</a></li>\n" +
                "<li data-key=\"7183\"><a href=\"https://m.imitui.com/manhua/zhouyelianmian/\">昼夜连绵</a></li>\n" +
                "<li data-key=\"165\"><a href=\"https://m.imitui.com/manhua/wanzhachaohuang/\">万渣朝凰</a></li>\n" +
                "<li data-key=\"7251\"><a href=\"https://m.imitui.com/manhua/wuhuangzaishang/\">吾凰在上</a></li></ul>\n" +
                "<ul class=\"tag\" id=\"HotTag\"></ul>\n" +
                "</div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<div class=\"menuBar\" id=\"menuBar\">\n" +
                "<ul id=\"w1\"><li data-key=\"14\"><a href=\"/rank/\" target=\"_blank\">热门漫画</a></li></ul> <a href=\"javascript:$('#menuBar').fadeOut();\" class=\"searClose\"></a>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"logo\" style=\"width: 127px; padding: 5px;\">\n" +
                "<a href=\"/\"><img src=\"https://img.imitui.com/images/logo/dmzj-phone.png\" width=\"117\" height=\"36\" alt=\"\"></a> </div>\n" +
                "<a href=\"#\" title=\"\" class=\"icon setup\" onclick=\"$('#menuBar').fadeIn()\"></a>\n" +
                "<a href=\"javascript:;\" title=\"\" class=\"icon serch\" id=\"search-trigger\"></a>\n" +
                "<a class=\"icon head\" href=\"/member/\" target=\"_blank\"><img id=\"avatar\" src=\"https://img.imitui.com/images/default/avatar.png\" width=\"36\" height=\"36\" alt=\"\"><span class=\"tip\" id=\"update-notice\" style=\"display: none;\"></span></a></div>\n" +
                "<div class=\"main_nav\" style=\"display: none;\">\n" +
                "<nav>\n" +
                "<p class=\"c gold\">\n" +
                "<a href=\"/list/lianzai/\">连载漫画</a> <a href=\"/list/wanjie/\">完结漫画</a> <a href=\"/list/shaonian/\">少年漫画</a> <a href=\"/list/shaonv/\">少女漫画</a> <a href=\"/list/qingnian/\">青年漫画</a> <a href=\"/list/riben/\">日本漫画</a> <a href=\"/list/dalu/\">大陆漫画</a> <a href=\"/list/hongkong/\">香港漫画</a> <a href=\"/?device=desktop\" rel=\"nofollow\">电脑版</a>\n" +
                "</p>\n" +
                "</nav>\n" +
                "</div>\n" +
                "<ul class=\"nav autoHeight\" style=\"position: absolute;\">\n" +
                "<li><a class=\"\" href=\"/\">推荐</a></li>\n" +
                "<li><a class=\"\" href=\"/update/\">更新</a></li>\n" +
                "<li><a class=\"cur \" href=\"/rank/\">排行</a></li>\n" +
                "<li><a class=\"\" href=\"/list/\">分类</a></li>\n" +
                "<li><a class=\"\" href=\"/list/wanjie/\">完结</a></li>\n" +
                "</ul>\n" +
                "<div class=\"page-main\">\n" +
                "<div class=\"UpdateList\" style=\"margin-top: 40px;\">\n" +
                "<div class=\"TopComBar\">\n" +
                "<ul class=\"TopComTit\" id=\"TopComTit\">\n" +
                "<li class=\"\"><a href=\"/rank/popularity/\">人气排行</a></li> <li class=\"cur\"><a href=\"/rank/click/\">点击排行</a></li> <li class=\"\"><a href=\"/rank/subscribe/\">订阅排行</a></li> </ul>\n" +
                "\n" +
                "<a class=\"FilterBtn\">筛选</a>\n" +
                "<div class=\"FilterBox\" style=\"display: none;\">\n" +
                "<ul class=\"leftTit\" id=\"FilterTit\">\n" +
                "<li class=\"\" data-tab=\"class\" id=\"classTab\" style=\"display: none;\">分类</li>\n" +
                "<li class=\"cur\" data-tab=\"data\" id=\"dataTab\">时间</li>\n" +
                "</ul>\n" +
                "<div class=\"rithtCon\" id=\"FilterCon\">\n" +
                "\n" +
                "<ul class=\"classPanel\">\n" +
                "<li class=\"\"><a href=\"/rank/mofa/\">魔法漫画</a></li> <li class=\"\"><a href=\"/rank/shaonian/\">少年漫画</a></li> <li class=\"\"><a href=\"/rank/shaonv/\">少女漫画</a></li> <li class=\"\"><a href=\"/rank/qingnian/\">青年漫画</a></li> <li class=\"\"><a href=\"/rank/gaoxiao/\">搞笑漫画</a></li> <li class=\"\"><a href=\"/rank/kehuan/\">科幻漫画</a></li> <li class=\"\"><a href=\"/rank/rexue/\">热血漫画</a></li> <li class=\"\"><a href=\"/rank/maoxian/\">冒险漫画</a></li> <li class=\"\"><a href=\"/rank/wanjie/\">完结漫画</a></li> </ul>\n" +
                "\n" +
                "\n" +
                "<ul class=\"dataPanel select\">\n" +
                "<li><a class=\"asc\" href=\"/rank/-click/\" data-sort=\"-click\">总</a></li>\n" +
                "<li><a href=\"/rank/click-daily/\" data-sort=\"click-daily\">日</a></li>\n" +
                "<li><a href=\"/rank/click-weekly/\" data-sort=\"click-weekly\">周</a></li>\n" +
                "<li><a href=\"/rank/click-monthly/\" data-sort=\"click-monthly\">月</a></li>\n" +
                "</ul> \n" +
                "</div>\n" +
                "</div>\n" +
                "\n" +
                "</div>\n" +
                "<div id=\"topImgCon\" class=\"rand_tc\"><div class='items clearfix'><div class=\"itemBox\" data-key=\"10999\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/yuanlong/\"><img src=\"https://img.imitui.com/images/cover/202005/1589247867CsY6Ufa13POpGecB.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/yuanlong/\">元龙</a>\n" +
                "<p class=\"txtItme\">任怨,闻源文化<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|玄幻|冒险|改编</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-04 09:42</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number number1\">1</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"6948\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/mozunyaobaobao/\"><img src=\"https://img.imitui.com/images/cover/202001/1578538993UkubGSJFNPu31xcu.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/mozunyaobaobao/\">魔尊要抱抱</a>\n" +
                "<p class=\"txtItme\">魑羽<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|玄幻|剧情|重生</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number number2\">2</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7259\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/chaonenglifang/\"><img src=\"https://img.imitui.com/images/cover/202001/1578621821AdA4QHjkuY10tcSa.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/chaonenglifang/\">超能立方</a>\n" +
                "<p class=\"txtItme\">水落声声<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|都市|奇幻|逆转</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-04 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number number3\">3</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"6917\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/mohuangdaguanjia/\"><img src=\"https://img.imitui.com/images/cover/202001/1578538865rBP780k2GmurfECQ.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/mohuangdaguanjia/\">魔皇大管家</a>\n" +
                "<p class=\"txtItme\">夜枭（原著）+無二漫画<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|玄幻|重生|逆转</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">4</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"841\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/yinshihuazu/\"><img src=\"https://img.imitui.com/images/cover/201911/1573926069yrXQ3W7GSrjz27el.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/yinshihuazu/\">隐世华族</a>\n" +
                "<p class=\"txtItme\">米沙,翻翻动漫<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|恋爱|冒险|少女</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-20 09:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">5</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"9633\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/gelaipunier/\"><img src=\"https://img.imitui.com/images/cover/202003/1585109766tyoIhEiILdnaLFpN.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/gelaipunier/\">格莱普尼尔</a>\n" +
                "<p class=\"txtItme\">武田寸,讲谈社<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|奇幻|冒险|猎奇</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-20 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">6</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7099\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/moshichaojixitong/\"><img src=\"https://img.imitui.com/images/cover/202001/1578542642MTcHOHg3hItDhQwx.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/moshichaojixitong/\">末世超级系统</a>\n" +
                "<p class=\"txtItme\">猫族皇子（原著）+大行道动漫<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|少年|逆转|生化</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-31 10:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">7</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"3253\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/guimiezhiren/\"><img src=\"https://img.imitui.com/images/cover/201911/1574586963cExhC2pV68X9tKWj.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/guimiezhiren/\">鬼灭之刃</a>\n" +
                "<p class=\"txtItme\">吾峠呼世晴<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|冒险|神鬼</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-02-15 10:00</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">8</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"6999\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/zaiduyuni/\"><img src=\"https://img.imitui.com/images/cover/202001/15785423033J5xshQye9NZL7jP.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/zaiduyuni/\">再度与你</a>\n" +
                "<p class=\"txtItme\">晗旭L-Mo<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|治愈|青春|逆转</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">9</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7183\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/zhouyelianmian/\"><img src=\"https://img.imitui.com/images/cover/202001/1578560563n6nlAtCbyo23F6pv.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/zhouyelianmian/\">昼夜连绵</a>\n" +
                "<p class=\"txtItme\">Anjeo<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|奇幻|宠爱|异族</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-31 10:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">10</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"165\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/wanzhachaohuang/\"><img src=\"https://img.imitui.com/images/cover/201911/1573922584FcykeDsv7Kw8MZBc.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/wanzhachaohuang/\">万渣朝凰</a>\n" +
                "<p class=\"txtItme\">时代漫王<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|恋爱|穿越|科幻</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-05 09:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">11</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7251\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/wuhuangzaishang/\"><img src=\"https://img.imitui.com/images/cover/202001/1578568035Nplby-aemHP0Fx4p.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/wuhuangzaishang/\">吾凰在上</a>\n" +
                "<p class=\"txtItme\">嗷小泽<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|穿越|大女主|宠爱</span>\n" +
                "</p>\n" +
                " <p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-02 09:42</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">12</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7078\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/mingyueyaoyao/\"><img src=\"https://img.imitui.com/images/cover/202001/1578542627z_T9yHCPnjfzEIEp.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/mingyueyaoyao/\">明月烑烑</a>\n" +
                "<p class=\"txtItme\">第五庸人（主笔）+画中仙喵（编剧）<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|玄幻|古风|剧情</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-18 17:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">13</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7002\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/feiyourenguanxi/\"><img src=\"https://img.imitui.com/images/cover/202001/1578542306tupybTBe0C3_GYzW.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/feiyourenguanxi/\">非友人关系</a>\n" +
                "<p class=\"txtItme\">子雾啊<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|都市|搞笑|剧情</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-30 14:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">14</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"303\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/moshifanren/\"><img src=\"https://img.imitui.com/images/cover/201911/1573923426awlyZsk1da-q4hRp.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/moshifanren/\">末世凡人</a>\n" +
                "<p class=\"txtItme\">惊奇工场<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|恐怖</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 09:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">15</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7080\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/chuanyuechengfanpaiyaoruhehuoming/\"><img src=\"https://img.imitui.com/images/cover/202001/1578542629_AZ1QBatbVBFQCW_.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/chuanyuechengfanpaiyaoruhehuoming/\">穿越成反派要如何活命</a>\n" +
                "<p class=\"txtItme\">王一（主笔）+伊依以翼（原著）<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|古风|穿越|剧情</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-31 11:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">16</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"607\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/fangkainagenvwu/\"><img src=\"https://img.imitui.com/images/cover/201911/1573924930vbskd-nCkeSs5bsA.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/fangkainagenvwu/\">放开那个女巫</a>\n" +
                "<p class=\"txtItme\">阅文漫画<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|冒险</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-02 14:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">17</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"9631\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/xingluezhe/\"><img src=\"https://img.imitui.com/images/cover/202003/1585023967llqZldNUxz0teG_f.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/xingluezhe/\">星掠者</a>\n" +
                "<p class=\"txtItme\">水无月嵩,角川<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|奇幻|冒险</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-27 17:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">18</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7831\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/woduzishengji/\"><img src=\"https://img.imitui.com/images/cover/202002/1580525707T2U4IYly5Yd_K7gB.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/woduzishengji/\">我独自升级</a>\n" +
                "<p class=\"txtItme\">DUBU(主笔) + Chugong（原著）<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|都市|奇幻|逆转</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-16 14:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">19</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7750\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/moutianchengweigongzhu/\"><img src=\"https://img.imitui.com/images/cover/202002/1580521933FsXKz61W0wi2u_31.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/moutianchengweigongzhu/\">某天成为公主</a>\n" +
                "<p class=\"txtItme\">Spoon+Plutus+CARROTOON<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|穿越|奇幻|大女主</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-30 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">20</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"5945\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/wozaihuanggongdangjuju/\"><img src=\"https://img.imitui.com/images/cover/201912/1577091547G8YgClNc1RxmTX5v.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/wozaihuanggongdangjuju/\">我在皇宫当巨巨</a>\n" +
                "<p class=\"txtItme\">白梦社,之臻,小球<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|古风|恋爱|少女</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-29 16:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">21</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7422\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/zujienvyou/\"><img src=\"https://img.imitui.com/images/cover/202001/1579226547B0l0lMYVH9Tu1GA7.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/zujienvyou/\">租借女友</a>\n" +
                "<p class=\"txtItme\">宫岛礼吏,讲谈社<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|恋爱|日常|后宫</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-05 09:46</span>\n" +
                " </p>\n" +
                "</div>\n" +
                "<div class=\"number \">22</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"1891\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/yiquanchaoren/\"><img src=\"https://img.imitui.com/images/cover/201911/15739300930grf--UaFnQZAmxG.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/yiquanchaoren/\">一拳超人</a>\n" +
                "<p class=\"txtItme\">村田雄介 ONE<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|冒险|格斗|日漫</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-06-10 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">23</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"6986\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/shuangmianzhubo/\"><img src=\"https://img.imitui.com/images/cover/202001/1578542294_oytzR5Q_3asRlI0.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/shuangmianzhubo/\">双面主播</a>\n" +
                "<p class=\"txtItme\">苍苍（主笔）+漠花（编剧）<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|都市|剧情|竞技</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">24</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"2999\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/nvdidehougong/\"><img src=\"https://img.imitui.com/images/cover/201911/157455990657p9z9kGcpYRZ2eD.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/nvdidehougong/\">女帝的后宫</a>\n" +
                "<p class=\"txtItme\">Morel<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|古风|恋爱</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 11:11</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">25</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"6775\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/shengxu/\"><img src=\"https://img.imitui.com/images/cover/202001/1578534969ZrMwYZen0NthxEPN.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/shengxu/\">圣墟</a>\n" +
                "<p class=\"txtItme\">辰东（原著）+常盘勇者<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|玄幻|冒险|生化</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-03 09:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">26</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"7480\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/bilanzhihai/\"><img src=\"https://img.imitui.com/images/cover/202001/1579330871f0uRaG37PSHfM_fa.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/bilanzhihai/\">碧蓝之海</a>\n" +
                "<p class=\"txtItme\">井上坚二,吉冈公威,讲谈社<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|搞笑</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-27 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">27</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"1929\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/haizeiwang/\"><img src=\"https://img.imitui.com/images/cover/201911/1573930264W3q7hz10NYqIzEkx.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/haizeiwang/\">海贼王</a>\n" +
                "<p class=\"txtItme\">尾田荣一郎<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|冒险|日漫</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-18 09:43</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">28</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"2726\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/zhanguoqiannian/\"><img src=\"https://img.imitui.com/images/cover/201911/1573933395mSUZNCDJpHYoZ9U0.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/zhanguoqiannian/\">战国千年</a>\n" +
                "<p class=\"txtItme\">蓝迪漫娱,时代漫王<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|古风</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-05 09:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">29</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"348\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/lanchi/\"><img src=\"https://img.imitui.com/images/cover/201911/1573923662CntU6MwixnJO0HHs.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/lanchi/\">蓝翅</a>\n" +
                "<p class=\"txtItme\">徐璐AKO<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|恋爱|生活|少女</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-31 18:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">30</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"421\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/yirenzhixia/\"><img src=\"https://img.imitui.com/images/cover/201911/1573924030kC0O3P66YxXZhHVL.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/yirenzhixia/\">一人之下</a>\n" +
                "<p class=\"txtItme\">动漫堂<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|玄幻|爆笑|格斗</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-31 14:41</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">31</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"1890\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/wangzhetianxia/\"><img src=\"https://img.imitui.com/images/cover/201911/1573930090-V3PEUx7S2aEo_T3.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/wangzhetianxia/\">王者天下</a>\n" +
                "<p class=\"txtItme\">原泰久<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|战争|武侠|励志|日漫</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-20 09:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">32</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"8430\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/xianwangderichangshenghuo/\"><img src=\"https://img.imitui.com/images/cover/202002/1582077250P8TVOuOHTE2i96H1.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/xianwangderichangshenghuo/\">仙王的日常生活</a>\n" +
                "<p class=\"txtItme\">娱乐没错<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|搞笑</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-01 11:11</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">33</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"6933\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/sanjietaobaodian/\"><img src=\"https://img.imitui.com/images/cover/202001/1578538979hjVXdlq1PSu3vdNE.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/sanjietaobaodian/\">三界淘宝店</a>\n" +
                "<p class=\"txtItme\">宁逍遥（原著）+风狸绘漫画<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|奇幻|少年|逆转</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-08-02 09:42</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">34</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"682\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/huanghouhenmang/\"><img src=\"https://img.imitui.com/images/cover/201911/1573925288Jw-I_tH8JNQCbDrF.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/huanghouhenmang/\">皇后很忙</a>\n" +
                "<p class=\"txtItme\">昭花社<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|恋爱|后宫|古装</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-01-04 04:09</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">35</div>\n" +
                "</div>\n" +
                "<div class=\"itemBox\" data-key=\"937\">\n" +
                "<div class=\"itemImg\">\n" +
                "<a href=\"https://m.imitui.com/manhua/heisesiyecao/\"><img src=\"https://img.imitui.com/images/cover/201911/1573926603RhFh_Bvq-0AQMumf.jpg\" width=\"100%\" alt=\"\" default=\"images/default/cover.png\"></a></div>\n" +
                "<div class=\"itemTxt\">\n" +
                "<a class=\"title\" href=\"https://m.imitui.com/manhua/heisesiyecao/\">黑色四叶草</a>\n" +
                "<p class=\"txtItme\">集英社<span class=\"icon icon01\"></span></p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon02\"></span>\n" +
                "<span class=\"pd\">少年漫画|热血|魔幻|励志</span>\n" +
                "</p>\n" +
                "<p class=\"txtItme\">\n" +
                "<span class=\"icon icon03\"></span>\n" +
                "<span class=\"date\">2020-07-20 09:40</span>\n" +
                "</p>\n" +
                "</div>\n" +
                "<div class=\"number \">36</div>\n" +
                "</div></div></div></div>\n" +
                "</div>\n" +
                "<div class=\"bottom\">\n" +
                "<ul class=\"subNav\">\n" +
                "<li><a href=\"/?device=desktop\"><span class=\"a4\">返回pc版</span></a></li>\n" +
                "<li id=\"w3\"><a href=\"https://res.duoduomh.1a3.net/download/app/\" target=\"_blank\"><span class=\"a5\">客户端</span></a></li> <li class=\"last\"><a href=\"javascript:SinMH.scrollTo(0);\"><span class=\"a6\">返回顶部</span></a></li>\n" +
                "</ul>\n" +
                "<p class=\"record\">\n" +
                "&copy; 2019-2020 IMITUI.COM<br />\n" +
                "<span id=\"w4\"><a href=\"/main/about/\" target=\"_blank\">关于我们</a></span> </p>\n" +
                "</div>\n" +
                "<div style=\"display: none;\">\n" +
                "<script type=\"text/javascript\" src=\"https://v1.cnzz.com/z_stat.php?id=1278728102&web_id=1278728102\"></script>\n" +
                "\n" +
                "<script async src=\"https://www.googletagmanager.com/gtag/js?id=UA-152707423-1\"></script>\n" +
                "<script>\n" +
                "  window.dataLayer = window.dataLayer || [];\n" +
                "  function gtag(){dataLayer.push(arguments);}\n" +
                "  gtag('js', new Date());\n" +
                "\n" +
                "  gtag('config', 'UA-152707423-1');\n" +
                "</script>\n" +
                "<script>\n" +
                "var _hmt = _hmt || [];\n" +
                "(function() {\n" +
                "  var hm = document.createElement(\"script\");\n" +
                "  hm.src = \"https://hm.baidu.com/hm.js?6eb6192ef3967abd2875d75cdd878104\";\n" +
                "  var s = document.getElementsByTagName(\"script\")[0]; \n" +
                "  s.parentNode.insertBefore(hm, s);\n" +
                "})();\n" +
                "</script>\n" +
                "\n" +
                "</div>\n" +
                "<div class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"alert-modal\" id=\"alert-modal\">\n" +
                "<div class=\"modal-dialog modal-sm\">\n" +
                "<div class=\"modal-content\">\n" +
                "<div class=\"modal-header\">\n" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "<span aria-hidden=\"true\">×</span>\n" +
                "</button>\n" +
                "<h4 class=\"modal-title\">提示信息</h4>\n" +
                "</div>\n" +
                "<div class=\"modal-body text-center\"> 弹窗内容</div>\n" +
                "<div class=\"modal-footer text-center\" style=\"text-align: center;\">\n" +
                "<button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\" aria-label=\"Close\">确定</button>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"confirm-modal\" id=\"confirm-modal\">\n" +
                "<div class=\"modal-dialog modal-sm\">\n" +
                "<div class=\"modal-content\">\n" +
                "<div class=\"modal-header\">\n" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "<span aria-hidden=\"true\">×</span>\n" +
                "</button>\n" +
                "<h4 class=\"modal-title\">提示信息</h4>\n" +
                "</div>\n" +
                "<div class=\"modal-body text-center\"> 弹窗内容 </div>\n" +
                "<div class=\"modal-footer text-center\" style=\"text-align: center;\">\n" +
                "<button type=\"button\" class=\"btn btn-primary btn-confirm\" data-dismiss=\"modal\" aria-label=\"Confirm\">确定</button>\n" +
                "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" aria-label=\"Close\">取消</button>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"mask\" id=\"mask\" style=\"top: 92px;\"></div>\n" +
                "<script id=\"search-template\" type=\"text/html\">\n" +
                "    <%if(items&&items.length>0){%>\n" +
                "    <%for(var i = 0; i\n" +
                "    <items.length;i++){\n" +
                "    var item = items[i];\n" +
                "    %>\n" +
                "    <li title=\"<%=item.title%>\" data-url=\"<%= item.uri %>\">\n" +
                "        [<a href=\"/author/id-<%= item.author_id %>/\"><%=item.author%></a>]\n" +
                "        <a href=\"<%= item.uri %>\"><%=item.title%></a>\n" +
                "        [<span class=\"<%=item.serialise==1?'text-danger':'text-success'%>\"><%=item.serialise==1?'连载中':'已完结'%></span>]\n" +
                "    </li>\n" +
                "    <%}%>\n" +
                "    <%}else{%>\n" +
                "    暂无搜索结果    <%}%>\n" +
                "</script>\n" +
                "<script id=\"history\" type=\"text/html\">\n" +
                "    <%if(list&&list.length>0){\n" +
                "    for(var i = 0; i < list.length;i++){\n" +
                "    var item = list[i];\n" +
                "    %>\n" +
                "    <li>\n" +
                "        <div>\n" +
                "            <a href=\"<%=item.comic_url%>\"><%=item.comic_name%></a>\n" +
                "            <span><a href=\"<%=item.read_chapter_url%>\"><%=item.read_chapter%></a></span>\n" +
                "            <span><a href=\"<%=item.read_chapter_url%>#p=<%=item.read_page%>\">第<%=item.read_page%>页</a></span>\n" +
                "            <span class=\"time\"><%= filter.asDuration(item.read_at)%></span>\n" +
                "        </div>\n" +
                "        <a class=\"btn-close\" href=\"javascript:SinMH.removeHistory(<%=item.comic_id%>)\"></a>\n" +
                "\n" +
                "    </li>\n" +
                "\n" +
                "    <%}\n" +
                "    }else{%>\n" +
                "    暂无历史纪录    <%}%>\n" +
                "</script>\n" +
                "<script src=\"/assets/7bfd4231/jquery.js\"></script>\n" +
                "<script src=\"/assets/16dcbbc7/yii.js\"></script>\n" +
                "<script src=\"/assets/e8e83fb4/js/bootstrap.js\"></script>\n" +
                "<script src=\"/plugins/toastr/toastr.min.js\"></script>\n" +
                "<script src=\"/plugins/baiduTemplate.js\"></script>\n" +
                "<script src=\"/plugins/jquery/jquery.cookie.js\"></script>\n" +
                "<script src=\"/plugins/jquery/jquery.image.js\"></script>\n" +
                "<script src=\"/plugins/jquery/jquery.lazyload.min.js\"></script>\n" +
                "<script src=\"/plugins/jquery/jquery.hotkeys.js\"></script>\n" +
                "<script src=\"/plugins/bootstrap/bootstrap.hover.dropdown.js\"></script>\n" +
                "<script src=\"/plugins/bootstrap/bootstrap.hover.tab.js\"></script>\n" +
                "<script src=\"/js/config.js\"></script>\n" +
                "<script src=\"/js/common.js\"></script>\n" +
                "<script src=\"/assets/d49067f5/js/theme.js\"></script>\n" +
                "<script>jQuery(function ($) {\n" +
                ";SinTheme.initRank();\n" +
                ";SinMH.init();SinTheme.init();$(\"img\").lazyload();\n" +
                "});</script></body>\n" +
                "</html>\n";
    }

    public static String getHtmlByFile() {
        try {
            String filePath = "D:\\Programming\\Files\\ZA\\2020寒假\\CUR\\text.html";
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTextByFile() {
        try {
            String filePath = "D:\\Programming\\Files\\ZA\\2020寒假\\CUR\\text.txt";
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
