package com.sikayetvar.beStaj.ai.controller;

import com.sikayetvar.beStaj.ai.dto.ChatRequest;
import com.sikayetvar.beStaj.ai.dto.ChatResponse;
import com.sikayetvar.beStaj.ai.service.AiChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private AiChatService chatService;

    private ChatController controller;

    @BeforeEach
    void setUp() {
        controller = new ChatController(chatService);
    }

    @Test
    void validMessage_callsServiceAndReturnsReply() {
        when(chatService.getChatReply("Hello")).thenReturn("Hi, how can I help you?");

        ChatResponse response = controller.chat(new ChatRequest("Hello"));

        assertThat(response.reply()).isEqualTo("Hi, how can I help you?");
        verify(chatService).getChatReply("Hello");
    }

    @Test
    void nullMessage_throwsExceptionWithoutCallingService() {
        assertThatThrownBy(() -> controller.chat(new ChatRequest(null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("boş olamaz");

        verifyNoInteractions(chatService);
    }

    @Test
    void blankMessage_throwsExceptionWithoutCallingService() {
        assertThatThrownBy(() -> controller.chat(new ChatRequest("   ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("boş olamaz");

        verifyNoInteractions(chatService);
    }
}
