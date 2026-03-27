package com.aliyara.generatorservice.adapter.out.template;

import com.aliyara.generatorservice.application.port.out.TemplateEnginePort;
import com.aliyara.generatorservice.domain.exception.DocumentGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThymeleafTemplateAdapter implements TemplateEnginePort {

    private final TemplateEngine templateEngine;

    @Override
    public String render(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            return templateEngine.process(templateName, context);
        } catch (Exception e) {
            log.error("Failed to render template: {}", templateName, e);
            throw new DocumentGenerationException("Template rendering failed for: " + templateName, e);
        }
    }
}
