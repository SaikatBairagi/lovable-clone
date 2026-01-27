package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CompletionService {

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);
    private final FileService fileService;

    @Async
    public void saveFileAndFileTreeOnCompletion(String fullResponseFromLLM, Long projectId) {
//        String dummy = """
//                <message> I'm going to read the files and generate the code </message>
//                <file path="src/App.jsx">
//                    import App from './App.jsx'
//                    .....
//                </file>
//                <message> I'm going to read the files and generate the code </message>
//                <file path="src/App.jsx">
//                    import App from './App.jsx'
//                    .....
//                </file>
//                """;
        Matcher matcher = FILE_TAG_PATTERN.matcher(fullResponseFromLLM);
        while (matcher.find()) {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2).trim();

            fileService.saveFile(projectId, filePath, fileContent);
        }
    }
}
