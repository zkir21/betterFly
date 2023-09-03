package com.unitedthinkers.fly.utils.code_as_html;

import com.intellij.openapi.editor.markup.TextAttributes;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/13
 */
public class RangeHighlight implements Comparable {

	private int _startOffset;
	private int _endOffset;
	private TextAttributes _textAttributes;

	public RangeHighlight(int startOffset, int endOffset, TextAttributes textAttributes) {
		this._startOffset = startOffset;
		this._endOffset = endOffset;
		this._textAttributes = textAttributes;
	}

	public Object getTextAttributes() {
		return this._textAttributes;
	}

	public int getStartOffset() {
		return this._startOffset;
	}

	public int getEndOffset() {
		return this._endOffset;
	}

	public int compareTo(Object o) {
		RangeHighlight rangeHighlight = (RangeHighlight)o;
		return this._startOffset - rangeHighlight._startOffset;
	}

	public boolean equals(Object o) {
		if (!(o instanceof RangeHighlight)) {
			return false;
		} else {
			RangeHighlight rangeHighlight = (RangeHighlight)o;
			return this._startOffset == rangeHighlight._startOffset;
		}
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("range=").append(this._startOffset).append("...").append(this._endOffset);
		stringBuilder.append(", foreground=").append(this._textAttributes.getForegroundColor());
		stringBuilder.append(", background=").append(this._textAttributes.getBackgroundColor());
		stringBuilder.append(", font style=").append(this._textAttributes.getFontType());
		return stringBuilder.toString();
	}
}
