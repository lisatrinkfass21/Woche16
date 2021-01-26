package at.htlgkr.steamgameapp;

import at.htlgkr.steam.ReportType;

public class ReportTypeSpinnerItem {
    ReportType type;
    String text;

    public ReportTypeSpinnerItem(ReportType type, String displayText) {
        this.type = type;
        this.text = displayText;
    }

    public ReportType getType() {
        return this.type;
    }

    public String getDisplayText() {
        return this.text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}