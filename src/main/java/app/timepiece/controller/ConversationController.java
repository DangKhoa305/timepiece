package app.timepiece.controller;

import app.timepiece.dto.ConversationDTO;
import app.timepiece.dto.WatchDTO;
import app.timepiece.entity.Conversation;
import app.timepiece.entity.Watch;
import app.timepiece.repository.WatchRepository;
import app.timepiece.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private WatchRepository watchRepository;

    @PostMapping("/start")
    public ResponseEntity<?> startConversation(@RequestParam Long senderId, @RequestParam Long recipientId, @RequestParam Long watchId) {
        try{
        ConversationDTO conversationDTO = conversationService.getOrCreateConversation(senderId, recipientId, watchId);
        return ResponseEntity.ok(conversationDTO);
    }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversationsByUserId(@PathVariable Long userId) {
        List<ConversationDTO> conversations = conversationService.getConversationsByUserId(userId);
        return ResponseEntity.ok(conversations);
    }


}
