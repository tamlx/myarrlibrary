package b.laixuantam.myaarlibrary.widgets.showmoretextview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/*
        <ShowMoreTextView
        android:id="@+id/text_view_show_more"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

        ShowMoreTextView textView = findViewById(R.id.text_view_show_more);

        textView.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

        textView.setShowingLine(2);
        //textView.setShowingChar(30);

        textView.addShowMoreText("Continue");
        textView.addShowLessText("Less");

        textView.setShowMoreColor(Color.RED); // or other color
        textView.setShowLessTextColor(Color.RED); // or other color

 */


public class ShowMoreTextView extends TextView {

    private static final String TAG = ShowMoreTextView.class.getName();

    private int showingLine = 1;
    private int showingChar;
    private boolean isCharEnable;

    private String showMore = "Show more";
    private String showLess = "Show less";
    private String dotdot = "...";

    private int MAGIC_NUMBER = 5;

    private int showMoreTextColor = Color.RED;
    private int showLessTextColor = Color.RED;

    private String mainText;

    private boolean isAlreadySet;

    public ShowMoreTextView(Context context) {
        super(context);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mainText = getText().toString();
    }


    private void addShowMore() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                String text = getText().toString();
                if (!isAlreadySet) {
                    mainText = getText().toString();
                    isAlreadySet = true;
                }
                String showingText = "";
                if (isCharEnable) {
                    if (showingChar >= text.length()) {
                        try {
                            throw new Exception("Character count cannot be exceed total line count");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String newText = text.substring(0, showingChar);
                    newText += dotdot + showMore;
                    setText(newText);
                    Log.d(TAG, "Text: " + newText);
                } else {

                    if (showingLine >= getLineCount()) {
                        try {
                            throw new Exception("Line Number cannot be exceed total line count");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error: " + e.getMessage());
                        }
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        return;
                    }
                    int start = 0;
                    int end;
                    for (int i = 0; i < showingLine; i++) {
                        end = getLayout().getLineEnd(i);
                        showingText += text.substring(start, end);
                        start = end;
                    }

                    String newText = showingText.substring(0, showingText.length() - (dotdot.length() + showMore.length() + MAGIC_NUMBER));
                    Log.d(TAG, "Text: " + newText);
                    Log.d(TAG, "Text: " + showingText);
                    newText += dotdot + showMore;

                    setText(newText);
                }

                setShowMoreColoringAndClickable();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });


    }

    private void setShowMoreColoringAndClickable() {
        final SpannableString spannableString = new SpannableString(getText());

        Log.d(TAG, "Text: " + getText());
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                    }

                                    @Override
                                    public void onClick(@Nullable View view) {
                                        setMaxLines(Integer.MAX_VALUE);
                                        setText(mainText);
                                        showLessButton();
                                        Log.d(TAG, "Item clicked: " + mainText);

                                    }
                                },
                getText().length() - (dotdot.length() + showMore.length()),
                getText().length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(showMoreTextColor),
                getText().length() - (dotdot.length() + showMore.length()),
                getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, BufferType.SPANNABLE);
    }

    private void showLessButton() {

        String text = getText() + dotdot + showLess;
        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                    }

                                    @Override
                                    public void onClick(@Nullable View view) {

                                        setMaxLines(showingLine);

                                        addShowMore();

                                        Log.d(TAG, "Item clicked: ");

                                    }
                                },
                text.length() - (dotdot.length() + showLess.length()),
                text.length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(showLessTextColor),
                text.length() - (dotdot.length() + showLess.length()),
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, BufferType.SPANNABLE);
    }


    /*
     * User added field
     * */

    /**
     * User can add minimum line number to show collapse text
     *
     * @param lineNumber int
     */
    public void setShowingLine(int lineNumber) {
        if (lineNumber == 0) {
            try {
                throw new Exception("Line Number cannot be 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        isCharEnable = false;

        showingLine = lineNumber;

        setMaxLines(showingLine);


        addShowMore();
    }

    /**
     * User can limit character limit of text
     *
     * @param character int
     */
    public void setShowingChar(int character) {
        if (character == 0) {
            try {
                throw new Exception("Character length cannot be 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        isCharEnable = true;
        this.showingChar = character;


        addShowMore();
    }

    /**
     * User can add their own  show more text
     *
     * @param text String
     */
    public void addShowMoreText(String text) {
        showMore = text;
    }

    /**
     * User can add their own show less text
     *
     * @param text String
     */
    public void addShowLessText(String text) {
        showLess = text;
    }

    /**
     * User Can add show more text color
     *
     * @param color Integer
     */
    public void setShowMoreColor(int color) {
        showMoreTextColor = color;
    }

    /**
     * User can add show less text color
     *
     * @param color Integer
     */
    public void setShowLessTextColor(int color) {
        showLessTextColor = color;
    }
}
