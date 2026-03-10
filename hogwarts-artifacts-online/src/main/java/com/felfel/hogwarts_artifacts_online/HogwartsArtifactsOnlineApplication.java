package com.felfel.hogwarts_artifacts_online;

import com.felfel.hogwarts_artifacts_online.artifact.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The type Hogwarts artifacts online application.
 */
@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
	}

	/**
	 * Id worker id worker.
	 *
	 * @return the id worker
	 */
	@Bean
	public IdWorker idWorker()
	{
		return new IdWorker(1,1);
	}

}