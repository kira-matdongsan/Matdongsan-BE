package com.example.matdongsan.common.util.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateRenderer {

    private final TemplateEngine templateEngine;

    public String buildTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context); // ex: "email/verification"
    }
}
