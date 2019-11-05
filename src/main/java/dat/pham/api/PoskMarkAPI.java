package dat.pham.api;

import dat.pham.api.dto.poskmask.MessageResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PoskMarkAPI {
	@Headers({"Accept: application/json", "X-Postmark-Server-Token: 4aa477fa-cd1b-424a-ae8d-55d4be8234de"})
	@GET("messages/outbound")
	Call<MessageResponse> get(@Query("recipient") String recipient,
							  @Query("count") String count,
							  @Query("offset") String offset,
							  @Query("fromdate") String fromdate,
							  @Query("todate") String todate,
							  @Query("subject") String subject);
}
