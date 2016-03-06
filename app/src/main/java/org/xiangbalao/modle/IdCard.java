package org.xiangbalao.modle;

/**
 * Created by longtaoge on 2016/3/4.
 */
public class IdCard {

    private String fg;
    private String bg;

    public String getFg() {
        return fg;
    }

    public void setFg(String fg) {
        this.fg = fg;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "bg='" + bg + '\'' +
                ", fg='" + fg + '\'' +
                '}';
    }
}
