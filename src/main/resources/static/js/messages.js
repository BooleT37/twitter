function onLoad() {
    "use strict";
    var data = window.data;
    var messagesJquery = data.messages.reduce(function(previousElement, messageModel) {
        return previousElement.add(renderMessage(messageModel));
    }, $());
    $("#messages").append(messagesJquery);

    $("#newMessageTextarea").on("input", function(e) {
        var textArea = $(e.target);
        if (textArea.val().length > 0) {
            textArea.removeClass("newMessageTextarea_folded");
            $("#sendButton").removeAttr("disabled");
        } else {
            textArea.addClass("newMessageTextarea_folded");
            $("#sendButton").attr("disabled", "disabled");
        }
    })

    $("#sendButton").click(function() {
        var messageText = $("#newMessageTextarea").val().toString();
        $.ajax({
            url: "/addMessage",
            data: JSON.stringify({content: messageText}),
            method: "post",
            contentType: "application/json; charset=UTF-8"
        }).done(function(response) {
            if (response.id !== undefined) {
                var jQMessage = renderMessage({content: messageText, id: response.id});
                $("#messages").append(jQMessage);
                jQMessage.hide().fadeIn(300);
                $("#newMessageTextarea").val("");
                $("#newMessageTextarea").trigger("input");
            } else {
                fail(response);
            }
        }).fail(fail)

        function fail(error) {
            console.error("Couldn't add a message, error is " + error.toString())
        }
    })

    function renderMessage(model) {
        var jQMessage = $(Mustache.render(templates.message, model));
        jQMessage.find(".message__delete").click(onDeleteMessageClick);
        return jQMessage;
    }

    function onDeleteMessageClick(e) {
        var id = $(e.target).data("id");
        if (window.confirm("Do you really want to delete this message?"))
            $.ajax({
                url: "/deleteMessage",
                data: JSON.stringify({id: id}),
                method: "delete",
                contentType: "application/json"
            }).done(function(response) {
                if (response.message !== undefined) {
                    var messages = $(".message");
                    var message;
                    for (var i = 0; i < messages.length; i++) {
                        var currentMessage = $(messages[i]);
                        if (currentMessage.data("id") == id) {
                            message = currentMessage;
                            break;
                        }
                    }
                    if (message !== undefined)
                        message.fadeOut(300);
                        window.setTimeout(300, function() {
                            message.remove();
                        })
                } else {
                    fail(response);
                }
            }).fail(fail)


        function fail(error) {
            console.error("Couldn't delete a message");
            console.log(error);
        }
    }
}