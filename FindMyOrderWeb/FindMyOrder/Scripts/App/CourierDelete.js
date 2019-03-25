
(function () {

    var config = {
        apiKey: "AIzaSyCD7FFMRJnrmTEgbURDm0V-xJkkCDe5sak",
        authDomain: "findmyorder-cd5f3.firebaseapp.com",
        databaseURL: "https://findmyorder-cd5f3.firebaseio.com",
        projectId: "findmyorder-cd5f3",
        storageBucket: "findmyorder-cd5f3.appspot.com",
        messagingSenderId: "802048111602"
    };
    firebase.initializeApp(config);

    var auth = firebase.auth();


    $("#firebaseBtnDelete").on('click', e => {

        var myEmail = $("#firebaseEmailDelete").val();
        var myPassword = $("#firebasePasswordDelete").val();

        auth.signOut()
        auth.signInWithEmailAndPassword(myEmail, myPassword)
            .then(function (user) {
                user.delete().then(function () {
                    alert("utilizator sters cu succes !");
                    $("#SubmitDeleteFormButton").click();
                }), function (error) {
                    alert(error.message);
                }
            })
            .catch(function (error) {
                alert(error.message);
            });
        auth.signOut()

    });
}());