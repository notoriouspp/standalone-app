/*
 * Copyright 2017 Sam Sun <github-contact@samczsun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heliosdecompiler.helios.gui.view.editors;

import com.github.haixing_hu.javafx.control.textfield.SearchBox;
import com.heliosdecompiler.helios.controller.files.OpenedFile;
import com.heliosdecompiler.helios.gui.search.impl.PlainTextSearch;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.util.concurrent.CompletableFuture;

public abstract class EditorView {

    public static final PlainTextSearch PLAIN_TEXT_SEARCH = new PlainTextSearch();

    public final Node createView(OpenedFile file, String path) {
        Node node = createView0(file, path);
        node.getProperties().put("editor", this);
        node.getProperties().put("file", file); // probably a memory leak
        node.getProperties().put("path", path);
        return node;
    }

    protected abstract Node createView0(OpenedFile file, String path);

    public boolean canSave() {
        return true;
    }

    public CompletableFuture<byte[]> save(Node node) {
        return CompletableFuture.completedFuture(new byte[0]);
    }

    public abstract String getDisplayName();

    VBox createCodeAreaWithSearch(CodeArea codeArea) {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(20));
        SearchBox searchBox = new SearchBox();
        VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
        searchBox.setOnInputMethodTextChanged(event -> {
            String text = searchBox.getText();
            if(text == null || text.isEmpty()) {
                PLAIN_TEXT_SEARCH.search(searchBox.getText(), codeArea, false);
            }
        });
        searchBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.ENTER) {
                PLAIN_TEXT_SEARCH.search(searchBox.getText(), codeArea, event.isShiftDown());
            }
        });
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        box.getChildren().add(searchBox);
        box.getChildren().add(scrollPane);
        return box;
    }
}
