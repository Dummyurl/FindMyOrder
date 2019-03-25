const functions = require('firebase-functions');

let admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/chat_rooms/{chatRoomId}/{messageId}').onWrite(event => {
	
	//get the message that was written
	let chatRoomId = event.params.chatRoomId;
	console.log("chatRoomId: ", chatRoomId);
	
	let messageId = event.params.messageId;
	console.log("messageId: ", messageId);
	
	let message = event.data.child('message').val();
	console.log("message: ", message);
	
	let senderId = event.data.child('senderUid').val();
	console.log("senderId: ", senderId);
	
	let receiverId = event.data.child('receiverUid').val();
	console.log("receiverId: ", receiverId);
	
	let senderName = event.data.child('senderName').val();
	console.log("senderName: ", senderName);
	
	let receiverFirebaseToken = event.data.child('receiverFirebaseToken').val();
	console.log("receiverFirebaseToken: ", receiverFirebaseToken);

	//we have everything we need
			//Build the message payload and send the message
			console.log("Construction the notification message.");
			const payload = {
				data: {
					data_type: "direct_message",
					title: "FindMyOrder: " + senderName,
					message: message,
					message_id: messageId,
				}
			};
			
			return admin.messaging().sendToDevice(receiverFirebaseToken, payload)
						.then(function(response) {
							console.log("Successfully sent message:", response);
						  })
						  .catch(function(error) {
							console.log("Error sending message:", error);
						  });
	
});