package dat.pham;

import dat.pham.api.PoskMarkAPI;
import dat.pham.api.dto.poskmask.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class TestService {
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);

	@Autowired
	private PoskMarkAPI poskMarkAPI;

	@PostConstruct
	public void test() throws IOException {
//		getMessage();
	}

	private void getMessage() throws IOException {
		String recipient = "jonathancrisp63@gmail.com";
		String count = "50";
		String offset = "0";
		String fromdate = "2019-09-01";
		String todate = "2019-09-30";
		String subject = "West Wind - Burrumbeet Jumpout Tomorrow";
		MessageResponse response = poskMarkAPI.get(recipient, count, offset, fromdate, todate, subject).execute().body();
		logger.info("View Booking Notes: {}", response.getMessages().get(0).getStatus());
	}
}
