
function AssociateUserRelCar(Id, e) {
    $("#CarId").val(Id);

    $.ajax({
        url: "/Couriers/GetCarInfoAjax",
        type: "GET",
        data: { id: Id },
        success: function (response) {
            $("#CarName").val(response.Data);
        },
        error: function (response) {
            alert(JSON.stringify(response));
        }
    });
}