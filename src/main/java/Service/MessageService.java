package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();

     // Create a message
     public Message createMessage(Message newMessage) {
        if (newMessage.getMessage_text() == null || newMessage.getMessage_text().trim().isEmpty() || newMessage.getMessage_text().length() > 255) {
            return null; 
        }
        if (newMessage.getPosted_by() <= 0) {
            return null;
        }
        return messageDAO.createMessage(newMessage);
    }

    // Delete a message
    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId); 
    }

    // Get all messages for a specific user
    public List<Message> getAllMessagesForUser(int userId) {
        return messageDAO.getAllMessagesForUser(userId); 
    }

    // Method to get all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages(); 
    }

    // Update a message
    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return null; 
        }
        if (messageDAO.updateMessage(messageId, newMessageText)) {
            return messageDAO.getMessageById(messageId); 
        }
        return null;
    }

    // Get a message by its ID
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
}
