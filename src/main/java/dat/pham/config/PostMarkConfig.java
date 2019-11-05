package dat.pham.config;

import dat.pham.api.PoskMarkAPI;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class PostMarkConfig {

	private static final String API_BASE_URL = "https://api.postmarkapp.com/";

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	private static Retrofit.Builder builder =
			new Retrofit.Builder()
					.baseUrl(API_BASE_URL)
					.addConverterFactory(JacksonConverterFactory.create());

	private static Retrofit retrofit = builder.build();

	public static <S> S createService(Class<S> serviceClass) {
		return createService(serviceClass, null, null);
	}

	public static <S> S createService(
			Class<S> serviceClass, String username, String password) {
		if (!StringUtils.isEmpty(username)
				&& !StringUtils.isEmpty(password)) {
			String authToken = Credentials.basic(username, password);
			return createService(serviceClass, authToken);
		}

		return createService(serviceClass, null);
	}

	public static <S> S createService(
			Class<S> serviceClass, final String authToken) {

		httpClient.callTimeout(60, TimeUnit.SECONDS);
		httpClient.readTimeout(60, TimeUnit.SECONDS);

		if (!StringUtils.isEmpty(authToken)) {
			AuthenticationInterceptor authInterceptor =
					new AuthenticationInterceptor(authToken);

			if (!httpClient.interceptors().contains(authInterceptor)) {
				httpClient.addInterceptor(authInterceptor);
			}

			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			// set your desired log level
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			if (!httpClient.interceptors().contains(loggingInterceptor)) {
				// add logging as last interceptor
				httpClient.addInterceptor(loggingInterceptor);
			}

			builder.client(httpClient.build());
			retrofit = builder.build();
		}

		return retrofit.create(serviceClass);
	}

	@Bean
	public PoskMarkAPI poskMarkAPI() {
		return createService(PoskMarkAPI.class, "4aa477fa-cd1b-424a-ae8d-55d4be8234de");
	}
}
