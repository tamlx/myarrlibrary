package b.laixuantam.myaarlibrary.widgets.dialog.bottom_sheet;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import org.michaelbel.bottomsheetdialog.BottomSheet;

/**
 * Created by laixuantam on 1/13/18.
 */

public class MyBottomSheet
{

    public interface MyBottomSheetListener
    {
        void onItemSelected(int pos);
    }

    private Context context;
    private int[] items1;
    private int[] icons1;
    private String title;

    private boolean darkTheme;
    private boolean isShowGrid;
    private boolean isShowDivider;
    private boolean isShowTitleMultiLine;
    private MyBottomSheetListener listener;

    public MyBottomSheet(Context context, int[] items1, int[] icons1, String title, MyBottomSheetListener listener)
    {
        this.context = context;
        this.items1 = items1;
        this.icons1 = icons1;
        this.title = title;
        this.listener = listener;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public void setShowGrid(boolean showGrid) {
        isShowGrid = showGrid;
    }

    public void setShowDivider(boolean showDivider) {
        isShowDivider = showDivider;
    }

    public void setShowTitleMultiLine(boolean showTitleMultiLine) {
        isShowTitleMultiLine = showTitleMultiLine;
    }

    public void showMyBottomSheet()
    {
        BottomSheet.Builder builder = new BottomSheet.Builder(context);

        if (!TextUtils.isEmpty(title))
        {
            builder.setTitle(title);
        }

        if (isShowGrid)
        {
            builder.setContentType(BottomSheet.GRID);
        }

        builder.setTitleMultiline(isShowTitleMultiLine);

        builder.setDividers(isShowDivider);
        builder.setFullWidth(true);
//        builder.setItemTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

        if (items1 != null && icons1 != null) {
//            builder.setItems(items1, icons1, (dialogInterface, i) ->
//                    listener.onItemSelected(i));

            builder.setItems(items1, icons1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onItemSelected(i);
                }
            });


        } else {
//            builder.setItems(items1, (dialogInterface, i) -> listener.onItemSelected(i));
            builder.setItems(items1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onItemSelected(i);
                }
            });

        }

        builder.show();

    }
    /*
    https://github.com/michaelbel/BottomSheet?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6623
    int[] items = new int[]{
                R.string.gmail,
                R.string.hangout,
                R.string.google_plus,
                R.string.mail,
                R.string.message,
                R.string.copy,
                R.string.facebook,
                R.string.twitter
        };

        int[] icons = new int[] {
                R.drawable.ic_gmail,
                R.drawable.ic_hangouts,
                R.drawable.ic_google_plus,
                R.drawable.ic_mail,
                R.drawable.ic_message,
                R.drawable.ic_copy_48,
                R.drawable.ic_facebook,
                R.drawable.ic_twitter
        };
     */
}
