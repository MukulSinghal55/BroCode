package com.example.gpt3;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.theokanning.openai.completion.*;
import com.theokanning.openai.engine.*;

import com.theokanning.openai.completion.chat.*


import java.util.HashMap;
import java.util.List;

public class GPT3Service {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String MODEL_ID = "YOUR_MODEL_ID";

    private final Project project;

    public GPT3Service(Project project) {
        this.project = project;
    }

    public String generateText(String prompt, int maxTokens) {
        try {
            // Get a list of available engines
            Request<List<Engine>> engineListRequest = new EngineListRequest(API_KEY);
            List<Engine> engines = engineListRequest.execute();

            // Find the GPT-3 engine by ID
            Engine engine = null;
            for (Engine e : engines) {
                if (e.id.equals(MODEL_ID)) {
                    engine = e;
                    break;
                }
            }

            if (engine == null) {
                throw new RuntimeException("Could not find GPT-3 engine");
            }

            // Set up the completion request
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("engine", engine.id);
            parameters.put("prompt", prompt);
            parameters.put("max_tokens", maxTokens);
            CompletionsRequest completionsRequest = new CompletionsRequest(API_KEY, parameters);

            // Make the completion request and get the generated text
            Completion completion = completionsRequest.execute().getChoices().get(0);
            String generatedText = completion.getText();

            return generatedText;
        } catch (Exception e) {
            ApplicationManager.getApplication().invokeLater(() -> {
                project.getMessageBus().syncPublisher(GPT3ActionListener.TOPIC).onError(e);
            });
            return null;
        }
    }
}

