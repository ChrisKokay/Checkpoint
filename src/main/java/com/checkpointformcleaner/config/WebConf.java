package com.checkpointformcleaner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConf extends WebMvcConfigurerAdapter {
	
	//ez csak azért van használva, h kikérjük belőle a registry-t
	//a registry az, ami tárol kapcsolódási pontokat az end-point és a nézetek közt
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		//a registry-hez adok egy /logjn end-point-ot, mivel ezt a SecurityConf configure metódusában is
		//megtettem (ide irányítjuk az oldalt), s a setViewName-vel megadjuk, h pontosan hol van a html fájl
		//tehát ha valaki a /login-t keresi, akkor az auth/login.html helyen találja meg a fájlt, a nézetet
        registry.addViewController("/login").setViewName("auth/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);		
	}	

}
