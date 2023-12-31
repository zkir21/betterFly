package com.unitedthinkers.fly.utils.code_as_html;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/13
 */
public class PreWrapHTMLEditorKit extends HTMLEditorKit {

	ViewFactory viewFactory = new HTMLFactory() {

		@Override
		public View create(Element elem) {
			AttributeSet attrs = elem.getAttributes();
			Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
			Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
			if (o instanceof HTML.Tag) {
				HTML.Tag kind = (HTML.Tag) o;
				if (kind == HTML.Tag.IMPLIED) {
					return new javax.swing.text.html.ParagraphView(elem);
				}
			}
			return super.create(elem);
		}
	};

	@Override
	public ViewFactory getViewFactory() {
		return this.viewFactory;
	}

}
