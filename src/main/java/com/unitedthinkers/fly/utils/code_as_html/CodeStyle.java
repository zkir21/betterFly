package com.unitedthinkers.fly.utils.code_as_html;

import java.awt.Color;
import java.text.Format;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/13
 */
public class CodeStyle {

	private static final Format _colorFormat = new ColorFormat();
	private Color _foregroundColor;
	private Color _backgroundColor;
	private boolean _bold;
	private boolean _italic;
	private Color _underlineColor;
	private Color _strikeThroughColor;
	private Color _boxColor;

	public CodeStyle(Color foregroundColor, Color backgroundColor, boolean bold, boolean italic, Color underlineColor, Color strikeThroughColor, Color boxColor) {
		this._foregroundColor = foregroundColor;
		this._backgroundColor = backgroundColor;
		this._bold = bold;
		this._italic = italic;
		this._underlineColor = underlineColor;
		this._strikeThroughColor = strikeThroughColor;
		this._boxColor = boxColor;
	}

	public Color getForegroundColor() {
		return this._foregroundColor;
	}

	public Color getBackgroundColor() {
		return this._backgroundColor;
	}

	public boolean isBold() {
		return this._bold;
	}

	public boolean isItalic() {
		return this._italic;
	}

	public Color getUnderlineColor() {
		return this._underlineColor;
	}

	public Color getStrikeThroughColor() {
		return this._strikeThroughColor;
	}

	public Color getBoxColor() {
		return this._boxColor;
	}

	public String startHtml() {
		StringBuffer buffer = new StringBuffer();
		if (this._boxColor != null) {
			buffer.append("<span style=\"border-style:solid; border-width:0.01mm; border-color:");
			buffer.append(_colorFormat.format(this._boxColor));
			buffer.append("\">");
		}

		if (this._underlineColor != null) {
			buffer.append("<span style=\"text-decoration:underline;color:");
			buffer.append(_colorFormat.format(this._underlineColor));
			buffer.append("\">");
		}

		if (this._strikeThroughColor != null) {
			buffer.append("<span style=\"text-decoration:line-through;color:");
			buffer.append(_colorFormat.format(this._strikeThroughColor));
			buffer.append("\">");
		}

		if (this.hasBasicTextStyle()) {
			buffer.append("<span style=\"");
			if (this._foregroundColor != null) {
				buffer.append("color:");
				buffer.append(_colorFormat.format(this._foregroundColor));
				buffer.append(";");
			}

			if (this._backgroundColor != null) {
				buffer.append("background-color:");
				buffer.append(_colorFormat.format(this._backgroundColor));
				buffer.append(";");
			}

			if (this._bold) {
				buffer.append("font-weight:bold;");
			}

			if (this._italic) {
				buffer.append("font-style:italic;");
			}

			buffer.append("\">");
		}

		return new String(buffer);
	}

	private boolean hasBasicTextStyle() {
		return this._foregroundColor != null | this._backgroundColor != null | this._bold | this._italic;
	}

	public String endHtml() {
		StringBuffer buffer = new StringBuffer();
		if (this.hasBasicTextStyle()) {
			buffer.append("</span>");
		}

		if (this._strikeThroughColor != null) {
			buffer.append("</span>");
		}

		if (this._underlineColor != null) {
			buffer.append("</span>");
		}

		if (this._boxColor != null) {
			buffer.append("</span>");
		}

		return new String(buffer);
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o != null && this.getClass() == o.getClass()) {
			CodeStyle codeStyle = (CodeStyle)o;
			if (this._bold != codeStyle._bold) {
				return false;
			} else if (this._italic != codeStyle._italic) {
				return false;
			} else {
				label74: {
					if (this._backgroundColor != null) {
						if (this._backgroundColor.equals(codeStyle._backgroundColor)) {
							break label74;
						}
					} else if (codeStyle._backgroundColor == null) {
						break label74;
					}

					return false;
				}

				if (this._boxColor != null) {
					if (!this._boxColor.equals(codeStyle._boxColor)) {
						return false;
					}
				} else if (codeStyle._boxColor != null) {
					return false;
				}

				label60: {
					if (this._foregroundColor != null) {
						if (this._foregroundColor.equals(codeStyle._foregroundColor)) {
							break label60;
						}
					} else if (codeStyle._foregroundColor == null) {
						break label60;
					}

					return false;
				}

				if (this._strikeThroughColor != null) {
					if (!this._strikeThroughColor.equals(codeStyle._strikeThroughColor)) {
						return false;
					}
				} else if (codeStyle._strikeThroughColor != null) {
					return false;
				}

				if (this._underlineColor != null) {
					if (!this._underlineColor.equals(codeStyle._underlineColor)) {
						return false;
					}
				} else if (codeStyle._underlineColor != null) {
					return false;
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public int hashCode() {
		int result = this._foregroundColor != null ? this._foregroundColor.hashCode() : 0;
		result = 29 * result + (this._backgroundColor != null ? this._backgroundColor.hashCode() : 0);
		result = 29 * result + (this._bold ? 1 : 0);
		result = 29 * result + (this._italic ? 1 : 0);
		result = 29 * result + (this._underlineColor != null ? this._underlineColor.hashCode() : 0);
		result = 29 * result + (this._strikeThroughColor != null ? this._strikeThroughColor.hashCode() : 0);
		result = 29 * result + (this._boxColor != null ? this._boxColor.hashCode() : 0);
		return result;
	}
}
