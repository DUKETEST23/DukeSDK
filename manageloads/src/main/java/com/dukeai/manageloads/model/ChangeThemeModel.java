package com.dukeai.manageloads.model;


import com.dukeai.manageloads.R;
import com.dukeai.manageloads.utils.AppConstants;

public class ChangeThemeModel {
    /**
     * To change the Background color of the screens
     */
    String backgroundColor;
    /**
     * To change the font color of the screens
     */
    String fontColor;
    /**
     * To get the current theme of the app
     */
    String theme;
    /**
     * To change the title color of the screens
     */
    String titlecolor;
    /**
     * To change the floating button colors of the screens
     */
    String floatingButtonColor;
    String floatingButtonBackgroundColor;
    /**
     * To change the custom tab button colors of the screens
     */
    String highlightedTabTextColor;
    String tabTextColor;
    /**
     * To change the next button color of the balance sheet list screens
     */
    String nextForwardButtonColor;
    /**
     * To change the icon colors of the profile buttons drawable icons
     */
    String inputFieldIconColor;
    /**
     * To change the custom header colors of the screens which doesnot include profile
     */
    String headerTitleColor;
    String headerBackgroundColor;
    String headerBackButtonColor;
    /**
     * To change the pop up heading colors of the screen
     */
    String popupHeadingTextColor;
    /**
     * To change the icon colors of the input field drawable icons
     */
    int pswdIconcolor;
    /**
     * To change the custom header colors of the screens which includes profile
     */
    int profileHeaderTextColor;
    int profileHeaderBackButtonColor;
    int profileHeaderBackgroundColor;
    /**
     * To change the logo of the screen
     */
    int logo;


    public ChangeThemeModel() {
        /**
         *  To get the current theme of the app
         */
        theme = AppConstants.currentTheme;

        switch (theme) {
            case "truck":
                /**
                 *  To change the Background color of the screens
                 */
                backgroundColor = "#108140";
                /**
                 *  To change the font color of the screens
                 */
                fontColor = "#108140";
                /**
                 *  To change the title color of the screens
                 */
                titlecolor = "#ffffff";
                /**
                 *  To change the custom header colors of the screens which doesnot include profile
                 */
                headerTitleColor = "#2c3247";
                headerBackButtonColor = "#2c3247";
                headerBackgroundColor = "#00FFFFFF";
                /**
                 *  To change the custom header colors of the screens which includes profile
                 */
                profileHeaderTextColor = R.color.colorWhite;
                profileHeaderBackButtonColor = R.color.colorWhite;
                profileHeaderBackgroundColor = R.color.colorBlack;
                /**
                 *  To change the floating button colors of the screens
                 */
                floatingButtonColor = "#ffffff";
                floatingButtonBackgroundColor = "#2c3247";
                /**
                 *  To change the custom tab button colors of the screens
                 */
                highlightedTabTextColor = "#ffffff"; //Highlighted tab text color
                tabTextColor = "#ffffff"; //Unselected Tab text color

                /**
                 *  To change the next button color of the balance sheet list screens
                 */
                nextForwardButtonColor = "#000000";

                /**
                 *  To change the icon colors of the profile buttons drawable icons
                 */
                inputFieldIconColor = "#404866";
                /**
                 *  To change the pop up heading colors of the screen
                 */
                popupHeadingTextColor = "#3c3f4e";
                /**
                 *  To change the icon colors of the input field drawable icons
                 */
                pswdIconcolor = R.color.colorIcon;
                /**
                 *  To change the logo of the screen
                 */
                logo = R.drawable.truck;

                break;

            case "duke":
                backgroundColor = "#E7D543";
                fontColor = "#5189C7";
                titlecolor = "#2c3247";
                floatingButtonColor = "#ffffff";
                highlightedTabTextColor = "#2c3247";
                floatingButtonBackgroundColor = "#2c3247";
                tabTextColor = "#6e6328";
                nextForwardButtonColor = "#000000";
                headerTitleColor = "#2c3247";
                headerBackButtonColor = "#2c3247";
                headerBackgroundColor = "#00FFFFFF";
                inputFieldIconColor = "#404866";
                popupHeadingTextColor = "#3c3f4e";
                pswdIconcolor = R.color.colorIcon;
                profileHeaderTextColor = R.color.colorWhite;
                profileHeaderBackButtonColor = R.color.colorWhite;
                profileHeaderBackgroundColor = R.color.colorBlack;
                logo = R.drawable.logo;
                break;
        }
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getProfileHeaderTextColor() {
        return profileHeaderTextColor;
    }

    public void setProfileHeaderTextColor(int profileHeaderTextColor) {
        this.profileHeaderTextColor = profileHeaderTextColor;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public int getProfileHeaderBackButtonColor() {
        return profileHeaderBackButtonColor;
    }

    public void setProfileHeaderBackButtonColor(int profileHeaderBackButtonColor) {
        this.profileHeaderBackButtonColor = profileHeaderBackButtonColor;
    }

    public int getProfileHeaderBackgroundColor() {
        return profileHeaderBackgroundColor;
    }

    public void setProfileHeaderBackgroundColor(int profileHeaderBackgroundColor) {
        this.profileHeaderBackgroundColor = profileHeaderBackgroundColor;
    }

    public String getNextForwardButtonColor() {
        return nextForwardButtonColor;
    }

    public void setNextForwardButtonColor(String nextForwardButtonColor) {
        this.nextForwardButtonColor = nextForwardButtonColor;
    }

    public String getInputFieldIconColor() {
        return inputFieldIconColor;
    }

    public void setInputFieldIconColor(String inputFieldIconColor) {
        this.inputFieldIconColor = inputFieldIconColor;
    }

    public String getFontColor() {
        return this.fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getHighlightedTabTextColor() {
        return highlightedTabTextColor;
    }

    public void setHighlightedTabTextColor(String highlightedTabTextColor) {
        this.highlightedTabTextColor = highlightedTabTextColor;
    }

    public String getTabTextColor() {
        return tabTextColor;
    }

    public void setTabTextColor(String tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public String getPopupHeadingTextColor() {
        return popupHeadingTextColor;
    }

    public void setPopupHeadingTextColor(String popupHeadingTextColor) {
        this.popupHeadingTextColor = popupHeadingTextColor;
    }

    public int getPswdIconcolor() {
        return pswdIconcolor;
    }

    public void setPswdIconcolor(int pcolor) {
        this.pswdIconcolor = pcolor;
    }


    public String getHeaderTitleColor() {
        return headerTitleColor;
    }

    public void setHeaderTitleColor(String headerTitleColor) {
        this.headerTitleColor = headerTitleColor;
    }

    public String getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(String headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public String getHeaderBackButtonColor() {
        return headerBackButtonColor;
    }

    public void setHeaderBackButtonColor(String headerBackButtonColor) {
        this.headerBackButtonColor = headerBackButtonColor;
    }

    public String getFloatingButtonColor() {
        return floatingButtonColor;
    }

    public void setFloatingButtonColor(String floatingButtonColor) {
        this.floatingButtonColor = floatingButtonColor;
    }

    public String getFloatingButtonBackgroundColor() {
        return floatingButtonBackgroundColor;
    }

    public void setFloatingButtonBackgroundColor(String floatingButtonBackgroundColor) {
        this.floatingButtonBackgroundColor = floatingButtonBackgroundColor;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitlecolor() {
        return titlecolor;
    }

    public void setTitlecolor(String titlecolor) {
        this.titlecolor = titlecolor;
    }
}
