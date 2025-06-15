import java.awt.*;

public class CellSettings {
    int width;
    int height;
    Color backgroundColor;
    int strokeThickness;
    Color borderColor;
    int borderThickness;

    private CellSettings(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.backgroundColor = builder.backgroundColor;
        this.strokeThickness = builder.strokeThickness;
        this.borderColor = builder.borderColor;
        this.borderThickness = builder.borderThickness;
    }

    public static class Builder {
        int width = 150;
        int height = 150;
        Color backgroundColor = Color.white;
        int strokeThickness = 5;
        Color borderColor = Color.black;
        int borderThickness = 2;

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder backgroundColor(Color bgColor) {
            this.backgroundColor = bgColor;
            return this;
        }

        public Builder strokeThickness(int thickness) {
            this.strokeThickness = thickness;
            return this;
        }

        public Builder borderColor(Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder borderThickness(int thickness) {
            this.borderThickness = thickness;
            return this;
        }

        public CellSettings build() {
            return new CellSettings(this);
        }

    }
}
