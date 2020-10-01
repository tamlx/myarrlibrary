package b.laixuantam.myaarlibrary.widgets.dialog.single_choise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by laixuantam on 1/13/18.
 */

public class MyCustomSingleChoise {
    public interface MyCustomSingleChoiseListener {
        void onItemSelected(int pos);
    }

    private Context context;
    private String[] arr_data;
    private String title;
    private int oldSelection;
    private MyCustomSingleChoiseListener listener;

    private boolean cancelable = false;

    public MyCustomSingleChoise(Context context, String title, int oldSelection, String[] arr_data, MyCustomSingleChoiseListener listener) {
        this.context = context;
        this.title = title;
        this.oldSelection = oldSelection;
        this.arr_data = arr_data;
        this.listener = listener;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setCancelable(cancelable);

        int selected = -1;

        if (oldSelection >= 0) {

            selected = oldSelection;
        }

        builder.setSingleChoiceItems(arr_data, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                listener.onItemSelected(position);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}

/**

 private String[] arr_gender = new String[2];

 private void initDataGender() {
 arr_gender[0] = "Nam";
 arr_gender[1] = "Nữ";
 }

 private int positionGenderSelected = -1;

 private void showDialogSelectCustomerGender() {
 MyCustomSingleChoise option = new MyCustomSingleChoise(getContext(), "Chọn giới tính", positionGenderSelected, arr_gender, new MyCustomSingleChoise.MyCustomSingleChoiseListener() {
@Override
public void onItemSelected(int pos) {
positionGenderSelected = pos;
ui.tvCustomerGender.setText(arr_gender[pos]);
}

});
 option.setCancelable(true);
 option.show();
 }


 */
