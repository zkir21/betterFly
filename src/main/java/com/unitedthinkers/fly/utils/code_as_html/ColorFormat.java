package com.unitedthinkers.fly.utils.code_as_html;

import java.awt.Color;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/13
 */
public class ColorFormat extends Format {

	public Object parseObject(String source, ParsePosition pos) {
		return null;
	}

	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		Color color = (Color)obj;
		toAppendTo.append('#');
		this.appendColor(toAppendTo, color.getRed());
		this.appendColor(toAppendTo, color.getGreen());
		this.appendColor(toAppendTo, color.getBlue());
		return toAppendTo;
	}

	private void appendColor(StringBuffer buffer, int value) {
		String hex = Integer.toHexString(value);
		if (hex.length() < 2) {
			buffer.append('0');
		}

		buffer.append(hex);
	}
}
