(function(window) {
    "use strict";
    var templates = {};
    templates.message = '<div class="message" data-id={{id}}><div class="message__delete" title="Delete message" data-id={{id}}>x</div>{{content}}</div>'
    window.templates = templates;
})(window);