/*
 * Copyright 2016-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.paramount.msp.starter;

import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;
import net.paramount.ase.config.MspQuartzConfig;
import net.paramount.msp.config.BaseConfiguration;
import net.paramount.msp.config.SecurityConfig;
import net.paramount.msp.model.Car;
import net.paramount.msp.util.FacesUtilities;

/**
 * @author ducbq
 */
@Slf4j
@Import(value = { BaseConfiguration.class, SecurityConfig.class, MspQuartzConfig.class })
@SpringBootApplication(scanBasePackages={"net.paramount"})
@EnableAsync
public class AdminStarterApplication {
	@Inject
	private FacesUtilities utils;

	@Bean
	public List<Car> getCars() {
		return utils.getCars();
	}

	public static void main(String[] args) {
		AdminStarterApplication adminStarterApplication = new AdminStarterApplication();
		SpringApplication springBootApp = new SpringApplication(adminStarterApplication.getClass());
		ConfigurableApplicationContext configAppContext = springBootApp.run(args);
		log.info("Started application context: " + configAppContext);
	}
}
