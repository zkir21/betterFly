package com.unitedthinkers.fly.utils.code_as_html;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.TextRange;
import java.util.Arrays;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/13
 */
public class Utils {

	static String replaceAll(String text, char original, String replacement) {
		StringBuilder stringBuilder = new StringBuilder(text.length());
		int i = 0;

		for(int length = text.length(); i < length; ++i) {
			char c = text.charAt(i);
			if (c == original) {
				stringBuilder.append(replacement);
			} else {
				stringBuilder.append(c);
			}
		}

		return stringBuilder.toString();
	}

	static void replaceAll(StringBuffer buffer, char original, String replacement) {
		for(int i = 0; i < buffer.length(); ++i) {
			char c = buffer.charAt(i);
			if (c == original) {
				buffer.replace(i, i + 1, replacement);
				i += replacement.length() - 1;
			}
		}

	}

	static String formatInt(int value, int minDigits) {
		StringBuffer buffer = new StringBuffer();
		String valueText = Integer.toString(value);

		for(int i = 0; i < minDigits - valueText.length(); ++i) {
			buffer.append(" ");
		}

		buffer.append(valueText);
		return new String(buffer);
	}

	static boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	static void quoteForXml(StringBuffer tokenText) {
		replaceAll(tokenText, '<', "&lt;");
		replaceAll(tokenText, '>', "&gt;");
	}

	static TextRange getSelectedTextRange(Editor editor) {
		SelectionModel selectionModel = editor.getSelectionModel();
		int selectionStart;
		int selectionEnd;
		if (selectionModel.hasSelection()) {
			selectionStart = selectionModel.getSelectionStart();
			selectionEnd = selectionModel.getSelectionEnd();
		} else {
			selectionStart = 0;
			selectionEnd = Integer.MAX_VALUE;
		}

		return new TextRange(selectionStart, selectionEnd);
	}

	public static String repeat(char c, int count) {
		char[] chars = new char[count];
		Arrays.fill(chars, c);
		return new String(chars);
	}
}
