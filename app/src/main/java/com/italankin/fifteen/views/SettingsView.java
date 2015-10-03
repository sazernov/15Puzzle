package com.italankin.fifteen.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.italankin.fifteen.Colors;
import com.italankin.fifteen.Dimensions;
import com.italankin.fifteen.R;
import com.italankin.fifteen.Settings;

/**
 * Класс объединяет элементы интерфейса настроек и управляет их отрисовкой и поведением
 */
public class SettingsView extends BaseView {

    private Paint mPaintText;                       // заголовок элемента настроек
    private Paint mPaintValue;                      // значение элемента настроек
    private Paint mPaintControls;                   // кнопки управления (назад)
    private Paint mPaintIcon;                       // для графического представления (например, цвет плиток)

    private String mTextWidth;                      // ширина поля
    private String mTextWidthValue;
    private String mTextHeight;                     // высота поля
    private String mTextHeightValue;
    private String mTextBf;                         // hardmode
    private String mTextBfValue[];
    private String mTextAnimations;                 // анимации
    private String mTextAnimationsValue[];
    private String mTextColor;                      // цвет плиток
    private String mTextColorMode;                  // цвет фона
    private String mTextColorModeValue[];           // цветовая тема
    private String mTextMode;                       // режим игры
    private String mTextModeValue[];
    private String mTextBack;                       // кнопка "назад"

    private RectF mRectWidth;                       // граница элемента настройки ширины
    private RectF mRectHeight;                      // ... высоты
    private RectF mRectBf;                          // ... "слепого" режима
    private RectF mRectColor;                       // ... цвета
    private RectF mRectColorMode;                   // ... цвета фона
    private RectF mRectColorIcon;                   // ... визуальное представление цвета
    private RectF mRectAnimations;                  // ... анимации
    private RectF mRectMode;                        // ... режим игры
    private RectF mRectBack;                        // ... "назад"

    private Callbacks mCallbacks;

    public SettingsView(Resources res) {
        int h = (int) (Dimensions.surfaceHeight * 0.082f); // промежуток между строками
        int ch = (int) (Dimensions.surfaceHeight * 0.15f); // отступ от верхнего края экрана
        int sp = -h / 4;

        mPaintText = new Paint();
        mPaintText.setAntiAlias(Settings.antiAlias);
        mPaintText.setColor(Colors.getOverlayTextColor());
        mPaintText.setTextSize(Dimensions.menuFontSize);
        mPaintText.setTypeface(Settings.typeface);
        mPaintText.setTextAlign(Paint.Align.RIGHT);

        mPaintValue = new Paint();
        mPaintValue.setAntiAlias(Settings.antiAlias);
        mPaintValue.setColor(Colors.menuTextValue);
        mPaintValue.setTextSize(Dimensions.menuFontSize);
        mPaintValue.setTypeface(Settings.typeface);
        mPaintValue.setTextAlign(Paint.Align.LEFT);

        mPaintControls = new Paint(mPaintText);
        mPaintControls.setTextAlign(Paint.Align.CENTER);

        mPaintIcon = new Paint();
        mPaintIcon.setAntiAlias(Settings.antiAlias);

        mTextHeight = res.getString(R.string.pref_height);
        mTextHeightValue = Integer.toString(Settings.gameHeight);
        mTextWidth = res.getString(R.string.pref_width);
        mTextWidthValue = Integer.toString(Settings.gameWidth);
        mTextMode = res.getString(R.string.pref_mode);
        mTextModeValue = res.getStringArray(R.array.game_modes);
        mTextBf = res.getString(R.string.pref_bf);
        mTextBfValue = res.getStringArray(R.array.difficulty_modes);
        mTextAnimations = res.getString(R.string.pref_animation);
        mTextAnimationsValue = res.getStringArray(R.array.toggle);
        mTextColorMode = res.getString(R.string.pref_color_mode);
        mTextColorModeValue = res.getStringArray(R.array.color_mode);
        mTextColor = res.getString(R.string.pref_color);
        mTextBack = res.getString(R.string.back);

        Rect r = new Rect();
        mPaintText.getTextBounds(mTextWidth, 0, mTextWidth.length(), r);

        ch += h;
        mRectMode = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectMode.inset(0, sp);

        ch += h;
        mRectBf = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectBf.inset(0, sp);

        ch += h;
        mRectWidth = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectWidth.inset(0, sp);

        ch += h;
        mRectHeight = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectHeight.inset(0, sp);

        ch += h;
        mRectAnimations = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectAnimations.inset(0, sp);

        ch += h;
        mRectColorMode = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectColorMode.inset(0, sp);

        ch += h;
        mRectColor = new RectF(0, ch, Dimensions.surfaceWidth, ch + r.height());
        mRectColor.inset(0, sp);
        mRectColorIcon = new RectF(Dimensions.surfaceWidth / 2 + 2.0f * Dimensions.spacing,
                mRectColor.top - sp,
                Dimensions.surfaceWidth / 2 + 2.0f * Dimensions.spacing + r.height(),
                mRectColor.bottom + sp);
        mRectColorIcon.inset(-mRectColorIcon.width() / 4, -mRectColorIcon.width() / 4);


        mRectBack = new RectF(0, Dimensions.surfaceHeight - h,
                Dimensions.surfaceWidth, Dimensions.surfaceHeight - h + r.height());
        mRectBack.inset(0, sp);
    }

    /**
     * Обработка событий нажатия
     *
     * @param x  координата x нажатия
     * @param y  координата y нажатия
     * @param dx направление жеста
     */
    public void onClick(int x, int y, int dx) {

        if (Math.abs(dx) < 15) {
            dx = 0;
        }

        // -- ширина поля --
        if (mRectWidth.contains(x, y)) {
            Settings.gameWidth += ((dx == 0) ? 1 : Math.signum(dx));
            if (Settings.gameWidth < Settings.MIN_GAME_WIDTH) {
                Settings.gameWidth = Settings.MAX_GAME_WIDTH;
            }
            if (Settings.gameWidth > Settings.MAX_GAME_WIDTH) {
                Settings.gameWidth = Settings.MIN_GAME_WIDTH;
            }
            Settings.save();
            if (mCallbacks != null) {
                mCallbacks.onChanged(true);
            }
        }

        // -- высота поля --
        if (mRectHeight.contains(x, y)) {
            Settings.gameHeight += ((dx == 0) ? 1 : Math.signum(dx));
            if (Settings.gameHeight < Settings.MIN_GAME_HEIGHT) {
                Settings.gameHeight = Settings.MAX_GAME_HEIGHT;
            }
            if (Settings.gameHeight > Settings.MAX_GAME_HEIGHT) {
                Settings.gameHeight = Settings.MIN_GAME_HEIGHT;
            }
            Settings.save();
            if (mCallbacks != null) {
                mCallbacks.onChanged(true);
            }
        }

        // -- переключение анимаций --
        if (mRectAnimations.contains(x, y)) {
            Settings.animations = !Settings.animations;
            Settings.save();
        }

        // -- цвет спрайтов --
        if (mRectColor.contains(x, y)) {
            if (dx < 0) {
                if (--Settings.tileColor < 0) {
                    Settings.tileColor += Colors.tiles.length;
                }
            } else {
                Settings.tileColor = (++Settings.tileColor % Colors.tiles.length);
            }
            Settings.save();
            if (mCallbacks != null) {
                mCallbacks.onChanged(false);
            }
        }

        // -- цвет фона --
        if (mRectColorMode.contains(x, y)) {
            Settings.colorMode = (++Settings.colorMode % Settings.COLOR_MODES);
            Settings.save();
            if (mCallbacks != null) {
                mCallbacks.onChanged(false);
            }
        }

        // -- режим игры --
        if (mRectMode.contains(x, y)) {
            Settings.gameMode = (++Settings.gameMode % Settings.GAME_MODES);
            Settings.save();
            if (mCallbacks != null) {
                mCallbacks.onChanged(true);
            }
        }

        // -- режим игры --
        if (mRectBf.contains(x, y)) {
            Settings.hardmode = !Settings.hardmode;
            Settings.save();
            if (mCallbacks != null) {
                mCallbacks.onChanged(true);
            }
        }

        // -- назад --
        if (mRectBack.contains(x, y)) {
            hide();
        }
    }

    public void draw(Canvas canvas) {
        if(!mShow) {
            return;
        }

        // отступ от центра
        float right = Dimensions.surfaceWidth / 2 + Dimensions.spacing;
        // для выравнивания элементов
        float left = Dimensions.surfaceWidth / 2 - Dimensions.spacing;
        // смещение по вертикали
        float s = (int) (Dimensions.surfaceHeight * 0.02f);

        // фон
        canvas.drawColor(Colors.getOverlayColor());

        // чтение настроек игры
        mTextWidthValue = Integer.toString(Settings.gameWidth);
        mTextHeightValue = Integer.toString(Settings.gameHeight);

        // ширина поля
        canvas.drawText(mTextWidth, left, mRectWidth.bottom - s, mPaintText);
        canvas.drawText(mTextWidthValue, right, mRectWidth.bottom - s, mPaintValue);

        // высота поля
        canvas.drawText(mTextHeight, left, mRectHeight.bottom - s, mPaintText);
        canvas.drawText(mTextHeightValue, right, mRectHeight.bottom - s, mPaintValue);

        // анимации
        canvas.drawText(mTextAnimations, left, mRectAnimations.bottom - s, mPaintText);
        canvas.drawText(mTextAnimationsValue[Settings.animations ? 1 : 0],
                right, mRectAnimations.bottom - s, mPaintValue);

        // цвет
        canvas.drawText(mTextColor, left, mRectColor.bottom - s, mPaintText);
        mPaintIcon.setColor(Colors.getTileColor());
        canvas.drawRect(mRectColorIcon, mPaintIcon);

        // цвет фона
        canvas.drawText(mTextColorMode, left, mRectColorMode.bottom - s, mPaintText);
        canvas.drawText(mTextColorModeValue[Settings.colorMode],
                right, mRectColorMode.bottom - s, mPaintValue);

        // режим
        canvas.drawText(mTextMode, left, mRectMode.bottom - s, mPaintText);
        canvas.drawText(mTextModeValue[Settings.gameMode],
                right, mRectMode.bottom - s, mPaintValue);

        // bf
        canvas.drawText(mTextBf, left, mRectBf.bottom - s, mPaintText);
        canvas.drawText(mTextBfValue[Settings.hardmode ? 1 : 0],
                right, mRectBf.bottom - s, mPaintValue);

        // кнопка "назад"
        canvas.drawText(mTextBack, Dimensions.surfaceWidth / 2,
                mRectBack.bottom - s, mPaintControls);
    }

    public void update() {
        mPaintText.setColor(Colors.getOverlayTextColor());
        mPaintControls.setColor(Colors.getOverlayTextColor());
        mPaintValue.setColor(Colors.menuTextValue);

        mPaintText.setAntiAlias(Settings.antiAlias);
        mPaintControls.setAntiAlias(Settings.antiAlias);
        mPaintValue.setAntiAlias(Settings.antiAlias);
        mPaintIcon.setAntiAlias(Settings.antiAlias);
    }

    public void addCallback(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public interface Callbacks {
        void onChanged(boolean needUpdate);
    }

}