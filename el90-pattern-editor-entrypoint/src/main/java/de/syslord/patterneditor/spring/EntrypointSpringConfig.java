package de.syslord.patterneditor.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import spring.ParserSpringConfig;
import de.syslord.electonePattern.spring.ElectonePatternSpringConfig;

@Configuration
@Import({ ElectonePatternSpringConfig.class, ParserSpringConfig.class })
@ComponentScan(basePackages = { "de.syslord.*" })
public class EntrypointSpringConfig {

}
