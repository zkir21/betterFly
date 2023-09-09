package com.unitedthinkers.fly.utils.code_as_html;

import com.intellij.lang.Language;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/13
 */
public class HtmlHelper {

	private static final Logger LOGGER = Logger.getInstance(HtmlHelper.class.getName());
	private static final Format _colorFormat = new ColorFormat();
	private int _lineNumberCharCount;
	private CodeStyle _lineNoCodeStyle;
	private boolean _isStartOfLine;
	private int _lineNo;
	private boolean _showLineNos;
	private boolean _unindent;
	private int _highlightStartIndex;
	private Integer _fontSize;
	private String _tabText;
	private static final Pattern LINE_PATTERN = Pattern.compile("\\n|\\r\\n|\\r");

	public String getCodeAsHtml(AnActionEvent event) {
		DataContext dataContext = event.getDataContext();
		Editor editor = (Editor)dataContext.getData(PlatformDataKeys.EDITOR.getName());

		if (editor != null) {
			TextRange textRange = Utils.getSelectedTextRange(editor);
			if (textRange.getEndOffset() == Integer.MAX_VALUE) {
				return null;
			}
			PsiFile psiFile = (PsiFile)dataContext.getData(LangDataKeys.PSI_FILE.getName());
			EditorColorsScheme colorsScheme = editor.getColorsScheme();
			CodeStyle defaultCodeStyle = this.getDefaultCodeStyle(editor);
			Document document = editor.getDocument();
			int startLineNo = document.getLineNumber(textRange.getStartOffset()) + 1;
			int endLineNo = document.getLineNumber(textRange.getEndOffset() - 1) + 1;
			int maxLineNo = endLineNo;
			this._lineNumberCharCount = (int)Math.ceil(Math.log((double)(maxLineNo + 1)) / Math.log(10.0));
			Color lineNosColor = colorsScheme.getColor(EditorColors.LINE_NUMBERS_COLOR);
			Color lineNosBackgroundColor = colorsScheme.getColor(EditorColors.GUTTER_BACKGROUND);
			this._lineNoCodeStyle = new CodeStyle(lineNosColor, lineNosBackgroundColor, false, false, (Color)null, (Color)null, (Color)null);
			this._lineNo = startLineNo;
			this._isStartOfLine = true;
			this._highlightStartIndex = 0;
			this._showLineNos =  editor.getSettings().isLineNumbersShown();
			this._unindent = true;
			this._fontSize = null;
			this._fontSize = colorsScheme.getEditorFontSize();

			this._tabText = null;
			this._tabText = Utils.repeat(' ', 4);

			Project project = (Project)dataContext.getData(PlatformDataKeys.PROJECT.getName());
			String html = this.copyAsHtml(project, defaultCodeStyle, colorsScheme, editor, psiFile, textRange);
			LOGGER.info(html);

			return html;
		}
		return null;
	}

	private CodeStyle getDefaultCodeStyle(Editor editor) {
		EditorColorsScheme colorsScheme = editor.getColorsScheme();
		Color backgroundColor = this.getBackgroundColor(editor);
		Color foregroundColor = this.getForegroundColor(editor);
		TextAttributes textAttributes = colorsScheme.getAttributes(HighlighterColors.TEXT);
		int fontType = textAttributes.getFontType();
		boolean isBold = (fontType & 1) != 0;
		boolean isItalic = (fontType & 2) != 0;
		EffectType effectType = textAttributes.getEffectType();
		Color underlineColor = EffectType.LINE_UNDERSCORE != effectType && EffectType.WAVE_UNDERSCORE != effectType ? null : textAttributes.getEffectColor();
		Color strikeThroughColor = EffectType.STRIKEOUT == effectType ? textAttributes.getEffectColor() : null;
		Color boxColor = EffectType.BOXED == effectType ? textAttributes.getEffectColor() : null;
		CodeStyle defaultCodeStyle = new CodeStyle(foregroundColor, backgroundColor, isBold, isItalic, underlineColor, strikeThroughColor, boxColor);
		return defaultCodeStyle;
	}

	private Color getForegroundColor(Editor editor) {
		EditorColorsScheme colorsScheme = editor.getColorsScheme();
		TextAttributes textAttributes = colorsScheme.getAttributes(HighlighterColors.TEXT);
		Color foregroundColor = textAttributes.getForegroundColor();
		return foregroundColor;
	}

	private Color getBackgroundColor(Editor editor) {
		Document document = editor.getDocument();
		EditorColorsScheme colorsScheme = editor.getColorsScheme();
		TextAttributes textAttributes = colorsScheme.getAttributes(HighlighterColors.TEXT);
		boolean isWritable = document.isWritable();
		Color backgroundColor = isWritable ? textAttributes.getBackgroundColor() : colorsScheme.getColor(EditorColors.READONLY_BACKGROUND_COLOR);
		if (backgroundColor == null) {
			backgroundColor = Color.WHITE;
		}

		return backgroundColor;
	}

	private static SyntaxHighlighter getSyntaxHighlighter(Language language, Project project, VirtualFile virtualFile) {
		SyntaxHighlighter syntaxHighlighter;
		try {
			Class shf = Class.forName("com.intellij.openapi.fileTypes.SyntaxHighlighterFactory", true, HtmlHelper.class.getClassLoader());
			syntaxHighlighter = (SyntaxHighlighter)reflect("Can't get SyntaxHighlighter", shf, "getSyntaxHighlighter", (Object)null, new Class[]{Language.class, Project.class, VirtualFile.class}, new Object[]{language, project, virtualFile});
		} catch (Exception e) {
			try {
				syntaxHighlighter = (SyntaxHighlighter)reflect("Can't get SyntaxHighlighter", Language.class, "getSyntaxHighlighter", language, new Class[]{Project.class, VirtualFile.class}, new Object[]{project, virtualFile});
			} catch (RuntimeException ex) {
				syntaxHighlighter = (SyntaxHighlighter)reflect("Can't get SyntaxHighlighter", Language.class, "getSyntaxHighlighter", language, new Class[]{Project.class}, new Object[]{project});
			}
		}

		return syntaxHighlighter;
	}

	private static Object reflect(String errorMessage, Class<Language> clazz, String methodName, Object object, Class[] parameterClasses, Object[] args) {
		try {
			Method method = clazz.getMethod(methodName, parameterClasses);
			Object result = method.invoke(object, args);
			return result;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(errorMessage, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(errorMessage, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(errorMessage, e);
		}
	}

	private String copyAsHtml(Project project, CodeStyle defaultCodeStyle, EditorColorsScheme colorsScheme, Editor editor, PsiFile psiFile, TextRange textRange) {
		Language language = psiFile.getLanguage();
		VirtualFile virtualFile = psiFile.getVirtualFile();
		SyntaxHighlighter syntaxHighlighter = getSyntaxHighlighter(language, project, virtualFile);
		Lexer lexer = syntaxHighlighter.getHighlightingLexer();
		String text = psiFile.getText();
		int startOffset = textRange.getStartOffset();
		int endOffset = textRange.getEndOffset();
		if (endOffset > 0 && text.charAt(endOffset - 1) == '\n') {
			--endOffset;
		}

		StringBuffer buffer = new StringBuffer();
		CodeStyle currentCodeStyle = null;
		int commonWhiteSpacePrefixCount = this._unindent ? this.getCommonWhiteSpacePrefixCount(text, startOffset, endOffset) : 0;
		List rangeHighlighters = this.getRangeHighlighters(editor);
		buffer.append("<pre style=\"line-height: 100%;font-family:monospace;background-color:");
		buffer.append(_colorFormat.format(defaultCodeStyle.getBackgroundColor()));
		buffer.append("; border-width:0.01mm; border-color:#000000; border-style:solid;");

		buffer.append("padding:").append(10).append("px;");

		if (this._fontSize != null) {
			buffer.append("font-size:").append(this._fontSize).append("pt;");
		}

		buffer.append("\">");
		lexer.start(text);

		IElementType tokenType;
		for(; (tokenType = lexer.getTokenType()) != null; lexer.advance()) {
			int tokenStart = lexer.getTokenStart();
			int tokenEnd = lexer.getTokenEnd();
			if (tokenStart < endOffset && tokenEnd >= startOffset) {
				tokenStart = Math.max(tokenStart, startOffset);
				tokenEnd = Math.min(tokenEnd, endOffset);
				StringBuffer tokenText = new StringBuffer();
				tokenText.append(text, tokenStart, tokenEnd);
				Utils.quoteForXml(tokenText);
				CodeStyle codeStyle = this.getCodeStyle(defaultCodeStyle, syntaxHighlighter, colorsScheme, tokenType, tokenStart, rangeHighlighters);
				currentCodeStyle = this.appendToBuffer(buffer, currentCodeStyle, codeStyle, tokenText, commonWhiteSpacePrefixCount);
			}
		}

		if (currentCodeStyle != null) {
			buffer.append(currentCodeStyle.endHtml());
		}

		buffer.append("</pre>");
		return new String(buffer);
	}

	private int getCommonWhiteSpacePrefixCount(String chars, int startOffset, int endOffset) {
		String text = chars.substring(startOffset, endOffset);
		String[] lines = LINE_PATTERN.split(text, -1);
		String whiteSpace = null;

		for(int i = 0; i < lines.length; ++i) {
			String line = lines[i];
			if (!isWhiteSpace(line)) {
				String testWhiteSpace = this.getWhiteSpacePrefix(line);
				if (testWhiteSpace.length() == 0) {
					return 0;
				}

				if (whiteSpace == null) {
					whiteSpace = testWhiteSpace;
				} else if (testWhiteSpace.length() < whiteSpace.length()) {
					if (!whiteSpace.startsWith(testWhiteSpace)) {
						return 0;
					}

					whiteSpace = testWhiteSpace;
				} else if (!testWhiteSpace.startsWith(whiteSpace)) {
					return 0;
				}
			}
		}

		return whiteSpace == null ? 0 : whiteSpace.length();
	}

	private static boolean isWhiteSpace(String text) {
		int i = 0;

		for(int length = text.length(); i < length; ++i) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	private String getWhiteSpacePrefix(String line) {
		int charIndex = 0;

		for(int length = line.length(); length > charIndex && Character.isWhitespace(line.charAt(charIndex)); ++charIndex) {
		}

		return line.substring(0, charIndex);
	}

	private List getRangeHighlighters(Editor editor) {
		MarkupModel documentMarkupModel = editor.getMarkupModel();
		RangeHighlighter[] highlighters = documentMarkupModel.getAllHighlighters();
		List<RangeHighlight> rangeHighlighters = new ArrayList(highlighters.length);
//		Configuration configuration = Configuration.getInstance();
		boolean includeWarningHighlights = true;//configuration.getIncludeWarningHighlights();

		for(int i = 0; i < highlighters.length; ++i) {
			RangeHighlighter highlighter = highlighters[i];
			int layer = highlighter.getLayer();
			if (layer == 2000 || layer == 3000 || includeWarningHighlights && (layer == 4000 || layer == 5000)) {
				int highlighterStartOffset = highlighter.getStartOffset();
				int highlighterEndOffset = highlighter.getEndOffset();
				TextAttributes textAttributes = highlighter.getTextAttributes();
				if (textAttributes != null) {
					RangeHighlight rangeHighlight = new RangeHighlight(highlighterStartOffset, highlighterEndOffset, textAttributes);
					rangeHighlighters.add(rangeHighlight);
				}
			}
		}

		Collections.sort(rangeHighlighters);
		return rangeHighlighters;
	}

	private CodeStyle appendToBuffer(StringBuffer buffer, CodeStyle oldCodeStyle, CodeStyle newCodeStyle, StringBuffer text, int whiteSpacePrefixCount) {
		String[] lines = LINE_PATTERN.split(text, -1);
		int lineCount = lines.length;

		for(int i = 0; i < lineCount; ++i) {
			String line = lines[i];
			if (this._isStartOfLine) {
				if (this._showLineNos) {
					++this._lineNo;
					oldCodeStyle = this.appendChunkToBuffer(buffer, oldCodeStyle, this._lineNoCodeStyle, Utils.formatInt(this._lineNo, this._lineNumberCharCount) + " ");
				}

				if (line.length() >= whiteSpacePrefixCount) {
					line = line.substring(whiteSpacePrefixCount);
				}

				this._isStartOfLine = false;
			}

			if (this._tabText != null) {
				line = Utils.replaceAll(line, '\t', this._tabText);
			}

			if (line.length() > 0) {
				oldCodeStyle = this.appendChunkToBuffer(buffer, oldCodeStyle, newCodeStyle, line);
			}

			if (i != lineCount - 1) {
				CodeStyle newLineCodeStyle = oldCodeStyle == null ? null : new CodeStyle(oldCodeStyle.getForegroundColor(), oldCodeStyle.getBackgroundColor(), oldCodeStyle.isBold(), oldCodeStyle.isItalic(), oldCodeStyle.getUnderlineColor(), oldCodeStyle.getStrikeThroughColor(), (Color)null);
				oldCodeStyle = this.appendChunkToBuffer(buffer, oldCodeStyle, newLineCodeStyle, "<br>");
				this._isStartOfLine = true;
			}
		}

		return oldCodeStyle;
	}

	private CodeStyle appendChunkToBuffer(StringBuffer buffer, CodeStyle oldCodeStyle, CodeStyle newCodeStyle, String line) {
		if (!Utils.equals(oldCodeStyle, newCodeStyle)) {
			String startHtml;
			if (oldCodeStyle != null) {
				startHtml = oldCodeStyle.endHtml();
				buffer.append(startHtml);
			}

			if (newCodeStyle != null) {
				startHtml = newCodeStyle.startHtml();
				buffer.append(startHtml);
			}
		}
		buffer.append(line);
		return newCodeStyle;
	}

	private CodeStyle getCodeStyle(CodeStyle defaultCodeStyle, SyntaxHighlighter syntaxHighlighter, EditorColorsScheme colorsScheme, IElementType tokenType, int tokenStart, List rangeHighlighters) {
		TextAttributesKey[] syntaxTextAttributeKeys = syntaxHighlighter.getTokenHighlights(tokenType);
		Color foregroundColor = defaultCodeStyle.getForegroundColor();
		Color backgroundColor = defaultCodeStyle.getBackgroundColor();
		boolean isBold = defaultCodeStyle.isBold();
		boolean isItalic = defaultCodeStyle.isItalic();
		Color underlineColor = defaultCodeStyle.getUnderlineColor();
		Color strikeThroughColor = defaultCodeStyle.getStrikeThroughColor();
		Color boxColor = defaultCodeStyle.getBoxColor();
		TextAttributes[] highlightTextAttributes = this.getTextAttributes(rangeHighlighters, tokenStart);
		ArrayList<TextAttributes> textAttributes = new ArrayList(syntaxTextAttributeKeys.length + highlightTextAttributes.length);

		int i;
		for(i = 0; i < syntaxTextAttributeKeys.length; ++i) {
			TextAttributesKey tokenHighlight = syntaxTextAttributeKeys[i];
			TextAttributes attributes = colorsScheme.getAttributes(tokenHighlight);
			if (attributes != null) {
				textAttributes.add(attributes);
			}
		}

		textAttributes.addAll(Arrays.asList(highlightTextAttributes));

		for(i = 0; i < textAttributes.size(); ++i) {
			TextAttributes attributes = (TextAttributes)textAttributes.get(i);
			Color highlightForegroundColor = attributes.getForegroundColor();
			if (highlightForegroundColor != null) {
				foregroundColor = highlightForegroundColor;
			}

			Color highlightBackgroundColor = attributes.getBackgroundColor();
			if (highlightBackgroundColor != null) {
				backgroundColor = highlightBackgroundColor;
			}

			EffectType effectType = attributes.getEffectType();
			Color attributeUnderlineColor = EffectType.LINE_UNDERSCORE != effectType && EffectType.WAVE_UNDERSCORE != effectType ? null : attributes.getEffectColor();
			if (attributeUnderlineColor != null) {
				underlineColor = attributeUnderlineColor;
			}

			Color attributeStrikeThroughColor = EffectType.STRIKEOUT == effectType ? attributes.getEffectColor() : null;
			if (attributeStrikeThroughColor != null) {
				strikeThroughColor = attributeStrikeThroughColor;
			}

			Color attributeBoxColor = EffectType.BOXED == effectType ? attributes.getEffectColor() : null;
			if (attributeBoxColor != null) {
				boxColor = attributeBoxColor;
			}

			int fontType = attributes.getFontType();
			isBold |= (fontType & 1) != 0;
			isItalic |= (fontType & 2) != 0;
		}

		return new CodeStyle(foregroundColor, backgroundColor, isBold, isItalic, underlineColor, strikeThroughColor, boxColor);
	}

	private TextAttributes[] getTextAttributes(List rangeHighlighters, int offset) {
		List<Object> textAttributes = new ArrayList();

		for(int i = this._highlightStartIndex; i < rangeHighlighters.size(); ++i) {
			RangeHighlight rangeHighlight = (RangeHighlight)rangeHighlighters.get(i);
			int startOffset = rangeHighlight.getStartOffset();
			int endOffset = rangeHighlight.getEndOffset();
			if (endOffset < offset) {
				++this._highlightStartIndex;
			}

			if (startOffset <= offset && offset < endOffset) {
				textAttributes.add(rangeHighlight.getTextAttributes());
			}

			if (startOffset > offset) {
				break;
			}
		}

		return (TextAttributes[])((TextAttributes[])textAttributes.toArray(new TextAttributes[textAttributes.size()]));
	}
}
