(function(open) {
  XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {
    console.log(url);
    open.call(this, method, url, async, user, pass);
  };
})(XMLHttpRequest.prototype.open);