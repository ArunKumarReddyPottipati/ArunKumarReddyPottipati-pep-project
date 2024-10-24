package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("messages", this::createMessageHandler);
        app.delete("messages/{id}", this::deleteMessageHandler);
        app.get("accounts/{userId}/messages", this::getAllMessagesHandler);
        app.get("messages", this::getAllMessages);
        app.get("messages/{messageId}", this::getMessageById);
        app.patch("messages/{id}", this::updateMessageHandler);
        app.post("/login", this::loginHandler);
        app.post("/register", this::registerUserHandler);
        return app;
    }

    // Handler for creating a message
    private void createMessageHandler(Context context) {
        String requestBody = context.body(); 
        try {
            Message newMessage = objectMapper.readValue(requestBody, Message.class); 
            Message createdMessage = messageService.createMessage(newMessage); 
            if (createdMessage != null) {
                context.status(200).json(createdMessage); 
            } else {
                context.status(400); 
            }
        } catch (Exception e) {
            context.status(400); 
        }
    }

    // Handler for deleting a message
    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id")); 
        Message deletedMessage = messageService.deleteMessage(messageId); 

        if (deletedMessage != null) {
            context.status(200).json(deletedMessage); 
        } else {
            context.status(200); 
        }
    }

     // Handler for retrieving all messages for a user
     private void getAllMessagesHandler(Context context) {
        int userId = Integer.parseInt(context.pathParam("userId")); 
        List<Message> messages = messageService.getAllMessagesForUser(userId); 
        context.status(200).json(messages); 
    }

    // Handler for retrieving all messages
    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages(); 
        context.status(200).json(messages); 
    }

    // Handler for retrieving a message by its ID
    private void getMessageById(Context context) {
        String messageIdParam = context.pathParam("messageId");
        int messageId = Integer.parseInt(messageIdParam);
        Message message = messageService.getMessageById(messageId); // Call service to get the message by its ID
        if (message != null) {
            context.status(200).json(message);
        } else {
            context.status(200).result(""); // Return empty body if message is not found
        }
    }

    // Handler for updating a message
    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id")); // Get message ID from the path
        String requestBody = context.body(); // Get the request body
        try {
            Message messageUpdate = objectMapper.readValue(requestBody, Message.class); // Deserialize the message update

            // Update message in the service
            Message updatedMessage = messageService.updateMessage(messageId, messageUpdate.getMessage_text());

            if (updatedMessage != null) {
                context.status(200).json(updatedMessage); // Return the updated message with status 200
            } else {
                context.status(400); // Invalid message text or update failed
            }

        } catch (Exception e) {
            context.status(400); // Handle parsing errors or invalid input
        }

    }

    // Handler for user registration
    private void registerUserHandler(Context context) {
    try {
        Account newAccount = objectMapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.registerAccount(newAccount);
        
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}