/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.navigation;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.intellij.remoterobot.utils.Keyboard;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils.listOfRemoteTextToString;

/**
 * Search everywhere popup fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "SearchEverywhereUI type", xpath = "//div[@accessiblename='Search everywhere' and @class='SearchEverywhereUI']")
@FixtureName(name = "Search Everywhere Popup")
public class SearchEverywherePopup extends CommonContainerFixture {
    private RemoteRobot remoteRobot;

    public SearchEverywherePopup(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Activate given tab
     *
     * @param tabName name of the tab in the Search Everywhere popup
     */
    public void activateTab(String tabName) {
        ComponentFixture tab = find(ComponentFixture.class, byXpath("//div[@accessiblename='" + tabName + "' and @class='SETab' and @text='" + tabName + "']"));
        tab.click();
    }

    /**
     * Invoke a command using the search field
     *
     * @param cmdToEnter command that will be invoked using the search field
     */
    public void invokeCmd(String cmdToEnter) {
        JTextFieldFixture searchField = find(JTextFieldFixture.class, JTextFieldFixture.Companion.byType(), Duration.ofSeconds(10));
        searchField.click();
        searchField.setText(cmdToEnter);
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(1), "The search in the Search Everywhere popup did not finish in 30 seconds.", () -> didSearchFinish(cmdToEnter));
        new Keyboard(remoteRobot).hotKey(KeyEvent.VK_ENTER);
    }

    private boolean didSearchFinish(String cmdToInvoke) {
        String searchResultsString = listOfRemoteTextToString(getSearchResults());
        return searchResultsString.toLowerCase().contains(cmdToInvoke.toLowerCase());
    }

    private List<RemoteText> getSearchResults() {
        ComponentFixture searchResultsList = find(ComponentFixture.class, byXpath("//div[@class='JBList']"));
        return searchResultsList.findAllText();
    }
}