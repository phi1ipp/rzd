XMLHttpRequest.prototype.reallySend = XMLHttpRequest.prototype.send;
XMLHttpRequest.prototype.send = function(body) {
    console.log(body);
    this.reallySend(body);
};
var req = new XMLHttpRequest();
req.open("GET", "any.html", true);
req.send(null);