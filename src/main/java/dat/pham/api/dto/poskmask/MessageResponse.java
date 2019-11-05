package dat.pham.api.dto.poskmask;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse {
	@JsonProperty("Messages")
	private List<POResponse> messages;

	public List<POResponse> getMessages() {
		return messages;
	}

	public void setMessages(List<POResponse> messages) {
		this.messages = messages;
	}
}
