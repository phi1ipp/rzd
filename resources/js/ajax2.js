(function(XHR) {
    "use strict";
    
    var stats = [];
    var timeoutId = null;
    
    var open = XHR.prototype.open;
    var send = XHR.prototype.send;
    
    XHR.prototype.open = function(method, url, async, user, pass) {
        this._url = url;
	console.log('XHR open:' + url);
        open.call(this, method, url, async, user, pass);
    };
    
    XHR.prototype.send = function(data) {
        var self = this;
        var start;
        var oldOnReadyStateChange;
        var url = this._url;
        
        function onReadyStateChange() {
            if(self.readyState == 4 /* complete */) {
                var time = new Date() - start;
                stats.push({
                    url: url,
                    duration: time                    
                });
                
                if(!timeoutId) {
                    timeoutId = window.setTimeout(function() {
                        var xhr = new XHR();
                        xhr.noIntercept = true;
                        xhr.open("POST", "/clientAjaxStats", true);
                        xhr.setRequestHeader("Content-type","application/json");
                        xhr.send(JSON.stringify({ stats: stats } ));                        
                        
                        timeoutId = null;
                        stats = []; 
                    }, 2000);
                }                
            }
            
            if(oldOnReadyStateChange) {
                oldOnReadyStateChange();
            }
        }
        
        if(!this.noIntercept) {
            start = new Date();
            
            if(this.addEventListener) {
                this.addEventListener("readystatechange", onReadyStateChange, false);
            } else {
                oldOnReadyStateChange = this.onreadystatechange; 
                this.onreadystatechange = onReadyStateChange;
            }
        }
        
        send.call(this, data);
    }
})(XMLHttpRequest);