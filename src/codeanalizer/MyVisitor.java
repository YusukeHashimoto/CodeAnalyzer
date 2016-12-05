package codeanalizer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class MyVisitor extends ASTVisitor {
	private List<MethodDeclaration> methodList;
	private List<VariableDeclarationFragment> variableList;
	private List<Block> blockList;
	static final String LINE_COUNT = "line";
	static final String LIFE_SPAN = "life";
	static final String DECLARED_LINE = "declared_line";
	static final String CYCLOMATIC_COMPLEXITY = "mccabe";
	private MyParser parser;
	private int cyclomaticComplexity = 1;

	public List<MethodDeclaration> getMethodList() {
		return methodList;
	}

	public List<VariableDeclarationFragment> getVariableList() {
		return variableList;
	}

	public List<Block> getBlockList() {
		return blockList;
	}

	MyVisitor(String code) {
		methodList = new ArrayList<>();
		variableList = new ArrayList<>();
		blockList = new ArrayList<>();
		parser = new MyParser(code);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		node.setProperty(LINE_COUNT, Integer.valueOf(countLines(node.getBody().toString())));
		node.setProperty(CYCLOMATIC_COMPLEXITY, 1);
		methodList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		node.setProperty(LIFE_SPAN, parser.lifeSpanOf(node));
		variableList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Block node) {
		blockList.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(IfStatement node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchCase node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	@Override
	public boolean visit(WhileStatement node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	@Override
	public boolean visit(ForStatement node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);

	}

	@Override
	public boolean visit(CatchClause node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	@Override
	public boolean visit(DoStatement node) {
		cyclomaticComplexity++;
		increaseCC(parentMethodOf(node));
		return super.visit(node);
	}

	int totalCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	MethodDeclaration parentMethodOf(ASTNode node) {
		ASTNode parent = node.getParent();
		while(!(parent instanceof MethodDeclaration)) {
			parent = parent.getParent();
		}
		return (MethodDeclaration) parent;
	}

	void increaseCC(MethodDeclaration node) {
		node.setProperty(CYCLOMATIC_COMPLEXITY, (Integer) node.getProperty(CYCLOMATIC_COMPLEXITY) + 1);
	}

	private static int countLines(String code) {
		int n = 0;
		for(char c : code.toCharArray()) {
			if(c == '\n') n++;
		}
		return n;
	}
}
