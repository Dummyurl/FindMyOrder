


function AssociateUserRelClient(Id) {
    $("#ClientId").val(Id);

    $('#ClientPackageId').val(0)
    $('#ClientPackageName').val("")


    $.ajax({
        url: "/UserRelationship/AjaxGetUserRelClientName",
        type: "GET",
        data: { id: Id },
        success: function (response) {
            $("#ClientName").val(response.Data);
        },
        error: function (response) {
            alert(response);
        }
    });
}


function AssociateUserRelCourier(Id) {
    $("#CourierId").val(Id);
    $.ajax({
        url: "/UserRelationship/AjaxGetUserRelCourierName",
        type: "GET",
        data: { id: Id },
        success: function (response) {
            $("#CourierName").val(response.Data);
        },
        error: function (response) {
            alert(response);
        }
    });
}

function AssociateUserRelClientPackage(Id) {
    $("#ClientPackageId").val(Id);
    $.ajax({
        url: "/UserRelationship/AjaxGetUserRelClientPackageName",
        type: "GET",
        data: { id: Id },
        success: function (response) {
            $("#ClientPackageName").val(response.Data);
        },
        error: function (response) {
            alert(response);
        }
    });
}

