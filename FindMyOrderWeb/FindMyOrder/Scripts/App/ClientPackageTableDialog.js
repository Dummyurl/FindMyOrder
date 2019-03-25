

function AssociateClient(Id) {

    var lastVal = $("#ClientId").val();

    $.ajax({
        url: "/ClientPackage/AjaxGetClientName",
        type: "GET",
        data: { id: Id },
        success: function (response) {
            $("#ClientName").val(response.Data);
        },
        error: function (response) {
            alert(response);
        }
    });


    $("#ClientId").val(Id);

}