package com.jonathancromie.brisbaneevents;

/**
 * Created by jonathancromie on 24/02/2016.
 */
public class NavItem {
    private String title;
    private int icon;

    public NavItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
