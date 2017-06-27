package org.iptc.extra.core.cql;

import org.antlr.v4.runtime.Token;

/**
 * @author manosetro
 *
 */
public class SyntaxError {
		
		private int line;
		private int position;
		private String offendingToken;
		private int[] offendingTokenPosition = new int[2];
		
		public SyntaxError(int line, int position, Token offendingToken) {
			this.line = line;
			this.position = position;
			this.offendingToken = offendingToken.getText();
			this.offendingTokenPosition[0] = offendingToken.getStartIndex();
			this.offendingTokenPosition[1] = offendingToken.getStopIndex();
		}
		
		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public String getOffendingToken() {
			return offendingToken;
		}

		public void setOffendingToken(String offendingToken) {
			this.offendingToken = offendingToken;
		}

		public int[] getOffendingTokenPosition() {
			return offendingTokenPosition;
		}

		public void setOffendingTokenPosition(int[] offendingTokenPosition) {
			this.offendingTokenPosition = offendingTokenPosition;
		}

		public String toString() {
			return "Syntax Error at line " + line + ":" + position + ". Offending Token: " + offendingToken + " [" + offendingTokenPosition[0] + ":" + offendingTokenPosition[1] + "]";
		}
	}