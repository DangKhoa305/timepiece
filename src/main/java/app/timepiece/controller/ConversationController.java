package app.timepiece.controller;

import app.timepiece.entity.Conversation;
import app.timepiece.entity.Watch;
import app.timepiece.repository.WatchRepository;
import app.timepiece.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private WatchRepository watchRepository;

    @PostMapping("/start")
    public Conversation startConversation(@RequestParam Long senderId, @RequestParam Long watchId) {
        Watch watch = watchRepository.findById(watchId).orElseThrow(() -> new RuntimeException("Watch not found"));
        Long recipientId = watch.getUser().getId();

        return conversationService.getOrCreateConversation(senderId, recipientId);
    }
}
