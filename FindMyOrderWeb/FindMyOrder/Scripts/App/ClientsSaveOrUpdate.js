
(function () {


    var initialEmail = $("#firebaseEmail").val();

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


    $("#firebaseBtnSignUpClient").on('click', e => {

        var email = $("#firebaseEmail").val();
        var myId = $("#Id").val();
        var myPassword = document.getElementById("Password").value;


        if ($("#popupFormClient").valid()) { //daca e valid adaugam in baza

            if (myId == -1) { //create
                auth.createUserWithEmailAndPassword(email, myPassword)
                    .then(function (user) {
                        document.getElementById("Token").value = user.uid;
                        alert("Utilizator creat cu succes !");
                        $("#SubmitFormButtonClient").click();
                    })
                    .catch(function (error) {
                        if (error.message === "The email address is already in use by another account.") {
                            alert("Aceasta adresa de email este deja alocata unui user.");
                        } else {
                            alert(error.message);
                        }
                    });
            } else if (myId != -1) { //update  
                auth.signOut()
                auth.signInWithEmailAndPassword(initialEmail, myPassword)
                    .then(function (user) {
                        user.updateEmail(email)
                        alert("Informatii modificate cu succes !");
                        $("#SubmitFormButtonClient").click();
                    })
                    .catch(function (error) {
                        alert(error.message);
                    });
                auth.signOut()
            }
        } else { //avem erori...

        }

    });
}());