package com.company.wordAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        String sourceFile = "test1.txt";
        String outputFile = "tokenOut1.txt";
        String errorFile = "errorOut1.txt";
        WordAnalysis lexer=new WordAnalysis();

        List<String> sentences = new ArrayList<>();
        File file = new File(sourceFile);
        if(!file.exists()){
            System.out.println("00000");
        }
        if (file.isFile() && file.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                sentences.add(line + "\n");
            }
            br.close();
        }
        // 记录行号
        int line = 1;
        // 逐行处理
        for (String s : sentences) {
			System.out.println(line + ": " + s);
            lexer.handle(s, line);
			System.out.println("finish");
            line++;
        }
        // 写Token文件
        lexer.output(outputFile);
        // 写Error文件
        lexer.errorOutput(errorFile);
    }
}
