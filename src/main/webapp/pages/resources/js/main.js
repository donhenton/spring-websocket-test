          /***********************************************/
            /* PLEASE READ UP ON STOMP           */
            /* 1) http://jmesnil.net/stomp-websocket/doc/  */
           
            /***********************************************/

            //Declare a stompclient which will connect to the server
            var stompClient = null;

            /************************************************************************** 
            /*  JQUERY WAY OF BEING UNOBTRUSIVE AND ADDING EVENT HANDLERS TO WIDGETS, 
            /*  THUS KEEPING HTML AND JAVASCRIPT SEPARATE 
            /*************************************************************************/
            // Runs this code only when the DOM (all elements) are ready
            $(document).ready(function() {
                // On page load the text input field 'MESSAGE', 'DISCONNECT' and 'SEND' buttons 
                // should all be disabled as user has not clickedd 'CONNECT' button yet.
                $("#disconnect").prop('disabled', true);
                $("#txtSendMessage").prop('disabled', true);
                $("#sendMessage").prop('disabled', true);

                //Also all text in server message should be empty
                $("#txtSendMessage").val("");
                //Remove any server responses from previous interactions
                $("#response").empty();
                //Hide the validation and info alerts on page load
                $(".alert").hide();
                // Event handler: Connect button
                $("#connect").on("click", function(e) {
                    // If alert is visible, hide it
                    $("#formAlert").slideUp(400);
                    connect();
                });

                // Event handler: Disconnect button 
                $("#disconnect").on("click", function(e) {
                    // If alert is visible, hide it
                    $("#formAlert").slideUp(400);
                    disconnect();
                });
                // Event handler: X button on top right of info alert.
                // Clicking the X button on top right will dismiss it from the screen and hide it
                $(".alert").find(".close").on("click", function(e) {
                    // Find all elements with the "alert" class, get all descendant elements 
                    // with the class "close", and bind a "click" event handler

                    // Don't allow the click to bubble up the DOM
                    e.stopPropagation();

                    // Don't let any default functionality occur (in case it's a link)
                    e.preventDefault();

                    // Hide this specific Alert
                    $(this).closest(".alert").slideUp(400);

                    // Focus on the Send Message textfield
                    $("#txtSendMessage").select();
                    $("#txtSendMessage").focus();
                });

                // Event handler: Send button
                $("#sendMessage").on("click", function(e) {

                    // Find the input text element for the server message
                    var messageForServer = $("#txtSendMessage").val();

                    if (messageForServer === "") {

                        // If message is empty prevent submission and show the alert
                        e.preventDefault();
                        $("#formAlert").slideDown(400);

                    } else {

                        // Message is not empty so send to server
                        $("#formAlert").slideUp(400);

                        // Show a please wait alert
                        $("#formInfoAlert").slideDown(400);

                        // Send message to server. The message for the server must 
                        // be in JSON format. 
                        // Also refer SimpleMessage.java POJO.
                        sendMessageToServer(messageForServer);
                    }
                });
            });

            //Function sets the state of the Connect and Disconnect buttons
            function setConnected(connected) {
                //Since we are using bootstrap, this is how you disable buttons 
                // and input widgets
                $("#connect").prop('disabled', connected);
                $("#disconnect").prop('disabled', !connected);
                $("#sendMessage").prop('disabled', !connected);
                $("#txtSendMessage").prop('disabled', !connected)
            }

            // Function to connect the web client to the websocket server
            function connect() {
                //Remove any server responses from previous interactions
                $("#response").empty();
                //Also all text in server message input field should be empty
                $("#txtSendMessage").val("");
                $("#txtSendMessage").focus();
                $("#txtSendMessage").select();
                // Register a websocket endpoint using   stomp.js
                                 
                stompClient = Stomp.client( "ws://192.168.99.100:61614/stomp", "v11.stomp" );
                // Now that a stomp client is defined, its time to open a connection
                // 1) First we connect to the websocket server
              
                // 2) The last argument is a callback function which is called when connection succeeds
                stompClient.connect('admin','admin', function(frame) {
                    //set the connect and disconnect button state. (disable connect button)
                    setConnected(true);
                    // In production code remove the next line
                    console.log("Connected: " + frame);
                    //Lets show a connection success message
                    showServerBroadcast("Connection established: " + frame, false);
                    // Now subscribe to a topic of interest.
                   
                    // When a broadcast message is received by the client on that destination, it will be shown by appending
                    // a paragraph to the DOM in the client browser.
                    stompClient.subscribe("testQueue.out", function(servermessage) {//Callback when server responds
                        console.log("--- "+servermessage.body+" ---");
                        showServerBroadcast(JSON.parse(servermessage.body).messageContent, false);
                        //Server responded so hide the info alert
                        $("#formInfoAlert").slideUp(400);
                        //Also all text in server message input field should be empty
                        $("#txtSendMessage").val("");
                        $("#txtSendMessage").focus();
                        $("#txtSendMessage").select();
                    });
                });
            }

            // Function to disconnect the web client to the websocket server
            function disconnect() {
                //First hide any alerts
                $("#formAlert").slideUp(400);
                $("#formInfoAlert").slideUp(400);
                // Disconnect the stompClient
                stompClient.disconnect();
                // Set the connect and disconnect button states
                setConnected(false);
                // In production remove the next line
                console.log("Disconnected");
                showServerBroadcast("WebSocket connection is now terminated!", true);
            }

            // Function to send the message typed in by the user to the "/app/simplemessages" destination on the server.
            // WebsocketBroadcastController will receive this message and broadcast the results after 
            // an artificially introduced delay.
            function sendMessageToServer(messageForServer) {
                //Show on the browser page that a message is being sent
                showServerBroadcast("Your message '" + messageForServer + "' is being sent to the server.", true);
                // The message for the server must be in JSON format. Also refer SimpleMessage.java POJO.
                stompClient.send("testQueue.in", {}, JSON.stringify({
                    'message' : messageForServer
                }));
            }

            /**
             * Function to show the server response on the web page
             * @param servermessage - text to be shown on webpage
             * @param localMessage - boolean, if true then it means its a 
             *                       client side javascript generated message.
             */
            function showServerBroadcast(servermessage, localMessage) {
                // Server surrounds the user sent message with <b> and </b> 
                // as &ltb&gt;message%lt;/b&gt;
                // Use Jquery to decode the HTML and show it as <b>message</b>
                var decoded = $("<div/>").html(servermessage).text();

                var tmp = "";
                var serverResponse = document.getElementById("response");
                var p = document.createElement('p');
                p.style.wordWrap = 'break-word';

                if (localMessage) {
                    p.style.color = '#006600';
                    tmp = "<span class='glyphicon glyphicon-dashboard'></span> " + decoded + " (Browser time:" + getCurrentDateTime() + ")";
                } else {
                    p.style.color = '#8A0808';
                    tmp = "<span class='glyphicon glyphicon-arrow-right'></span> " + decoded;
                }
                //Assigning the decoded HTML to the <p> element
                p.innerHTML = tmp;
                serverResponse.appendChild(p);
            }

            /**
             * Utility function to return the date time in simple format
             * like Tue Jan 07 2014 @ 11:47:24 AM
             */
            function getCurrentDateTime() {
                var date = new Date();
                var n = date.toDateString();
                var time = date.toLocaleTimeString();
                return n + " @ " + time;
            }