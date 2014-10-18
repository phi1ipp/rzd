(function(XHR) {
    "use strict";
    
    var open = XHR.prototype.open;
    var send = XHR.prototype.send;
    
    XHR.prototype.open = function(method, url, async, user, pass) {
        this._url = url;
        open.call(this, method, url, async, user, pass);
    };
    
    XHR.prototype.send = function(data) {
	ajaxHook.send(this._url);
        send.call(this, data);
    }
})(XMLHttpRequest);