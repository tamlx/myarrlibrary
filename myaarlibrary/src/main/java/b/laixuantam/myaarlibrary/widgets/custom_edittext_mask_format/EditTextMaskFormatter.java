package b.laixuantam.myaarlibrary.widgets.custom_edittext_mask_format;

import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import b.laixuantam.myaarlibrary.helper.NumericFormater;

public class EditTextMaskFormatter implements TextWatcher {

    public enum MARK {
        PHONE,
        BIRTHDAY,
        CURRENCY,
        CUSTOM,
        NONE

    }

    private static final String PHONE_FOMAT = "9999 999 9999";

    private static final String BIRDAY_FORMAT = "99/99/9999";

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    private static final char SPACE = ' ';

    private String mask;
    private final EditText maskedField;
    private final char maskCharacter;

    private boolean editTextChange;
    private int newIndex;
    private String textBefore;
    private int selectionBefore;
    private int passwordMask;
    private boolean isDefaultEditText = false;
    private boolean isCurrencyFomat = false;
    boolean mEditing = false;

    public EditTextMaskFormatter(MARK mask, EditText maskedField) {
        this(mask, maskedField, SPACE, null);
    }

    public EditTextMaskFormatter(MARK mask, EditText maskedField, char maskCharacter, String markFormat) {

        switch (mask) {
            case PHONE:
                isCurrencyFomat = false;
                if (!TextUtils.isEmpty(markFormat)) {
                    this.mask = markFormat;
                } else {
                    this.mask = PHONE_FOMAT;
                }
                this.isDefaultEditText = false;
                break;

            case BIRTHDAY:
                isCurrencyFomat = false;
                if (!TextUtils.isEmpty(markFormat)) {
                    this.mask = markFormat;
                } else {
                    this.mask = BIRDAY_FORMAT;
                }

                this.isDefaultEditText = false;
                break;

            case CURRENCY:
                isCurrencyFomat = true;

                this.isDefaultEditText = false;

                break;

            case CUSTOM:
                isCurrencyFomat = false;
                if (!TextUtils.isEmpty(markFormat)) {
                    this.mask = markFormat;
                    this.isDefaultEditText = false;
                } else {
                    this.isDefaultEditText = true;
                }


                break;

            default:
                isCurrencyFomat = false;
                this.isDefaultEditText = true;
                break;
        }

        this.maskedField = maskedField;
        this.maskCharacter = maskCharacter;
//        this.passwordMask = getPasswordMask(maskedField);
        if (!isDefaultEditText && !isCurrencyFomat)
            setInputTypeBasedOnMask();
    }

    private int getPasswordMask(EditText maskedField) {
        int inputType = maskedField.getInputType();
        int maskedFieldPasswordMask = (inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD
                | inputType & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            maskedFieldPasswordMask |= inputType & InputType.TYPE_NUMBER_VARIATION_PASSWORD;
            maskedFieldPasswordMask |= inputType & InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
        }

        return maskedFieldPasswordMask;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int index, int toBeReplacedCount, int addedCount) {

        if (isCurrencyFomat) {
            return;
        }
        if (!isDefaultEditText) {
            textBefore = s.toString();
            selectionBefore = maskedField.getSelectionEnd();
        }

    }


    @Override
    public void onTextChanged(CharSequence s, int index, int replacedCount, int addedCount) {
        if (!isDefaultEditText) {
            if (isCurrencyFomat) {

            } else {
                if (editTextChange) {
                    maskedField.setSelection(newIndex);
                    editTextChange = false;
                    return;
                }

                try {
                    String appliedMaskString = applyMask(s.toString());

                    if (!appliedMaskString.equals(s.toString())) {
                        editTextChange = true;
                        newIndex = countNewIndex(addedCount, appliedMaskString);
                        maskedField.setText(appliedMaskString);
                    }

                } catch (InvalidTextException ie) {
                    editTextChange = true;
                    newIndex = selectionBefore;
                    maskedField.setText(textBefore);
                }
            }
        }
    }

    private int countNewIndex(int addedCount, String appliedMaskString) {
        if (appliedMaskString.length() == 0) {
            return 0;
        }

        if (addedCount < 1) {
            return newIndexForRemovingCharacters(appliedMaskString);
        }

        return newIndexForAddingCharacters(appliedMaskString);
    }

    private int newIndexForRemovingCharacters(String appliedMaskString) {
        if (selectionBefore > appliedMaskString.length()) {
            selectionBefore = appliedMaskString.length();
        } else {
            selectionBefore = selectionBefore - 1;
        }

        if (selectionBefore < 0) {
            return 0;
        }

        if (selectionBefore - 1 >= 0
                && appliedMaskString.charAt(selectionBefore - 1) == maskCharacter) {
            return selectionBefore - 1;
        }

        return selectionBefore;
    }

    private int newIndexForAddingCharacters(String appliedMaskString) {
        if (selectionBefore >= appliedMaskString.length()) {
            return appliedMaskString.length();
        }

        if (appliedMaskString.charAt(selectionBefore) == maskCharacter) {
            return selectionBefore + 2;
        }

        return selectionBefore + 1;
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (isCurrencyFomat) {
            if (!mEditing) {
                mEditing = true;

                String digits = s.toString().replaceAll("\\D", "");
                try {
                    String formatted = NumericFormater.formatMoney(Long.parseLong(digits));
                    s.replace(0, s.length(), formatted);
                    maskedField.setText(formatted);
                    maskedField.setSelection(formatted.length());
                } catch (NumberFormatException nfe) {
                    s.clear();
                }
                mEditing = false;
            }
        } else {
            if (!isDefaultEditText) {
                if (editTextChange) {
                    maskedField.setSelection(newIndex);

                }
                setInputTypeBasedOnMask();
            }
        }
        if (maskedField.length() > 0) {
            maskedField.setError(null);
        }
    }

    private String applyMask(String newValue) throws InvalidTextException {
        String newValueWithoutSpaces = newValue.replaceAll(String.valueOf(maskCharacter), "");
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (char c : newValueWithoutSpaces.toCharArray()) {
            if (index >= mask.length()) {
                throw new InvalidTextException();
            }
            while (mask.charAt(index) == maskCharacter) {
                sb.append(maskCharacter);
                index++;
            }
            sb.append(applyMaskToChar(c, index));
            index++;
        }

        return sb.toString();
    }

    private char applyMaskToChar(char c, int maskIndex) throws InvalidTextException {
        return CharTransforms.transformChar(c, mask.charAt(maskIndex));
    }

    private void setInputTypeBasedOnMask() {
        int selection = maskedField.getSelectionEnd();
        if (selection >= mask.length()) {
            return;
        }

        char maskChar = getFirstNotWhiteCharFromMask();
        if (maskChar == maskCharacter) {
            return;
        }

        maskedField.setInputType(passwordMask | CharInputType.getKeyboardTypeForNextChar(maskChar));
    }

    private char getFirstNotWhiteCharFromMask() {
        int maskIndex = maskedField.getSelectionEnd();
        while (maskIndex < mask.length() && mask.charAt(maskIndex) == maskCharacter) {
            maskIndex++;
        }
        return mask.charAt(maskIndex);
    }

    public String getRawTextValue() {

        return maskedField.getText().toString().replaceAll(String.valueOf(maskCharacter), "");
    }


}
