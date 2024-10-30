package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Message;
import Service.MessageService;
import Service.AccountService;
import Model.Account;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageService messageService = new MessageService();
    private AccountService accountService = new AccountService();
    private ObjectMapper objectMapper = new ObjectMapper();

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
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            context.status(200).json(message);
        } else {
            context.status(200).result(""); 
        }
    }

    // Handler for updating a message
    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id")); 
        String requestBody = context.body(); 
        try {
            Message messageUpdate = objectMapper.readValue(requestBody, Message.class);
            Message updatedMessage = messageService.updateMessage(messageId, messageUpdate.getMessage_text());

            if (updatedMessage != null) {
                context.status(200).json(updatedMessage); 
            } else {
                context.status(400); 
            }

        } catch (Exception e) {
            context.status(400); 
        }

    }

    // Handler for user registration
    private void registerUserHandler(Context context) {
    try {
        Account newAccount = objectMapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.registerAccount(newAccount);
        
        if (createdAccount != null) {
            context.status(200).json(createdAccount); 
        } else {
            context.status(400).result(""); 
        }
    } catch (Exception e) {
        context.status(400).result("Invalid request body");
    }
}


    // New handler for user login
    private void loginHandler(Context context) {
        try {
            Account loginDetails = objectMapper.readValue(context.body(), Account.class);
            Account account = accountService.validateLogin(loginDetails.getUsername(), loginDetails.getPassword());
            if (account != null) {
                context.status(200).json(account);
            } else {
                context.status(401).result(""); 
            }
        } catch (Exception e) {
            context.status(400).result("Invalid request body");
        }
    }


}
