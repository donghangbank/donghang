package bank.donghang.core.common.interceptor;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import bank.donghang.core.common.inspector.QueryCountInspector;

@ExtendWith(MockitoExtension.class)
class QueryLoggingInterceptorTest {

	private MockHttpServletRequest mockHttpServletRequest;

	@Mock
	private QueryCountInspector queryCountInspector;

	@InjectMocks
	private QueryLoggingInterceptor queryLoggingInterceptor;

	@BeforeEach
	void setUp() {
		mockHttpServletRequest = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));
	}

	@Test
	@DisplayName("preHandle 단계에서 queryCountInspector.startCounter()가 호출된다.")
	void preHandle() throws Exception {
		doNothing().when(queryCountInspector).startCounter();

		queryLoggingInterceptor.preHandle(
			mockHttpServletRequest,
			null,
			null
		);

		Mockito.verify(queryCountInspector).startCounter();
	}

	@Test
	@DisplayName("afterCompletion 단계에서 queryCountInspector의 값을 가져와 로그에 출력한다.")
	void afterCompletion() throws Exception {
		doNothing().when(queryCountInspector).clearCounter();
		Mockito.when(queryCountInspector.getCount())
			.thenReturn(1L);
		Mockito.when(queryCountInspector.getTimeStamp())
			.thenReturn(0L);

		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		mockHttpServletResponse.setStatus(200);

		queryLoggingInterceptor.afterCompletion(
			mockHttpServletRequest,
			mockHttpServletResponse,
			null,
			null
		);

		Mockito.verify(queryCountInspector).getCount();
		Mockito.verify(queryCountInspector).getTimeStamp();
	}

	@Test
	@DisplayName("쿼리 횟수가 특정 횟수를 넘어가면 경고 로그를 출력한다.")
	void afterCompletion_log_warn() throws Exception {
		doNothing().when(queryCountInspector).clearCounter();
		Mockito.when(queryCountInspector.getCount()).
			thenReturn(100000L);
		Mockito.when(queryCountInspector.getTimeStamp())
			.thenReturn(0L);

		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		mockHttpServletResponse.setStatus(200);

		queryLoggingInterceptor.afterCompletion(
			mockHttpServletRequest,
			mockHttpServletResponse,
			null,
			null
		);

		Mockito.verify(queryCountInspector).getCount();
		Mockito.verify(queryCountInspector).getTimeStamp();
	}

}