package com.company.syntaxAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainS {

    public static void main(String[] args) throws IOException {
        String tokenOutFilename = "tokenOut8.txt";
        String syntaxOutFilename = "syntaxOut8.txt";
        String errorFilename = "syntaxErrorOut8.txt";
        System.out.println(32321);
        SyntaxAnalysis Syntax = new SyntaxAnalysis();

        List<String> tokenList = new ArrayList<>();
        File file = new File(tokenOutFilename);

        if (file.isFile() && file.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                tokenList.add(line);
            }
            br.close();
        }
        Syntax.setTokenList(tokenList);

        Syntax.analysis();

        Syntax.output(syntaxOutFilename);
        Syntax.errorOutput(errorFilename);
        System.out.println(32321);
    }
}
