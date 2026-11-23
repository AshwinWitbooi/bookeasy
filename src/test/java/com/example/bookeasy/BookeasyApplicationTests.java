package com.example.bookeasy;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookeasyApplicationTests {

	@Autowired
	private InfoEndpoint infoEndpoint;

	@Test
	void contextLoads() {
	}

	@Test
	void infoEndpointIncludesApplicationBuildAndAvailableGitInformation() {
		Map<String, Object> info = infoEndpoint.info();

		Map<?, ?> app = (Map<?, ?>) info.get("app");
		Assertions.assertEquals("bookeasy", app.get("name"));

		Map<?, ?> build = (Map<?, ?>) info.get("build");
		Assertions.assertEquals("bookeasy", build.get("artifact"));
		Assertions.assertEquals("0.0.1-SNAPSHOT", build.get("version"));
		assertTimestamp(build.get("time"));

		Map<?, ?> git = (Map<?, ?>) info.get("git");
		if (git != null) {
			Assertions.assertFalse(((String) git.get("branch")).isBlank());
			Map<?, ?> commit = (Map<?, ?>) git.get("commit");
			Assertions.assertFalse(((String) commit.get("id")).isBlank());
			assertTimestamp(commit.get("time"));
		}
	}

	private static void assertTimestamp(Object timestamp) {
		if (timestamp instanceof Instant) {
			return;
		}
		Instant.parse((String) timestamp);
	}

}
