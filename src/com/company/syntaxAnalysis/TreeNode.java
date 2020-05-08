package com.company.syntaxAnalysis;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    List<TreeNode> children = new ArrayList<>();
    private Statement statement;//该节点的推到类型，即时那棵语法树
    private TokenType tokenType;//该节点的字符类型
    private String value;//该节点的值

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        String string;
        string = "Statement" + statement.toString();
        if (tokenType != null && value != null) {
            string = string + " <" + tokenType.toString() + " , "+value+"> ";

        }
        return string;
    }

    public static StringBuilder proOrder(TreeNode nowNode,StringBuilder nowResult,String prefix){
        if(nowNode!=null){
            nowResult.append(prefix+nowNode.toString()+"\r\n");
            if(nowNode.children!=null&&nowNode.children.size()!=0){
                for(TreeNode node:nowNode.children){
                    proOrder(nowNode,nowResult,"\t"+prefix);
                }
            }
        }
        return nowResult;
    }

}