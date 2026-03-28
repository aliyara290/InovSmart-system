package com.aliyara.generatorservice.application.port.out;

import java.util.Map;

public interface TemplateEnginePort {

    String render(String templateName, Map<String, Object> variables);
}
