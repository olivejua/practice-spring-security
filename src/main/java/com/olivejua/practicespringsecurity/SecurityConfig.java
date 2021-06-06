package com.olivejua.practicespringsecurity;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SecurityConfig implements Filter{

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // do something before the rest of the application
        chain.doFilter(request, response); // invoke the rest of the application
        // do something after the rest of the application
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // Lazily get Filter that was registered as a Spring Bean
        // For the example in delegatingFilterProxy delegate is an instance of Bean Filter0
        Filter delegate = getFilterBean(someBeanName);
        //Delegate work to the Spring Bean
        chain.doFilter(request, response); // invoke the rest of the application
    }
}
