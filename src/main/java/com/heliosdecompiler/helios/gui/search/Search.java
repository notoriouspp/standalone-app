package com.heliosdecompiler.helios.gui.search;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyledTextArea;

/**
 * com.heliosdecompiler.helios.gui.search
 *
 * @author Notorious
 * @version 1.0.0
 * @since 5/31/2017
 */
public interface Search {

    void search(String search, StyledTextArea codeArea, boolean reverse);

}
