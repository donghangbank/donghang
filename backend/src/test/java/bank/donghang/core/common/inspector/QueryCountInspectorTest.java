package bank.donghang.core.common.inspector;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(locations = "file:${user.dir}/test.env")
@SpringBootTest
class QueryCountInspectorTest {

	@Autowired
	private QueryCountInspector queryCountInspector;

	@Test
	@DisplayName("JPA에 의해 inspect이 호출되면 카운트가 증가한다.")
	void inspect() {
		queryCountInspector.startCounter();

		queryCountInspector.inspect("test query");

		Assertions.assertThat(queryCountInspector.getCount()).isOne();
	}

}