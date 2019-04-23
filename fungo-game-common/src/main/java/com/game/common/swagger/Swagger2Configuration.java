package com.game.common.swagger;

import com.game.common.dto.MemberUserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	@Bean
	public Docket buildDocket() {
		List<Parameter> operationParameters = new ArrayList<>();
		operationParameters.add(new ParameterBuilder().name("").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		return new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(buildApiInf())//
				.select()//
				.apis(RequestHandlerSelectors.any())//
				.paths(PathSelectors.any())//
				.build()//
				.globalOperationParameters(operationParameters)//
				.ignoredParameterTypes(MemberUserProfile.class);
	}

	private ApiInfo buildApiInf() {
		return new ApiInfoBuilder()//
				.title("API文档")//
				.contact(new Contact("fungo", "http://www.fungo.com", "sam@qq.com"))//
				.version("1.0")//
				.build();
	}
}
