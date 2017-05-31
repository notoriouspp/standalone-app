package com.heliosdecompiler.helios.gui.search.impl;

import org.fxmisc.richtext.StyledTextArea;

/**
 * com.heliosdecompiler.helios.gui.search.impl
 *
 * @author Notorious
 * @version 1.0.0
 * @since 5/31/2017
 */
public class PlainTextSearch extends AbstractSearch {

    @Override
    public void search(String search, StyledTextArea codeArea, boolean reverse) {
        if(search != null && !search.isEmpty()) {
            if(!search.equals(lastSearch)){
                lastSearch = null;
                lastIndex = 0;
            }
            String code = codeArea.getText();
            int index;
            if(reverse) {
                String substring = code.substring(0, lastIndex - (lastSearch != null ? lastSearch.length() : 0));
                index = substring.lastIndexOf(search);
            } else {
                index = code.indexOf(search, lastIndex);
            }
            if(index != -1){
                int position = index + search.length();
                codeArea.selectRange(index, position);
                codeArea.showParagraphAtTop(codeArea.getCurrentParagraph());
                lastIndex = position;
            }
            lastSearch = search;
        }
    }
}
