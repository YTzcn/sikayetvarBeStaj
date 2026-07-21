package com.sikayetvar.beStaj.ai.service;

import com.sikayetvar.beStaj.ai.config.openai.OpenAiProperties;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiChatRequest;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiChatResponse;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiChoice;
import com.sikayetvar.beStaj.ai.dto.openai.OpenAiMessage;
import com.sikayetvar.beStaj.ai.exception.openai.OpenAiIntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAiChatServiceTest {

    private static final String API_KEY = "test-api-key";
    private static final String MODEL = "gpt-4o-mini";

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private OpenAiChatService service;

    @BeforeEach
    void setUp() {
        OpenAiProperties properties = new OpenAiProperties("https://api.openai.com/v1", API_KEY, MODEL, 30);
        service = new OpenAiChatService(restClient, properties);
    }

    @Test
    void blankApiKey_throwsExceptionWithoutSendingRequest() {
        OpenAiProperties blankApiKeyProperties = new OpenAiProperties("https://api.openai.com/v1", "", MODEL, 30);
        OpenAiChatService serviceWithBlankApiKey = new OpenAiChatService(restClient, blankApiKeyProperties);

        assertThatThrownBy(() -> serviceWithBlankApiKey.getChatReply("hello"))
                .isInstanceOf(OpenAiIntegrationException.class)
                .hasMessageContaining("OPENAI_API_KEY");

        verifyNoInteractions(restClient);
    }

    @Test
    void successfulResponse_returnsModelReply() {
        mockRequestChain();
        when(responseSpec.body(OpenAiChatResponse.class)).thenReturn(
                new OpenAiChatResponse(List.of(new OpenAiChoice(new OpenAiMessage("assistant", "Hello!")))));

        String reply = service.getChatReply("Hello");

        assertThat(reply).isEqualTo("Hello!");
    }

    @Test
    void sendsRequestWithCorrectModelAndUserMessage() {
        mockRequestChain();
        when(responseSpec.body(OpenAiChatResponse.class)).thenReturn(
                new OpenAiChatResponse(List.of(new OpenAiChoice(new OpenAiMessage("assistant", "ok")))));

        service.getChatReply("Hi there");

        ArgumentCaptor<OpenAiChatRequest> requestCaptor = ArgumentCaptor.forClass(OpenAiChatRequest.class);
        verify(requestBodySpec).body(requestCaptor.capture());

        OpenAiChatRequest sentRequest = requestCaptor.getValue();
        assertThat(sentRequest.model()).isEqualTo(MODEL);
        assertThat(sentRequest.messages()).hasSize(1);
        assertThat(sentRequest.messages().getFirst().role()).isEqualTo("user");
        assertThat(sentRequest.messages().getFirst().content()).isEqualTo("Hi there");
    }

    @Test
    void networkErrorDuringRequest_wrapsInCustomException() {
        mockRequestChain();
        when(responseSpec.body(OpenAiChatResponse.class)).thenThrow(new RestClientException("connection error"));

        assertThatThrownBy(() -> service.getChatReply("Hello"))
                .isInstanceOf(OpenAiIntegrationException.class)
                .hasCauseInstanceOf(RestClientException.class);
    }

    @Test
    void nullResponseBody_throwsException() {
        mockRequestChain();
        when(responseSpec.body(OpenAiChatResponse.class)).thenReturn(null);

        assertThatThrownBy(() -> service.getChatReply("Hello"))
                .isInstanceOf(OpenAiIntegrationException.class)
                .hasMessageContaining("boş yanıt");
    }

    @Test
    void emptyChoicesList_throwsException() {
        mockRequestChain();
        when(responseSpec.body(OpenAiChatResponse.class)).thenReturn(new OpenAiChatResponse(List.of()));

        assertThatThrownBy(() -> service.getChatReply("Hello"))
                .isInstanceOf(OpenAiIntegrationException.class)
                .hasMessageContaining("boş yanıt");
    }

    private void mockRequestChain() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(OpenAiChatRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
    }
}
